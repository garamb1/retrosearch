package it.garambo.retrosearch.controller;

import it.garambo.retrosearch.ddg.client.DDGClient;
import it.garambo.retrosearch.ddg.model.SearchResults;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
public class WebSearchController {

  @Autowired private DDGClient ddgClient;

  @GetMapping
  public String index(Model model) {
    int randomLogoId = (int) (Math.random() * 3) + 1;
    model.addAttribute("logoId", randomLogoId);
    return "index";
  }

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
      return "search/results";
    } catch (Exception e) {
      log.error(
          "Error getting search result, for parameter query: {}, locale: {}", query, locale, e);
      model.addAttribute("error", "Could not get search results. Sorry :(");
      model.addAttribute("query", query.get());
      return "search/error";
    }
  }
}
