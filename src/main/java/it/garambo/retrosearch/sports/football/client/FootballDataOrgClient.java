package it.garambo.retrosearch.sports.football.client;

import static java.util.Objects.isNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.garambo.retrosearch.http.HttpService;
import it.garambo.retrosearch.http.ParsedHttpResponse;
import it.garambo.retrosearch.sports.football.model.FootballDataResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.message.BasicHeader;

@AllArgsConstructor
public class FootballDataOrgClient {

  private final String apiUrl;
  private final String apiKey;
  private final HttpService httpService;

  public FootballDataResponse fetchFootballData(LocalDate dateFrom, LocalDate dateTo)
      throws IOException, URISyntaxException {
    URI apiUri = new URI(apiUrl);
    Header apiKeyHeader = new BasicHeader("X-Auth-Token", apiKey);

    Map<String, String> params = new HashMap<>();
    if (!isNull(dateFrom) && !isNull(dateTo)) {
      String formattedDateFrom = dateFrom.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
      String formattedDateTo = dateTo.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
      params.put("dateFrom", formattedDateFrom);
      params.put("dateTo", formattedDateTo);
    }

    ParsedHttpResponse response = httpService.get(apiUri, params, List.of(apiKeyHeader));
    return new ObjectMapper().readValue(response.content(), FootballDataResponse.class);
  }
}
