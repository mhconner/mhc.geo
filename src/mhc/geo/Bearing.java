package mhc.geo;

/// This class represents a bearing in degrees. A bearing is an angle measured in degrees from
/// the north direction. The bearing is typically used to specify a direction of travel or a
/// heading. The bearing is represented as a double value in the range [0, 360) degrees, where 0
/// degrees corresponds to north, 90 degrees corresponds to east, 180 degrees corresponds to
/// south, and 270 degrees corresponds to west. If Bearing is created with a value outside the
/// range [0, 360) it will be normalized to fit within the range. For example, a bearing of -45
/// degrees will be normalized to 315 degrees, and a bearing of 370 degrees will be normalized
/// to 10 degrees.
public record Bearing(double degrees) {

  /// Creates a new {@link Bearing} instance with the specified angle in degrees. The degrees
  /// value is normalized to fit within the range [0, 360) degrees. For example, a bearing of
  /// -45 degrees will be normalized to 315 degrees, and a bearing of 370 degrees will be
  /// normalized to 10 degrees.
  public Bearing {
    degrees = ((degrees % 360.0) + 360.0) % 360.0;
  }
  
  /// Creates a new {@link Bearing} instance from the specified angle in radians. The radians
  /// value is converted to degrees and normalized to fit within the range [0, 360) degrees.
  public static Bearing fromRadians(double radians) {
    double degrees = Math.toDegrees(radians);
    return new Bearing(degrees);
  }

  /// Converts this bearing to radians. The degrees value is converted to radians.
  public double toRadians() {
    return (2.0 * Math.PI) * (degrees / 360.0);
  }

  /// Returns a String representation of this bearing in the format "Bearing(XX.XX degrees)",
  /// where XX.XX is the degrees value formatted to two decimal places.
  @Override
  public String toString() {
    return String.format("Bearing(%.2f degrees)", degrees);
  }
  
  /// Returns a new {@link Bearing} instance that represents the reverse of this bearing.
  /// The reverse
  public Bearing reverse() {
    return new Bearing((degrees + 180.0) % 360.0);
  }

  /// Returns a new {@link Bearing} instance that represents the result of rotating this
  /// bearing by the specified angle in degrees. The angleDegrees value is added to the current
  /// degrees value and the result is normalized to fit within the range [0, 360) degrees. For
  /// example, if this bearing is 350 degrees and the angleDegrees is 20 degrees, the resulting
  /// bearing will be 10 degrees. A positive angleDegrees value will rotate the bearing
  /// clockwise, while a negative angleDegrees value will rotate the bearing counterclockwise.
  public Bearing rotate(double angleDegrees) {
    return new Bearing(degrees + angleDegrees);
  }
}
