package mhc.geo;

import java.io.PrintWriter;

/// Represents a KML LabelStyle element, which defines the style for labels displayed on KML
/// features such as placemarks. The style includes color and scale properties.
public class KMLLabelStyle extends KMLStyle {
  
  private static final String KML_TEMPLATE = """
    <Style id="%s">
      <LabelStyle>
        <color>%s</color>
        <scale>%s</scale>
      </LabelStyle>
    </Style>
    """;
  
  /// The color of the label in aabbggrr format. Defaults to white (ffffffff).
  private String color = "ffffffff";
  
  /// The scale factor for the label text. Defaults to 1.0 (normal size).
  private double scale = 1.0;
  
  /// Writes the KML Style definition for this label style to the provided PrintWriter.
  ///
  /// @param pw the PrintWriter to write the KML style definition to
  @Override
  public void generateKMLStyleDefination(PrintWriter pw) {
    pw.print(KML_TEMPLATE.formatted(id, color, scale));
  }
  
  /// Returns the label color in aabbggrr format.
  ///
  /// @return the color string
  public String getColor() {
    return color;
  }
  
  /// Returns the scale factor for the label text.
  ///
  /// @return the scale factor
  public double getScale() {
    return scale;
  }
  
  /// Sets the label color in aabbggrr format.
  ///
  /// @param color the color string to set
  public void setColor(String color) {
    this.color = color;
  }
  
  /// Sets the scale factor for the label text.
  ///
  /// @param scale the scale factor to set
  public void setScale(double scale) {
    this.scale = scale;
  }
}
