package it.garambo.retrosearch.ddg.util;

import lombok.Getter;

@Getter
public class DDGScraperConstants {
  public static final String RESULT_LINKS_CLASS_SELECTOR = ".results_links";
  public static final String RESULT_TITLE_CLASS_SELECTOR = ".result__title > a";
  public static final String RESULT_DESCRIPTION_CLASS_SELECTOR = "a.result__snippet";
}
