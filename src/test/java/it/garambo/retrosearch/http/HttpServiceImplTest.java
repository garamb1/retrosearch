package it.garambo.retrosearch.http;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HttpServiceImplTest {

  @Test
  void testGet(@Mock HttpClient httpClient, @Mock ResponseHandler<String> responseHandler)
      throws IOException, URISyntaxException {

    when(httpClient.execute(any(HttpGet.class), eq(responseHandler))).thenReturn("Success");

    HttpService httpService = new HttpServiceImpl(httpClient, responseHandler);
    assertEquals("Success", httpService.get(new URI("http://test.com")));
  }

  @Test
  void testPost(@Mock HttpClient httpClient, @Mock ResponseHandler<String> responseHandler)
      throws IOException, URISyntaxException {

    when(httpClient.execute(any(HttpPost.class), eq(responseHandler))).thenReturn("Success");

    HttpService httpService = new HttpServiceImpl(httpClient, responseHandler);
    assertEquals("Success", httpService.post(new URI("http://test.com"), "post-body"));
  }

  @Test
  void testPostFormData(@Mock HttpClient httpClient, @Mock ResponseHandler<String> responseHandler)
      throws IOException, URISyntaxException {

    when(httpClient.execute(any(HttpPost.class), eq(responseHandler))).thenReturn("Success");

    HttpService httpService = new HttpServiceImpl(httpClient, responseHandler);
    Map<String, String> formData = Map.of("key", "value");
    assertEquals("Success", httpService.post(new URI("http://test.com"), formData));
  }
}
