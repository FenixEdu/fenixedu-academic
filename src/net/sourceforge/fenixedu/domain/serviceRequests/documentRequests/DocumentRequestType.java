package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;

public enum DocumentRequestType {

    SCHOOL_REGISTRATION_CERTIFICATE(true, false, AdministrativeOfficeType.DEGREE, AdministrativeOfficeType.MASTER_DEGREE),

    ENROLMENT_CERTIFICATE(true, false, AdministrativeOfficeType.DEGREE, AdministrativeOfficeType.MASTER_DEGREE),

    APPROVEMENT_CERTIFICATE(true, false, AdministrativeOfficeType.DEGREE, AdministrativeOfficeType.MASTER_DEGREE),

    DEGREE_FINALIZATION_CERTIFICATE(true, false, AdministrativeOfficeType.DEGREE, AdministrativeOfficeType.MASTER_DEGREE),

    EXAM_DATE_CERTIFICATE(true, false, AdministrativeOfficeType.DEGREE),

    SCHOOL_REGISTRATION_DECLARATION(false, true, AdministrativeOfficeType.DEGREE, AdministrativeOfficeType.MASTER_DEGREE),

    ENROLMENT_DECLARATION(false, true, AdministrativeOfficeType.DEGREE, AdministrativeOfficeType.MASTER_DEGREE),

    IRS_DECLARATION(true, true, AdministrativeOfficeType.DEGREE, AdministrativeOfficeType.MASTER_DEGREE),

    DIPLOMA_REQUEST(false, false, AdministrativeOfficeType.DEGREE, AdministrativeOfficeType.MASTER_DEGREE),

    PHOTOCOPY(false, false),

    COURSE_LOAD(true, false, AdministrativeOfficeType.DEGREE),

    EXTERNAL_COURSE_LOAD(true, false /* , AdministrativeOfficeType.DEGREE */),

    PROGRAM_CERTIFICATE(true, false, AdministrativeOfficeType.DEGREE),

    EXTERNAL_PROGRAM_CERTIFICATE(true, false /* , AdministrativeOfficeType.DEGREE */);

    private boolean hasAdditionalInformation;

    private Collection<AdministrativeOfficeType> administrativeOfficeTypes;

    private boolean allowedToQuickDeliver;

    static private List<DocumentRequestType> CERTIFICATES = Arrays.asList(SCHOOL_REGISTRATION_CERTIFICATE, ENROLMENT_CERTIFICATE,
	    APPROVEMENT_CERTIFICATE, DEGREE_FINALIZATION_CERTIFICATE, EXAM_DATE_CERTIFICATE, COURSE_LOAD, EXTERNAL_COURSE_LOAD,
	    PROGRAM_CERTIFICATE, EXTERNAL_PROGRAM_CERTIFICATE);

    static private List<DocumentRequestType> DECLARATIONS = Arrays.asList(SCHOOL_REGISTRATION_DECLARATION, ENROLMENT_DECLARATION,
	    IRS_DECLARATION);

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
	return CERTIFICATES.contains(this);
    }

    public boolean isDeclaration() {
	return DECLARATIONS.contains(this);
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
