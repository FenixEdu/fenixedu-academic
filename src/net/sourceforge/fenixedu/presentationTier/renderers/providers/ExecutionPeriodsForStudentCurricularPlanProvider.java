/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.student.IStudentCurricularPlanBean;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
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
		studentCurricularPlan.getStartDate(), Calendar.getInstance().getTime());

	final ExecutionSemester first = studentCurricularPlan.getDegreeCurricularPlan().getFirstExecutionPeriodEnrolments();
	for (final ExecutionSemester executionSemester : executionPeriodsInTimePeriod) {
	    if (first.isBeforeOrEquals(executionSemester)) {
		result.add(executionSemester);
	    }
	}

	return result;
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
