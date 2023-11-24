package it.garambo.retrosearch.ddg.client;

import it.garambo.retrosearch.ddg.model.SearchResults;
import java.io.IOException;
import java.net.URISyntaxException;

public interface DDGClient {

  SearchResults search(String query) throws IOException, URISyntaxException;

  SearchResults search(String query, String locale) throws IOException, URISyntaxException;
}
