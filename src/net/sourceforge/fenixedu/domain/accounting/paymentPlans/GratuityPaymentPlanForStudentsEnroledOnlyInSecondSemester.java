package net.sourceforge.fenixedu.domain.accounting.paymentPlans;

import java.util.Collection;
import java.util.Collections;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.paymentPlanRules.HasEnrolmentsOnlyInSecondSemesterPaymentPlanRule;
import net.sourceforge.fenixedu.domain.accounting.paymentPlanRules.PaymentPlanRule;
import net.sourceforge.fenixedu.domain.accounting.paymentPlanRules.PaymentPlanRuleFactory;
import net.sourceforge.fenixedu.domain.accounting.serviceAgreementTemplates.DegreeCurricularPlanServiceAgreementTemplate;

public class GratuityPaymentPlanForStudentsEnroledOnlyInSecondSemester extends
		GratuityPaymentPlanForStudentsEnroledOnlyInSecondSemester_Base {

	protected GratuityPaymentPlanForStudentsEnroledOnlyInSecondSemester() {
		super();
	}

	public GratuityPaymentPlanForStudentsEnroledOnlyInSecondSemester(final ExecutionYear executionYear,
			final DegreeCurricularPlanServiceAgreementTemplate serviceAgreementTemplate) {
		this(executionYear, serviceAgreementTemplate, false);
	}

	public GratuityPaymentPlanForStudentsEnroledOnlyInSecondSemester(final ExecutionYear executionYear,
			final DegreeCurricularPlanServiceAgreementTemplate serviceAgreementTemplate, final Boolean defaultPlan) {
		this();
		super.init(executionYear, serviceAgreementTemplate, defaultPlan);
	}

	@Override
	protected Collection<PaymentPlanRule> getSpecificPaymentPlanRules() {
		return Collections.singletonList(PaymentPlanRuleFactory.create(HasEnrolmentsOnlyInSecondSemesterPaymentPlanRule.class));
	}

}
