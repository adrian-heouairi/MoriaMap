package dev.moriamap.model.network.traversal;

import dev.moriamap.model.network.Edge;
import dev.moriamap.model.network.Stop;
import dev.moriamap.model.network.TransportNetwork;
import dev.moriamap.model.network.TransportSegment;
import dev.moriamap.model.network.WalkSegment;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Objects;
import java.util.function.BiFunction;

/** */
public class TravelTimeAsWeight implements BiFunction<Double, Edge, Double> {

  /** Time when the travel started */
  LocalTime startTime;

  /** Transport network the travel is on */
  TransportNetwork tn;

  /**
   * Constructor for TravelTimeAsWeight
   *
   * @param startTime Time when the travel started
   * @param tn Transport network the travel is on
   */
  public TravelTimeAsWeight(LocalTime startTime, TransportNetwork tn) {
    if (startTime == null || tn == null)
      throw new IllegalArgumentException("startTime or tn can't be null");
    this.startTime = startTime;
    this.tn = tn;
  }

  @Override
  public Double apply(Double current, Edge edge) {
    Objects.requireNonNull(current);
    Objects.requireNonNull(edge);
    if (edge instanceof TransportSegment segment) {
      LocalTime time = startTime.plusSeconds(current.longValue());
      Duration nextFromSchdl =
          tn.getPassages((Stop) segment.getFrom())
              .getWaitTimeWithWrap(time, segment.getVariantName(), segment.getLineName());
      if (nextFromSchdl == null) return Double.POSITIVE_INFINITY;
      return (double) nextFromSchdl.toSeconds() + segment.getTravelDuration().toSeconds();
    } else if (edge instanceof WalkSegment segment) {
      return (double) segment.travelTime().toSeconds();
    }
    throw new UnsupportedOperationException(
        "Invalid edge type. The apply() method can only handle TransportSegment and WalkSegment edges.");
  }
}
