package dev.moriamap.model.network;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.ArrayList;

class GeographicPositionTest {

    @Test void latitudeOfNullIslandIs0() {
        GeographicPosition sut = GeographicPosition.NULL_ISLAND;
        assertEquals(0.0, sut.getLatitude());
    }

    @Test void longitudeOfNullIslandIs0() {
        GeographicPosition sut = GeographicPosition.NULL_ISLAND;
        assertEquals(0.0, sut.getLongitude());
    }

    @Test void latitudeOfGPAtLatitude5Is5() {
        GeographicPosition sut = GeographicPosition.at(5.0, 0.0);
        assertEquals(5.0, sut.getLatitude());
    }

    @Test void longitudeOfGPAtLongitude42Is42() {
        GeographicPosition sut = GeographicPosition.at(18.39, 42.0);
        assertEquals(42.0, sut.getLongitude());
    }

    @Test void latitudeOf180DegreesThrowsIllegalArgumentException() {
        assertThrows(
          IllegalArgumentException.class,
          () -> GeographicPosition.at(180.0, 42.0)
        );
    }

    @Test void latitudeOfMinus91DegreesThrowsIllegalArgumentException() {
        assertThrows(
          IllegalArgumentException.class,
          () -> GeographicPosition.at(-91.5, 42.0)
        );
    }

    @Test void longitudeOfMinus181DegreesThrowsIllegalArgumentException() {
        assertThrows(
          IllegalArgumentException.class,
          () -> GeographicPosition.at(42.0, -181.0)
        );
    }

    @Test void longitudeOf281DegreesThrowsIllegalArgumentException() {
        assertThrows(
          IllegalArgumentException.class,
          () -> GeographicPosition.at(42.0, 281.4)
        );
    }

    @Test void cartesianCoordinatesOfNullIslandIsOnEarthAreRt_0_0(){
        GeographicPosition nullIsland = GeographicPosition.NULL_ISLAND;
        List<Double> res = new ArrayList<>();
        double radius = GeographicPosition.EARTH_RADIUS;
        res.add(radius);
        res.add(0.0);
        res.add(0.0);
        assertEquals(res, nullIsland.toCartesianAsList(radius));
    }

    @Test void cartesianCoordinatesOfNorthPoleOnEarthAre0_0_Rt() {
        GeographicPosition northPole = GeographicPosition.NORTH_POLE;
        List<Double> res = new ArrayList<>();
        double radius = GeographicPosition.EARTH_RADIUS;
        res.add(0.0);
        res.add(0.0);
        res.add(radius);
        assertEquals(res, northPole.toCartesianAsList(radius));
    }

    @Test void cartesianCoordinatesOfSouthPoleOnEarthAre0_0_MinusRt() {
        GeographicPosition southPole = GeographicPosition.SOUTH_POLE;
        List<Double> res = new ArrayList<>();
        double radius = GeographicPosition.EARTH_RADIUS;
        res.add(0.0);
        res.add(0.0);
        res.add(-radius);
        assertEquals(res, southPole.toCartesianAsList(radius));
    }

    @Test void negativeRadiusForCartesianCoordinatesThrowsException() {
        GeographicPosition point = GeographicPosition.at(-42.0, 142.0);
        assertThrows(
          IllegalArgumentException.class,
          () -> point.toCartesian(-0.0)
        );
    }

    @Test void euclideanDistanceBetweenSameGPIs0() {
        GeographicPosition point = GeographicPosition.at(-42.0, -42.0);
        double res = GeographicPosition.euclideanDistance(
          point,
          point,
          GeographicPosition.EARTH_RADIUS
        );
        assertEquals(0.0, res);
    }

    @Test void euclideanDistanceBetweenNorthAndSouthPoleIsTwiceEarthRadius() {
        GeographicPosition northPole = GeographicPosition.NORTH_POLE;
        GeographicPosition southPole = GeographicPosition.SOUTH_POLE;
        double distance = GeographicPosition.EARTH_RADIUS;
        assertEquals(
          2 * distance,
          GeographicPosition.euclideanDistance(northPole, southPole, distance)
        );
    }

