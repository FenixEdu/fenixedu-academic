package net.sourceforge.fenixedu.domain.accounting.events.candidacy;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class SecondCycleIndividualCandidacyExemptionJustification extends
	SecondCycleIndividualCandidacyExemptionJustification_Base {

    private SecondCycleIndividualCandidacyExemptionJustification() {
	super();
    }

    SecondCycleIndividualCandidacyExemptionJustification(final SecondCycleIndividualCandidacyExemption exemption,
	    final CandidacyExemptionJustificationType justificationType) {
	this();
	checkParameters(justificationType);
	super.init(exemption, "");
	setCandidacyExemptionJustificationType(justificationType);
    }

    private void checkParameters(final CandidacyExemptionJustificationType justificationType) {
	if (justificationType == null) {
	    throw new DomainException("error.SecondCycleIndividualCandidacyExemptionJustification.invalid.justificationType");
	}
    }

    @Override
    public LabelFormatter getDescription() {
	return new LabelFormatter().appendLabel(getCandidacyExemptionJustificationType().getQualifiedName(),
		LabelFormatter.ENUMERATION_RESOURCES);
    }

}
