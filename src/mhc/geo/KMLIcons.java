package mhc.geo;

/// Enum representing common KML icon styles with their associated href URLs. Includes
/// pushpins, letter/number paddles, and the placemark circle.
public enum KMLIcons {
  
  // Pushpins
  PushpinRed("http://maps.google.com/mapfiles/kml/pushpin/red-pushpin.png"),
  PushpinBlue("http://maps.google.com/mapfiles/kml/pushpin/blue-pushpin.png"),
  PushpinGreen("http://maps.google.com/mapfiles/kml/pushpin/grn-pushpin.png"),
  PushpinYellow("http://maps.google.com/mapfiles/kml/pushpin/ylw-pushpin.png"),
  // Letter paddles A-Z
  PaddleA("http://maps.google.com/mapfiles/kml/paddle/A.png"),
  PaddleB("http://maps.google.com/mapfiles/kml/paddle/B.png"),
  PaddleC("http://maps.google.com/mapfiles/kml/paddle/C.png"),
  PaddleD("http://maps.google.com/mapfiles/kml/paddle/D.png"),
  PaddleE("http://maps.google.com/mapfiles/kml/paddle/E.png"),
  PaddleF("http://maps.google.com/mapfiles/kml/paddle/F.png"),
  PaddleG("http://maps.google.com/mapfiles/kml/paddle/G.png"),
  PaddleH("http://maps.google.com/mapfiles/kml/paddle/H.png"),
  PaddleI("http://maps.google.com/mapfiles/kml/paddle/I.png"),
  PaddleJ("http://maps.google.com/mapfiles/kml/paddle/J.png"),
  PaddleK("http://maps.google.com/mapfiles/kml/paddle/K.png"),
  PaddleL("http://maps.google.com/mapfiles/kml/paddle/L.png"),
  PaddleM("http://maps.google.com/mapfiles/kml/paddle/M.png"),
  PaddleN("http://maps.google.com/mapfiles/kml/paddle/N.png"),
  PaddleO("http://maps.google.com/mapfiles/kml/paddle/O.png"),
  PaddleP("http://maps.google.com/mapfiles/kml/paddle/P.png"),
  PaddleQ("http://maps.google.com/mapfiles/kml/paddle/Q.png"),
  PaddleR("http://maps.google.com/mapfiles/kml/paddle/R.png"),
  PaddleS("http://maps.google.com/mapfiles/kml/paddle/S.png"),
  PaddleT("http://maps.google.com/mapfiles/kml/paddle/T.png"),
  PaddleU("http://maps.google.com/mapfiles/kml/paddle/U.png"),
  PaddleV("http://maps.google.com/mapfiles/kml/paddle/V.png"),
  PaddleW("http://maps.google.com/mapfiles/kml/paddle/W.png"),
  PaddleX("http://maps.google.com/mapfiles/kml/paddle/X.png"),
  PaddleY("http://maps.google.com/mapfiles/kml/paddle/Y.png"),
  PaddleZ("http://maps.google.com/mapfiles/kml/paddle/Z.png"),
  // Number paddles 0-9
  Paddle0("http://maps.google.com/mapfiles/kml/paddle/0.png"),
  Paddle1("http://maps.google.com/mapfiles/kml/paddle/1.png"),
  Paddle2("http://maps.google.com/mapfiles/kml/paddle/2.png"),
  Paddle3("http://maps.google.com/mapfiles/kml/paddle/3.png"),
  Paddle4("http://maps.google.com/mapfiles/kml/paddle/4.png"),
  Paddle5("http://maps.google.com/mapfiles/kml/paddle/5.png"),
  Paddle6("http://maps.google.com/mapfiles/kml/paddle/6.png"),
  Paddle7("http://maps.google.com/mapfiles/kml/paddle/7.png"),
  Paddle8("http://maps.google.com/mapfiles/kml/paddle/8.png"),
  Paddle9("http://maps.google.com/mapfiles/kml/paddle/9.png"),
  // Placemark circle
  PlacemarkCircle("http://maps.google.com/mapfiles/kml/shapes/placemark_circle.png");
  
  private final String href;
  
  KMLIcons(String href) {
    this.href = href;
  }
  
  /// Returns the href URL string for this KML icon.
  ///
  /// @return the icon href URL
  public String getHref() {
    return href;
  }
}
