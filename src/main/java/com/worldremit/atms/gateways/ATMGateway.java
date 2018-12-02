package com.worldremit.atms.gateways;

import com.worldremit.atms.domain.ATMLocation;
import com.worldremit.atms.domain.Coordinates;
import com.worldremit.atms.domain.Distance;

import java.util.Set;

public interface ATMGateway {

    Set<ATMLocation> availableATMLocations(Coordinates center, Distance radius);

    Set<ATMLocation> availableATMLocations(Coordinates center);
}
