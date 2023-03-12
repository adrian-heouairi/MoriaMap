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
     * Adds a neighbor to the caller.
     * @param neighbor to be added.
     * @return true if neighbor was added, false if it was already there.
     */
    public boolean addNeighbor(Vertex neighbor) {

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
