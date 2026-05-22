package mhc.geo;

import java.io.PrintWriter;

/**
 * Class that provides support for generating KML (Keyhole Markup Language) files. The normal
 * sequence of calls is:
 * <ol>
 * <li>{@link #generateFooter(PrintWriter)}<br>
 * A mix of lines and rectangles:
 * <ol>
 * <li>{@link #generateKMLLine(PrintWriter, String, Style, GeoLine)}
 * <li>{@link #generateKMLRectangle(PrintWriter, String, Style, GeoRectangle)}</li>
 * <li>{@link #generateLabel(PrintWriter, String, GeoPoint)}</li>
 * <li>{@link #generateMarker(PrintWriter, String, GeoPoint)}</li>
 * <li>{@link #generateKMLPointCoordinates(PrintWriter, GeoPoint)}</li>
 * </ol>
 * <li>{@link #generateFooter(PrintWriter)}</li>
 * </ol>
 */
public class KMLGenerator {

  /**
   * Enumeration of color and fill styles that can be applied to KML elements. There names
   * correspond to the style IDs defined in the KML_Styles constant.
   */
  public enum Style {
    /**
     * Style for yellow lines with a width of 2 pixels and no polygon fill.
     */
    YELLOW,
    /**
     * Style for green lines with a width of 2 pixels and no polygon fill.
     */
    GREEN,
    /**
     * Style for red lines with a width of 2 pixels and no polygon fill.
     */
    RED_THIN,
    /**
     * Style for red lines with a width of 4 pixels and no polygon fill.
     */
    RED_MEDIUM,
    /**
     * Style for red lines with a width of 2 pixels and a light yellow fill.
     */
    RED_LtYellowFill
  }

  public final static String KML_HEADER = """
    <?xml version="1.0" encoding="UTF-8"?>
    <kml xmlns="http://www.opengis.net/kml/2.2" xmlns:gx="http://www.google.com/kml/ext/2.2"
         xmlns:kml="http://www.opengis.net/kml/2.2" xmlns:atom="http://www.w3.org/2005/Atom">
    <Document>
        <name>%s.kml</name>
        """;

  public final static String KML_Styles = """
    <Style id="YELLOW">
        <LineStyle>
            <color>ff00ffff</color>
            <width>2</width>
        </LineStyle>
        <PolyStyle>
            <fill>0</fill>
            <color>00ffffff</color>
        </PolyStyle>
    </Style>
    <Style id="GREEN">
        <LineStyle>
            <color>ff00ff00</color>
            <width>1</width>
        </LineStyle>
        <PolyStyle>
            <color>ff00ff00</color>
            <fill>0</fill>
        </PolyStyle>
    </Style>
    <Style id="RED_THIN">
        <LineStyle>
            <color>ff0000ff</color>
            <width>2</width>
        </LineStyle>
        <PolyStyle>
            <color>00ffffff</color>
            <fill>0</fill>
        </PolyStyle>
    </Style>
    <Style id="RED_MEDIUM">
        <LineStyle>
            <color>ff0000ff</color>
            <width>1</width>
        </LineStyle>
        <PolyStyle>
            <color>00ffffff</color>
            <fill>0</fill>
        </PolyStyle>
    </Style>
    <Style id="RED_LtYellowFill">
        <LineStyle>
            <color>ff0000ff</color>
            <width>2</width>
        </LineStyle>
        <PolyStyle>
            <color>667fffff</color>
        </PolyStyle>
    </Style>
    <Style id="PlotLabel">
        <IconStyle>
            <Icon>
            </Icon>
        </IconStyle>
        <ListStyle>
        </ListStyle>
    </Style>
    <Style id="Marker">
        <IconStyle>
            <Icon>
              <href>http://maps.google.com/mapfiles/kml/shapes/placemark_circle.png</href>
            </Icon>
        </IconStyle>
        <LabelStyle>
            <color>00ffffff</color>
        </LabelStyle>
        <ListStyle>
        </ListStyle>
    </Style>
        """;
  
  public static final String KML_FolderHeader = """
    <Folder>
        <name>%s</name>
        <open>1</open>
        """;

  public static final String KML_Footer = """
    </Folder>
    </Document>
    </kml>
    """;

  public static final String KML_Lable = """
    <Placemark>
        <name>%s</name>
        <styleUrl>PlotLabel</styleUrl>
        <Point>
            <gx:drawOrder>1</gx:drawOrder>
            <coordinates>%f,%f,0</coordinates>
        </Point>
    </Placemark>
    """;
  
