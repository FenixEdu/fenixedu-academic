package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.log.requests.RequestLogMonth;
import net.sourceforge.fenixedu.presentationTier.Action.manager.ErrorLogDispatchAction.RequestLogDayBean;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class RequestLogMonthProvider implements DataProvider {

    @Override
    public Converter getConverter() {
        return null;
    }

    @Override
    public Object provide(Object source, Object currentValue) {
        ArrayList<RequestLogMonth> months = new ArrayList<RequestLogMonth>();
        if (((RequestLogDayBean) source).getYear() != null) {
            for (RequestLogMonth requestLogMonth : ((RequestLogDayBean) source).getYear().getMonths()) {
                months.add(requestLogMonth);
            }
            Collections.sort(months, new Comparator<RequestLogMonth>() {
                @Override
                public int compare(RequestLogMonth arg0, RequestLogMonth arg1) {
                    return arg0.getMonthOfYear().compareTo(arg1.getMonthOfYear());
                }

            });
            return months;
        } else {
            return new ArrayList<RequestLogMonth>();
        }
    }

}
