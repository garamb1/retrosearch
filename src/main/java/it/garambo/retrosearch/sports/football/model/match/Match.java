package it.garambo.retrosearch.sports.football.model.match;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.garambo.retrosearch.sports.football.model.match.enums.Status;
import java.util.Date;
import org.jetbrains.annotations.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Match(
    int id,
    Date utcDate,
    Date lastUpdated,
    Area area,
    Status status,
    Competition competition,
    Score score)
    implements Comparable<Match> {

  @Override
  public int compareTo(@NotNull Match o) {
    return this.area.id()
        - o.area.id()
        + this.competition.id()
        - o.competition.id()
        + this.id
        - o.id
        + this.status.ordinal()
        - this.status.ordinal();
  }
}
