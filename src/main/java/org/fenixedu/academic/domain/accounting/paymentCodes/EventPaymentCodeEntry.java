package org.fenixedu.academic.domain.accounting.paymentCodes;

import org.fenixedu.academic.FenixEduAcademicConfiguration;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;
import pt.ist.fenixframework.Atomic;

/***
 * This is used to register the promise of a payment by an event owner
 */
public class EventPaymentCodeEntry extends EventPaymentCodeEntry_Base {
    
    protected EventPaymentCodeEntry() {
        super();
        setCreated(new DateTime());
    }

    protected EventPaymentCodeEntry(EventPaymentCode eventPaymentCode, Event event, Money amount) {
        this();
        setEvent(event);
        setPaymentCode(eventPaymentCode);
        setAmount(amount);
        setDueDate(getCreated().plusDays(FenixEduAcademicConfiguration.getConfiguration().getMaxDaysBetweenPromiseAndPayment()).toLocalDate());
    }

    @Atomic(mode = Atomic.TxMode.WRITE)
    public static EventPaymentCodeEntry create(Event event, Money amount) {
        final EventPaymentCodeEntry eventPaymentCodeEntry = new EventPaymentCodeEntry(Bennu.getInstance().getPaymentCodePool().getAvailablePaymentCode(), event, amount);
        eventPaymentCodeEntry.getPaymentCode().setPerson(event.getPerson());
        return eventPaymentCodeEntry;
    }

    public static EventPaymentCodeEntry getOrCreate(Event event, Money amount){
        return event.getAvailablePaymentCodeEntry().orElseGet(() -> create(event, amount));
    }

}

