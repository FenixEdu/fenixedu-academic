package org.fenixedu.academic.domain.accounting.paymentCodes;

import java.util.Optional;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.PaymentCodeType;
import org.fenixedu.academic.util.Money;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.ist.fenixframework.consistencyPredicates.ConsistencyPredicate;

public class EventPaymentCode extends EventPaymentCode_Base {

    public EventPaymentCode(Person creator, LocalDate startDate, LocalDate endDate, Money minAmount, Money
            maxAmount) {
        init(PaymentCodeType.EVENT, startDate.toDateMidnight().toYearMonthDay(), endDate.toDateTimeAtMidnight().toYearMonthDay(), minAmount,
                maxAmount, creator);
    }

    @ConsistencyPredicate
    public boolean checkEntries() {
        final long count = getEventPaymentCodeEntrySet().stream().map(EventPaymentCodeEntry::getEvent).distinct().count();
        return count == 0 || count == 1;
    }

    public boolean isInUsed() {
        return getEvent().isPresent();
    }

    public Optional<Event> getEvent() {
        return getEventPaymentCodeEntrySet().stream().map(EventPaymentCodeEntry::getEvent).findAny();
    }

    private void setStart(LocalDate start) {
        setStartDate(start.toDateMidnight().toYearMonthDay());
    }

    private void setEnd(LocalDate end) {
        setEndDate(end.toDateMidnight().toYearMonthDay());
    }

    public void edit(LocalDate start, LocalDate end) {
        setStart(start);
        setEnd(end);
    }

    @Override
    protected void internalProcess(Person person, Money amount, DateTime whenRegistered, String sibsTransactionId, String comments) {
//        event.process(person.getUser(), this, amount,
//                new SibsTransactionDetailDTO(whenRegistered, sibsTransactionId, getCode(), comments));
    }

    @Override
    public void delete() {
        setPaymentCodePool(null);
        super.delete();
    }
}
