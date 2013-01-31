package net.sourceforge.fenixedu.domain.accounting.paymentPlans;

import java.util.Collection;
import java.util.Collections;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.paymentPlanRules.HasEnrolmentsForExecutionSemesterPaymentPlanRule;
import net.sourceforge.fenixedu.domain.accounting.paymentPlanRules.PaymentPlanRule;
import net.sourceforge.fenixedu.domain.accounting.paymentPlanRules.PaymentPlanRuleFactory;
import net.sourceforge.fenixedu.domain.accounting.serviceAgreementTemplates.DegreeCurricularPlanServiceAgreementTemplate;

public class FullGratuityPaymentPlan extends FullGratuityPaymentPlan_Base {

	protected FullGratuityPaymentPlan() {
		super();
	}

	public FullGratuityPaymentPlan(final ExecutionYear executionYear,
			final DegreeCurricularPlanServiceAgreementTemplate serviceAgreementTemplate, final Boolean defaultPlan) {
		this();
		super.init(executionYear, serviceAgreementTemplate, defaultPlan);
	}

	public FullGratuityPaymentPlan(final ExecutionYear executionYear,
			final DegreeCurricularPlanServiceAgreementTemplate serviceAgreementTemplate) {
		this(executionYear, serviceAgreementTemplate, false);
	}

	@Override
	protected Collection<PaymentPlanRule> getSpecificPaymentPlanRules() {
		return Collections.singleton(PaymentPlanRuleFactory.create(HasEnrolmentsForExecutionSemesterPaymentPlanRule.class));
	}

}
