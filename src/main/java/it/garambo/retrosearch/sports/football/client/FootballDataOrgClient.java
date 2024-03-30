package it.garambo.retrosearch.sports.football.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.garambo.retrosearch.http.HttpService;
import it.garambo.retrosearch.sports.football.model.FootballDataResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value = "retrosearch.sports.football.enable", havingValue = "true")
public class FootballDataOrgClient {

  private final String API_URL = "https://api.football-data.org/v4/matches/";

  @Value("${retrosearch.sports.football.api.key:}")
  private String apiKey;

  @Autowired HttpService httpService;

  public FootballDataResponse fetchFootballData() throws IOException, URISyntaxException {
    URI apiUri = new URI(API_URL);
    BasicHeader apiKeyHeader = new BasicHeader("X-Auth-Token", apiKey);

    String response = httpService.get(apiUri, List.of(apiKeyHeader));
    return new ObjectMapper().readValue(response, FootballDataResponse.class);
  }
}
