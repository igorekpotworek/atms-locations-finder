package com.worldremit.atms.gateways;

import com.worldremit.atms.clients.ATMClient;
import com.worldremit.atms.config.ATMLocationsFinderProperties;
import com.worldremit.atms.domain.ATMLocation;
import com.worldremit.atms.domain.Coordinates;
import com.worldremit.atms.domain.Distance;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

import static java.lang.Math.ceil;
import static java.util.stream.Collectors.toSet;

@Service
@RequiredArgsConstructor
public class ATMGateway {

  private final ATMLocationsFinderProperties atmLocationsFinderProperties;
  private final ATMClient atmClient;

  public Set<ATMLocation> availableATMLocations(Coordinates center, Distance radius) {
    return atmClient
        .availableATMs(center.getLatitude(), center.getLongitude(), (int) ceil(radius.getMiles()))
        .getAtms()
        .stream()
        .map(atm -> new ATMLocation(atm.getId(), atm.getCoordinates(), center))
        .collect(toSet());
  }

  public Set<ATMLocation> availableATMLocations(Coordinates center) {
    return availableATMLocations(center, atmLocationsFinderProperties.getMaxRadius());
  }
}
