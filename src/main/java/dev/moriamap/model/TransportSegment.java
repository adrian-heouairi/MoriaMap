package dev.moriamap.model;
import java.time.Duration;

/**
 * A TransportSegment is an Edge of our Graph
 */
public class TransportSegment extends Edge{

    /** The variant's name of this TransportSegment */
    public final String variantName;

    /** The line's name of this TransportSegment */
    public final String lineName;

    /** The travel time of this TransportSegment */
    public final Duration travelTime;

    /** The distance of this TransportSegment */
    public final double distance;

    private TransportSegment(Vertex from, Vertex to,String variantName, String lineName,Duration travelTime, double distance){
        super(from, to);
        this.variantName = variantName;
        this.lineName = lineName;
        this.travelTime = travelTime;
        this.distance = distance;
    }

    /**
     * Static factory method returning a TransportSegment
     * @param from The origin of this TransportSegment
     * @param to The destination of this TransportSegment
     * @param lineName The name of the line of this TransportSegment
     * @param variantName The name of the variant of this TransportSegment
     * @param travelTime The travel time of this TransportSegment
     * @param distance The distance of this Transport Segment
     * @return a new TransportSegment
     */
    public static TransportSegment from(Vertex from, Vertex to, String lineName,String variantName, Duration travelTime, double distance){
        if(lineName == null || variantName == null || travelTime == null)
            throw new IllegalArgumentException("Null parameters are not allowed");
        return new TransportSegment(from,to,lineName,variantName,travelTime,distance);
    }

    /**
     * Returns the weight of a TransportSegment. Useful to implement algorithms such as A*.
     * @return the weight of this TransportSegment
     */
    @Override
    public double getWeight(){
        return travelTime.getSeconds() + distance;
    }

    /**
     * Check if this transport segment is equal to the given transport segment.
     * <p>
     *     Two transport segment are equal if they have the same lineVariantName, the same
     *     distance and the same travelTime
     * </p>
     * @param object to be compared to
     * @return true if this is equal to object
     */
    public boolean equals(Object object){
        if (this ==  object)
            return true;
        if (object == null || object.getClass() != this.getClass())
            return false;
        TransportSegment other = (TransportSegment) object;
        return this.lineName.equals(other.lineName) && this.variantName.equals(other.variantName) && this.distance == other.distance && this.travelTime.equals(other.travelTime);
    }

    /**
     * Gets the hash code of this transport segment
     * @return the hash code of this transport segment
     */
    @Override public int hashCode(){
        final int prime = 13;
        int hash = 1;
        hash *= prime;
        hash += this.lineName.hashCode();
        hash += this.variantName.hashCode();
        hash += this.travelTime.hashCode();
        hash += Long.valueOf(Double.doubleToLongBits(this.distance)).hashCode();
        return hash;
    }

}
