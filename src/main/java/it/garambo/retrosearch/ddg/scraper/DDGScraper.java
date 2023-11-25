package it.garambo.retrosearch.ddg.scraper;

import it.garambo.retrosearch.ddg.model.ResultEntry;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public interface DDGScraper {
  List<ResultEntry> scrapeResults(String html);

  Document getDocument(String html);

  String getEntryTitle(Element resultElement);

  URI getEntryUri(Element resultElement) throws MalformedURLException;

  String getEntryDescription(Element resultElement);
}
