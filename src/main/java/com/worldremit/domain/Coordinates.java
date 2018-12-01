package com.worldremit.domain;

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

  public Coordinates move(double distanceInMilesByLatitude,
                          double distanceInMilesByLongitude) {
    return new Coordinates(
        latitude + milesToDegreesLat(distanceInMilesByLatitude),
        longitude + milesToDegreesLong(distanceInMilesByLongitude, latitude));
  }

  private static double milesToDegreesLat(double distanceInMiles) {
    return distanceInMiles / 69.1;
  }

  private static double milesToDegreesLong(double distanceInMiles, double latitude) {
    return distanceInMiles / (69.1 * cos(toRadians(latitude)));
  }

   double distance(Coordinates other) {
    double theta = longitude - other.longitude;
    double dist =
        sin(toRadians(latitude)) * sin(toRadians(other.latitude))
            + cos(toRadians(latitude)) * cos(toRadians(other.latitude)) * cos(toRadians(theta));
    dist = acos(dist);
    dist = toDegrees(dist);
    dist = dist * 69.1;
    return dist; // km dist * 1.609344;
  }
}
