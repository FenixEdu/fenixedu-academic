package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;

public enum DocumentRequestType {

    SCHOOL_REGISTRATION_CERTIFICATE(true, AdministrativeOfficeType.DEGREE, false), 
    ENROLMENT_CERTIFICATE(true, AdministrativeOfficeType.DEGREE, false), 
    APPROVEMENT_CERTIFICATE(true, AdministrativeOfficeType.DEGREE, false), 
    DEGREE_FINALIZATION_CERTIFICATE(true, AdministrativeOfficeType.DEGREE, false), 
    SCHOOL_REGISTRATION_DECLARATION(false, AdministrativeOfficeType.DEGREE, true), 
    ENROLMENT_DECLARATION(false, AdministrativeOfficeType.DEGREE, true), 
    IRS_DECLARATION(true, AdministrativeOfficeType.DEGREE, true), 
    DIPLOMA_REQUEST(false, AdministrativeOfficeType.DEGREE, false);

    private boolean hasAdditionalInformation;
    
    private AdministrativeOfficeType administrativeOfficeType;
    
    private boolean allowedToQuickDeliver;

    private DocumentRequestType(boolean hasAdditionalInformation,
	    AdministrativeOfficeType administrativeOfficeType, boolean allowedToQuickDeliver) {
	this.hasAdditionalInformation = hasAdditionalInformation;
	this.administrativeOfficeType = administrativeOfficeType;
	this.allowedToQuickDeliver = allowedToQuickDeliver;
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

    public boolean isAllowedToQuickDeliver() {
        return allowedToQuickDeliver;
    }

    public void setAllowedToQuickDeliver(boolean allowedToQuickDeliver) {
        this.allowedToQuickDeliver = allowedToQuickDeliver;
    }

    public boolean getCanBeFreeProcessed() {
	return isDeclaration();
    }

}
