package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;

public class CreateAdministrativeOfficeFeeAndInsuranceEvent extends Service {

    public CreateAdministrativeOfficeFeeAndInsuranceEvent() {
	super();
    }

    public void run(final StudentCurricularPlan studentCurricularPlan, final ExecutionYear executionYear) {
	studentCurricularPlan.getRegistration().getStudent().createAdministrativeOfficeFeeEvent(studentCurricularPlan,
		executionYear);
    }

}