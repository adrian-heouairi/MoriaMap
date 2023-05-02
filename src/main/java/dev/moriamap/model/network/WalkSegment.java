package dev.moriamap.model.network;

import java.time.Duration;

/**
 * A WalkSegment represents the action of walking from a geographic vertex to
 * another geographic vertex.
 */
public class WalkSegment extends Edge {

    /**
     * Walk speed in km/h.
     */
    public static final double WALK_SPEED = 2.0;

    /**
     * Drudgery factor of the action of walking.
     */
    public static final double WALK_DRUDGERY = 10.0;

    /**
     * The distance between the ends of this WalkSegment in meters.
     */
    public final double distance;

    /**
     * Class constructor specifying origin and destination. The ends are
     * considered on Earth.
     * @param from the GeographicVertex where this WalkSegment starts
     * @param to the GeographicVertex where this WalkSegment ends
     */
    public WalkSegment(GeographicVertex from, GeographicVertex to) {
        super(from, to);
        this.distance = from.getGeographicPosition()
            .distanceFrom(to.getGeographicPosition());
    }

    /**
     * {@return the travel duration of this WalkSegment}
     */
    public Duration travelTime() {
        return Duration.ofSeconds((long) (this.distance
                                        / (WalkSegment.WALK_SPEED / 3.6)));
    }
}
