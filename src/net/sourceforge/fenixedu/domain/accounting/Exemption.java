package net.sourceforge.fenixedu.domain.accounting;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accounting.events.ExemptionJustification;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

import org.joda.time.DateTime;

import dml.runtime.RelationAdapter;

public abstract class Exemption extends Exemption_Base {

    static {
	ExemptionEvent.addListener(new RelationAdapter<Exemption, Event>() {

	    @Override
	    public void beforeAdd(Exemption exemption, Event event) {
		if (event != null && !event.isExemptionAppliable()) {
		    throw new DomainException(
			    "error.accounting.Exemption.event.does.not.support.exemption");
		}
	    }
	});

    }

    protected Exemption() {
	super();
	super.setRootDomainObject(RootDomainObject.getInstance());
	super.setOjbConcreteClass(getClass().getName());
	super.setWhenCreated(new DateTime());
    }

    protected void init(final Employee employee, final Event event,
	    final ExemptionJustification exemptionJustification) {
	checkParameters(employee, event, exemptionJustification);

	super.setEmployee(employee);
	super.setEvent(event);
	super.setExemptionJustification(exemptionJustification);
    }

    private void checkParameters(Employee employee, Event event,
	    ExemptionJustification exemptionJustification) {
	if (employee == null) {
	    throw new DomainException(
		    "error.accounting.events.gratuity.GratuityExemption.field_name.cannot.be.null");
	}

	if (event == null) {
	    throw new DomainException("error.accounting.Exemption.event.cannot.be.null");
	}

	if (exemptionJustification == null) {
	    throw new DomainException("error.accounting.Exemption.exemptionJustification.cannot.be.null");
	}
    }

    @Override
    public void setEvent(Event event) {
	throw new DomainException("error.domain.accounting.Exemption.cannot.modify.event");
    }

    @Override
    public void setEmployee(Employee employee) {
	throw new DomainException("error.accounting.Exemption.cannot.modify.employee");
    }

    @Override
    public void setWhenCreated(DateTime whenCreated) {
	throw new DomainException("error.accounting.Exemption.cannot.modify.whenCreated");
    }

    @Override
    public void setExemptionJustification(ExemptionJustification exemptionJustification) {
	throw new DomainException("error.accounting.Exemption.cannot.modify.exemptionJustification");
    }

    public void delete() {
	removeRootDomainObject();
	removeEmployee();
	removeEvent();
	getExemptionJustification().delete();

	super.deleteDomainObject();
    }

    @Override
    public void removeEmployee() {
	super.setEmployee(null);
    }

    @Override
    public void removeEvent() {
	super.setEvent(null);
    }

    public LabelFormatter getDescription() {
	return getExemptionJustification().getDescription();
    }

    public String getReason() {
	return getExemptionJustification().getReason();
    }

}
