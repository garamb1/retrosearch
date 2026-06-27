package it.garambo.retrosearch.browse.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import it.garambo.retrosearch.browse.model.ParsedHtmlPage;
import it.garambo.retrosearch.configuration.ApplicationSettings;
import it.garambo.retrosearch.configuration.HTMLVersion;
import it.garambo.retrosearch.http.HttpService;
import it.garambo.retrosearch.http.ParsedHttpResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// TODO: add test cases
class ModernHtmlParserImplTest {
  private ModernHtmlParserImpl parser;

  private HttpService httpService;
  private static final String TEST_URI = "http://test.com";
  private static final String REDIRECT_URI = "http://redirect.com";

  @BeforeEach
  void setup() {
    httpService = mock(HttpService.class);
    ApplicationSettings settings = new ApplicationSettings(HTMLVersion.HTML_2_0, "UTF-8");
    parser = new ModernHtmlParserImpl(httpService, settings);
  }

  @Test
  void successfulParse() throws URISyntaxException, IOException {
    String pageContent =
        """
            <head>
            	<title>Test title</title>
            <body>
            	<nav>
            		<a href="/home\\">Home</a>
            		<a href="/about\\">About</a>
            	</nav>
            	<p>Hello</p>
            	<script>alert('xss')</script>
            	<a href="http://example.com">link</a>
            	<canvas></canvas>
            </body>
            """;

    ParsedHttpResponse parsedHttpResponse =
        ParsedHttpResponse.builder()
            .content(pageContent)
            .originalUri(new URI(TEST_URI))
            .redirectionUri(new URI(REDIRECT_URI))
            .build();
    when(httpService.get(new URI(TEST_URI))).thenReturn(parsedHttpResponse);

    ParsedHtmlPage result = parser.parsePage(new URI(TEST_URI));

    assertNotNull(result);
    assertEquals(REDIRECT_URI, result.redirectTo().toString());
    assertEquals("Test title", result.title());

    String htmlContent = result.htmlContent();
    assertFalse(htmlContent.contains("<script>"));
    assertTrue(htmlContent.contains("<p>Hello</p>"));
    assertTrue(
        htmlContent.contains(
            "<a href=\"/browse?url=http://example.com\" rel=\"nofollow\">link</a>"));

    List<String> navigation = result.navigation();
    assertEquals(2, navigation.size());
    assertTrue(navigation.contains("<a rel=\"nofollow\" href=\"/browse?url=\">Home</a>"));
    assertTrue(navigation.contains("<a rel=\"nofollow\" href=\"/browse?url=\">About</a>"));
  }

  @Test
  void parseNoContent() throws URISyntaxException, IOException {
    when(httpService.get(new URI(TEST_URI)))
        .thenReturn(ParsedHttpResponse.builder().content(" ").build());
    assertThrows(NullPointerException.class, () -> parser.parsePage(new URI(TEST_URI)));
  }
}
