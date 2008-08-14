package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.Date;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.renderers.DateRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;

/**
 * Renders a DateTime as a simple Date. This renderer convers the incoming
 * DateTime into a Date and then presents the Date in a standard way.
 * 
 * @author cfgi
 */
public class DateTimeAsDateRenderer extends DateRenderer {

    @Override
    protected Layout getLayout(Object object, Class type) {
	Date date = object == null ? null : ((DateTime) object).toDate();

	return super.getLayout(date, Date.class);
    }

    @Override
    protected HtmlComponent renderComponent(Layout layout, Object object, Class type) {
	Date date = object == null ? null : ((DateTime) object).toDate();

	return super.renderComponent(layout, date, Date.class);
    }

}
