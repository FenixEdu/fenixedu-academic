package net.sourceforge.fenixedu.presentationTier.gwt.frameworks.Raphael.client;

/**
 * Programmatic API to build SVG paths
 *
 * See http://www.w3.org/TR/SVG/paths.html#PathData
 */
public class PathBuilder {

  private StringBuffer s = new StringBuffer();

  public PathBuilder() { }

  /**
   * append an SVG Path command to this instance
   */
  public PathBuilder append(String cmd, double ... coords) {
    s.append(cmd).append(" ");
    for (double a : coords) {
      s.append(a).append(" ");
    }
    return this;
  }

  /**
   * remove all commands from this instance
   */
  public void clear() {
    s.delete(0, s.length());
  }

  /**
   * generate the SVG Path string representation of this instance
   */
  public String toString() {
    return s.toString();
  }

  /**
   * moveto (absolute)
   *
   * Start a new sub-path at the given (x,y) coordinate. M (uppercase) indicates that absolute
   * coordinates will follow; m (lowercase) indicates that relative coordinates will follow.
   * If a relative moveto (m) appears as the first element of the path, then it is treated as
   * a pair of absolute coordinates. If a moveto is followed by multiple pairs of coordinates,
   * the subsequent pairs are treated as implicit lineto commands.
   *
   * @param coords (x y)+
   */
  public PathBuilder M(double ... coords) {
    return append("M", coords);
  }

  /**
   * moveto (relative)
   *
   * Start a new sub-path at the given (x,y) coordinate. M (uppercase) indicates that absolute
   * coordinates will follow; m (lowercase) indicates that relative coordinates will follow.
   * If a relative moveto (m) appears as the first element of the path, then it is treated as
   * a pair of absolute coordinates. If a moveto is followed by multiple pairs of coordinates,
   * the subsequent pairs are treated as implicit lineto commands.
   *
   * @param coords (x y)+
   */
  public PathBuilder m(double ... coords) {
    return append("m", coords);
  }

  /**
   * closepath
   *
   * Close the current subpath by drawing a straight line from the current point to current
   * subpath's initial point.
   */
  public PathBuilder Z() {
    return append("Z");
  }

  /**
   * closepath
   *
   * Close the current subpath by drawing a straight line from the current point to current
   * subpath's initial point.
   */
  public PathBuilder z() {
    return append("z");
  }

  /**
   * lineto (absolute)
   *
   * Draw a line from the current point to the given (x,y) coordinate which becomes the new
   * current point. L (uppercase) indicates that absolute coordinates will follow; l (lowercase)
   * indicates that relative coordinates will follow. A number of coordinates pairs may be
   * specified to draw a polyline. At the end of the command, the new current point is set
   * to the final set of coordinates provided.
   *
   * @param coords (x y)+
   */
  public PathBuilder L(double ... coords) {
    return append("L", coords);
  }

  /**
   * lineto (relative)
   *
   * Draw a line from the current point to the given (x,y) coordinate which becomes the new
   * current point. L (uppercase) indicates that absolute coordinates will follow; l (lowercase)
   * indicates that relative coordinates will follow. A number of coordinates pairs may be
   * specified to draw a polyline. At the end of the command, the new current point is set
   * to the final set of coordinates provided.
   *
   * @param coords (x y)+
   */
  public PathBuilder l(double ... coords) {
    return append("l", coords);
  }

  /**
   * horizontal lineto (absolute)
   *
   * Draws a horizontal line from the current point (cpx, cpy) to (x, cpy). H (uppercase)
   * indicates that absolute coordinates will follow; h  (lowercase) indicates that relative
   * coordinates will follow. Multiple x values can be provided (although usually this doesn't
   * make sense). At the end of the command, the new current point becomes (x, cpy) for the
   * final value of x.
   *
   * @param coords x+
   */
  public PathBuilder H(double ... coords) {
    return append("H", coords);
  }

  /**
   * horizontal lineto (relative)
   *
   * Draws a horizontal line from the current point (cpx, cpy) to (x, cpy). H (uppercase)
   * indicates that absolute coordinates will follow; h  (lowercase) indicates that relative
   * coordinates will follow. Multiple x values can be provided (although usually this doesn't
   * make sense). At the end of the command, the new current point becomes (x, cpy) for the
   * final value of x.
   *
   * @param coords x+
   */
  public PathBuilder h(double ... coords) {
    return append("h", coords);
  }

