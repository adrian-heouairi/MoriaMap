package dev.moriamap.model;

import java.util.*;

/**
 * A Vertex at a geographic position.
 */
public class GeographicVertex implements Vertex {

    /**
     * The geographic position of this GeographicVertex.
     */
    protected final GeographicPosition geographicPosition;

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
        hash += this.geographicPosition.hashCode();
        return hash;
    }

    /**
     * Given a center and a list of GeographicVertices
     * (which may include the center), returns a sorted map associating each GeographicVertex
     * to its distance on Earth in meters from the center. The center is not in the sorted map.
     * @param center the center used to calculate distances
     * @param geographicVertices the vertices for which we want the sorted map
     * @return a sorted map associating each GeographicVertex to its distance from the center
     */
    public static SortedMap<Double, GeographicVertex> makeDistanceSortedMap(
            GeographicVertex center,
            List<GeographicVertex> geographicVertices
    ) {
        SortedMap<Double, GeographicVertex> res = new TreeMap<>();

        GeographicPosition gpOfCenter = center.getGeographicPosition();
        for (GeographicVertex gv : geographicVertices) {
            if (gv.equals(center)) continue;

            GeographicPosition gp = gv.getGeographicPosition();
            res.put(gpOfCenter.distanceFrom(gp), gv);
        }

        return res;
    }

    /**
     * {@return a list of between 0 and n elements, containing the geographic vertices
     * of distanceSortedMap which are the closest to its center and inside the circle
     * of radius radius}
     * @param n the maximum number of GeographicVertices to return
     * @param radius the radius of the circle inside which returned elements must
     *               be, in meters
     * @param distanceSortedMap the geographic vertices we want to consider, in
     *                          a centered sorted map. See
     *                          {@link #makeDistanceSortedMap(GeographicVertex, List)}
     */
    public static List<GeographicVertex> getNClosestGVsWithinRadius(
            int n, double radius,
            SortedMap<Double, GeographicVertex> distanceSortedMap) {
        Set<Double> keys = distanceSortedMap.keySet();
        Object[] results = keys.stream()
                .filter(d -> d <= radius)
                .limit(Math.min(keys.size(), n))
                .toArray();

        List<GeographicVertex> ret = new ArrayList<>();
        for (Object key : results) ret.add(distanceSortedMap.get(key));
        return ret;
    }

    /**
     * {@return a list of between 0 and n elements, containing the geographic vertices
     * of distanceSortedMap which are the closest to its center and inside the circle
     * of radius radius. If none are inside the circle, we return a list containing
     * only one element, which is the closest GeographicVertex to the center, no matter
     * the distance}
     * @param n the maximum number of GeographicVertices to return
     * @param radius the radius of the circle inside which returned elements must
     *               be, in meters
     * @param distanceSortedMap the geographic vertices we want to consider, in
     *                          a centered sorted map. See
     *                          {@link #makeDistanceSortedMap(GeographicVertex, List)}
     * @throws NoSuchElementException if distanceSortedMap is empty
     */
    public static List<GeographicVertex> getNClosestGVsWithinRadiusOrLeastDistantGV(
            int n, double radius,
            SortedMap<Double, GeographicVertex> distanceSortedMap) {
        List<GeographicVertex> res = getNClosestGVsWithinRadius(n, radius, distanceSortedMap);
        if (res.isEmpty()) {
            res = new ArrayList<>();
            GeographicVertex[] values = distanceSortedMap.values().toArray(new GeographicVertex[0]);
            if (values.length != 0) res.add(values[0]);
            else throw new NoSuchElementException("Distance-sorted map is empty");
        }
        return res;
    }

    @Override
    public String toString() {
        return String.format("(%3.2f,%3.2f)",
                this.geographicPosition.getLatitude(),
                this.geographicPosition.getLongitude());
    }
}
