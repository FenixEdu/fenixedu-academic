package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;

public class CreateEnrolmentOutOfPeriodEvent extends Service {

    public CreateEnrolmentOutOfPeriodEvent() {
	super();
    }

    public void run(final StudentCurricularPlan studentCurricularPlan, final ExecutionPeriod executionPeriod,
	    final Integer numberOfDelayDays) {
	studentCurricularPlan.getRegistration().getStudent().createEnrolmentOutOfPeriodEvent(studentCurricularPlan,
		executionPeriod, numberOfDelayDays);
    }

}