package net.sourceforge.fenixedu.presentationTier.renderers;

import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlInlineContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;

/**
 * This renderer interprets the value of String as a web color (like #ff4455")
 * and presents the value and a demonstration of what the color looks like.
 * 
 * @author cfgi
 */
public class WebcolorRenderer extends OutputRenderer {

    @Override
    protected Layout getLayout(Object object, Class type) {
	return new Layout() {

	    @Override
	    public HtmlComponent createComponent(Object object, Class type) {
		String value = (String) object;

		if (value == null) {
		    return new HtmlText();
		}

		HtmlInlineContainer container = new HtmlInlineContainer();

		HtmlText color = new HtmlText("&nbsp;", false);
		color.setStyle(String.format("border: 1px solid %1$s; background-color: %1$s; padding: 0px 7px;", value));

		container.addChild(new HtmlText(value));
		container.addChild(color);

		return container;
	    }

	};
    }

}
