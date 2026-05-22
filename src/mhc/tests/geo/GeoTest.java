package mhc.tests.geo;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

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
import mhc.geo.StudyLayoutGenerator;
import mhc.io.Out;

/**
 * Class: GeoTest
 */
class GeoTest {
  
  public static StudyLayoutGenerator gen;
  
  /**
   * The Yaupon thicket BirdPuc and House BirdPuc are used for testing geographic calculations.
   * These points are defined by their latitude and longitude.
   */
  public GeoPoint yPt = new GeoPoint(30.799900, -96.757900); // Yaupon thicket BirdPuc
  
  /**
   * The House BirdPuc is used for testing geographic calculations.
   */
  public GeoPoint hPt = new GeoPoint(30.801490, -96.758822); // House BirdPuc

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
  
  public void assertClose(double expected, double actual) {
    assertClose(expected, actual, 0.01);
  }
  
  public void assertClose(double expected, double actual, double percentTolerance) {
    double tolerance = Math.abs((percentTolerance / 100.0) * expected);
    assertTrue(Math.abs(expected - actual) <= tolerance, //
            String.format("Expected: %12.8f, Actual: %12.8f, Tolerance: %12.8f", expected, actual,
                    tolerance));
  }
  
  public void checkDistance(Bearing baring, double distance) {
    int numSteps = 17;
    GeoPoint anchor = StudyLayoutGenerator.LayoutAnchor;
    GeoPoint oneStepPt = mhc.geo.Geo.projectPt(anchor, baring, distance);
    double oneStepDistance = mhc.geo.Geo.distance(anchor, oneStepPt);
    double distanceIncrmental = distance / numSteps;
    GeoPoint stepPt = anchor;
    for (int step = 1; step <= numSteps; step++ ) {
      stepPt = mhc.geo.Geo.projectPt(stepPt, baring, distanceIncrmental);
    }
    double incrementalDistance = mhc.geo.Geo.distance(anchor, stepPt);
    double diff2Pts = Math.abs(oneStepDistance - incrementalDistance);
    assertClose(distance, incrementalDistance);
    assertClose(distance, oneStepDistance);
    assertClose(oneStepDistance, incrementalDistance);
    Out.trace(true, """
      checkDistance:
          oneStepPt: %s,
             distance: %12.8f,
             stepPt: %s,
             distance: %12.8f,
          seperation: %8.6f%n""",//
            oneStepPt.toString(), oneStepDistance, stepPt, incrementalDistance,
            mhc.geo.Geo.distance(oneStepPt, stepPt));
    Out.trace(true,
            "checkDistance: baring: %8.3f, distance: %12.6f, increments: %d, diff: %10.6f%n",
            baring.degrees(), distance, numSteps, diff2Pts);
  }
  
  public void genPlotsIncremental() {
    int numRows = gen.getTotalNumberOfPlotRows();
    int numCols = StudyLayoutGenerator.NumberOfPlotColumns;
    GeoRectangle[][] plots = new GeoRectangle[numRows][numCols];
    GeoPoint rowAnchor = StudyLayoutGenerator.LayoutAnchor;
    for (int row = numRows; row > 0; row-- ) {
      GeoPoint plotAnchor = rowAnchor;
      int rowIndex = numRows - row;
      for (int col = 0; col < numCols; col++ ) {
        plots[rowIndex][col] = GeoRectangle.fromCorner(plotAnchor, StudyLayoutGenerator.PlotWidth,
                StudyLayoutGenerator.PlotHeight, StudyLayoutGenerator.HorizontalBearing);
        plotAnchor = mhc.geo.Geo.projectPt(plotAnchor, StudyLayoutGenerator.HorizontalBearing,
                StudyLayoutGenerator.PlotWidth + StudyLayoutGenerator.VerticalInnerBuffer);
      }
      rowAnchor = Geo.projectPt(rowAnchor, StudyLayoutGenerator.VerticalBearing,
              StudyLayoutGenerator.PlotHeight);
    }
  }
  
