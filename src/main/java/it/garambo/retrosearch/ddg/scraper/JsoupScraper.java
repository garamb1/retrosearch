package it.garambo.retrosearch.ddg.scraper;

import static it.garambo.retrosearch.ddg.util.DDGScraperConstants.*;

import it.garambo.retrosearch.ddg.model.ResultEntry;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.UrlValidator;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Slf4j
@Service
public class JsoupScraper implements Scraper {

  private final UrlValidator urlValidator = new UrlValidator();

  @Override
  public List<ResultEntry> scrapeResults(String html) {
    Document document = getDocument(html);
    log.debug("Got document: {}", document.title());

    List<ResultEntry> resultEntries = new ArrayList<>();

    Elements resultLinks = getResultElements(document);
    for (Element resultElement : resultLinks) {
      try {
        resultEntries.add(getResultEntry(resultElement));
      } catch (Exception e) {
        log.debug("Invalid entry, skipping {} ", resultElement);
      }
    }

    return resultEntries;
  }

  @Override
  public Document getDocument(String html) {
    return Jsoup.parse(html);
  }

  private ResultEntry getResultEntry(Element resultElement) throws Exception {
    String title = getEntryTitle(resultElement);
    URI uri = getEntryUri(resultElement);
    String description = getEntryDescription(resultElement);

    if (ObjectUtils.isEmpty(title) || ObjectUtils.isEmpty(description)) {
      throw new Exception("Invalid Entry");
    }

    return ResultEntry.builder().title(title).uri(uri).description(description).build();
  }

  public Elements getResultElements(Document document) {
    return document.select(RESULT_LINKS_CLASS_SELECTOR);
  }

  @Override
  public String getEntryTitle(Element resultElement) {
    String title = getTextFromQuery(resultElement, RESULT_TITLE_CLASS_SELECTOR);
    log.debug("{} title: {} ", resultElement, title);
    return title;
  }

  @Override
  public URI getEntryUri(Element resultElement) throws MalformedURLException {
    String url = getAttributeFromQuery(resultElement, RESULT_TITLE_CLASS_SELECTOR, "href");

    if (url == null) {
      return null;
    }

    if (url.startsWith(HREF_PREFIX)) {
      return extractHrefURI(url);
    }

    if (!urlValidator.isValid(url)) {
      throw new MalformedURLException("Invalid url for entry, url: " + url);
    }

    log.debug("{} url: {} ", resultElement, url);
    return URI.create(url);
  }

  @Override
  public String getEntryDescription(Element resultElement) {
    String description = getTextFromQuery(resultElement, RESULT_DESCRIPTION_CLASS_SELECTOR);
    log.debug("{} description: {} ", resultElement, description);
    return description;
  }

  private String getTextFromQuery(Element element, String query) {
    Element queryResult = element.select(query).first();

    if (queryResult == null) {
      return null;
    }

    return queryResult.text();
  }

  private String getAttributeFromQuery(Element element, String query, String attribute) {
    Element queryResult = element.select(query).first();

    if (queryResult == null) {
      return null;
    }

    return queryResult.attr(attribute);
  }

  private URI extractHrefURI(String ddgRedirectUrl) throws MalformedURLException {
    String validUri = "http:" + ddgRedirectUrl;
    URI completeURI = URI.create(validUri);
    Optional<NameValuePair> uriParams =
        new URIBuilder(completeURI)
            .getQueryParams().stream()
                .filter(nameValuePair -> REDIRECT_URL_PARAM.equals(nameValuePair.getName()))
                .toList()
                .stream()
                .findFirst();

    Optional<String> hrefUri = uriParams.map(NameValuePair::getValue);

    if (hrefUri.isEmpty() || !urlValidator.isValid(hrefUri.get())) {
      throw new MalformedURLException("Invalid url for entry, url: " + hrefUri);
    }

    return URI.create(hrefUri.get());
  }
}
