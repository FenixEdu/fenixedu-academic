package net.sourceforge.fenixedu.domain.accounting.events;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.Exemption;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.DegreeFinalizationCertificateRequestEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;

import dml.runtime.RelationAdapter;

public class DegreeFinalizationCertificateRequestExemption extends DegreeFinalizationCertificateRequestExemption_Base {

    static {
	ExemptionEvent.addListener(new RelationAdapter<Exemption, Event>() {
	    @Override
	    public void beforeAdd(Exemption exemption, Event event) {
		if (exemption != null && event != null) {
		    if (exemption instanceof DegreeFinalizationCertificateRequestExemption) {
			final DegreeFinalizationCertificateRequestEvent candidacyEvent = (DegreeFinalizationCertificateRequestEvent) event;
			if (candidacyEvent.hasDegreeFinalizationCertificateRequestExemption()) {
			    throw new DomainException(
				    "error.accounting.events.DegreeFinalizationCertificateRequestExemption.event.already.has.exemption");
			}
		    }
		}
	    }
	});
    }

    private DegreeFinalizationCertificateRequestExemption() {
	super();
    }

    public DegreeFinalizationCertificateRequestExemption(final Employee employee,
	    final DegreeFinalizationCertificateRequestEvent event, final Money value,
	    final DegreeFinalizationCertificateRequestJustificationType justificationType, final String reason) {

	this();
	super.init(employee, event, createJustification(justificationType, reason));

	check(value, "error.DegreeFinalizationCertificateRequestExemption.invalid.amount");
	setValue(value);

	event.recalculateState(new DateTime());
    }

    private ExemptionJustification createJustification(
	    final DegreeFinalizationCertificateRequestJustificationType justificationType, final String reason) {
	return new DegreeFinalizationCertificateRequestExemptionJustification(this, justificationType, reason);
    }

    @Override
    public void delete() {
	checkRulesToDelete();
	super.delete();
    }

    private void checkRulesToDelete() {
	if (getEvent().hasAnyPayments()) {
	    throw new DomainException(
		    "error.accounting.events.candidacy.DegreeFinalizationCertificateRequestExemption.cannot.delete.event.has.payments");
	}
    }

    @Service
    static public DegreeFinalizationCertificateRequestExemption create(final Employee employee,
	    final DegreeFinalizationCertificateRequestEvent event, final Money value,
	    final DegreeFinalizationCertificateRequestJustificationType justificationType, final String reason) {
	return new DegreeFinalizationCertificateRequestExemption(employee, event, value, justificationType, reason);
    }
}
