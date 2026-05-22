package mhc.geo;

import java.io.PrintWriter;

/// Represents a KML Style element that combines IconStyle, LineStyle, and PolyStyle into a
/// single style definition. This is useful for KML features such as Placemarks that require all
/// three style types. The style includes icon color, scale, heading, and href properties, as
/// well as line color and width, and polygon color, fill, and outline.
public class KMLCombinedStyle extends KMLStyle {
  
  private static final String KML_TEMPLATE = """
    <Style id="%s">
      <IconStyle>
        <color>%s</color>
        <scale>%s</scale>
        <heading>%s</heading>
    %s\
      </IconStyle>
      <LineStyle>
        <color>%s</color>
        <width>%s</width>
      </LineStyle>
      <PolyStyle>
        <color>%s</color>
        <fill>%s</fill>
        <outline>%s</outline>
      </PolyStyle>
    </Style>
    """;
  
  private static final String ICON_HREF_TEMPLATE = """
        <Icon>
          <href>%s</href>
        </Icon>
    """;
  
  /// The color of the icon in aabbggrr format. Defaults to white (ffffffff).
  private String iconColor = "ffffffff";
  
  /// The scale factor for the icon. Defaults to 1.0 (normal size).
  private double iconScale = 1.0;
  
  /// The heading (rotation) of the icon in degrees. Defaults to 0.0 (north).
  private double iconHeading = 0.0;
  
  /// The URL of the icon image. If null, the default icon is used.
  private String iconHref;
  
  /// The color of the line in aabbggrr format. Defaults to white (ffffffff).
  private String lineColor = "ffffffff";
  
  /// The width of the line in pixels. Defaults to 1.0.
  private double lineWidth = 1.0;
  
  /// The color of the polygon in aabbggrr format. Defaults to white (ffffffff).
  private String polyColor = "ffffffff";
  
  /// Whether the polygon interior is filled. Defaults to true.
  private boolean polyFill = true;
  
  /// Whether the polygon outline is drawn. Defaults to true.
  private boolean polyOutline = true;
  
  /// Writes the KML Style definition for this combined style to the provided PrintWriter.
  ///
  /// @param pw the PrintWriter to write the KML style definition to
  @Override
  public void generateKMLStyleDefination(PrintWriter pw) {
    String iconHrefKml = iconHref != null ? ICON_HREF_TEMPLATE.formatted(iconHref) : "";
    pw.print(KML_TEMPLATE.formatted(id, iconColor, iconScale, iconHeading, iconHrefKml, lineColor,
            lineWidth, polyColor, polyFill ? 1 : 0, polyOutline ? 1 : 0));
  }
  
  /// Returns the icon color in aabbggrr format.
  ///
  /// @return the icon color string
  public String getIconColor() {
    return iconColor;
  }
  
  /// Returns the heading (rotation) of the icon in degrees.
  ///
  /// @return the icon heading
  public double getIconHeading() {
    return iconHeading;
  }
  
  /// Returns the URL of the icon image, or null if using the default icon.
  ///
  /// @return the icon href URL
  public String getIconHref() {
    return iconHref;
  }
  
  /// Returns the scale factor for the icon.
  ///
  /// @return the icon scale factor
  public double getIconScale() {
    return iconScale;
  }
  
  /// Returns the line color in aabbggrr format.
  ///
  /// @return the line color string
  public String getLineColor() {
    return lineColor;
  }
  
  /// Returns the width of the line in pixels.
  ///
  /// @return the line width
  public double getLineWidth() {
    return lineWidth;
  }
  
  /// Returns the polygon color in aabbggrr format.
  ///
  /// @return the polygon color string
  public String getPolyColor() {
    return polyColor;
  }
  
  /// Returns whether the polygon interior is filled.
  ///
  /// @return true if the polygon is filled, false otherwise
  public boolean isPolyFill() {
    return polyFill;
  }
  
  /// Returns whether the polygon outline is drawn.
  ///
  /// @return true if the outline is drawn, false otherwise
  public boolean isPolyOutline() {
    return polyOutline;
  }
  
  /// Sets the icon color in aabbggrr format.
  ///
  /// @param iconColor the icon color string to set
  public void setIconColor(String iconColor) {
    this.iconColor = iconColor;
  }
  
  /// Sets the heading (rotation) of the icon in degrees.
  ///
  /// @param iconHeading the icon heading to set
  public void setIconHeading(double iconHeading) {
    this.iconHeading = iconHeading;
  }
  
  /// Sets the URL of the icon image. Set to null to use the default icon.
  ///
  /// @param iconHref the icon href URL to set
  public void setIconHref(String iconHref) {
    this.iconHref = iconHref;
  }
  
  /// Sets the scale factor for the icon.
  ///
  /// @param iconScale the icon scale factor to set
  public void setIconScale(double iconScale) {
    this.iconScale = iconScale;
  }
  
  /// Sets the line color in aabbggrr format.
  ///
  /// @param lineColor the line color string to set
  public void setLineColor(String lineColor) {
    this.lineColor = lineColor;
  }
  
  /// Sets the width of the line in pixels.
  ///
  /// @param lineWidth the line width to set
  public void setLineWidth(double lineWidth) {
    this.lineWidth = lineWidth;
  }
  
  /// Sets the polygon color in aabbggrr format.
  ///
  /// @param polyColor the polygon color string to set
  public void setPolyColor(String polyColor) {
    this.polyColor = polyColor;
  }
  
  /// Sets whether the polygon interior is filled.
  ///
  /// @param polyFill true to fill the polygon, false otherwise
  public void setPolyFill(boolean polyFill) {
    this.polyFill = polyFill;
  }
  
  /// Sets whether the polygon outline is drawn.
  ///
  /// @param polyOutline true to draw the outline, false otherwise
  public void setPolyOutline(boolean polyOutline) {
    this.polyOutline = polyOutline;
  }
}
