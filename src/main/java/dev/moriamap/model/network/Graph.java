package dev.moriamap.model.network;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Collections;
import java.util.Objects;
import java.util.function.BiFunction;

import dev.moriamap.model.network.traversal.DFSTraversalStrategy;
import dev.moriamap.model.network.traversal.TraversalStrategy;

/**
 * A graph is a set of vertices and a set of edges between those vertices.
 */
public abstract class Graph {
    private static final String NULL_ARGUMENT_ERROR_MSG =
        "Argument can not be null";
    private static final String ABSENT_VERTEX_ERROR_MSG = "No such vertex";

    // A map that associates a Vertex to its outgoing edges
    private final Map<Vertex, List<Edge>> vertexToOutgoingEdges;

    // The traversal strategy of this Graph, defaults to DFS
    private TraversalStrategy traversalStrategy = new DFSTraversalStrategy();

    /**
     * Creates a new empty Graph.
     */
    protected Graph() {
        this.vertexToOutgoingEdges = new HashMap<>();
    }

    /**
     * {@return the vertices of this Graph}
     */
    public List<Vertex> getVertices() {
        return new ArrayList<>(this.vertexToOutgoingEdges.keySet());
    }

    /**
     * {@return the edges of this Graph}
     */
    public List<Edge> getEdges() {
        List<Edge> edges = new ArrayList<>();
        for (Map.Entry<Vertex, List<Edge>> entry:
                 this.vertexToOutgoingEdges.entrySet()) {
            if (entry.getValue() != null)
                edges.addAll(entry.getValue());
        }
        return edges;
    }

    /**
     * Adds the specified vertex to this Graph. If it is already present, does
     * nothing.
     * @param vertex the Vertex to add
     * @throws IllegalArgumentException if the given vertex is null
     */
    protected void addVertex(Vertex vertex) {
        if (vertex == null)
            throw new IllegalArgumentException(NULL_ARGUMENT_ERROR_MSG);
        this.vertexToOutgoingEdges.putIfAbsent(vertex, null);
    }

    /**
     * Adds the specified edge to this Graph. If edge's source or destination
     * are not already in this Graph, they are added too. If the specified Edge
     * is already present, does nothing.
     * @param edge the Edge to add
     * @throws IllegalArgumentException if the given edge is null
     */
    protected void addEdge(Edge edge) {
        if (edge == null)
            throw new IllegalArgumentException("Edge can not be null");
        Vertex from = edge.getFrom();
        Vertex to = edge.getTo();
        if (!this.vertexToOutgoingEdges.containsKey(from))
            this.addVertex(from);
        if (!this.vertexToOutgoingEdges.containsKey(to))
            this.addVertex(to);
        List<Edge> outgoingEdges = this.vertexToOutgoingEdges.get(from);
        if (outgoingEdges == null) {
            outgoingEdges = new ArrayList<>();
            outgoingEdges.add(edge);
            this.vertexToOutgoingEdges.replace(from, outgoingEdges);
        } else {
            if (!outgoingEdges.contains(edge))
                outgoingEdges.add(edge);
        }
    }

    /**
     * {@return the list of outgoing Edges of the specified Vertex}
     * @param vertex some Vertex
     * @throws IllegalArgumentException if vertex is null
     * @throws NoSuchElementException if vertex is not in this Graph
     */
    public List<Edge> getOutgoingEdgesOf(Vertex vertex) {
        if (vertex == null)
            throw new IllegalArgumentException(NULL_ARGUMENT_ERROR_MSG);
        if (!this.vertexToOutgoingEdges.containsKey(vertex))
            throw new NoSuchElementException(ABSENT_VERTEX_ERROR_MSG);
        List<Edge> edges = this.vertexToOutgoingEdges.get(vertex);
        if (edges == null)
            return new ArrayList<>();
        return new ArrayList<>(edges); // Return a copy instead of the original
    }

    /**
     * {@return true if vertex is not null and is in this Graph}
     * @param vertex some vertex that might be in this Graph
     * @throws IllegalArgumentException if vertex is null
     */
    public boolean contains(Vertex vertex) {
        if (vertex == null)
            throw new IllegalArgumentException(NULL_ARGUMENT_ERROR_MSG);
        return this.vertexToOutgoingEdges.containsKey(vertex);
    }

