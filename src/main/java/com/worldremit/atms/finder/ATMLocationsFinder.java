package com.worldremit.atms.finder;

import com.worldremit.atms.config.ATMLocationsFinderProperties;
import com.worldremit.atms.domain.ATMLocation;
import com.worldremit.atms.domain.Coordinates;
import com.worldremit.atms.domain.Distance;
import com.worldremit.atms.gateways.ATMGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toSet;

@Service
@RequiredArgsConstructor
public class ATMLocationsFinder {

  private final ATMLocationsFinderProperties atmLocationsFinderProperties;
  private final ATMGateway atmGateway;
  private final CircleCoveringAlgorithm circleCoveringAlgorithm;

  public Set<ATMLocation> limitedAvailableATMsLocations(Coordinates center, Distance radius) {
    return filterClosest(availableATMsLocations(center, radius));
  }

  private Set<ATMLocation> availableATMsLocations(Coordinates center, Distance radius) {
    if (radius.compareTo(atmLocationsFinderProperties.getMaxRadius()) <= 0) {
      return atmGateway.availableATMLocations(center, radius);
    } else {
      return circleCoveringAlgorithm.getAtmLocationsUsingSmallerRequests(center, radius);
    }
  }

  private Set<ATMLocation> filterClosest(Set<ATMLocation> atms) {
    return atms.stream()
        .sorted(comparing(ATMLocation::getDistanceFromCenter))
        .limit(atmLocationsFinderProperties.getMaxResults())
        .collect(toSet());
  }
}
