package com.worldremit.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("finder")
public class ATMLocationsFinderProperties {
  @Setter @Getter private int maxRadius;
  @Setter @Getter private int maxResults;

}
