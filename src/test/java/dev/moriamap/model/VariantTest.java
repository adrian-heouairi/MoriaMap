package dev.moriamap.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalTime;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

class VariantTest {

    @Test void construction() {
        Variant v = Variant.empty("1", "14");
        assertEquals("1", v.getName());
    }

    @Test void emptyVariantWithNullLineThrowsException() {
        assertThrows(
            IllegalArgumentException.class,
            () -> Variant.empty("1", null)
        );
    }

    @Test void emptyVariantWithNullNameThrowsException() {
        assertThrows(
            IllegalArgumentException.class,
            () -> Variant.empty(null, "14")
        );
    }

    @Test void variantsHavingSameIdAndLinesWithSameReferencesAreEqual() {
        Variant v = Variant.empty("1","14");
        Variant v1 = Variant.empty("1","14");
        LocalTime t = LocalTime.of(0, 0);
        v.addDeparture(t);
        v1.addDeparture(t);
        assertEquals(v,v1);
    }

    @Test void variantsHavingSameIdAndSameLineNameAndSameTransportSegmentsAreEqual() {
        Variant v = Variant.empty("Variant 1","14");
        Variant v1 = Variant.empty("Variant 1","14");
        Stop s1 = Stop.from("s1",GeographicPosition.SOUTH_POLE);
        Stop s2 = Stop.from("s2",GeographicPosition.NORTH_POLE);
        TransportSegment ts = TransportSegment.from(s1, s2, "14","Variant 1", Duration.ZERO, 0.0);
        v.addTransportSegment(ts);
        v1.addTransportSegment(ts);
        assertEquals(v,v1);
    }

    @Test void variantsHavingSameIdAndSameLineNameAndDifferentTransportSegmentsAreNotEqual() {
        Variant v = Variant.empty("Variant 1","14");
        Variant v1 = Variant.empty("Variant 1","14");
        Stop s1 = Stop.from("s1",GeographicPosition.SOUTH_POLE);
        Stop s2 = Stop.from("s2",GeographicPosition.NORTH_POLE);
        TransportSegment ts1 = TransportSegment.from(s1, s2, "14","Variant 1", Duration.ZERO, 0.0);
        TransportSegment ts2 = TransportSegment.from(s1, s2, "14","Variant 1", Duration.ZERO, 1.0);
        v.addTransportSegment(ts1);
        v1.addTransportSegment(ts2);
        assertNotEquals(v,v1);
    }

    @Test void variantsHavingSameIdAndSameLineNameAndDifferentTransportSegmentsSizeAreNotEqual() {
        Variant v = Variant.empty("Variant 1","14");
        Variant v1 = Variant.empty("Variant 1","14");
        Stop s1 = Stop.from("s1",GeographicPosition.SOUTH_POLE);
        Stop s2 = Stop.from("s2",GeographicPosition.NORTH_POLE);
        TransportSegment ts1 = TransportSegment.from(s1, s2, "14","Variant 1", Duration.ZERO, 0.0);
        TransportSegment ts2 = TransportSegment.from(s1, s2, "14","Variant 1", Duration.ZERO, 1.0);
        v.addTransportSegment(ts1);
        v.addTransportSegment(ts2);
        v1.addTransportSegment(ts2);
        assertNotEquals(v,v1);
    }

    @Test void addANotConcordingTransportSegmentLineName(){
        Variant v = Variant.empty("Variant 1", "14");
        Stop s1 = Stop.from("s1",GeographicPosition.SOUTH_POLE);
        Stop s2 = Stop.from("s2",GeographicPosition.NORTH_POLE);
        TransportSegment ts = TransportSegment.from(s1, s2, "13","Variant 1", Duration.ZERO, 0.0);
        assertThrows(IllegalArgumentException.class,
                () -> v.addTransportSegment(ts));
    }
    @Test void addANotConcordingTransportSegmentVariantName(){
        Variant v = Variant.empty("Variant 1", "14");
        Stop s1 = Stop.from("s1",GeographicPosition.SOUTH_POLE);
        Stop s2 = Stop.from("s2",GeographicPosition.NORTH_POLE);
        TransportSegment ts = TransportSegment.from(s1, s2, "14","Variant 2", Duration.ZERO, 0.0);
        assertThrows(IllegalArgumentException.class,
                () -> v.addTransportSegment(ts));
    }


    @Test void variantsWithDifferentTrainDepartureSizeAreNotEqual(){
        Variant v = Variant.empty("1","14");
        Variant v1 = Variant.empty("1","14");
        LocalTime t1 = LocalTime.of(0, 0);
        LocalTime t2 = LocalTime.of(1, 0);
        v.addDeparture(t1);
        v.addDeparture(t2);
        v1.addDeparture(t2);
        assertNotEquals(v, v1);
    }

