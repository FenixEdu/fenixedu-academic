package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import java.util.Arrays;
import java.util.Collection;

import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;

public enum DocumentRequestType {

    SCHOOL_REGISTRATION_CERTIFICATE(true, false, AdministrativeOfficeType.DEGREE), 
    ENROLMENT_CERTIFICATE(true, false, AdministrativeOfficeType.DEGREE), 
    APPROVEMENT_CERTIFICATE(true, false, AdministrativeOfficeType.DEGREE), 
    DEGREE_FINALIZATION_CERTIFICATE(true, false, AdministrativeOfficeType.DEGREE), 
    SCHOOL_REGISTRATION_DECLARATION(false, true, AdministrativeOfficeType.DEGREE), 
    ENROLMENT_DECLARATION(false, true, AdministrativeOfficeType.DEGREE), 
    IRS_DECLARATION(true, true, AdministrativeOfficeType.DEGREE), 
    DIPLOMA_REQUEST(false, false, AdministrativeOfficeType.DEGREE, AdministrativeOfficeType.MASTER_DEGREE);

    private boolean hasAdditionalInformation;
    
    private Collection<AdministrativeOfficeType> administrativeOfficeTypes;
    
    private boolean allowedToQuickDeliver;

    private DocumentRequestType(boolean hasAdditionalInformation, boolean allowedToQuickDeliver, AdministrativeOfficeType ... administrativeOfficeTypes) {
	this.hasAdditionalInformation = hasAdditionalInformation;
	this.allowedToQuickDeliver = allowedToQuickDeliver;
	this.administrativeOfficeTypes = Arrays.asList(administrativeOfficeTypes);    
    }

    public String getName() {
	return name();
    }

    public Collection<AdministrativeOfficeType> getAdministrativeOfficeTypes() {
	return administrativeOfficeTypes;
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

    public boolean isDiploma() {
	return this == DIPLOMA_REQUEST;
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
