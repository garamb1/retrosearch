package it.garambo.retrosearch.http;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicStatusLine;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HttpServiceImplTest {

  private HttpService httpService;
  private CloseableHttpClient httpClient;

  @BeforeEach
  void setup() {
    httpClient = mock(CloseableHttpClient.class);
    httpService = new HttpServiceImpl(httpClient);
  }

  @Test
  void testGet() throws IOException, URISyntaxException {
    CloseableHttpResponse response = getMockSuccessfulResponse("Success");
    when(httpClient.execute(any())).thenReturn(response);

    assertEquals("Success", httpService.get(new URI("http://test.com")));
  }

  @Test
  void testPost() throws IOException, URISyntaxException {
    CloseableHttpResponse response = getMockSuccessfulResponse("Success");
    when(httpClient.execute(any(HttpPost.class))).thenReturn(response);

    assertEquals("Success", httpService.post(new URI("http://test.com"), "post-body"));
  }

  @Test
  void testPostFormData() throws IOException, URISyntaxException {
    CloseableHttpResponse response = getMockSuccessfulResponse("Success");
    when(httpClient.execute(any(HttpPost.class))).thenReturn(response);

    Map<String, String> formData = Map.of("key", "value");
    assertEquals("Success", httpService.post(new URI("http://test.com"), formData));
  }

  private static @NotNull CloseableHttpResponse getMockSuccessfulResponse(String content)
      throws IOException {
    CloseableHttpResponse response = mock(CloseableHttpResponse.class);
    StatusLine statusLine = new BasicStatusLine(new ProtocolVersion("http", 1, 1), 200, "success");
    when(response.getStatusLine()).thenReturn(statusLine);
    HttpEntity entity = mock(HttpEntity.class);
    when(entity.getContent()).thenReturn(new ByteArrayInputStream(content.getBytes()));
    when(response.getEntity()).thenReturn(entity);
    return response;
  }
}
