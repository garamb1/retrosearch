package it.garambo.retrosearch.sports.football.client;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import it.garambo.retrosearch.http.HttpService;
import it.garambo.retrosearch.http.ParsedHttpResponse;
import it.garambo.retrosearch.sports.football.model.FootballDataResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FootballDataOrgClientTest {
  @Mock HttpService httpService;

  FootballDataOrgClient client;

  @BeforeEach
  void setup() {
    client =
        new FootballDataOrgClient(
            "https://api.football-data.org/v4/matches", "testKey", httpService);
  }

  @Test
  void testFetchFootballData() throws IOException, URISyntaxException {
    URI uri = new URI("https://api.football-data.org/v4/matches");
    when(httpService.get(eq(uri), eq(Map.of()), any()))
        .thenReturn(
            ParsedHttpResponse.builder()
                .content("{" + "\"resultSet\":" + "{\"kind\" : \"no-dates\"}" + "}")
                .build());

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
        .thenReturn(
            ParsedHttpResponse.builder()
                .content("{" + "\"resultSet\":" + "{\"kind\" : \"with-dates\"}" + "}")
                .build());
    LocalDate from = LocalDate.of(2024, 1, 1);
    LocalDate to = LocalDate.of(2024, 1, 2);

    FootballDataResponse noDatesResponse = client.fetchFootballData(from, to);
    assertNotNull(noDatesResponse);
    assertNotNull(noDatesResponse.resultSet());
    assertTrue(noDatesResponse.resultSet().containsKey("kind"));
    assertEquals("with-dates", noDatesResponse.resultSet().get("kind"));
  }

  @Test
  void testFetchFootballDataFailed() throws IOException, URISyntaxException {
    URI uri = new URI("https://api.football-data.org/v4/matches");
    Map<String, String> testDatesMap = Map.of("dateFrom", "2024-01-01", "dateTo", "2024-01-02");

    doThrow(IOException.class).when(httpService).get(eq(uri), eq(testDatesMap), any());

    LocalDate from = LocalDate.of(2024, 1, 1);
    LocalDate to = LocalDate.of(2024, 1, 2);
    assertThrows(IOException.class, () -> client.fetchFootballData(from, to));
  }
}
