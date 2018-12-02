package com.worldremit.atms.finder;

import com.google.common.collect.Sets;
import com.worldremit.atms.config.ATMLocationsFinderProperties;
import com.worldremit.atms.domain.ATMLocation;
import com.worldremit.atms.domain.Coordinates;
import com.worldremit.atms.domain.Distance;
import com.worldremit.atms.gateways.ATMGateway;
import io.vavr.collection.Stream;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;

import static java.lang.Math.sqrt;
import static java.util.stream.Collectors.toSet;

// starting from square inscribed in a maximum available by API circle and provided center,
// incrementally adding new layers consisting from such squares to the grid till finding
// all atms which fit provided conditions (distance from center and maximum number of results)
@Service
class CircleCoveringAlgorithm {

  private final ATMGateway atmGateway;
  private final ATMLocationsFinderProperties properties;
  private final Grid grid;

  CircleCoveringAlgorithm(ATMGateway atmGateway, ATMLocationsFinderProperties properties) {
    this.atmGateway = atmGateway;
    this.properties = properties;
    // square inscribed in a maximum available by API circle will be grid unit square
    this.grid = new Grid(sqrt(2) * properties.getMaxUnderlyingClientRadius().getMiles());
  }

  Set<ATMLocation> getAtmLocationsUsingSmallerRequests(Coordinates center, Distance radius) {
    Set<ATMLocation> interior =
        atmGateway.availableATMLocations(center, properties.getMaxUnderlyingClientRadius());
    AlgorithmResult firstIteration = new AlgorithmResult(0, interior, fullyCoveredRadiusForLayerNumber(0));

    return Stream.iterate(
            firstIteration,
            (previousIteration) -> {
              int i = previousIteration.getIterationNumber() + 1;
              Distance currentRadius = fullyCoveredRadiusForLayerNumber(i);
              Set<ATMLocation> nLayerLocations = getATMsFromNLayerOfGrid(center, i, radius);
              return new AlgorithmResult(
                  i, Sets.union(nLayerLocations, previousIteration.getResults()), currentRadius);
            })
        .dropWhile(it -> !it.test(radius, properties.getMaxResults()))
        .head()
        .getResults();
  }

  private Set<ATMLocation> getATMsFromNLayerOfGrid(Coordinates center, int n, Distance radius) {
    Set<ATMLocation> atms =
        grid.coordinatesOfNLayerEdgeSquares(center, n)
            .stream()
            .map(atmGateway::availableATMLocations)
            .flatMap(Collection::stream)
            .collect(toSet());
    return filterATMsIfNeeded(atms, n, radius);
  }

  private Set<ATMLocation> filterATMsIfNeeded(Set<ATMLocation> atms, int n, Distance radius) {
    if (maximumScopeForLayerNumber(n).compareTo(radius) > 0)
      return atms.stream()
          .filter(l -> l.getDistanceFromCenter().compareTo(radius) <= 0)
          .collect(toSet());
    else return atms;
  }

  private Distance maximumScopeForLayerNumber(int n) {
    return Distance.ofMiles(grid.nLayerSide(n).getMiles() / sqrt(2));
  }

  // diameter is equal to grid size
  private Distance fullyCoveredRadiusForLayerNumber(int n) {
    return Distance.ofMiles(grid.nLayerSide(n).getMiles() / 2);
  }
}
