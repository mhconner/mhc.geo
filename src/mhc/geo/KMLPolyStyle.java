package mhc.geo;

import java.io.PrintWriter;

/// Represents a KML PolyStyle element, which defines the style for polygons displayed on KML
/// features. The style includes color, fill, and outline properties.
public class KMLPolyStyle extends KMLStyle {
  
  private static final String KML_TEMPLATE = """
    <Style id="%s">
      <PolyStyle>
        <color>%s</color>
        <fill>%s</fill>
        <outline>%s</outline>
      </PolyStyle>
    </Style>
    """;
  
  /// The color of the polygon in aabbggrr format. Defaults to white (ffffffff).
  private String color = "ffffffff";
  
  /// Whether the polygon interior is filled. Defaults to true.
  private boolean fill = true;
  
  /// Whether the polygon outline is drawn. Defaults to true.
  private boolean outline = true;
  
  /// Writes the KML Style definition for this polygon style to the provided PrintWriter.
  ///
  /// @param pw the PrintWriter to write the KML style definition to
  @Override
  public void generateKMLStyleDefination(PrintWriter pw) {
    pw.print(KML_TEMPLATE.formatted(id, color, fill ? 1 : 0, outline ? 1 : 0));
  }
  
  /// Returns the polygon color in aabbggrr format.
  ///
  /// @return the color string
  public String getColor() {
    return color;
  }
  
  /// Returns whether the polygon interior is filled.
  ///
  /// @return true if the polygon is filled, false otherwise
  public boolean isFill() {
    return fill;
  }
  
  /// Returns whether the polygon outline is drawn.
  ///
  /// @return true if the outline is drawn, false otherwise
  public boolean isOutline() {
    return outline;
  }
  
  /// Sets the polygon color in aabbggrr format.
  ///
  /// @param color the color string to set
  public void setColor(String color) {
    this.color = color;
  }
  
  /// Sets whether the polygon interior is filled.
  ///
  /// @param fill true to fill the polygon, false otherwise
  public void setFill(boolean fill) {
    this.fill = fill;
  }
  
  /// Sets whether the polygon outline is drawn.
  ///
  /// @param outline true to draw the outline, false otherwise
  public void setOutline(boolean outline) {
    this.outline = outline;
  }
}
