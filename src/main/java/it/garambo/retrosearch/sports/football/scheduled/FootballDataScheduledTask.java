package it.garambo.retrosearch.sports.football.scheduled;

import it.garambo.retrosearch.sports.football.client.FootballDataOrgClient;
import it.garambo.retrosearch.sports.football.model.FootballDataResponse;
import it.garambo.retrosearch.sports.football.repository.FootballRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(value = "retrosearch.sports.football.enable", havingValue = "true")
public class FootballDataScheduledTask {

  @Autowired private FootballDataOrgClient apiClient;

  @Autowired private FootballRepository repository;

  @Scheduled(fixedRate = 30 * 60 * 1000)
  private void updateFootballData() {
    try {
      log.info("Updating football result list...");
      FootballDataResponse footballData = apiClient.fetchFootballData();
      repository.updateAll(footballData.matches());
    } catch (Exception e) {
      log.error("Football result list update failed:", e);
    }
  }
}