  /**
   * vertical lineto (absolute)
   *
   * Draws a vertical line from the current point (cpx, cpy) to (cpx, y). V (uppercase)
   * indicates that absolute coordinates will follow; v  (lowercase) indicates that relative
   * coordinates will follow. Multiple y values can be provided (although usually this doesn't
   * make sense). At the end of the command, the new current point becomes (cpx, y) for the
   * final value of y.
   *
   * @param coords y+
   */
  public PathBuilder V(double ... coords) {
    return append("V", coords);
  }

  /**
   * vertical lineto (relative)
   *
   * Draws a vertical line from the current point (cpx, cpy) to (cpx, y). V (uppercase)
   * indicates that absolute coordinates will follow; v  (lowercase) indicates that relative
   * coordinates will follow. Multiple y values can be provided (although usually this doesn't
   * make sense). At the end of the command, the new current point becomes (cpx, y) for the
   * final value of y.
   *
   * @param coords y+
   */
  public PathBuilder v(double ... coords) {
    return append("v", coords);
  }

  /**
   * cubic Bezier curveto (absolute)
   *
   * Draws a cubic Bezier curve from the current point to (x,y) using (x1,y1) as the control
   * point at the beginning of the curve and (x2,y2) as the control point at the end of the
   * curve. C (uppercase) indicates that absolute coordinates will follow; c (lowercase)
   * indicates that relative coordinates will follow. Multiple sets of coordinates may be
   * specified to draw a polybezier. At the end of the command, the new current point becomes
   * the final (x,y) coordinate pair used in the polybezier.
   *
   * @param coords (x1 y1 x2 y2 x y)+
   */
  public PathBuilder C(double ... coords) {
    return append("C", coords);
  }

  /**
   * cubic Bezier curveto (relative)
   *
   * Draws a cubic Bezier curve from the current point to (x,y) using (x1,y1) as the control
   * point at the beginning of the curve and (x2,y2) as the control point at the end of the
   * curve. C (uppercase) indicates that absolute coordinates will follow; c (lowercase)
   * indicates that relative coordinates will follow. Multiple sets of coordinates may be
   * specified to draw a polybezier. At the end of the command, the new current point becomes
   * the final (x,y) coordinate pair used in the polybezier.
   *
   * @param coords (x1 y1 x2 y2 x y)+
   */
  public PathBuilder c(double ... coords) {
    return append("c", coords);
  }

  /**
   * shorthand/smooth curveto (absolute)
   *
   * Draws a cubic Bezier curve from the current point to (x,y). The first control point is
   * assumed to be the reflection of the second control point on the previous command relative
   * to the current point. (If there is no previous command or if the previous command was not
   * an C, c, S or s, assume the first control point is coincident with the current point.)
   * (x2,y2) is the second control point (i.e., the control point at the end of the curve).
   * S (uppercase) indicates that absolute coordinates will follow; s (lowercase) indicates
   * that relative coordinates will follow. Multiple sets of coordinates may be specified to
   * draw a polybezier. At the end of the command, the new current point becomes the final
   * (x,y) coordinate pair used in the polybezier.
   *
   * @param coords (x2 y2 x y)+
   */
  public PathBuilder S(double ... coords) {
    return append("S", coords);
  }

  /**
   * shorthand/smooth curveto (relative)
   *
   * Draws a cubic Bezier curve from the current point to (x,y). The first control point is
   * assumed to be the reflection of the second control point on the previous command relative
   * to the current point. (If there is no previous command or if the previous command was not
   * an C, c, S or s, assume the first control point is coincident with the current point.)
   * (x2,y2) is the second control point (i.e., the control point at the end of the curve).
   * S (uppercase) indicates that absolute coordinates will follow; s (lowercase) indicates
   * that relative coordinates will follow. Multiple sets of coordinates may be specified to
   * draw a polybezier. At the end of the command, the new current point becomes the final
   * (x,y) coordinate pair used in the polybezier.
   *
   * @param coords (x2 y2 x y)+
   */
  public PathBuilder s(double ... coords) {
    return append("s", coords);
  }

