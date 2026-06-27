package it.garambo.retrosearch.http;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HttpServiceImplTest {

  private HttpService httpService;
  private CloseableHttpClient httpClient;

  private static final String SUCCESSFUL_RESPONSE_CONTENT = "Success";
  private static final String TEST_URI = "http://test.com";

  @BeforeEach
  void setup() {
    httpClient = mock(CloseableHttpClient.class);
    httpService = new HttpServiceImpl(httpClient);
  }

  @Test
  void testGet() throws IOException, URISyntaxException {
    try (MockedStatic<EntityUtils> entityUtils = mockStatic(EntityUtils.class)) {
      entityUtils.when(() -> EntityUtils.toString(any())).thenReturn(SUCCESSFUL_RESPONSE_CONTENT);
      stubExecute(getSuccessfulResponse());

      ParsedHttpResponse result = httpService.get(new URI(TEST_URI));

      assertEquals(SUCCESSFUL_RESPONSE_CONTENT, result.content());
      assertEquals(new URI(TEST_URI), result.originalUri());
      entityUtils.verify(() -> EntityUtils.toString(any()));
    }
  }

  @Test
  void testPost() throws IOException, URISyntaxException {
    try (MockedStatic<EntityUtils> entityUtils = mockStatic(EntityUtils.class)) {
      entityUtils.when(() -> EntityUtils.toString(any())).thenReturn(SUCCESSFUL_RESPONSE_CONTENT);
      stubExecute(getSuccessfulResponse());

      ParsedHttpResponse result = httpService.post(new URI(TEST_URI), "post-body");

      assertEquals(SUCCESSFUL_RESPONSE_CONTENT, result.content());
      assertEquals(new URI(TEST_URI), result.originalUri());
      entityUtils.verify(() -> EntityUtils.toString(any()));
    }
  }

  @Test
  void testPostFormDataWithRedirectionUrlSet() throws IOException, URISyntaxException {
    try (MockedStatic<EntityUtils> entityUtils = mockStatic(EntityUtils.class)) {
      entityUtils.when(() -> EntityUtils.toString(any())).thenReturn(SUCCESSFUL_RESPONSE_CONTENT);
      stubExecute(getSuccessfulResponse());

      ParsedHttpResponse result = httpService.post(new URI(TEST_URI), Map.of("key", "value"));

      assertEquals(SUCCESSFUL_RESPONSE_CONTENT, result.content());
      assertEquals(SUCCESSFUL_RESPONSE_CONTENT, result.content());
      assertEquals(new URI(TEST_URI), result.originalUri());
      assertEquals(new URI(TEST_URI + "?key=value"), result.redirectionUri());
      entityUtils.verify(() -> EntityUtils.toString(any()));
    }
  }

  @Test
  void testFailingResponse() throws IOException, URISyntaxException {
    try (MockedStatic<EntityUtils> entityUtils = mockStatic(EntityUtils.class)) {
      stubExecute(getBadRequestResponse());

      assertThrows(IOException.class, () -> httpService.get(new URI(TEST_URI)));

      entityUtils.verify(() -> EntityUtils.consume(any()));
    }
  }

  @Test
  void testParseException() throws IOException, URISyntaxException {
    try (MockedStatic<EntityUtils> entityUtils = mockStatic(EntityUtils.class)) {
      entityUtils.when(() -> EntityUtils.toString(any())).thenThrow(ParseException.class);
      stubExecute(getSuccessfulResponse());

      assertThrows(IOException.class, () -> httpService.get(new URI(TEST_URI)));

      entityUtils.verify(() -> EntityUtils.consume(any()));
    }
  }

  private void stubExecute(CloseableHttpResponse response) throws IOException {
    when(httpClient.execute(any(ClassicHttpRequest.class), any(HttpClientResponseHandler.class)))
        .thenAnswer(
            invocation -> {
              HttpClientResponseHandler<String> handler = invocation.getArgument(1);
              return handler.handleResponse(response);
            });
  }

  private static CloseableHttpResponse getSuccessfulResponse() {
    ClassicHttpResponse response = mock(CloseableHttpResponse.class);
    when(response.getCode()).thenReturn(HttpStatus.SC_SUCCESS);
    when(response.getEntity()).thenReturn(new StringEntity(SUCCESSFUL_RESPONSE_CONTENT));
    return CloseableHttpResponse.adapt(response);
  }

  private static CloseableHttpResponse getBadRequestResponse() {
    ClassicHttpResponse response = mock(CloseableHttpResponse.class);
    when(response.getCode()).thenReturn(HttpStatus.SC_BAD_REQUEST);
    when(response.getEntity()).thenReturn(new StringEntity(SUCCESSFUL_RESPONSE_CONTENT));
    return CloseableHttpResponse.adapt(response);
  }
}
