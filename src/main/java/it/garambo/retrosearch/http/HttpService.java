package it.garambo.retrosearch.http;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import org.apache.http.Header;

public interface HttpService {

  String get(URI uri) throws IOException, URISyntaxException;

  String get(URI uri, Map<String, String> params) throws IOException, URISyntaxException;

  String get(URI uri, List<Header> additionalHeaders) throws IOException, URISyntaxException;

  String get(URI uri, Map<String, String> params, List<Header> additionalHeaders)
      throws IOException, URISyntaxException;

  String post(URI uri, String body) throws IOException;

  String post(URI uri, Map<String, String> formData) throws IOException, URISyntaxException;
}
