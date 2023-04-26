package dev.moriamap.model;

import java.io.OutputStream;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.BiFunction;

/**
 * Query that compute and print the shortest path from a starting stop
 * to a target stop with the selected optimization way
 */
public class PLAN1Query extends Query {

	private final String startStopName;
	private final String targetStopName;
	private final RouteOptimization optimizationChoice;
	private final LocalTime startTime;

	/**
	 * Constructor of PLAN1Query
	 * @param out the outputStream where the result will be written
	 * @param startStopName name of the starting stop
	 * @param targetStopName name of the target stop
	 * @param optimizationChoice optimization method used
	 * @param startTime starting time when the travel start
	 */
	public PLAN1Query( OutputStream out,
					   String startStopName,
					   String targetStopName,
					   RouteOptimization optimizationChoice,
					   LocalTime startTime) {
		super(out);
		Objects.requireNonNull(startStopName);
		Objects.requireNonNull(targetStopName);
		Objects.requireNonNull(optimizationChoice);
		Objects.requireNonNull(startTime);
		this.startStopName = startStopName;
		this.targetStopName = targetStopName;
		this.optimizationChoice = optimizationChoice;
		this.startTime = startTime;
	}

	@Override
	protected String run( TransportNetwork network ) throws QueryFailureException {
		BiFunction<Double, Edge, Double> optimizationBiFun;
		if(optimizationChoice == RouteOptimization.DISTANCE)
			optimizationBiFun = new DistanceAsWeight(network);
		else if(optimizationChoice == RouteOptimization.TIME){
			optimizationBiFun = new TravelTimeAsWeight( startTime, network );
		} else
			throw new UnsupportedOperationException("Optimization choice doesn't exists or is not yet supported");
		network.setTraversalStrategy( new DijkstraTraversalStrategy() );

		Stop start = network.getStopByName( startStopName );
		Stop target = network.getStopByName( targetStopName );
		if(start == null || target == null)
			throw new QueryFailureException("One of the stops was not found");
		Map<Vertex, Edge> traversal =  network.traversal( start, target, optimizationBiFun, true );
		try {
			List<Edge> path = Graph.getRouteFromTraversal( traversal, start, target );
			return network.getRouteDescription( path, startTime );
		} catch( NoSuchElementException | IllegalStateException e ) {
			throw new QueryFailureException("Impossible to get to destination because there " +
											"are no transports going to '" + targetStopName + "'");
		}
	}

}
