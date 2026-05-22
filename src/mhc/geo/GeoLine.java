package mhc.geo;

/**
 * The GeoLine class represents a line segment defined by two geographic points.
 */
public record GeoLine(GeoPoint start, GeoPoint end) {
  
  /**
   * Constructs a new {@link GeoLine} with the specified start and end points.
   *
   * @param start the starting point of the line
   * @param end the ending point of the line
   */
  public GeoLine {
    if ((start == null) || (end == null))
      throw new IllegalArgumentException("Start and end points cannot be null.");
  }
  
  @Override
  public String toString() {
    return String.format("GeoLine(start=%s, end=%s)", start, end);
  }

  /**
   * Returns the length of the line segment in feet.
   *
   * @return the length of the line segment in feet
   */
  public double length() {
    return Geo.distance(start, end);
  }
  
  /**
   * Creates a new {@link GeoLine} from the specified start point, distance in feet, and
   * bearing in degrees.
   *
   * @param start the starting point of the line
   * @param distanceFeet the distance to project in feet
   * @param bearingDegrees the bearing in degrees from the north
   * @return a new {@link GeoLine} instance
   */
  public static GeoLine fromBearingVal(GeoPoint start, double distanceFeet, double bearingDegrees) {
    return fromBearing(start, distanceFeet, new Bearing(bearingDegrees));
  }
  
  /// Creates a new {@link GeoLine} from the specified start point, distance in feet, and
  /// bearing as a {@link Bearing} object.
  ///
  /// @param start the starting point of the line
  /// @param distanceFeet the distance to project in feet
  /// @param bearing the bearing as a {@link Bearing} object
  /// @return a new {@link GeoLine} instance
  ///
  public static GeoLine fromBearing(GeoPoint start, double distanceFeet, Bearing bearing) {
    GeoPoint end = Geo.projectPt(start, bearing, distanceFeet);
    return new GeoLine(start, end);
  }

  /**
   * Returns the bearing from the start point to the end point in degrees.
   *
   * @return the bearing in degrees
   */
  private double bearingVal() {
    return Geo.bearing(start, end).degrees();
  }

  /// Returns the bearing from the start point to the end point as a {@link Bearing} object.
  ///
  /// @return the bearing as a {@link Bearing} object
  ///
  public Bearing bearing() {
    return Geo.bearing(start, end);
  }
}
