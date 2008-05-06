package net.sourceforge.fenixedu.domain.candidacyProcess;

public enum IndividualCandidacyState {

    STAND_BY,

    ACCEPTED,

    REJECTED,

    CANCELLED;

    public String getName() {
	return name();
    }

    public String getQualifiedName() {
	return IndividualCandidacyState.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
	return IndividualCandidacyState.class.getName() + "." + name();
    }
}
