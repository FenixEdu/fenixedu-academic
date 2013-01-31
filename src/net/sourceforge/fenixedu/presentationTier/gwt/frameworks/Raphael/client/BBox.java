package net.sourceforge.fenixedu.presentationTier.gwt.frameworks.Raphael.client;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * bounding box object returned by Element.getBBox()
 */
public class BBox extends JavaScriptObject {

	protected BBox() {
	}

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
