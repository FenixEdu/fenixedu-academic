package net.sourceforge.fenixedu.presentationTier.gwt.frameworks.Raphael.client;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;
import java.util.ArrayList;

public class Raphael extends Widget {

  private RaphaelJS overlay;
  private final ArrayList<Shape> shapes = new ArrayList<Shape>();

  public Raphael(int width, int height) {
    super();
    Element raphaelDiv = DOM.createDiv();
    setElement(raphaelDiv);
    overlay = RaphaelJS.create(raphaelDiv, width, height);
  }

  public static boolean isSupported() {
    return RaphaelJS.isDefined();
  }

  public void clear() {
    overlay.clear();
  }

  /**
   * detach our children explicitly here since they were
   * attached via JavaScript outside of GWT framework
   */
  @Override
  protected void doDetachChildren() {
    super.doDetachChildren();
    for (Shape s : shapes)
      s.doDetach();
  }

  public class Set {
    protected RaphaelJS.Set set;
    private final ArrayList<Shape> sh = new ArrayList<Shape>();
    public Set() {
      this.set = overlay.set();
    }
    public Set push(Shape s) {
      set.push(s.el);
      sh.add(s);
      return this;
    }
    public Set animate(JSONObject newAttrs, int duration) {
      set.animate(newAttrs.getJavaScriptObject(), duration);
      return this;
    }
    public Set animate(JSONObject newAttrs, int duration, AnimationCallback callback) {
      set.animate(newAttrs.getJavaScriptObject(), duration, callback);
      return this;
    }
    public Set animate(JSONObject newAttrs, int duration, String easing) {
      set.animate(newAttrs.getJavaScriptObject(), duration, easing);
      return this;
    }
    public Set animate(JSONObject newAttrs, int duration, String easing, AnimationCallback callback) {
      set.animate(newAttrs.getJavaScriptObject(), duration, easing, callback);
      return this;
    }
    public Set attr(String attributeName, String value) {
      set.attr(attributeName, value);
      return this;
    }
    public Set attr(String attributeName, double value) {
      set.attr(attributeName, value);
      return this;
    }
    public Set attr(JSONObject params) {
      set.attr(params.getJavaScriptObject());
      return this;
    }
    public double attrAsDouble(String name) {
      return set.attrAsDouble(name);
    }
    public String attrAsString(String name) {
      return set.attrAsString(name);
    }
    public JSONObject attr(JSONArray attributeNames) {
      return new JSONObject(set.attr(attributeNames.getJavaScriptObject()));
    }
    public BBox getBBox() {
      return set.getBBox();
    }
    public void remove() {
      set.remove();
    }
    public Set rotate(double degree) {
      set.rotate(degree);
      return this;
    }
    public Set rotate(double degree, boolean isAbsolute) {
      set.rotate(degree, isAbsolute);
      return this;
    }
    public Set rotate(double degree, double cx, double cy) {
      set.rotate(degree, cx, cy);
      return this;
    }
    public Set rotate(double degree, double cx, double cy, boolean isAbsolute) {
      for (Shape s : sh)
        s.rotate(degree, cx, cy, isAbsolute);
      return this;
    }
    public Set scale(double sx, double sy) {
      set.scale(sx, sy);
      return this;
    }
    public Set scale(double sx, double sy, double cx, double cy) {
      set.scale(sx, sy, cx, cy);
      return this;
    }
    public Set toFront() {
      set.toFront();
      return this;
    }
    public Set toBack() {
      set.toBack();
      return this;
    }
    public Set translate(double dx, double dy) {
      set.translate(dx, dy);
      return this;
    }
  }

  public class Shape extends Widget {
    protected RaphaelJS.Element el;
    protected double rot = 0;
    protected Shape(RaphaelJS.Element obj) {
      super();
      setElement(obj.node());
      shapes.add(this);
      el = obj;
      onAttach(); // signal that the widget has been attached
    }

    /**
     * this is ugly, but necessary for the parent Raphael
     * widget to detach this widget from the DOM, because
     * the onDetach() method is protected.
     */
    public void doDetach() {
      onDetach();
    }

    public Shape animate(JSONObject newAttrs, int duration) {
      el.animate(newAttrs.getJavaScriptObject(), duration);
      return this;
    }
    public Shape animate(JSONObject newAttrs, int duration, AnimationCallback callback) {
      el.animate(newAttrs.getJavaScriptObject(), duration, callback);
      return this;
    }
    public Shape animate(JSONObject newAttrs, int duration, String easing) {
      el.animate(newAttrs.getJavaScriptObject(), duration, easing);
      return this;
    }
    public Shape animate(JSONObject newAttrs, int duration, String easing, AnimationCallback callback) {
      el.animate(newAttrs.getJavaScriptObject(), duration, easing, callback);
      return this;
    }

