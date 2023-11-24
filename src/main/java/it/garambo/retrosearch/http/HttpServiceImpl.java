package it.garambo.retrosearch.http;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class HttpServiceImpl implements HttpService {
  ResponseHandler<String> responseHandler = new InternalHttpClientResponseHandler();

  @Override
  public String get(URI uri) throws IOException, URISyntaxException {
    return get(uri, Collections.emptyMap());
  }

  @Override
  public String get(URI uri, Map<String, String> params) throws IOException, URISyntaxException {
    URIBuilder newUri = new URIBuilder(uri).setParameters(mapToNameValuePair(params));
    final HttpGet get = new HttpGet(newUri.build());
    get.setHeader("charset", "UTF-8");
    return createHttpClient().execute(get, responseHandler);
  }

  @Override
  public String post(URI uri, String body) throws IOException {
    final HttpPost post = new HttpPost(uri);
    post.setEntity(new StringEntity(body));
    post.setHeader("charset", "UTF-8");
    return createHttpClient().execute(post, responseHandler);
  }

  @Override
  public String post(URI uri, Map<String, String> formData) throws IOException, URISyntaxException {
    URIBuilder newUri = new URIBuilder(uri).setParameters(mapToNameValuePair(formData));
    final HttpPost post = new HttpPost(newUri.build());
    return createHttpClient().execute(post, responseHandler);
  }

  private List<NameValuePair> mapToNameValuePair(Map<String, String> map) {
    if (CollectionUtils.isEmpty(map)) {
      return Collections.emptyList();
    }
    return map.keySet().stream()
        .map(key -> new BasicNameValuePair(key, map.get(key)))
        .collect(Collectors.toList());
  }

  private HttpClient createHttpClient() {
    return HttpClientBuilder.create().build();
  }

  private static class InternalHttpClientResponseHandler implements ResponseHandler<String> {
    @Override
    public String handleResponse(HttpResponse response)
        throws ClientProtocolException, IOException {
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
}
