package net.sourceforge.fenixedu.domain.accounting.events;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public abstract class PenaltyExemption extends PenaltyExemption_Base {

    protected PenaltyExemption() {
	super();
	super.setRootDomainObject(RootDomainObject.getInstance());
    }

    protected PenaltyExemption(final PenaltyExemptionJustificationType justificationType, final GratuityEvent gratuityEvent,
	    final Employee employee, final String comments, final YearMonthDay dispatchDate) {
	this();
	init(justificationType, gratuityEvent, employee, comments, dispatchDate);
    }

    protected void init(PenaltyExemptionJustificationType justificationType, Event event, Employee employee, String reason,
	    YearMonthDay dispatchDate) {
	checkParameters(justificationType);
	super.init(employee, event, PenaltyExemptionJustificationFactory.create(this, justificationType, reason, dispatchDate));
	event.recalculateState(new DateTime());

    }

    private void checkParameters(PenaltyExemptionJustificationType penaltyExemptionType) {
	if (penaltyExemptionType == null) {
	    throw new DomainException(
		    "error.accounting.events.gratuity.exemption.penalty.PenaltyExemption.penaltyExemptionType.cannot.be.null");
	}
    }

    @Override
    public void setEmployee(Employee employee) {
	throw new DomainException(
		"error.net.sourceforge.fenixedu.domain.accounting.events.gratuity.exemption.penalty.PenaltyExemption.cannot.modify.employee");
    }

    public PenaltyExemptionJustificationType getJustificationType() {
	return getExemptionJustification().getPenaltyExemptionJustificationType();
    }

    @Override
    public PenaltyExemptionJustification getExemptionJustification() {
	return (PenaltyExemptionJustification) super.getExemptionJustification();
    }

}
