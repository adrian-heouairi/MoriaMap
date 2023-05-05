package dev.moriamap.model.network;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * A geographic position is a pair of angles that describe the offset to the equator and to the
 * prime meridian (without taking into account the altitude).
 */
public final class GeographicPosition {

  // Maximum latitude value in degrees.
  private static final double MAX_LATITUDE = 90.0;

  // Minimum latitude value in degrees.
  private static final double MIN_LATITUDE = -90.0;

  // Maximum longitude value in degrees.
  private static final double MAX_LONGITUDE = 180.0;

  // Minimum longitude value in degrees.
  private static final double MIN_LONGITUDE = -180.0;

  /** The geographic position of the Null Island. */
  public static final GeographicPosition NULL_ISLAND = new GeographicPosition(0.0, 0.0);

  /** The geographic position of the North Pole on Earth. */
  public static final GeographicPosition NORTH_POLE = new GeographicPosition(90.0, 0.0);

  /** The geographic position of the South Pole on Earth. */
  public static final GeographicPosition SOUTH_POLE = new GeographicPosition(-90.0, 0.0);

  /** The radius of the Earth in meters. */
  public static final double EARTH_RADIUS = 6371000.0;

  // The angle between north and south poles on a sphere in degrees.
  private final double latitude;

  // The angle between west and east poles on a sphere in degrees.
  private final double longitude;

  // Returns true if lat <= MAX_LATITUDE && lat >= MIN_LATITUDE
  private static boolean isValidLatitude(double lat) {
    return lat <= MAX_LATITUDE && lat >= MIN_LATITUDE;
  }

  // Returns true if lon <= MAX_LONGITUDE && lon >= MIN_LONGITUDE
  private static boolean isValidLongitude(double lon) {
    return lon <= MAX_LONGITUDE && lon >= MIN_LONGITUDE;
  }

  /**
   * Creates a new GeographicPosition at specified latitude and longitude.
   *
   * @param latitude a value between +/- 90 degrees
   * @param longitude a value between +/- 180 degrees
   * @return a new GeographicPosition at latitude and longitude
   * @throws IllegalArgumentException if the values for latitude or longitude are not valid
   */
  public static GeographicPosition at(double latitude, double longitude) {
    if (isValidLatitude(latitude) && isValidLongitude(longitude))
      return new GeographicPosition(latitude, longitude);
    throw new IllegalArgumentException("Illegal geographic position");
  }

  /**
   * Converts the specified GeographicPositions to cartesian representation and computes the
   * euclidean distance in meters between the obtained vectors.
   *
   * @param p1 a GeographicPosition
   * @param p2 a GeographicPosition
   * @param radius the radius of the sphere on which p1 and p2 are
   * @return the euclidean distance between p1 and p2
   * @throws IllegalArgumentException if radius is inferior or equal to 0.0
   */
  public static double euclideanDistance(
      GeographicPosition p1, GeographicPosition p2, double radius) {
    double[] p1Cart = p1.toCartesian(radius);
    double[] p2Cart = p2.toCartesian(radius);
    double res = 0.0;
    for (int i = 0; i < p1Cart.length; i++) {
      res += Math.pow(p2Cart[i] - p1Cart[i], 2);
    }
    return Math.sqrt(res);
  }

  /**
   * Returns the euclidean distance in meters between the specified GeographicPosition on Earth.
   * Equivalent to `euclideanDistance(p1, p2, EARTH_RADIUS)`.
   *
   * @param p1 a GeographicPosition
   * @param p2 a GeographicPosition
   * @return the euclidean distance between p1 and p2 with Earth's radius
   */
  public static double euclideanDistanceOnEarth(GeographicPosition p1, GeographicPosition p2) {
    return euclideanDistance(p1, p2, EARTH_RADIUS);
  }

  /**
   * Computes the euclidean distance in meters on Earth from this GeographicPosition to the
   * specified GeographicPosition.
   *
   * @param other a GeographicPosition
   * @return `euclideanDistanceOnEarth(this, other)`
   */
  public double distanceFrom(GeographicPosition other) {
    return GeographicPosition.euclideanDistanceOnEarth(this, other);
  }

  // Class constructor specifying latitude and longitude coordinates.
  private GeographicPosition(double latitude, double longitude) {
    this.latitude = latitude;
    this.longitude = longitude;
  }

  /**
   * Gets the value of the angle in degrees formed by this GeographicPosition and the Equator
   * (latitude of 0.0).
   *
   * @return the latitude coordinate of this GeographicPosition
   */
  public double getLatitude() {
    return this.latitude;
  }

  /**
   * Gets the value of the angle in degrees formed by this GeographicPosition and the prime meridian
   * (longitude of 0.0).
   *
   * @return the longitude coordinate of this GeographicPosition
   */
  public double getLongitude() {
    return this.longitude;
  }

