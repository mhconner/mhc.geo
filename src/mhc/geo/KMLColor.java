package mhc.geo;

/// defines a set of standard KML colors in `aabbggrr` format, where `aa` is the alpha
/// (opacity) component, `bb` is the blue component, `gg` is the green component, and `rr` is
/// the red component. These colors can be used for styling KML elements such as placemarks,
/// lines, and polygons.
public enum KMLColor {
  
  White("ffffffff"), //
  Black("ff000000"), //
  Red("ff0000ff"), //
  Green("ff00ff00"), //
  Blue("ffff0000"), //
  Yellow("ff00ffff"), //
  Cyan("ffffff00"), //
  Magenta("ffff00ff"), //
  Orange("ff0080ff"), //
  Purple("ff800080"), //
  Pink("ffcbc0ff"), //
  Brown("ff2a2aa5"), //
  Gray("ff808080"), //
  LightGray("ffd3d3d3"), //
  DarkGray("ff404040"), //
  Lime("ff00ff80"), //
  Teal("ff808000"), //
  Navy("ff800000"), //
  Maroon("ff000080"), //
  Olive("ff008080"); //
  
  private final String colorValue;
  
  KMLColor(String colorValue) {
    this.colorValue = colorValue;
  }
  
  /// Returns the KMLColor corresponding to the given color value. The color value should be in
  /// `aabbggrr` format. If no matching KMLColor is found, an IllegalArgumentException is
  /// thrown.
  ///
  /// @param color the KML color value in `aabbggrr` format
  /// @return the corresponding KMLColor enum constant
  /// @throws IllegalArgumentException if no KMLColor with the specified color value exists
  /// @see #getColorValue()
  static KMLColor fromColorValue(String color) {
    for (KMLColor kmlColor : KMLColor.values()) {
      if (kmlColor.getColorValue().equalsIgnoreCase(color))
        return kmlColor;
    }
    throw new IllegalArgumentException("No KMLColor with color value: " + color);
  }
  
  /// Returns the alpha (opacity) component of the KML color as a hexadecimal string. The alpha
  /// component is represented by the first two characters of the color value string, which is
  /// in `aabbggrr` format. For example, if the color value is "80ff0000", this method will
  /// return "80", which represents the alpha component of the color (50% opacity).
  /// 
  /// @return the alpha component of the KML color as a hexadecimal string
  /// @see #getColorValue()
  public String getAlphaValue() {
    return colorValue.substring(0, 2);
  }

  /// Returns the blue component of the KML color as a hexadecimal string. The blue component
  /// is represented by the third and fourth characters of the color value string, which is in
  /// `aabbggrr` format. For example, if the color value is "ffff0000", this method will return
  /// "ff", which represents the blue component of the color.
  /// 
  /// @return the blue component of the KML color as a hexadecimal string
  /// @see #getColorValue()
  public String getBlueValue() {
    return colorValue.substring(2, 4);
  }

  /**
   * Returns the KML color value in aabbggrr format.
   *
   * @return the KML color string
   */
  public String getColorValue() {
    return colorValue;
  }

  /// Returns the green component of the KML color as a hexadecimal string. The green component
  /// is represented by the middle two characters of the color value string, which is in
  /// `aabbggrr` format. For example, if the color value is "ff00ff00", this method will return
  /// "ff", which represents the green component of the color.
  /// 
  /// @return the green component of the KML color as a hexadecimal string
  /// @see #getColorValue()
  public String getGreenValue() {
    return colorValue.substring(4, 6);
  }

  /// Returns the red component of the KML color as a hexadecimal string. The red component is
  /// represented by the last two characters of the color value string, which is in `aabbggrr`
  /// format. For example, if the color value is "ff0000ff", this method will return "ff", which
  /// represents the red component of the color.
  public String getRedValue() {
    return colorValue.substring(6, 8);
  }
}
