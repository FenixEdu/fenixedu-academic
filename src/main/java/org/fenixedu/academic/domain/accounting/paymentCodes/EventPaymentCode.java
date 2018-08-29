package org.fenixedu.academic.domain.accounting.paymentCodes;

import java.util.Optional;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.PaymentCodeType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.accounting.SibsTransactionDetailDTO;
import org.fenixedu.academic.util.Money;
import org.fenixedu.academic.util.sibs.incomming.SibsIncommingPaymentFileDetailLine;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.ist.fenixframework.consistencyPredicates.ConsistencyPredicate;

public class EventPaymentCode extends EventPaymentCode_Base implements IEventPaymentCode {

    public EventPaymentCode(Person creator, LocalDate startDate, LocalDate endDate, Money minAmount, Money
            maxAmount) {
        init(PaymentCodeType.EVENT, startDate.toDateMidnight().toYearMonthDay(), endDate.toDateTimeAtMidnight().toYearMonthDay(), minAmount,
                maxAmount, creator);
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
    public void delete() {
        setPaymentCodePool(null);
        super.delete();
    }

    @Override protected void internalProcess(Person person, SibsIncommingPaymentFileDetailLine sibsDetailLine) {
        final Event event = getEvent().orElseThrow(() -> new DomainException("invalid.payment.code.missing.event"));
        event.process(person.getUser(), this, sibsDetailLine.getAmount(), new SibsTransactionDetailDTO(sibsDetailLine, sibsDetailLine.getWhenOccuredTransaction(), sibsDetailLine.getSibsTransactionId(), getCode(), ""));
    }
}
