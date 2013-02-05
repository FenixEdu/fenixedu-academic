package net.sourceforge.fenixedu.domain.accounting.events.serviceRequests;

import net.sourceforge.fenixedu.domain.accounting.events.AcademicEventExemption;

import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class AcademicEventExemptionJustification extends AcademicEventExemptionJustification_Base {

    private AcademicEventExemptionJustification() {
        super();
    }

    public AcademicEventExemptionJustification(final AcademicEventExemption exemption,
            final AcademicEventJustificationType justificationType, final LocalDate dispatchDate, final String reason) {

        this();

        check(justificationType, "error.AcademicEventExemptionJustification.invalid.justificationType");
        check(dispatchDate, "error.AcademicEventExemptionJustification.invalid.dispatchDate");

        super.init(exemption, reason);
        setJustificationType(justificationType);
        setDispatchDate(dispatchDate);
    }

    @Override
    public LabelFormatter getDescription() {
        return new LabelFormatter().appendLabel(getJustificationType().getQualifiedName(), LabelFormatter.ENUMERATION_RESOURCES);
    }

}
