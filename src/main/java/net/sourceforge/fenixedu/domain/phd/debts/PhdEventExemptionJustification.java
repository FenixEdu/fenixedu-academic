package net.sourceforge.fenixedu.domain.phd.debts;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class PhdEventExemptionJustification extends PhdEventExemptionJustification_Base {

    private PhdEventExemptionJustification() {
        super();
    }

    public PhdEventExemptionJustification(PhdEventExemption exemption, PhdEventExemptionJustificationType justificationType,
            LocalDate dispatchDate, String reason) {

        this();
        String[] args = {};

        if (justificationType == null) {
            throw new DomainException("error.PhdEventExemptionJustificationType.invalid.justification.type", args);
        }
        String[] args1 = {};
        if (dispatchDate == null) {
            throw new DomainException("error.PhdEventExemptionJustificationType.invalid.dispatch.date", args1);
        }

        init(exemption, reason);
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
