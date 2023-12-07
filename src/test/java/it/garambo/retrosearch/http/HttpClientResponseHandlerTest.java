package it.garambo.retrosearch.http;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HttpClientResponseHandlerTest {

  @Test
  void mapResponse200(
      @Mock HttpResponse okResponse, @Mock StatusLine statusLine, @Mock HttpEntity httpEntity) {
    when(statusLine.getStatusCode()).thenReturn(200);
    when(okResponse.getStatusLine()).thenReturn(statusLine);
    when(okResponse.getEntity()).thenReturn(httpEntity);

    try (MockedStatic<EntityUtils> entityUtilsMock = mockStatic(EntityUtils.class)) {
      entityUtilsMock.when(() -> EntityUtils.toString(eq(httpEntity))).thenReturn("Success");
      assertEquals("Success", new HttpClientResponseHandler().handleResponse(okResponse));
    } catch (IOException e) {
      fail();
    }
  }

  @Test
  void mapResponse403(@Mock HttpResponse notFoundResponse, @Mock StatusLine statusLine) {
    when(statusLine.getStatusCode()).thenReturn(403);
    when(notFoundResponse.getStatusLine()).thenReturn(statusLine);

    Exception expectedException =
        assertThrows(
            IOException.class,
            () -> {
              new HttpClientResponseHandler().handleResponse(notFoundResponse);
            });
  }
}
