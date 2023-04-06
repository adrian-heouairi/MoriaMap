package dev.moriamap.model;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.Collections;

/**
 * A graph is a set of vertices and a set of edges between those vertices.
 */
public abstract class Graph {
    private static final String NULL_ARGUMENT_ERROR_MSG =
        "Argument can not be null";
    private static final String ABSENT_VERTEX_ERROR_MSG = "No such vertex";

    // A map that associates a Vertex to its outgoing edges
    private final Map<Vertex, List<Edge>> vertexToOutgoingEdges;

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
     * Performs a depth-first search from the specified vertex and computes a
     * map that associates each visited Vertex to the Edge that led to it during
     * the traversal.
     * @param src the starting vertex for the search
     * @return a map that associates each visited Vertex to the Edge that led to
     *         it during the traversal
     * @throws IllegalArgumentException if src is null
     * @throws NoSuchElementException if src is not in this Graph
     */
    public Map<Vertex, Edge> depthFirstSearch(Vertex src) {
        if (src == null)
            throw new IllegalArgumentException(NULL_ARGUMENT_ERROR_MSG);
        if (!this.contains(src))
            throw new NoSuchElementException(ABSENT_VERTEX_ERROR_MSG);
        Deque<Vertex> stack = new ArrayDeque<>();
        Map<Vertex, Edge> parents = new HashMap<>();
        List<Vertex> visited = new ArrayList<>();
        Vertex tmp = null;
        Vertex to = null;
        stack.push(src);
        visited.add(src);
        while (!stack.isEmpty()) {
            tmp = stack.pop();
            for (Edge outgoingEdge: this.getOutgoingEdgesOf(tmp)) {
                to = outgoingEdge.getTo();
                if (!visited.contains(to)) {
                    visited.add(to);
                    parents.put(to, outgoingEdge);
                    stack.push(to);
                }
            }
        }
        return parents;
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
    public static List<Edge> getRouteFromTraversal(
      Map<Vertex, Edge> parents,
      Vertex src,
      Vertex dst
    ) {
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
}
