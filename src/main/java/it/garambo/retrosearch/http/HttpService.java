package it.garambo.retrosearch.http;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public interface HttpService {

  String get(URI uri) throws IOException, URISyntaxException;

  String get(URI uri, Map<String, String> params) throws IOException, URISyntaxException;

  String post(URI uri, String body) throws IOException;

  String post(URI uri, Map<String, String> formData) throws IOException, URISyntaxException;
}
