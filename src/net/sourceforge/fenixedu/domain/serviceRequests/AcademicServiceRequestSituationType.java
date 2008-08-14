package net.sourceforge.fenixedu.domain.serviceRequests;

public enum AcademicServiceRequestSituationType {

    NEW,

    PROCESSING,

    SENT_TO_EXTERNAL_ENTITY,

    RECEIVED_FROM_EXTERNAL_ENTITY,

    CONCLUDED,

    DELIVERED,

    REJECTED,

    CANCELLED;

    public String getName() {
	return name();
    }

    public String getQualifiedName() {
	return AcademicServiceRequestSituationType.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
	return AcademicServiceRequestSituationType.class.getName() + "." + name();
    }

}