    @Test void euclideanDistanceWithNegativeRadiusThrowsException() {
        GeographicPosition p1 = GeographicPosition.at(32.0, 42.0);
        GeographicPosition p2 = GeographicPosition.at(42.0, 15.0);
        assertThrows(
          IllegalArgumentException.class,
          () -> GeographicPosition.euclideanDistance(p1, p2, -0.0)
        );
    }

    @Test void distanceFromNorthPoleToSouthPoleIsTwiceTheEarthRadius() {
        GeographicPosition northPole = GeographicPosition.NORTH_POLE;
        GeographicPosition southPole = GeographicPosition.SOUTH_POLE;
        double distance = GeographicPosition.EARTH_RADIUS;
        assertEquals(2 * distance, northPole.distanceFrom(southPole));
    }

    @Test void sameGPInstancesAreEqual() {
        GeographicPosition pos = GeographicPosition.at(42.0, -32.0);
        assertEquals(pos,pos);
    }

    @Test void gpInstanceIsNotEqualToNull() {
        GeographicPosition pos = GeographicPosition.at(27.435, 59.235);
        assertNotEquals(null, pos);
    }

    @Test void sameGPAreEqual() {
        GeographicPosition p1 = GeographicPosition.at(43.234, 35.4853);
        GeographicPosition p2 = GeographicPosition.at(43.234, 35.4853);
        assertTrue(p1.equals(p2) && p2.equals(p1));
    }

    @Test void hashCodeOfEqualObjectIsEqual() {
        GeographicPosition p1 = GeographicPosition.at(43.352, 59.435);
        GeographicPosition p2 = GeographicPosition.at(43.352, 59.435);
        assertTrue(
          p1.hashCode() == p2.hashCode()
          && p1.equals(p2)
          && p2.equals(p1)
        );
    }

    @Test void differentLongitudeOfGPsMakesThemNotEqual() {
        GeographicPosition p1 = GeographicPosition.at(45.235, 139.3);
        GeographicPosition p2 = GeographicPosition.at(45.235, -24.355);
        assertTrue(!p1.equals(p2) && !p2.equals(p1));
    }

    @Test void differentLatitudeOfGPsMakesThemNotEqual() {
        GeographicPosition p1 = GeographicPosition.at(39.3, -24.355);
        GeographicPosition p2 = GeographicPosition.at(45.235, -24.355);
        assertTrue(!p1.equals(p2) && !p2.equals(p1));
    }

    @Test void readTwoStringSameFormatDecimal(){
        GeographicPosition gp = GeographicPosition.from("2.17", "45.2");

       assertTrue(gp.getLatitude() == 2.17 && gp.getLongitude() == 45.2);
    }

    @Test void readTwoStringSameFormatDegree(){
        GeographicPosition gp = GeographicPosition.from("2 12 45 N", "40 52 36 E");

        boolean isBetweenLatitude = (gp.getLatitude() > 2.1 &&  gp.getLatitude() < 2.3);
        boolean isBetweenLongitude = (gp.getLongitude() > 40.7 && gp.getLongitude() < 40.9);

        assertTrue(isBetweenLatitude && isBetweenLongitude);
    }

    @Test void readTwoStringDifferentFormat(){
        GeographicPosition gp = GeographicPosition.from("2.17", "40 52 36 E");

        boolean isBetweenLongitude = (gp.getLongitude() > 40.7 && gp.getLongitude() < 40.9);


        assertTrue(gp.getLatitude() == 2.17 && isBetweenLongitude);
    }

    @Test void fromReadNull(){
        assertThrows(
          IllegalArgumentException.class,
          () -> GeographicPosition.from(null,"2.25")
        );
    }

    @Test void fromWrongFormat(){
        assertThrows(
          IllegalArgumentException.class,
          () -> GeographicPosition.from("2.75 We","2.25")
        );
    }

}
