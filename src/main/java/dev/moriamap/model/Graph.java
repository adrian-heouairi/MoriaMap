package dev.moriamap.model;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * A graph is a set of vertices and a set of edges between those vertices.
 */
public abstract class Graph {

    // A map that associates a Vertex to its outgoing edges
    private Map<Vertex, List<Edge>> vertexToOutgoingEdges;

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
    public void addVertex(Vertex vertex) {
        if (vertex == null)
            throw new IllegalArgumentException("Vertex can not be null");
        this.vertexToOutgoingEdges.putIfAbsent(vertex, null);
    }

    /**
     * Adds the specified edge to this Graph. If edge's source or destination
     * are not already in this Graph, they are added too. If the specified Edge
     * is already present, does nothing.
     * @param edge the Edge to add
     * @throws IllegalArgumentException if the given edge is null
     */
    public void addEdge(Edge edge) {
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
     * @param vertex some Vertex
     * {@return the list of outgoing Edges of the specified Vertex}
     * @throws IllegalArgumentException if vertex is null
     * @throws NoSuchElementException if vertex is not in this Graph
     */
    public List<Edge> getOutgoingEdgesOf(Vertex vertex) {
        if (vertex == null)
            throw new IllegalArgumentException("Vertex can not be null");
        if (!this.vertexToOutgoingEdges.containsKey(vertex))
            throw new NoSuchElementException("Vertex is absent");
        List<Edge> edges = this.vertexToOutgoingEdges.get(vertex);
        if (edges == null)
            return new ArrayList<>();
        return edges;
    }
}
