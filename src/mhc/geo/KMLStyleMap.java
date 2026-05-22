package mhc.geo;

import java.io.PrintWriter;

/// Represents a KML StyleMap element, which defines a mapping between normal and highlight
/// styles for KML features. A StyleMap allows you to specify different styles for normal and
/// highlighted states of KML features, such as Placemarks. It references existing styles
/// defined in the KML document using their URLs.
public class KMLStyleMap extends KMLStyle {
  
  private static final String KML_TEMPLATE = """
    <StyleMap id="%s">
      <Pair>
        <key>normal</key>
        <styleUrl>%s</styleUrl>
      </Pair>
      <Pair>
        <key>highlight</key>
        <styleUrl>%s</styleUrl>
      </Pair>
    </StyleMap>
    """;
  
  /// The URL of the normal style (e.g., "#normalStyle").
  private String normalStyleUrl;
  
  /// The URL of the highlight style (e.g., "#highlightStyle").
  private String highlightStyleUrl;
  
  public KMLStyleMap() {
  }
  
  /// Constructor with id, normal style URL, and highlight style URL.
  ///
  /// @param id the unique identifier for this StyleMap
  /// @param normalStyleUrl the URL of the normal style (e.g., "#normalStyle")
  /// @param highlightStyleUrl the URL of the highlight style (e.g., "#highlightStyle")
  public KMLStyleMap(String id, String normalStyleUrl, String highlightStyleUrl) {
    this.id = id;
    this.normalStyleUrl = normalStyleUrl;
    this.highlightStyleUrl = highlightStyleUrl;
  }
  
  /// Writes the KML StyleMap definition for this style map to the provided PrintWriter.
  ///
  /// @param pw the PrintWriter to write the KML style definition to
  @Override
  public void generateKMLStyleDefination(PrintWriter pw) {
    pw.print(KML_TEMPLATE.formatted(id, normalStyleUrl, highlightStyleUrl));
  }
  
  /// Returns the URL of the highlight style.
  ///
  /// @return the highlight style URL
  public String getHighlightStyleUrl() {
    return highlightStyleUrl;
  }
  
  /// Returns the URL of the normal style.
  ///
  /// @return the normal style URL
  public String getNormalStyleUrl() {
    return normalStyleUrl;
  }
  
  /// Sets the URL of the highlight style.
  ///
  /// @param highlightStyleUrl the highlight style URL to set
  public void setHighlightStyleUrl(String highlightStyleUrl) {
    this.highlightStyleUrl = highlightStyleUrl;
  }
  
  /// Sets the URL of the normal style.
  ///
  /// @param normalStyleUrl the normal style URL to set
  public void setNormalStyleUrl(String normalStyleUrl) {
    this.normalStyleUrl = normalStyleUrl;
  }
}
