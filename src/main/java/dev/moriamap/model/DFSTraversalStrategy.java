package dev.moriamap.model;

import java.util.*;
import java.util.function.BiFunction;

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
     * @param weightFunction not used by this TraversalStrategy
     * @param stopAtDestination a flag that indicates whether to stop when
     *                          destination Vertex is found
     * @param graph the graph to explore
     * @return a map that associates to each visited Vertex its incoming Edge
     *         when explored
     */
    public Map<Vertex, Edge> traversal(
      Vertex src,
      Vertex dst,
      BiFunction<Double, Edge, Double> weightFunction,
      boolean stopAtDestination,
      Graph graph
    ) {
        Objects.requireNonNull(src);
        if (stopAtDestination)
            Objects.requireNonNull(dst);
        Objects.requireNonNull(graph);
        if (!graph.contains(src))
            throw new NoSuchElementException("Absent source Vertex");
        if (src.equals(dst) && stopAtDestination)
            return new HashMap<>();
        Deque<Vertex> stack = new ArrayDeque<>();
        Map<Vertex, Edge> parents = new HashMap<>();
        List<Vertex> visited = new ArrayList<>();
        stack.push(src);
        visited.add(src);
        while (!stack.isEmpty()) {
            Vertex tmp = stack.pop();
            for (Edge outgoingEdge: graph.getOutgoingEdgesOf(tmp)) {
                var to = outgoingEdge.getTo();
                if (!visited.contains(to)) {
                    visited.add(to);
                    parents.put(to, outgoingEdge);
                    if (to.equals(dst))
                        return parents;
                    stack.push(to);
                }
            }
        }
        return parents;
    }
}
