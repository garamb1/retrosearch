package it.garambo.retrosearch.http;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

@Component
public class HttpClientResponseHandler implements ResponseHandler<String> {
  @Override
  public String handleResponse(HttpResponse response) throws IOException {
    int statusCode = response.getStatusLine().getStatusCode();
    if (statusCode >= 200 && statusCode < 400) {
      HttpEntity entity = response.getEntity();
      return EntityUtils.toString(entity);
    } else {
      throw new IOException(
          String.format(
              "Response %s, got status code %s",
              response.getStatusLine().getReasonPhrase(), statusCode));
    }
  }
}
