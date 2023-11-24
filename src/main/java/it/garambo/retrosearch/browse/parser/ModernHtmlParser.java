package it.garambo.retrosearch.browse.parser;

import it.garambo.retrosearch.browse.model.ParsedHtmlPage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public interface ModernHtmlParser {

  ParsedHtmlPage parsePage(URI uri) throws IOException, URISyntaxException;
}
