package net.sourceforge.fenixedu.domain.accounting.paymentPlans;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.serviceAgreementTemplates.DegreeCurricularPlanServiceAgreementTemplate;

public class FullGratuityPaymentPlanForPartialRegime extends FullGratuityPaymentPlanForPartialRegime_Base {

    protected FullGratuityPaymentPlanForPartialRegime() {
	super();
    }

    public FullGratuityPaymentPlanForPartialRegime(final ExecutionYear executionYear,
	    final DegreeCurricularPlanServiceAgreementTemplate serviceAgreementTemplate, final Boolean defaultPaymentPlan) {
	this();
	init(executionYear, serviceAgreementTemplate, defaultPaymentPlan);

    }

    @Override
    public boolean isAppliableFor(StudentCurricularPlan studentCurricularPlan, ExecutionYear executionYear) {
	return getExecutionYear() == executionYear && studentCurricularPlan.getRegistration().isPartialRegime(executionYear)
		&& studentCurricularPlan.hasAnyEnrolmentForExecutionPeriod(executionYear.getFirstExecutionPeriod());
    }

    @Override
    public boolean isForPartialRegime() {
	return true;
    }

}
