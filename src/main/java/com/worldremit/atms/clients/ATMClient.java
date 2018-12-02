package com.worldremit.atms.clients;

import com.worldremit.atms.clients.model.AvailableATMs;
import com.worldremit.atms.config.ATMClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "atms", url = "${client.url}", configuration = ATMClientConfiguration.class)
public interface ATMClient {

  @GetMapping("${client.uri}")
  AvailableATMs availableATMs(
      @PathVariable("lat") double latitude,
      @PathVariable("long") double longitude,
      @RequestParam(value = "radius") int radius);
}
