package net.sourceforge.fenixedu.domain.accounting.events.gratuity;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

import dml.runtime.RelationAdapter;

public abstract class GratuityExemption extends GratuityExemption_Base {

    static {
	GratuityExemptionGratuityEvent
		.addListener(new RelationAdapter<GratuityExemption, GratuityEvent>() {

		    @Override
		    public void beforeAdd(GratuityExemption exemption, GratuityEvent gratuityEvent) {
			if (gratuityEvent != null && !gratuityEvent.canApplyExemption()) {
			    throw new DomainException(
				    "error.accounting.events.gratuity.GratuityExemption.gratuity.exemption.cannot.applied.to.gratuity.event");
			}
		    }

		    @Override
		    public void beforeRemove(GratuityExemption exemption, GratuityEvent gratuityEvent) {
			if (gratuityEvent != null) {
			    checkRulesToDelete(gratuityEvent);
			} else if (exemption != null && exemption.getGratuityEvent() != null) {
			    checkRulesToDelete(exemption.getGratuityEvent());
			}
		    }

		    private void checkRulesToDelete(final GratuityEvent gratuityEvent) {
			if (!gratuityEvent.canRemoveExemption(new DateTime())) {
			    throw new DomainException(
				    "error.accounting.events.gratuity.GratuityExemption.remove.gratuity.exemption.will.cause.event.to.open");
			}
		    }

		});
    }

    protected GratuityExemption() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setOjbConcreteClass(getClass().getName());
    }

    protected void init(final Employee employee, final GratuityExemptionType exemptionType,
	    final GratuityEvent gratuityEvent) {
	checkParameters(employee, exemptionType, gratuityEvent);

	super.setEmployee(employee);
	super.setExemptionType(exemptionType);
	super.setGratuityEvent(gratuityEvent);

	gratuityEvent.recalculateState(new DateTime());
    }

    private void checkParameters(Employee employee, GratuityExemptionType exemptionType,
	    GratuityEvent gratuityEvent) {
	if (employee == null) {
	    throw new DomainException(
		    "error.accounting.events.gratuity.GratuityExemption.field_name.cannot.be.null");
	}

	if (exemptionType == null) {
	    throw new DomainException(
		    "error.accounting.events.gratuity.GratuityExemption.exemptionType.cannot.be.null");
	}

	if (gratuityEvent == null) {
	    throw new DomainException(
		    "error.accounting.events.gratuity.GratuityExemption.gratuityEvent.cannot.be.null");
	}
    }

    @Override
    public void setExemptionType(GratuityExemptionType exemptionType) {
	throw new DomainException(
		"error.accounting.events.gratuity.GratuityExemption.cannot.modify.exemptionType");
    }

    @Override
    public void setGratuityEvent(GratuityEvent gratuityEvent) {
	throw new DomainException(
		"error.accounting.events.gratuity.GratuityExemption.cannot.modify.gratuityEvent");
    }

    @Override
    public void setEmployee(Employee employee) {
	throw new DomainException(
		"error.net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityExemption.cannot.modify.employee");
    }

    public void delete() {
	removeRootDomainObject();
	removeEmployee();
	tryRemoveGratuityEvent();

	super.deleteDomainObject();
    }

    private void tryRemoveGratuityEvent() {
	final GratuityEvent gratuityEvent = getGratuityEvent();
	boolean previouslyClosed = gratuityEvent.isClosed();
	removeGratuityEvent();

	gratuityEvent.recalculateState(new DateTime());

	if (gratuityEvent.isClosed() != previouslyClosed) {
	    throw new DomainException(
		    "error.net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityExemption.cannot.remove.exemption.because.forces.event.to.open");
	}
    }

    @Override
    public void removeGratuityEvent() {
	super.setGratuityEvent(null);
    }

    @Override
    public void removeEmployee() {
	super.setEmployee(null);
    }

    abstract public BigDecimal calculateDiscountPercentage(Money amount);

}
