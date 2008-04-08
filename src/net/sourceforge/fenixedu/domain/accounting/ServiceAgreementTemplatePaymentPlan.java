package net.sourceforge.fenixedu.domain.accounting;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import dml.runtime.RelationAdapter;

public abstract class ServiceAgreementTemplatePaymentPlan extends ServiceAgreementTemplatePaymentPlan_Base {

    static {
	ServiceAgreementTemplateServiceAgreementTemplatePaymentPlan
		.addListener(new RelationAdapter<ServiceAgreementTemplatePaymentPlan, ServiceAgreementTemplate>() {
		    @Override
		    public void beforeAdd(ServiceAgreementTemplatePaymentPlan paymentPlanToAdd,
			    ServiceAgreementTemplate serviceAgreementTemplate) {

			if (paymentPlanToAdd != null) {
			    if (paymentPlanToAdd.isDefault()
				    && serviceAgreementTemplate.hasDefaultPaymentPlan(paymentPlanToAdd.getExecutionYear())) {
				throw new DomainException(
					"error.domain.accounting.ServiceAgreementTemplate.already.has.a.default.payment.plan.for.execution.year");
			    }

			}
		    }
		});
    }

    protected ServiceAgreementTemplatePaymentPlan() {
	super();
    }

    protected void init(final ExecutionYear executionYear, final ServiceAgreementTemplate serviceAgreementTemplate,
	    final Boolean defaultPlan) {
	super.init(executionYear, defaultPlan);
	checkParameters(serviceAgreementTemplate);
	super.setServiceAgreementTemplate(serviceAgreementTemplate);
    }

    private void checkParameters(final ServiceAgreementTemplate serviceAgreementTemplate) {
	if (serviceAgreementTemplate == null) {
	    throw new DomainException(
		    "error.accounting.ServiceAgreementTemplatePaymentPlan.serviceAgreementTemplate.cannot.be.null");
	}
    }

    @Override
    public void setServiceAgreementTemplate(ServiceAgreementTemplate serviceAgreementTemplate) {
	throw new DomainException("error.accounting.ServiceAgreementTemplatePaymentPlan.cannot.modify.serviceAgreementTemplate");
    }
}
