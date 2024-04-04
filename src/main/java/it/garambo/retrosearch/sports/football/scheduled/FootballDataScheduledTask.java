package it.garambo.retrosearch.sports.football.scheduled;

import static java.util.Objects.isNull;

import it.garambo.retrosearch.sports.football.client.FootballDataOrgClient;
import it.garambo.retrosearch.sports.football.model.FootballDataResponse;
import it.garambo.retrosearch.sports.football.repository.FootballRepository;
import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(value = "retrosearch.sports.football.enable", havingValue = "true")
public class FootballDataScheduledTask {

  @Autowired private FootballDataOrgClient apiClient;

  @Autowired private FootballRepository repository;

  @Value("${retrosearch.sports.football.results.past.days:3}")
  private Integer pastDays;

  @Scheduled(fixedRate = 30 * 60 * 1000)
  private void updateFootballData() {
    LocalDate dateTo = LocalDate.now();
    LocalDate dateFrom = null;
    if (!isNull(pastDays) && pastDays > 0) {
      dateFrom = dateTo.minusDays(pastDays);
    }

    try {
      log.info("Updating football result list from {} to {}...", dateFrom, dateTo);
      FootballDataResponse footballData = apiClient.fetchFootballData(dateFrom, dateTo);
      repository.updateAll(footballData.matches());
    } catch (Exception e) {
      log.error("Football result list update failed:", e);
    }
  }
}
