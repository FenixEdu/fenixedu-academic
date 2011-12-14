package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.presentationTier.Action.student.enrollment.SpecialSeasonStudentEnrollmentBean;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ExecutionSemestersForSpecialSeasonProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	
	final ExecutionYear currentYear = ExecutionYear.readCurrentExecutionYear();
	final ExecutionYear previousYear = currentYear.getPreviousExecutionYear();
	final List<ExecutionSemester> executionSemesters = new ArrayList<ExecutionSemester>();
	
	SpecialSeasonStudentEnrollmentBean bean = (SpecialSeasonStudentEnrollmentBean) source;
	if(bean.getScp().getDegreeCurricularPlan().hasOpenSpecialSeasonEnrolmentPeriod(currentYear.getLastExecutionPeriod())) {
	    executionSemesters.add(currentYear.getLastExecutionPeriod());
	}
	if(bean.getScp().getDegreeCurricularPlan().hasOpenSpecialSeasonEnrolmentPeriod(currentYear.getFirstExecutionPeriod())) {
	    executionSemesters.add(currentYear.getFirstExecutionPeriod());
	}
	if(bean.getScp().getDegreeCurricularPlan().hasOpenSpecialSeasonEnrolmentPeriod(previousYear.getLastExecutionPeriod())) {
	    executionSemesters.add(previousYear.getLastExecutionPeriod());
	}
	if(bean.getScp().getDegreeCurricularPlan().hasOpenSpecialSeasonEnrolmentPeriod(previousYear.getFirstExecutionPeriod())) {
	    executionSemesters.add(previousYear.getFirstExecutionPeriod());
	}
	
	Collections.sort(executionSemesters, ExecutionSemester.COMPARATOR_BY_SEMESTER_AND_YEAR);
	Collections.reverse(executionSemesters);
	return executionSemesters;
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
