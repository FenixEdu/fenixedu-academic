package net.sourceforge.fenixedu.domain.candidacyProcess;

public enum CandidacyProcessState {

    STAND_BY,

    SENT_TO_JURY,

    SENT_TO_COORDINATOR,

    SENT_TO_SCIENTIFIC_COUNCIL,

    PUBLISHED;

    public String getName() {
	return name();
    }

    public String getQualifiedName() {
	return CandidacyProcessState.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
	return CandidacyProcessState.class.getName() + "." + name();
    }
}
