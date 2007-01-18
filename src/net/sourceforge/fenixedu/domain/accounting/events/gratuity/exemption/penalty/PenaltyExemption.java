package net.sourceforge.fenixedu.domain.accounting.events.gratuity.exemption.penalty;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import dml.runtime.RelationAdapter;

public abstract class PenaltyExemption extends PenaltyExemption_Base {

    static {
	PenaltyExemptionGratuityEvent
		.addListener(new RelationAdapter<PenaltyExemption, GratuityEvent>() {

		    @Override
		    public void beforeAdd(PenaltyExemption penaltyExemption, GratuityEvent gratuityEvent) {
			if (gratuityEvent != null) {
			    if (!gratuityEvent.isPenaltyExemptionApplicable()) {
				throw new DomainException(
					"error.accounting.events.gratuity.exemption.penalty.PenaltyExemption.gratuity.event.does.not.support.penalty.exemption");
			    }

			    if (gratuityEvent.isClosed()) {
				throw new DomainException(
					"error.accounting.events.gratuity.exemption.penalty.PenaltyExemption.cannot.add.penalty.exemptions.on.closed.events");
			    }

			}
		    }

		});
    }

    protected PenaltyExemption() {
	super();
	super.setOjbConcreteClass(getClass().getName());
	super.setWhenCreated(new DateTime());
    }

    protected PenaltyExemption(final PenaltyExemptionType penaltyExemptionType,
	    final GratuityEvent gratuityEvent, final Employee employee, final String comments) {
	this();
	init(penaltyExemptionType, gratuityEvent, employee, comments);
    }

    protected void init(PenaltyExemptionType penaltyExemptionType, GratuityEvent gratuityEvent,
	    Employee employee, String comments) {
	checkParameters(penaltyExemptionType, gratuityEvent, employee, comments);

	super.setExemptionType(penaltyExemptionType);
	super.setGratuityEvent(gratuityEvent);
	super.setEmployee(employee);
	super.setComments(comments);
    }

    private void checkParameters(PenaltyExemptionType penaltyExemptionType, GratuityEvent gratuityEvent,
	    Employee employee, String comments) {
	if (penaltyExemptionType == null) {
	    throw new DomainException(
		    "error.accounting.events.gratuity.exemption.penalty.PenaltyExemption.penaltyExemptionType.cannot.be.null");
	}

	if (gratuityEvent == null) {
	    throw new DomainException(
		    "error.accounting.events.gratuity.exemption.penalty.PenaltyExemption.gratuityEvent.cannot.be.null");
	}

	if (employee == null) {
	    throw new DomainException(
		    "error.accounting.events.gratuity.exemption.penalty.PenaltyExemption.employee.cannot.be.null");
	}

	if (penaltyExemptionType == PenaltyExemptionType.DIRECTIVE_COUNCIL_AUTHORIZATION
		&& StringUtils.isEmpty(comments)) {
	    throw new DomainException(
		    "error.accounting.events.gratuity.exemption.penalty.PenaltyExemption.penalty.type.requires.filling.comments");
	}

    }

    @Override
    public void setExemptionType(PenaltyExemptionType exemptionType) {
	throw new DomainException(
		"error.net.sourceforge.fenixedu.domain.accounting.events.gratuity.exemption.penalty.PenaltyExemption.cannot.modify.exemptionType");
    }

    @Override
    public void setEmployee(Employee employee) {
	throw new DomainException(
		"error.net.sourceforge.fenixedu.domain.accounting.events.gratuity.exemption.penalty.PenaltyExemption.cannot.modify.employee");
    }

    @Override
    public void setComments(String comments) {
	throw new DomainException(
		"error.net.sourceforge.fenixedu.domain.accounting.events.gratuity.exemption.penalty.PenaltyExemption.cannot.modify.comments");
    }

    @Override
    public void setGratuityEvent(GratuityEvent gratuityEvent) {
	throw new DomainException(
		"error.net.sourceforge.fenixedu.domain.accounting.events.gratuity.exemption.penalty.PenaltyExemption.cannot.modify.gratuityEvent");

    }

    public void delete() {

	checkRulesToDelete();

	super.setGratuityEvent(null);
	super.setEmployee(null);

	super.deleteDomainObject();

    }

    private void checkRulesToDelete() {
	if (getGratuityEvent().isClosed()) {
	    throw new DomainException(
		    "error.accounting.events.gratuity.exemption.penalty.PenaltyExemption.cannot.delete.penalty.exemptions.for.closed.events");
	}
    }

}
