package dev.moriamap.model;

import java.util.function.*;
import java.time.*;

/**
 * A function that returns the weight of an Edge depending on its travel time.
 */
public class TravelTimeAsWeight implements BiFunction<Double, Edge, Double> {
    private LocalTime departureTime;
    private TransportNetwork network;

    /**
     * Class constructor specifying departure time and network.
     * @param departureTime the time of departure
     * @param network the traversed network
     */
    public TravelTimeAsWeight(LocalTime departureTime,
                              TransportNetwork network) {
        this.departureTime = departureTime;
        this.network = network;
    }

    @Override public Double apply(Double current, Edge edge) {
        return current;
    }
}
