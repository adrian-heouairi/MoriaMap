package dev.moriamap.model;

import java.util.*;
import java.util.function.BiFunction;

/**
 * Represents the Dijkstra graph traversal strategy.
 */
public class DijkstraTraversalStrategy implements TraversalStrategy {

    /**
     * Explores the vertices of the given Graph, computing the shortest path
     * from src to dst if singleDestination is true or to all other vertices if
     * false. weightFunction is used to determine
     * which Edge to take during the traversal. The map returned
     * corresponds to the shortest path tree from src to all other nodes in the
     * given Graph if singleDestination is false. If singleDestination is true,
     * the map returned corresponds to the shortest path from src to dst.
     * @param src the starting Vertex of the traversal
     * @param dst the destination Vertex if singleDestination is true
     * @param weightFunction the weight calculation function
     * @param singleDestination a flag that indicates whether to stop when
     *                          destination Vertex is found
     * @param graph the graph to explore
     * @return the shortest path tree from src to dst if singleDestination is
     *         true, the shortest path tree from src to all other vertices
     *         otherwise
     * @throws NullPointerException if one of the arguments is null
     */
    @Override
    public Map<Vertex, Edge> traversal(
      Vertex src,
      Vertex dst,
      BiFunction<Double, Edge, Double> weightFunction,
      boolean singleDestination,
      Graph graph
    ) {
        Objects.requireNonNull(src);
        if (singleDestination)
            Objects.requireNonNull(dst);
        Objects.requireNonNull(weightFunction);
        Objects.requireNonNull(graph);

        List<Vertex> visited = new ArrayList<>();
        Map<Vertex,Double> distance = new HashMap<>();
        for (Vertex v : graph.getVertices())
            distance.put(v, Double.POSITIVE_INFINITY);
        distance.put(src, 0.0);

        Map<Vertex,Edge> res = new HashMap<>();
        List<Vertex> notVisited = graph.getVertices();

        while (!notVisited.isEmpty()) {
            Vertex a = findMin(distance, notVisited);

            if (hasToStop(a, dst, singleDestination))
                return res;

            notVisited.remove(a);
            visited.add(a);

            for (Edge ab: graph.getOutgoingEdgesOf(a)) {
                if (!visited.contains(ab.getTo())) {
                    double distA = distance.get(a);
                    double distanceFromSrcToB = 
                        distA + weightFunction.apply(distA, ab);
                    Vertex to = ab.getTo();
                    if (distance.get(to) > distanceFromSrcToB) {
                        distance.put(to, distanceFromSrcToB);
                        res.put(to, ab);
                    }
                }
            }

        }
        return res;
    }

    // Returns true if a and dst are equal and single destination is true
    private boolean hasToStop(Vertex a, Vertex dst, boolean singleDestination) {
        return a.equals(dst) && singleDestination;
    }

    // Returns the Vertex of vertices that has the minimal value associated in
    // dist. If none of the vertices is at strictly less than 
    // Double.POSITIVE_INFINITY distance, returns the first element of 
    // vertices.
    private Vertex findMin(Map<Vertex, Double> dist, List<Vertex> vertices) {
        Vertex a = vertices.get(0);
        double minD = Double.POSITIVE_INFINITY;        
        for(Vertex v : vertices){
            if(dist.get(v) < minD){
                minD = dist.get(v);
                a = v;
            }
        }
        return a;
    }
}