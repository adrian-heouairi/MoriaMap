package dev.moriamap.model.network;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;

class TransportSegmentTest {

  @Test
  void testConstruction() {
    Stop s1 = Stop.from("s1", GeographicPosition.SOUTH_POLE);
    Stop s2 = Stop.from("s2", GeographicPosition.NORTH_POLE);
    TransportSegment ts = TransportSegment.from(s1, s2, "14", "Variant 1", Duration.ZERO, 0.0);
    assertEquals(0.0, ts.getDistance());
  }

  @Test
  void TransportSegmentWithNullLineNameThrowsException() {
    Stop s1 = Stop.from("s1", GeographicPosition.SOUTH_POLE);
    Stop s2 = Stop.from("s2", GeographicPosition.NORTH_POLE);
    assertThrows(
        IllegalArgumentException.class,
        () -> TransportSegment.from(s1, s2, null, "Variant 1", Duration.ZERO, 0));
  }

  @Test
  void TransportSegmentWithNullVariantNameThrowsException() {
    Stop s1 = Stop.from("s1", GeographicPosition.SOUTH_POLE);
    Stop s2 = Stop.from("s2", GeographicPosition.NORTH_POLE);
    assertThrows(
        IllegalArgumentException.class,
        () -> TransportSegment.from(s1, s2, "14", null, Duration.ZERO, 0));
  }

  @Test
  void TransportSegmentWithNullDurationThrowsException() {
    Stop s1 = Stop.from("s1", GeographicPosition.SOUTH_POLE);
    Stop s2 = Stop.from("s2", GeographicPosition.NORTH_POLE);
    assertThrows(
        IllegalArgumentException.class,
        () -> TransportSegment.from(s1, s2, "14", "Variant 1", null, 0));
  }

  @Test
  void testTransportSegmentNotEqualToNull() {
    Stop s1 = Stop.from("s1", GeographicPosition.SOUTH_POLE);
    Stop s2 = Stop.from("s2", GeographicPosition.NORTH_POLE);
    TransportSegment ts = TransportSegment.from(s1, s2, "14", "Variant 1", Duration.ZERO, 0.0);
    assertNotEquals(null, ts);
  }

  @Test
  void testObjectIsNotEqualToTransportSegment() {
    Object o = new Object();
    Stop s1 = Stop.from("s1", GeographicPosition.SOUTH_POLE);
    Stop s2 = Stop.from("s2", GeographicPosition.NORTH_POLE);
    TransportSegment ts = TransportSegment.from(s1, s2, "14", "Variant 1", Duration.ZERO, 0.0);
    assertNotEquals(ts, o);
  }

  @Test
  void testEqualsOnItselfReturnsTrue() {
    Stop s1 = Stop.from("s1", GeographicPosition.SOUTH_POLE);
    Stop s2 = Stop.from("s2", GeographicPosition.NORTH_POLE);
    TransportSegment ts = TransportSegment.from(s1, s2, "14", "Variant 1", Duration.ZERO, 0.0);
    assertEquals(ts, ts);
  }

  @Test
  void testEqualsOnATransportSegmentWithTheSameValuesReturnsTrue() {
    Stop s1 = Stop.from("s1", GeographicPosition.SOUTH_POLE);
    Stop s2 = Stop.from("s2", GeographicPosition.NORTH_POLE);
    TransportSegment ts1 = TransportSegment.from(s1, s2, "14", "Variant 1", Duration.ZERO, 0.0);
    TransportSegment ts2 = TransportSegment.from(s1, s2, "14", "Variant 1", Duration.ZERO, 0.0);
    assertEquals(ts1, ts2);
  }

  @Test
  void testEqualsOnATransportSegmentWithDifferentDistanceReturnsFalse() {
    Stop s1 = Stop.from("s1", GeographicPosition.SOUTH_POLE);
    Stop s2 = Stop.from("s2", GeographicPosition.NORTH_POLE);
    TransportSegment ts1 = TransportSegment.from(s1, s2, "14", "Variant 1", Duration.ZERO, 1.0);
    TransportSegment ts2 = TransportSegment.from(s1, s2, "14", "Variant 1", Duration.ZERO, 0.0);
    assertNotEquals(ts1, ts2);
  }

