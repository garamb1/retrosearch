package it.garambo.retrosearch.browse.parser;

import it.garambo.retrosearch.browse.model.ParsedHtmlPage;
import it.garambo.retrosearch.configuration.ApplicationSettings;
import it.garambo.retrosearch.http.HttpService;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import net.dankito.readability4j.Article;
import net.dankito.readability4j.Readability4J;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Safelist;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ModernHtmlParserImpl implements ModernHtmlParser {

  @Autowired private HttpService httpService;
  @Autowired private ApplicationSettings settings;

  private static final Map<String, String> tagReplacements = Map.of("span", "var");

  @Override
  public ParsedHtmlPage parsePage(URI uri) throws IOException, URISyntaxException {
    Document document = Jsoup.parse(httpService.get(uri));
    Article article = parseArticle(uri, document);
    String title = article.getTitle();
    List<String> navigation = getPageNavigation(document.body());

    return ParsedHtmlPage.builder()
        .title(title)
        .navigation(navigation)
        .htmlContent(cleanPage(article.getContent(), true))
        .build();
  }

  @Override
  public Article parseArticle(URI uri, Document document) {
    return new Readability4J(uri.toASCIIString(), document).parse();
  }

  @Override
  public String cleanPage(String articleContent, boolean replacePageLinks) {

    String safePage =
        Jsoup.clean(
            Objects.requireNonNull(articleContent),
            Safelist.basic()
                .addTags(settings.getHtmlVersion().getAllowedTags())
                .addProtocols("a", "href", "http", "https"));

    String safePageWithSupportedTags = removeUnsupportedTags(safePage).html();

    if (!replacePageLinks) {
      return safePageWithSupportedTags;
    }

    return replaceLinks(safePageWithSupportedTags).html();
  }

  protected Elements replaceLinks(String articleContent) {
    Element document = Jsoup.parse(articleContent);
    Elements links = Objects.requireNonNull(document).select("a");
    for (Element element : links) {
      element.attr("href", "/browse?url=" + element.attr("abs:href"));
    }
    return document.select("body");
  }

  protected Elements removeUnsupportedTags(String articleContent) {
    Element document = Jsoup.parse(articleContent);
    Set<String> unsupported = tagReplacements.keySet();
    for (String unsupportedTag : unsupported) {
      document.select(unsupportedTag).tagName(tagReplacements.get(unsupportedTag));
    }
    return document.select("body");
  }

  protected List<String> getPageNavigation(Element documentBody) {
    Elements nav = documentBody.select("nav > a");
    List<String> navigationLinks = new ArrayList<>();
    for (Element link : nav) {
      if (link.hasText()) {
        navigationLinks.add(cleanPage(link.outerHtml(), true));
      }
    }
    return navigationLinks;
  }
}
