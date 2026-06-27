package it.garambo.retrosearch.http;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import org.apache.hc.core5.http.Header;

public interface HttpService {

  ParsedHttpResponse get(URI uri) throws IOException, URISyntaxException;

  ParsedHttpResponse get(URI uri, Map<String, String> params)
      throws IOException, URISyntaxException;

  ParsedHttpResponse get(URI uri, List<Header> additionalHeaders)
      throws IOException, URISyntaxException;

  ParsedHttpResponse get(URI uri, Map<String, String> params, List<Header> additionalHeaders)
      throws IOException, URISyntaxException;

  ParsedHttpResponse post(URI uri, String body) throws IOException;

  ParsedHttpResponse post(URI uri, Map<String, String> formData)
      throws IOException, URISyntaxException;
}
