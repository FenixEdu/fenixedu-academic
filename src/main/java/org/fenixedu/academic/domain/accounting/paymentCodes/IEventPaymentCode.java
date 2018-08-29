package org.fenixedu.academic.domain.accounting.paymentCodes;

import org.fenixedu.academic.domain.accounting.Event;

import java.util.Optional;

public interface IEventPaymentCode {
    Optional<Event> getEvent();
}
