package mhc.geo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.EnumSet;

import mhc.geo.KMLGenerator.Style;
import mhc.io.Out;

/// This generates a KML (or CSV) file that describes the layout of a study area or various
/// components of the layout such as the grid, labels, post points, etc.
///
/// The layout is based on a grid of plots with specified dimensions and buffers between the
/// plots and the outer boundary of the study area. The layout can also include inner rectangles
/// that define the sampling area within each plot, seeding strips, and shredding plots in the
/// outer buffer. The output can be a KML file for visualization in mapping software or a CSV
/// file with the coordinates of the post points.
///
/// Some key concepts:
/// * Width means is in the direction of the {@link #HorizontalBearing}. Which is in direction
///   of the columns, i.e., left to right in the layout,
/// * Height means is in the direction of the {@link #VerticalBearing}. Which is in the
///   direction of rows, i.e., up in the layout.
/// * The anchor point for the layout is the lower left (SW) corner of the outer rectangle that
///   defines the study area boundary. This is the reference point from which all other points
///   in the layout are derived.
/// * The plots are named based on their column and row, with columns named A, B, C, etc. from
///   left to right and rows named 01, 02, 03, etc. from top to bottom. For example, the plot in
///   the first column and first row is named c01, the plot in the second column and third row
///   is named B03, etc.
/// * The plot c01 the plot nearest the NW corner of the {@link #boundaryRect}, and the plot in
///   the SE corner is D15 based on the default layout of 4 columns and 15 rows (7 in upper
///   section and 8 in lower section).
/// * All directions are based on the horizontal bearing, which is the direction of the columns
///   in the layout (the width of the layout). The vertical bearing is 90 degrees
///   counter-clockwise from the horizontal bearing and is the direction of the columns in the
///   layout (the height of the layout).
///
public class StudyLayoutGenerator {
  
  /**
   * The choices for what to generate in the study area layout.
   */
  public enum GenChoice {
    /** Generate the outer boundary for the study area. */
    Boundary,
    /** Generate the grid layout for the study area. */
    Grid,
    /** Generate labels for the plots in the study area. */
    Labels,
    /** Show inner rectangles that define the sampling area within each plot. */
    InnerRectangles,
    /** Fill the plots with a color. */
    FilledPlots,
    /** Generate post points for the study area. */
    PostPoints,
    /** Generate seeding strips in the study area. */
    SeedingStrips,
    /** Output post points as a CSV file. */
    PostPointsCSV,
    /**
     * Generate plots for the 4 Shredding-only, these are all placed in the outer buffer of the
     * study area
     */
    ShreddingPlots
  }
  
  public enum PlotChoice {
    Front, FrontPosts, FrontGPSCoords
  }
  
  /**
   * The set of generation choices for the study area layout.
   */
  public static EnumSet<GenChoice> genChoices = EnumSet.noneOf(GenChoice.class);
  
  public static PlotChoice choice = PlotChoice.Front; // Default choice for the layout

  /**
   * The single character names for the columns in the study area layout.
   */
  public static char[] ColumnNameChar = {
      'A', 'B', 'C', 'D' }; // Column names for the plots
  
  /**
   * Show the inner rectangle that defines the sampling area within each plot.
   */
  public static boolean ShowInnerRectangle = false; // Show the inner sampling rectangles
  
  /**
   * Show labels in the plots.
   */
  public static boolean ShowLabelsInPlots = false; // Show labels in the plots
  
  /**
   * Fill the plots with a color.
   */
  public static boolean FillPlots = false; // Fill the plots with a color
  
  /**
   * Show coordinates of plot corners and posts in the standard out for debugging.
   */
  public static boolean ShowCoordinates = false; // Show coordinates in the to standard out.
  
  /**
   * Show GPS coordinates of plot corners and posts rather than generating the layout kml file.
   */
  public static boolean ShowGPSCoordinates = false; // Show GPS coordinates of plot corners and
                                                    // posts rather than generating the layout
                                                    // kml file.
  
  /**
   * The base file path for the output files.
   */
  public final static String //
  MapFileBase = "\\\\NAS4DRIVE\\Server\\WildlifeManagement\\Herbcide Study\\StudyAreaMaps\\";
  
  /**
   * The width of each plot in the study area layout.
   */
  public static final double PlotWidth = 104.0; // feet
  
