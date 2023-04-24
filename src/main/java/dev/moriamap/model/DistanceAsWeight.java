package dev.moriamap.model;

import java.time.Duration;
import java.time.LocalTime;
import java.util.function.*;
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
     * {@return the length in kilometers of edge if it is a TransportSegment}
     * @param current ignored
     * @param edge the Edge whose length we want
     */
    @Override public Double apply(Double current, Edge edge) {
        Objects.requireNonNull(current);
        Objects.requireNonNull(edge);
        if (edge instanceof TransportSegment ts) {
            Duration nextFromSchdl = tn.getPassages( (Stop) ts.getFrom() ).getWaitTimeWithWrap
                      ( LocalTime.MIN, ts.getVariantName(), ts.getLineName());
            if(nextFromSchdl == null)
                return Double.POSITIVE_INFINITY;
            return ts.getDistance();
        }
        throw new UnsupportedOperationException("Edge is not TransportSegment");
    }
}
