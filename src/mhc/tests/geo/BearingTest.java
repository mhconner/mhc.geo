package mhc.tests.geo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import mhc.geo.Bearing;
import mhc.io.Out;

/**
 * Class: BearingTest
 */
class BearingTest {
  
  private static final double DELTA = 1e-6;
  
  public static boolean ShowOut = true;
  
  /// @throws java.lang.Exception
  @BeforeAll
  static void setUpBeforeClass() throws Exception {
  }

  /// @throws java.lang.Exception
  @AfterAll
  static void tearDownAfterClass() throws Exception {
  }
  
  /// @param d
  private void bearingTest(double d) {
    Bearing b = new Bearing(d);
    Out.formatD(ShowOut, "Bearing %.2f degrees is %s%n", d, b);
    double expected = ((d % 360.0) + 360.0) % 360.0;
    assertEquals(expected, b.degrees(), DELTA);
  }
  
  private void reverseTest(double d, double expected) {
    Bearing b = new Bearing(d);
    Bearing r = b.reverse();
    Out.formatD(ShowOut, "Reverse of %s is %s%n", b, r);
    assertEquals(expected, r.degrees(), DELTA);
  }
  
  /// @throws java.lang.Exception
  @BeforeEach
  void setUp() throws Exception {
    Out.setDevelopmentMode(true);
  }
  
  /// @throws java.lang.Exception
  @AfterEach
  void tearDown() throws Exception {
  }
  
  /// Test method for {@link mhc.geo.Bearing#Bearing(double)}.
  @Test
  void testBearing() {
    bearingTest(45.0);
    bearingTest(45.0 - 360.0);
    bearingTest( -180.0);
    bearingTest( -360.0);
    bearingTest(360.0);
    bearingTest(450.0);
    bearingTest( -450.0);
    bearingTest( -90.0);
  }
  
  /// Test method for {@link mhc.geo.Bearing#reverse()} and {@link Bearing#rotate(double)}.
  @Test
  void testReverse() {
    reverseTest(45.0, 225.0);
    Bearing b = new Bearing(45.0);
    Bearing revB = b.reverse();
    Bearing rotB = b.rotate(180.0);
    assertEquals(revB.degrees(), rotB.degrees(), DELTA);
    rotB = b.rotate( -180.0);
    assertEquals(revB.degrees(), rotB.degrees(), DELTA);
  }
  
  /// Test method for {@link mhc.geo.Bearing#toRadians()}.
  @Test
  void testToRadians() {
    toRadinsTest(180.0, Math.PI);
    toRadinsTest( -90.0, Math.PI * 1.5);
  }

  private void toRadinsTest(double degrees, double expected) {
    Bearing b = new Bearing(degrees);
    double radians = b.toRadians();
    Out.formatD(ShowOut, "%s in radians is %.6f%n", b, radians);
    // assertEquals(expected, radians, DELTA);
  }
}
