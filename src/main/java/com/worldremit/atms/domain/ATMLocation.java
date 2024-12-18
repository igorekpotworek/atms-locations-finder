package com.worldremit.atms.domain;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ATMLocation {

  @EqualsAndHashCode.Include private String id;
  private Coordinates coordinates;
  private Distance distanceFromCenter;

  public ATMLocation(String id, Coordinates coordinates, Coordinates center) {
    this.id = id;
    this.coordinates = coordinates;
    this.distanceFromCenter = coordinates.distance(center);
  }
}
