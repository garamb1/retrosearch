package it.garambo.retrosearch.ddg.client;

import it.garambo.retrosearch.ddg.model.ResultEntry;
import it.garambo.retrosearch.ddg.model.SearchResults;
import it.garambo.retrosearch.ddg.scraper.DDGScraper;
import it.garambo.retrosearch.ddg.util.DDGConstants;
import it.garambo.retrosearch.http.HttpService;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DDGClientImpl implements DDGClient {

  @Autowired private HttpService httpService;

  @Autowired private DDGScraper ddgScraper;

  @Override
  public SearchResults search(String query) throws IOException, URISyntaxException {
    return search(query, "");
  }

  @Override
  public SearchResults search(String query, String locale) throws IOException, URISyntaxException {
    Map<String, String> searchParams =
        Map.of(
            DDGConstants.QUERY_PARAM_NAME,
            query,
            DDGConstants.REGION_PARAM_NAME,
            getDDGLocale(locale));

    String resultPage = httpService.get(URI.create(DDGConstants.BASE_URL), searchParams);
    List<ResultEntry> resultEntryList = ddgScraper.scrapeResults(resultPage);

    return SearchResults.builder()
        .query(query)
        .locale(locale)
        .resultEntries(resultEntryList)
        .build();
  }

  private String getDDGLocale(String locale) {
    return locale.toLowerCase().replace("_", "-");
  }
}
