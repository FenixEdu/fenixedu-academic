package net.sourceforge.fenixedu.domain.accounting.events.gratuity.exemption.penalty;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accounting.Installment;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEvent;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEventWithPaymentPlan;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import dml.runtime.RelationAdapter;

public class InstallmentPenaltyExemption extends InstallmentPenaltyExemption_Base {

    static {
	PenaltyExemptionGratuityEvent
		.addListener(new RelationAdapter<PenaltyExemption, GratuityEvent>() {
		    @Override
		    public void beforeAdd(PenaltyExemption penaltyExemption, GratuityEvent gratuityEvent) {
			if (penaltyExemption != null && gratuityEvent != null) {
			    if (penaltyExemption instanceof InstallmentPenaltyExemption) {
				if (!(gratuityEvent instanceof GratuityEventWithPaymentPlan)) {
				    throw new DomainException(
					    "error.accounting.events.gratuity.exemption.penalty.InstallmentPenaltyExemption.cannot.add.installment.penalty.exemption.to.events.without.payment.plan");
				}
			    }
			}
		    }
		});
    }

    protected InstallmentPenaltyExemption() {
	super();
	super.setRootDomainObject(RootDomainObject.getInstance());
    }

    public InstallmentPenaltyExemption(final PenaltyExemptionType penaltyExemptionType,
	    final GratuityEventWithPaymentPlan gratuityEventWithPaymentPlan, final Employee employee,
	    final Installment installment, final String notes) {
	this();
	init(penaltyExemptionType, gratuityEventWithPaymentPlan, employee, installment, notes);

    }

    protected void init(PenaltyExemptionType penaltyExemptionType,
	    GratuityEventWithPaymentPlan gratuityEventWithPaymentPlan, Employee employee,
	    Installment installment, String notes) {
	super.init(penaltyExemptionType, gratuityEventWithPaymentPlan, employee, notes);
	checkParameters(installment);
	checkRulesToCreate(gratuityEventWithPaymentPlan, installment);
	super.setInstallment(installment);
    }

    private void checkRulesToCreate(final GratuityEventWithPaymentPlan gratuityEventWithPaymentPlan,
	    final Installment installment) {
	if (gratuityEventWithPaymentPlan.hasPenaltyExemptionFor(installment)) {
	    throw new DomainException(
		    "error.accounting.events.gratuity.exemption.penalty.InstallmentPenaltyExemption.event.already.has.penalty.exemption.for.installment");

	}

    }

    private void checkParameters(Installment installment) {
	if (installment == null) {
	    throw new DomainException(
		    "error.accounting.events.gratuity.exemption.penalty.InstallmentPenaltyExemption.installment.cannot.be.null");
	}
    }

    @Override
    public void setInstallment(Installment installment) {
	throw new DomainException(
		"error.net.sourceforge.fenixedu.domain.accounting.events.gratuity.exemption.penalty.InstallmentPenaltyExemption.cannot.modify.installment");
    }

    @Override
    public void delete() {
	super.setInstallment(null);
	super.delete();
    }

}