  /**
   * The height of each plot in the study area layout.
   */
  public static final double PlotHeight = 104.0; // feet
  
  /**
   * Buffer distance between the sides of plots and the other boundary of the study area
   * running with the rows, i.e., up and down.
   */
  public static final double VerticalOuterBuffer = 50.0; // feet
  
  /**
   * Buffer distance between the sides of plots and the other boundary of the study area
   * running with the columns, i.e., left and right.
   */
  public static final double HorizontalOuterBuffer = 50.0; // feet
  
  /**
   * Buffer distance between plots running with the rows, i.e., up and down.
   */
  public static final double VerticalInnerBuffer = 12.0; // feet
  
  /**
   * Buffer distance between plot sections running with the columns, i.e., left and right.
   */
  public static final double HorizontalInnerBuffer = 15.0; // feet
  
  /**
   * Buffer distance between the sides of plots and the inner sampling area within each plot
   * running with the columns, i.e., left and right.
   */
  public static final double HorizontalInnerPlotBuffer = 10.0; // feet
  
  /**
   * Buffer distance between the sides of plots and the inner sampling area within each plot
   * running with the rows, i.e., up and down.
   */
  public static final double VerticalInnerPlotBuffer = 10.0; // feet

  public static int NumberOfPlotColumns; // columns of plots
  
  public static int NumberOfPlotRowsInUpperSection; // plots in upper section
  
  public static int NumberOfPlotRowsInLowerSection; // plots in lower section
  
  /**
   * Whether the seeding strips run across rows (left to right) or columns (up and down).
   */
  public static boolean SeedingSriptsAccrossRows;
  
  /**
   * The width of the seeding strips within each plot.
   */
  public static double SeedingStripWidth; // feet

  /**
   * The offsets for the seeding strips within each plot. These are the distances from the
   * lower edge of the plot.
   */
  public static double[] SeedingStripOffsets;
  
  /**
   * Whether the seeding strips run the width of study area or just accross all the plots.
   */
  public static boolean SeedingStripFullWidth;

  /**
   * The anchor point for the study area layout. This is the point from which all other points
   * are derived. This is the lower left corner of the outer rectangle for purposes of naming
   * the plots and posts.
   */
  public static GeoPoint LayoutAnchor;
  
  public static GeoPoint LayoutAnchorFront = new GeoPoint(30.801493, -96.751077);
  
  /**
   * The horizontal bearing for all lines in the study area layout. This is the direction of
   * the increasing row number in the layout which is the width of the layout and is used as
   * the reference for all other directions in the layout.
   */
  public static Bearing HorizontalBearing;
  
  public static boolean GeneratePosts = true;
  
  public static final Bearing HorizontalBearingFront = new Bearing(30.0);
  /**
   * Setup for the static variables based on the chosen layout.
   */
  static {
    NumberOfPlotColumns = 4; // 4 columns of plots
    NumberOfPlotRowsInLowerSection = 8; // 8 plots in lower section
    NumberOfPlotRowsInUpperSection = 7; // 7 plots in upper section
    LayoutAnchorFront = Geo.projectPt(LayoutAnchorFront, HorizontalBearingFront, -60.0, -165.0);
    LayoutAnchor = LayoutAnchorFront;
    HorizontalBearing = HorizontalBearingFront;
    SeedingSriptsAccrossRows = false;
    SeedingStripWidth = 12.0;
    SeedingStripFullWidth = false;
    if (SeedingSriptsAccrossRows) {
      SeedingStripOffsets = new double[]{
          HorizontalInnerPlotBuffer, //
          (PlotHeight / 2.0) - (SeedingStripWidth / 2.0), //
          (PlotHeight - HorizontalInnerPlotBuffer) - SeedingStripWidth }; // feet from lower
                                                                          // edge of plot
    } else {
      SeedingStripOffsets = new double[]{
          VerticalInnerPlotBuffer, //
          (PlotWidth / 2.0) - (SeedingStripWidth / 2.0), //
          (PlotWidth - HorizontalInnerPlotBuffer) - SeedingStripWidth }; // feet from lower
                                                                         // edge of plot
    }
    // double innerPlotHeight = PlotHeight - (2 * HorizontalInnerPlotBuffer);
    // double SeedingStripSpacing = (innerPlotHeight - (2 * HorizontalInnerPlotBuffer)) / 3.0;
    // SeedingStripOffsets = new double[]{
    // HorizontalInnerPlotBuffer + SeedingStripSpacing, //
    // HorizontalInnerPlotBuffer + (2 * SeedingStripSpacing) + SeedingStripHeight };
    /*
     *
     */
    // genChoices.add(GenChoice.Boundary);
    genChoices.add(GenChoice.Grid);
    // genChoices.add(GenChoice.Labels);
    // genChoices.add(GenChoice.InnerRectangles);
    // genChoices.add(GenChoice.FilledPlots);
    // genChoices.add(GenChoice.PostPoints);
    // genChoices.add(GenChoice.PostPointsCSV);
    // genChoices.add(GenChoice.SeedingStrips);
    // genChoices.add(GenChoice.ShreddingPlots);
  }
  
