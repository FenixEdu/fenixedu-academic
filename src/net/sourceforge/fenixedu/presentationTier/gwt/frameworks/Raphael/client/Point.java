package net.sourceforge.fenixedu.presentationTier.gwt.frameworks.Raphael.client;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * point object returned by Path.getPointAtLength()
 */
public class Point extends JavaScriptObject {

	protected Point() {
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

	public final native double alpha() /*-{
										if (this.alpha == undefined)
										return -1;
										else
										return this.alpha;
										}-*/;

}
