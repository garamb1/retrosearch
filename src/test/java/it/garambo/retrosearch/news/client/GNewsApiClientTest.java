package it.garambo.retrosearch.news.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import it.garambo.retrosearch.PrimaryTestConfiguration;
import it.garambo.retrosearch.http.HttpService;
import it.garambo.retrosearch.news.model.GNewsApiResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = PrimaryTestConfiguration.class)
class GNewsApiClientTest {

  @Mock private HttpService httpService;

  @InjectMocks GNewsApiClient client;

  @BeforeEach
  void setup() {
    ReflectionTestUtils.setField(client, "apiKey", "testKey");
  }

  @Test
  void testFetchNews(@Value("classpath:news/response.json") Resource responseJson)
      throws IOException, URISyntaxException {
    URI uri = new URI("https://gnews.io/api/v4/top-headlines");

    Map<String, String> params =
        Map.of(
            "category", "general",
            "max", "10",
            "lang", "it",
            "country", "it",
            "apikey", "testKey");

    when(httpService.get(eq(uri), eq(params)))
        .thenReturn(responseJson.getContentAsString(StandardCharsets.UTF_8));

    GNewsApiResponse response = client.fetchNews("it", "it");
    assertNotNull(response);
    assertEquals(54904, response.totalArticles());
    assertNotNull(response.articles());
    assertEquals(1, response.articles().size());
  }
}