  /**
   * The vertical bearing for all lines in the study area layout. This is 90 degrees
   * counter-clockwise from the horizontal bearing and runs in the direction of increasing
   * column number in the layout. If the HorizontalBearing is 30 degrees, then the
   * VerticalBearing is 300 degrees.
   */
  public static Bearing VerticalBearing = HorizontalBearing.rotate( -90.0);

  /**
   * The output file for the study area layout, this is set based on the generation choices.
   */
  File outputFile = null;
  
  /**
   * The output writer for the study area layout.
   */
  PrintWriter out = null;
  
  /**
   * The style for the outer rectangle that defines the study area layout.
   */
  Style BoundaryRectangleStyle = Style.YELLOW;
  
  /**
   * The style for the inner rectangle study area grid layout.
   */
  Style InnerRectangleStyle = Style.RED_MEDIUM;
  
  Style InnerSamplingRectangleStyle = Style.RED_THIN;
  
  /**
   * The outer rectangle that defines the study area boundary.
   */
  GeoRectangle boundaryRect;
  
  /**
   * Constructs a StudyLayoutGenerator.
   */
  public StudyLayoutGenerator() {
    boundaryRect = getStudyAreaOuterRectangle();
  }
  
  /**
   * @param args
   * @throws FileNotFoundException
   */
  public static void main(String[] args) throws FileNotFoundException {
    StudyLayoutGenerator generator = new StudyLayoutGenerator();
    generator.generateAll();
  }
  
  public void generateAll() throws FileNotFoundException {
    String fileName;
    if (isAnyGenChoiceSet(GenChoice.PostPointsCSV)) {
      fileName = "Post Coordinates.csv";
    } else if (isAnyGenChoiceSet(GenChoice.PostPoints)) {
      fileName = "Post Points.kml";
    } else {
      fileName = "Grid Layout.kml";
    }
    outputFile = new File(MapFileBase + fileName);
    out = new PrintWriter(outputFile);
    if (isAnyGenChoiceSet(GenChoice.PostPointsCSV)) {
      generatePostPointsCSV();
    } else {
      /*
       * Generating a KML file
       */
      String docName = fileName.replace(".kml", "");
      KMLGenerator.generateHeader(out, docName);
      if (isAnyGenChoiceSet(GenChoice.PostPoints)) {
        generatePostPointsKML();
      } else {
        if (isAnyGenChoiceSet(GenChoice.SeedingStrips)) {
          generateSeedingStripsKML();
        }
        generateLayoutKML();
      }
      KMLGenerator.generateFooter(out);
    }
    Out.setDevelopmentMode(true);
    Out.trace(true, "File generated: %s%n", outputFile.getAbsolutePath());
    Out.trace(true, "Generation choices are: %s%n", genChoices.toString());
    out.close();
  }
  
  public void generateLayoutKML() {
    if (isAnyGenChoiceSet(GenChoice.Boundary)) {
      KMLGenerator.generateKMLRectangle(out, "Study Boundry", BoundaryRectangleStyle, boundaryRect);
    }
    generatePlotKMLs();
    generateShreddingPlotsKML();
  }
  
