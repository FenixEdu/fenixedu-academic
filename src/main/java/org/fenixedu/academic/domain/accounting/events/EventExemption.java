package org.fenixedu.academic.domain.accounting.events;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.Money;
import org.joda.time.DateTime;

public class EventExemption extends EventExemption_Base {

    private EventExemption() {
        super();
    }

    public EventExemption(final Event event, final Person responsible, final Money value,
            final EventExemptionJustificationType justificationType, final DateTime dispatchDate, final String reason) {

        this();
        super.init(responsible, event, createJustification(justificationType, dispatchDate == null ? null : dispatchDate.toLocalDate(), reason));

        if (value == null || !value.isPositive()) {
            throw new DomainException("error.EventExemption.invalid.amount");
        }

        setValue(value);

        event.recalculateState(new DateTime());
    }

    @Override public Money getExemptionAmount(Money money) {
        return getValue();
    }

    @Override
    public void delete() {
        if (getExemptionJustification().getJustificationType() == EventExemptionJustificationType.CUSTOM_PAYMENT_PLAN) {
            getEvent().deleteCustomPaymentPlan(getValue());
        }
        super.delete();
    }

}
