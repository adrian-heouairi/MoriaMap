package dev.moriamap.model;

import java.util.*;

/**
 * Represents the Depth-First Search graph traversal strategy.
 */
public class DFSTraversalStrategy implements TraversalStrategy {

    /**
     * Explores the vertices of the given Graph, starting at src and stopping at
     * dst if stopAtDestination is true, following the Depth-First Search
     * manner. As this strategy does not involve weights, they are not used. The
     * map returned corresponds to the association of each visited Vertex to its
     * incoming Edge during the traversal.
     * @param src the starting Vertex of the traversal
     * @param dst the Vertex at which the traversal stops if stopAtDestination
     *            is true
     * @param weights not used by this TraversalStrategy
     * @param stopAtDestination a flag that indicates whether to stop when
     *                          destination Vertex is found
     * @param graph the graph to explore
     * @return a map that associates to each visited Vertex its incoming Edge
     *         when explored
     */
    public Map<Vertex, Edge> traversal(
      Vertex src,
      Vertex dst,
      Map<Edge, Double> weights,
      boolean stopAtDestination,
      Graph graph
    ) {
        Objects.requireNonNull(src);
        if (stopAtDestination)
            Objects.requireNonNull(dst);
        Objects.requireNonNull(graph);
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
