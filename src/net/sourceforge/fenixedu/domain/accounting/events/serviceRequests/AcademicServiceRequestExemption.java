package net.sourceforge.fenixedu.domain.accounting.events.serviceRequests;

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

public class AcademicServiceRequestExemption extends AcademicServiceRequestExemption_Base {

    static {
	ExemptionEvent.addListener(new RelationAdapter<Exemption, Event>() {
	    @Override
	    public void beforeAdd(Exemption exemption, Event event) {
		if (exemption != null && event != null) {
		    if (exemption instanceof AcademicServiceRequestExemption) {
			final AcademicServiceRequestEvent academicEvent = (AcademicServiceRequestEvent) event;
			if (academicEvent.hasAcademicServiceRequestExemption()) {
			    throw new DomainException(
				    "error.accounting.events.AcademicServiceRequestExemption.event.already.has.exemption");
			}
		    }
		}
	    }
	});
    }

    private AcademicServiceRequestExemption() {
	super();
    }

    public AcademicServiceRequestExemption(final Employee employee, final AcademicServiceRequestEvent event, final Money value,
	    final AcademicServiceRequestJustificationType justificationType, final LocalDate dispatchDate, final String reason) {

	this();
	super.init(employee, event, createJustification(justificationType, dispatchDate, reason));

	check(value, "error.AcademicServiceRequestExemption.invalid.amount");
	setValue(value);

	event.recalculateState(new DateTime());
    }

    private ExemptionJustification createJustification(AcademicServiceRequestJustificationType justificationType,
	    final LocalDate dispatchDate, String reason) {
	return new AcademicServiceRequestExemptionJustification(this, justificationType, dispatchDate, reason);
    }

    @Override
    public void delete() {
	checkRulesToDelete();
	super.delete();
    }

    private void checkRulesToDelete() {
	if (getEvent().hasAnyPayments()) {
	    throw new DomainException(
		    "error.accounting.events.candidacy.AcademicServiceRequestExemption.cannot.delete.event.has.payments");
	}
    }

    @Service
    static public AcademicServiceRequestExemption create(final Employee employee, final AcademicServiceRequestEvent event,
	    final Money value, final AcademicServiceRequestJustificationType justificationType, final LocalDate dispatchDate,
	    final String reason) {
	return new AcademicServiceRequestExemption(employee, event, value, justificationType, dispatchDate, reason);
    }

}
