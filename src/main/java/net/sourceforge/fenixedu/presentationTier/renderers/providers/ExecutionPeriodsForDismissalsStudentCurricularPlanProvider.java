package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.student.IStudentCurricularPlanBean;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;

import org.apache.commons.collections.comparators.ReverseComparator;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ExecutionPeriodsForDismissalsStudentCurricularPlanProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        final List<ExecutionSemester> result = new ArrayList<ExecutionSemester>();
        final StudentCurricularPlan studentCurricularPlan = ((IStudentCurricularPlanBean) source).getStudentCurricularPlan();
        ExecutionSemester scpSemester = studentCurricularPlan.getStartExecutionPeriod();
        while (!scpSemester.isCurrent()) {
            result.add(scpSemester);
            scpSemester = scpSemester.getNextExecutionPeriod();
        }
        result.add(scpSemester); //add the current semester
        Collections.sort(result, new ReverseComparator());
        return result;
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
