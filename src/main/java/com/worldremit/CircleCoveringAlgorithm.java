package com.worldremit;

import com.google.common.collect.Sets;
import com.worldremit.config.ATMLocationsFinderProperties;
import com.worldremit.domain.ATMLocation;
import com.worldremit.domain.Coordinates;
import com.worldremit.services.ATMService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.lang.Math.sqrt;
import static java.util.stream.Collectors.toSet;

@Service
public class CircleCoveringAlgorithm {

  private final ATMService atmService;
  private final ATMLocationsFinderProperties properties;

  // square inscribed in a maximum available by API circle
  private final double gridSquareSide;

  public CircleCoveringAlgorithm(ATMService atmService, ATMLocationsFinderProperties properties) {
    this.atmService = atmService;
    this.properties = properties;
    this.gridSquareSide = sqrt(2) * properties.getMaxRadius();
  }

  Set<ATMLocation> getAtmLocationsUsingSmallerRequests(Coordinates center, double radius) {
    Set<ATMLocation> prev = atmService.availableATMsLocations(center, properties.getMaxRadius());
    for (int i = 1; ; i++) {
      double currentRadius = radiusForLayerNumber(i);
      Set<ATMLocation> current = clearATMs(getATMsAfterAddingNLayer(center, i, prev), radius);

      if (currentRadius >= radius || current.size() < properties.getMaxResults()) {
        return current;
      }
      prev = current;
    }
  }

  private Set<ATMLocation> clearATMs(Set<ATMLocation> locations, double radius) {
    return locations.stream().filter(l -> l.getDistanceFromCenter() <= radius).collect(toSet());
  }

  private Set<ATMLocation> getATMsAfterAddingNLayer(
      Coordinates center, int n, Set<ATMLocation> interior) {
    return Sets.union(getATMsFromNLayerOfGrid(center, n), interior);
  }

  private Set<ATMLocation> getATMsFromNLayerOfGrid(Coordinates center, int n) {
    return constructNLayerOfGrid(center, n)
        .stream()
        .flatMap(c -> atmService.availableATMsLocations(c).stream())
        .collect(toSet());
  }

  // diameter is equal to grid size
  private double radiusForLayerNumber(int n) {
    return gridSize(n) / 2;
  }

  // number of small squares sizes
  private double gridSize(int n) {
    return numberOfSquaresOnEdge(n) * gridSquareSide;
  }

  private int numberOfSquaresOnEdge(int n) {
    return 2 * n + 1;
  }

  private List<Coordinates> constructNLayerOfGrid(Coordinates center, int n) {
    List<Coordinates> centers = new ArrayList<>();
    Coordinates point = center.move(-n * gridSquareSide, -n * gridSquareSide);

    // direction
    int dx = 1;
    int dy = 0;

    for (int side = 0; side < 4; side++) {
      for (int i = 1; i < numberOfSquaresOnEdge(n); i++) {
        centers.add(point);
        point = point.move(dx * gridSquareSide, dy * gridSquareSide);
      }
      // change direction
      int t = dx;
      dx = -dy;
      dy = t;
    }
    return centers;
  }
}