  /**
   * Generates a single plot in the study area layout.
   *
   * @param plotAnchor the anchor point for the plot, this is the lower left corner of the plot
   * @param row the row index of the plot in the study area layout
   * @param col the column index of the plot in the study area layout
   */
  public void generatePlotKML(GeoPoint plotAnchor, int row, int col) {
    /*
     * Generate the outer rectangle for the plot
     */
    GeoRectangle plotRect = GeoRectangle.fromCorner(plotAnchor, PlotWidth, PlotHeight,
            HorizontalBearing);
    String plotName = "%s%s".formatted(getPlotColumnName(col), getPlotRowName(row));
    if (ShowCoordinates) {
      Out.format("Plot %s:%n %s%n", plotName, plotRect.toString());
    }
    Style plotStyle;
    if (isAnyGenChoiceSet(GenChoice.FilledPlots)) {
      plotStyle = Style.RED_LtYellowFill;
    } else {
      plotStyle = Style.RED_MEDIUM;
    }
    if (isAnyGenChoiceSet(GenChoice.Grid, GenChoice.FilledPlots)) {
      KMLGenerator.generateKMLRectangle(out, plotName, plotStyle, plotRect);
    }
    /*
     * Generate the inner rectangle for the plot sampling area
     */
    if (isAnyGenChoiceSet(GenChoice.InnerRectangles)) {
      GeoPoint innerAnchor = Geo.projectPt(plotAnchor, HorizontalBearing, VerticalInnerPlotBuffer,
              HorizontalInnerPlotBuffer);
      GeoRectangle innerRect = GeoRectangle.fromCorner(innerAnchor,
              PlotWidth - (VerticalInnerPlotBuffer * 2),
              PlotHeight - (HorizontalInnerPlotBuffer * 2), HorizontalBearing);
      String innerRectangleName = "S%d.%d".formatted(row, col);
      KMLGenerator.generateKMLRectangle(out, innerRectangleName, InnerRectangleStyle, innerRect);
    }
    if (isAnyGenChoiceSet(GenChoice.Labels)) {
      // Generate the label for the plot
      GeoPoint labelPoint = Geo.projectPt(plotAnchor, HorizontalBearing, 0.8 * (PlotWidth / 2.0),
              PlotHeight / 2.0);
      KMLGenerator.generateLabel(out, plotName, labelPoint);
    }
  }
  
  /**
   * Generates all plots in the study area layout.
   */
  public void generatePlotKMLs() {
    for (int row = 1; row <= getTotalNumberOfPlotRows(); row++ ) {
      for (int col = 1; col <= NumberOfPlotColumns; col++ ) {
        GeoPoint plotAnchor = getPlotLowerLeft(row, col);
        generatePlotKML(plotAnchor, row, col);
      }
    }
  }
  
  /**
   * Generates the post points for the study area layout as a CSV file.
   */
  public void generatePostPointsCSV() {
    out.format("%10s,%10s,%10s,", "Name", "Lat1", "Long1");
    out.format("%10s,%10s,%10s%n", "NameE", "Lat2", "Long2");
    GeoRectangle outerRect = getStudyAreaOuterRectangle();
    generateRectanglePointsCSV(outerRect, "C", true);
    for (int row = 1; row <= (NumberOfPlotRowsInUpperSection
            + NumberOfPlotRowsInLowerSection); row++ ) {
      for (int col = 1; col <= NumberOfPlotColumns; col++ ) {
        GeoPoint plotAnchor = getPlotLowerLeft(row, col);
        GeoRectangle plotRect = GeoRectangle.fromCorner(plotAnchor, PlotWidth, PlotHeight,
                HorizontalBearing);
        boolean showSouth = false;
        if ((row == 7) || (row == 15)) {
          showSouth = true;
        }
        generateRectanglePointsCSV(plotRect, getPlotColumnName(col) + getPlotRowName(row), //
                showSouth);
      }
    }
  }
  
  /**
   * Generates the post points for the study area layout as a KML file of markers.
   */
  public void generatePostPointsKML() {
    generateRectanglePointsKML(boundaryRect, "C", true);
    for (int row = 1; row <= (NumberOfPlotRowsInUpperSection
            + NumberOfPlotRowsInLowerSection); row++ ) {
      for (int col = 1; col <= NumberOfPlotColumns; col++ ) {
        GeoPoint plotAnchor = getPlotLowerLeft(row, col);
        GeoRectangle plotRect = GeoRectangle.fromCorner(plotAnchor, PlotWidth, PlotHeight,
                HorizontalBearing);
        boolean showSouth = false;
        if ((row == 7) || (row == 15)) {
          showSouth = true;
        }
        generateRectanglePointsKML(plotRect, getPlotColumnName(col) + getPlotRowName(row), //
                showSouth);
      }
    }
  }
  
