package dev.moriamap.model;

import java.util.*;

/**
 * Represents a vertex of a graph
 */
public abstract class Vertex {

    /**
     * The list of neighbors to which this vertex is linked.
     */
    protected List<Vertex> neighbors;

    /**
     * Builds a Vertex which does not have neighbors.
     */
    public Vertex() {
        this.neighbors = new ArrayList<>();
    }

    /**
     * Gets the list of the neighbors.
     * @return a copy of the neighbor's list of the caller.
     */
    public List<Vertex> getNeighbors() {
        List<Vertex> res = new ArrayList<>(this.neighbors.size());
        for (Vertex v : this.neighbors) {
            res.add(v);
        }
        return res;
    }

    /**
     * Adds the specified Vertex to this Vertex neighbors if it is not already
     * a neighbor.
     *
     * @param neighbor the Vertex to add to this Vertex neighbors
     * @return true if the specified vertex was added to this Vertex neighbors
     * @throws IllegalArgumentException if specified Vertex is this Vertex
     * @throws NullPointerException if specified Vertex is null
     */
    public boolean addNeighbor(Vertex neighbor) {
        if (this == neighbor)
            throw new IllegalArgumentException("Can not add this to neighbors");
        if (neighbor == null)
            throw new NullPointerException("Can not add null to neighbors");
        if (!this.neighbors.contains(neighbor)) {
            this.neighbors.add(neighbor);
            return true;
        }
        return false;
    }

    /**
     * Removes a neighbor to the caller.
     * @param neighbor to be removed.
     * @return true if the neighbor was removed.
     */
    public boolean removeNeighbor(Vertex neighbor) {
        return this.neighbors.remove(neighbor);
    }
}
