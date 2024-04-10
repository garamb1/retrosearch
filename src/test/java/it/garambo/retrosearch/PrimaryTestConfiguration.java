package it.garambo.retrosearch;

import it.garambo.retrosearch.news.repository.InMemoryNewsRepository;
import it.garambo.retrosearch.news.repository.NewsRepository;
import it.garambo.retrosearch.sports.football.repository.FootballRepository;
import it.garambo.retrosearch.sports.football.repository.InMemoryFootballRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class PrimaryTestConfiguration {

  @Bean
  @Primary
  public FootballRepository footballRepository() {
    return new InMemoryFootballRepository();
  }

  @Bean
  @Primary
  public NewsRepository newsRepository() {
    return new InMemoryNewsRepository();
  }
}