  /**
   * Generates the corner points of the specified rectangle as CSV entries.
   *
   * @param rect the rectangle for which to generate corner points
   * @param name the base name for the corner points
   * @param showSouth whether to show the south corners
   */
  public void generateRectanglePointsCSV(GeoRectangle rect, String name, boolean showSouth) {
    GeoPoint ul = rect.c3();
    GeoPoint ur = rect.c2();
    GeoPoint ll = rect.c0();
    GeoPoint lr = rect.c1();
    out.format("%5s-NW,%10.6f,%10.6f,", name, ul.lat(), ul.lon());
    out.format("%5s-NE,%10.6f,%10.6f%n", name, ur.lat(), ur.lon());
    if (showSouth) {
      out.format("%5s-SW,%10.6f,%10.6f,", name, ll.lat(), ll.lon());
      out.format("%5s-SE,%10.6f,%10.6f%n", name, lr.lat(), lr.lon());
    }
  }
  
  /**
   * Generates the corner points of the specified rectangle as KML markers.
   *
   * @param rect the rectangle for which to generate corner points
   * @param name the base name for the corner points
   * @param showSouth whether to show the south corners
   */
  public void generateRectanglePointsKML(GeoRectangle rect, String name, boolean showSouth) {
    GeoPoint ul = rect.c3();
    GeoPoint ur = rect.c2();
    GeoPoint ll = rect.c0();
    GeoPoint lr = rect.c1();
    KMLGenerator.generateMarker(out, name + "-NW", ul);
    KMLGenerator.generateMarker(out, name + "-NE", ur);
    if (showSouth) {
      KMLGenerator.generateMarker(out, name + "-SW", ll);
      KMLGenerator.generateMarker(out, name + "-SE", lr);
    }
  }
  
  public void generateSeedingStripsKML() {
    if (SeedingSriptsAccrossRows) {
      for (int row = 1; row <= getTotalNumberOfPlotRows(); row++ ) {
        GeoPoint rowAnchor = getPlotLowerLeft(row, 1);
        GeoPoint stripStart;
        double stripWidth;
        if (SeedingStripFullWidth) {
          stripStart = Geo.projectPt(rowAnchor, HorizontalBearing, -VerticalOuterBuffer);
          stripWidth = (((NumberOfPlotColumns * (PlotWidth + VerticalInnerBuffer))
                  - VerticalInnerBuffer)) + (2 * VerticalOuterBuffer);
        } else {
          stripStart = rowAnchor;
          stripWidth = (NumberOfPlotColumns * (PlotWidth + VerticalInnerBuffer))
                  - VerticalInnerBuffer;
        }
        for (int i = 0; i < SeedingStripOffsets.length; i++ ) {
          double offset = SeedingStripOffsets[i];
          GeoPoint stripLowerLeft = Geo.projectPt(stripStart, VerticalBearing, offset);
          GeoRectangle seedingStripRect = GeoRectangle.fromCorner(stripLowerLeft, stripWidth,
                  SeedingStripWidth, HorizontalBearing);
          String stripName = "%s%s-SS%df".formatted(getPlotColumnName(1), getPlotRowName(row), i);
          KMLGenerator.generateKMLRectangle(out, stripName, Style.GREEN, seedingStripRect);
        }
      }
    } else {
      for (int col = 1; col <= NumberOfPlotColumns; col++ ) {
        GeoPoint colAnchor = getPlotLowerLeft(15, col);
        GeoPoint stripStart;
        double stripWidth;
        if (SeedingStripFullWidth) {
          stripStart = Geo.projectPt(colAnchor, VerticalBearing, -HorizontalOuterBuffer);
          stripWidth = (getTotalNumberOfPlotRows() * PlotHeight) + VerticalInnerBuffer
                  + +(2 * VerticalOuterBuffer);
        } else {
          stripStart = colAnchor;
          stripWidth = (getTotalNumberOfPlotRows() * PlotHeight) + VerticalInnerBuffer;
        }
        for (int i = 0; i < SeedingStripOffsets.length; i++ ) {
          double offset = SeedingStripOffsets[i];
          GeoPoint stripLowerLeft = Geo.projectPt(stripStart, HorizontalBearing, offset);
          GeoRectangle seedingStripRect = GeoRectangle.fromCorner(stripLowerLeft, SeedingStripWidth,
                  stripWidth, HorizontalBearing);
          String stripName = "%s%s-SS%dc".formatted(getPlotColumnName(col), getPlotRowName(1), i);
          KMLGenerator.generateKMLRectangle(out, stripName, Style.GREEN, seedingStripRect);
        }
      }
    }
  }
  
