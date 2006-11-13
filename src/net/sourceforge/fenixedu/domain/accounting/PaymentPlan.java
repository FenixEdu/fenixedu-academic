package net.sourceforge.fenixedu.domain.accounting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

import dml.runtime.RelationAdapter;

public abstract class PaymentPlan extends PaymentPlan_Base {

    static {
	ServiceAgreementTemplatePaymentPlan
		.addListener(new RelationAdapter<PaymentPlan, ServiceAgreementTemplate>() {
		    @Override
		    public void beforeAdd(PaymentPlan paymentPlanToAdd,
			    ServiceAgreementTemplate serviceAgreementTemplate) {

			if (paymentPlanToAdd != null) {
			    for (final PaymentPlan paymentPlan : serviceAgreementTemplate
				    .getPaymentPlansSet()) {
				if (paymentPlan.getClass().equals(paymentPlanToAdd.getClass())
					&& paymentPlan.getExecutionYear() == paymentPlanToAdd
						.getExecutionYear()) {
				    throw new DomainException(
					    "error.domain.accounting.PaymentPlan.is.already.defined.for.execution.year.in.service.agreement.template");
				}

			    }
			}
		    }
		});
    }

    protected PaymentPlan() {
	super();
	super.setRootDomainObject(RootDomainObject.getInstance());
	super.setOjbConcreteClass(getClass().getName());
	super.setWhenCreated(new DateTime());
    }

    protected void init(final ExecutionYear executionYear,
	    final ServiceAgreementTemplate serviceAgreementTemplate) {

	checkParameters(executionYear, serviceAgreementTemplate);

	super.setExecutionYear(executionYear);
	super.setServiceAgreementTemplate(serviceAgreementTemplate);

    }

    private void checkParameters(final ExecutionYear executionYear,
	    final ServiceAgreementTemplate serviceAgreementTemplate) {
	if (executionYear == null) {
	    throw new DomainException("error.accounting.PaymentCondition.executionYear.cannot.be.null");
	}

	if (serviceAgreementTemplate == null) {
	    throw new DomainException(
		    "error.accounting.PaymentCondition.serviceAgreementTemplate.cannot.be.null");
	}

    }

    @Override
    public void setExecutionYear(ExecutionYear executionYear) {
	throw new DomainException("error.accounting.PaymentCondition.cannot.modify.executionYear");
    }

    @Override
    public void setServiceAgreementTemplate(ServiceAgreementTemplate serviceAgreementTemplate) {
	throw new DomainException(
		"error.accounting.PaymentCondition.cannot.modify.serviceAgreementTemplate");
    }

    public List<Installment> getInstallmentsSortedByEndDate() {
	final List<Installment> result = new ArrayList<Installment>(getInstallmentsSet());
	Collections.sort(result, Installment.COMPARATOR_BY_END_DATE);

	return result;
    }

    public Installment getLastInstallment() {
	return (getInstallmentsCount() == 0) ? null : Collections.max(getInstallmentsSet(),
		Installment.COMPARATOR_BY_ORDER);
    }
    
    public int getLastInstallmentOrder() {
	final Installment installment = getLastInstallment();
	return installment == null ? 0 : installment.getOrder(); 
    }

    @Override
    public void addInstallments(Installment installment) {
	throw new DomainException("error.accounting.PaymentPlan.cannot.add.installment");
    }

    @Override
    public List<Installment> getInstallments() {
	return Collections.unmodifiableList(super.getInstallments());
    }

    @Override
    public Set<Installment> getInstallmentsSet() {
	return Collections.unmodifiableSet(super.getInstallmentsSet());
    }

    @Override
    public Iterator<Installment> getInstallmentsIterator() {
	return getInstallmentsSet().iterator();
    }

    @Override
    public void removeInstallments(Installment installment) {
	throw new DomainException("error.accounting.PaymentPlan.cannot.remove.installment");
    }

}
