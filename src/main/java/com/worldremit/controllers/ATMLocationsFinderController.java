package com.worldremit.controllers;

import com.worldremit.ATMLocationsFinder;
import com.worldremit.domain.ATMLocation;
import com.worldremit.domain.Coordinates;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@Validated
public class ATMLocationsFinderController {

  private final ATMLocationsFinder atmLocationsFinder;

  @GetMapping("atm")
  public Set<ATMLocation> availableATMs(
      @RequestParam(value = "lat", defaultValue = "51.50551621597067") double lat,
      @RequestParam(value = "long", defaultValue = "-0.0180120225995") double lon,
      @RequestParam(value = "radius", required = false, defaultValue = "10") @Max(31)
          double radius) {
    return atmLocationsFinder.limitedAvailableATMsLocations(new Coordinates(lat, lon), radius);
  }
}
