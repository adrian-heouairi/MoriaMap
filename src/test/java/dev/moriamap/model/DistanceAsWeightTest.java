package dev.moriamap.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.*;

class DistanceAsWeightTest {
    static class DummyVertex implements Vertex {}
    static class DummyEdge extends Edge {
        public DummyEdge(DummyVertex from, DummyVertex to) {super(from, to);}
    }

    @Test void distanceAsWeightOfEdgeDifferentThanTSThrowsException() {
        var v1 = new DummyVertex();
        var v2 = new DummyVertex();
        var edge = new DummyEdge(v1, v2);
        var sut = new DistanceAsWeight(TransportNetwork.empty());
        assertThrows(UnsupportedOperationException.class,
                     () -> sut.apply(34.0, edge));
    }

    @Test void dawOfNullEdgeThrowsException() {
        var sut = new DistanceAsWeight(TransportNetwork.empty());
        assertThrows(NullPointerException.class,
                     () -> sut.apply(42.0, null));
    }

    @Test void dawOfNullCurrentThrowsException() {
        var e = new DummyEdge(new DummyVertex(), new DummyVertex());
        var sut = new DistanceAsWeight(TransportNetwork.empty());
        assertThrows(NullPointerException.class,
                     () -> sut.apply(null, e));
    }

    @Test void dawOfEdgeReturnsTransportSegmentDistance() {
        var tn = TransportNetwork.empty();
        var s1 = Stop.from("stop1", GeographicPosition.NULL_ISLAND);
        var s2 = Stop.from("stop2", GeographicPosition.NORTH_POLE);
        var dist = 42.0;
        var ts = TransportSegment.from(s1, s2, "l", "v", Duration.ZERO, dist);

        Variant v = Variant.empty( "v", "l" );
        v.addTransportSegment( ts );
        v.addDeparture( LocalTime.MIN );
        Line l = Line.of( "l" );
        l.addVariant( v );
        tn.addLine( l );
        tn.addStop( s1 );
        tn.addStop( s2 );
        tn.addTransportSegment( ts );
        var sut = new DistanceAsWeight(tn);
        assertEquals(dist, sut.apply(0.0, ts));
    }
}
