package net.sourceforge.fenixedu.domain.accounting.events;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class InsuranceExemptionJustification extends InsuranceExemptionJustification_Base {

    public InsuranceExemptionJustification() {
        super();
    }

    public InsuranceExemptionJustification(InsuranceExemption exemption, InsuranceExemptionJustificationType justificationType,
            String reason) {
        this();
        init(exemption, justificationType, reason);
    }

    protected void init(InsuranceExemption exemption, InsuranceExemptionJustificationType justificationType, String reason) {
        super.init(exemption, reason);
        checkParameters(justificationType);
        super.setJustificationType(justificationType);
    }

    private void checkParameters(InsuranceExemptionJustificationType justificationType) {
        if (justificationType == null) {
            throw new DomainException("error.accounting.events.InsuranceExemptionJustification.justificationType.cannot.be.null");
        }

    }

    @Override
    public LabelFormatter getDescription() {
        final LabelFormatter labelFormatter = new LabelFormatter();
        labelFormatter.appendLabel(getJustificationType().getQualifiedName(), LabelFormatter.ENUMERATION_RESOURCES);

        return labelFormatter;
    }

}
