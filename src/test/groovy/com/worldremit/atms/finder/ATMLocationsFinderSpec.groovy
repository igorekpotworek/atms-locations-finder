package com.worldremit.atms.finder

import com.worldremit.atms.config.ATMLocationsFinderProperties
import com.worldremit.atms.domain.ATMLocation
import com.worldremit.atms.domain.Coordinates
import com.worldremit.atms.domain.Distance
import com.worldremit.atms.gateways.ATMGateway
import spock.lang.Specification

class ATMLocationsFinderSpec extends Specification {

    private def center = new Coordinates(0.0, 0.0)

    private def point1 = new Coordinates(0.01255259, -0.28255044)
    private def atm1 = new ATMLocation("1", point1, center)
    private def point2 = new Coordinates(-0.02806433, 0.0939164)
    private def atm2 = new ATMLocation("2", point2, center)
    private def point3 = new Coordinates(0.02155503, 0.0580325)
    private def atm3 = new ATMLocation("3", point3, center)
    private def point4 = new Coordinates(-0.23480865, -0.020968)
    private def atm4 = new ATMLocation("4", point4, center)

    private def radius = 17.0
    private def allATMs = [atm1, atm2, atm3, atm4] as Set
    private def atmsInRadius = [atm2, atm3, atm4] as Set
    private def limitedATMsInRadius = [atm2, atm3] as Set
    private def maxUnderlyingClientRadiusInMiles = 10

    private def gateway = Mock(ATMGateway)

    def setup() {
        gateway.availableATMLocations(_ as Coordinates, _ as Distance) >> { Coordinates c, Distance d ->
            allATMs.findAll { a -> a.getCoordinates().distance(c) < d }.toSet()
        }
        gateway.availableATMLocations(_ as Coordinates) >> { Coordinates c ->
            gateway.availableATMLocations(c, Distance.ofMiles(maxUnderlyingClientRadiusInMiles))
        }
    }

    def "all atms locations are found"() {
        given:
        def limit = 4
        def properties = new ATMLocationsFinderProperties(maxUnderlyingClientRadiusInMiles, limit)
        def algorithm = new CircleCoveringAlgorithm(gateway, properties)
        def atmLocationsFinder = new ATMLocationsFinder(properties, gateway, algorithm)

        when:
        def results = atmLocationsFinder.limitedAvailableATMsLocations(center, Distance.ofMiles(radius))

        then:
        results == atmsInRadius
    }

    def "limited atms locations are found"() {
        given:
        def limit = 2
        def properties = new ATMLocationsFinderProperties(maxUnderlyingClientRadiusInMiles, limit)
        def algorithm = new CircleCoveringAlgorithm(gateway, properties)
        def atmLocationsFinder = new ATMLocationsFinder(properties, gateway, algorithm)

        when:
        def results = atmLocationsFinder.limitedAvailableATMsLocations(center, Distance.ofMiles(radius))

        then:
        results == limitedATMsInRadius
    }
}
