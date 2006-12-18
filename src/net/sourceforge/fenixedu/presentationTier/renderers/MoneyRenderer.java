package net.sourceforge.fenixedu.presentationTier.renderers;

import java.text.DecimalFormat;

import net.sourceforge.fenixedu.renderers.OutputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.util.Money;

/**
 * This renderer provides a generic presentation for a money number. The
 * number is formatted according to the format property
 * 
 * @author naat
 * @author jdnf
 */
public class MoneyRenderer extends OutputRenderer {

    private String format;

    private static final String DEFAULT_FORMAT = "######0.00";

    public MoneyRenderer() {
	setFormat(DEFAULT_FORMAT);
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
	return new Layout() {

	    @Override
	    public HtmlComponent createComponent(Object object, Class type) {
		if (object != null) {
		    final Number number = (Number) ((Money) object).getAmount();
		    return new HtmlText(new DecimalFormat(getFormat()).format(number));
		} else {
		    return new HtmlText("");
		}
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
