package net.sourceforge.fenixedu.domain.accounting.events.candidacy;


public enum CandidacyExemptionJustificationType {
    TIME;

    public String getName() {
	return name();
    }

    public String getQualifiedName() {
	return getClass().getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
	return getClass().getName() + "." + name();
    }
}
