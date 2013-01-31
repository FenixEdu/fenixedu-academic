package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.log.requests.RequestLogDay;
import net.sourceforge.fenixedu.presentationTier.Action.manager.ErrorLogDispatchAction.RequestLogDayBean;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class RequestLogDayProvider implements DataProvider {

	@Override
	public Converter getConverter() {
		return null;
	}

	@Override
	public Object provide(Object source, Object currentValue) {
		ArrayList<RequestLogDay> days = new ArrayList<RequestLogDay>();
		if (((RequestLogDayBean) source).getMonth() != null) {
			for (RequestLogDay requestLogDay : ((RequestLogDayBean) source).getMonth().getDays()) {
				days.add(requestLogDay);
			}
			Collections.sort(days, new Comparator<RequestLogDay>() {
				@Override
				public int compare(RequestLogDay arg0, RequestLogDay arg1) {
					return arg0.getDayOfMonth().compareTo(arg1.getDayOfMonth());
				}

			});
			return days;
		} else {
			return new ArrayList<RequestLogDay>();
		}
	}

}
