package it.garambo.retrosearch.sports.football.repository;

import it.garambo.retrosearch.sports.football.model.match.Match;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(value = "retrosearch.sports.football.enable", havingValue = "true")
public class InMemorySportsRepository implements SportsRepository {

  Map<String, Set<Match>> matchByArea;
  private Date updatedAt;

  @Override
  public Set<Match> getAllMatchesByArea(String areaName) {
    return matchByArea.get(areaName);
  }

  @Override
  public void updateAll(Set<Match> newMatches) {
    matchByArea.clear();
    newMatches.forEach(
        match -> {
          String areaName = match.area().name();
          matchByArea.putIfAbsent(areaName, Set.of());
          matchByArea.get(areaName).add(match);
        });
    log.info("Football Results updated");
  }

  @Override
  public Date getUpdatedAt() {
    return updatedAt;
  }
}
