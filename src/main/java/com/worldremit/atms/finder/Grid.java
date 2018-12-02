package com.worldremit.atms.finder;

import com.worldremit.atms.domain.Coordinates;
import com.worldremit.atms.domain.Distance;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;

@Value
class Grid {

    private final double gridSquareSide;

    private static int numberOfSquaresOnNLayerSide(int n) {
        return 2 * n + 1;
    }

    // number of small squares sizes
    Distance gridNLayerSide(int n) {
        return Distance.ofMiles(numberOfSquaresOnNLayerSide(n) * gridSquareSide);
    }

    List<Coordinates> coordinatesOfNLayerEdgeSquares(Coordinates center, int n) {
        List<Coordinates> centers = new ArrayList<>();
        Coordinates point =
                center.move(Distance.ofMiles(-n * gridSquareSide), Distance.ofMiles(-n * gridSquareSide));
        // direction
        int dx = 1;
        int dy = 0;

        for (int side = 0; side < 4; side++) {
            for (int i = 1; i < numberOfSquaresOnNLayerSide(n); i++) {
                centers.add(point);
                point =
                        point.move(
                                Distance.ofMiles(dx * gridSquareSide), Distance.ofMiles(dy * gridSquareSide));
            }
            // change direction
            int t = dx;
            dx = -dy;
            dy = t;
        }
        return centers;
    }

}
