package net.sourceforge.fenixedu.dataTransferObject.accounting;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.accounting.events.candidacy.CandidacyExemptionJustificationType;
import net.sourceforge.fenixedu.domain.accounting.events.candidacy.SecondCycleIndividualCandidacyEvent;

public class SecondCycleIndividualCandidacyExemptionBean implements Serializable {

	private SecondCycleIndividualCandidacyEvent event;

	private CandidacyExemptionJustificationType justificationType;

	public SecondCycleIndividualCandidacyExemptionBean(final SecondCycleIndividualCandidacyEvent event) {
		setEvent(event);
	}

	public SecondCycleIndividualCandidacyEvent getEvent() {
		return this.event;
	}

	public void setEvent(SecondCycleIndividualCandidacyEvent event) {
		this.event = event;
	}

	public CandidacyExemptionJustificationType getJustificationType() {
		return justificationType;
	}

	public void setJustificationType(CandidacyExemptionJustificationType justificationType) {
		this.justificationType = justificationType;
	}

}
