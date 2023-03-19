package dev.moriamap.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

public class GeographicVertexTest {

    @Test void geoVertexHasNoNeighborsOnCreation() {
        GeographicVertex sut = GeographicVertex.at(
          GeographicPosition.NULL_ISLAND
        );
        List<Vertex> neighbors = sut.getNeighbors();
        assertTrue(neighbors.isEmpty());
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
        assertTrue(v.getNeighbors().isEmpty());
    }

    @Test void vertexAddedToItsNeighborsThrowsException() {
        Vertex v = GeographicVertex.at(32.0, 43.0);
        assertThrows(IllegalArgumentException.class, () -> v.addNeighbor(v));
    }

    @Test void nullAddedToNeighborsThrowsException() {
        Vertex v = GeographicVertex.at(58.134, 22.4);
        assertThrows(NullPointerException.class, () -> v.addNeighbor(null));
    }

    @Test void nullGeoPosOnGeoVertexCreationThrowsException() {
        assertThrows(
          IllegalArgumentException.class,
          () -> GeographicVertex.at(null)
        );
    }

    @Test void equalsOnItselfReturnsTrue() {
        GeographicVertex v = GeographicVertex.at(58.134, 22.4);
        assertEquals(v,v);
    }

    @Test void semanticallyEqualInstancesAreEqual() {
        GeographicVertex u = GeographicVertex.at(58.134, 22.4);
        GeographicVertex v = GeographicVertex.at(58.134, 22.4);
        assertEquals(u,v);
    }

    @Test void semanticallyUnequalInstancesAreNotEqual() {
        GeographicVertex u = GeographicVertex.at(58.134, 22.4);
        GeographicVertex v = GeographicVertex.at(58.134, 12.4);
        assertNotEquals(u,v);
    }

    @Test void GeographicVertexIsNotEqualToNull() {
        GeographicVertex v = GeographicVertex.at(58.134, 12.4);
        assertNotEquals(v,null);
    }

    @Test void instanceOfDifferentClassIsNotEqual() {
        GeographicVertex v = GeographicVertex.at(75.0,90.0);
        Object o = new Object();
        assertNotEquals(v,o);
    }

    @Test void hashCodeOfEqualObjectsIsEqual() {
        GeographicVertex u = GeographicVertex.at(88.134, 25.4);
        GeographicVertex v = GeographicVertex.at(88.134, 25.4);
        assertEquals(u.hashCode(), v.hashCode());
    }
}

