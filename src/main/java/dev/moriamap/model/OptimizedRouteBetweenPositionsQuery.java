package dev.moriamap.model;

import java.io.OutputStream;
import java.time.LocalTime;
import java.util.*;
import java.util.function.BiFunction;

/**
 * Query that computes and prints the shortest path from a starting geographic
 * position to a target geographic position with the selected optimization method
 */
public class OptimizedRouteBetweenPositionsQuery extends Query {
	/**
	 * The radius of the circle inside which we are looking for geographic
	 * vertices that are close to the start or the destination point of the route.
	 * It is in meters.
	 */
	protected static final double GEOVERTEX_SEARCH_RADIUS = 2000;

	/**
	 * The maximum number of geographic vertices to keep within the
	 * above-mentioned circle. The closest are kept.
	 * It means that we create at most
	 * MAX_CLOSEST_GEOVERTICES WalkSegments from the start to
	 * Stops, or from Stops to the destination.
	 */
	private static final int MAX_CLOSEST_GEOVERTICES = 5;

	private final GeographicVertex startPoint;
	private final GeographicVertex targetPoint;
	private final RouteOptimization optimizationChoice;
	private final LocalTime startTime;

	/**
	 * Constructor of OptimizedRouteBetweenPositionsQuery
	 * @param out the outputStream where the result will be written
	 * @param startPoint geographic Vertex of the starting point
	 * @param targetPoint geographic Vertex of the starting point
	 * @param optimizationChoice optimization method used
	 * @param startTime starting time when the travel start
	 * @throws NullPointerException if any argument is null except out
	 */
	public OptimizedRouteBetweenPositionsQuery( OutputStream out,
												GeographicVertex startPoint ,
												GeographicVertex targetPoint,
												RouteOptimization optimizationChoice,
												LocalTime startTime ) {
		super(out);
		Objects.requireNonNull(startPoint);
		Objects.requireNonNull(targetPoint);
		Objects.requireNonNull(optimizationChoice);
		Objects.requireNonNull(startTime);
		this.startPoint = startPoint;
		this.targetPoint = targetPoint;
		this.optimizationChoice = optimizationChoice;
		this.startTime = startTime;
	}

	private void addWalkSegments(TransportNetwork network,
			GeographicVertex gv, boolean outgoing) {
		if (!(gv instanceof Stop)) { // We created the GeographicVertex
			var distanceMap = GeographicVertex.makeDistanceSortedMap(
					gv, network.getGeographicVertices());
			List<GeographicVertex> closestGVs =
					GeographicVertex.getNClosestGVsWithinRadiusOrLeastDistantGV(
							MAX_CLOSEST_GEOVERTICES, GEOVERTEX_SEARCH_RADIUS, distanceMap);

			for (GeographicVertex v : closestGVs) {
				var ws = outgoing ? new WalkSegment(gv, v) : new WalkSegment(v, gv);
				network.addWalkSegment(ws);
			}
		}
	}

	/**
	 * This method take a geographic vertex and try to find a Stop in the transport network
	 * that have exactly the same position, if a Stop if found then it's returned,
	 * otherwise it add the geographic vertex to the network and return the same geographic vertex;
	 * @param network the network where we try to find the Stop
	 * @param gv the geographic vertex we try to convert to a Stop
	 * @return a geographic vertex, which is either instance of a Stop or the same gv as passed to method's arguments
	 */
	private GeographicVertex getStopFromGeoVertex(TransportNetwork network, GeographicVertex gv) {
		if (!(gv instanceof Stop)) {
			GeographicVertex startStop = network.getStopFromPosition( gv.getGeographicPosition() );
			if(startStop == null) {
				network.addGeographicVertex( gv );
			} else
				return startStop;
		}
		return gv;
	}

	/**
	 * For use by subclasses. Run at the start of run().
	 * @param network the network this Query acts on
	 */
	protected void pre(TransportNetwork network) {
		// For use by subclasses
	}

	/**
	 * Returns an optimized route between two positions. If one of the positions
	 * matches a Stop that is in the transport network, the route uses it
	 * as start/destination. If the start or destination is not a Stop,
	 * we add a few WalkSegments to connect it to the nearest Stops. Then we
	 * apply a time or distance-optimized Dijkstra algorithm to get a good
	 * route, and we finally remove the start, destination and WalkSegments
	 * we added.
	 * @param network the network this Query acts on
	 * @return a route going from the start position to the destination position
	 * @throws QueryFailureException if anything fails
	 */
	@Override
	protected String run( TransportNetwork network ) throws QueryFailureException {
		pre(network);

		GeographicVertex startGV = getStopFromGeoVertex(network, startPoint);
		GeographicVertex targetGV = getStopFromGeoVertex(network, targetPoint);

		if (startGV.equals(targetGV))
			throw new QueryFailureException("Start and target stop should be different");

		this.addWalkSegments(network, startGV, true);
		this.addWalkSegments(network, targetGV, false);

		BiFunction<Double, Edge, Double> optimizationBiFun;
		if(optimizationChoice == RouteOptimization.DISTANCE)
			optimizationBiFun = new DistanceAsWeight(network);
		else if(optimizationChoice == RouteOptimization.TIME){
			optimizationBiFun = new TravelTimeAsWeight( startTime, network );
		} else
			throw new UnsupportedOperationException("Optimization choice doesn't exists or is not yet supported");

		network.setTraversalStrategy( new DijkstraTraversalStrategy() );

		Map<Vertex, Edge> traversal =  network.traversal( startGV, targetGV,
				optimizationBiFun, true );

		try {
			List<Edge> path = Graph.getRouteFromTraversal( traversal, startGV, targetGV );
			return network.getRouteDescription( path, startTime );
		} catch( NoSuchElementException | IllegalStateException e ) {
			throw new QueryFailureException("Impossible to find a route");
		} finally {
			for (WalkSegment ws : network.getWalkSegments()) network.removeWalkSegment(ws);
			if (!(startGV instanceof Stop)) network.removeGeographicVertex(startGV);
			if (!(targetGV instanceof Stop)) network.removeGeographicVertex(targetGV);
		}
	}

}
