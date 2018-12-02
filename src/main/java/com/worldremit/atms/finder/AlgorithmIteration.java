package com.worldremit.atms.finder;

import com.worldremit.atms.domain.ATMLocation;
import com.worldremit.atms.domain.Distance;
import lombok.Value;

import java.util.Set;

@Value
class AlgorithmIteration {

    private int iterationNumber;
    private Set<ATMLocation> results;
    private Distance currentRadius;

    boolean test(Distance radius, int maxResults) {
        return currentRadius.compareTo(radius) >= 0 || results.size() >= maxResults;
    }
}
