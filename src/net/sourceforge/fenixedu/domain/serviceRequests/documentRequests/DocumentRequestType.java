package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

public enum DocumentRequestType {

	DEGREE_FINALIZATION_CERTIFICATE, APPROVEMENT_CERTIFICATE, ENROLMENT_CERTIFICATE, SCHOOL_REGISTRATION_CERTIFICATE;

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
