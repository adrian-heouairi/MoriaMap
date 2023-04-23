package dev.moriamap.model;

import java.util.*;

/**
 * Represents the Depth-First Search graph traversal strategy.
 */
public class DijkstraTraversalStrategy implements TraversalStrategy {

    /**
     * Explores the vertices of the given Graph, computing the shortest path
     * from src to dst if singleDestination is true or to all other vertices if
     * false. If weights is not empty, the given weights are used to determine
     * which Edge to take during the traversal, otherwise the traversal
     * considers each Edge as having the same weight. The map returned
     * corresponds to the shortest path tree from src to all other nodes in the
     * given Graph if singleDestination is false. If singleDestination is true,
     * the map returned corresponds to the shortest path from src to dst.
     * @param src the starting Vertex of the traversal
     * @param dst the destination Vertex if singleDestination is true
     * @param weights if not empty, the weight associated to each Edge
     * @param singleDestination a flag that indicates whether to stop when
     *                          destination Vertex is found
     * @param graph the graph to explore
     * @return the shortest path tree from src to dst if singleDestination is
     *         true, the shortest path tree from src to all other vertices
     *         otherwise
     */
    @Override
    public Map<Vertex, Edge> traversal(
      Vertex src,
      Vertex dst,
      Map<Edge, Double> weights, /* Update with bifunction */
      boolean singleDestination,
      Graph graph
    ) {
        Objects.requireNonNull(src);
        if (singleDestination)
            Objects.requireNonNull(dst);
        Objects.requireNonNull(weights);
        Objects.requireNonNull(graph);
        if (src.equals(dst) && singleDestination)
            return new HashMap<Vertex,Edge>();

        List<Vertex> p = new ArrayList<>();
        Map<Vertex,Double> d = new HashMap<>();
        for(Vertex v : graph.getVertices())
            d.put(v,Double.POSITIVE_INFINITY);
        d.put(src, 0.0);

        Map<Vertex,Edge> res = new HashMap<>();
        List<Vertex> pComplement = graph.getVertices();

        while(!pComplement.isEmpty()){
            Vertex a = pComplement.get(0);
            double minD = Double.POSITIVE_INFINITY;
            for(Vertex v : pComplement){
                if(d.get(v) < minD){
                    minD = d.get(v);
                    a = v;
                }
            }
            pComplement.remove(a);
            p.add(a);

            for(Edge ab : graph.getOutgoingEdgesOf(a)){
                if( !p.contains(ab.getTo())){
                    double newWeight = d.get(a) + weights.get(ab);
                    if(d.get(ab.getTo()) > newWeight){
                        d.put(ab.getTo(),newWeight);
                        res.put(ab.getTo(), ab);
                    }
                }
            }

        }
        return res;
    }

}
