package com.worldremit;

import com.worldremit.config.ATMLocationsFinderProperties;
import com.worldremit.domain.ATMLocation;
import com.worldremit.domain.Coordinates;
import com.worldremit.services.ATMService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toSet;

@Service
@RequiredArgsConstructor
public class ATMLocationsFinder {

  private final ATMLocationsFinderProperties atmLocationsFinderProperties;
  private final ATMService atmService;
  private final CircleCoveringAlgorithm circleCoveringAlgorithm;

  public Set<ATMLocation> limitedAvailableATMsLocations(Coordinates center, double radius) {
    return filterClosest(availableATMsLocations(center, radius));
  }

  private Set<ATMLocation> availableATMsLocations(Coordinates center, double radius) {
    if (radius <= atmLocationsFinderProperties.getMaxRadius()) {
      return atmService.availableATMsLocations(center, radius);
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
