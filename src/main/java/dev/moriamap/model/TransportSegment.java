package dev.moriamap.model;
import java.time.Duration;

/**
 * A TransportSegment is an Edge of our Graph
 */
public class TransportSegment extends Edge{

    /** The variant's name of this TransportSegment */
    private String variantName;

    /** The line's name of this TransportSegment */
    private String lineName;

    /** The travel duration of this TransportSegment */
    private Duration travelDuration;

    /** The distance of this TransportSegment */
    private double distance;

    private TransportSegment(Stop from, Stop to,String lineName, String variantName,Duration travelDuration, double distance){
        super(from, to);
        this.variantName = variantName;
        this.lineName = lineName;
        this.travelDuration = travelDuration;
        this.distance = distance;
    }

    /**
     * Static factory method returning a TransportSegment
     * @param from The origin of this TransportSegment
     * @param to The destination of this TransportSegment
     * @param lineName The name of the line of this TransportSegment
     * @param variantName The name of the variant of this TransportSegment
     * @param travelDuration The travel time of this TransportSegment
     * @param distance The distance of this Transport Segment
     * @return a new TransportSegment
     */
    public static TransportSegment from(Stop from, Stop to, String lineName,String variantName, Duration travelDuration, double distance){
        if(lineName == null || variantName == null || travelDuration == null)
            throw new IllegalArgumentException("Null parameters are not allowed");
        return new TransportSegment(from,to,lineName,variantName,travelDuration,distance);
    }

    /**
     * Returns the weight of a TransportSegment. Useful to implement algorithms such as A*.
     * @return the weight of this TransportSegment
     */
    @Override
    public double getWeight(){
        return travelDuration.getSeconds() + distance;
    }

    /**
     * {@return the line's name of this TransportSegment}
     */
    public String getLineName(){
        return this.lineName;
    }

    /**
     * {@return the variant's name of this TransportSegment}
     */
    public String getVariantName(){
        return this.variantName;
    }

    /**
     * {@return the travel duration of this TransportSegment}
     */
    public Duration getTravelDuration(){
        return this.travelDuration;
    }

    /**
     * {@return the distance of this TransportSegment}
     */
    public double getDistance(){
        return this.distance;
    }

    /**
     * Check if this transport segment is equal to the given transport segment.
     * <p>
     *     Two transport segment are equal if they have the same from, the same to, 
     *      lineVariantName, the same distance and the same travelDuration
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
        return (this.getFrom()).equals(other.getFrom()) && (this.getTo()).equals(other.getTo()) 
               && this.lineName.equals(other.lineName) && this.variantName.equals(other.variantName) 
               && this.distance == other.distance && this.travelDuration.equals(other.travelDuration);
    }

    /**
     * Gets the hash code of this transport segment
     * @return the hash code of this transport segment
     */
    @Override public int hashCode(){
        final int prime = 13;
        int hash = 1;
        hash *= prime;
        hash += ((Stop)this.getFrom()).hashCode();
        hash += ((Stop)this.getTo()).hashCode();
        hash += this.lineName.hashCode();
        hash += this.variantName.hashCode();
        hash += this.travelDuration.hashCode();
        hash += Long.valueOf(Double.doubleToLongBits(this.distance)).hashCode();
        return hash;
    }

}
