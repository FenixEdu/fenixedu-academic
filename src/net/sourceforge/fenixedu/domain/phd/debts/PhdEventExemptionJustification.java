package net.sourceforge.fenixedu.domain.phd.debts;

import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class PhdEventExemptionJustification extends PhdEventExemptionJustification_Base {

    private PhdEventExemptionJustification() {
        super();
    }

    public PhdEventExemptionJustification(PhdEventExemption exemption, PhdEventExemptionJustificationType justificationType,
            LocalDate dispatchDate, String reason) {

        this();

        check(justificationType, "error.PhdEventExemptionJustificationType.invalid.justification.type");
        check(dispatchDate, "error.PhdEventExemptionJustificationType.invalid.dispatch.date");

        init(exemption, reason);
        setJustificationType(justificationType);
        setDispatchDate(dispatchDate);

    }

    @Override
    public LabelFormatter getDescription() {
        return new LabelFormatter().appendLabel(getJustificationType().getQualifiedName(), LabelFormatter.ENUMERATION_RESOURCES);
    }

}