  /**
   * Generates a single shredding plot as a KML rectangle.
   *
   * @param plotAnchor the anchor point for the shredding plot, this is the lower left corner
   *          of the plot
   * @param plotName the name for the shredding plot
   * @param northSide true if the shredding plot is on the north side of the layout, false if
   *          it is on the south
   */
  public void generateShreddingPlotKML(GeoPoint plotAnchor, String plotName, boolean northSide) {
    double height = -1.0 * 2.0 * (PlotWidth - (2 * VerticalInnerPlotBuffer));
    double width = (northSide ? 1.0 : -1.0) * 0.5 * (PlotHeight - (2 * HorizontalInnerPlotBuffer));
    GeoRectangle plotRect = GeoRectangle.fromCorner(plotAnchor, width, height, HorizontalBearing);
    if (ShowCoordinates) {
      Out.format("Shredding Plot %s:%n %s%n", plotName, plotRect.toString());
    }
    Style plotStyle;
    if (isAnyGenChoiceSet(GenChoice.FilledPlots)) {
      plotStyle = Style.RED_LtYellowFill;
    } else {
      plotStyle = Style.RED_MEDIUM;
    }
    if (isAnyGenChoiceSet(GenChoice.Grid, GenChoice.FilledPlots)) {
      KMLGenerator.generateKMLRectangle(out, plotName, plotStyle, plotRect);
    }
    if (isAnyGenChoiceSet(GenChoice.Labels)) {
      // Generate the label for the plot
      GeoPoint labelPoint = Geo.projectPt(plotAnchor, HorizontalBearing, 0.8 * (width / 2.0),
              height / 2.0);
      KMLGenerator.generateLabel(out, plotName, labelPoint);
    }
  }
  
  /**
   * Generates the 4 shredding-only plots in the outer buffer of the study area layout as KML
   * rectangles. These are all placed in the horizontal outer buffer, two on the west side and
   * two on the east side.
   * <p>
   * Shredding plots are twice the width of the normal plots minus the inner buffers and half
   * the height of the normal plots minus the inner buffers.
   * <p>
   * The shredding plots are named SNW, SSW, SNE, and SSW, where the first letter indicates
   * that they are shredding plots and the next two letters indicate their location (NW, SW,
   * NE, SE).
   */
  public void generateShreddingPlotsKML() {
    /*
     * Compute the for NW corner (which is the lower left corner) of each shredding plot.
     */
    double horizontalOffset = 12.0; // distance from the western most edge of the west plots
                                    // and
    // the eastern most edge of the east plots to the shredding
    // plots
    /*
     * A shredding plot reference point is it SW corner
     */
    Bearing bearing = HorizontalBearing;
    GeoPoint snwRefPoint = getPlotLowerLeft(1, 1);
    GeoPoint snwAnchor = Geo.projectPt(snwRefPoint, bearing, -1 * horizontalOffset);
    GeoPoint sswRefPoint = getPlotLowerLeft(10, 1);
    GeoPoint sswAnchor = Geo.projectPt(sswRefPoint, bearing, -1 * horizontalOffset);
    GeoPoint sneRefPoint = getPlotLowerLeft(5, 4);
    GeoPoint sneAnchor = Geo.projectPt(sneRefPoint, bearing, PlotHeight + horizontalOffset);
    GeoPoint sseRefPoint = getPlotLowerLeft(13, 4);
    GeoPoint sseAnchor = Geo.projectPt(sseRefPoint, bearing, PlotHeight + horizontalOffset);
    double height = 2.0 * (PlotWidth - (2 * VerticalInnerPlotBuffer));
    sseAnchor = Geo.projectPt(sseAnchor, VerticalBearing, height - PlotHeight);
    generateShreddingPlotKML(snwAnchor, "SO1", false);
    generateShreddingPlotKML(sswAnchor, "SO2", false);
    generateShreddingPlotKML(sneAnchor, "SO4", true);
    generateShreddingPlotKML(sseAnchor, "SO3", true);
  }
  
