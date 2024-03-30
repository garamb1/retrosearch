package it.garambo.retrosearch;

import it.garambo.retrosearch.sports.football.repository.FootballDataRepository;
import it.garambo.retrosearch.sports.football.repository.InMemoryFootballDataRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class PrimaryTestConfiguration {

  @Bean
  @Primary
  public FootballDataRepository repository() {
    return new InMemoryFootballDataRepository();
  }
}
