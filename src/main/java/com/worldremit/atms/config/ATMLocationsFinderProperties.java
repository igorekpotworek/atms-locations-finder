package com.worldremit.atms.config;

import com.worldremit.atms.domain.Distance;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("finder")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ATMLocationsFinderProperties {
  private int maxUnderlyingClientRadiusInMiles;
  private int maxResults;

  public Distance getMaxUnderlyingClientRadius() {
    return Distance.ofMiles(maxUnderlyingClientRadiusInMiles);
  }
}
