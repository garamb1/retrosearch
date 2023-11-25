package it.garambo.retrosearch.controller;

import it.garambo.retrosearch.browse.model.ParsedHtmlPage;
import it.garambo.retrosearch.browse.parser.ModernHtmlParser;
import it.garambo.retrosearch.ddg.client.DDGClient;
import it.garambo.retrosearch.ddg.model.SearchResults;
import java.net.URI;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
public class RetroSearchController {

  @Autowired private DDGClient ddgClient;
  @Autowired private UrlValidator urlValidator;
  @Autowired private ModernHtmlParser htmlParser;

  @GetMapping("/search")
  public String search(
      @RequestParam() Optional<String> query, @RequestParam Optional<String> locale, Model model) {
    if (query.isEmpty() || query.get().isEmpty()) {
      return "redirect:/";
    }

    try {
      SearchResults searchResults =
          locale.isEmpty()
              ? ddgClient.search(query.get())
              : ddgClient.search(query.get(), locale.get());
      model.addAttribute(searchResults);
      return "search-results";
    } catch (Exception e) {
      log.error(
          "Error getting search result, for parameter query: {}, locale: {}", query, locale, e);
      model.addAttribute("error", "Could not get search results. Sorry :(");
      model.addAttribute(
          "details", "Your search query '" + query.get() + "' could not be processed");
      return "error-details";
    }
  }

  @GetMapping("/browse")
  public String browse(@RequestParam String url, Model model) {
    if (!urlValidator.isValid(url)) {
      return "redirect:/";
    }

    URI uri = URI.create(url);
    try {
      ParsedHtmlPage parsedHtmlPage = htmlParser.parsePage(uri);
      model.addAttribute("parsedHtmlPage", parsedHtmlPage);
      model.addAttribute("originalUrl", uri.toASCIIString());
      return "browse";
    } catch (Exception e) {
      log.error("Could not parse page at {}", uri, e);
      model.addAttribute("error", "Could not parse this page. Sorry :(");
      model.addAttribute("details", "Original address: " + uri);
      return "error-details";
    }
  }

  @GetMapping("/about")
  public String about() {
    return "about";
  }
}
