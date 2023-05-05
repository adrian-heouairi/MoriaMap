package dev.moriamap.model.network;

import java.time.LocalTime;

/**
 * Represents the time of departure of a transport going from a given Stop towards the end Stop of a
 * Variant.
 */
public record TransportSchedule(LocalTime time, Stop stop, Variant variant) {

  /**
   * Constructor of TransportSchedule
   *
   * @param time at what point a train arrive
   * @param stop the stop hen there train
   * @param variant the variant to which this train belong to
   */
  public TransportSchedule {
    if (time == null || stop == null || variant == null)
      throw new IllegalArgumentException("No TransportSchedule values can be null");
  }
}
