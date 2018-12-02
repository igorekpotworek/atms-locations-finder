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

    private def atms = [atm1, atm2, atm3, atm4] as Set
    private def limitedATMs = [atm2, atm3, atm4] as Set
    private def maxDistanceInMiles = 10

    private def gateway = Mock(ATMGateway)

    def setup() {
        gateway.availableATMLocations(_ as Coordinates, _ as Distance) >> { Coordinates c, Distance d ->
            atms.findAll { a -> a.getCoordinates().distance(c) < d }.toSet()
        }
        gateway.availableATMLocations(_ as Coordinates) >> { Coordinates c ->
            gateway.availableATMLocations(c, Distance.ofMiles(maxDistanceInMiles))
        }
    }

    def "all atms locations are found"() {
        given:
        def limit = 10
        def properties = new ATMLocationsFinderProperties(maxDistanceInMiles, limit)
        def algorithm = new CircleCoveringAlgorithm(gateway, properties)
        def atmLocationsFinder = new ATMLocationsFinder(properties, gateway, algorithm)

        when:
        def results = atmLocationsFinder.limitedAvailableATMsLocations(center, Distance.ofMiles(20.0))

        then:
        results == atms
    }

    def "limited atms locations are found"() {
        given:
        def limit = 3
        def properties = new ATMLocationsFinderProperties(maxDistanceInMiles, limit)
        def algorithm = new CircleCoveringAlgorithm(gateway, properties)
        def atmLocationsFinder = new ATMLocationsFinder(properties, gateway, algorithm)

        when:
        def results = atmLocationsFinder.limitedAvailableATMsLocations(center, Distance.ofMiles(20.0))

        then:
        results == limitedATMs
    }
}
