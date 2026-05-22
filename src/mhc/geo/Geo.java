package mhc.geo;

/// This class provides methods for geographic calculations, such as projecting a point. The
/// methods in this class assume a spherical Earth and use the Haversine formula for distance
/// calculations. Latitude and longitude values are expected to be in decimal degrees. The
/// distance is returned in feet, and the bearing is returned in degrees from the north. The
/// projection methods take distance in feet and bearing in degrees as input parameters. The
/// methods in this class are static and can be called without creating an instance of the
/// [Geo] class.
public class Geo {
  
  /// Returns the bearing from point a to point b as a {@link Bearing} object.
  public static Bearing bearing(GeoPoint a, GeoPoint b) {
    return new Bearing(bearingVal(a, b));
  }
  
  /// Calculates the bearing from point a to point b in degrees.
  ///
  /// @param a the starting geographic point
  /// @param b the destination geographic point
  /// @return the bearing in degrees from point A to point B
  private static double bearingVal(GeoPoint a, GeoPoint b) {
    double lat1 = Math.toRadians(a.lat());
    double lon1 = Math.toRadians(a.lon());
    double lat2 = Math.toRadians(b.lat());
    double lon2 = Math.toRadians(b.lon());
    double dLon = lon2 - lon1;
    double y = Math.sin(dLon) * Math.cos(lat2);
    double x = (Math.cos(lat1) * Math.sin(lat2))
            - (Math.sin(lat1) * Math.cos(lat2) * Math.cos(dLon));
    double bearingRad = Math.atan2(y, x);
    double bearingDeg = Math.toDegrees(bearingRad);
    return (bearingDeg + 360.0) % 360.0; // Normalize to 0-360°
  }

  /// Returns the distance between the two points on the earth's surface in feet using the
  /// Haversine formula.
  ///
  /// @param p1 the first geographic point
  /// @param p2 the second geographic point
  public static double distance(GeoPoint p1, GeoPoint p2) {
    double lat1 = p1.lat();
    double lon1 = p1.lon();
    double lat2 = p2.lat();
    double lon2 = p2.lon();
    double dLat = Math.toRadians(lat2 - lat1);
    double dLon = Math.toRadians(lon2 - lon1);
    double a = (Math.sin(dLat / 2) * Math.sin(dLat / 2)) + (Math.cos(Math.toRadians(lat1))
            * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2));
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    double distance = 3958.8 * c; // distance in miles
    return distance * 5280.0; // convert to feet
  }

  /// Projects a new {@link GeoPoint} from the specified start point, distance in feet, and
  /// bearing as a {@link Bearing} object. This method is a convenience overload that allows the
  /// caller to specify the bearing as a {@link Bearing} object instead of a double value in
  /// degrees.
  ///
  /// @param start the starting point
  /// @param bearing the bearing as a {@link Bearing} object
  /// @param distanceFeet the distance to project in feet
  /// @return a new {@link GeoPoint} projected from the start point
  ///
  public static GeoPoint projectPt(GeoPoint start, Bearing bearing, double distanceFeet) {
    return projectPt(start, bearing.degrees(), distanceFeet);
  }

  /// Projects a new {@link GeoPoint} from the specified start point over in the bearing line
  /// and up perpendicular to the bearing line, using a {@link Bearing} object for the bearing.
  /// This method is a convenience overload that allows the caller to specify the bearing as a
  /// {@link Bearing} object instead of a double value in degrees.
  ///
  /// @param start the starting point
  /// @param bearing the bearing as a {@link Bearing} object
  /// @param overDelta the over distance to project in feet
  /// @param upDelta the up distance to project in feet
  /// @return a new {@link GeoPoint} projected from the start point
  ///
  public static GeoPoint projectPt(GeoPoint start,
          Bearing bearing,
          double overDelta,
          double upDelta) {
    return projectPt(start, bearing.degrees(), overDelta, upDelta);
  }
  
  /// Projects a new {@link GeoPoint} from the specified start point, distance in feet, and
  /// bearing in degrees.
  ///
  /// @param start the starting point
  /// @param bearingDegrees the bearing in degrees from the north
  /// @param distanceFeet the distance to project in feet
  /// @return a new {@link GeoPoint} projected from the start point
  private static GeoPoint projectPt(GeoPoint start, double bearingDegrees, double distanceFeet) {
    double earthRadiusMeters = 6371000.0;
    double distanceMeters = distanceFeet * 0.3048;
    double bearingRad = Math.toRadians(bearingDegrees);
    double lat1 = Math.toRadians(start.lat());
    double lon1 = Math.toRadians(start.lon());
    double lat2 = Math
            .asin((Math.sin(lat1) * Math.cos(distanceMeters / earthRadiusMeters)) + (Math.cos(lat1)
                    * Math.sin(distanceMeters / earthRadiusMeters) * Math.cos(bearingRad)));
    double lon2 = lon1 + Math.atan2(
            Math.sin(bearingRad) * Math.sin(distanceMeters / earthRadiusMeters) * Math.cos(lat1),
            Math.cos(distanceMeters / earthRadiusMeters) - (Math.sin(lat1) * Math.sin(lat2)));
    return new GeoPoint(Math.toDegrees(lat2), Math.toDegrees(lon2));
  }

  /// Projects a new {@link GeoPoint} from the specified start point over in the bearing line
  /// and up perpendicular to the bearing line.
  ///
  /// @param start the starting point
  /// @param bearing the bearing in degrees from the north
  /// @param overDelta the over distance to project in feet
  /// @param upDelta the up distance to project in feet
  /// @return a new {@link GeoPoint} projected from the start point
  private static GeoPoint projectPt(GeoPoint start,
          double bearing,
          double overDelta,
          double upDelta) {
    // Move the point over in the bearing direction
    GeoPoint overPt = projectPt(start, bearing, overDelta);
    // Move the point up perpendicular to the bearing direction
    GeoPoint finalPt = projectPt(overPt, bearing - 90.0, upDelta);
    return finalPt;
  }
}
