package net.sourceforge.fenixedu.domain.accounting.paymentPlans;

import java.util.Collection;
import java.util.Collections;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.paymentPlanRules.FirstTimeInstitutionStudentsPaymentPlanRule;
import net.sourceforge.fenixedu.domain.accounting.paymentPlanRules.PaymentPlanRule;
import net.sourceforge.fenixedu.domain.accounting.paymentPlanRules.PaymentPlanRuleFactory;
import net.sourceforge.fenixedu.domain.accounting.serviceAgreementTemplates.DegreeCurricularPlanServiceAgreementTemplate;

public class FullGratuityPaymentPlanForFirstTimeInstitutionStudents extends
	FullGratuityPaymentPlanForFirstTimeInstitutionStudents_Base {

    private FullGratuityPaymentPlanForFirstTimeInstitutionStudents() {
	super();
    }

    public FullGratuityPaymentPlanForFirstTimeInstitutionStudents(final ExecutionYear executionYear,
	    final DegreeCurricularPlanServiceAgreementTemplate serviceAgreementTemplate) {
	this();
	super.init(executionYear, serviceAgreementTemplate, false);
    }

    @Override
    protected Collection<PaymentPlanRule> getSpecificPaymentPlanRules() {
	return Collections.singleton(PaymentPlanRuleFactory.create(FirstTimeInstitutionStudentsPaymentPlanRule.class));
    }

}
