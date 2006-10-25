package net.sourceforge.fenixedu.renderers;

import java.text.DecimalFormat;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;

/**
 * This renderer provides a generic presentation for a decimal number. The
 * number is formatted according to the format property
 * 
 * @author naat
 * @author jdnf
 */
public class DecimalRenderer extends OutputRenderer {

    private String format;

    private static final String DEFAULT_FORMAT = "######0.00";

    public DecimalRenderer() {
	setFormat(DEFAULT_FORMAT);
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
	return new Layout() {

	    @Override
	    public HtmlComponent createComponent(Object object, Class type) {
		return new HtmlText(new DecimalFormat(getFormat()).format((Number) object));
	    }

	};
    }

    public String getFormat() {
	return format;
    }

    public void setFormat(String format) {
	this.format = format;
    }

}
