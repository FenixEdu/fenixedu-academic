package net.sourceforge.fenixedu.domain.candidacy;

public enum CandidacyOperationType {

    FILL_PERSONAL_DATA,

    REGISTRATION,

    PRINT_SCHEDULE,

    PRINT_REGISTRATION_DECLARATION,

    PRINT_SYSTEM_ACCESS_DATA,

    CANCEL;

    public String getName() {
	return name();
    }

    public String getQualifiedName() {
	return CandidacyOperationType.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
	return CandidacyOperationType.class.getName() + "." + name();
    }
}
