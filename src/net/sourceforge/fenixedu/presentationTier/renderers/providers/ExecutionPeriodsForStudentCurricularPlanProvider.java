/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.student.IStudentCurricularPlanBean;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;

import org.apache.commons.collections.comparators.ReverseComparator;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * @author - Ângela Almeida (argelina@ist.utl.pt)
 * 
 */

public class ExecutionPeriodsForStudentCurricularPlanProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	final List<ExecutionSemester> result = new ArrayList<ExecutionSemester>();
	final StudentCurricularPlan studentCurricularPlan = ((IStudentCurricularPlanBean) source).getStudentCurricularPlan();

	final List<ExecutionSemester> executionPeriodsInTimePeriod = ExecutionSemester.readExecutionPeriodsInTimePeriod(
		studentCurricularPlan.getStartDate(), getEndDate());

	final ExecutionSemester first = studentCurricularPlan.getDegreeCurricularPlan().getFirstExecutionPeriodEnrolments();
	for (final ExecutionSemester executionSemester : executionPeriodsInTimePeriod) {
	    if (first.isBeforeOrEquals(executionSemester) && !executionSemester.isNotOpen()) {
		result.add(executionSemester);
	    }
	}

	Collections.sort(result, new ReverseComparator(ExecutionSemester.EXECUTION_PERIOD_COMPARATOR_BY_SEMESTER_AND_YEAR));

	return result;
    }

    private Date getEndDate() {
	return ExecutionYear.readCurrentExecutionYear().getLastExecutionPeriod().getEndDate();
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
