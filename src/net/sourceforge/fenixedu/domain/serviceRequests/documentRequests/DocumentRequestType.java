package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

public enum DocumentRequestType {

    SCHOOL_REGISTRATION_CERTIFICATE(true), ENROLMENT_CERTIFICATE(true), APPROVEMENT_CERTIFICATE(true), DEGREE_FINALIZATION_CERTIFICATE(
	    true), SCHOOL_REGISTRATION_DECLARATION(false), ENROLMENT_DECLARATION(false), IRS_DECLARATION(
	    false), DEGREE_DIPLOMA(false);

    private boolean hasAdditionalInformation;

    private DocumentRequestType(boolean hasAdditionalInformation) {
	this.hasAdditionalInformation = hasAdditionalInformation;
    }

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

    public boolean getHasAdditionalInformation() {
	return hasAdditionalInformation;
    }

}
