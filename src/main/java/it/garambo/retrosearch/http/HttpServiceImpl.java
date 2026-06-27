package it.garambo.retrosearch.http;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicHeader;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.net.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class HttpServiceImpl implements HttpService {

  private final CloseableHttpClient closeableHttpClient;

  private final List<BasicHeader> defaultClientHeaders =
      List.of(
          new BasicHeader("charset", "UTF-8"),
          new BasicHeader(
              "User-Agent",
              "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"));

  public HttpServiceImpl(@Autowired CloseableHttpClient closeableHttpClient) {
    this.closeableHttpClient = closeableHttpClient;
  }

  @Override
  public ParsedHttpResponse get(URI uri) throws IOException, URISyntaxException {
    return get(uri, Collections.emptyMap());
  }

  @Override
  public ParsedHttpResponse get(URI uri, Map<String, String> params)
      throws IOException, URISyntaxException {
    return get(uri, params, Collections.emptyList());
  }

  @Override
  public ParsedHttpResponse get(URI uri, List<Header> additionalHeaders)
      throws IOException, URISyntaxException {
    return get(uri, Collections.emptyMap(), additionalHeaders);
  }

  @Override
  public ParsedHttpResponse get(URI uri, Map<String, String> params, List<Header> additionalHeaders)
      throws IOException, URISyntaxException {
    URI newUri = new URIBuilder(uri).setParameters(mapToNameValuePair(params)).build();
    final HttpGet get = new HttpGet(newUri);

    Header[] requestHeaders = defaultClientHeaders.toArray(new Header[0]);

    if (!CollectionUtils.isEmpty(additionalHeaders)) {
      List<Header> newHeaders = new ArrayList<>(defaultClientHeaders);
      newHeaders.addAll(additionalHeaders);
      requestHeaders = newHeaders.toArray(new Header[0]);
    }

    get.setHeaders(requestHeaders);
    String content = getResponseContent(get);
    return ParsedHttpResponse.builder()
        .originalUri(uri)
        .redirectionUri(newUri.equals(uri) ? null : newUri)
        .content(content)
        .build();
  }

  @Override
  public ParsedHttpResponse post(URI uri, String body) throws IOException {
    final HttpPost post = new HttpPost(uri);
    post.setEntity(new StringEntity(body));
    post.setHeaders(defaultClientHeaders.toArray(new Header[0]));
    return ParsedHttpResponse.builder().originalUri(uri).content(getResponseContent(post)).build();
  }

  @Override
  public ParsedHttpResponse post(URI uri, Map<String, String> formData)
      throws IOException, URISyntaxException {
    URI newUri = new URIBuilder(uri).setParameters(mapToNameValuePair(formData)).build();
    final HttpPost post = new HttpPost(newUri);
    return ParsedHttpResponse.builder()
        .originalUri(uri)
        .redirectionUri(newUri)
        .content(getResponseContent(post))
        .build();
  }

  private List<NameValuePair> mapToNameValuePair(Map<String, String> map) {
    if (CollectionUtils.isEmpty(map)) {
      return Collections.emptyList();
    }
    return map.keySet().stream()
        .map(key -> new BasicNameValuePair(key, map.get(key)))
        .collect(Collectors.toList());
  }

  private String getResponseContent(ClassicHttpRequest request) throws IOException {
    return closeableHttpClient.execute(
        request,
        response -> {
          HttpEntity entity = response.getEntity();
          if (entity == null) {
            throw new IOException();
          }

          int statusCode = response.getCode();
          if (!is2xxSuccessful(statusCode)) {
            EntityUtils.consume(entity);
            throw new IOException(String.format("Response status code %s", statusCode));
          }

          try {
            return EntityUtils.toString(entity);
          } catch (ParseException e) {
            EntityUtils.consume(entity);
            throw new IOException("Failed to parse response entity", e);
          }
        });
  }

  private static boolean is2xxSuccessful(int statusCode) {
    return statusCode >= HttpStatus.SC_OK && statusCode < HttpStatus.SC_MULTIPLE_CHOICES;
  }
}
