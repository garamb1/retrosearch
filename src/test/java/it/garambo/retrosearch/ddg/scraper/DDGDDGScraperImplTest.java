package it.garambo.retrosearch.ddg.scraper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import it.garambo.retrosearch.ddg.model.ResultEntry;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.List;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class DDGDDGScraperImplTest {

  @Autowired DDGScraper DDGScraper;

  @Test
  void testScrapeResults(@Value("classpath:ddg/scraper/test_result.html") Resource testResultHtml)
      throws IOException {
    String content = testResultHtml.getContentAsString(Charset.defaultCharset());
    List<ResultEntry> results = DDGScraper.scrapeResults(content);
    assertNotNull(results);
    assertEquals(4, results.size());

    ResultEntry firstEntry = results.get(0);
    assertEquals("Description 1", firstEntry.description());
    assertEquals("Title 1", firstEntry.title());
    assertEquals(URI.create("http://test1.com"), firstEntry.uri());

    // entry with regular url
    ResultEntry lastEntry = results.get(3);
    assertEquals(URI.create("http://test4.com"), lastEntry.uri());
  }

  @Test
  void testScrapeResultsWithInvalidElement(
      @Value("classpath:ddg/scraper/test_result_invalid.html") Resource testResultHtml)
      throws IOException {
    String content = testResultHtml.getContentAsString(Charset.defaultCharset());
    List<ResultEntry> results = DDGScraper.scrapeResults(content);
    assertNotNull(results);
    assertEquals(1, results.size());
  }

  @Test
  void testGetDocument(@Value("classpath:ddg/scraper/test_result.html") Resource testResultHtml)
      throws IOException {
    String content = testResultHtml.getContentAsString(Charset.defaultCharset());
    Document document = DDGScraper.getDocument(content);

    assertNotNull(document);
    Assertions.assertEquals("Test Page", document.title());
  }

  @Test
  void testGetEntryTitle(@Mock Element resultElement, @Mock Elements queryResults) {
    Element firstElement = mock(Element.class);
    when(firstElement.text()).thenReturn("Title");
    when(queryResults.first()).thenReturn(firstElement);
    when(resultElement.select(eq(DDGScraperConstants.RESULT_TITLE_CLASS_SELECTOR)))
        .thenReturn(queryResults);

    String title = DDGScraper.getEntryTitle(resultElement);
    assertEquals("Title", title);
  }

  @Test
  void testGetEntryTitleEmpty(@Mock Element resultElement) {
    when(resultElement.select(eq(DDGScraperConstants.RESULT_TITLE_CLASS_SELECTOR)))
        .thenReturn(new Elements());
    String title = DDGScraper.getEntryTitle(resultElement);
    assertNull(title);
  }

  @Test
  void testGetEntryUri(@Mock Element resultElement, @Mock Elements queryResults)
      throws MalformedURLException {
    Element firstElement = mock(Element.class);
    when(firstElement.attr(eq("href"))).thenReturn("http://github.com");
    when(queryResults.first()).thenReturn(firstElement);
    when(resultElement.select(eq(DDGScraperConstants.RESULT_TITLE_CLASS_SELECTOR)))
        .thenReturn(queryResults);

    URI title = DDGScraper.getEntryUri(resultElement);
    assertEquals(URI.create("http://github.com"), title);
  }

  @Test
  void testGetEntryUriEmpty(@Mock Element resultElement) throws MalformedURLException {
    when(resultElement.select(eq(DDGScraperConstants.RESULT_TITLE_CLASS_SELECTOR)))
        .thenReturn(new Elements());
    URI title = DDGScraper.getEntryUri(resultElement);
    assertNull(title);
  }

  @Test
  void testGetEntryUriInvalid(@Mock Element resultElement, @Mock Elements queryResults) {
    Element firstElement = mock(Element.class);
    when(firstElement.attr(eq("href"))).thenReturn("not-a-uri");
    when(queryResults.first()).thenReturn(firstElement);
    when(resultElement.select(eq(DDGScraperConstants.RESULT_TITLE_CLASS_SELECTOR)))
        .thenReturn(queryResults);

    Exception expectedException =
        assertThrows(
            MalformedURLException.class,
            () -> {
              DDGScraper.getEntryUri(resultElement);
            });
    assertEquals("Invalid url for entry, url: not-a-uri", expectedException.getMessage());
  }

  @Test
  void testGetEntryDescription(@Mock Element resultElement, @Mock Elements queryResults) {
    Element firstElement = mock(Element.class);
    when(firstElement.text()).thenReturn("Description");
    when(queryResults.first()).thenReturn(firstElement);
    when(resultElement.select(eq(DDGScraperConstants.RESULT_DESCRIPTION_CLASS_SELECTOR)))
        .thenReturn(queryResults);

    String description = DDGScraper.getEntryDescription(resultElement);
    assertEquals("Description", description);
  }

  @Test
  void testGetEntryDescriptionEmpty(@Mock Element resultElement) {
    when(resultElement.select(eq(DDGScraperConstants.RESULT_DESCRIPTION_CLASS_SELECTOR)))
        .thenReturn(new Elements());
    String description = DDGScraper.getEntryDescription(resultElement);
    assertNull(description);
  }
}
