package dev.moriamap.model;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Line variant, a unidirectional traversal of a Line such that the
 * first Stop has no incoming transport segments and the last Stop has no
 * outgoing transport segments, without loops between them.
 */
public final class Variant {
    private static final String NULL_ARG_ERR_MSG = "Argument can not be null";

    /**
     * The name of this Variant.
     */
    private String name;

    /**
     * The name of the owner Line.
     */
    private String lineName;

    /**
     * The TransportSegments in this Variant.
     */
    private List<TransportSegment> transportSegments;

    /**
     * The departures of transports from this Variant first Stop.
     */
    private List<LocalTime> departures;

    /**
     * Class constructor specifying variant name and owner line name
     * @param name the name of this Variant
     * @param lineName the name of the owner Line of this Variant
     */
    private Variant(String name, String lineName) {
        this.name = name;
        this.lineName = lineName;
        this.transportSegments = new ArrayList<>();
        this.departures = new ArrayList<>();
    }

    /**
     * {@return a new empty Variant with the given id and owner line name}
     * @param name the name of this Variant
     * @param lineName the owner Line name of this Variant
     * @throws IllegalArgumentException if an argument is null
     */
    public static Variant empty(String name, String lineName) {
        if (lineName == null || name == null) {
            throw new IllegalArgumentException(NULL_ARG_ERR_MSG);
        }
        return new Variant(name, lineName);
    }

    /**
     * Returns the first Stop in the traversal of this Variant. 
     * The first Stop is such that there is no transportSegment that includes it
     * as a destination in this variant. The variant must have only one first
     * stop and be in the shape of a line (not a circular shape or a fork).
     * @return the first Stop of this Variant
     */
    public Stop getStart(){
        List<Stop> stops = new ArrayList<>();
        List<Stop> prevTo = new ArrayList<>();
        for(TransportSegment ts : transportSegments){
            Stop from = (Stop)ts.getFrom();
            Stop to = (Stop)ts.getTo();
            prevTo.add(to);
            if(stops.contains(to))
                stops.remove(to);
            if(!prevTo.contains(from))
                stops.add(from);
        }
        return stops.get(0);
    }

    /**
     * Get the last Stop in the traversal of this Variant.
     * The last Stop is such that there is no transportSegment that includes it
     * as a departure in this variant.
     * The variant must have only one first stop and be in the shape of a line.
     * @return the last Stop of this Variant
     */
    public Stop getEnd(){
        List<Stop> stops = new ArrayList<>();
        List<Stop> prevFrom = new ArrayList<>();
        for(TransportSegment ts : transportSegments){
            Stop from = (Stop)ts.getFrom();
            Stop to = (Stop)ts.getTo();
            prevFrom.add(from);
            if(stops.contains(from))
                stops.remove(from);
            if(!prevFrom.contains(to))
                stops.add(to);
        }
        return stops.get(0);
    }

    /**
     * Adds the given TransportSegment to this Variant.
     * @param ts the TransportSegment to add
     * @return true if the given TransportSegment was added
     * @throws IllegalArgumentException if ts is null
     */
    public boolean addTransportSegment(TransportSegment ts){
        if (ts == null)
            throw new IllegalArgumentException(NULL_ARG_ERR_MSG);
        if (this.transportSegments.contains(ts))
            return false;
        return this.transportSegments.add(ts);
    }

    /**
     * Adds the given departure to this Variant.
     * @param departure the departure to be add
     * @return true if the given departure was added
     * @throws IllegalArgumentException if departure is null
     */
    public boolean addDeparture(LocalTime departure){
        if (departure == null)
            throw new IllegalArgumentException(NULL_ARG_ERR_MSG);
        if (this.departures.contains(departure))
            return false;
        return this.departures.add(departure);
    }

    /**
     * {@return the name of this Variant}
     */
    public String getName(){
        return this.name;
    }

    /**
     * {@return the lineName of this Variant}
     */
    public String getLineName(){
        return this.lineName;
    }

    /**
     * {@return a copy of this Variant's transport departures list}
     */
    public List<LocalTime> getDepartures(){
        return new ArrayList<>(this.departures);
    }

    /**
     * {@return a copy of this Variant's transport segments}
     */
    public List<TransportSegment> getTransportSegments(){
        return new ArrayList<>(this.transportSegments);
    }

    /**
     * Check if this variant is equal to the given line.
     * <p>
     *     Two variants are equal if they have the same lineName, the same id 
     *     and the same (by a call to equals) transportSegments in the same
     *     order.
     * </p>
     * @param object the Object to compare
     * @return true if this is equal to object
     */
    @Override public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null || object.getClass() != this.getClass())
            return false;
        Variant other = (Variant) object;
        return other.lineName.equals(this.lineName)
            && other.name.equals(this.name)
            && other.departures.equals(this.departures)
            && other.transportSegments.equals(this.transportSegments);
    }

    /**
     * {@return the hash code of this Variant}
     */
    @Override public int hashCode(){
        final int prime = 13;
        int hash = 1;
        hash *= prime;
        hash += this.name.hashCode();
        hash += this.lineName.hashCode();
        for(TransportSegment ts : this.transportSegments) hash += ts.hashCode();
        for(LocalTime t : this.departures) hash += t.hashCode();
        return hash;
    }
}
