package it.garambo.retrosearch.sports.football.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.garambo.retrosearch.PrimaryTestConfiguration;
import it.garambo.retrosearch.sports.football.model.FootballDataResponse;
import it.garambo.retrosearch.sports.football.model.match.Match;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

@SpringBootTest(classes = PrimaryTestConfiguration.class)
@ExtendWith(MockitoExtension.class)
class InMemoryFootballRepositoryTest {

  @Autowired FootballRepository footballRepository;

  @Test
  void testUpdate(@Value("classpath:sports/football/response.json") Resource responseJson)
      throws IOException {
    String content = responseJson.getContentAsString(StandardCharsets.UTF_8);
    FootballDataResponse response =
        new ObjectMapper().readValue(content, FootballDataResponse.class);
    assertNotNull(response);
    footballRepository.updateAll(response.matches());
    assertNotNull(footballRepository.getUpdatedAt());

    Map<String, Set<Match>> matches = footballRepository.getAllMatches();
    assertEquals(7, matches.keySet().size());
    assertTrue(matches.containsKey("Italy"));

    Set<Match> italianMatches = footballRepository.getAllMatchesByArea("Italy");
    assertEquals(5, italianMatches.size());
  }
}
