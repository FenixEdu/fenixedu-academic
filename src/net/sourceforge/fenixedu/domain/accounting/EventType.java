package net.sourceforge.fenixedu.domain.accounting;

public enum EventType {

    CANDIDACY_ENROLMENT,

    SCHOOL_REGISTRATION_CERTIFICATE_REQUEST,

    ENROLMENT_CERTIFICATE_REQUEST,

    APPROVEMENT_CERTIFICATE_REQUEST,

    DEGREE_FINALIZATION_CERTIFICATE_REQUEST,

    GRATUITY;

    public String getName() {
	return name();
    }

    public String getQualifiedName() {
	return EventType.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
	return EventType.class.getName() + "." + name();
    }
}
