package it.garambo.retrosearch.browse.parser;

import it.garambo.retrosearch.browse.model.ParsedHtmlPage;
import it.garambo.retrosearch.http.HttpService;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import net.dankito.readability4j.Article;
import net.dankito.readability4j.Readability4J;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Safelist;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModernHtmlParserImpl implements ModernHtmlParser {

  @Autowired private HttpService httpService;

  @Override
  public ParsedHtmlPage parsePage(URI uri) throws IOException, URISyntaxException {
    Document document = Jsoup.parse(httpService.get(uri));
    Article article = parseArticle(uri, document);
    String title = article.getTitle();

    return ParsedHtmlPage.builder()
        .title(title)
        .htmlContent(cleanPage(article.getContent(), true))
        .build();
  }

  public String cleanPage(String articleContent, boolean replacePageLinks) {
    String safePage =
        Jsoup.clean(
            Objects.requireNonNull(articleContent),
            Safelist.basic()
                .addProtocols("a", "href", "http", "https")
                .preserveRelativeLinks(true));

    if (!replacePageLinks) {
      return safePage;
    }

    return replacePageLinks(safePage).html();
  }

  public Element replacePageLinks(String articleContent) {
    Element document = Jsoup.parse(articleContent);
    Elements links = Objects.requireNonNull(document).select("a");
    for (Element element : links) {
      String originalHref = element.attr("abs:href");
      element.attr("href", "/browse?url=" + originalHref);
    }
    return document;
  }

  @Override
  public Article parseArticle(URI uri, Document document) {
    return new Readability4J(uri.toASCIIString(), document).parse();
  }
}
