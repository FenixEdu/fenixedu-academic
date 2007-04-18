package net.sourceforge.fenixedu.domain.accounting.serviceAgreements;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.PaymentPlan;
import net.sourceforge.fenixedu.domain.accounting.paymentPlans.GratuityPaymentPlan;
import net.sourceforge.fenixedu.domain.accounting.serviceAgreementTemplates.DegreeCurricularPlanServiceAgreementTemplate;

public class DegreeCurricularPlanServiceAgreement extends DegreeCurricularPlanServiceAgreement_Base {

    private DegreeCurricularPlanServiceAgreement() {
	super();
    }

    public DegreeCurricularPlanServiceAgreement(Person person,
	    DegreeCurricularPlanServiceAgreementTemplate degreeCurricularPlanServiceAgreementTemplate) {
	this();
	super.init(person, degreeCurricularPlanServiceAgreementTemplate);
    }

    @Override
    public DegreeCurricularPlanServiceAgreementTemplate getServiceAgreementTemplate() {
	return (DegreeCurricularPlanServiceAgreementTemplate) super.getServiceAgreementTemplate();
    }

    public GratuityPaymentPlan getGratuityPaymentPlanFor(
	    final StudentCurricularPlan studentCurricularPlan, final ExecutionYear executionYear) {
	return getServiceAgreementTemplate().getGratuityPaymentPlanFor(studentCurricularPlan,
		executionYear);
    }

    public List<GratuityPaymentPlan> getGratuityPaymentPlans() {
	final List<GratuityPaymentPlan> result = new ArrayList<GratuityPaymentPlan>();
	for (final PaymentPlan paymentPlan : getServiceAgreementTemplate().getPaymentPlansSet()) {
	    if (paymentPlan instanceof GratuityPaymentPlan) {
		result.add((GratuityPaymentPlan) paymentPlan);
	    }
	}

	return result;
    }

}
