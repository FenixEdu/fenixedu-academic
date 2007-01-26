package net.sourceforge.fenixedu.domain.accounting.events;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.Exemption;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEvent;
import net.sourceforge.fenixedu.domain.accounting.events.penaltyExemptionJustifications.DirectiveCouncilAuthorizationJustification;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

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
	    final YearMonthDay directiveCouncilDispatchDate) {
	this();
	init(justificationType, gratuityEvent, employee, comments, directiveCouncilDispatchDate);
    }

    protected void init(PenaltyExemptionJustificationType justificationType, Event event,
	    Employee employee, String comments, YearMonthDay directiveCouncilDispatchDate) {
	super.init(employee, event);
	checkParameters(justificationType);
	super.setPenaltyExemptionJustification(createPenaltyExemptionJustification(comments,
		directiveCouncilDispatchDate, justificationType));
    }

    private PenaltyExemptionJustification createPenaltyExemptionJustification(String comments,
	    YearMonthDay directiveCouncilDispatchDate,
	    PenaltyExemptionJustificationType justificationType) {
	if (justificationType == PenaltyExemptionJustificationType.DIRECTIVE_COUNCIL_AUTHORIZATION) {
	    return new DirectiveCouncilAuthorizationJustification(this, comments,
		    directiveCouncilDispatchDate);
	} else {
	    return new PenaltyExemptionJustification(this, justificationType, comments);
	}

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

    public LabelFormatter getDescription() {
	return getPenaltyExemptionJustification().getDescription();
    }

    public String getComments() {
	return getPenaltyExemptionJustification().getComments();
    }

    public PenaltyExemptionJustificationType getJustificationType() {
	return getPenaltyExemptionJustification().getJustificationType();
    }

}
