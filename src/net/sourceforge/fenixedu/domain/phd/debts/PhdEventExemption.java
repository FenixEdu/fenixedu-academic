package net.sourceforge.fenixedu.domain.phd.debts;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.Exemption;
import net.sourceforge.fenixedu.domain.accounting.events.ExemptionJustification;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.ist.fenixWebFramework.services.Service;
import dml.runtime.RelationAdapter;

public class PhdEventExemption extends PhdEventExemption_Base {

    static {
	ExemptionEvent.addListener(new RelationAdapter<Exemption, Event>() {
	    @Override
	    public void beforeAdd(Exemption exemption, Event event) {
		if (exemption != null && event != null) {
		    if (exemption instanceof PhdEventExemption) {
			final PhdEvent phdEvent = (PhdEvent) event;
			if (phdEvent.hasPhdEventExemption()) {
			    throw new DomainException("error.accounting.events.PhdEventExemption.event.already.has.exemption");
			}
		    }
		}
	    }
	});
    }

    protected PhdEventExemption() {
	super();
    }

    public PhdEventExemption(final Employee employee, final PhdEvent event, final Money value,
	    final PhdEventExemptionJustificationType justificationType, final LocalDate dispatchDate, final String reason) {

	this();
	super.init(employee, event, createJustification(justificationType, dispatchDate, reason));

	check(value, "error.PhdEventExemption.invalid.amount");
	setValue(value);

	event.recalculateState(new DateTime());
    }

    private ExemptionJustification createJustification(PhdEventExemptionJustificationType justificationType,
	    LocalDate dispatchDate, String reason) {
	return new PhdEventExemptionJustification(this, justificationType, dispatchDate, reason);
    }

    @Override
    public void delete() {
	checkRulesToDelete();
	super.delete();
    }

    private void checkRulesToDelete() {
	if (getEvent().hasAnyPayments()) {
	    throw new DomainException("error.PhdEventExemption.cannot.delete.event.has.payments");
	}
    }

    @Service
    static public PhdEventExemption create(final Employee employee, final PhdEvent event, final Money value,
	    final PhdEventExemptionJustificationType justificationType, final LocalDate dispatchDate, final String reason) {
	return new PhdEventExemption(employee, event, value, justificationType, dispatchDate, reason);
    }

    @Override
    public boolean isPhdEventExemption() {
	return true;
    }

}
