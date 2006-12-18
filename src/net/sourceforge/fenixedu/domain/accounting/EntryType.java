package net.sourceforge.fenixedu.domain.accounting;

public enum EntryType {

    TRANSFER,

    ADJUSTMENT,

    CANDIDACY_ENROLMENT_FEE,

    SCHOOL_REGISTRATION_CERTIFICATE_REQUEST_FEE,

    ENROLMENT_CERTIFICATE_REQUEST_FEE,

    APPROVEMENT_CERTIFICATE_REQUEST_FEE,

    DEGREE_FINALIZATION_CERTIFICATE_REQUEST_FEE,

    SCHOOL_REGISTRATION_DECLARATION_REQUEST_FEE,

    ENROLMENT_DECLARATION_REQUEST_FEE,

    GRATUITY_FEE,

    REGISTRATION_FEE,

    INSURANCE_FEE,

    ADMINISTRATIVE_OFFICE_FEE;

    public String getName() {
	return name();
    }

    public String getQualifiedName() {
	return EntryType.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
	return EntryType.class.getName() + "." + name();
    }
}
