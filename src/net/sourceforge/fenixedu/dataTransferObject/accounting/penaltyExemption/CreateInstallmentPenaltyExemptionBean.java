package net.sourceforge.fenixedu.dataTransferObject.accounting.penaltyExemption;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.accounting.Installment;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEventWithPaymentPlan;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.exemption.penalty.PenaltyExemptionType;

public class CreateInstallmentPenaltyExemptionBean implements Serializable {

    private DomainReference<GratuityEventWithPaymentPlan> gratuityEventWithPaymentPlan;

    private PenaltyExemptionType exemptionType;

    private List<DomainReference<Installment>> installments;

    private String comments;

    public CreateInstallmentPenaltyExemptionBean(
	    GratuityEventWithPaymentPlan gratuityEventWithPaymentPlan) {
	super();
	setGratuityEventWithPaymentPlan(gratuityEventWithPaymentPlan);
	setInstallments(new ArrayList<Installment>());
    }

    public GratuityEventWithPaymentPlan getGratuityEventWithPaymentPlan() {
	return (this.gratuityEventWithPaymentPlan != null) ? this.gratuityEventWithPaymentPlan
		.getObject() : null;
    }

    private void setGratuityEventWithPaymentPlan(
	    GratuityEventWithPaymentPlan gratuityEventWithPaymentPlan) {
	this.gratuityEventWithPaymentPlan = (gratuityEventWithPaymentPlan != null) ? new DomainReference<GratuityEventWithPaymentPlan>(
		gratuityEventWithPaymentPlan)
		: null;
    }

    public List<Installment> getInstallments() {
	final List<Installment> result = new ArrayList<Installment>();
	for (final DomainReference<Installment> installment : installments) {
	    result.add(installment.getObject());
	}

	return result;
    }

    public void setInstallments(List<Installment> installments) {
	final List<DomainReference<Installment>> result = new ArrayList<DomainReference<Installment>>();
	for (final Installment installment : installments) {
	    result.add(new DomainReference<Installment>(installment));
	}

	this.installments = result;
    }

    public PenaltyExemptionType getExemptionType() {
	return exemptionType;
    }

    public void setExemptionType(PenaltyExemptionType exemptionType) {
	this.exemptionType = exemptionType;
    }

    public String getComments() {
	return comments;
    }

    public void setComments(String notes) {
	this.comments = notes;
    }

}
