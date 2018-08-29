package org.fenixedu.academic.domain.accounting.events.gratuity.exemption.penalty;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.events.PenaltyExemptionJustificationType;
import org.fenixedu.academic.util.Money;
import org.joda.time.DateTime;

public abstract class FixedAmountPenaltyExemption extends FixedAmountPenaltyExemption_Base {
    
    protected FixedAmountPenaltyExemption() {
        super();
    }

    public FixedAmountPenaltyExemption(PenaltyExemptionJustificationType justificationType, Event event, Person responsible, String reason, DateTime dispatchDate, Money value) {
        init(justificationType, event, responsible, reason, dispatchDate, value);
    }

    public void init(PenaltyExemptionJustificationType justificationType, Event event, Person responsible, String reason, DateTime dispatchDate, Money value) {
        super.init(justificationType, event, responsible, reason, dispatchDate.toYearMonthDay());
        setValue(value);
    }

    @Override
    public Money getExemptionAmount(Money money) {
        return getValue();
    }

    @Override
    public boolean isForFine() {
        return false;
    }

    @Override
    public boolean isForInterest() {
        return false;
    }
}
