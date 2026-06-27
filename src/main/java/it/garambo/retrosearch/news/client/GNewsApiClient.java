package it.garambo.retrosearch.news.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.garambo.retrosearch.http.HttpService;
import it.garambo.retrosearch.http.ParsedHttpResponse;
import it.garambo.retrosearch.news.model.GNewsApiResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GNewsApiClient {

  private final String apiUrl;
  private final String apiKey;
  private final HttpService httpService;

  public GNewsApiResponse fetchNews(String language, String country)
      throws URISyntaxException, IOException {
    URI apiUri = new URI(apiUrl);
    Map<String, String> params =
        Map.of(
            "category", "general",
            "max", "10",
            "lang", language,
            "country", country,
            "apikey", apiKey);

    ParsedHttpResponse response = httpService.get(apiUri, params);
    return new ObjectMapper().readValue(response.content(), GNewsApiResponse.class);
  }
}
