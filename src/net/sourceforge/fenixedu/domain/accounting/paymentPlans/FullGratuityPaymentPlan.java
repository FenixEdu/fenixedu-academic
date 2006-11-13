package net.sourceforge.fenixedu.domain.accounting.paymentPlans;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;

public class FullGratuityPaymentPlan extends FullGratuityPaymentPlan_Base {

    protected FullGratuityPaymentPlan() {
	super();
    }

    public FullGratuityPaymentPlan(final ExecutionYear executionYear,
	    final ServiceAgreementTemplate serviceAgreementTemplate) {
	this();
	super.init(executionYear, serviceAgreementTemplate);
    }

    @Override
    public boolean isAppliableFor(StudentCurricularPlan studentCurricularPlan, ExecutionYear executionYear) {
	return getExecutionYear() == executionYear
		&& studentCurricularPlan.hasAnyEnrolmentForExecutionPeriod(executionYear
			.getFirstExecutionPeriod());
    }
}
