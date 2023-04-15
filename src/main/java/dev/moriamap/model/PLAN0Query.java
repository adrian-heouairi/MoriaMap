package dev.moriamap.model;

import java.util.List;
import java.util.Map;

/**
 * A Plan0Query represent a request for a path from a starting stop to the target stop

 */
public record PLAN0Query(
		  String startStopName,
		  String targetStopName) implements Query {

	/**
	 * Constructor of PLAN0Query
	 * @param startStopName the name of the starting stop
	 * @param targetStopName the name of the target stop
	 */
	public PLAN0Query {
		if( startStopName == null || targetStopName == null )
			throw new IllegalArgumentException("Stops names can't be null");
	}

	/**
	 * Auxiliary method that run the PLAN0query
	 * @param network The TransportNetwork the query is run on
	 * @return the result of the query to print
	 * @throws QueryFailureException if start or target stops were not found
	 */
	String run( TransportNetwork network ) throws QueryFailureException {
		Stop start = network.getStopByName( startStopName );
		Stop target = network.getStopByName( targetStopName );
		if(start == null || target == null)
			throw new QueryFailureException("One of the stops was not found");
		Map<Vertex, Edge> dfs = network.depthFirstSearch( start );
		List<Edge> path = Graph.getRouteFromTraversal( dfs, start, target );
		return PrettyPrinter.printTransportSegmentPath(network, path );
	}

	@Override
	public void execute( TransportNetwork network ) {
		String result;
		try {
			result = run( network );
		} catch( Exception e ) {
			result =  "Error:" + e.getMessage();
		}
		System.out.println( result );
	}

}
