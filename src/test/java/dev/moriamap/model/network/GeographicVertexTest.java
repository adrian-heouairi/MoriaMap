package dev.moriamap.model.network;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

class GeographicVertexTest {

  @Test
  void geoPosOfGeoVertexAtNorthPoleIsNorthPole() {
    GeographicVertex sut = GeographicVertex.at(GeographicPosition.NORTH_POLE);
    assertEquals(GeographicPosition.NORTH_POLE, sut.getGeographicPosition());
  }

  @Test
  void nullGeoPosOnGeoVertexCreationThrowsException() {
    assertThrows(IllegalArgumentException.class, () -> GeographicVertex.at(null));
  }

  @Test
  void equalsOnItselfReturnsTrue() {
    GeographicVertex v = GeographicVertex.at(58.134, 22.4);
    assertEquals(v, v);
  }

  @Test
  void semanticallyEqualInstancesAreEqual() {
    GeographicVertex u = GeographicVertex.at(58.134, 22.4);
    GeographicVertex v = GeographicVertex.at(58.134, 22.4);
    assertEquals(u, v);
  }

  @Test
  void semanticallyUnequalInstancesAreNotEqual() {
    GeographicVertex u = GeographicVertex.at(58.134, 22.4);
    GeographicVertex v = GeographicVertex.at(58.134, 12.4);
    assertNotEquals(u, v);
  }

  @Test
  void GeographicVertexIsNotEqualToNull() {
    GeographicVertex v = GeographicVertex.at(58.134, 12.4);
    assertNotEquals(null, v);
  }

  @Test
  void instanceOfDifferentClassIsNotEqual() {
    GeographicVertex v = GeographicVertex.at(75.0, 90.0);
    Object o = new Object();
    assertNotEquals(v, o);
  }

  @Test
  void hashCodeOfEqualObjectsIsEqual() {
    GeographicVertex u = GeographicVertex.at(88.134, 25.4);
    GeographicVertex v = GeographicVertex.at(88.134, 25.4);
    assertEquals(u.hashCode(), v.hashCode());
  }

  @Test
  void toStringReturns2digitPrecisionCoordinates() {
    GeographicVertex v = new GeographicVertex(GeographicPosition.at(12.4, 15.5));
    assertEquals("(12.40,15.50)", v.toString());
  }
}