  /// This test method generates a compass rose with points every 30 degrees at a distance of
  /// 100 feet
  @Test
  public void illustrate() {
    // Generate a compass rose with points every 30 degrees at a distance of 1000 feet from the
    // Yaupon thicket BirdPuc using KMLGenerator to draw the lines and label the end points.
    try (PrintWriter out = new PrintWriter(Files.newBufferedWriter(
            Path.of(System.getProperty("user.home"), "downloads", "compass_rose.kml"),
            java.nio.file.StandardOpenOption.CREATE,
            java.nio.file.StandardOpenOption.TRUNCATE_EXISTING))) {
      // KMLGenerator kmlGen = new KMLGenerator();
      KMLGenerator.generateHeader(out, "Compass Rose from Yaupon thicket BirdPuc");
      KMLGenerator.generateLabel(out, "Center", yPt);
      for (int bearingAngle = 0; bearingAngle < 360; bearingAngle += 30) {
        Bearing bearing = new Bearing(bearingAngle);
        GeoPoint pt = mhc.geo.Geo.projectPt(yPt, bearing, 200.0);
        KMLGenerator.generateKMLLine(out, "Bearing " + bearingAngle, KMLGenerator.Style.YELLOW,
                new GeoLine(yPt, pt));
        KMLGenerator.generateLabel(out, "%03d".formatted(bearingAngle), pt);
      }
      GeoPoint boxAnchor = mhc.geo.Geo.projectPt(yPt, new Bearing(45.0), 400.0);
      GeoRectangle box = GeoRectangle.fromCorner(boxAnchor, 200.0, 200.0, new Bearing(45.0));
      KMLGenerator.generateKMLRectangle(out, "Box", KMLGenerator.Style.RED_THIN, box);
      KMLGenerator.generateLabel(out, "0:Anchor", boxAnchor);
      KMLGenerator.generateLabel(out, "1", box.c1());
      KMLGenerator.generateLabel(out, "2", box.c2());
      KMLGenerator.generateLabel(out, "3", box.c3());
      KMLGenerator.generateFooter(out);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * @throws java.lang.Exception
   */
  @BeforeEach
  void setUp() throws Exception {
    Out.setDevelopmentMode(true);
    gen = new StudyLayoutGenerator();
  }

  /**
   * @throws java.lang.Exception
   */
  @AfterEach
  void tearDown() throws Exception {
  }
  
  // @Test
  void testDirections() {
    try (PrintWriter out = new PrintWriter(Files.newBufferedWriter(
            Path.of(System.getProperty("user.home"), "downloads", "compass_rose.kml"),
            java.nio.file.StandardOpenOption.CREATE,
            java.nio.file.StandardOpenOption.TRUNCATE_EXISTING))) {
      KMLGenerator.generateHeader(out, "Compass Rose from Yaupon thicket BirdPuc");
      KMLGenerator.generateLabel(out, "Center", yPt);
      for (int bearingAngle = 0; bearingAngle < 360; bearingAngle += 30) {
        Bearing bearing = new Bearing(bearingAngle);
        GeoPoint pt = mhc.geo.Geo.projectPt(yPt, bearing, 1000.0);
        KMLGenerator.generateKMLLine(out, "Bearing " + bearingAngle, KMLGenerator.Style.YELLOW,
                new GeoLine(yPt, pt));
        KMLGenerator.generateLabel(out, "%03d".formatted(bearingAngle), pt);
      }
      KMLGenerator.generateFooter(out);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Test method for {@link mhc.geo.Geo#distance(mhc.geo.GeoPoint, mhc.geo.GeoPoint)}.
   */
  @Test
  final void testDistance() {
    checkDistance(StudyLayoutGenerator.VerticalBearing, 1675.0);
    checkDistance(StudyLayoutGenerator.HorizontalBearing, 1675.0);
  }
  
  @Test
  void testLine() {
    GeoPoint pt1 = mhc.geo.Geo.projectPt(yPt, new Bearing(30.0), 1000.0);
    GeoLine line = new GeoLine(yPt, pt1);
    Out.trace(false, "Line from Yaupon thicket BirdPuc to projected point: " + line);
    double distance = line.length();
    Out.trace(false, "Length of line segment: " + distance + " feet");
    assertClose(1000.0, distance, 0.01);
    double bearing = line.bearing().degrees();
    Out.trace(false,
            "Bearing from Yaupon thicket BirdPuc to projected point: " + bearing + " degrees");
    assertClose(30.0, bearing, 0.01);
  }
  
  /**
   * Test method for {@link mhc.geo.Geo#projectPt(mhc.geo.GeoPoint, double, double)}.
   */
  @Test
  final void testProject() {
    GeoPoint pt1 = mhc.geo.Geo.projectPt(yPt, new Bearing(120.0), 1000.0);
    double distance = mhc.geo.Geo.distance(yPt, pt1);
    Out.trace(false,
            "Projected point from Yaupon thicket BirdPuc at 1000 feet, bearing 30 degrees: " + pt1);
    Out.trace(false,
            "Distance from Yaupon thicket BirdPuc to projected point: " + distance + " feet");
    assertClose(1000.0, distance, 0.01);
    Bearing bearing = mhc.geo.Geo.bearing(yPt, pt1);
    Out.trace(false, "Bearing from Yaupon thicket BirdPuc to projected point: " + bearing.degrees()
            + " degrees");
    assertClose(120.0, bearing.degrees(), 0.01);
  }
}
