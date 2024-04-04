package it.garambo.retrosearch.sports.football.client;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import it.garambo.retrosearch.http.HttpService;
import it.garambo.retrosearch.sports.football.model.FootballDataResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FootballDataOrgClientTest {
  @Mock private HttpService httpService;

  @InjectMocks FootballDataOrgClient client;

  @Test
  void testFetchFootballData() throws IOException, URISyntaxException {
    URI uri = new URI("https://api.football-data.org/v4/matches");
    when(httpService.get(eq(uri), eq(Map.of()), any()))
        .thenReturn("{" + "\"resultSet\":" + "{\"kind\" : \"no-dates\"}" + "}");

    FootballDataResponse noDatesResponse = client.fetchFootballData(null, null);
    assertNotNull(noDatesResponse);
    assertNotNull(noDatesResponse.resultSet());
    assertTrue(noDatesResponse.resultSet().containsKey("kind"));
    assertEquals("no-dates", noDatesResponse.resultSet().get("kind"));
  }

  @Test
  void testFetchFootballData_withDates() throws IOException, URISyntaxException {
    URI uri = new URI("https://api.football-data.org/v4/matches");

    Map<String, String> testDatesMap = Map.of("dateFrom", "2024-01-01", "dateTo", "2024-01-02");

    when(httpService.get(eq(uri), eq(testDatesMap), any()))
        .thenReturn("{" + "\"resultSet\":" + "{\"kind\" : \"with-dates\"}" + "}");

    LocalDate from = LocalDate.of(2024, 1, 1);
    LocalDate to = LocalDate.of(2024, 1, 2);

    FootballDataResponse noDatesResponse = client.fetchFootballData(from, to);
    assertNotNull(noDatesResponse);
    assertNotNull(noDatesResponse.resultSet());
    assertTrue(noDatesResponse.resultSet().containsKey("kind"));
    assertEquals("with-dates", noDatesResponse.resultSet().get("kind"));
  }
}
