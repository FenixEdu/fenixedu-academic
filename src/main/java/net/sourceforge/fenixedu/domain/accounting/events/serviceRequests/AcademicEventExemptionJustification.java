package net.sourceforge.fenixedu.domain.accounting.events.serviceRequests;

import net.sourceforge.fenixedu.domain.accounting.events.AcademicEventExemption;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class AcademicEventExemptionJustification extends AcademicEventExemptionJustification_Base {

    private AcademicEventExemptionJustification() {
        super();
    }

    public AcademicEventExemptionJustification(final AcademicEventExemption exemption,
            final AcademicEventJustificationType justificationType, final LocalDate dispatchDate, final String reason) {

        this();
        String[] args = {};

        if (justificationType == null) {
            throw new DomainException("error.AcademicEventExemptionJustification.invalid.justificationType", args);
        }
        String[] args1 = {};
        if (dispatchDate == null) {
            throw new DomainException("error.AcademicEventExemptionJustification.invalid.dispatchDate", args1);
        }

        super.init(exemption, reason);
        setJustificationType(justificationType);
        setDispatchDate(dispatchDate);
    }

    @Override
    public LabelFormatter getDescription() {
        return new LabelFormatter().appendLabel(getJustificationType().getQualifiedName(), LabelFormatter.ENUMERATION_RESOURCES);
    }

    @Deprecated
    public boolean hasJustificationType() {
        return getJustificationType() != null;
    }

    @Deprecated
    public boolean hasDispatchDate() {
        return getDispatchDate() != null;
    }

}
