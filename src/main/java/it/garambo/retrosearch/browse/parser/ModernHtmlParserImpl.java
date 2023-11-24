package it.garambo.retrosearch.browse.parser;

import it.garambo.retrosearch.browse.model.ParsedHtmlPage;
import it.garambo.retrosearch.http.HttpService;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import net.dankito.readability4j.Article;
import net.dankito.readability4j.Readability4J;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModernHtmlParserImpl implements ModernHtmlParser {

  @Autowired private HttpService httpService;

  @Override
  public ParsedHtmlPage parsePage(URI uri) throws IOException, URISyntaxException {
    String document = httpService.get(uri);
    Article article = new Readability4J(uri.toASCIIString(), document).parse();

    return ParsedHtmlPage.builder()
        .title(article.getTitle())
        .content(article.getTextContent())
        .build();
  }
}