  /**
   * quadratic Bezier curveto (absolute)
   *
   * Draws a quadratic Bezier curve from the current point to (x,y) using (x1,y1) as the
   * control point. Q (uppercase) indicates that absolute coordinates will follow; q (lowercase)
   * indicates that relative coordinates will follow. Multiple sets of coordinates may be
   * specified to draw a polybezier. At the end of the command, the new current point becomes
   * the final (x,y) coordinate pair used in the polybezier.
   *
   * @param coords (x1 y1 x y)+
   */
  public PathBuilder Q(double ... coords) {
    return append("Q", coords);
  }

  /**
   * quadratic Bezier curveto (relative)
   *
   * Draws a quadratic Bezier curve from the current point to (x,y) using (x1,y1) as the
   * control point. Q (uppercase) indicates that absolute coordinates will follow; q (lowercase)
   * indicates that relative coordinates will follow. Multiple sets of coordinates may be
   * specified to draw a polybezier. At the end of the command, the new current point becomes
   * the final (x,y) coordinate pair used in the polybezier.
   *
   * @param coords (x1 y1 x y)+
   */
  public PathBuilder q(double ... coords) {
    return append("q", coords);
  }

  /**
   * shorthand/smooth quadratic Bezier curveto (absolute)
   *
   * Draws a quadratic Bezier curve from the current point to (x,y). The control point is
   * assumed to be the reflection of the control point on the previous command relative to
   * the current point. (If there is no previous command or if the previous command was not
   * a Q, q, T or t, assume the control point is coincident with the current point.) T (uppercase)
   * indicates that absolute coordinates will follow; t  (lowercase) indicates that relative
   * coordinates will follow. At the end of the command, the new current point becomes the
   * final (x,y) coordinate pair used in the polybezier.
   *
   * @param coords (x y)+
   */
  public PathBuilder T(double ... coords) {
    return append("T", coords);
  }

  /**
   * shorthand/smooth quadratic Bezier curveto (relative)
   *
   * Draws a quadratic Bezier curve from the current point to (x,y). The control point is
   * assumed to be the reflection of the control point on the previous command relative to
   * the current point. (If there is no previous command or if the previous command was not
   * a Q, q, T or t, assume the control point is coincident with the current point.) T (uppercase)
   * indicates that absolute coordinates will follow; t  (lowercase) indicates that relative
   * coordinates will follow. At the end of the command, the new current point becomes the
   * final (x,y) coordinate pair used in the polybezier.
   *
   * @param coords (x y)+
   */
  public PathBuilder t(double ... coords) {
    return append("t", coords);
  }

  /**
   * elliptical arc (absolute)
   *
   * Draws an elliptical arc from the current point to (x, y). The size and orientation of
   * the ellipse are defined by two radii (rx, ry) and an x-axis-rotation, which indicates
   * how the ellipse as a whole is rotated relative to the current coordinate system. The
   * center (cx, cy) of the ellipse is calculated automatically to satisfy the constraints
   * imposed by the other parameters. large-arc-flag and sweep-flag contribute to the
   * automatic calculations and help determine how the arc is drawn.
   *
   * @param coords (rx ry x-axis-rotation large-arc-flag sweep-flag x y)+
   */
  public PathBuilder A(double ... coords) {
    return append("A", coords);
  }

  /**
   * elliptical arc (relative)
   *
   * Draws an elliptical arc from the current point to (x, y). The size and orientation of
   * the ellipse are defined by two radii (rx, ry) and an x-axis-rotation, which indicates
   * how the ellipse as a whole is rotated relative to the current coordinate system. The
   * center (cx, cy) of the ellipse is calculated automatically to satisfy the constraints
   * imposed by the other parameters. large-arc-flag and sweep-flag contribute to the
   * automatic calculations and help determine how the arc is drawn.
   *
   * @param coords (rx ry x-axis-rotation large-arc-flag sweep-flag x y)+
   */
  public PathBuilder a(double ... coords) {
    return append("a", coords);
  }

}

