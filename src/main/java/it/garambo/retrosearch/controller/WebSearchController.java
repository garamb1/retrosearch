package it.garambo.retrosearch.controller;

import it.garambo.retrosearch.ddg.client.DDGClient;
import it.garambo.retrosearch.ddg.model.SearchResults;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebSearchController {

  @Autowired private DDGClient ddgClient;

  @GetMapping("/search")
  public String search(
      @RequestParam String query, @RequestParam Optional<String> locale, Model model) {
    try {
      SearchResults searchResults =
          locale.isEmpty() ? ddgClient.search(query) : ddgClient.search(query, locale.get());
      model.addAttribute(searchResults);
      return "search-results";
    } catch (Exception e) {
      model.addAttribute("error", e);
      return "search-error-page";
    }
  }
}
