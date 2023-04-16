package dev.moriamap.model;

import java.util.*;

/**
 * Represents a graph traversal strategy.
 */
public interface TraversalStrategy {

    /**
     * Explores the vertices of the given Graph, starting at src and stopping at
     * dst if stopAtDestination is true. If weights is not empty and if the
     * strategy involves weighted edges, each edge is considered according to
     * its corresponding weight given by weights. The map returned corresponds
     * to the association of each visited Vertex to its incoming Edge during the
     * traversal.
     * @param src the starting Vertex of the traversal
     * @param dst the Vertex at which the traversal stops if stopAtDestination
     *            is true
     * @param weights an association of Edges to their weight
     * @param stopAtDestination a flag that indicates whether to stop when
     *                          destination Vertex is found
     * @param graph the graph to explore
     * @return a map that associates to each visited Vertex its incoming Edge
     *         when explored
     */
    Map<Vertex, Edge> traversal(Vertex src,
                                Vertex dst,
                                Map<Edge, Double> weights,
                                boolean stopAtDestination,
                                Graph graph);
}
