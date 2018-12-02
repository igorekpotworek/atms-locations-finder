package com.worldremit.atms.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("validation")
public class ValidationProperties {
  @Setter @Getter private int maxRadiusInMiles;
}
