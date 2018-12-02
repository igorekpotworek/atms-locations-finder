package com.worldremit.atms.config;

import com.worldremit.atms.domain.Distance;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("finder")
public class ATMLocationsFinderProperties {
    @Setter
    @Getter
    private int maxRadiusInMiles;
  @Setter @Getter private int maxResults;

    public Distance getMaxRadius() {
        return Distance.ofMiles(maxRadiusInMiles);
    }
}
