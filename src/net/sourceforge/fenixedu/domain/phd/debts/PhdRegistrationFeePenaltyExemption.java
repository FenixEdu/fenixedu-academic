package net.sourceforge.fenixedu.domain.phd.debts;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.Exemption;
import net.sourceforge.fenixedu.domain.accounting.events.PenaltyExemptionJustificationType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.YearMonthDay;

import dml.runtime.RelationAdapter;

public class PhdRegistrationFeePenaltyExemption extends PhdRegistrationFeePenaltyExemption_Base {

    static {
	ExemptionEvent.addListener(new RelationAdapter<Exemption, Event>() {
	    @Override
	    public void beforeAdd(Exemption exemption, Event event) {
		if (exemption != null && event != null) {
		    if (exemption instanceof PhdRegistrationFeePenaltyExemption) {
			final PhdRegistrationFee phdEvent = (PhdRegistrationFee) event;
			if (phdEvent.hasPhdRegistrationFeePenaltyExemption()) {
			    throw new DomainException("error.PhdRegistrationFeePenaltyExemption.event.already.has.exemption");
			}

		    }
		}
	    }
	});
    }

    private PhdRegistrationFeePenaltyExemption() {
	super();
    }

    public PhdRegistrationFeePenaltyExemption(final PenaltyExemptionJustificationType penaltyExemptionType,
	    final PhdRegistrationFee event, final Person responsible, final String comments,
	    final YearMonthDay directiveCouncilDispatchDate) {
	this();
	super.init(penaltyExemptionType, event, responsible, comments, directiveCouncilDispatchDate);
    }

    @Override
    public PhdRegistrationFee getEvent() {
	return (PhdRegistrationFee) super.getEvent();
    }
}
