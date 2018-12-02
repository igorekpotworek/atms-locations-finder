package com.worldremit.atms.domain;

import io.vavr.API;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.Arrays;

import static com.worldremit.atms.domain.Distance.Unit.*;
import static io.vavr.API.*;
import static java.lang.Math.cos;
import static java.lang.Math.toRadians;

@Value(staticConstructor = "ofMiles")
public class Distance implements Comparable<Distance> {

  private double miles;

  private static Distance ofKilometers(double kilometers) {
    return Distance.ofMiles(0.621371192 * kilometers);
  }

  private static Distance ofMeters(double meters) {
    return ofKilometers(meters / 1000.0);
  }

  public static Distance of(double distance, Unit unit) {
    return Match(unit)
        .of(
            Case(API.$(Kilometers), ofKilometers(distance)),
            Case(API.$(Miles), ofMiles(distance)),
            Case(API.$(Meters), ofMeters(distance)),
            Case(
                $(),
                () -> {
                  throw new IllegalArgumentException();
                }));
  }

  @Override
  public int compareTo(Distance other) {
    return Double.compare(miles, other.miles);
  }

  double toDegreesLat() {
    return miles / 69.1;
  }

  double toDegreesLong(double latitude) {
    return miles / (69.1 * cos(toRadians(latitude)));
  }

  @RequiredArgsConstructor
  public enum Unit {
    Meters("m"),
    Kilometers("km"),
    Miles("mi");

    private final String unit;

    public static Unit fromString(String string) {
      return Arrays.stream(Unit.values())
          .filter(v -> v.unit.equals(string))
          .findFirst()
          .orElseThrow(() -> new IllegalArgumentException("unknown value: " + string));
    }
  }
}
