package dev.moriamap.model;

/**
 * A Vertex at a geographic position.
 */
public class GeographicVertex extends Vertex {

    // The geographic position of this GeographicVertex
    private final GeographicPosition geographicPosition;

    /**
     * Class constructor specifying geographic position.
     *
     * @param pos the geographic position of this new GeographicVertex
     */
    protected GeographicVertex(GeographicPosition pos) {
        this.geographicPosition = pos;
    }

    /**
     * Creates a new GeographicVertex at specified position.
     *
     * @param  pos the geographic position of this new GeographicVertex
     * @return a new GeographicVertex at specified position
     */
    public static GeographicVertex at(GeographicPosition pos) {
        return new GeographicVertex(pos);
    }

    /**
     * Creates a new GeographicVertex at specified latitude and longitude.
     *
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
     *
     * @return the geographic position of this GeographicVertex.
     */
    public GeographicPosition getGeographicPosition() {
        return this.geographicPosition;
    }
}
