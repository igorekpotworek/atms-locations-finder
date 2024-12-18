package com.worldremit.atms.controllers;

import com.worldremit.atms.config.ValidationProperties;
import com.worldremit.atms.domain.ATMLocation;
import com.worldremit.atms.domain.Coordinates;
import com.worldremit.atms.domain.Distance;
import com.worldremit.atms.domain.Distance.Unit;
import com.worldremit.atms.finder.ATMLocationsFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.beans.PropertyEditorSupport;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@Validated
public class ATMLocationsFinderController {

  private final ATMLocationsFinder atmLocationsFinder;
  private final ValidationProperties properties;

  @GetMapping("atm")
  public Set<ATMLocation> availableATMs(
      @RequestParam(value = "lat") double latitude,
      @RequestParam(value = "long") double longitude,
      @RequestParam(value = "radius", required = false, defaultValue = "1") double radius,
      @RequestParam(value = "unit", required = false, defaultValue = "m") Unit unit) {
    Distance distance = Distance.of(radius, unit);
    validateRadius(distance);
    return atmLocationsFinder.limitedAvailableATMsLocations(
        new Coordinates(latitude, longitude), distance);
  }

  private void validateRadius(Distance radius) {
    if (radius.getMiles() > properties.getMaxRadiusInMiles()) throw new InvalidRadiusException();
  }

  @InitBinder
  public void initBinder(final WebDataBinder webdataBinder) {
    webdataBinder.registerCustomEditor(Unit.class, new DistanceUnitConverter());
  }

  public static class DistanceUnitConverter extends PropertyEditorSupport {
    public void setAsText(final String text) {
      setValue(Unit.fromString(text));
    }
  }
}
