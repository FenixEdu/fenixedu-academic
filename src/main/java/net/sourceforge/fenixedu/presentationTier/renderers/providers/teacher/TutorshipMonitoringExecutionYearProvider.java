package net.sourceforge.fenixedu.presentationTier.renderers.providers.teacher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.StudentsPerformanceInfoBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;

import org.apache.commons.collections.comparators.ReverseComparator;

import pt.ist.bennu.core.domain.Bennu;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class TutorshipMonitoringExecutionYearProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        StudentsPerformanceInfoBean bean = (StudentsPerformanceInfoBean) source;
        return getExecutionYears(bean);
    }

    public static List<ExecutionYear> getExecutionYears(StudentsPerformanceInfoBean bean) {
        List<ExecutionYear> executionYears = new ArrayList<ExecutionYear>();
        for (ExecutionYear year : Bennu.getInstance().getExecutionYearsSet()) {
            if (year.isAfterOrEquals(bean.getStudentsEntryYear())) {
                executionYears.add(year);
            }
        }
        Collections.sort(executionYears, new ReverseComparator());
        return executionYears;
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
