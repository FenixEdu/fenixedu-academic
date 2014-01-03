package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.OptionalEnrolment;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.PostingRule;
import net.sourceforge.fenixedu.domain.accounting.postingRules.serviceRequests.CertificateRequestPR;
import net.sourceforge.fenixedu.domain.accounting.serviceAgreementTemplates.AdministrativeOfficeServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;
import net.sourceforge.fenixedu.domain.serviceRequests.RegistrationAcademicServiceRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.Under23TransportsDeclarationRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.CertificateRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.CourseLoadRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.ExternalCourseLoadRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.ExternalProgramCertificateRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.IDocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.ProgramCertificateRequest;
import net.sourceforge.fenixedu.domain.student.MobilityProgram;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.curriculum.ICurriculum;
import net.sourceforge.fenixedu.domain.student.curriculum.ICurriculumEntry;
import net.sourceforge.fenixedu.domain.studentCurriculum.ExternalEnrolment;
import net.sourceforge.fenixedu.presentationTier.docs.FenixReport;
import net.sourceforge.fenixedu.util.FenixStringTools;
import net.sourceforge.fenixedu.util.HtmlToTextConverterUtil;
import net.sourceforge.fenixedu.util.Money;
import net.sourceforge.fenixedu.util.StringFormatter;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class AdministrativeOfficeDocument extends FenixReport {

    static final protected int LINE_LENGTH = 64;

    static final protected int SUFFIX_LENGTH = 12;

    static final protected String[] identifiers = { "i) ", "ii) ", "iii) ", "iv) ", "v) ", "vi) ", "vii) ", "viii) ", "ix) ",
            "x) " };

    static final protected String LINE_BREAK = "\n";

    static final protected char END_CHAR = '-';

    protected IDocumentRequest documentRequestDomainReference;

    protected ResourceBundle portugueseEnumerationBundle;

    protected ResourceBundle portugueseAcademicBundle;

    public static class AdministrativeOfficeDocumentCreator {

        @SuppressWarnings("unchecked")
        public static <T extends AdministrativeOfficeDocument> List<T> create(final IDocumentRequest documentRequest) {
            switch (documentRequest.getDocumentRequestType()) {
            case ENROLMENT_CERTIFICATE:
                return Collections.<T> singletonList((T) new EnrolmentCertificate(documentRequest));
            case APPROVEMENT_CERTIFICATE:
                return Collections.<T> singletonList((T) new ApprovementCertificate(documentRequest));
            case APPROVEMENT_MOBILITY_CERTIFICATE:
                return Collections.<T> singletonList((T) new ApprovementMobilityCertificate(documentRequest));
            case DEGREE_FINALIZATION_CERTIFICATE:
                return Collections.<T> singletonList((T) new DegreeFinalizationCertificate(documentRequest));
            case PHD_FINALIZATION_CERTIFICATE:
                return Collections.<T> singletonList((T) new PhdFinalizationCertificate(documentRequest));
            case SCHOOL_REGISTRATION_DECLARATION:
                return Collections.<T> singletonList((T) new RegistrationDeclaration(documentRequest));
            case SCHOOL_REGISTRATION_CERTIFICATE:
                return Collections.<T> singletonList((T) new RegistrationCertificate(documentRequest));
            case ENROLMENT_DECLARATION:
                return Collections.<T> singletonList((T) new EnrolmentDeclaration(documentRequest));
            case IRS_DECLARATION:
                return Collections.<T> singletonList((T) new IRSDeclaration(documentRequest));
            case DIPLOMA_REQUEST:
                if (documentRequest.isRequestForRegistration()) {
                    return Collections.<T> singletonList((T) new Diploma(documentRequest));
                }

                if (documentRequest.isRequestForPhd()) {
                    return Collections.<T> singletonList((T) new PhdDiploma(documentRequest));
                }
            case REGISTRY_DIPLOMA_REQUEST:
                if (documentRequest.isRequestForRegistration()) {
                    return Collections.<T> singletonList((T) new RegistryDiploma(documentRequest));
                }

                if (documentRequest.isRequestForPhd()) {
                    return Collections.<T> singletonList((T) new PhdRegistryDiploma(documentRequest));
                }
            case DIPLOMA_SUPPLEMENT_REQUEST:
                List<T> result = new ArrayList<T>();
                for (Locale locale : DiplomaSupplement.suportedLocales) {
                    result.add((T) new DiplomaSupplement(documentRequest, locale));
                }
                return result;
            case EXAM_DATE_CERTIFICATE:
                return Collections.<T> singletonList((T) new ExamDateCertificate(documentRequest));
            case COURSE_LOAD:
                return Collections.<T> singletonList((T) new CourseLoadRequestDocument((CourseLoadRequest) documentRequest));
            case EXTERNAL_COURSE_LOAD:
                return Collections.<T> singletonList((T) new ExternalCourseLoadRequestDocument(
                        (ExternalCourseLoadRequest) documentRequest));
            case PROGRAM_CERTIFICATE:
                return Collections.<T> singletonList((T) new ProgramCertificateRequestDocument(
                        (ProgramCertificateRequest) documentRequest));
            case EXTERNAL_PROGRAM_CERTIFICATE:
                return Collections.<T> singletonList((T) new ExternalProgramCertificateRequestDocument(
                        (ExternalProgramCertificateRequest) documentRequest));
            case EXTRA_CURRICULAR_CERTIFICATE:
                return Collections.<T> singletonList((T) new ExtraCurricularCertificateRequestDocument(documentRequest));
            case STANDALONE_ENROLMENT_CERTIFICATE:
                return Collections.<T> singletonList((T) new StandaloneEnrolmentCertificateRequestDocument(documentRequest));
            case UNDER_23_TRANSPORTS_REQUEST:
                return Collections.<T> singletonList((T) new Under23TransportsDeclarationDocument(
                        (Under23TransportsDeclarationRequest) documentRequest));
            default:
                return Collections.<T> singletonList((T) new AdministrativeOfficeDocument(documentRequest));
            }
        }

    }

    protected AdministrativeOfficeDocument(final IDocumentRequest documentRequest) {
        this(documentRequest, new Locale(documentRequest.getLanguage().name()));
    }

    public AdministrativeOfficeDocument(final IDocumentRequest documentRequest, final Locale locale) {
        super(locale);
        this.portugueseEnumerationBundle =
                ResourceBundle.getBundle("resources.EnumerationResources", Language.getDefaultLocale());

        this.portugueseAcademicBundle = ResourceBundle.getBundle("resources.AcademicAdminOffice", Language.getDefaultLocale());

        setResourceBundle(ResourceBundle.getBundle("resources.AcademicAdminOffice", locale));
        this.documentRequestDomainReference = documentRequest;

        fillReport();
    }

    protected IDocumentRequest getDocumentRequest() {
        return documentRequestDomainReference;
    }

    @Override
    public String getReportTemplateKey() {
        return getDocumentRequest().getDocumentTemplateKey();
    }

    protected UniversityUnit getUniversity(DateTime date) {
        return UniversityUnit.getInstitutionsUniversityUnitByDate(date);
    }

    protected String getUniversityName(DateTime date) {
        return getUniversity(date).getPartyName().getContent(getLanguage());
    }

    protected String getInstitutionName() {
        return Bennu.getInstance().getInstitutionUnit().getPartyName().getContent(getLanguage());
    }

    @Override
    public String getReportFileName() {
        final StringBuilder result = new StringBuilder();

        result.append(getDocumentRequest().getPerson().getIstUsername());
        result.append("-");
        result.append(new DateTime().toString(YYYYMMMDD, getLocale()));
        result.append("-");
        result.append(getDocumentRequest().getDescription().replace(":", EMPTY_STR).replace(SINGLE_SPACE, EMPTY_STR));
        result.append("-");
        result.append(getLanguage().toString());

        return result.toString();
    }

    protected AdministrativeOffice getAdministrativeOffice() {
        return getDocumentRequest().getAdministrativeOffice();
    }

    protected Registration getRegistration() {
        if (getDocumentRequest().isRequestForRegistration()) {
            return ((RegistrationAcademicServiceRequest) getDocumentRequest()).getRegistration();
        }

        return null;
    }

    @Override
    protected void fillReport() {
        addParameter("bundle", getResourceBundle());
        addParameter("documentRequest", getDocumentRequest());
        addParameter("registration", getRegistration());

        if (showPriceFields() && !((AcademicServiceRequest) getDocumentRequest()).isFree()) {
            addPriceFields();
        }

        addIntroParameters();
        setDocumentTitle();
        setPersonFields();

        if (getDocumentRequest().hasExecutionYear()) {
            String situation = getExecutionYear().containsDate(new DateTime()) ? "label.is" : "label.was";
            addParameter("situation", getResourceBundle().getString(situation));
        }

        addParameter("degreeDescription", getDegreeDescription());
        addParameter("day", new LocalDate().toString(DD_MMMM_YYYY, getLocale()));

        newFillReport();
    }

    protected void newFillReport() {
    }

    protected boolean showPriceFields() {
        return getDocumentRequest().isCertificate() && getDocumentRequest().getEventType() != null;
    }

    protected void addPriceFields() {
        final CertificateRequest certificateRequest = (CertificateRequest) getDocumentRequest();
        final CertificateRequestPR certificateRequestPR = (CertificateRequestPR) getPostingRule();

        final Money amountPerPage = certificateRequestPR.getAmountPerPage();
        final Money baseAmountPlusAmountForUnits =
                certificateRequestPR.getBaseAmount().add(
                        certificateRequestPR.getAmountPerUnit().multiply(
                                BigDecimal.valueOf(certificateRequest.getNumberOfUnits())));
        final Money urgencyAmount = certificateRequest.getUrgentRequest() ? certificateRequestPR.getBaseAmount() : Money.ZERO;
        addParameter("printed", getResourceBundle().getString("label.academicDocument.certificate.printingPriceLabel"));
        addParameter("printPriceLabel", getResourceBundle().getString("label.academicDocument.certificate.issuingPriceLabel"));
        addParameter("urgency", getResourceBundle().getString("label.academicDocument.certificate.fastDeliveryPriceLabel"));
        addParameter("total", getResourceBundle().getString("label.academicDocument.certificate.totalsPriceLabel"));
        addParameter("amountPerPage", amountPerPage);
        addParameter("baseAmountPlusAmountForUnits", baseAmountPlusAmountForUnits);
        addParameter("urgencyAmount", urgencyAmount);
        addParameter("printPriceFields", printPriceParameters(certificateRequest));
    }

    final protected PostingRule getPostingRule() {
        final AdministrativeOfficeServiceAgreementTemplate serviceAgreementTemplate =
                getDocumentRequest().getAdministrativeOffice().getServiceAgreementTemplate();
        return serviceAgreementTemplate.findPostingRuleByEventType(getDocumentRequest().getEventType());
    }

    final protected boolean printPriceParameters(final CertificateRequest certificateRequest) {
        return certificateRequest.getAcademicServiceRequestSituationType() == AcademicServiceRequestSituationType.PROCESSING
                && !certificateRequest.isFree() || certificateRequest.hasEvent();
    }

    protected void addIntroParameters() {
        Unit adminOfficeUnit = getAdministrativeOffice().getUnit();
        addParameter("administrativeOfficeCoordinator", adminOfficeUnit.getActiveUnitCoordinator());
        addParameter("administrativeOfficeName", getMLSTextContent(adminOfficeUnit.getPartyName()));

        addParameter("employeeLocation", adminOfficeUnit.getCampus().getLocation());
        addParameter("supervisingUnit", getResourceBundle().getString("label.academicDocument.direcaoAcademica"));

        addParameter("institutionName", getInstitutionName());
        addParameter("universityName", getUniversityName(getDocumentRequest().getRequestDate()));
    }

    protected void setDocumentTitle() {
    }

    protected void setPersonFields() {
        final Person person = getDocumentRequest().getPerson();

        StringBuilder builder1 = new StringBuilder();
        builder1.append(getResourceBundle().getString("label.with"));
        builder1.append(SINGLE_SPACE).append(person.getIdDocumentType().getLocalizedName(getLocale()));
        builder1.append(SINGLE_SPACE).append(getResourceBundle().getString("label.number.short"));
        builder1.append(SINGLE_SPACE).append(person.getDocumentIdNumber());

        StringBuilder builder2 = new StringBuilder();
        builder2.append(getResourceBundle().getString("documents.birthLocale"));
        builder2.append(SINGLE_SPACE).append(getBirthLocale(person, false));

        if (getDocumentRequest().getDocumentRequestType().equals(DocumentRequestType.APPROVEMENT_MOBILITY_CERTIFICATE)) {
            addParameter("name", person.getName().toUpperCase());
            addParameter("documentIdNumber", builder1.toString());
            addParameter("birthLocale", builder2.toString());
        } else {
            addParameter("name", FenixStringTools.multipleLineRightPad(person.getName().toUpperCase(), LINE_LENGTH, END_CHAR));
            addParameter("documentIdNumber", FenixStringTools.multipleLineRightPad(builder1.toString(), LINE_LENGTH, END_CHAR));
            addParameter("birthLocale", FenixStringTools.multipleLineRightPad(builder2.toString(), LINE_LENGTH, END_CHAR));
        }

        setNationality(person);
    }

    protected void setNationality(final Person person) {
        /*StringBuilder builder = new StringBuilder();
        builder.append(getResourceBundle().getString("label.and")).append(SINGLE_SPACE);
        builder.append(getResourceBundle().getString("documents.nationality.one"));
        */
        final String nationality = person.getCountry().getFilteredNationality(getLocale()).toUpperCase();
        // builder.append(SINGLE_SPACE).append(nationality.toUpperCase()).append(SINGLE_SPACE);
        String labelNationality = getResourceBundle().getString("message.documents.nationality");

        String nationalityMessage = MessageFormat.format(labelNationality, nationality);
        if (getDocumentRequest().getDocumentRequestType().equals(DocumentRequestType.APPROVEMENT_MOBILITY_CERTIFICATE)) {
            addParameter("nationality", nationalityMessage);
        } else {
            addParameter("nationality", FenixStringTools.multipleLineRightPad(nationalityMessage, LINE_LENGTH, END_CHAR));
        }
    }

    protected String getBirthLocale(final Person person, final boolean prettyPrint) {
        final StringBuilder result = new StringBuilder();

        if (person.getParishOfBirth() == null) {
            return "";
        }

        final String parishOfBirth =
                prettyPrint ? StringFormatter.prettyPrint(person.getParishOfBirth()) : person.getParishOfBirth();
        final String districtSubdivision =
                prettyPrint ? StringFormatter.prettyPrint(person.getDistrictSubdivisionOfBirth()) : person
                        .getDistrictSubdivisionOfBirth();

        result.append(parishOfBirth);
        if (!parishOfBirth.equals(districtSubdivision)) {
            result.append(",").append(SINGLE_SPACE).append(districtSubdivision);
        }

        return result.toString();
    }

    protected String getDegreeDescription() {
        final Registration registration = getRegistration();
        final DegreeType degreeType = registration.getDegreeType();
        final CycleType cycleType =
                degreeType.hasExactlyOneCycleType() ? degreeType.getCycleType() : registration.getCycleType(getExecutionYear());
        return registration.getDegreeDescription(cycleType, getLocale());
    }

    protected ExecutionYear getExecutionYear() {
        if (getDocumentRequest().hasExecutionYear()) {
            return getDocumentRequest().getExecutionYear();
        }

        return ExecutionYear.readByDateTime(getDocumentRequest().getRequestDate());
    }

    final protected String getCreditsDescription() {
        if (getDocumentRequest().isRequestForRegistration()) {
            return ((RegistrationAcademicServiceRequest) getDocumentRequest()).getDegreeType().getCreditsDescription();
        }

        return null;
    }

    final protected String generateEndLine() {
        return StringUtils.rightPad(EMPTY_STR, LINE_LENGTH, END_CHAR);
    }

    final protected String getCurriculumEntryName(final Map<Unit, String> academicUnitIdentifiers, final ICurriculumEntry entry) {
        final StringBuilder result = new StringBuilder();

        if (entry instanceof ExternalEnrolment) {
            result.append(getAcademicUnitIdentifier(academicUnitIdentifiers, ((ExternalEnrolment) entry).getAcademicUnit()));
        }
        result.append(getPresentationNameFor(entry).toUpperCase());

        return result.toString();
    }

    protected String getPresentationNameFor(final ICurriculumEntry entry) {
        final MultiLanguageString result;

        if (entry instanceof OptionalEnrolment) {
            final OptionalEnrolment optionalEnrolment = (OptionalEnrolment) entry;
            result = optionalEnrolment.getCurricularCourse().getNameI18N();
        } else {
            result = entry.getName();
        }

        return getMLSTextContent(result);
    }

    protected String getMLSTextContent(final MultiLanguageString mls) {
        return getMLSTextContent(mls, getLanguage());
    }

    protected String getMLSTextContent(final MultiLanguageString mls, final Language language) {
        if (mls == null) {
            return EMPTY_STR;
        }
        final String content =
                mls.hasContent(language) && !StringUtils.isEmpty(mls.getContent(language)) ? mls.getContent(language) : mls
                        .getContent();
        return content;
        // return convert(content);
    }

    protected String convert(final String content) {
        return HtmlToTextConverterUtil.convertToText(content).replace("\n\n", "\t").replace(LINE_BREAK, EMPTY_STR)
                .replace("\t", "\n\n").trim();
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
            result.append(LINE_BREAK);

            final String remainingCreditsInfo = getResourceBundle().getString("documents.remainingCreditsInfo");
            result.append(FenixStringTools.multipleLineRightPadWithSuffix(remainingCreditsInfo + ":", LINE_LENGTH, END_CHAR,
                    remainingCredits + getCreditsDescription()));

            result.append(LINE_BREAK);
        }

        return result.toString();
    }

    final protected String getAcademicUnitInfo(final Map<Unit, String> unitIDs, final MobilityProgram mobilityProgram) {
        final StringBuilder result = new StringBuilder();

        for (final Entry<Unit, String> academicUnitId : unitIDs.entrySet()) {
            final StringBuilder unit = new StringBuilder();

            unit.append(academicUnitId.getValue());
            unit.append(SINGLE_SPACE).append(getResourceBundle().getString("documents.external.curricular.courses.one"));
            unit.append(SINGLE_SPACE).append(getMLSTextContent(academicUnitId.getKey().getPartyName()).toUpperCase());
            if (mobilityProgram != null) {
                unit.append(SINGLE_SPACE).append(getResourceBundle().getString("documents.external.curricular.courses.two"));
                unit.append(SINGLE_SPACE).append(mobilityProgram.getDescription(getLocale()).toUpperCase());
            }

            result.append(FenixStringTools.multipleLineRightPad(unit.toString(), LINE_LENGTH, END_CHAR));
            result.append(LINE_BREAK);
        }

        return result.toString();
    }

    protected String getCreditsAndGradeInfo(final ICurriculumEntry entry, final ExecutionYear executionYear) {
        final StringBuilder result = new StringBuilder();

        if (getDocumentRequest().isToShowCredits()) {
            getCreditsInfo(result, entry);
        }
        result.append(entry.getGradeValue());
        result.append(StringUtils.rightPad("(" + getEnumerationBundle().getString(entry.getGradeValue()) + ")", SUFFIX_LENGTH,
                ' '));

        result.append(SINGLE_SPACE);
        final String in = getResourceBundle().getString("label.in");
        if (executionYear == null) {
            result.append(StringUtils.rightPad(EMPTY_STR, in.length(), ' '));
            result.append(SINGLE_SPACE).append(StringUtils.rightPad(EMPTY_STR, 9, ' '));
        } else {
            result.append(in);
            result.append(SINGLE_SPACE).append(executionYear.getYear());
        }

        return result.toString();
    }

    protected void setFooter(DocumentRequest documentRequest) {
        Registration registration = documentRequest.getRegistration();
        String student;

        if (registration.getStudent().getPerson().isMale()) {
            student = getResourceBundle().getString("label.academicDocument.declaration.maleStudent");
        } else {
            student = getResourceBundle().getString("label.academicDocument.declaration.femaleStudent");
        }

        String stringTemplate = getResourceBundle().getString("label.academicDocument.declaration.footer.studentNumber");
        addParameter("studentNumber", MessageFormat.format(stringTemplate, student, registration.getNumber().toString()));

        stringTemplate = getResourceBundle().getString("label.academicDocument.declaration.footer.documentNumber");
        addParameter("documentNumber", MessageFormat.format(stringTemplate, documentRequest.getServiceRequestNumberYear()));
        addParameter("checked", getResourceBundle().getString("label.academicDocument.irs.declaration.checked"));
        addParameter("page", getResourceBundle().getString("label.academicDocument.declaration.footer.page"));
        addParameter("pageOf", getResourceBundle().getString("label.academicDocument.declaration.footer.pageOf"));
    }

    protected void fillEmployeeFields() {
        final Unit adminOfficeUnit = getAdministrativeOffice().getUnit();
        final String institutionName = getInstitutionName();
        final Person coordinator = adminOfficeUnit.getActiveUnitCoordinator();

        String coordinatorTitle;
        coordinatorTitle = getCoordinatorGender(coordinator);
        String stringTemplate = getResourceBundle().getString("label.academicDocument.irs.declaration.signer");
        addParameter("signer",
                MessageFormat.format(stringTemplate, coordinatorTitle, getMLSTextContent(adminOfficeUnit.getPartyName())));

        String departmentAndInstitute = getResourceBundle().getString("label.academicDocument.declaration.signer");
        addParameter("departmentAndInstitute",
                MessageFormat.format(departmentAndInstitute, getMLSTextContent(adminOfficeUnit.getPartyName()), institutionName));

        addParameter("administrativeOfficeCoordinator", coordinator);
        String location = adminOfficeUnit.getCampus().getLocation();
        String dateDD = new LocalDate().toString("dd", getLocale());
        String dateMMMM = new LocalDate().toString("MMMM", getLocale());
        String dateYYYY = new LocalDate().toString("yyyy", getLocale());
        stringTemplate = getResourceBundle().getString("label.academicDocument.declaration.signerLocation");
        addParameter("signerLocation",
                MessageFormat.format(stringTemplate, institutionName, location, dateDD, dateMMMM, dateYYYY));
    }

    protected String getCoordinatorGender(final Person coordinator) {
        if (coordinator.isMale()) {
            return getResourceBundle().getString("label.academicDocument.declaration.maleCoordinator");
        } else {
            return getResourceBundle().getString("label.academicDocument.declaration.femaleCoordinator");
        }
    }

}