  @Test
  void testEqualsOnATransportSegmentWithDifferentDurationReturnsFalse() {
    Stop s1 = Stop.from("s1", GeographicPosition.SOUTH_POLE);
    Stop s2 = Stop.from("s2", GeographicPosition.NORTH_POLE);
    TransportSegment ts1 = TransportSegment.from(s1, s2, "14", "Variant 1", Duration.ZERO, 0.0);
    TransportSegment ts2 =
        TransportSegment.from(
            s1, s2, "14", "Variant 1", Duration.between(LocalTime.NOON, LocalTime.MIDNIGHT), 0.0);
    assertNotEquals(ts1, ts2);
  }

  @Test
  void TransportSegmentWithDifferentToAreNotEquals() {
    Stop s1 = Stop.from("s1", GeographicPosition.SOUTH_POLE);
    Stop s2 = Stop.from("s2", GeographicPosition.NORTH_POLE);
    Stop s3 = Stop.from("s3", GeographicPosition.NORTH_POLE);
    TransportSegment ts1 = TransportSegment.from(s1, s2, "14", "Variant 1", Duration.ZERO, 0.0);
    TransportSegment ts2 = TransportSegment.from(s1, s3, "14", "Variant 1", Duration.ZERO, 0.0);
    assertNotEquals(ts1, ts2);
  }

  @Test
  void TransportSegmentWithDifferentFromAreNotEquals() {
    Stop s1 = Stop.from("s1", GeographicPosition.SOUTH_POLE);
    Stop s2 = Stop.from("s2", GeographicPosition.NORTH_POLE);
    Stop s3 = Stop.from("s3", GeographicPosition.NORTH_POLE);
    TransportSegment ts1 = TransportSegment.from(s1, s2, "14", "Variant 1", Duration.ZERO, 0.0);
    TransportSegment ts2 = TransportSegment.from(s3, s2, "14", "Variant 1", Duration.ZERO, 0.0);
    assertNotEquals(ts1, ts2);
  }

  @Test
  void testGetFrom() {
    Stop s1 = Stop.from("s1", GeographicPosition.SOUTH_POLE);
    Stop s2 = Stop.from("s2", GeographicPosition.NORTH_POLE);
    TransportSegment ts = TransportSegment.from(s1, s2, "14", "Variant 1", Duration.ZERO, 1.0);
    assertEquals(s1, ts.getFrom());
  }

  @Test
  void testGetTo() {
    Stop s1 = Stop.from("s1", GeographicPosition.SOUTH_POLE);
    Stop s2 = Stop.from("s2", GeographicPosition.NORTH_POLE);
    TransportSegment ts = TransportSegment.from(s1, s2, "14", "Variant 1", Duration.ZERO, 0.0);
    assertEquals(s2, ts.getTo());
  }

  @Test
  void hashCodeOfSemanticallyEqualTransportSegmentsAreEqual() {
    Stop s1 = Stop.from("s1", GeographicPosition.SOUTH_POLE);
    Stop s2 = Stop.from("s2", GeographicPosition.NORTH_POLE);
    TransportSegment ts1 = TransportSegment.from(s1, s2, "14", "Variant 1", Duration.ZERO, 0.0);
    TransportSegment ts2 = TransportSegment.from(s1, s2, "14", "Variant 1", Duration.ZERO, 0.0);
    assertEquals(ts1.hashCode(), ts2.hashCode());
  }

  @Test
  void getLineName() {
    Stop s1 = Stop.from("s1", GeographicPosition.SOUTH_POLE);
    Stop s2 = Stop.from("s2", GeographicPosition.NORTH_POLE);
    TransportSegment ts = TransportSegment.from(s1, s2, "14", "Variant 1", Duration.ZERO, 0.0);
    assertEquals("14", ts.getLineName());
  }

  @Test
  void getVariantName() {
    Stop s1 = Stop.from("s1", GeographicPosition.SOUTH_POLE);
    Stop s2 = Stop.from("s2", GeographicPosition.NORTH_POLE);
    TransportSegment ts = TransportSegment.from(s1, s2, "14", "Variant 1", Duration.ZERO, 0.0);
    assertEquals("Variant 1", ts.getVariantName());
  }

  @Test
  void getTravelDuration() {
    Stop s1 = Stop.from("s1", GeographicPosition.SOUTH_POLE);
    Stop s2 = Stop.from("s2", GeographicPosition.NORTH_POLE);
    TransportSegment ts = TransportSegment.from(s1, s2, "14", "Variant 1", Duration.ZERO, 0.0);
    assertEquals(Duration.ZERO, ts.getTravelDuration());
  }
}
