package net.sourceforge.fenixedu.presentationTier.gwt.frameworks.Raphael.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

/**
 * Overlay class for GWT to use raphaeljs library
 */
class RaphaelJS extends JavaScriptObject {

  /**
   * determine whether Raphael is defined on the client platform
   * NB: do not call any other methods if Raphael is not defined
   */
  static public final native boolean isDefined() /*-{
    return $wnd.Raphael!=undefined;
  }-*/;

  /**
   * Raphael factory method
   */
  static public final native RaphaelJS create(String elementId, int width, int height) /*-{
      return $wnd.Raphael(elementId, width, height);
  }-*/;

  /**
   * Raphael factory method
   */
  static public final native RaphaelJS create(com.google.gwt.user.client.Element element, int width, int height) /*-{
      return $wnd.Raphael(element, width, height);
  }-*/;

  /**
   * bounding box object returned by Element.getBBox()
   */
  protected static class BBox extends JavaScriptObject {
    protected BBox() {}
    public final native double x() /*-{
      if (this.x == undefined)
        return -1;
      else
        return this.x;
    }-*/;
    public final native double y() /*-{
      if (this.y == undefined)
        return -1;
      else
        return this.y;
    }-*/;
    public final native double width() /*-{
      if (this.width == undefined)
        return -1;
      else
        return this.width;
    }-*/;
    public final native double height() /*-{
      if (this.height == undefined)
        return -1;
      else
        return this.height;
    }-*/;
  }

  /**
   * color object returned by Element.getColor() and friends
   */
  protected static class Color extends JavaScriptObject {
    protected Color() {}
    /**
     * reset getColor() function to restart from the beginning
     */
    public final native void reset() /*-{
      this.reset();
    }-*/;
  }

  /**
   * element objected returned by circle(), rect(), etc.
   */
  protected static class Element extends JavaScriptObject {
    protected Element() { }

    /**
     * Gives you a reference to the DOM object, so you can assign event handlers or just mess around. 
     *
     * @return reference to the current DOM object
     */
    public final native com.google.gwt.user.client.Element node() /*-{
      return this.node;
    }-*/;

    /**
     *  Removes element from the DOM. You can't use it after this method call. 
     */
    public final native void remove() /*-{
      this.remove();
    }-*/;

    /**
     * Makes element invisible
     *
     * @return the javascript object hidden
     */
    public final native Element hide() /*-{
      return this.hide();
    }-*/;

    /**
     * Makes element visible
     *
     * @return the javascript object made visible
     */
    public final native Element show() /*-{
      return this.show();
    }-*/;

    /**
     * Rotates the element by the given degree from its center point relatively.
     *
     * @param degree        number of degrees of rotation (0-360)
     *
     * @return the javascript object rotated
     */
    public final native Element rotate(double degree) /*-{
      return this.rotate(degree);
    }-*/;

    /**
     * Rotates the element by the given degree from its center point.
     *
     * @param degree        number of degrees of rotation (0-360)
     * @param isAbsolute    boolean Specifies if degree is relative to previous position (false) or is it absolute angle (true)
     *
     * @return the javascript object rotated
     */
    public final native Element rotate(double degree, boolean isAbsolute) /*-{
      return this.rotate(degree, isAbsolute);
    }-*/;

    /**
     * Rotates the element by the given degree from its center point relatively.
     *
     * @param degree        number of degrees of rotation (0-360)
     * @param cx            number X coordinate of center of rotation        
     * @param cy            number Y coordinate of center of rotation        
     *
     * @return the javascript object rotated
     */
    public final native Element rotate(double degree, double cx, double cy) /*-{
      return this.rotate(degree, cx, cy);
    }-*/;

    /**
     * Moves the element around the canvas by the given distances.
     *
     * @param dx    number of pixels of translation by X-axis
     * @param dy    number of pixels of translation by Y-axis
     *
     * @return the javascript object translated
     */
    public final native Element translate(double dx, double dy) /*-{
      return this.translate(dx, dy);
    }-*/;

    /**
     * Resizes the element by the given multipliers.
     *
     * @param Xtimes    factor to scale horizontally
     * @param Ytimes    factor to scale vertically
     *
     * @return the javascript object scaled
     */
    public final native Element scale(double Xtimes, double Ytimes) /*-{
      return this.scale(Xtimes, Ytimes);
    }-*/;

    /**
     * Resizes the element by the given multipliers.
     *
     * @param Xtimes    factor to scale horizontally
     * @param Ytimes    factor to scale vertically
     *
     * @return the javascript object scaled
     */
    public final native Element scale(double Xtimes, double Ytimes, double centerX) /*-{
      return this.scale(Xtimes, Ytimes, centerX);
    }-*/;

