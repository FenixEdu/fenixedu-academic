package net.sourceforge.fenixedu.domain.accounting.paymentPlans;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;

public class FullGratuityPaymentPlan extends FullGratuityPaymentPlan_Base {

    protected FullGratuityPaymentPlan() {
	super();
    }

    public FullGratuityPaymentPlan(final ExecutionYear executionYear,
	    final ServiceAgreementTemplate serviceAgreementTemplate, final Boolean defaultPlan) {
	this();
	super.init(executionYear, serviceAgreementTemplate, defaultPlan);
    }

    public FullGratuityPaymentPlan(final ExecutionYear executionYear,
	    final ServiceAgreementTemplate serviceAgreementTemplate) {
	this(executionYear, serviceAgreementTemplate, false);
    }

    @Override
    public boolean isAppliableFor(StudentCurricularPlan studentCurricularPlan,
	    ExecutionYear executionYear) {
	return getExecutionYear() == executionYear
		&& studentCurricularPlan.hasAnyEnrolmentForExecutionPeriod(executionYear
			.getFirstExecutionPeriod());
    }
}
