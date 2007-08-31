/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment.StudentEnrolmentBean;
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
	final StudentCurricularPlan studentCurricularPlan = ((StudentEnrolmentBean) source)
		.getStudentCurricularPlan();
	List<ExecutionPeriod> executionPeriodsInTimePeriod = ExecutionPeriod.readExecutionPeriodsInTimePeriod(
		studentCurricularPlan.getStartDate(), Calendar.getInstance().getTime());
	
	ExecutionPeriod firstExecutionPeriodEnrolments = studentCurricularPlan.getDegreeCurricularPlan().getFirstExecutionPeriodEnrolments();
	List<ExecutionPeriod> result = new ArrayList<ExecutionPeriod>();
	for (ExecutionPeriod executionPeriod : executionPeriodsInTimePeriod) {
	    if(firstExecutionPeriodEnrolments.isBeforeOrEquals(executionPeriod)) {
		result.add(executionPeriod);
	    }
	}
	return result;
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
