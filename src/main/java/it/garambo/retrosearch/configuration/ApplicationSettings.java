package it.garambo.retrosearch.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApplicationSettings {

  private HTMLVersion htmlVersion;
  private String encoding;
}
