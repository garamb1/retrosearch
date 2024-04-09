package it.garambo.retrosearch.news.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.garambo.retrosearch.http.HttpService;
import it.garambo.retrosearch.news.model.GNewsApiResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value = "retrosearch.news.enable", havingValue = "true")
public class GNewsApiClient {

  private final String API_URL = "https://gnews.io/api/v4/top-headlines";

  @Value("${retrosearch.news.api.key:}")
  private String apiKey;

  @Autowired HttpService httpService;

  public GNewsApiResponse fetchNews(String language, String country)
      throws URISyntaxException, IOException {
    URI apiUri = new URI(API_URL);
    Map<String, String> params =
        Map.of(
            "category", "general",
            "max", "10",
            "lang", language,
            "country", country,
            "apikey", apiKey);

    String response = httpService.get(apiUri, params);
    return new ObjectMapper().readValue(response, GNewsApiResponse.class);
  }
}
