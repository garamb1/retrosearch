package it.garambo.retrosearch.http;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class HttpClientFactoryTest {

  @Test
  void buildClient() {
    assertNotNull(new HttpClientFactory().createHttpClient());
  }
}
