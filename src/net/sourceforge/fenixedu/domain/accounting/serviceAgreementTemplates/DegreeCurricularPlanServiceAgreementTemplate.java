package net.sourceforge.fenixedu.domain.accounting.serviceAgreementTemplates;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.PaymentPlan;
import net.sourceforge.fenixedu.domain.accounting.paymentPlans.FullGratuityPaymentPlan;
import net.sourceforge.fenixedu.domain.accounting.paymentPlans.GratuityPaymentPlan;
import net.sourceforge.fenixedu.domain.accounting.paymentPlans.GratuityPaymentPlanForStudentsEnroledOnlyInSecondSemester;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class DegreeCurricularPlanServiceAgreementTemplate extends
	DegreeCurricularPlanServiceAgreementTemplate_Base {

    private DegreeCurricularPlanServiceAgreementTemplate() {
	super();
    }

    public DegreeCurricularPlanServiceAgreementTemplate(DegreeCurricularPlan degreeCurricularPlan) {
	this();
	init(degreeCurricularPlan);
    }

    private void checkParameters(DegreeCurricularPlan degreeCurricularPlan) {
	if (degreeCurricularPlan == null) {
	    throw new DomainException(
		    "error.accounting.agreement.serviceAgreementTemplates.DegreeCurricularPlanServiceAgreementTemplate.degreeCurricularPlan.cannot.be.null");
	}

    }

    protected void init(DegreeCurricularPlan degreeCurricularPlan) {
	checkParameters(degreeCurricularPlan);
	super.setDegreeCurricularPlan(degreeCurricularPlan);
    }

    @Override
    public void setDegreeCurricularPlan(DegreeCurricularPlan degreeCurricularPlan) {
	throw new DomainException(
		"error.accounting.agreement.serviceAgreementTemplates.DegreeCurricularPlanServiceAgreementTemplate.cannot.modify.degreeCurricularPlan");
    }

    public GratuityPaymentPlan getGratuityPaymentPlanFor(
	    final StudentCurricularPlan studentCurricularPlan, final ExecutionYear executionYear) {
	for (final PaymentPlan paymentPlan : getPaymentPlansSet()) {
	    if (paymentPlan instanceof GratuityPaymentPlan
		    && ((GratuityPaymentPlan) paymentPlan).isAppliableFor(studentCurricularPlan,
			    executionYear)) {
		return (GratuityPaymentPlan) paymentPlan;
	    }
	}

	return null;
    }

    public boolean hasFullGratuityPaymentPlanFor(final ExecutionYear executionYear) {
	for (final PaymentPlan paymentPlan : getPaymentPlansSet()) {
	    if (paymentPlan instanceof FullGratuityPaymentPlan
		    && paymentPlan.getExecutionYear() == executionYear) {
		return true;
	    }
	}
	return false;
    }

    public boolean hasGratuityPaymentPlanForStudentsEnroledOnlyInSecondSemesterBy(
	    final ExecutionYear executionYear) {
	for (final PaymentPlan paymentPlan : getPaymentPlansSet()) {
	    if (paymentPlan instanceof GratuityPaymentPlanForStudentsEnroledOnlyInSecondSemester
		    && paymentPlan.getExecutionYear() == executionYear) {
		return true;
	    }
	}
	return false;
    }

}
