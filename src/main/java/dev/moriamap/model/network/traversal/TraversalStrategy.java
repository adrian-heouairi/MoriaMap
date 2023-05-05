package dev.moriamap.model.network.traversal;

import dev.moriamap.model.network.Edge;
import dev.moriamap.model.network.Graph;
import dev.moriamap.model.network.Vertex;
import java.util.*;
import java.util.function.BiFunction;

/** Represents a graph traversal strategy. */
public interface TraversalStrategy {

  /**
   * Explores the vertices of the given Graph, starting at src and stopping at dst if
   * stopAtDestination is true. If the strategy involves weighted edges, the weight of each edge is
   * calculated as the algorithm needs it with weightFunction. The weight function takes a Double
   * which can be used by traversal strategies to represent whatever they need, and the Edge whose
   * weight we need to calculate. The map returned corresponds to the association of each visited
   * Vertex to its incoming Edge during the traversal.
   *
   * @param src the starting Vertex of the traversal
   * @param dst the Vertex at which the traversal stops if stopAtDestination is true
   * @param weightFunction the weight calculation function
   * @param stopAtDestination a flag that indicates whether to stop when destination Vertex is found
   * @param graph the graph to explore
   * @return a map that associates to each visited Vertex its incoming Edge when explored
   */
  Map<Vertex, Edge> traversal(
      Vertex src,
      Vertex dst,
      BiFunction<Double, Edge, Double> weightFunction,
      boolean stopAtDestination,
      Graph graph);
}
