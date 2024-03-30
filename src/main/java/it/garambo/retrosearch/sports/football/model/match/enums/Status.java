package it.garambo.retrosearch.sports.football.model.match.enums;

public enum Status {
  SCHEDULED("Scheduled"),
  TIMED("Timed"),
  IN_PLAY("In Play"),
  PAUSED("Paused"),
  FINISHED("Finished"),
  SUSPENDED("Suspended"),
  POSTPONED("Postponed"),
  CANCELLED("Canceled"),
  AWARDED("Awarded");

  final String description;

  private Status(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
}
