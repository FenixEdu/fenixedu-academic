package net.sourceforge.fenixedu.domain.accounting.paymentPlans;

import dml.runtime.RelationAdapter;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.PaymentPlan;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreement;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementPaymentPlan;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEventWithPaymentPlan;
import net.sourceforge.fenixedu.domain.accounting.serviceAgreements.DegreeCurricularPlanServiceAgreement;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class CustomGratuityPaymentPlan extends CustomGratuityPaymentPlan_Base {

    static {
	ServiceAgreementServiceAgreementPaymentPlan
		.addListener(new RelationAdapter<ServiceAgreementPaymentPlan, ServiceAgreement>() {
		    @Override
		    public void beforeAdd(ServiceAgreementPaymentPlan paymentPlanToAdd, ServiceAgreement serviceAgreement) {

			if (paymentPlanToAdd != null) {
			    if (paymentPlanToAdd.isCustomGratuityPaymentPlan()
				    && serviceAgreement.hasCustomGratuityPaymentPlan(paymentPlanToAdd.getExecutionYear())) {
				throw new DomainException(
					"error.domain.accounting.ServiceAgreement.already.has.a.customGratuity.payment.plan.for.execution.year");
			    }

			}
		    }
		});

	GratuityPaymentPlanGratuityEventWithPaymentPlan
		.addListener(new RelationAdapter<PaymentPlan, GratuityEventWithPaymentPlan>() {
		    @Override
		    public void beforeAdd(PaymentPlan paymentPlan, GratuityEventWithPaymentPlan event) {
			if (paymentPlan != null && event != null) {
			    if (paymentPlan.isCustomGratuityPaymentPlan() && paymentPlan.hasAnyGratuityEventsWithPaymentPlan()) {
				throw new DomainException("error.domain.accounting.PaymentPlan.already.has.gratuityEvent");
			    }
			}
		    }
		});
    }

    private CustomGratuityPaymentPlan() {
	super();
    }

    public CustomGratuityPaymentPlan(final ExecutionYear executionYear,
	    final DegreeCurricularPlanServiceAgreement serviceAgreement) {
	this();
	super.init(executionYear, serviceAgreement, Boolean.FALSE);
    }

    @Override
    public boolean isGratuityPaymentPlan() {
	return true;
    }

    @Override
    public boolean isCustomGratuityPaymentPlan() {
	return true;
    }

    public void delete() {
	while (hasAnyInstallments()) {
	    getInstallments().get(0).delete();
	}
	getGratuityEventsWithPaymentPlan().clear();
	removeParameters();
	removeRootDomainObject();
	super.deleteDomainObject();
    }
}
