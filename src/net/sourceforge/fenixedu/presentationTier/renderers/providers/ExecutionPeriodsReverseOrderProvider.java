package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class ExecutionPeriodsReverseOrderProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
        final List<ExecutionSemester> executionSemesters = new ArrayList<ExecutionSemester>(RootDomainObject.getInstance().getExecutionPeriodsSet());
        Collections.sort(executionSemesters, ExecutionSemester.EXECUTION_PERIOD_COMPARATOR_BY_SEMESTER_AND_YEAR);
        Collections.reverse(executionSemesters);
        return executionSemesters; 
    }

    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
