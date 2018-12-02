package com.worldremit.atms.controllers;

import com.worldremit.atms.domain.ATMLocation;
import com.worldremit.atms.domain.Coordinates;
import com.worldremit.atms.domain.Distance;
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

  @GetMapping("atm")
  public Set<ATMLocation> availableATMs(
          @RequestParam(value = "lat", defaultValue = "51.50551621597067") double lat,
          @RequestParam(value = "long", defaultValue = "-0.0180120225995") double lon,
          @RequestParam(value = "radius", required = false, defaultValue = "10") double radius,
          @RequestParam(value = "unit", required = false, defaultValue = "mi") Distance.Unit unit) {
      return atmLocationsFinder.limitedAvailableATMsLocations(
              new Coordinates(lat, lon), Distance.of(radius, unit));
  }

    @InitBinder
    public void initBinder(final WebDataBinder webdataBinder) {
        webdataBinder.registerCustomEditor(Distance.Unit.class, new DistanceUnitConverter());
    }

    public static class DistanceUnitConverter extends PropertyEditorSupport {
        public void setAsText(final String text) {
            setValue(Distance.Unit.fromString(text));
        }
  }
}
