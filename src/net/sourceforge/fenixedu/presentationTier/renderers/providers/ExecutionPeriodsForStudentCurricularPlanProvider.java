/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Calendar;

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
	return ExecutionPeriod.readExecutionPeriodsInTimePeriod(studentCurricularPlan.getStartDate(),
		Calendar.getInstance().getTime());
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
