package dev.moriamap.model;

import java.io.OutputStream;
import java.time.LocalTime;
import java.util.List;

/**
 * Query that computes and prints the shortest path from a starting geographic
 * position to a target geographic position with the selected optimization method.
 * If walking is more optimal, the route returned may include walking sections.
 */
public class OptimizedRouteBetweenPositionsWithWalkQuery extends OptimizedRouteBetweenPositionsQuery {

    /**
     * Constructor of OptimizedRouteBetweenPositionsQuery
     * @param out                the outputStream where the result will be written
     * @param startPoint         geographic Vertex of the starting point
     * @param targetPoint        geographic Vertex of the starting point
     * @param optimizationChoice optimization method used
     * @param startTime          starting time when the travel start
     * @throws NullPointerException if any argument is null except out
     */
    public OptimizedRouteBetweenPositionsWithWalkQuery(OutputStream out,
            GeographicVertex startPoint, GeographicVertex targetPoint,
            RouteOptimization optimizationChoice, LocalTime startTime) {
        super(out, startPoint, targetPoint, optimizationChoice, startTime);
    }

    private void addWalkSegments(TransportNetwork network, GeographicVertex gv) {
        var distanceMap = GeographicVertex.makeDistanceSortedMap(
                gv, network.getGeographicVertices());
        List<GeographicVertex> gvsWithinRadius =
                GeographicVertex.getNClosestGVsWithinRadius(
                        Integer.MAX_VALUE, GEOVERTEX_SEARCH_RADIUS, distanceMap);

        for (GeographicVertex v : gvsWithinRadius) {
            var ws = new WalkSegment(gv, v);
            network.addWalkSegment(ws);
        }
    }

    @Override
    protected void pre(TransportNetwork network) {
        for (Stop stop : network.getStops())
            this.addWalkSegments(network, stop);
    }
}