    /**
     * Resizes the element by the given multipliers.
     *
     * @param Xtimes    factor to scale horizontally
     * @param Ytimes    factor to scale vertically
     *
     * @return the javascript object scaled
     */
    public final native Element scale(double Xtimes, double Ytimes, double centerX, double centerY) /*-{
      return this.scale(Xtimes, Ytimes, centerX, centerY);
    }-*/;

    /**
     * Linearly changes an attribute from its current value to its specified value in the given amount of milliseconds. 
     *
     * @param newAttrs    string Attributes of the object after animation (not all attributes can be animated)
     *  @attributes_to_animate:
     *   1.  cx number
     *   2.  cy number
     *   3.  fill colour
     *   4.  fill-opacity number
     *   5.  font-size number
     *   6.  height number
     *   7.  opacity number
     *   8.  path pathString
     *   9.  r number
     *   10. rotation number
     *   11. rx number
     *   12. ry number
     *   13. scale CSV
     *   14. stroke colour
     *   15. stroke-opacity number
     *   16. stroke-width number
     *   17. translation CSV
     *   18. width number
     *   19. x number
     *   20. y number
     * @param duration    The duration of the animation, given in milliseconds
     *
     */
    public final native Element animate(JavaScriptObject newAttrs, int ms) /*-{
      return this.animate(newAttrs, ms);
    }-*/;

    /**
     * Linearly changes an attribute from its current value to its specified value in the given amount of milliseconds (with callback)
     *
     * @param newAttrs    string Attributes of the object after animation (not all attributes can be animated)
     *  @attributes_to_animate: see animate(String, int) function for complete listing
     * @param ms      int the duration of the animation, given in milliseconds
     * @param easing  [>, <, <>, backIn, backOut, bounce, elastic]
     *
     */
    public final native Element animate(JavaScriptObject newAttrs, int ms, String easing) /*-{
      return this.animate(newAttrs, ms, easing);
    }-*/;

    /**
     * Sets the attributes of elements directly.
     *
     * @param attributeName    string of the attribute name
     *  @attribute_names:
     *   1.  cx number
     *   2.  cy number
     *   3.  fill colour
     *   4.  fill-opacity number
     *   5.  font string
     *   6.  font-family string
     *   7.  font-size number
     *   8.  font-weight string
     *   9.  gradient object|string
     *   10. height number
     *   11. opacity number
     *   12. path pathString
     *   13. r number
     *   14. rotation number
     *   15. rx number
     *   16. ry number
     *   17. scale CSV
     *   18. src string (URL)
     *   19. stroke colour
     *   20. stroke-dasharray string
     *   21. stroke-linecap string
     *   22. stroke-linejoin string
     *   23. stroke-miterlimit number
     *   24. stroke-opacity number
     *   25. stroke-width number
     *   26. translation CSV
     *   27. width number
     *   28. x number
     *   29. y number
     * @param value    string of the new value    
     *
     * @return the javascript object having attributes modified
     */
    public final native Element attr(String attributeName, String value) /*-{
      return this.attr(attributeName, value);
    }-*/;
    public final native Element attr(String attributeName, double value) /*-{
      return this.attr(attributeName, value);
    }-*/;

    /**
     * sets a values for given attribute names
     *
     * @return the current value for the given attribute name
     */
    public final native Element attr(JavaScriptObject params) /*-{
      return this.attr(params);
    }-*/;

    /**
     * gets the value for an attribute
     *
     * @return the current value for the given attribute name
     */
    public final native double attrAsDouble(String attributeName) /*-{
      return this.attr(attributeName);
    }-*/;

    /**
     * gets the value for an attribute
     *
     * @return the current value for the given attribute name
     */
    public final native String attrAsString(String attributeName) /*-{
      return this.attr(attributeName);
    }-*/;

    /**
     * gets an array of values for given attribute names
     *
     * @return the current value for the given attribute name
     */
    public final native JsArray attr(JsArray attributeNames) /*-{
      return this.attr(attributeNames);
    }-*/;

    public final native BBox getBBox() /*-{
      return this.getBBox();
    }-*/;

    public final native Element toFront() /*-{
      return this.toFront();
    }-*/;

    public final native Element toBack() /*-{
      return this.toBack();
    }-*/;

    public final native Element insertBefore(Element obj) /*-{
      return this.insertBefore(obj);
    }-*/;

    public final native Element insertAfter(Element obj) /*-{
      return this.insertAfter(obj);
    }-*/;
  }

  /**
   * font object returned by Path.getFont() and friends
   */
  protected static class Font extends JavaScriptObject {
    protected Font() {}
  }

