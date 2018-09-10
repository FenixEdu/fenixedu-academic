package org.fenixedu.academic.domain.accounting.events.gratuity.exemption.penalty;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.events.EventExemptionJustificationType;
import org.fenixedu.academic.util.Money;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.Collections;
import java.util.Set;

public class FixedAmountFineExemption extends FixedAmountFineExemption_Base {

    public FixedAmountFineExemption(Event event, Person responsible, Money value, EventExemptionJustificationType justificationType,
            DateTime dispatchDate, String reason) {
        super.init(justificationType, event, responsible, reason, dispatchDate, value);
    }

    @Override
    public boolean isForFine() {
        return true;
    }

    @Override public Set<LocalDate> getDueDates(DateTime when) {
        return Collections.emptySet();
    }
}
