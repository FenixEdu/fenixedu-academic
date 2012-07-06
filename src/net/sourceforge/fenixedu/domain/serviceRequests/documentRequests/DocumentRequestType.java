package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import static net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType.DEGREE;
import static net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType.MASTER_DEGREE;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;

public enum DocumentRequestType {

    SCHOOL_REGISTRATION_CERTIFICATE(true, false, DEGREE, MASTER_DEGREE),

    ENROLMENT_CERTIFICATE(true, false, DEGREE, MASTER_DEGREE),

    APPROVEMENT_CERTIFICATE(true, false, DEGREE, MASTER_DEGREE),

    APPROVEMENT_MOBILITY_CERTIFICATE(true, false, DEGREE, MASTER_DEGREE),

    DEGREE_FINALIZATION_CERTIFICATE(true, false, true, DEGREE, MASTER_DEGREE),

    PHD_FINALIZATION_CERTIFICATE(false, false, false, MASTER_DEGREE),

    EXAM_DATE_CERTIFICATE(true, false, DEGREE),

    SCHOOL_REGISTRATION_DECLARATION(false, true, DEGREE, MASTER_DEGREE),

    ENROLMENT_DECLARATION(false, true, DEGREE, MASTER_DEGREE),

    IRS_DECLARATION(true, true, DEGREE, MASTER_DEGREE),

    GENERIC_DECLARATION(true, true, DEGREE),

    REGISTRY_DIPLOMA_REQUEST(true, false, DEGREE, MASTER_DEGREE),

    DIPLOMA_REQUEST(false, false, DEGREE, MASTER_DEGREE),

    DIPLOMA_SUPPLEMENT_REQUEST(true, false, false, false, true, DEGREE, MASTER_DEGREE),

    PAST_DIPLOMA_REQUEST(true, false, false, true, false, DEGREE/*
								 * ,
								 * MASTER_DEGREE
								 */),

    PHOTOCOPY(false, false, DEGREE, MASTER_DEGREE),

    COURSE_LOAD(true, false, DEGREE, MASTER_DEGREE),

    EXTERNAL_COURSE_LOAD(true, false, DEGREE),

    PROGRAM_CERTIFICATE(true, false, DEGREE, MASTER_DEGREE),

    EXTERNAL_PROGRAM_CERTIFICATE(true, false, DEGREE),

    EXTRA_CURRICULAR_CERTIFICATE(true, false, DEGREE),

    UNDER_23_TRANSPORTS_REQUEST(false, false, DEGREE),

    STANDALONE_ENROLMENT_CERTIFICATE(true, false, DEGREE);

    private boolean hasAdditionalInformation;

    private boolean allowedToQuickDeliver;

    private boolean withBranch;

    private boolean preBolonha;

    private boolean bolonhaOnly;

    private Collection<AdministrativeOfficeType> administrativeOfficeTypes;

    static private List<DocumentRequestType> CERTIFICATES = Arrays.asList(SCHOOL_REGISTRATION_CERTIFICATE, ENROLMENT_CERTIFICATE,
	    APPROVEMENT_CERTIFICATE, APPROVEMENT_MOBILITY_CERTIFICATE, DEGREE_FINALIZATION_CERTIFICATE, EXAM_DATE_CERTIFICATE,
	    COURSE_LOAD, EXTERNAL_COURSE_LOAD, PROGRAM_CERTIFICATE, EXTERNAL_PROGRAM_CERTIFICATE, EXTRA_CURRICULAR_CERTIFICATE,
	    STANDALONE_ENROLMENT_CERTIFICATE);

    static private List<DocumentRequestType> DECLARATIONS = Arrays.asList(SCHOOL_REGISTRATION_DECLARATION, ENROLMENT_DECLARATION,
	    IRS_DECLARATION, GENERIC_DECLARATION);

    private DocumentRequestType(boolean hasAdditionalInformation, boolean allowedToQuickDeliver, boolean withBranch,
	    boolean preBolonha, boolean bolonhaOnly, AdministrativeOfficeType... administrativeOfficeTypes) {
	this.hasAdditionalInformation = hasAdditionalInformation;
	this.allowedToQuickDeliver = allowedToQuickDeliver;
	this.withBranch = withBranch;
	this.preBolonha = preBolonha;
	this.bolonhaOnly = bolonhaOnly;
	this.administrativeOfficeTypes = Arrays.asList(administrativeOfficeTypes);
    }

    private DocumentRequestType(boolean hasAdditionalInformation, boolean allowedToQuickDeliver, boolean withBranch,
	    AdministrativeOfficeType... administrativeOfficeTypes) {
	this(hasAdditionalInformation, allowedToQuickDeliver, withBranch, false, false, administrativeOfficeTypes);
    }

    private DocumentRequestType(boolean hasAdditionalInformation, boolean allowedToQuickDeliver,
	    AdministrativeOfficeType... administrativeOfficeTypes) {
	this(hasAdditionalInformation, allowedToQuickDeliver, false, false, false, administrativeOfficeTypes);
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

    public boolean isDiplomaSupplement() {
	return this == DIPLOMA_SUPPLEMENT_REQUEST;
    }

    public boolean isDiploma() {
	return this == DIPLOMA_REQUEST;
    }

    public boolean isRegistryDiploma() {
	return this == REGISTRY_DIPLOMA_REQUEST;
    }

    public boolean isPastDiploma() {
	return this == PAST_DIPLOMA_REQUEST;
    }

    final public boolean getHasAdditionalInformation() {
	return hasAdditionalInformation;
    }

    final public boolean getHasCycleTypeDependency(final DegreeType degreeType) {
	return degreeType.getCycleTypes().size() > 1;
    }

    public boolean isAllowedToQuickDeliver() {
	return allowedToQuickDeliver;
    }

    public boolean isPreBolonha() {
	return preBolonha;
    }

    public boolean isBolonhaOnly() {
	return bolonhaOnly;
    }

    public boolean withBranch() {
	return withBranch;
    }

    public boolean getCanBeFreeProcessed() {
	return isDeclaration() || this == SCHOOL_REGISTRATION_CERTIFICATE || this == DEGREE_FINALIZATION_CERTIFICATE;
    }

}
