package org.fenixedu.academic.domain.accounting.events;

import org.fenixedu.academic.domain.accounting.Exemption;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.LabelFormatter;
import org.fenixedu.bennu.core.signals.DomainObjectEvent;
import org.fenixedu.bennu.core.signals.Signal;
import org.joda.time.LocalDate;

public class EventExemptionJustification extends EventExemptionJustification_Base {

    public static final String SIGNAL_CREATED ="fenixedu.academic.eventExemptionJustification.created";

    private EventExemptionJustification() {
        super();
    }

    public EventExemptionJustification(final Exemption exemption,
            final EventExemptionJustificationType justificationType, final LocalDate dispatchDate, final String reason) {
        this();

        checkParameters(justificationType, dispatchDate);

        super.init(exemption, reason);
        setJustificationType(justificationType);
        setDispatchDate(dispatchDate);
        Signal.emit(SIGNAL_CREATED, new DomainObjectEvent<>(this));
    }

    private void checkParameters(EventExemptionJustificationType justificationType, LocalDate dispatchDate) {
        if (justificationType == null) {
            throw new DomainException("error.EventExemptionJustification.invalid.justificationType");
        }

        if (dispatchDate == null) {
            throw new DomainException("error.EventExemptionJustification.invalid.dispatchDate");
        }

        if (dispatchDate.isAfter(LocalDate.now())) {
            throw new DomainException("error.EventExemptionJustification.invalid.future.date");
        }
    }

    @Override
    public LabelFormatter getDescription() {
        final LabelFormatter labelFormatter = new LabelFormatter();
        labelFormatter.appendLabel(getJustificationType().getQualifiedName(), Bundle.ENUMERATION);

        return labelFormatter;

    }
    
}
