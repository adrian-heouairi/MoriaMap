package dev.moriamap.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.HashMap;

class DijkstraTraversalStrategyTest {
    static class DummyVertex implements Vertex {}
    static class DummyGraph extends Graph {}

    @Test void traversalWithNullSrcThrowsException() {
        var sut = new DijkstraTraversalStrategy();
        assertThrows(
          NullPointerException.class,
          () -> sut.traversal(null, null, null, true, null)
        );
    }

    @Test void traversalWithNullDstAndFlagTrueThrowsException() {
        var sut = new DijkstraTraversalStrategy();
        var dv = new DummyVertex();
        assertThrows(
          NullPointerException.class,
          () -> sut.traversal(dv, null, null, true, null)
        );
    }

    @Test void traversalWithNullDstAndFlagFalseDoesNotThrowNPE() {
        var sut = new DijkstraTraversalStrategy();
        var src = new DummyVertex();
        var graph = new DummyGraph();
        var weights = new HashMap<Edge, Double>();
        try {
            sut.traversal(src, null, weights, false, graph);
        } catch (NullPointerException npe) {
            fail("Thrown NullPointerException");
        } catch (Throwable t) {}
    }

    @Test void traversalWithNullGraphThrowsException() {
        var sut = new DijkstraTraversalStrategy();
        var src = new DummyVertex();
        var dst = new DummyVertex();
        assertThrows(
          NullPointerException.class,
          () -> sut.traversal(src, dst, null, false, null)
        );
    }

    @Test void traversalWithNullWeightThrowsException() {
        var sut = new DijkstraTraversalStrategy();
        var src = new DummyVertex();
        var dst = new DummyVertex();
        var graph = new DummyGraph();
        assertThrows(
          NullPointerException.class,
          () -> sut.traversal(src, null, null, false, graph)
        );
    }
}
