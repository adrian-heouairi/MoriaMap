package dev.moriamap.model;

import org.junit.jupiter.api.Test;

import dev.moriamap.model.GraphTest.DummyVertex;
import dev.moriamap.model.GraphTest.DummyEdge;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class TransportNetworkTest {
    @Test
    void getStopFromPositionThrowsException() {
        TransportNetwork tn = TransportNetwork.empty();
        assertThrows(
                IllegalArgumentException.class,
                () -> tn.getStopFromPosition(null)
        );
    }

    @Test
    void getStopByNameThrowsException() {
        TransportNetwork tn = TransportNetwork.empty();
        assertThrows(
                IllegalArgumentException.class,
                () -> tn.getStopByName(null)
        );
    }

    @Test
    void addLineThrowsException() {
        TransportNetwork tn = TransportNetwork.empty();
        assertThrows(
                IllegalArgumentException.class,
                () -> tn.addLine(null)
        );
    }

    @Test
    void findStopThrowsException() {
        TransportNetwork tn = TransportNetwork.empty();
        assertThrows(
                IllegalArgumentException.class,
                () -> tn.findStop(null)
        );
    }

    @Test
    void findLineThrowsException() {
        TransportNetwork tn = TransportNetwork.empty();
        assertThrows(
                IllegalArgumentException.class,
                () -> tn.findLine(null)
        );
    }

    private TransportNetwork newTransportNetworkHelper() {
        Stop s1 = Stop.from("s1", GeographicPosition.SOUTH_POLE);
        Stop s2 = Stop.from("s2", GeographicPosition.NORTH_POLE);

        TransportSegment ts1 = TransportSegment.from(s1, s2, "7B", "1",
                Duration.ofMinutes( 3 ), 4);
        TransportSegment ts2 = TransportSegment.from(s2, s1, "7B", "2",
                Duration.ofMinutes( 4 ), 4);

        Variant v1 = Variant.empty("1", "7B");
        v1.addTransportSegment(ts1);
        Variant v2 = Variant.empty("2", "7B");
        v2.addTransportSegment(ts2);

        Line l = Line.of("7B");
        l.addVariant(v1);
        l.addVariant(v2);

        TransportNetwork tn = TransportNetwork.empty();
        tn.addStop(s1);
        tn.addStop(s2);
        tn.addTransportSegment(ts1);
        tn.addTransportSegment(ts2);
        tn.addLine(l);

        return tn;
    }
    @Test
    void getStopFromPositionSucceeds() {
        TransportNetwork tn = newTransportNetworkHelper();
        Stop s = tn.getStopFromPosition(GeographicPosition.SOUTH_POLE);

        Stop s1 = Stop.from("s1", GeographicPosition.SOUTH_POLE);

        assertEquals(s1, s);
    }

    @Test
    void getStopByNameSucceeds() {
        TransportNetwork tn = newTransportNetworkHelper();
        Stop s = tn.getStopByName("s1");

        Stop s1 = Stop.from("s1", GeographicPosition.SOUTH_POLE);

        assertEquals(s1, s);
    }

    @Test
    void getStopByAnInexactName() {
        TransportNetwork tn = TransportNetwork.empty();
        Stop s1 = Stop.from("Lourmel",GeographicPosition.SOUTH_POLE);
        Stop s2 = Stop.from("Châtelet",GeographicPosition.SOUTH_POLE);
        Stop s3 = Stop.from("Hoche",GeographicPosition.SOUTH_POLE);
        Stop s4 = Stop.from("Pyramide",GeographicPosition.SOUTH_POLE);
        tn.addStop(s1);
        tn.addStop(s2);
        tn.addStop(s3);
        tn.addStop(s4);
        Stop s5 = tn.getStopByInexactName("Hochet");
        assertEquals(s3,s5);
    }

    @Test
    void getStopsByAnInexactName() {
        TransportNetwork tn = TransportNetwork.empty();
        Stop s1 = Stop.from("Thibault",GeographicPosition.SOUTH_POLE);
        Stop s2 = Stop.from("Johnny",GeographicPosition.SOUTH_POLE);
        Stop s3 = Stop.from("Bernard",GeographicPosition.SOUTH_POLE);
        Stop s4 = Stop.from("Joan",GeographicPosition.SOUTH_POLE);
        tn.addStop(s1);
        tn.addStop(s2);
        tn.addStop(s3);
        tn.addStop(s4);
        List<Stop> expected = new ArrayList<>();
        expected.add(s4);
        expected.add(s2);
        assertEquals(expected,tn.getNearestStopsByInexactName("John", 2));
    }

    @Test
    void getStopByATooFarInexactNameReturnsNull() {
        TransportNetwork tn = TransportNetwork.empty();
        Stop s1 = Stop.from("Châtelet",GeographicPosition.SOUTH_POLE);
        tn.addStop(s1);
        Stop s5 = tn.getStopByInexactName("Chatelet-les-halles");
        assertNull(s5);

    }

    @Test
    void getStopFromPositionFails() {
        TransportNetwork tn = newTransportNetworkHelper();
        Stop s = tn.getStopFromPosition(GeographicPosition.NULL_ISLAND);
        assertNull(s);
    }

    @Test
    void getStopByNameFails() {
        TransportNetwork tn = newTransportNetworkHelper();
        Stop s = tn.getStopByName("Dijkstra");
        assertNull(s);
    }

    @Test
    void getLines() {
        TransportNetwork tn = newTransportNetworkHelper();
        List<Line> l = tn.getLines();
        assertEquals(1, l.size());
    }

    @Test
    void getVariants() {
        TransportNetwork tn = newTransportNetworkHelper();
        List<Variant> v = tn.getVariants();
        assertEquals(2, v.size());
    }

    @Test
    void getStops() {
        TransportNetwork tn = newTransportNetworkHelper();
        List<Stop> s = tn.getStops();
        assertEquals(2, s.size());
    }

    @Test
    void getTransportSegments() {
        TransportNetwork tn = newTransportNetworkHelper();
        List<TransportSegment> t = tn.getTransportSegments();
        assertEquals(2, t.size());
    }

    @Test
    void addLineReturnsFalseOnDuplicateAdd() {
        Stop s1 = Stop.from("s1", GeographicPosition.SOUTH_POLE);
        Stop s2 = Stop.from("s2", GeographicPosition.NORTH_POLE);
        TransportSegment ts1 = TransportSegment.from(s1, s2, "7B", "1",
                Duration.ofMinutes( 3 ), 4);
        TransportSegment ts2 = TransportSegment.from(s2, s1, "7B", "2",
                Duration.ofMinutes( 4 ), 4);
        Variant v1 = Variant.empty("1", "7B");
        v1.addTransportSegment(ts1);
        Variant v2 = Variant.empty("2", "7B");
        v2.addTransportSegment(ts2);
        Line l = Line.of("7B");
        l.addVariant(v1);
        l.addVariant(v2);

        TransportNetwork tn = newTransportNetworkHelper();

        assertFalse(tn.addLine(l));
    }

    @Test
    void addLineSucceeds() {
        TransportNetwork tn = newTransportNetworkHelper();
        Line l = Line.of("8");
        assertTrue(tn.addLine(l));
    }

    @Test
    void findStopSucceeds() {
        TransportNetwork tn = newTransportNetworkHelper();
        Stop s1 = Stop.from("s1", GeographicPosition.SOUTH_POLE);
        Stop s = tn.findStop(s1);
        assertEquals(s1, s);
    }

    @Test
    void findStopFails() {
        TransportNetwork tn = newTransportNetworkHelper();
        Stop s1 = Stop.from("Clairefontaine", GeographicPosition.NULL_ISLAND);
        Stop s = tn.findStop(s1);
        assertNull(s);
    }

    @Test
    void findLineSucceeds() {
        TransportNetwork tn = newTransportNetworkHelper();
        Line l = tn.findLine("7B");
        assertNotNull(l);
    }

    @Test
    void findLineFails() {
        TransportNetwork tn = newTransportNetworkHelper();
        Line l = tn.findLine("3");
        assertNull(l);
    }

    @Test
    void addStop() {
        TransportNetwork tn = TransportNetwork.empty();
        Stop s1 = Stop.from("s1", GeographicPosition.SOUTH_POLE);
        tn.addStop(s1);
        assertTrue(tn.getStops().contains(s1));
    }

    @Test
    void addTransportSegment() {
        TransportNetwork tn = TransportNetwork.empty();

        Stop s1 = Stop.from("s1", GeographicPosition.SOUTH_POLE);
        Stop s2 = Stop.from("s2", GeographicPosition.NORTH_POLE);
        TransportSegment ts1 = TransportSegment.from(s1, s2, "7B", "1",
                Duration.ZERO, 4);

        tn.addTransportSegment(ts1);
        assertTrue(tn.getTransportSegments().contains(ts1));
    }

    @Test
    void getPassagesAtStop() {
        Stop s1 = Stop.from("s1", GeographicPosition.SOUTH_POLE);
        Stop s2 = Stop.from("s2", GeographicPosition.NORTH_POLE);

        TransportSegment ts = TransportSegment.from(s1, s2, "7B", "1",
                Duration.ZERO, 4);


        Variant v1 = Variant.empty("1", "7B");
        v1.addTransportSegment(ts);

        Line l = Line.of("7B");
        l.addVariant(v1);

        v1.addDeparture(LocalTime.MIN);
        v1.addDeparture(LocalTime.MAX);


        TransportNetwork tn = TransportNetwork.empty();
        tn.addStop(s1);
        tn.addStop(s2);
        tn.addTransportSegment(ts);
        tn.addLine(l);

        List<TransportSchedule> res = new ArrayList<>();
        TransportSchedule tsch1 = new TransportSchedule(LocalTime.MIN, s1, v1);
        TransportSchedule tsch2 = new TransportSchedule(LocalTime.MAX, s1, v1);
        res.add(tsch1);
        res.add(tsch2);

        assertEquals(res, tn.getPassages(s1).getTransportSchedules());
        
    }

    @Test
    void getRouteDescriptionTestThrowException() {
        var tn = newTransportNetworkHelper();
        Stop s1 = tn.getStopByName( "s1" );
        Stop s2 = tn.getStopByName( "s2" );
        TransportSegment ts1 = TransportSegment.from(s1, s2, "7B", "1",
                                                     Duration.ofMinutes( 3 ), 4);
        TransportSegment ts2 = TransportSegment.from(s2, s1, "7B", "2",
                                                     Duration.ofMinutes( 4 ), 4);
        List<Edge> route = new ArrayList<>();
        route.add(ts1);
        route.add(ts2);
        assertThrows(
                  IllegalStateException.class,
                  () -> tn.getRouteDescription(route, LocalTime.MIN)
                    );
    }

    @Test
    void getRouteDescriptionTest() {
        var tn = newTransportNetworkHelper();
        tn.getVariants().get(0).addDeparture( LocalTime.MIN );
        tn.getVariants().get(1).addDeparture( LocalTime.of( 0,7,0 ) );

        Stop s1 = tn.getStopByName( "s1" );
        Stop s2 = tn.getStopByName( "s2" );
        TransportSegment ts1 = TransportSegment.from(s1, s2, "7B", "1",
                                                     Duration.ofMinutes( 3 ), 4);
        TransportSegment ts2 = TransportSegment.from(s2, s1, "7B", "2",
                                                     Duration.ofMinutes( 4 ), 4);
        List<Edge> route = new ArrayList<>();
        route.add(ts1);
        route.add(ts2);

        assertNotEquals( "", tn.getRouteDescription(route, LocalTime.MIN) );
    }

    @Test
    void addDepartureToVariantThrowExceptionWithNullArgTest() {
        var tn = newTransportNetworkHelper();
        assertThrows(
                  IllegalArgumentException.class,
                  () -> tn.addDepartureToVariant( null, "1", LocalTime.MIN )
                    );
        assertThrows(
                  IllegalArgumentException.class,
                  () -> tn.addDepartureToVariant( "14", null, LocalTime.MIN )
                    );
        assertThrows(
                  IllegalArgumentException.class,
                  () -> tn.addDepartureToVariant( "14", "1", null )
                    );
    }

    @Test
    void addDepartureToVariantReturnTrueTest() {
        var tn = newTransportNetworkHelper();
        assertTrue(tn.addDepartureToVariant( "7B", "1", LocalTime.now()));
    }
    @Test
    void addDepartureToVariantReturnFalseTest() {
        var tn = newTransportNetworkHelper();
        assertFalse(tn.addDepartureToVariant( "14", "1", LocalTime.now()));
    }

    @Test
    void addGeographicVertexTrue() {
        TransportNetwork tn = TransportNetwork.empty();
        GeographicVertex geoVertex = new GeographicVertex(GeographicPosition.NORTH_POLE);
        tn.addGeographicVertex(geoVertex);
        assertTrue(tn.getVertices().contains(geoVertex));
    }

    @Test
    void addGeographicVertexNoDouble() {
        TransportNetwork tn = TransportNetwork.empty();
        GeographicVertex geoVertex = new GeographicVertex(GeographicPosition.NORTH_POLE);
        tn.addGeographicVertex(geoVertex);
        tn.addGeographicVertex(geoVertex);
        assertEquals(1,tn.getVertices().size());
        assertTrue(tn.getVertices().contains(geoVertex));
    }

    @Test
    void addGeographicVertexNull() {
        TransportNetwork tn = TransportNetwork.empty();
        assertThrows( NullPointerException.class,
        () -> tn.addGeographicVertex(null) );
    }

    @Test
    void removeGeographicVertex() {
        TransportNetwork tn = TransportNetwork.empty();
        GeographicVertex geoVertex = new GeographicVertex(GeographicPosition.NORTH_POLE);
        tn.addGeographicVertex(geoVertex);

        tn.removeGeographicVertex(geoVertex);
        assertEquals(0,tn.getVertices().size());
    }

    @Test
    void removeGeographicVertexNull() {
        TransportNetwork tn = TransportNetwork.empty();

        assertThrows( NullPointerException.class,
        () -> tn.addGeographicVertex(null) );
    }

    @Test
    void removeGeographicVertexNoSuchElement() {
        TransportNetwork tn = TransportNetwork.empty();
        GeographicVertex geoVertex = new GeographicVertex(GeographicPosition.NORTH_POLE);
        
        assertThrows( NoSuchElementException.class,
        () -> tn.removeGeographicVertex(geoVertex) );
    }

    @Test 
    void getGeographicVerticesNonEmpty(){
        TransportNetwork tn = TransportNetwork.empty();
        GeographicVertex geoVertex1 = new GeographicVertex(GeographicPosition.NORTH_POLE);
        GeographicVertex geoVertex2 = new GeographicVertex(GeographicPosition.SOUTH_POLE);
        GeographicVertex geoVertex3 = new GeographicVertex(GeographicPosition.NULL_ISLAND);
        DummyVertex vertex = new DummyVertex();

        tn.addVertex(vertex);
        tn.addGeographicVertex(geoVertex1);
        tn.addGeographicVertex(geoVertex2);
        tn.addGeographicVertex(geoVertex3);

        assertEquals(3,tn.getGeographicVertices().size());
    }


    @Test
    void addWalkSegmentTrue() {
        TransportNetwork tn = TransportNetwork.empty();

        GeographicVertex geoVertex1 = new GeographicVertex(GeographicPosition.NORTH_POLE);
        GeographicVertex geoVertex2 = new GeographicVertex(GeographicPosition.SOUTH_POLE);

        WalkSegment ws1 = new WalkSegment(geoVertex1,geoVertex2);


        tn.addWalkSegment(ws1);
        assertTrue(tn.getEdges().contains(ws1));
    }

    @Test
    void addWalkSegmentNoDouble() {
        TransportNetwork tn = TransportNetwork.empty();

        GeographicVertex geoVertex1 = new GeographicVertex(GeographicPosition.NORTH_POLE);
        GeographicVertex geoVertex2 = new GeographicVertex(GeographicPosition.SOUTH_POLE);

        WalkSegment ws1 = new WalkSegment(geoVertex1,geoVertex2);


        tn.addWalkSegment(ws1);
        tn.addWalkSegment(ws1);
        assertEquals(1,tn.getEdges().size());
    }

    @Test
    void addWalkSegmentNull() {
        TransportNetwork tn = TransportNetwork.empty();
        assertThrows( IllegalArgumentException.class,
        () -> tn.addWalkSegment(null) );
    }

    @Test
    void removeWalkSegmentTrue() {
        TransportNetwork tn = TransportNetwork.empty();

        GeographicVertex geoVertex1 = new GeographicVertex(GeographicPosition.NORTH_POLE);
        GeographicVertex geoVertex2 = new GeographicVertex(GeographicPosition.SOUTH_POLE);

        WalkSegment ws1 = new WalkSegment(geoVertex1,geoVertex2);


        tn.addWalkSegment(ws1);
        tn.removeWalkSegment(ws1);
        assertEquals(0,tn.getEdges().size());
    }

    @Test
    void removeWalkSegmentNull() {
        TransportNetwork tn = TransportNetwork.empty();

        assertThrows( NullPointerException.class,
        () -> tn.removeWalkSegment(null) );
    }

    @Test 
    void getWalkSegmentNonEmpty(){
        TransportNetwork tn = TransportNetwork.empty();
        GeographicVertex geoVertex1 = new GeographicVertex(GeographicPosition.NORTH_POLE);
        GeographicVertex geoVertex2 = new GeographicVertex(GeographicPosition.SOUTH_POLE);
        GeographicVertex geoVertex3 = new GeographicVertex(GeographicPosition.NULL_ISLAND);
        
        WalkSegment ws1 = new WalkSegment(geoVertex1, geoVertex2);
        WalkSegment ws2 = new WalkSegment(geoVertex2, geoVertex3);

        DummyEdge edge = new DummyEdge();

        tn.addWalkSegment(ws1);
        tn.addWalkSegment(ws2);
        tn.addEdge(edge);
        assertEquals(2,tn.getWalkSegments().size());
    }

}
