package org.fenixedu.academic.domain.accounting.paymentCodes;

import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.util.Money;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

/***
 * This is used to register the promise of a payment by an event owner
 */
public class EventPaymentCodeEntry extends EventPaymentCodeEntry_Base {
    
    protected EventPaymentCodeEntry() {
        super();
        setCreated(new DateTime());
    }

    protected EventPaymentCodeEntry(EventPaymentCode eventPaymentCode, Event event, Money amount, LocalDate dueDate) {
        super();
        setEvent(event);
        setPaymentCode(eventPaymentCode);
        setAmount(amount);
        setDueDate(dueDate);
    }

}

