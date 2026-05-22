package mhc.geo;

import java.io.PrintWriter;

/// Represents a KML BalloonStyle element, which defines the style for the info balloon
/// displayed when a KML feature such as a Placemark is clicked. The style includes background
/// color, text color, text content, and display mode properties.
public class KMLBalloonStyle extends KMLStyle {
  
  private static final String KML_TEMPLATE = """
    <Style id="%s">
      <BalloonStyle>
        <bgColor>%s</bgColor>
        <textColor>%s</textColor>
    %s\
        <displayMode>%s</displayMode>
      </BalloonStyle>
    </Style>
    """;
  
  private static final String TEXT_TEMPLATE = """
        <text>%s</text>
    """;
  
  /// The background color of the balloon in aabbggrr format. Defaults to white (ffffffff).
  private String bgColor = "ffffffff";
  
  /// The text color of the balloon in aabbggrr format. Defaults to black (ff000000).
  private String textColor = "ff000000";
  
  /// The text content of the balloon. If null, no text element is written.
  private String text;
  
  /// The display mode of the balloon. Defaults to "default". Use "hide" to hide the balloon.
  private String displayMode = "default";
  
  /// Writes the KML Style definition for this balloon style to the provided PrintWriter.
  ///
  /// @param pw the PrintWriter to write the KML style definition to
  @Override
  public void generateKMLStyleDefination(PrintWriter pw) {
    String textKml = text != null ? TEXT_TEMPLATE.formatted(text) : "";
    pw.print(KML_TEMPLATE.formatted(id, bgColor, textColor, textKml, displayMode));
  }
  
  /// Returns the background color of the balloon in aabbggrr format.
  ///
  /// @return the background color string
  public String getBgColor() {
    return bgColor;
  }
  
  /// Returns the display mode of the balloon.
  ///
  /// @return the display mode string
  public String getDisplayMode() {
    return displayMode;
  }
  
  /// Returns the text content of the balloon, or null if not set.
  ///
  /// @return the text content string
  public String getText() {
    return text;
  }
  
  /// Returns the text color of the balloon in aabbggrr format.
  ///
  /// @return the text color string
  public String getTextColor() {
    return textColor;
  }
  
  /// Sets the background color of the balloon in aabbggrr format.
  ///
  /// @param bgColor the background color string to set
  public void setBgColor(String bgColor) {
    this.bgColor = bgColor;
  }
  
  /// Sets the display mode of the balloon. Use "default" or "hide".
  ///
  /// @param displayMode the display mode string to set
  public void setDisplayMode(String displayMode) {
    this.displayMode = displayMode;
  }
  
  /// Sets the text content of the balloon. Set to null to omit the text element.
  ///
  /// @param text the text content string to set
  public void setText(String text) {
    this.text = text;
  }
  
  /// Sets the text color of the balloon in aabbggrr format.
  ///
  /// @param textColor the text color string to set
  public void setTextColor(String textColor) {
    this.textColor = textColor;
  }
}