    @Test void variantsWithDifferentTrainDepartureAreNotEqual(){
        Variant v = Variant.empty("1","14");
        Variant v1 = Variant.empty("1","14");
        LocalTime t1 = LocalTime.of(0, 0);
        LocalTime t2 = LocalTime.of(1, 0);
        v.addDeparture(t1);
        v1.addDeparture(t2);
        assertNotEquals(v, v1);
    }

    @Test void variantIsEqualToItself() {
        Variant v = Variant.empty("1", "13");
        assertEquals(v, v);
    }

    @Test void variantInstanceIsNotEqualToNull() {
        Variant v = Variant.empty("1", "14");
        assertNotEquals(null, v);
    }

    @Test void objectIsNotEqualToVariant() {
        Object o = new Object();
        Variant v = Variant.empty("3","4");
        assertNotEquals(v,o);
    }

    @Test void variantsWithDifferentIdAndSameLineAreNotEqual(){
        Variant v = Variant.empty("2","4");
        Variant v1 = Variant.empty("4", "4");
        assertNotEquals(v,v1);
    }

    @Test void variantOfSameIdInDifferentLinesAreNotEqual() {
        Variant v = Variant.empty("2", "2");
        Variant v1 = Variant.empty("2", "3");
        assertNotEquals(v,v1);
    }

    @Test void variantHasNoTransportSegmentAtCreation(){
        Variant v = Variant.empty("1", "14");
        List<TransportSegment> ts = v.getTransportSegments();
        assertTrue(ts.isEmpty());
    }

    @Test void addANullTransportSegmentThrowsException(){
        Variant v = Variant.empty("1", "14");
        assertThrows(IllegalArgumentException.class,
                () -> v.addTransportSegment(null)
        );
    }

    @Test void addATransportSegmentToVariant(){
        Variant v = Variant.empty("Variant 1", "14");
        Stop s1 = Stop.from("s1",GeographicPosition.SOUTH_POLE);
        Stop s2 = Stop.from("s2",GeographicPosition.NORTH_POLE);
        TransportSegment ts = TransportSegment.from(s1, s2, "14","Variant 1", Duration.ZERO, 0.0);
        assertTrue(v.addTransportSegment(ts));
    }

    @Test void addingTwiceTheSameTransportSegmentReturnFalse(){
        Variant v = Variant.empty("Variant 1", "14");
        Stop s1 = Stop.from("s1",GeographicPosition.SOUTH_POLE);
        Stop s2 = Stop.from("s2",GeographicPosition.NORTH_POLE);
        TransportSegment ts = TransportSegment.from(s1, s2, "14","Variant 1", Duration.ZERO, 0.0);
        v.addTransportSegment(ts);
        assertFalse(v.addTransportSegment(ts));
    }

    @Test void addNullTimeToVariantThrowsException(){
        Variant v = Variant.empty("1", "14");
        assertThrows(IllegalArgumentException.class,
                () -> v.addDeparture(null)
        );
    }

    @Test void addingTwiceTheSameTimeToVariantReturnFalse(){
        Variant v = Variant.empty("1", "14");
        LocalTime t = LocalTime.of(0, 0);
        v.addDeparture(t);
        assertFalse(v.addDeparture(t));
    }

    @Test void getTransportSegments(){
        Variant v = Variant.empty("Variant 1", "14");
        Stop s1 = Stop.from("s1",GeographicPosition.SOUTH_POLE);
        Stop s2 = Stop.from("s2",GeographicPosition.NORTH_POLE);
        TransportSegment ts = TransportSegment.from(s1, s2, "14","Variant 1", Duration.ZERO, 0.0);
        List<TransportSegment> res = new ArrayList<TransportSegment>();
        v.addTransportSegment(ts);
        res.add(ts);
        assertEquals(res, v.getTransportSegments());
    }

    @Test void getLineName(){
        Variant v = Variant.empty("1", "14");
        assertEquals("14", v.getLineName());
    }

    @Test void getDepartures(){
        Variant v = Variant.empty("1", "14");
        LocalTime t = LocalTime.of(0, 0);
        List<LocalTime> res = new ArrayList<LocalTime>();
        res.add(t);
        v.addDeparture(t);
        assertEquals(res, v.getDepartures());
    }

