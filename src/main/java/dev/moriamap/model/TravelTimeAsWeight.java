package dev.moriamap.model;

import java.time.Duration;
import java.time.LocalTime;
import java.util.function.BiFunction;

/**
 *
 */
public class TravelTimeAsWeight implements BiFunction<Double, Edge, Double> {

	/**
	 * Time when the travel started
	 */
	LocalTime startTime;

	/**
	 * Transport network the travel is on
	 */
	TransportNetwork tn;

	/**
	 * Constructor for TravelTimeAsWeight
	 * @param startTime Time when the travel started
	 * @param tn Transport network the travel is on
	 */
	public TravelTimeAsWeight(LocalTime startTime, TransportNetwork tn) {
		if(startTime == null || tn == null)
			throw new IllegalArgumentException("startTime or tn can't be null");
		this.startTime = startTime;
		this.tn = tn;
	}

	@Override
	public Double apply( Double current, Edge edge ) {
		if( edge instanceof TransportSegment segment) {
			LocalTime time = startTime.plusSeconds( current.longValue() );
			Duration nextFromSchdl = tn.getPassages( (Stop) segment.getFrom() ).getWaitTimeWithWrap
					  ( time, segment.getVariantName(), segment.getLineName());
			if(nextFromSchdl == null)
				return Double.POSITIVE_INFINITY;
			return (double) nextFromSchdl.toSeconds() + segment.getTravelDuration().toSeconds();
		}
		throw new UnsupportedOperationException("Only TransportSegment are implemented for now");
	}

}