  /**
   * Returns an array that correspond to the cartesian representation of this GeographicPosition on
   * a sphere of specified radius. The projection is computed as follows:
   *
   * <p>X-axis = radius * cos(this.latitude) * cos(this.longitude) Y-axis = radius *
   * cos(this.latitude) * sin(this.longitude) Z-axis = radius * sin(this.latitude)
   *
   * <p>and each value is rounded to 4 decimal places for 11.1m precision at the equator.
   *
   * @param radius the distance from this GeographicPosition and the center of the XYZ coordinate
   *     system
   * @return an array containing the projection on each axis of this GeographicPosition in the order
   *     X Y Z
   * @throws IllegalArgumentException if radius is negative
   */
  public double[] toCartesian(double radius) {
    if (radius <= 0.0) throw new IllegalArgumentException("Radius can not be negative");
    double latRad = this.latitude * Math.PI / 180.0;
    double lonRad = this.longitude * Math.PI / 180.0;
    double z = radius * Math.sin(latRad);
    double tmp = radius * Math.cos(latRad);
    double x = tmp * Math.cos(lonRad);
    double y = tmp * Math.sin(lonRad);
    final int places = 4;
    BigDecimal bdx = new BigDecimal(Double.toString(x));
    BigDecimal bdy = new BigDecimal(Double.toString(y));
    BigDecimal bdz = new BigDecimal(Double.toString(z));
    return new double[] {
      bdx.setScale(places, RoundingMode.HALF_UP).doubleValue(),
      bdy.setScale(places, RoundingMode.HALF_UP).doubleValue(),
      bdz.setScale(places, RoundingMode.HALF_UP).doubleValue()
    };
  }

  /**
   * Computes the cartesian representation with `toCartesian` and converts the result to a list.
   *
   * @param radius the distance from this GeographicPosition and the center of the XYZ coordinate
   *     system
   * @return a list containing the cartesian representation of this GeographicPosition in the order
   *     X Y Z
   */
  public List<Double> toCartesianAsList(double radius) {
    double[] res = this.toCartesian(radius);
    List<Double> resAsList = new ArrayList<>();
    for (double re : res) resAsList.add(re);
    return resAsList;
  }

  /**
   * Returns true if this.latitude == ((GeographicPosition)object).latitude and this.longitude ==
   * ((GeographicPosition)object).longitude.
   *
   * @return this.latitude == ((GeographicPosition)object).latitude and this.longitude ==
   *     ((GeographicPosition)object).longitude
   */
  @Override
  public boolean equals(Object object) {
    if (this == object) return true;
    if (object == null || object.getClass() != this.getClass()) return false;
    GeographicPosition other = (GeographicPosition) object;
    return other.latitude == this.latitude && other.longitude == this.longitude;
  }

  /**
   * Returns the hash code of this GeographicPosition.
   *
   * @return the hash code of this GeographicPosition.
   */
  @Override
  public int hashCode() {
    final int prime = 7;
    int hash = 1;
    hash *= prime;
    hash += Math.ceil(this.latitude);
    hash *= prime;
    hash += Math.ceil(this.longitude);
    return hash;
  }

  /**
   * Two format : Decimal or complete Decimal : 2.17 Degree Minute Second : 24 12 35 N
   *
   * @param latitude a String that contains the latitude in one of the two format
   * @param longitude a String that contains the longitude in one of the two format
   * @return a GeographicPosition
   */
  public static GeographicPosition from(String latitude, String longitude) {
    if (latitude == null || longitude == null)
      throw new IllegalArgumentException("one of the argument is null");

    double latitudeDouble;
    double longitudeDouble;

    if (latitude.contains("N")
        || latitude.contains("S")) { // if contains N or S then Complete Format for latitude
      latitudeDouble = readCompleteFormat(latitude);
    } else {
      try {
        latitudeDouble = Double.parseDouble(latitude);
      } catch (Exception e) {
        throw new IllegalArgumentException("Wrong format for the latitude");
      }
    }

    if (longitude.contains("E") || longitude.contains("W")) {
      longitudeDouble = readCompleteFormat(longitude);
    } else {
      try {
        longitudeDouble = Double.parseDouble(longitude);
      } catch (Exception e) {
        throw new IllegalArgumentException("Wrong format for the latitude");
      }
    }

    return GeographicPosition.at(latitudeDouble, longitudeDouble);
  }

  /**
   * read the Degree Minute Second and return a Decimal in the form double
   *
   * @param str either the latitude or the longitude in this format : 24 12 35 N
   * @return a double corresponding to the decimal format
   */
  private static Double readCompleteFormat(String str) {
    String[] array = str.split(" ");

    if (str.matches("^(\\d+ ){3}[NESW]$")) {
      Double decimal = Double.parseDouble(array[0]);
      Double minute = Double.parseDouble(array[1]) / 60;
      Double second = Double.parseDouble(array[2]) / 3600;
      int orientation = 1;
      if (array[3].equals("S") || array[3].equals("W")) orientation = -1;

      return (decimal + minute + second) * orientation;
    } else {
      throw new IllegalArgumentException("Wrong format of string for the latitude/longitude");
    }
  }
}
