package net.sourceforge.fenixedu.domain.accounting.events.gratuity.exemption.penalty;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.Exemption;
import net.sourceforge.fenixedu.domain.accounting.Installment;
import net.sourceforge.fenixedu.domain.accounting.events.PenaltyExemptionJustificationType;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEventWithPaymentPlan;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.Checked;

import org.joda.time.YearMonthDay;

import dml.runtime.RelationAdapter;

public class InstallmentPenaltyExemption extends InstallmentPenaltyExemption_Base {

    static {
	ExemptionEvent.addListener(new RelationAdapter<Exemption, Event>() {
	    @Override
	    public void beforeAdd(Exemption exemption, Event event) {
		if (exemption != null && event != null) {
		    if (exemption instanceof InstallmentPenaltyExemption) {
			if (!(event instanceof GratuityEventWithPaymentPlan)) {
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
    }

    public InstallmentPenaltyExemption(final PenaltyExemptionJustificationType penaltyExemptionType,
	    final GratuityEventWithPaymentPlan gratuityEventWithPaymentPlan, final Employee employee,
	    final Installment installment, final String comments,
	    final YearMonthDay directiveCouncilDispatchDate) {
	this();
	init(penaltyExemptionType, gratuityEventWithPaymentPlan, employee, installment, comments,
		directiveCouncilDispatchDate);

    }

    protected void init(PenaltyExemptionJustificationType penaltyExemptionType,
	    GratuityEventWithPaymentPlan gratuityEventWithPaymentPlan, Employee employee,
	    Installment installment, String comments, YearMonthDay directiveCouncilDispatchDate) {
	super.init(penaltyExemptionType, gratuityEventWithPaymentPlan, employee, comments,
		directiveCouncilDispatchDate);
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

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Override
    public void setInstallment(Installment installment) {
	super.setInstallment(installment);
    }

    @Override
    public void delete() {
	super.setInstallment(null);
	super.delete();
    }

}