    @Test void hashCodeOfSemanticallyEqualVariantsAreEqual(){
        Variant v1 = Variant.empty("Variant 1", "14");
        Variant v2 = Variant.empty("Variant 1", "14");
        Stop s1 = Stop.from("s1",GeographicPosition.SOUTH_POLE);
        Stop s2 = Stop.from("s2",GeographicPosition.NORTH_POLE);
        TransportSegment ts = TransportSegment.from(s1, s2, "14","Variant 1", Duration.ZERO, 0.0);
        v1.addTransportSegment(ts);
        v2.addTransportSegment(ts);
        LocalTime t = LocalTime.of(0, 0);
        v1.addDeparture(t);
        v2.addDeparture(t);
        assertEquals(v1.hashCode(), v2.hashCode());
    }

    private Variant newVariantHelper(){
        Variant v = Variant.empty("Variant 1", "14");
        Stop s1 = Stop.from("s1",GeographicPosition.SOUTH_POLE);
        Stop s2 = Stop.from("s2",GeographicPosition.NORTH_POLE);
        Stop s3 = Stop.from("s3",GeographicPosition.SOUTH_POLE);
        Stop s4 = Stop.from("s4",GeographicPosition.SOUTH_POLE);
        Stop s5 = Stop.from("s5",GeographicPosition.SOUTH_POLE);
        Stop s6 = Stop.from("s6",GeographicPosition.SOUTH_POLE);
        TransportSegment ts1 = TransportSegment.from(s1, s2, "14","Variant 1", Duration.ZERO, 0.0);
        TransportSegment ts2 = TransportSegment.from(s2, s3, "14","Variant 1", Duration.ZERO, 0.0);
        TransportSegment ts3 = TransportSegment.from(s3, s4, "14","Variant 1", Duration.ZERO, 0.0);
        TransportSegment ts4 = TransportSegment.from(s4, s5, "14","Variant 1", Duration.ZERO, 0.0);
        TransportSegment ts5 = TransportSegment.from(s5, s6, "14","Variant 1", Duration.ZERO, 0.0);
        v.addTransportSegment(ts2);
        v.addTransportSegment(ts4);
        v.addTransportSegment(ts5);
        v.addTransportSegment(ts3);
        v.addTransportSegment(ts1);
        return v;
    }
    @Test void getStart(){
        Variant v = newVariantHelper();
        assertEquals("s1",v.getStart().getName());
    }

    @Test void getEnd(){
        Variant v = newVariantHelper();
        assertEquals("s6",v.getEnd().getName());
    }

    @Test void hasStopThrowsExceptionWithNullArgument() {
        Variant sut = newVariantHelper();
        assertThrows(
          IllegalArgumentException.class,
          () -> sut.hasStop(null)
        );
    }

    @Test void hasStopReturnsFalseWhenStopIsAbsent() {
        Variant sut = newVariantHelper();
        Stop stop = Stop.from("Saint-Lazare", GeographicPosition.NULL_ISLAND);
        assertFalse(sut.hasStop(stop));
    }

    @Test void hasStopReturnsTrueWhenStopIsPresent() {
        Variant sut = Variant.empty("A4", "Papier 80g");
        Stop s1 = Stop.from("Clairefontaine", GeographicPosition.NULL_ISLAND);
        Stop s2 = Stop.from("Otail", GeographicPosition.NORTH_POLE);
        TransportSegment ts = TransportSegment.from(
          s1,
          s2,
          "Papier 80g",
          "A4",
          Duration.ZERO,
          45.0 // FTL
        );
        sut.addTransportSegment(ts);
        assertTrue(sut.hasStop(s2));
    }

    @Test void hasOutgoingSegmentThrowsExceptionWhenStopIsNull() {
        Variant sut = Variant.empty("Asus", "Acer");
        assertThrows(
          IllegalArgumentException.class,
          () -> sut.hasOutgoingSegment(null)
        );
    }

    @Test void hasOutgoingSegmentReturnsFalseWhenStopIsAbsent() {
        Variant sut = Variant.empty("Apple", "Microsoft");
        Stop stop = Stop.from("arrêt", GeographicPosition.NULL_ISLAND);
        assertFalse(sut.hasOutgoingSegment(stop));
    }

    @Test void hasOutgoingSegmentReturnsTrueWhenStopHasOutgoingSegment() {
        Variant sut = newVariantHelper();
        Stop stop = Stop.from("s5", GeographicPosition.SOUTH_POLE);
        assertTrue(sut.hasOutgoingSegment(stop));
    }

    @Test void getOutgoingSegmentThrowsExceptionWhenStopIsNull() {
        Variant sut = newVariantHelper();
        assertThrows(
            IllegalArgumentException.class,
            () -> sut.getOutgoingSegment(null)
        );
    }

    @Test void getOutgoingSegmentThrowsExceptionWhenStopHasNoOutgoingEdge() {
        Variant sut = newVariantHelper();
        Stop dummy = Stop.from("Null Island", GeographicPosition.NULL_ISLAND);
        assertThrows(
            NoSuchElementException.class,
            () -> sut.getOutgoingSegment(dummy)
        );
    }

