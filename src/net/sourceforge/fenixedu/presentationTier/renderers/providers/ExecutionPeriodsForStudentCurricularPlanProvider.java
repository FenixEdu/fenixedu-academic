/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.student.IStudentCurricularPlanBean;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;

import org.apache.commons.collections.comparators.ReverseComparator;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * @author - Ângela Almeida (argelina@ist.utl.pt)
 * 
 */

public class ExecutionPeriodsForStudentCurricularPlanProvider implements DataProvider {

	@Override
	public Object provide(Object source, Object currentValue) {
		// final List<ExecutionSemester> result = new
		// ArrayList<ExecutionSemester>();
		final StudentCurricularPlan studentCurricularPlan = ((IStudentCurricularPlanBean) source).getStudentCurricularPlan();

		final List<ExecutionSemester> executionPeriodsInTimePeriod =
				ExecutionSemester.readExecutionPeriodsInTimePeriod(studentCurricularPlan.getStartDate(), getEndDate());

		/*
		 * 26/08/2009 - For curriculum validation we want execution semesters
		 * since student curricular plan start date
		 * 
		 * Without the code below we'll get all execution semesters since the
		 * start date
		 */
		// final ExecutionSemester first =
		// studentCurricularPlan.getDegreeCurricularPlan().getFirstExecutionPeriodEnrolments();
		// for (final ExecutionSemester executionSemester :
		// executionPeriodsInTimePeriod) {
		// if (first.isBeforeOrEquals(executionSemester) &&
		// !executionSemester.isNotOpen()) {
		// result.add(executionSemester);
		// }
		// }

		/*
		 * 26/08/2009 - We sort and return executionPeriodsInTimePeriod instead
		 * of result
		 */

		Collections.sort(executionPeriodsInTimePeriod, new ReverseComparator(ExecutionSemester.COMPARATOR_BY_SEMESTER_AND_YEAR));

		return executionPeriodsInTimePeriod;
	}

	private Date getEndDate() {
		return ExecutionYear.readCurrentExecutionYear().getLastExecutionPeriod().getEndDate();
	}

	@Override
	public Converter getConverter() {
		return new DomainObjectKeyConverter();
	}

}
