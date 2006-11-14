package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.executionCourse.ExecutionCourseSearchBean;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class ExecutionDegreeProviderForExecutionCourseSearchBean implements DataProvider {

    public Object provide(Object source, Object currentValue) {
        final ExecutionCourseSearchBean executionCourseSearchBean = (ExecutionCourseSearchBean) source;
        final ExecutionPeriod executionPeriod = executionCourseSearchBean.getExecutionPeriod();
        final ExecutionYear executionYear = executionPeriod.getExecutionYear();
        final List<ExecutionDegree> executionDegrees = new ArrayList<ExecutionDegree>(executionYear.getExecutionDegreesSet());
        Collections.sort(executionDegrees, ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME);
        return executionDegrees; 
    }

    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
