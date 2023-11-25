package it.garambo.retrosearch.browse.parser;

import it.garambo.retrosearch.browse.model.ParsedHtmlPage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import net.dankito.readability4j.Article;
import org.jsoup.nodes.Document;

public interface ModernHtmlParser {
  ParsedHtmlPage parsePage(URI uri) throws IOException, URISyntaxException;

  String cleanPage(String articleContent, boolean replacePageLinks);

  Article parseArticle(URI uri, Document document);
}