  /**
   * path object returned by RaphaelJS.path()
   */
  protected static class Path extends Element {
    protected Path() {}
  }

  protected static class Set extends Element {
    protected Set() {}
    public final native Set push(Element obj) /*-{
      return this.push(obj);
    }-*/;
    public final native Element pop() /*-{
      return this.pop();
    }-*/;
  }

  /**
   * overlay raphael class constructor - must be protected, empty, and no-argument
   */
  protected RaphaelJS() {}

  /**
   * Draw a circle to the Raphael canvas
   *
   * @param x     number x coordinate of the center
   * @param y     number y coordinate of the center
   * @param r     r number radius of the circle
   *
   * @return      the circle object
   */
  public final native Element circle(double x, double y, double r) /*-{
    return this.circle(x,y,r);
  }-*/;

  /**
   * Draw an ellipse to the Raphael canvas
   *
   * @param x     number x coordinate of the center
   * @param y     number y coordinate of the center
   * @param rx    number horizontal radius
   * @param ry    number vertical radius
   *
   * @return      the ellipse object
   */
  public final native Element ellipse(double x, double y, double rx, double ry) /*-{
    return this.ellipse(x, y, rx, ry);
  }-*/;

  /**
   * return the next color in the spectrum
   */
  public final native Color getColor() /*-{
    return this.getColor();
  }-*/;

  /**
   * return the next color in the spectrum
   *
   * @param brightness
   */
  public final native Color getColor(double brightness) /*-{
    return this.getColor(brightness);
  }-*/;

  public final native Font getFont(String family) /*-{
    return this.getFont(family);
  }-*/;

  public final native Font getFont(String family, String weight) /*-{
    return this.getFont(family, weight);
  }-*/;

  public final native Font getFont(String family, String weight, String style) /*-{
    return this.getFont(family, weight, style);
  }-*/;

  public final native Font getFont(String family, String weight, String style, String stretch) /*-{
    return this.getFont(family, weight, style, stretch);
  }-*/;

  /**
   * parse color string and return a Color object
   */
  public final native Color getRGB(String color) /*-{
    return this.getRGB(color);
  }-*/;

  /**
   * Draw an image to the Raphael canvas
   *
   * @param src    string URI of the source image
   * @param x      number x coordinate position
   * @param y      number y coordinate position
   * @param width  number width of the image
   * @param height number height of the image
   *
   * @return       the image object
   */
  public final native Element image(String src, double x, double y, double width, double height) /*-{
    return this.image(src, x, y, width, height);
  }-*/;

  /**
   * Draws a path to the Raphael canvas
   *
   * @param obj    string Attributes for the resulting path as described in the attr reference.
   *
   * @return       the path object
   */
  public final native Path path() /*-{
    return this.path();
  }-*/;

  /**
   * Draws a path to the Raphael canvas given the SVG path string
   *
   * @param pathString   string Path data in SVG path string format.
   *
   * @return             the path object
   */
  public final native Path path(String pathString) /*-{
    return this.path(pathString);
  }-*/;

  public final native Font print(double x, double y, String text, Font font, double font_size) /*-{
    return this.print(x, y, text, font, font-size);
  }-*/;

  /**
   * Draw a rectangle to the Raphael canvas
   *
   * @param x        number x coordinate of top left corner
   * @param y        number y coordinate of top left corner
   * @param width    width of the rectangle
   * @param height   height of the rectangle
   *
   * @return         the rectangle object
   */
  public final native Element rect(double x, double y, double width, double height) /*-{
    return this.rect(x, y, width, height);
  }-*/;

  /**
   * Draw a rectangle (with rounded corners) to the Raphael canvas
   *
   * @param x        number x coordinate of top left corner
   * @param y        number y coordinate of top left corner
   * @param width    width of the rectangle
   * @param height   height of the rectangle
   * @param r        r number [optional] radius for rounded corners, default is 0
   *
   * @return         the rectangle object with rounded corners
   */
  public final native Element rect(double x, double y, double width, double height, double r) /*-{
    return this.rect(x, y, width, height, r);
  }-*/;

  public final native Font registerFont(JavaScriptObject font) /*-{
    return this.registerFont(font);
  }-*/;

  /**
   * creates an object to hold and operate on multiple Elements
   */
  public final native Set set() /*-{
    return this.set();
  }-*/;

  /**
   * change the dimensions of the canvas
   */
  public final native Element setSize(int width, int height) /*-{
    return this.setSize(width, height);
  }-*/;

  public final native Element text(double x, double y, String str) /*-{
    return this.text(x, y, str);
  }-*/;

}

