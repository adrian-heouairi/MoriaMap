package dev.moriamap.model.network.traversal;

import java.time.Duration;
import java.time.LocalTime;
import java.util.function.*;

import dev.moriamap.model.network.Edge;
import dev.moriamap.model.network.Stop;
import dev.moriamap.model.network.TransportNetwork;
import dev.moriamap.model.network.TransportSegment;
import dev.moriamap.model.network.WalkSegment;

import java.util.*;

/**
 * The interface {@code BiFunction<Double, Edge, Double>} is used by our Graph
 * traversal strategies (implementing TraversalStrategy) to compute the weight
 * of an Edge only when the algorithm needs it. For a TransportSegment, this
 * class returns its length in kilometers.
 */
public class DistanceAsWeight implements BiFunction<Double, Edge, Double> {

    /**
     * Transport network the travel is on
     */
    TransportNetwork tn;

    /**
     * Constructor for TravelTimeAsWeight
     * @param tn Transport network the travel is on
     */
    public DistanceAsWeight( TransportNetwork tn ) {
        Objects.requireNonNull( tn );
        this.tn = tn;
    }


    /**
     * Returns the weight of the given Edge, considering its distance from one
     * end to the other in kilometers. In the case of a TransportSegment, returns the distance
     * between the Stops it links. In the case of a WalkSegment, returns the
     * Euclidean distance between the geographic points it links, multiplied
     * by a drudgery factor, to take into account that the action of walking
     * implies more effort than travelling using a transport medium.
     * @param current ignored
     * @param edge the Edge whose length we want
     */
    @Override public Double apply(Double current, Edge edge) {
        Objects.requireNonNull(current);
        Objects.requireNonNull(edge);
        if (edge instanceof TransportSegment ts) {
            Duration nextFromSchedule = tn
                    .getPassages( (Stop) ts.getFrom() )
                    .getWaitTimeWithWrap(
                            LocalTime.MIN,
                            ts.getVariantName(),
                            ts.getLineName()
                    );
            if(nextFromSchedule == null)
                return Double.POSITIVE_INFINITY;
            return ts.getDistance();
        } else if (edge instanceof WalkSegment ws) {
            return WalkSegment.WALK_DRUDGERY * ws.distance / 1000.0;
        } else {
            throw new UnsupportedOperationException("Unknown Edge");
        }
    }
}
