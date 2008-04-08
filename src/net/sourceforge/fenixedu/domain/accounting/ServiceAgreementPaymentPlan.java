package net.sourceforge.fenixedu.domain.accounting;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public abstract class ServiceAgreementPaymentPlan extends ServiceAgreementPaymentPlan_Base {

    protected ServiceAgreementPaymentPlan() {
	super();
    }

    protected void init(final ExecutionYear executionYear, final ServiceAgreement serviceAgreement, final Boolean defaultPlan) {
	super.init(executionYear, defaultPlan);
	checkParameters(serviceAgreement);
	super.setServiceAgreement(serviceAgreement);
    }

    private void checkParameters(final ServiceAgreement serviceAgreement) {
	if (serviceAgreement == null) {
	    throw new DomainException("error.accounting.ServiceAgreementPaymentPlan.serviceAgreement.cannot.be.null");
	}
    }
    
    @Override
    protected void removeParameters() {
        super.removeParameters();
        super.setServiceAgreement(null);
    }

    @Override
    public void setServiceAgreement(ServiceAgreement serviceAgreement) {
	throw new DomainException("error.accounting.ServiceAgreementPaymentPlan.cannot.modify.serviceAgreement");
    }

    @Override
    public ServiceAgreementTemplate getServiceAgreementTemplate() {
	return getServiceAgreement().getServiceAgreementTemplate();
    }
}
