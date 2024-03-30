package it.garambo.retrosearch.http;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class HttpServiceImpl implements HttpService {

  private final HttpClientFactory clientFactory;

  private final ResponseHandler<String> responseHandler;

  private final List<BasicHeader> defaultClientHeaders =
      List.of(
          new BasicHeader("charset", "UTF-8"),
          new BasicHeader(
              "User-Agent",
              "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"));

  public HttpServiceImpl(
      @Autowired HttpClientFactory clientFactory,
      @Autowired ResponseHandler<String> responseHandler) {
    this.clientFactory = clientFactory;
    this.responseHandler = responseHandler;
  }

  @Override
  public String get(URI uri) throws IOException, URISyntaxException {
    return get(uri, Collections.emptyMap());
  }

  @Override
  public String get(URI uri, Map<String, String> params) throws IOException, URISyntaxException {
    return get(uri, params, Collections.emptyList());
  }

  @Override
  public String get(URI uri, List<Header> additionalHeaders)
      throws IOException, URISyntaxException {
    return get(uri, Collections.emptyMap(), additionalHeaders);
  }

  @Override
  public String get(URI uri, Map<String, String> params, List<Header> additionalHeaders)
      throws IOException, URISyntaxException {
    URIBuilder newUri = new URIBuilder(uri).setParameters(mapToNameValuePair(params));
    final HttpGet get = new HttpGet(newUri.build());

    Header[] requestHeaders = defaultClientHeaders.toArray(new Header[0]);

    if (!CollectionUtils.isEmpty(additionalHeaders)) {
      List<Header> newHeaders = new ArrayList<>(defaultClientHeaders);
      newHeaders.addAll(additionalHeaders);
      requestHeaders = newHeaders.toArray(new Header[0]);
    }

    get.setHeaders(requestHeaders);
    return clientFactory.createHttpClient().execute(get, responseHandler);
  }

  @Override
  public String post(URI uri, String body) throws IOException {
    final HttpPost post = new HttpPost(uri);
    post.setEntity(new StringEntity(body));
    post.setHeaders(defaultClientHeaders.toArray(new Header[0]));
    return clientFactory.createHttpClient().execute(post, responseHandler);
  }

  @Override
  public String post(URI uri, Map<String, String> formData) throws IOException, URISyntaxException {
    URIBuilder newUri = new URIBuilder(uri).setParameters(mapToNameValuePair(formData));
    final HttpPost post = new HttpPost(newUri.build());
    return clientFactory.createHttpClient().execute(post, responseHandler);
  }

  private List<NameValuePair> mapToNameValuePair(Map<String, String> map) {
    if (CollectionUtils.isEmpty(map)) {
      return Collections.emptyList();
    }
    return map.keySet().stream()
        .map(key -> new BasicNameValuePair(key, map.get(key)))
        .collect(Collectors.toList());
  }
}
