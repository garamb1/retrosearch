package it.garambo.retrosearch.controller;

import it.garambo.retrosearch.news.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@ConditionalOnBean(NewsRepository.class)
public class NewsController {

  @Autowired private NewsRepository newsRepository;

  @GetMapping("/news")
  public String news(Model model) {
    model.addAttribute("updatedAt", newsRepository.getUpdatedAt());
    model.addAttribute("articles", newsRepository.getAllArticles());
    return "news";
  }
}
