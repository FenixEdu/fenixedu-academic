package net.sourceforge.fenixedu.domain.candidacy;

public enum CandidacyOperationType {

    FILL_PERSONAL_DATA,

    REGISTRATION,

    PRINT_SCHEDULE,

    PRINT_REGISTRATION_DECLARATION,

    PRINT_SYSTEM_ACCESS_DATA,

    PRINT_UNDER_23_TRANSPORTS_DECLARATION,

    PRINT_MEASUREMENT_TEST_DATE,

    PRINT_ALL_DOCUMENTS,

    CANCEL,

    PRINT_GRATUITY_PAYMENT_CODES;

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
