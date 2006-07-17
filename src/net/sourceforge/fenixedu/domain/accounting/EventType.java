package net.sourceforge.fenixedu.domain.accounting;

public enum EventType {

    CANDIDACY_ENROLMENT,

    SCHOOL_REGISTRATION_CERTIFICATE_REQUEST,

    ENROLMENT_CERTIFICATE_REQUEST,

    APPROVEMENT_CERTIFICATE_REQUEST,

    DEGREE_FINALIZATION_CERTIFICATE_REQUEST;

    public String getName() {
        return name();
    }
}
