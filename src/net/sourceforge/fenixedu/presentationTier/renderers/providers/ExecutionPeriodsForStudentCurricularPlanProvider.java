/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.student.IStudentCurricularPlanBean;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * @author - Ângela Almeida (argelina@ist.utl.pt)
 * 
 */

public class ExecutionPeriodsForStudentCurricularPlanProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	final List<ExecutionPeriod> result = new ArrayList<ExecutionPeriod>();
	final StudentCurricularPlan studentCurricularPlan = ((IStudentCurricularPlanBean) source).getStudentCurricularPlan();

	final List<ExecutionPeriod> executionPeriodsInTimePeriod = ExecutionPeriod.readExecutionPeriodsInTimePeriod(
		studentCurricularPlan.getStartDate(), Calendar.getInstance().getTime());

	final ExecutionPeriod first = studentCurricularPlan.getDegreeCurricularPlan().getFirstExecutionPeriodEnrolments();
	for (final ExecutionPeriod executionPeriod : executionPeriodsInTimePeriod) {
	    if (first.isBeforeOrEquals(executionPeriod)) {
		result.add(executionPeriod);
	    }
	}

	return result;
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
