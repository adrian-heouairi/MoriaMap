package dev.moriamap.model.network.traversal;

import static org.junit.jupiter.api.Assertions.*;

import dev.moriamap.model.network.Edge;
import dev.moriamap.model.network.Graph;
import dev.moriamap.model.network.Vertex;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import org.junit.jupiter.api.Test;

class DijkstraTraversalStrategyTest {
  private static class DummyVertex implements Vertex {}

  private static class DummyGraph extends Graph {
    void addDummyEdge(DummyEdge de) {
      super.addEdge(de);
    }
  }

  private static class DummyEdge extends Edge {
    public DummyEdge(Vertex from, Vertex to) {
      super(from, to);
    }
  }

  @Test
  void traversalWithNullSrcThrowsException() {
    var sut = new DijkstraTraversalStrategy();
    assertThrows(NullPointerException.class, () -> sut.traversal(null, null, null, true, null));
  }

  @Test
  void traversalWithNullDstAndFlagTrueThrowsException() {
    var sut = new DijkstraTraversalStrategy();
    var dv = new DummyVertex();
    assertThrows(NullPointerException.class, () -> sut.traversal(dv, null, null, true, null));
  }

  @Test
  void traversalWithNullDstAndFlagFalseDoesNotThrowNPE() {
    var sut = new DijkstraTraversalStrategy();
    var src = new DummyVertex();
    var graph = new DummyGraph();
    try {
      sut.traversal(src, null, (Double d, Edge e) -> 0.0, false, graph);
    } catch (NullPointerException npe) {
      fail("Thrown NullPointerException");
    } catch (Throwable t) {
    }
  }

  @Test
  void traversalWithNullGraphThrowsException() {
    var sut = new DijkstraTraversalStrategy();
    var src = new DummyVertex();
    var dst = new DummyVertex();
    assertThrows(NullPointerException.class, () -> sut.traversal(src, dst, null, false, null));
  }

  @Test
  void traversalWithNullWeightThrowsException() {
    var sut = new DijkstraTraversalStrategy();
    var src = new DummyVertex();
    var graph = new DummyGraph();
    assertThrows(NullPointerException.class, () -> sut.traversal(src, null, null, false, graph));
  }

  @Test
  void traversalWithGraph() {
    var graph = new DummyGraph();

    DummyVertex v1 = new DummyVertex();
    DummyVertex v2 = new DummyVertex();
    DummyVertex v3 = new DummyVertex();
    DummyVertex v4 = new DummyVertex();

    DummyEdge e1 = new DummyEdge(v1, v2);
    DummyEdge e2 = new DummyEdge(v2, v3);
    DummyEdge e3 = new DummyEdge(v3, v4);

    DummyEdge e4 = new DummyEdge(v1, v4);

    graph.addDummyEdge(e1);
    graph.addDummyEdge(e2);
    graph.addDummyEdge(e3);
    graph.addDummyEdge(e4);

    BiFunction<Double, Edge, Double> weightFunction =
        (d, edge) -> {
          if (edge.equals(e1)) return 1.0;
          if (edge.equals(e2)) return 1.0;
          if (edge.equals(e3)) return 1.0;
          if (edge.equals(e4)) return 11.0;
          return Double.POSITIVE_INFINITY;
        };

    DijkstraTraversalStrategy dijkstra = new DijkstraTraversalStrategy();
    Map<Vertex, Edge> result = dijkstra.traversal(v1, v4, weightFunction, false, graph);
    Map<Vertex, Edge> compare = new HashMap<>();
    compare.put(v2, e1);
    compare.put(v3, e2);
    compare.put(v4, e3);

    assertEquals(compare, result);
  }
}
