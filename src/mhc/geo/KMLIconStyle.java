package mhc.geo;

import java.io.PrintWriter;

/// Defines the style for icons in KML. This class extends KMLStyle and provides properties
/// specific to icon styling, such as color, scale, heading, and the URL of the icon image. The
/// generateKMLStyleDefination method generates the KML representation of the icon style based
/// on these properties.
public class KMLIconStyle extends KMLStyle {

  private static final String KML_TEMPLATE = """
    <Style id="%s">
      <IconStyle>
        <color>%s</color>
        <scale>%s</scale>
        <heading>%s</heading>
    %s
      </IconStyle>
    </Style>
    """;

  private static final String ICON_HREF_TEMPLATE = """
        <Icon>
          <href>%s</href>
        </Icon>
    """;

  private String color = "ffffffff";

  private double scale = 1.0;

  private double heading = 0.0;

  private String iconHref;

  /// Constructs a KMLIconStyle with the specified color, scale, heading, and icon URL.
  ///
  /// @param color The color of the icon, represented as a KMLColor object. This will be
  ///          converted to a string in the format "aabbggrr" and stored in the color property.
  /// @param scale The scale of the icon, where 1.0 means the original size, 2.0 means twice the
  ///          original size, and 0.5 means half the original size.
  /// @param heading The heading of the icon in degrees, where 0 degrees means the icon is
  ///          pointing north, 90 degrees means it is pointing east, etc.
  /// @param iconHref The URL of the icon image to use. This should be a valid URL pointing to
  ///          an image file (e.g., PNG, JPEG) that will be used as the icon in KML. If null,
  ///          the default icon will be used.
  public KMLIconStyle(KMLColor color, double scale, double heading, String iconHref) {
    this.color = color.getColorValue();
    this.scale = scale;
    this.heading = heading;
    this.iconHref = iconHref;
  }

  @Override
  public void generateKMLStyleDefination(PrintWriter pw) {
    String iconBlock = iconHref != null ? ICON_HREF_TEMPLATE.formatted(iconHref) : "";
    pw.format(KML_TEMPLATE, id, color, scale, heading, iconBlock);
  }

  public String getColor() {
    return color;
  }

  public double getHeading() {
    return heading;
  }

  /// Returns the URL of the icon image. This is the value of the <href> element within the
  /// <Icon> element in the KML. If this property is not set, the default icon will be used.
  ///
  /// @return The URL of the icon image, or null if it is not set.
  public String getIconHref() {
    return iconHref;
  }

  /// Returns the color as a KMLColor object, which provides a more convenient way to work with
  /// colors in KML. The color is stored as a string in the format "aabbggrr", and the KMLColor
  /// class can parse this string to extract the individual color components (alpha, blue,
  /// green, red) for easier manipulation.
  public KMLColor getKMLColor() {
    return KMLColor.fromColorValue(color);
  }
  
  public double getScale() {
    return scale;
  }

  /// Sets the color using a KMLColor object. This method converts the KMLColor to its string
  /// representation in the format "aabbggrr" and stores it in the color property.
  public void setColor(KMLColor color) {
    this.color = color.getColorValue();
  }

  public void setColor(String color) {
    this.color = color;
  }

  /// Sets the heading of the icon in degrees. The heading is the rotation of the icon, where 0
  /// degrees means the icon is pointing north, 90 degrees means it is pointing east, etc.
  public void setHeading(double heading) {
    this.heading = heading;
  }

  /// Sets the URL of the icon image. This should be a valid URL pointing to an image file
  /// (e.g., PNG, JPEG) that will be used as the icon in KML. The iconHref property is optional;
  /// if it is not set, the default icon will be used.
  public void setIconHref(String iconHref) {
    this.iconHref = iconHref;
  }

  /// Sets the scale of the icon. The scale is a multiplier for the size of the icon, where 1.0
  /// means the original size, 2.0 means twice the original size, and 0.5 means half the
  /// original''s size.
  public void setScale(double scale) {
    this.scale = scale;
  }
}
