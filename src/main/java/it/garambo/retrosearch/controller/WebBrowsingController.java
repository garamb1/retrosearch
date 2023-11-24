package it.garambo.retrosearch.controller;

import it.garambo.retrosearch.browse.model.ParsedHtmlPage;
import it.garambo.retrosearch.browse.parser.ModernHtmlParser;
import java.net.URI;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
public class WebBrowsingController {

  @Autowired private UrlValidator urlValidator;

  @Autowired private ModernHtmlParser htmlParser;

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
}
