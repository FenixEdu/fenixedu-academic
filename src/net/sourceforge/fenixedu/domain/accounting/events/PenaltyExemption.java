package net.sourceforge.fenixedu.domain.accounting.events;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.Exemption;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.YearMonthDay;

import dml.runtime.RelationAdapter;

public abstract class PenaltyExemption extends PenaltyExemption_Base {

    static {
	ExemptionEvent.addListener(new RelationAdapter<Exemption, Event>() {

	    @Override
	    public void beforeAdd(Exemption exemption, Event event) {
		if (exemption instanceof PenaltyExemption) {
		    if (event != null && event.isClosed()) {
			throw new DomainException(
				"error.accounting.events.gratuity.exemption.penalty.PenaltyExemption.cannot.add.penalty.exemptions.on.closed.events");
		    }
		}
	    }
	});
    }

    protected PenaltyExemption() {
	super();
	super.setRootDomainObject(RootDomainObject.getInstance());
	super.setOjbConcreteClass(getClass().getName());
    }

    protected PenaltyExemption(final PenaltyExemptionJustificationType justificationType,
	    final GratuityEvent gratuityEvent, final Employee employee, final String comments,
	    final YearMonthDay dispatchDate) {
	this();
	init(justificationType, gratuityEvent, employee, comments, dispatchDate);
    }

    protected void init(PenaltyExemptionJustificationType justificationType, Event event,
	    Employee employee, String reason, YearMonthDay dispatchDate) {
	checkParameters(justificationType);
	super.init(employee, event, PenaltyExemptionJustificationFactory.create(this, justificationType,
		reason, dispatchDate));

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

    public void delete() {
	checkRulesToDelete();

	super.delete();

    }

    private void checkRulesToDelete() {
	if (getEvent().isClosed()) {
	    throw new DomainException(
		    "error.accounting.events.gratuity.exemption.penalty.PenaltyExemption.cannot.delete.penalty.exemptions.for.closed.events");
	}
    }

    public PenaltyExemptionJustificationType getJustificationType() {
	return getExemptionJustification().getPenaltyExemptionJustificationType();
    }

    @Override
    public PenaltyExemptionJustification getExemptionJustification() {
	return (PenaltyExemptionJustification) super.getExemptionJustification();
    }

}
