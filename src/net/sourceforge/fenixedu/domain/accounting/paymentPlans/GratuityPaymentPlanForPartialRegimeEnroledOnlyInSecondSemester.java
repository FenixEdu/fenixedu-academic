package net.sourceforge.fenixedu.domain.accounting.paymentPlans;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.serviceAgreementTemplates.DegreeCurricularPlanServiceAgreementTemplate;

public class GratuityPaymentPlanForPartialRegimeEnroledOnlyInSecondSemester extends
	GratuityPaymentPlanForPartialRegimeEnroledOnlyInSecondSemester_Base {

    protected GratuityPaymentPlanForPartialRegimeEnroledOnlyInSecondSemester() {
	super();
    }

    public GratuityPaymentPlanForPartialRegimeEnroledOnlyInSecondSemester(final ExecutionYear executionYear,
	    final DegreeCurricularPlanServiceAgreementTemplate serviceAgreementTemplate, final Boolean defaultPaymentPlan) {
	this();
	init(executionYear, serviceAgreementTemplate, defaultPaymentPlan);
    }

    @Override
    public boolean isAppliableFor(StudentCurricularPlan studentCurricularPlan, ExecutionYear executionYear) {
	return getExecutionYear() == executionYear
		&& studentCurricularPlan.getRegistration().isPartialRegime(executionYear)
		&& !studentCurricularPlan.hasAnyEnrolmentForExecutionPeriod(executionYear.getFirstExecutionPeriod())
		&& studentCurricularPlan.hasAnyEnrolmentForExecutionPeriod(executionYear.getFirstExecutionPeriod()
			.getNextExecutionPeriod());
    }

    @Override
    public boolean isForPartialRegime() {
	return true;
    }
}
