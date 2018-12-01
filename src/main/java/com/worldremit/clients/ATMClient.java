package com.worldremit.clients;

import com.worldremit.config.ATMClientConfiguration;
import com.worldremit.clients.model.AvailableATMs;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
    value = "atms",
    url = "https://api.hsbc.com/x-open-banking/v2.2",
    configuration = ATMClientConfiguration.class)
public interface ATMClient {

  @GetMapping("/atms/geo-location/lat/{lat}/long/{long}")
  AvailableATMs availableATMs(
      @PathVariable("lat") double latitude,
      @PathVariable("long") double longitude,
      @RequestParam(value = "radius") int radius);
}
