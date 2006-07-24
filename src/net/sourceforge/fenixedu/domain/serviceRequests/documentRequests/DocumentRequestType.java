package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

public enum DocumentRequestType {

    SCHOOL_REGISTRATION_CERTIFICATE, ENROLMENT_CERTIFICATE, APPROVEMENT_CERTIFICATE, DEGREE_FINALIZATION_CERTIFICATE;

	public String getName() {
		return name();
	}

	public String getQualifiedName() {
		return DocumentRequestType.class.getSimpleName() + "." + name();
	}

	public String getFullyQualifiedName() {
		return DocumentRequestType.class.getName() + "." + name();
	}
}
