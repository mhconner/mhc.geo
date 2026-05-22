package mhc.tests.geo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import mhc.geo.Bearing;
import mhc.geo.Geo;
import mhc.geo.GeoLine;
import mhc.geo.GeoPoint;
import mhc.geo.GeoRectangle;
import mhc.geo.KMLGenerator;
import mhc.io.Out;
import mhc.io.SOut;

/**
 * Class: KMLGeneratorTest
 */
class KMLGeneratorTest {

  /**
   * The Yaupon thicket BirdPuc and House BirdPuc are used for testing geographic calculations.
   * These points are defined by their latitude and longitude.
   */
  public GeoPoint yPt = new GeoPoint(30.799900, -96.757900); // Yaupon thicket BirdPuc

  /**
   * The House BirdPuc is used for testing geographic calculations.
   */
  public GeoPoint hPt = new GeoPoint(30.801490, -96.758822); // House BirdPuc
  
  public GeoPoint lowerLeft = Geo.projectPt(yPt, new Bearing(270.0), 350.0);

  public GeoPoint anchor = new GeoPoint(30.799940, -96.758796);

  public GeoPoint anchorOld = new GeoPoint(30.800026, -96.758978);

  public GeoRectangle rect = GeoRectangle.fromCorner(anchor, 624.0, 1620.0, 104.0);

  public GeoLine line0Degree = GeoLine.fromBearing(hPt, 300.0, new Bearing(0.0));
  
  public GeoLine line70Degree = GeoLine.fromBearing(hPt, 300.0, new Bearing(70.0));

  /**
   * @throws java.lang.Exception
   */
  @BeforeAll
  static void setUpBeforeClass() throws Exception {
  }
  
  /**
   * @throws java.lang.Exception
   */
  @AfterAll
  static void tearDownAfterClass() throws Exception {
  }
  
  /**
   * @throws java.lang.Exception
   */
  @BeforeEach
  void setUp() throws Exception {
  }
  
  /**
   * @throws java.lang.Exception
   */
  @AfterEach
  void tearDown() throws Exception {
  }
  
  /**
   * Test method for
   * {@link mhc.geo.KMLGenerator#generateHeader(java.io.PrintWriter, java.lang.String)}.
   *
   * @throws FileNotFoundException
   */
  @Test
  final void testGenerateDoc() throws FileNotFoundException {
    SOut.push();
    KMLGenerator.generateHeader(SOut.getOut(), "test");
    KMLGenerator.generateKMLRectangle(SOut.getOut(), "testRect", KMLGenerator.Style.YELLOW, rect);
    // KMLGenerator.generateKMLLine(SOut.getOut(), "0Line", Style.RED2, line0Degree);
    // KMLGenerator.generateKMLLine(SOut.getOut(), "70Line", Style.YELLOW2, line70Degree);
    KMLGenerator.generateFooter(SOut.getOut());
    String doc = SOut.pop();
    Out.println(doc);
    File file = new File(
            "\\\\NAS4DRIVE\\Server\\WildlifeManagement\\Herbcide Study\\StudyAreaMaps\\test.kml");
    PrintWriter pw = new PrintWriter(file);
    pw.println(doc);
    pw.close();
    Out.println("KML file written to: " + file.getAbsolutePath());
    Out.println("KML file size: " + file.length() + " bytes");
  }
}
