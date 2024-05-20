package it.garambo.retrosearch.configuration;

import java.util.List;
import java.util.Locale;
import lombok.Getter;

@Getter
public class SearchSettings {
  private final List<Locale> locales;

  public SearchSettings(List<Locale> locales) {
    this.locales = locales;
  }
}
