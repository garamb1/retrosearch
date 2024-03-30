package it.garambo.retrosearch.sports.football.scheduled;

import it.garambo.retrosearch.sports.football.client.FootballDataOrgClient;
import it.garambo.retrosearch.sports.football.model.FootballDataResponse;
import it.garambo.retrosearch.sports.football.repository.FootballDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(value = "retrosearch.news.enable", havingValue = "true")
public class FootballDataScheduledTask {

  @Autowired private FootballDataOrgClient apiClient;

  @Autowired private FootballDataRepository repository;

  @Scheduled(fixedRate = 30 * 60 * 1000)
  private void updateFootballData() {
    try {
      log.info("Updating article lists...");
      FootballDataResponse footballData = apiClient.fetchFootballData();
      repository.updateAll(footballData.matches());
    } catch (Exception e) {
      log.error("Article list update failed:", e);
    }
  }
}
