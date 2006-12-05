package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;

public enum DocumentRequestType {

    SCHOOL_REGISTRATION_CERTIFICATE(true, AdministrativeOfficeType.DEGREE), ENROLMENT_CERTIFICATE(true,
	    AdministrativeOfficeType.DEGREE), APPROVEMENT_CERTIFICATE(true,
	    AdministrativeOfficeType.DEGREE), DEGREE_FINALIZATION_CERTIFICATE(true,
	    AdministrativeOfficeType.DEGREE), SCHOOL_REGISTRATION_DECLARATION(true,
	    AdministrativeOfficeType.DEGREE), ENROLMENT_DECLARATION(true,
	    AdministrativeOfficeType.DEGREE), IRS_DECLARATION(true, AdministrativeOfficeType.DEGREE), DEGREE_DIPLOMA(
	    false, AdministrativeOfficeType.DEGREE);

    private boolean hasAdditionalInformation;

    private AdministrativeOfficeType administrativeOfficeType;

    private DocumentRequestType(boolean hasAdditionalInformation,
	    AdministrativeOfficeType administrativeOfficeType) {
	this.hasAdditionalInformation = hasAdditionalInformation;
	this.administrativeOfficeType = administrativeOfficeType;
    }

    public String getName() {
	return name();
    }

    public AdministrativeOfficeType getAdministrativeOfficeType() {
	return administrativeOfficeType;
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
