package dev.moriamap.model.query;

import static org.junit.jupiter.api.Assertions.*;

import dev.moriamap.model.network.Edge;
import dev.moriamap.model.network.GeographicPosition;
import dev.moriamap.model.network.GeographicVertex;
import dev.moriamap.model.network.Graph;
import dev.moriamap.model.network.Line;
import dev.moriamap.model.network.Stop;
import dev.moriamap.model.network.TransportNetwork;
import dev.moriamap.model.network.TransportSegment;
import dev.moriamap.model.network.Variant;
import dev.moriamap.model.network.Vertex;
import dev.moriamap.model.network.WalkSegment;
import dev.moriamap.model.network.traversal.DFSTraversalStrategy;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PrettyPrinterTest {

  final TransportNetwork tn;

  {
    Stop s1 = Stop.from("s1", GeographicPosition.SOUTH_POLE);
    Stop s2 = Stop.from("s2", GeographicPosition.NORTH_POLE);
    Stop s3 = Stop.from("s3", GeographicPosition.NORTH_POLE);

    TransportSegment ts1 = TransportSegment.from(s1, s2, "7B", "1", Duration.ofMinutes(2), 4);
    TransportSegment ts2 = TransportSegment.from(s2, s3, "14", "1", Duration.ofMinutes(2), 4);

    Variant v1 = Variant.empty("1", "7B");
    v1.addTransportSegment(ts1);
    Variant v2 = Variant.empty("1", "14");
    v2.addTransportSegment(ts2);

    v1.addDeparture(LocalTime.of(0, 1, 0));
    v2.addDeparture(LocalTime.of(0, 7, 0));

    Line l = Line.of("7B");
    l.addVariant(v1);
    Line l2 = Line.of("14");
    l2.addVariant(v2);

    tn = TransportNetwork.empty();
    tn.addStop(s1);
    tn.addStop(s2);
    tn.addStop(s3);
    tn.addTransportSegment(ts1);
    tn.addTransportSegment(ts2);
    tn.addLine(l);
    tn.addLine(l2);
  }

  final List<Edge> testRoute;

  {
    Stop s1 = Stop.from("s1", GeographicPosition.SOUTH_POLE);
    Stop s2 = Stop.from("s2", GeographicPosition.NORTH_POLE);
    Stop s3 = Stop.from("s3", GeographicPosition.NORTH_POLE);
    WalkSegment ws1 =
        new WalkSegment(
            GeographicVertex.at(1.5, 2.4), GeographicVertex.at(GeographicPosition.SOUTH_POLE));
    TransportSegment ts1 = TransportSegment.from(s1, s2, "7B", "1", Duration.ofMinutes(2), 4);
    TransportSegment ts2 = TransportSegment.from(s2, s3, "14", "1", Duration.ofMinutes(2), 4);
    testRoute = List.of(ws1, ts1, ts2);
  }

  @Test
  void lineChangeToStringWithLineNotFound() {
    assertThrows(
        IllegalArgumentException.class,
        () -> PrettyPrinter.lineChangeToString(tn, "no existing line name", null));
  }

  @Test
  void emptyListTest() {
    assertEquals("", PrettyPrinter.printTransportSegmentPath(tn, new ArrayList<>()));
  }

  @Test
  void nonEmptyListGiveNonEmptyStringTest() {
    Stop start = tn.getStopByName("s1");
    Stop target = tn.getStopByName("s3");
    var dfsTrav = new DFSTraversalStrategy();
    Map<Vertex, Edge> dfs = dfsTrav.traversal(start, target, null, false, tn);
    List<Edge> path = Graph.getRouteFromTraversal(dfs, start, target);
    assertNotEquals("", PrettyPrinter.printTransportSegmentPath(tn, path));
  }

  @Test
  void printTransportSegmentPathWithLineChangeTimesEmptyRouteTest() {
    assertEquals(
        "",
        PrettyPrinter.printTransportSegmentPathWithLineChangeTimes(
            tn, new ArrayList<>(), new ArrayList<>()));
  }

  @Test
  void printTransportSegmentPathWithLineChangeTimesTest() {
    List<LocalTime> lts = List.of(LocalTime.MIN, LocalTime.of(0, 3, 0), LocalTime.of(0, 10, 0));
    assertNotEquals(
        "", PrettyPrinter.printTransportSegmentPathWithLineChangeTimes(tn, testRoute, lts));
  }

  @Test
  void printTransportSegmentPathWithLineChangeTimesDifferentLtsSizeTest() {
    List<LocalTime> lts = List.of(LocalTime.MIN);
    assertThrows(
        IllegalArgumentException.class,
        () -> PrettyPrinter.printTransportSegmentPathWithLineChangeTimes(tn, testRoute, lts));
  }

  @Test
  void printTransportSegmentPathWithLineChangeTimesEmptyLtsListTest() {
    List<LocalTime> lts = new ArrayList<>();
    assertThrows(
        IllegalArgumentException.class,
        () -> PrettyPrinter.printTransportSegmentPathWithLineChangeTimes(tn, testRoute, lts));
  }

  @Test
  void formatDurationOnlyHourTest() {
    assertEquals("1 hour", PrettyPrinter.formatDuration(Duration.ofHours(1)));
  }

  @Test
  void formatDurationOnlyHoursTest() {
    assertEquals("12 hours", PrettyPrinter.formatDuration(Duration.ofHours(12)));
  }

  @Test
  void formatDurationOnlyMinuteTest() {
    assertEquals("1 minute", PrettyPrinter.formatDuration(Duration.ofMinutes(1)));
  }

  @Test
  void formatDurationOnlyMinutesTest() {
    assertEquals("12 minutes", PrettyPrinter.formatDuration(Duration.ofMinutes(12)));
  }

  @Test
  void formatDurationOnlySecondTest() {
    assertEquals("1 second", PrettyPrinter.formatDuration(Duration.ofSeconds(1)));
  }

  @Test
  void formatDurationOnlySecondsTest() {
    assertEquals("12 seconds", PrettyPrinter.formatDuration(Duration.ofSeconds(12)));
  }

  @Test
  void formatDurationTest() {
    assertEquals(
        "12 hours 49 minutes 10 seconds",
        PrettyPrinter.formatDuration(Duration.ofHours(12).plusMinutes(49).plusSeconds(10)));
  }
}
