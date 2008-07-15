package net.sourceforge.fenixedu.domain.accounting.events.candidacy;

import org.joda.time.DateTime;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.Exemption;
import net.sourceforge.fenixedu.domain.accounting.events.ExemptionJustification;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import dml.runtime.RelationAdapter;

public class SecondCycleIndividualCandidacyExemption extends SecondCycleIndividualCandidacyExemption_Base {

    static {
	ExemptionEvent.addListener(new RelationAdapter<Exemption, Event>() {
	    @Override
	    public void beforeAdd(Exemption exemption, Event event) {
		if (exemption != null && event != null) {
		    if (exemption instanceof SecondCycleIndividualCandidacyExemption) {
			final SecondCycleIndividualCandidacyEvent candidacyEvent = ((SecondCycleIndividualCandidacyEvent) event);
			if (candidacyEvent.hasSecondCycleIndividualCandidacyExemption()) {
			    throw new DomainException(
				    "error.accounting.events.SecondCycleIndividualCandidacyExemption.event.already.has.exemption");
			}
		    }
		}
	    }
	});
    }

    private SecondCycleIndividualCandidacyExemption() {
	super();
    }

    public SecondCycleIndividualCandidacyExemption(final Employee employee, final SecondCycleIndividualCandidacyEvent event,
	    final CandidacyExemptionJustificationType candidacyExemptionJustificationType) {
	this();
	super.init(employee, event, createJustification(candidacyExemptionJustificationType));
	event.recalculateState(new DateTime());
    }

    private ExemptionJustification createJustification(final CandidacyExemptionJustificationType justificationType) {
	return new SecondCycleIndividualCandidacyExemptionJustification(this, justificationType);
    }

    @Override
    public SecondCycleIndividualCandidacyEvent getEvent() {
	return (SecondCycleIndividualCandidacyEvent) super.getEvent();
    }

    @Override
    public void delete() {
	checkRulesToDelete();
	super.delete();
    }

    private void checkRulesToDelete() {
	if (getEvent().hasAnyPayments()) {
	    throw new DomainException(
		    "error.accounting.events.candidacy.SecondCycleIndividualCandidacyExemption.cannot.delete.event.has.payments");
	}

	if (getEvent().getIndividualCandidacy().isAccepted()) {
	    throw new DomainException(
		    "error.accounting.events.candidacy.SecondCycleIndividualCandidacyExemption.cannot.delete.candidacy.is.accepted");
	}
    }
}
