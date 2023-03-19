package dev.moriamap.model;

/**
 * A Vertex at a geographic position.
 */
public class GeographicVertex extends Vertex {

    // The geographic position of this GeographicVertex
    private final GeographicPosition geographicPosition;

    /**
     * Class constructor specifying geographic position.
     * @param pos the geographic position of this new GeographicVertex
     */
    protected GeographicVertex(GeographicPosition pos) {
        this.geographicPosition = pos;
    }

    /**
     * Creates a new GeographicVertex at specified position.
     * @param  pos the geographic position of this new GeographicVertex
     * @return a new GeographicVertex at specified position
     * @throws IllegalArgumentException if pos is null     
     */
    public static GeographicVertex at(GeographicPosition pos) {
        if (pos == null)
            throw new IllegalArgumentException("Position can not be null");
        return new GeographicVertex(pos);
    }

    /**
     * Creates a new GeographicVertex at specified latitude and longitude.
     * @param lat the latitude of this GeographicVertex
     * @param lon the longitude of this GeographicVertex
     * @return a new GeographicVertex at specified geographic position
     * @throws IllegalArgumentException if the values for latitude or longitude
     *         are not valid
     */
    public static GeographicVertex at(double lat, double lon) {
        return new GeographicVertex(GeographicPosition.at(lat, lon));
    }

    /**
     * Gets the geographic position of this GeographicVertex.
     * @return the geographic position of this GeographicVertex.
     */
    public GeographicPosition getGeographicPosition() {
        return this.geographicPosition;
    }

    /**
     * Returns true if this.geographicPosition == ((GeographicVertex)object).geographicPosition.
     *
     * @return this.geographicPosition == ((GeographicVertex)object).geographicPosition
     */
    @Override public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null || object.getClass() != this.getClass())
            return false;
        GeographicVertex other = (GeographicVertex) object;
        return other.geographicPosition.equals(this.geographicPosition);
    }

    /**
     * Returns the hash code of this GeographicVertex.
     *
     * @return the hash code of this GeographicVertex.
     */
    @Override public int hashCode() {
        final int prime = 11;
        int hash = 1;
        hash *= prime;
        hash += Math.ceil(this.geographicPosition.hashCode());
        return hash;
    }
}
