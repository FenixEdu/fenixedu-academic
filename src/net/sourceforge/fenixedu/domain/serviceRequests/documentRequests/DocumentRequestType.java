package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

public enum DocumentRequestType {

    SCHOOL_REGISTRATION_CERTIFICATE, ENROLMENT_CERTIFICATE, APPROVEMENT_CERTIFICATE, DEGREE_FINALIZATION_CERTIFICATE, SCHOOL_REGISTRATION_DECLARATION, ENROLMENT_DECLARATION, IRS_DECLARATION, DEGREE_DIPLOMA;

    public String getName() {
	return name();
    }

    public String getQualifiedName() {
	return DocumentRequestType.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
	return DocumentRequestType.class.getName() + "." + name();
    }

    public boolean isCertificate() {
	return this == SCHOOL_REGISTRATION_CERTIFICATE || this == ENROLMENT_CERTIFICATE
		|| this == APPROVEMENT_CERTIFICATE || this == DEGREE_FINALIZATION_CERTIFICATE;
    }

    public boolean isDeclaration() {
	return this == SCHOOL_REGISTRATION_DECLARATION || this == ENROLMENT_DECLARATION
		|| this == IRS_DECLARATION;
    }

}
