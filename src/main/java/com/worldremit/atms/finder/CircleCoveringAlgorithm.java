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

@Service
class CircleCoveringAlgorithm {

    private final ATMGateway atmGateway;
  private final ATMLocationsFinderProperties properties;
    private final Grid grid;

    CircleCoveringAlgorithm(ATMGateway atmGateway, ATMLocationsFinderProperties properties) {
        this.atmGateway = atmGateway;
    this.properties = properties;
        // square inscribed in a maximum available by API circle will be grid unit square
        this.grid = new Grid(sqrt(2) * properties.getMaxRadius().getMiles());
  }

    Set<ATMLocation> getAtmLocationsUsingSmallerRequests(Coordinates center, Distance radius) {
        Set<ATMLocation> interior = atmGateway.availableATMLocations(center, properties.getMaxRadius());
        AlgorithmIteration algorithmZeroIteration =
                new AlgorithmIteration(0, interior, radiusForLayerNumber(0));

        return Stream.iterate(
                algorithmZeroIteration,
                (previousIteration) -> {
                    int i = previousIteration.getIterationNumber() + 1;
                    Distance currentRadius = radiusForLayerNumber(i);
                    Set<ATMLocation> nLayerLocations = getATMsFromNLayerOfGrid(center, i, radius);
                    return new AlgorithmIteration(
                            i, Sets.union(nLayerLocations, previousIteration.getResults()), currentRadius);
                })
                .dropWhile(it -> it.test(radius, properties.getMaxResults()))
                .head()
                .getResults();
  }

    private Set<ATMLocation> getATMsFromNLayerOfGrid(Coordinates center, int n, Distance radius) {
        return grid.coordinatesOfNLayerEdgeSquares(center, n)
        .stream()
                .map(atmGateway::availableATMLocations)
                .flatMap(Collection::stream)
                .filter(l -> l.getDistanceFromCenter().compareTo(radius) <= 0)
        .collect(toSet());
  }

  // diameter is equal to grid size
  private Distance radiusForLayerNumber(int n) {
      return Distance.ofMiles(grid.gridNLayerSide(n).getMiles() / 2);
  }
}
