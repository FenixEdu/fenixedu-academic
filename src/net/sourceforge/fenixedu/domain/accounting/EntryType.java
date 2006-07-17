package net.sourceforge.fenixedu.domain.accounting;

public enum EntryType {

    TRANSFER,

    ADJUSTMENT,

    CANDIDACY_ENROLMENT_FEE,

    SCHOOL_REGISTRATION_CERTIFICATE_REQUEST_FEE,

    ENROLMENT_CERTIFICATE_REQUEST_FEE,

    APPROVEMENT_CERTIFICATE_REQUEST_FEE,

    DEGREE_FINALIZATION_CERTIFICATE_REQUEST_FEE;

    public String getName() {
        return name();
    }
}
