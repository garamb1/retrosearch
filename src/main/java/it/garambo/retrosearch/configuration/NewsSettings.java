package it.garambo.retrosearch.configuration;

import java.util.List;
import java.util.Locale;
import lombok.Getter;

@Getter
public class NewsSettings {

  private final boolean enabled;
  private final long rateLimiter;
  private final List<Locale> locales;

  public NewsSettings(boolean enabled, long rateLimiter, List<Locale> locales) {
    this.enabled = enabled;
    this.rateLimiter = rateLimiter;
    this.locales = locales;
  }
}