    /**
     * {@return a list of Edge that forms a route from src to dst}
     * @param parents a map that associates each visited Vertex to the Edge that
     *                led to it during a Graph traversal
     * @param src the source Vertex
     * @param dst the destination Vertex
     * @throws IllegalArgumentException if parents, src or dst are null
     * @throws NoSuchElementException if dst is not a key of parents
     */
    public static List<Edge> getRouteFromTraversal(Map<Vertex, Edge> parents,
                                                   Vertex src,
                                                   Vertex dst) {
        if (parents == null || src == null || dst == null)
            throw new IllegalArgumentException(NULL_ARGUMENT_ERROR_MSG);
        if (!parents.containsKey(dst))
            throw new NoSuchElementException("Destination is absent");
        if (!isVertexSourceOfEdgeInTraversalMap(parents, src))
            throw new NoSuchElementException("Source is absent");
        List<Edge> route = new ArrayList<>();
        route.add(parents.get(dst));
        Vertex parent = parents.get(dst).getFrom();
        Edge edgeToParent = null;
        while (!parent.equals(src)) {
            edgeToParent = parents.get(parent);
            route.add(edgeToParent);
            parent = edgeToParent.getFrom();
        }
        Collections.reverse(route); // as we start from destination
        return route;
    }

    // Returns true if source is a source vertex in at least one of the edges of
    // the given map
    private static boolean isVertexSourceOfEdgeInTraversalMap(
      Map<Vertex, Edge> parents,
      Vertex source
    ) {
        Edge current = null;
        for (Map.Entry<Vertex, Edge> entries: parents.entrySet()) {
            current = entries.getValue();
            if (current != null && current.getFrom().equals(source))
                return true;
        }
        return false;
    }

    /**
     * Explores the vertices of the given Graph, starting at src and stopping at
     * dst if stopAtDestination is true. If the strategy involves weighted edges,
     * the weight of each edge is calculated as the algorithm needs it with
     * weightFunction. The weight function takes a Double which can be used
     * by traversal strategies to represent whatever they need, and the Edge
     * whose weight we need to calculate.
     * The map returned corresponds
     * to the association of each visited Vertex to its incoming Edge during the
     * traversal.
     * @param src the starting Vertex of the traversal
     * @param dst the Vertex at which the traversal stops if stopAtDestination
     *            is true
     * @param weightFunction the weight calculation function
     * @param stopAtDestination a flag that indicates whether to stop when
     *                          destination Vertex is found
     * @return a map that associates to each visited Vertex its incoming Edge
     *         when explored
     * @throws NullPointerException if src is null or if dst is null and
     *                              stopAtDestination is true
     */
    public Map<Vertex, Edge> traversal(Vertex src,
                                       Vertex dst,
                                       BiFunction<Double, Edge, Double> weightFunction,
                                       boolean stopAtDestination) {
        return this.traversalStrategy
            .traversal(src, dst, weightFunction, stopAtDestination, this);
    }

    /**
     * Sets the value of this Graph traversal strategy to the given strategy.
     * @param newStrategy the new traversal strategy of this Graph
     * @throws NullPointerException if newStrategy is null
     */
    public void setTraversalStrategy(TraversalStrategy newStrategy) {
        Objects.requireNonNull(newStrategy);
        this.traversalStrategy = newStrategy;
    }

    /**
     * Removes the given edge from the graph. No vertices are removed.
     * @param e the edge to be removed
     * @throws NullPointerException if e is null
     */
    protected void removeEdge(Edge e) {
        Objects.requireNonNull(e);
        List<Edge> edges = this.vertexToOutgoingEdges.get(e.getFrom());
        if(edges != null)
            edges.remove(e);
    }

    /**
     * Removes the given vertex from the graph and all edges
     * that have this vertex on either side.
     * @param v the vertex to be removed
     */
    protected void removeVertex(Vertex v) {
        Objects.requireNonNull(v);
        if (this.vertexToOutgoingEdges.containsKey(v)) {
            for (Edge e: this.getEdges()) {
                if (e.getTo().equals(v))
                    removeEdge(e);
            }
            this.vertexToOutgoingEdges.remove(v);
        } else {
            throw new NoSuchElementException("Vertex was not found");
        }
    }
}
