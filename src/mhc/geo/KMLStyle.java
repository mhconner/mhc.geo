package mhc.geo;

import java.io.PrintWriter;

/// Class representing a KML style, which can be used to define the appearance of KML features
/// such as points, lines, and polygons. This is an abstract class that should be extended by
/// specific style types such as LineStyle, PolyStyle, LabelStyle, and BalloonStyle. Each
/// KMLStyle has a unique ID that can be referenced by KML features to apply the style. The
/// generateKMLStyleDefination method should be implemented by subclasses to output the
/// appropriate KML style definition based on the style's properties.
public abstract class KMLStyle {
  
  protected String id;

  public abstract void generateKMLStyleDefination(PrintWriter pw);

  /// Gets the unique identifier for this KML style. The id is used to reference this style
  /// from KML features such as Placemarks. It should be unique within the KML document to avoid
  /// conflicts with other styles.
  String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
