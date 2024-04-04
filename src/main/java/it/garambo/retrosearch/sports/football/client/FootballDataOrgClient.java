package it.garambo.retrosearch.sports.football.client;

import static java.util.Objects.isNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.garambo.retrosearch.http.HttpService;
import it.garambo.retrosearch.sports.football.model.FootballDataResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value = "retrosearch.sports.football.enable", havingValue = "true")
public class FootballDataOrgClient {

  private final String API_URL = "https://api.football-data.org/v4/matches";

  @Value("${retrosearch.sports.football.api.key:}")
  private String apiKey;

  @Autowired HttpService httpService;

  public FootballDataResponse fetchFootballData(LocalDate dateFrom, LocalDate dateTo)
      throws IOException, URISyntaxException {
    URI apiUri = new URI(API_URL);
    BasicHeader apiKeyHeader = new BasicHeader("X-Auth-Token", apiKey);

    Map<String, String> params = new HashMap<>();
    if (!isNull(dateFrom) && !isNull(dateTo)) {
      String formattedDateFrom = dateFrom.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
      String formattedDateTo = dateTo.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
      params.put("dateFrom", formattedDateFrom);
      params.put("dateTo", formattedDateTo);
    }

    String response = httpService.get(apiUri, params, List.of(apiKeyHeader));
    return new ObjectMapper().readValue(response, FootballDataResponse.class);
  }
}
