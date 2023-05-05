package dev.moriamap.model.query;

import dev.moriamap.model.network.Edge;
import dev.moriamap.model.network.Graph;
import dev.moriamap.model.network.Stop;
import dev.moriamap.model.network.TransportNetwork;
import dev.moriamap.model.network.Vertex;
import dev.moriamap.model.network.traversal.DFSTraversalStrategy;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * A RouteBetweenStopsQuery represent a request for a path from a starting stop to the target stop
 */
public class RouteBetweenStopsQuery extends Query {

  /** The name of the starting stop name */
  private final String startStopName;

  /** The name of the ending stop name */
  private final String targetStopName;

  /**
   * The constructor of RouteBetweenStopsQuery
   *
   * @param out the outputStream where the result will be written
   * @param startStopName the name of the starting stop
   * @param targetStopName the name of the target stop
   * @throws IllegalArgumentException if any of the stop names are null
   */
  public RouteBetweenStopsQuery(OutputStream out, String startStopName, String targetStopName) {
    super(out);
    if (startStopName == null || targetStopName == null)
      throw new IllegalArgumentException("Stops names can't be null");
    this.startStopName = startStopName;
    this.targetStopName = targetStopName;
  }

  @Override
  protected String run(TransportNetwork network) throws QueryFailureException {
    Stop start = network.getStopByName(startStopName);
    Stop target = network.getStopByName(targetStopName);
    if (start == null || target == null)
      throw new QueryFailureException("One of the stops was not found");
    if (start.equals(target))
      throw new QueryFailureException("Start and target stop should be different");
    var traversalStrategy = new DFSTraversalStrategy();
    Map<Vertex, Edge> dfs = traversalStrategy.traversal(start, target, null, true, network);
    List<Edge> path = Graph.getRouteFromTraversal(dfs, start, target);
    return PrettyPrinter.printTransportSegmentPath(network, path);
  }
}
