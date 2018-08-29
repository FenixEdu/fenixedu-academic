package org.fenixedu.academic.domain.accounting.events.gratuity.exemption.penalty;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.events.PenaltyExemptionJustificationType;
import org.fenixedu.academic.util.Money;
import org.joda.time.DateTime;

public class FixedAmountFineExemption extends FixedAmountFineExemption_Base {

    public FixedAmountFineExemption(PenaltyExemptionJustificationType justificationType, Event event, Person responsible, String reason,
            DateTime dispatchDate, Money value) {
        super.init(justificationType, event, responsible, reason, dispatchDate, value);
    }

    @Override
    public boolean isForFine() {
        return true;
    }
}
