package mhc.geo;

/// GeoRectangle is a [record] representing a four sided polygon defined by four corner points.
/// The corners are ordered as follows: `c0, c1, c2, c3`. The edges are defined by the corner
/// points in a counterclockwise manner. That is `c0 -> c1 ->c2 -> c3 -> c0`. The rectangle can
/// be defined by any four points, but it is typically used to represent a rectangular area on
/// the Earth's surface. The rectangle can be created from a lower-left corner point, width in
/// feet, height in feet, and bearing in degrees. The rectangle can also be created from four
/// corner points directly. The rectangle provides methods to get the lower, right, upper, and
/// left edges as GeoLine instances.
///
/// @param c0 the first corner point of the rectangle, called the anchor corner
/// @param c1 the next corner point
/// @param c2 the next corner point
/// @param c3 the next corner point that connects back to the anchor corner *
///
/// @see GeoPoint
public record GeoRectangle(GeoPoint c0, GeoPoint c1, GeoPoint c2, GeoPoint c3) {
  
  /// Constructs a new {@link GeoRectangle} with the specified corner points. The corners are
  /// ordered as follows: `c0, c1, c2, c3`. The edges are defined by the corner points in a
  /// counterclockwise manner. That is `c0 -> c1 ->c2 -> c3 -> c0`.
  ///
  /// @param c0 the anchor corner
  /// @param c1 the next corner point
  /// @param c2 the next corner point
  /// @param c3 the next corner point that connects back to the anchor corner
  public GeoRectangle {
    if ((c0 == null) || (c1 == null) || (c2 == null) || (c3 == null))
      throw new IllegalArgumentException("All corner points must be non-null.");
  }
  
  /// Creates a {@link GeoRectangle} from an anchor corner point, width in feet, height in
  /// feet, and bearing in degrees. The rectangle is defined by projecting the width and height
  /// from the anchor corner point in the specified bearing direction. The width is in the
  /// bearing direction and the height is in the bearing-90 degrees direction. The corners are
  /// ordered as follows: `c0, c1, c2, c3`. The edges are defined by the corner points in a
  /// counterclockwise manner. That is `c0 -> c1 ->c2 -> c3 -> c0`.
  ///
  /// @param c0 the anchor corner point
  /// @param widthFeet the width of the rectangle in feet
  /// @param heightFeet the height of the rectangle in feet
  /// @param bearing the bearing in degrees from the north
  /// @return a new {@link GeoRectangle} instance
  public static GeoRectangle fromCorner(GeoPoint c0,
          double widthFeet,
          double heightFeet,
          Bearing bearing) {
    GeoPoint c1 = Geo.projectPt(c0, bearing, widthFeet);
    GeoPoint c2 = Geo.projectPt(c1, bearing.rotate( -90), heightFeet);
    GeoPoint c3 = Geo.projectPt(c0, bearing.rotate( -90), heightFeet);
    return new GeoRectangle(c0, c1, c2, c3);
  }

  /**
   * Returns a new {@link GeoLine} representing the lower edge of the rectangle going from the
   * c0 corner to the c1 corner.
   *
   * @return a new {@link GeoLine} instance representing the lower edge
   */
  public GeoLine lower() {
    return new GeoLine(c0, c1);
  }

  /**
   * Returns a new {@link GeoLine} representing the right edge of the rectangle going from the
   * c1 corner to the c2 corner.
   *
   * @return a new {@link GeoLine} instance representing the right edge
   */
  public GeoLine right() {
    return new GeoLine(c1, c2);
  }

  /**
   * Returns a new {@link GeoLine} representing the upper edge of the rectangle going from the
   * c2 corner to the c3 corner.
   *
   * @return a new {@link GeoLine} instance representing the upper edge
   */
  public GeoLine upper() {
    return new GeoLine(c2, c3);
  }
  
  /**
   * Returns a new {@link GeoLine} representing the left edge of the rectangle going from the
   * c3 corner to the c0 corner.
   *
   * @return a new {@link GeoLine} instance representing the left edge
   */
  public GeoLine left() {
    return new GeoLine(c3, c0);
  }

  @Override
  public String toString() {
    return String.format("""
      GeoRectangle(
          c0=%s,
          c1=%s,
          c2=%s,
          c3=%s)""", //
            c0, c1, c2, c3);
  }
  
  /// Creates a {@link GeoRectangle} from an anchor corner point, width in feet, height in
  /// feet, and bearing. The rectangle is defined by projecting the width and height from the
  /// anchor corner point in the specified bearing direction. The width is in the bearing
  /// direction and the height is in the bearing-90 degrees direction. The corners are ordered
  /// as follows: `c0, c1, c2, c3`. The edges are defined by the corner points in a
  /// counterclockwise manner. That is `c0 -> c1 ->c2 -> c3 -> c0`.
  ///
  /// @param innerAnchor the anchor corner point
  /// @param widthFeet the width of the rectangle in feet
  /// @param heightFeet the height of the rectangle in feet
  /// @param horizontalBearing the bearing from the north in degrees
  /// @return a new {@link GeoRectangle} instance
  /// @see Bearing
  /// @see #fromCorner(GeoPoint, double, double, double)
  public static GeoRectangle fromCorner(GeoPoint innerAnchor,
          double widthFeet,
          double heightFeet,
          double horizontalBearing) {
    return fromCorner(innerAnchor, widthFeet, heightFeet, new Bearing(horizontalBearing));
  }
}
