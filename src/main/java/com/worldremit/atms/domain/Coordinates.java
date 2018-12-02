package com.worldremit.atms.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import static java.lang.Math.*;

@Value
public class Coordinates {

  private double latitude;
  private double longitude;

  @JsonCreator
  public Coordinates(
      @JsonProperty("Latitude") double latitude, @JsonProperty("Longitude") double longitude) {
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public Coordinates move(Distance distanceByLatitude, Distance distanceByLongitude) {
    return new Coordinates(
        latitude + distanceByLatitude.toDegreesLat(),
        longitude + distanceByLongitude.toDegreesLong(latitude));
  }

  public Distance distance(Coordinates other) {
    double theta = longitude - other.longitude;
    double dist =
        sin(toRadians(latitude)) * sin(toRadians(other.latitude))
            + cos(toRadians(latitude)) * cos(toRadians(other.latitude)) * cos(toRadians(theta));
    dist = acos(dist);
    dist = toDegrees(dist);
    dist = dist * 69.1;
    return Distance.ofMiles(dist);
  }
}
