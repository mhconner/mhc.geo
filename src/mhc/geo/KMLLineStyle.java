package mhc.geo;

import java.io.PrintWriter;

/// Represents a KML LineStyle element, which defines the style for lines displayed on KML
/// features such as paths and polygon outlines. The style includes color and width properties.
public class KMLLineStyle extends KMLStyle {
  
  private static final String KML_TEMPLATE = """
    <Style id="%s">
      <LineStyle>
        <color>%s</color>
        <width>%s</width>
      </LineStyle>
    </Style>
    """;
  
  /// The color of the line in aabbggrr format. Defaults to white (ffffffff).
  private String color = "ffffffff";
  
  /// The width of the line in pixels. Defaults to 1.0.
  private double width = 1.0;
  
  /// Writes the KML Style definition for this line style to the provided PrintWriter.
  ///
  /// @param pw the PrintWriter to write the KML style definition to
  @Override
  public void generateKMLStyleDefination(PrintWriter pw) {
    pw.print(KML_TEMPLATE.formatted(id, color, width));
  }
  
  /// Returns the line color in aabbggrr format.
  ///
  /// @return the color string
  public String getColor() {
    return color;
  }
  
  /// Returns the width of the line in pixels.
  ///
  /// @return the line width
  public double getWidth() {
    return width;
  }
  
  /// Sets the line color in aabbggrr format.
  ///
  /// @param color the color string to set
  public void setColor(String color) {
    this.color = color;
  }
  
  /// Sets the width of the line in pixels.
  ///
  /// @param width the line width to set
  public void setWidth(double width) {
    this.width = width;
  }
}
