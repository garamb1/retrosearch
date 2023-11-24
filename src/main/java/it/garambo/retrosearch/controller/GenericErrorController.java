package it.garambo.retrosearch.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GenericErrorController implements ErrorController {

  @GetMapping("/error")
  public String genericError(Model model) {
    return "error";
  }

}