    @Test void getOutgoingSegmentReturnsOutgoingSegment() {
        Variant sut = newVariantHelper();
        Stop s1 = Stop.from("South Pole", GeographicPosition.SOUTH_POLE);
        Stop s2 = Stop.from("North Pole", GeographicPosition.NORTH_POLE);
        TransportSegment ts = TransportSegment.from(
            s1,
            s2,
            "14",
            "Variant 1",
            Duration.ZERO,
            0.0
        );
        sut.addTransportSegment(ts);
        assertEquals(ts, sut.getOutgoingSegment(s1));
    }

    @Test void travelTimeWithNullArgumentThrowsException() {
        Variant sut = Variant.empty("v", "l");
        assertThrows(
            IllegalArgumentException.class,
            () -> sut.getTravelTimeTo(null)
        );
    }

    @Test void travelTimeWithoutOutgoingSegmentThrowsException() {
        Variant sut = Variant.empty("variant", "ligne");
        Stop start = Stop.from("stop1", GeographicPosition.at(32, 42));
        Stop s1 = Stop.from("stop2", GeographicPosition.at(45, 23));
        Stop s2 = Stop.from("stop3", GeographicPosition.at(59, 16));
        Stop end = Stop.from("stop4", GeographicPosition.at(25, 20));
        TransportSegment ts1 = TransportSegment.from(
            start,
            s1,
            "ligne",
            "variant",
            Duration.ofMinutes(42),
            42.0
        );
        TransportSegment ts2 = TransportSegment.from(
            s2,
            end,
            "ligne",
            "variant",
            Duration.ofMinutes(42),
            41.99
        );
        sut.addTransportSegment(ts1);
        sut.addTransportSegment(ts2);
        assertThrows(
            NoSuchElementException.class,
            () -> sut.getTravelTimeTo(end)
        );
    }

    @Test void travelTimeOfVariantWithOneSegmentEqualsDurationOfSegment() {
        Variant sut = Variant.empty("variant", "ligne");
        Stop from = Stop.from("from", GeographicPosition.at(32, 42));
        Stop to = Stop.from("to", GeographicPosition.at(45, 23));
        TransportSegment ts = TransportSegment.from(
            from,
            to,
            "ligne",
            "variant",
            Duration.ofMinutes(42),
            42.0
        );
        sut.addTransportSegment(ts);
        assertEquals(ts.getTravelDuration(), sut.getTravelTimeTo(to));
    }

    @Test void travelTimeOfVariantFromStartToEndEqualsSumOfSegmentDurations() {
        Variant sut = Variant.empty("v", "l");
        Stop s1 = Stop.from("d", GeographicPosition.at(32, 42));
        Stop s2 = Stop.from("i", GeographicPosition.at(65, 123));
        Stop s3 = Stop.from("j", GeographicPosition.at(20, 54));
        Stop s4 = Stop.from("k", GeographicPosition.at(19, 77));
        Stop s5 = Stop.from("s", GeographicPosition.at(32, 45));
        Stop s6 = Stop.from("t", GeographicPosition.at(14, 15));
        Stop s7 = Stop.from("r", GeographicPosition.at(45, 26));
        Stop s8 = Stop.from("a", GeographicPosition.at(44, 63));
        TransportSegment ts1 = TransportSegment.from(s1, s2, "l", "v", Duration.ofMinutes(1), 42.0);
        TransportSegment ts2 = TransportSegment.from(s2, s3, "l", "v", Duration.ofMinutes(2), 42.0);
        TransportSegment ts3 = TransportSegment.from(s3, s4, "l", "v", Duration.ofMinutes(3), 42.0);
        TransportSegment ts4 = TransportSegment.from(s4, s5, "l", "v", Duration.ofMinutes(4), 42.0);
        TransportSegment ts5 = TransportSegment.from(s5, s6, "l", "v", Duration.ofMinutes(5), 42.0);
        TransportSegment ts6 = TransportSegment.from(s6, s7, "l", "v", Duration.ofMinutes(6), 42.0);
        TransportSegment ts7 = TransportSegment.from(s7, s8, "l", "v", Duration.ofMinutes(7), 42.0);
        sut.addTransportSegment(ts1);
        sut.addTransportSegment(ts2);
        sut.addTransportSegment(ts3);
        sut.addTransportSegment(ts4);
        sut.addTransportSegment(ts5);
        sut.addTransportSegment(ts6);
        sut.addTransportSegment(ts7);
        assertEquals(Duration.ofMinutes(28), sut.getTravelTimeTo(s8));
    }
}