  public static final String KML_Marker = """
    <Placemark>
        <name>%s</name>
        <styleUrl>Marker</styleUrl>
        <Point>
            <gx:drawOrder>1</gx:drawOrder>
            <coordinates>%f,%f,0</coordinates>
        </Point>
    </Placemark>
    """;
  
  /**
   * Writes the KML footer to the specified PrintWriter.
   *
   * @param out the PrintWriter to write the KML footer to
   */
  public static void generateFooter(PrintWriter out) {
    out.println(KML_Footer);
  }

  /**
   * Writes the KML header to the specified PrintWriter.
   *
   * @param out the PrintWriter to write the KML header to
   * @param docName the name of the KML document and the single folder it contains.
   */
  public static void generateHeader(PrintWriter out, String docName) {
    out.format(KML_HEADER, docName);
    out.println(KML_Styles);
    out.format(KML_FolderHeader, docName);
  }

  /**
   * Generates a KML Placemark for a geographic line segment.
   *
   * @param out the PrintWriter to write the KML ton
   * @param objectName the name of the object to be used in the KML Placemark
   * @param style the {@link Style} to be applied to the line in the KML Placemark
   * @param line the {@link GeoLine} to be represented in the KML Placemark
   */
  public static void generateKMLLine(PrintWriter out,
          String objectName,
          Style style,
          GeoLine line) {
    out.format("""
      <Placemark>
        <name>%s</name>
        <styleUrl>%s</styleUrl>
        <LineString>
          <tessellate>0</tessellate>
          <coordinates>
        """, objectName, style.name());
    generateKMLPointCoordinates(out, line.start());
    generateKMLPointCoordinates(out, line.end());
    out.println("""
          </coordinates>
        </LineString>
      </Placemark>
      """);
  }
  
  /**
   * Generates a KML for the coordinates of a geographic point. E.g.:
   *
   * <pre>
   * -96.75708624931713,30.80405859495939,0
   * </pre>
   *
   * @param out the PrintWriter to write the KML coordinates to
   * @param point the geographic point to generate KML coordinates for
   */
  public static void generateKMLPointCoordinates(PrintWriter out, GeoPoint point) {
    out.format("%.8f,%.8f,0 %n", point.lon(), point.lat());
  }
  
  /**
   * Generates a KML Placemark for a geographic rectangle.
   *
   * @param out the PrintWriter to write the KML to
   * @param objectName the name of the object to be used in the KML Placemark
   * @param style the {@link Style} to be applied to the rectangle in the KML Placemark
   * @param rectangle the {@link GeoRectangle} to be represented in the KML Placemark
   */
  public static void generateKMLRectangle(PrintWriter out,
          String objectName,
          Style style,
          GeoRectangle rectangle) {
    out.format("""
      <Placemark>
        <name>%s</name>
        <styleUrl>#%s</styleUrl>
        <Polygon>
        <tessellate>0</tessellate>

          <outerBoundaryIs>
            <LinearRing>
              <coordinates>

        """, objectName, style.name());
    generateKMLPointCoordinates(out, rectangle.c0());
    generateKMLPointCoordinates(out, rectangle.c1());
    generateKMLPointCoordinates(out, rectangle.c2());
    generateKMLPointCoordinates(out, rectangle.c3());
    generateKMLPointCoordinates(out, rectangle.c0()); // Close the polygon
    out.println("""
              </coordinates>
            </LinearRing>
          </outerBoundaryIs>
        </Polygon>
      </Placemark>
      """);
  }
  
  /**
   * Generates a KML Placemark for a label at a centered at a specific geographic point.
   *
   * @param out the PrintWriter to write the KML to
   * @param labelName the name of the label to be used in the KML Placemark
   * @param point the {@link GeoPoint} where the label should be centered
   */
  public static void generateLabel(PrintWriter out, String labelName, GeoPoint point) {
    out.format(KML_Lable, labelName, point.lon(), point.lat());
  }

  /**
   * Generates a KML Placemark for a marker at a specific geographic point.
   *
   * @param out the PrintWriter to write the KML to
   * @param PlacemarkName the name for the KML Placemark, this will not be displayed on the map
   * @param point the {@link GeoPoint} where the marker should be placed
   */
  public static void generateMarker(PrintWriter out, String PlacemarkName, GeoPoint point) {
    out.format(KML_Marker, PlacemarkName, point.lon(), point.lat());
  }
}
