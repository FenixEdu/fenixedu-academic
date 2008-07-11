package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Map.Entry;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.OptionalEnrolment;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accounting.PostingRule;
import net.sourceforge.fenixedu.domain.accounting.postingRules.serviceRequests.CertificateRequestPR;
import net.sourceforge.fenixedu.domain.accounting.serviceAgreementTemplates.AdministrativeOfficeServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.CertificateRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.CourseLoadRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.ExternalCourseLoadRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.ExternalProgramCertificateRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.ProgramCertificateRequest;
import net.sourceforge.fenixedu.domain.student.MobilityProgram;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.curriculum.ICurriculum;
import net.sourceforge.fenixedu.domain.student.curriculum.ICurriculumEntry;
import net.sourceforge.fenixedu.domain.studentCurriculum.ExternalEnrolment;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.docs.FenixReport;
import net.sourceforge.fenixedu.util.Money;
import net.sourceforge.fenixedu.util.StringUtils;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public class AdministrativeOfficeDocument extends FenixReport {

    static final protected int LINE_LENGTH = 64;

    static final protected int SUFFIX_LENGTH = 12;

    static final protected String[] identifiers = { "*", "#", "+", "**", "***" };

    protected DomainReference<DocumentRequest> documentRequestDomainReference;

    public static class AdministrativeOfficeDocumentCreator {

	public static AdministrativeOfficeDocument create(final DocumentRequest documentRequest) {
	    switch (documentRequest.getDocumentRequestType()) {
	    case ENROLMENT_CERTIFICATE:
		return new EnrolmentCertificate(documentRequest);
	    case APPROVEMENT_CERTIFICATE:
		return new ApprovementCertificate(documentRequest);
	    case DEGREE_FINALIZATION_CERTIFICATE:
		return new DegreeFinalizationCertificate(documentRequest);
	    case SCHOOL_REGISTRATION_DECLARATION:
		return new RegistrationDeclaration(documentRequest);
	    case ENROLMENT_DECLARATION:
		return new EnrolmentDeclaration(documentRequest);
	    case IRS_DECLARATION:
		return new IRSDeclaration(documentRequest);
	    case DIPLOMA_REQUEST:
		return new Diploma(documentRequest);
	    case EXAM_DATE_CERTIFICATE:
		return new ExamDateCertificate(documentRequest);
	    case COURSE_LOAD:
		return new CourseLoadRequestDocument((CourseLoadRequest) documentRequest);
	    case EXTERNAL_COURSE_LOAD:
		return new ExternalCourseLoadRequestDocument((ExternalCourseLoadRequest) documentRequest);
	    case PROGRAM_CERTIFICATE:
		return new ProgramCertificateRequestDocument((ProgramCertificateRequest) documentRequest);
	    case EXTERNAL_PROGRAM_CERTIFICATE:
		return new ExternalProgramCertificateRequestDocument((ExternalProgramCertificateRequest) documentRequest);
	    default:
		return new AdministrativeOfficeDocument(documentRequest);
	    }
	}

    }

    protected AdministrativeOfficeDocument() {
	super();
    }

    protected AdministrativeOfficeDocument(final DocumentRequest documentRequest) {
	super(new ArrayList());
	this.resourceBundle = ResourceBundle.getBundle("resources.AcademicAdminOffice", Language.getLocale());
	this.documentRequestDomainReference = new DomainReference<DocumentRequest>(documentRequest);

	fillReport();
    }

    protected DocumentRequest getDocumentRequest() {
	return documentRequestDomainReference.getObject();
    }

    @Override
    public String getReportTemplateKey() {
	return getDocumentRequest().getDocumentTemplateKey();
    }

    @Override
    public String getReportFileName() {
	final StringBuilder result = new StringBuilder();

	result.append(getDocumentRequest().getRegistration().getPerson().getIstUsername());
	result.append("-");
	result.append(new DateTime().toString(DateTimeFormat.forPattern("yyyyMMdd")));
	result.append("-");
	result.append(getDocumentRequest().getDescription().replace(":", "").replace(" ", ""));

	return result.toString();
    }

    @Override
    protected void fillReport() {
	addParameter("documentRequest", getDocumentRequest());
	addParameter("registration", getDocumentRequest().getRegistration());

	if (showPriceFields()) {
	    setPriceFields();
	}

	setIntroFields(AccessControl.getPerson().getEmployee());
	setPersonFields();

	if (getDocumentRequest().hasExecutionYear()) {
	    addParameter("situation", getDocumentRequest().getExecutionYear().containsDate(new DateTime()) ? "ESTÁ" : "ESTEVE");
	}
	addParameter("degreeDescription", getDegreeDescription());

	addParameter("employeeLocation", AccessControl.getPerson().getEmployee().getCurrentCampus().getLocation());
	addParameter("day", new LocalDate().toString("dd 'de' MMMM 'de' yyyy", Language.getLocale()));
    }

    protected boolean showPriceFields() {
	return getDocumentRequest().isCertificate();
    }

    final private void setPriceFields() {
	final CertificateRequest certificateRequest = (CertificateRequest) getDocumentRequest();
	final CertificateRequestPR certificateRequestPR = (CertificateRequestPR) getPostingRule();

	final Money amountPerPage = certificateRequestPR.getAmountPerPage();
	final Money baseAmountPlusAmountForUnits = certificateRequestPR.getBaseAmount().add(
		certificateRequestPR.getAmountPerUnit().multiply(new BigDecimal(certificateRequest.getNumberOfUnits())));
	final Money urgencyAmount = certificateRequest.getUrgentRequest() ? certificateRequestPR.getBaseAmount() : Money.ZERO;

	addParameter("amountPerPage", amountPerPage);
	addParameter("baseAmountPlusAmountForUnits", baseAmountPlusAmountForUnits);
	addParameter("urgencyAmount", urgencyAmount);
	addParameter("printPriceFields", printPriceFields(certificateRequest));
    }

    final private boolean printPriceFields(final CertificateRequest certificateRequest) {
	return (certificateRequest.getAcademicServiceRequestSituationType() == AcademicServiceRequestSituationType.PROCESSING && !certificateRequest
		.isFree())
		|| certificateRequest.hasEvent();
    }

    final private PostingRule getPostingRule() {
	final AdministrativeOfficeServiceAgreementTemplate serviceAgreementTemplate = getDocumentRequest()
		.getAdministrativeOffice().getServiceAgreementTemplate();
	return serviceAgreementTemplate.findPostingRuleByEventType(getDocumentRequest().getEventType());
    }

    final protected void setIntroFields(final Employee employee) {
	addParameter("administrativeOfficeCoordinator", employee.getCurrentWorkingPlace().getActiveUnitCoordinator());
	addParameter("administrativeOfficeName", employee.getCurrentWorkingPlace().getName());

	addParameter("institutionName", RootDomainObject.getInstance().getInstitutionUnit().getName());
	addParameter("universityName", UniversityUnit.getInstitutionsUniversityUnit().getName());
    }

    protected void setPersonFields() {
	final Person person = getDocumentRequest().getRegistration().getPerson();
	final String name = person.getName().toUpperCase();
	addParameter("name", StringUtils.multipleLineRightPad(name, LINE_LENGTH, '-'));

	final String documentIdNumber = person.getDocumentIdNumber();
	addParameter("documentIdNumber", StringUtils.multipleLineRightPad("portador" + (person.isMale() ? "" : "a") + " do "
		+ person.getIdDocumentType().getLocalizedName() + " Nº " + documentIdNumber, LINE_LENGTH, '-'));

	final String birthLocale = person.getParishOfBirth().toUpperCase() + ", "
		+ person.getDistrictSubdivisionOfBirth().toUpperCase();
	addParameter("birthLocale", StringUtils.multipleLineRightPad("natural de " + birthLocale, LINE_LENGTH, '-'));

	final String nationality = person.getCountry().getFilteredNationality().toUpperCase();
	addParameter("nationality", StringUtils.multipleLineRightPad("de nacionalidade " + nationality, LINE_LENGTH, '-'));
    }

    protected String getDegreeDescription() {
	final Registration registration = getDocumentRequest().getRegistration();
	final DegreeType degreeType = registration.getDegreeType();
	return registration.getDegreeDescription(degreeType.hasExactlyOneCycleType() ? degreeType.getCycleType() : registration
		.getCurrentCycleType());
    }

    final protected String getCreditsDescription() {
	return getDocumentRequest().getDegreeType().getCreditsDescription();
    }

    final protected String generateEndLine() {
	return StringUtils.rightPad(StringUtils.EMPTY, LINE_LENGTH, '-');
    }

    final protected String getCurriculumEntryName(final Map<Unit, String> academicUnitIdentifiers, final ICurriculumEntry entry) {
	StringBuilder result = new StringBuilder();

	if (entry instanceof ExternalEnrolment) {
	    result.append(getAcademicUnitIdentifier(academicUnitIdentifiers, ((ExternalEnrolment) entry).getAcademicUnit()));
	}

	result.append(getPresentationNameFor(entry).toUpperCase());

	return result.toString();
    }

    private String getPresentationNameFor(final ICurriculumEntry entry) {
	if (entry instanceof OptionalEnrolment) {
	    final OptionalEnrolment optionalEnrolment = (OptionalEnrolment) entry;
	    return optionalEnrolment.getCurricularCourse().getName();
	} else {
	    return entry.getName().getContent();
	}
    }

    @SuppressWarnings("static-access")
    final protected String getAcademicUnitIdentifier(final Map<Unit, String> academicUnitIdentifiers, final Unit academicUnit) {
	if (!academicUnitIdentifiers.containsKey(academicUnit)) {
	    academicUnitIdentifiers.put(academicUnit, this.identifiers[academicUnitIdentifiers.size()]);
	}

	return academicUnitIdentifiers.get(academicUnit);
    }

    final protected void getCreditsInfo(final StringBuilder result, final ICurriculumEntry entry) {
	result.append(entry.getEctsCreditsForCurriculum()).append(getCreditsDescription()).append(", ");
    }

    final protected String getRemainingCreditsInfo(final ICurriculum curriculum) {
	final BigDecimal remainingCredits = curriculum.getRemainingCredits();

	final StringBuilder result = new StringBuilder();
	if (remainingCredits != BigDecimal.ZERO) {
	    result.append("\n");
	    result.append(StringUtils.multipleLineRightPadWithSuffix("Créditos obtidos por Equivalência:", LINE_LENGTH, '-',
		    remainingCredits + getCreditsDescription()));
	    result.append("\n");
	}

	return result.toString();
    }

    final protected String getAcademicUnitInfo(final Map<Unit, String> academicUnitIdentifiers,
	    final MobilityProgram mobilityProgram) {
	final StringBuilder result = new StringBuilder();

	for (final Entry<Unit, String> academicUnitIdentifier : academicUnitIdentifiers.entrySet()) {
	    final StringBuilder academicUnit = new StringBuilder();

	    academicUnit.append(academicUnitIdentifier.getValue());
	    academicUnit.append(" ").append(resourceBundle.getString("documents.external.curricular.courses.program"));
	    academicUnit.append(" ").append(mobilityProgram.getDescription().toUpperCase());
	    academicUnit.append(" ").append(resourceBundle.getString("in.feminine"));
	    academicUnit.append(" ").append(academicUnitIdentifier.getKey().getName().toUpperCase());

	    result.append(StringUtils.multipleLineRightPad(academicUnit.toString(), LINE_LENGTH, '-') + "\n");
	}

	return result.toString();
    }

}
