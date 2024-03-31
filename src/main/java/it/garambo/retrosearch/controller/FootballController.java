package it.garambo.retrosearch.controller;

import it.garambo.retrosearch.sports.football.repository.FootballRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@ConditionalOnBean(FootballRepository.class)
public class FootballController {

  @Autowired private FootballRepository footballRepository;

  @GetMapping(path = {"/football", "/sports/football"})
  public String football(Model model) {
    model.addAttribute("updatedAt", footballRepository.getUpdatedAt());
    model.addAttribute("results", footballRepository.getAllMatches());
    return "football";
  }
}
