package dev.moriamap.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

class VariantTest {

    @Test void testConstruction() {
        Variant v = Variant.empty(1, "14");
        assertEquals(1, v.id);
    }

    @Test void testEmptyVariantWithNegativeIdThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> Variant.empty(-1,"3"));
    }

    @Test void testEmptyVariantWithNullLineThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> Variant.empty(1, null)
        );
    }

    @Test void variantsHavingSameIdAndLinesWithSameReferencesAreEqual() {
        Variant v = Variant.empty(1,"14");
        Variant v1 = Variant.empty(1,"14");
        assertEquals(v,v1);
    }

    @Test void variantsHavingSameIdAndSameLineNameAndSameTransportSegmentsAreEqual() {
        Variant v = Variant.empty(1,"14");
        Variant v1 = Variant.empty(1,"14");
        Stop s1 = Stop.from("s1",GeographicPosition.SOUTH_POLE);
        Stop s2 = Stop.from("s2",GeographicPosition.NORTH_POLE);
        TransportSegment ts = TransportSegment.from(s1, s2, "14 Variant 1", Duration.ZERO, 0.0);
        v.addTransportSegments(ts);
        v1.addTransportSegments(ts);
        assertEquals(v,v1);
    }

    @Test void variantsHavingSameIdAndSameLineNameAndDifferentTransportSegmentsAreNotEqual() {
        Variant v = Variant.empty(1,"14");
        Variant v1 = Variant.empty(1,"14");
        Stop s1 = Stop.from("s1",GeographicPosition.SOUTH_POLE);
        Stop s2 = Stop.from("s2",GeographicPosition.NORTH_POLE);
        TransportSegment ts1 = TransportSegment.from(s1, s2, "14 Variant 1", Duration.ZERO, 0.0);
        TransportSegment ts2 = TransportSegment.from(s1, s2, "14 Variant 2", Duration.ZERO, 0.0);
        v.addTransportSegments(ts1);
        v1.addTransportSegments(ts2);
        assertNotEquals(v,v1);
    }

    @Test void variantsHavingSameIdAndSameLineNameAndDifferentTransportSegmentsSizeAreNotEqual() {
        Variant v = Variant.empty(1,"14");
        Variant v1 = Variant.empty(1,"14");
        Stop s1 = Stop.from("s1",GeographicPosition.SOUTH_POLE);
        Stop s2 = Stop.from("s2",GeographicPosition.NORTH_POLE);
        TransportSegment ts1 = TransportSegment.from(s1, s2, "14 Variant 1", Duration.ZERO, 0.0);
        TransportSegment ts2 = TransportSegment.from(s1, s2, "14 Variant 2", Duration.ZERO, 0.0);
        v.addTransportSegments(ts1);
        v.addTransportSegments(ts2);
        v1.addTransportSegments(ts2);
        assertNotEquals(v,v1);
    }

    @Test void testVariantIsEqualToItself() {
        Variant v = Variant.empty(1, "13");
        assertEquals(v, v);
    }

    @Test void testVariantInstanceIsNotEqualToNull() {
        Variant v = Variant.empty(1, "14");
        assertNotEquals(v, null);
    }

    @Test void testObjectIsNotEqualToVariant() {
        Object o = new Object();
        Variant v = Variant.empty(3,"4");
        assertNotEquals(v,o);
    }

    @Test void testVariantsWithDifferentIdAndSameLineAreNotEqual(){
        Variant v = Variant.empty(2,"4");
        Variant v1 = Variant.empty(4, "4");
        assertNotEquals(v,v1);
    }

    @Test void testVariantOfSameIdInDifferentLinesAreNotEqual() {
        Variant v = Variant.empty(2, "2");
        Variant v1 = Variant.empty(2, "3");
        assertNotEquals(v,v1);
    }

    @Test void testVariantHasNoTransportSegmentAtCreation(){
        Variant v = Variant.empty(1, "14");
        List<TransportSegment> ts = v.getTransportSegments();
        assertTrue(ts.isEmpty());
    }

    @Test void testAddANullTansportSegmentThrowsException(){
        Variant v = Variant.empty(1, "14");
        assertThrows(IllegalArgumentException.class,
                () -> v.addTransportSegments(null)
        );
    }

    @Test void testAddATransportSegmentToVariant(){
        Variant v = Variant.empty(1, "14");
        Stop s1 = Stop.from("s1",GeographicPosition.SOUTH_POLE);
        Stop s2 = Stop.from("s2",GeographicPosition.NORTH_POLE);
        TransportSegment ts = TransportSegment.from(s1, s2, "14 Variant 1", Duration.ZERO, 0.0);
        assertTrue(v.addTransportSegments(ts));
    }

    @Test void testAddingTwiceTheSameTransportSegmentReturnFalse(){
        Variant v = Variant.empty(1, "14");
        Stop s1 = Stop.from("s1",GeographicPosition.SOUTH_POLE);
        Stop s2 = Stop.from("s2",GeographicPosition.NORTH_POLE);
        TransportSegment ts = TransportSegment.from(s1, s2, "14 Variant 1", Duration.ZERO, 0.0);
        v.addTransportSegments(ts);
        assertFalse(v.addTransportSegments(ts));
    }

    @Test void testGetTransportSegments(){
        Variant v = Variant.empty(1, "14");
        Stop s1 = Stop.from("s1",GeographicPosition.SOUTH_POLE);
        Stop s2 = Stop.from("s2",GeographicPosition.NORTH_POLE);
        TransportSegment ts = TransportSegment.from(s1, s2, "14 Variant 1", Duration.ZERO, 0.0);
        List<TransportSegment> res = new ArrayList<TransportSegment>();
        v.addTransportSegments(ts);
        res.add(ts);
        assertEquals(res, v.getTransportSegments());
    }

    @Test void hashCodeOfSemanticallyEqualVariantsAreEqual(){
        Variant v1 = Variant.empty(1, "14");
        Variant v2 = Variant.empty(1, "14");
        Stop s1 = Stop.from("s1",GeographicPosition.SOUTH_POLE);
        Stop s2 = Stop.from("s2",GeographicPosition.NORTH_POLE);
        TransportSegment ts = TransportSegment.from(s1, s2, "14 Variant 1", Duration.ZERO, 0.0);
        v1.addTransportSegments(ts);
        v2.addTransportSegments(ts);
        assertEquals(v1.hashCode(), v2.hashCode());
    }

}
