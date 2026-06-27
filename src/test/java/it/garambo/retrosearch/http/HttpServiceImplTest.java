package it.garambo.retrosearch.http;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.StringEntity;
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
    when(httpClient.execute(any(ClassicHttpRequest.class), any(HttpClientResponseHandler.class)))
        .thenAnswer(
            invocation -> {
              HttpClientResponseHandler<String> handler = invocation.getArgument(1);
              return handler.handleResponse(getMockSuccessfulResponse("Success"));
            });

    ParsedHttpResponse expected =
        ParsedHttpResponse.builder()
            .content("Success")
            .originalUri(new URI("http://test.com"))
            .build();

    ParsedHttpResponse result = httpService.get(new URI("http://test.com"));

    assertEquals(expected.content(), result.content());
    assertEquals(expected.originalUri(), result.originalUri());
  }

  // TODO: update test
  //  @Test
  //  void testPost() throws IOException, URISyntaxException {
  //    CloseableHttpResponse response = getMockSuccessfulResponse("Success");
  //    when(httpClient.execute(any(HttpPost.class))).thenReturn(response);
  //
  //    assertEquals("Success", httpService.post(new URI("http://test.com"), "post-body"));
  //  }

  // TODO: update test
  //  @Test
  //  void testPostFormData() throws IOException, URISyntaxException {
  //    CloseableHttpResponse response = getMockSuccessfulResponse("Success");
  //    when(httpClient.execute(any(HttpPost.class))).thenReturn(response);
  //
  //    Map<String, String> formData = Map.of("key", "value");
  //    assertEquals("Success", httpService.post(new URI("http://test.com"), formData));
  //  }

  // TODO: add more test cases

  private static @NotNull CloseableHttpResponse getMockSuccessfulResponse(String content) {
    CloseableHttpResponse response = mock(CloseableHttpResponse.class);
    when(response.getCode()).thenReturn(HttpStatus.SC_SUCCESS);
    when(response.getCode()).thenReturn(200);
    when(response.getEntity()).thenReturn(new StringEntity(content));
    return response;
  }
}
