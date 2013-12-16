package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.log.requests.RequestLogYear;
import pt.ist.bennu.core.domain.Bennu;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class RequestLogYearProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        ArrayList<RequestLogYear> years = new ArrayList<RequestLogYear>();
        for (RequestLogYear requestLogYear : Bennu.getInstance().getRequestLogYearsSet()) {
            years.add(requestLogYear);
        }
        Collections.sort(years, new Comparator<RequestLogYear>() {
            @Override
            public int compare(RequestLogYear arg0, RequestLogYear arg1) {
                return arg0.getYear().compareTo(arg1.getYear());
            }

        });
        return years;
    }

    @Override
    public Converter getConverter() {
        // TODO Auto-generated method stub
        return null;
    }

}
