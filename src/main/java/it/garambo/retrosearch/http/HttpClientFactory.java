package it.garambo.retrosearch.http;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Component;

@Component
public class HttpClientFactory {

  protected HttpClient createHttpClient() {
    return HttpClientBuilder.create().build();
  }
}
