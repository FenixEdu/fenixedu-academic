package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import java.util.Arrays;
import java.util.Collection;

import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;

public enum DocumentRequestType {

    SCHOOL_REGISTRATION_CERTIFICATE(true, false, AdministrativeOfficeType.DEGREE, AdministrativeOfficeType.MASTER_DEGREE),

    ENROLMENT_CERTIFICATE(true, false, AdministrativeOfficeType.DEGREE, AdministrativeOfficeType.MASTER_DEGREE),

    APPROVEMENT_CERTIFICATE(true, false, AdministrativeOfficeType.DEGREE, AdministrativeOfficeType.MASTER_DEGREE),

    DEGREE_FINALIZATION_CERTIFICATE(true, false, AdministrativeOfficeType.DEGREE, AdministrativeOfficeType.MASTER_DEGREE),

    SCHOOL_REGISTRATION_DECLARATION(false, true, AdministrativeOfficeType.DEGREE, AdministrativeOfficeType.MASTER_DEGREE),

    ENROLMENT_DECLARATION(false, true, AdministrativeOfficeType.DEGREE, AdministrativeOfficeType.MASTER_DEGREE),

    IRS_DECLARATION(true, true, AdministrativeOfficeType.DEGREE, AdministrativeOfficeType.MASTER_DEGREE),

    DIPLOMA_REQUEST(false, false, AdministrativeOfficeType.DEGREE, AdministrativeOfficeType.MASTER_DEGREE),

    PHOTOCOPY(false, false),

    COURSE_LOAD(false, false/* , AdministrativeOfficeType.DEGREE */),

    EXAM_DATE_CERTIFICATE(true, false /* ,AdministrativeOfficeType.DEGREE */);

    private boolean hasAdditionalInformation;

    private Collection<AdministrativeOfficeType> administrativeOfficeTypes;

    private boolean allowedToQuickDeliver;

    private DocumentRequestType(boolean hasAdditionalInformation, boolean allowedToQuickDeliver,
	    AdministrativeOfficeType... administrativeOfficeTypes) {
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
	return this == SCHOOL_REGISTRATION_CERTIFICATE || this == ENROLMENT_CERTIFICATE || this == APPROVEMENT_CERTIFICATE
		|| this == DEGREE_FINALIZATION_CERTIFICATE || this == EXAM_DATE_CERTIFICATE;
    }

    public boolean isDeclaration() {
	return this == SCHOOL_REGISTRATION_DECLARATION || this == ENROLMENT_DECLARATION || this == IRS_DECLARATION;
    }

    public boolean isDiploma() {
	return this == DIPLOMA_REQUEST;
    }

    final public boolean getHasAdditionalInformation() {
	return hasAdditionalInformation;
    }

    final public boolean getHasCycleTypeDependency(final DegreeType degreeType) {
	return degreeType.isComposite() && (this == DEGREE_FINALIZATION_CERTIFICATE || this == DIPLOMA_REQUEST);
    }

    public boolean isAllowedToQuickDeliver() {
	return allowedToQuickDeliver;
    }

    public void setAllowedToQuickDeliver(boolean allowedToQuickDeliver) {
	this.allowedToQuickDeliver = allowedToQuickDeliver;
    }

    public boolean getCanBeFreeProcessed() {
	return isDeclaration() || this == DEGREE_FINALIZATION_CERTIFICATE;
    }

}
