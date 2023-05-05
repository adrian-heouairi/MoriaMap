package dev.moriamap.model.network;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import org.junit.jupiter.api.Test;

class WalkSegmentTest {

  @Test
  void walkSegmentHasWalkSpeed() {
    assertEquals(2, WalkSegment.WALK_SPEED);
  }

  @Test
  void walkSegmentHasDrudgery() {
    assertEquals(10.0, WalkSegment.WALK_DRUDGERY);
  }

  @Test
  void distanceOfWalkSegmentBetweenNorthAndSouthPoleIsTwiceEarthRad() {
    var gv1 = GeographicVertex.at(GeographicPosition.NORTH_POLE);
    var gv2 = GeographicVertex.at(GeographicPosition.SOUTH_POLE);
    var sut = new WalkSegment(gv1, gv2);
    assertEquals(2.0 * GeographicPosition.EARTH_RADIUS, sut.distance);
  }

  @Test
  void travelTimeOfWalkSegmentBetweenNorthAndSouthPole() {
    var gv1 = GeographicVertex.at(GeographicPosition.NORTH_POLE);
    var gv2 = GeographicVertex.at(GeographicPosition.SOUTH_POLE);
    var sut = new WalkSegment(gv1, gv2);
    Duration real = Duration.ofHours(6371);
    assertEquals(real, sut.travelTime());
  }
}
