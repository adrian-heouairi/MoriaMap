package dev.moriamap.model;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * A Plan0Query represent a request for a path from a starting stop to the target stop
 */
public class PLAN0Query extends Query {

	/**
	 * The name of the starting stop name
	 */
	private final String startStopName;

	/**
	 * The name of the ending stop name
	 */
	private final String targetStopName;

	/**
	 * The constructor of PLAN0Query
	 * @param out the outputStream where the result will be written
	 * @param startStopName the name of the starting stop
	 * @param targetStopName the name of the target stop
	 * @throws IllegalArgumentException if any of the stop names are null
	 */
	public PLAN0Query(OutputStream out, String startStopName, String targetStopName) {
		super(out);
		if( startStopName == null || targetStopName == null )
			throw new IllegalArgumentException("Stops names can't be null");
		this.startStopName = startStopName;
		this.targetStopName = targetStopName;
	}

	@Override
	protected String run( TransportNetwork network ) throws QueryFailureException {
		Stop start = network.getStopByName( startStopName );
		Stop target = network.getStopByName( targetStopName );
		if(start == null || target == null)
			throw new QueryFailureException("One of the stops was not found");
		Map<Vertex, Edge> dfs = network.depthFirstSearch( start );
		List<Edge> path = Graph.getRouteFromTraversal( dfs, start, target );
		return PrettyPrinter.printTransportSegmentPath(network, path );
	}

}
