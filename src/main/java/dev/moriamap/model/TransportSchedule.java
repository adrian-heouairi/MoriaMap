package dev.moriamap.model;

import java.time.LocalTime;


/**
 * Record keeping time when a train from a variant
 * get to a stop, for a specific terminus
 */
public record TransportSchedule(
		  LocalTime time,
		  Stop stop,
		  Stop terminus,
		  Variant variant
) {

	/**
	 * Constructor of TransportSchedule
	 * @param time at what point a train arrive
	 * @param stop the stop hen there train
	 * @param terminus the train's direction stop
	 * @param variant the variant to which this train belong to
	 */
	public TransportSchedule {
		if( time == null || stop == null || terminus == null || variant == null )
			throw new IllegalArgumentException( "No TransportSchedule values can be null" );
	}

}