  /**
   * Gets the column name for the specified plot in the study area layout. Columns are numbered
   * from left to right starting with 1, and named A, B, C, etc.
   *
   * @param col the column index of the plot in the study area layout
   * @return the column name for the specified plot
   */
  public String getPlotColumnName(int col) {
    char columnChar = ColumnNameChar[col - 1];
    return "%c".formatted(columnChar);
  }
  
  /**
   * Gets the lower left corner of the specified plot in the study area layout.This is the SW
   * corner of the plot. Row and column are both 1 based. Rows are numbered from top to bottom
   * and columns are numbered from left to right. The anchor returned allows for the gaps
   * between column and the gap between the upper and lower sections
   *
   * @param row the row index of the plot in the study area layout
   * @param col the column index of the plot in the study area layout
   * @return the lower left corner of the specified plot
   */
  public GeoPoint getPlotLowerLeft(int row, int col) {
    GeoPoint lowerSectionAnchor = Geo.projectPt(LayoutAnchor, HorizontalBearing,
            HorizontalOuterBuffer, VerticalOuterBuffer);
    double verticalOffset = (getTotalNumberOfPlotRows() - row) * PlotHeight;
    if (row <= NumberOfPlotRowsInUpperSection) {
      // Plot is in upper section so add the inner buffer
      verticalOffset += HorizontalInnerBuffer;
    }
    double horizontalOffset = (col - 1) * (PlotWidth + VerticalInnerBuffer);
    GeoPoint plotAnchor = Geo.projectPt(lowerSectionAnchor, HorizontalBearing, horizontalOffset,
            verticalOffset);
    return plotAnchor;
  }
  
  /// Gets the rectangle for the specified plot in the study area layout. This is the rectangle
  /// defined by the lower left (SW) corner of the plot and the plot width and height. The
  /// rectangle is oriented based on the horizontal bearing of the layout. The lower left corner
  /// of the plot is the anchor point for the rectangle, and the width and height are defined in
  /// the direction of the horizontal and vertical bearings, respectively.
  ///
  /// @param row the row index of the plot in the study area layout
  /// @param col the column index of the plot in the study area layout
  /// @return the rectangle for the specified plot
  public GeoRectangle getPlotRect(int row, int col) {
    GeoPoint plotAnchor = getPlotLowerLeft(row, col);
    GeoRectangle plotRect = GeoRectangle.fromCorner(plotAnchor, PlotWidth, PlotHeight,
            HorizontalBearing);
    return plotRect;
  }
  
  /**
   * Gets the row name for the specified plot in the study area layout. Rows are numbered from
   * top to bottom starting with 1, and named 01, 02, 03, etc.
   *
   * @param row the row index of the plot in the study area layout
   * @return the row name for the specified plot
   */
  public String getPlotRowName(int row) {
    return "%02d".formatted(row);
  }
  
  /**
   * Gets the outer rectangle that defines the study area layout.
   *
   * @return the outer rectangle that defines the study area layout
   */
  public GeoRectangle getStudyAreaOuterRectangle() {
    double totalHeight = (PlotHeight * getTotalNumberOfPlotRows()) + (HorizontalOuterBuffer * 2)
            + HorizontalInnerBuffer;
    double totalWidth = (PlotWidth * NumberOfPlotColumns) + (VerticalOuterBuffer * 2)
            + (VerticalInnerBuffer * (NumberOfPlotColumns - 1));
    GeoRectangle outerRect = GeoRectangle.fromCorner(LayoutAnchor, totalWidth, totalHeight,
            HorizontalBearing);
    return outerRect;
  }
  
  public int getTotalNumberOfPlotRows() {
    return NumberOfPlotRowsInUpperSection + NumberOfPlotRowsInLowerSection;
  }
  
  /**
   * Checks if any of the specified generation choices are set.
   *
   * @param choices the generation choices to check
   * @return true if any of the specified generation choices are set, false otherwise
   */
  public boolean isAnyGenChoiceSet(GenChoice... choices) {
    for (GenChoice choice : choices) {
      if (genChoices.contains(choice))
        return true;
    }
    return false;
  }
}