    public Shape animateWith(Shape shape, JSONObject newAttrs, int duration) {
      el.animateWith(shape.el, newAttrs.getJavaScriptObject(), duration);
      return this;
    }
    public Shape animateWith(Shape shape, JSONObject newAttrs, int duration, AnimationCallback callback) {
      el.animateWith(shape.el, newAttrs.getJavaScriptObject(), duration, callback);
      return this;
    }
    public Shape animateWith(Shape shape, JSONObject newAttrs, int duration, String easing) {
      el.animateWith(shape.el, newAttrs.getJavaScriptObject(), duration, easing);
      return this;
    }
    public Shape animateWith(Shape shape, JSONObject newAttrs, int duration, String easing, AnimationCallback callback) {
      el.animateWith(shape.el, newAttrs.getJavaScriptObject(), duration, easing, callback);
      return this;
    }

    public Shape animateAlong(Path path, int duration) {
      el.animateAlong(path.el, duration);
      return this;
    }
    public Shape animateAlong(Path path, int duration, boolean rotate) {
      el.animateAlong(path.el, duration, rotate);
      return this;
    }
    public Shape animateAlong(Path path, int duration, boolean rotate, AnimationCallback callback) {
      el.animateAlong(path.el, duration, rotate, callback);
      return this;
    }

    public Shape animateAlongBack(Path path, int duration) {
      el.animateAlongBack(path.el, duration);
      return this;
    }
    public Shape animateAlongBack(Path path, int duration, boolean rotate) {
      el.animateAlongBack(path.el, duration, rotate);
      return this;
    }
    public Shape animateAlongBack(Path path, int duration, boolean rotate, AnimationCallback callback) {
      el.animateAlongBack(path.el, duration, rotate, callback);
      return this;
    }

    public Shape attr(String attributeName, String value) {
      el.attr(attributeName, value);
      return this;
    }
    public Shape attr(String attributeName, double value) {
      el.attr(attributeName, value);
      return this;
    }
    public Shape attr(JSONObject params) {
      el.attr(params.getJavaScriptObject());
      return this;
    }
    public double attrAsDouble(String name) {
      return el.attrAsDouble(name);
    }
    public String attrAsString(String name) {
      return el.attrAsString(name);
    }
    public JSONObject attr(JSONArray attributeNames) {
      return new JSONObject(el.attr(attributeNames.getJavaScriptObject()));
    }

    public BBox getBBox() {
      return el.getBBox();
    }

    public void hide() {
      el.hide();
    }

    public void remove() {
      el.remove();
    }

    public Shape rotate(double degree) {
      rot = degree;
      el.rotate(degree);
      return this;
    }
    public Shape rotate(double degree, boolean isAbsolute) {
      if (isAbsolute)
        rot = degree;
      else
        rot += degree;
      el.rotate(degree, isAbsolute);
      return this;
    }
    public Shape rotate(double degree, double cx, double cy) {
      rot = degree;
      el.rotate(degree, cx, cy);
      return this;
    }
    public Shape rotate(double degree, double cx, double cy, boolean isAbsolute) {
      if (isAbsolute)
        rot = degree;
      else
        rot += degree;
      el.rotate(rot, cx, cy);
      return this;
    }

    public Shape scale(double sx, double sy) {
      el.scale(sx, sy);
      return this;
    }
    public Shape scale(double sx, double sy, double cx, double cy) {
      el.scale(sx, sy, cx, cy);
      return this;
    }

    public void show() {
      el.show();
    }

    public Shape toFront() {
      el.toFront();
      return this;
    }

    public Shape toBack() {
      el.toBack();
      return this;
    }

    public Shape translate(double dx, double dy) {
      el.translate(dx, dy);
      return this;
    }

  }

  public class Circle extends Shape {
    public Circle(double x, double y, double r) {
      super(overlay.circle(x, y, r));
    }
  }

  public class Text extends Shape {
    public Text(double x, double y, String text) {
      super(overlay.text(x, y, text));
    }
  }

  public class Rect extends Shape {
    public Rect(double x, double y, double w, double h) {
      super(overlay.rect(x, y, w, h));
    }
    public Rect(double x, double y, double w, double h, double r) {
      super(overlay.rect(x, y, w, h, r));
    }
  }

  public class Ellipse extends Shape {
    public Ellipse(double x, double y, double rx, double ry) {
      super(overlay.ellipse(x, y, rx, ry));
    }
  }

  public class Image extends Shape {
    public Image(String src, double x, double y, double width, double height) {
      super(overlay.image(src, x, y, width, height));
    }
  }

  public class Path extends Shape {
    public Path() {
      super(overlay.path());
    }

    public Path(String pathString) {
      super(overlay.path(pathString));
    }

    public Path(PathBuilder builder) {
      this(builder.toString());
    }

    public int getTotalLength() {
      return ((RaphaelJS.Path)el).getTotalLength();
    }

    public Point getPointAtLength(int length) {
      return ((RaphaelJS.Path)el).getPointAtLength(length);
    }

    /**
     * doesn't seem to work
     *
    public String getSubpath(int from, int to) {
      return ((RaphaelJS.Path)el).getSubpath(from, to);
    }
     */
  }

}

