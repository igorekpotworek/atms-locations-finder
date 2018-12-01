package com.worldremit.services;

import com.worldremit.clients.ATMClient;
import com.worldremit.config.ATMLocationsFinderProperties;
import com.worldremit.domain.ATMLocation;
import com.worldremit.domain.Coordinates;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

import static java.lang.Math.ceil;
import static java.util.stream.Collectors.toSet;

@Service
@RequiredArgsConstructor
public class ATMService {

  private final ATMLocationsFinderProperties atmLocationsFinderProperties;
  private final ATMClient atmClient;

  public Set<ATMLocation> availableATMsLocations(Coordinates center, double radius) {
    return atmClient
        .availableATMs(center.getLatitude(), center.getLongitude(), (int) ceil(radius))
        .getAtms()
            .stream()
            .map(atm -> new ATMLocation(atm.getId(), atm.getCoordinates(), center))
            .collect(toSet());
  }

  public Set<ATMLocation> availableATMsLocations(Coordinates center) {
    return availableATMsLocations(center, atmLocationsFinderProperties.getMaxRadius());
  }
}
