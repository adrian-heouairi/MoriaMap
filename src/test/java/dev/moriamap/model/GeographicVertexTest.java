package dev.moriamap.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.util.List;
import java.util.ArrayList;

public class GeographicVertexTest {

    @Test void geoVertexHasNoNeighborsOnCreation() {
        GeographicVertex sut = GeographicVertex.at(
          GeographicPosition.NULL_ISLAND
        );
        List<Vertex> neighbors = sut.getNeighbors();
        assert(neighbors.isEmpty());
    }

    @Test void geoPosOfGeoVertexAtNorthPoleIsNorthPole() {
        GeographicVertex sut = GeographicVertex.at(
          GeographicPosition.NORTH_POLE
        );
        assertEquals(
          GeographicPosition.NORTH_POLE,
          sut.getGeographicPosition()
        );
    }

    @Test void neighborNotAddedIfAlreadyPresent() {
        GeographicVertex v = GeographicVertex.at(
          GeographicPosition.NORTH_POLE
        );
        GeographicVertex n = GeographicVertex.at(
          GeographicPosition.SOUTH_POLE
        );
        v.addNeighbor(n);
        v.addNeighbor(n);
        assertEquals(1, v.getNeighbors().size());
    }

    @Test void neighborRemovedIfPresent() {
        GeographicVertex v = GeographicVertex.at(
          GeographicPosition.NORTH_POLE
        );
        GeographicVertex n = GeographicVertex.at(
          GeographicPosition.SOUTH_POLE
        );
        v.addNeighbor(n);
        v.removeNeighbor(n);
        assert(v.getNeighbors().isEmpty());
    }

    @Test void vertexAddedToItsNeighborsThrowsException() {
        Vertex v = GeographicVertex.at(32.0, 43.0);
        assertThrows(IllegalArgumentException.class, () -> v.addNeighbor(v));
    }

    @Test void nullAddedToNeighborsThrowsException() {
        Vertex v = GeographicVertex.at(58.134, 22.4);
        assertThrows(NullPointerException.class, () -> v.addNeighbor(null));
    }
}
