package dev.moriamap.model.network.traversal;

import org.junit.jupiter.api.Test;

import dev.moriamap.model.network.Edge;
import dev.moriamap.model.network.GeographicPosition;
import dev.moriamap.model.network.GeographicVertex;
import dev.moriamap.model.network.Line;
import dev.moriamap.model.network.Stop;
import dev.moriamap.model.network.TransportNetwork;
import dev.moriamap.model.network.TransportSegment;
import dev.moriamap.model.network.Variant;
import dev.moriamap.model.network.Vertex;
import dev.moriamap.model.network.WalkSegment;

import java.time.Duration;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @Test void dawOfWalkSegmentReturnsDistanceTimesDrudgery() {
        var sut = new DistanceAsWeight(TransportNetwork.empty());
        var gv1 = GeographicVertex.at(GeographicPosition.NORTH_POLE);
        var gv2 = GeographicVertex.at(GeographicPosition.SOUTH_POLE);
        var ws = new WalkSegment(gv1, gv2);
        assertEquals(
                ws.distance * WalkSegment.WALK_DRUDGERY / 1000.0,
                sut.apply(0.0, ws)
        );
    }
}
