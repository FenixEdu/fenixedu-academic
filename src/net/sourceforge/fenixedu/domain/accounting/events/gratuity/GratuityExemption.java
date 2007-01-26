package net.sourceforge.fenixedu.domain.accounting.events.gratuity;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.Exemption;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

import dml.runtime.RelationAdapter;

public abstract class GratuityExemption extends GratuityExemption_Base {

    static {
	ExemptionEvent.addListener(new RelationAdapter<Exemption, Event>() {

	    @Override
	    public void beforeAdd(Exemption exemption, Event event) {
		if (event instanceof GratuityEvent) {
		    if (exemption instanceof GratuityExemption) {
			final GratuityEvent gratuityEvent = (GratuityEvent) event;
			if (gratuityEvent.hasGratuityExemption()) {
			    throw new DomainException(
				    "error.net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityExemption.event.already.has.gratuity.exemption");
			}

			if (!gratuityEvent.canApplyExemption()) {
			    throw new DomainException(
				    "error.accounting.events.gratuity.GratuityExemption.gratuity.exemption.cannot.applied.to.gratuity.event");
			}
		    }
		}
	    }

	});
    }

    protected GratuityExemption() {
	super();
    }

    protected void init(final Employee employee, final GratuityExemptionType exemptionType,
	    final GratuityEvent gratuityEvent) {
	super.init(employee, gratuityEvent);
	checkParameters(exemptionType);
	super.setGratuityExemptionType(exemptionType);

	gratuityEvent.recalculateState(new DateTime());
    }

    private void checkParameters(GratuityExemptionType exemptionType) {
	if (exemptionType == null) {
	    throw new DomainException(
		    "error.accounting.events.gratuity.GratuityExemption.exemptionType.cannot.be.null");
	}
    }

    @Override
    public void setGratuityExemptionType(GratuityExemptionType gratuityExemptionType) {
	throw new DomainException(
		"error.net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityExemption.cannot.modify.gratuityExemptionType");
    }

    public GratuityEvent getGratuityEvent() {
	return (GratuityEvent) getEvent();
    }

    public void delete() {
	checkRulesToDelete();
	super.delete();

    }

    private void checkRulesToDelete() {
	if (!getGratuityEvent().canRemoveExemption(new DateTime())) {
	    throw new DomainException(
		    "error.accounting.events.gratuity.GratuityExemption.remove.gratuity.exemption.will.cause.event.to.open");
	}
    }

    abstract public BigDecimal calculateDiscountPercentage(Money amount);

}
