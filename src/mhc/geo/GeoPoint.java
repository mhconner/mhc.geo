package mhc.geo;

/**
 * This class represents a geographic point defined by latitude and longitude.
 */
public record GeoPoint(double lat, double lon) {

  /**
   * Constructs a new {@link GeoPoint} with the specified latitude and longitude.
   *
   * @param lat the latitude of the point in degrees
   * @param lon the longitude of the point in degrees
   */
  public GeoPoint {
    if ((lat < -90.0) || (lat > 90.0))
      throw new IllegalArgumentException("Latitude must be between -90 and 90 degrees.");
    if ((lon < -180.0) || (lon > 180.0))
      throw new IllegalArgumentException("Longitude must be between -180 and 180 degrees.");
  }
  
  @Override
  public String toString() {
    return String.format("GeoPoint(lat=%.9f, lon=%.9f)", lat, lon);
  }
}
