package org.fenixedu.academic.domain.accounting.events;

import org.fenixedu.academic.domain.accounting.Exemption;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.LabelFormatter;
import org.joda.time.LocalDate;

public class EventExemptionJustification extends EventExemptionJustification_Base {
    
    private EventExemptionJustification() {
        super();
    }

    public EventExemptionJustification(final Exemption exemption,
            final EventExemptionJustificationType justificationType, final LocalDate dispatchDate, final String reason) {
        this();

        if (justificationType == null) {
            throw new DomainException("error.EventExemptionJustification.invalid.justificationType");
        }

        super.init(exemption, reason);
        setJustificationType(justificationType);
        setDispatchDate(dispatchDate);
    }

    @Override
    public LabelFormatter getDescription() {
        final LabelFormatter labelFormatter = new LabelFormatter();
        labelFormatter.appendLabel(getJustificationType().getQualifiedName(), Bundle.ENUMERATION);

        return labelFormatter;

    }
    
}
