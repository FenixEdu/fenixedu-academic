package org.fenixedu.academic.domain.accounting.events.gratuity.exemption.penalty;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.events.PenaltyExemptionJustificationType;
import org.fenixedu.academic.util.Money;
import org.joda.time.DateTime;

public class FixedAmountPenaltyExemption extends FixedAmountPenaltyExemption_Base {
    
    protected FixedAmountPenaltyExemption() {
        super();
    }

    public FixedAmountPenaltyExemption(PenaltyExemptionJustificationType justificationType, Event event, Person responsible, String reason, DateTime dispatchDate, Money value) {
        setValue(value);
        super.init(justificationType, event, responsible, reason, dispatchDate.toYearMonthDay());
    }

    @Override
    public Money getExemptionAmount(Money money) {
        return getValue();
    }

    @Override
    public boolean isForInterest() {
        return true;
    }

    @Override
    public boolean isForFine() {
        return false;
    }
}
