package it.garambo.retrosearch.news.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.garambo.retrosearch.PrimaryTestConfiguration;
import it.garambo.retrosearch.news.model.Article;
import it.garambo.retrosearch.news.model.GNewsApiResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

@SpringBootTest(classes = PrimaryTestConfiguration.class)
@ExtendWith(MockitoExtension.class)
class InMemoryNewsRepositoryTest {

  @Autowired NewsRepository newsRepository;

  @Test
  void testUpdate(@Value("classpath:news/response.json") Resource responseJson)
      throws IOException, URISyntaxException {

    String content = responseJson.getContentAsString(StandardCharsets.UTF_8);
    GNewsApiResponse response = new ObjectMapper().readValue(content, GNewsApiResponse.class);
    assertNotNull(response);

    Map<String, List<Article>> articles = Map.of("Example", response.articles());

    newsRepository.updateAll(articles);

    assertNotNull(newsRepository.getUpdatedAt());
    assertTrue(newsRepository.isCountrySupported("Example"));
    assertNotNull(newsRepository.getArticlesByCountry("Example"));
    assertEquals(1, newsRepository.getArticlesByCountry("Example").size());
    assertFalse(newsRepository.isCountrySupported("Wrong"));
  }
}
