/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.report.academicAdministrativeOffice;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Locality;
import org.fenixedu.academic.domain.OptionalEnrolment;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.PostingRule;
import org.fenixedu.academic.domain.accounting.postingRules.serviceRequests.CertificateRequestPR;
import org.fenixedu.academic.domain.accounting.serviceAgreementTemplates.AdministrativeOfficeServiceAgreementTemplate;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.organizationalStructure.UniversityUnit;
import org.fenixedu.academic.domain.phd.serviceRequests.documentRequests.PhdDocumentRequest;
import org.fenixedu.academic.domain.serviceRequests.AcademicServiceRequest;
import org.fenixedu.academic.domain.serviceRequests.AcademicServiceRequestSituationType;
import org.fenixedu.academic.domain.serviceRequests.RegistrationAcademicServiceRequest;
import org.fenixedu.academic.domain.serviceRequests.Under23TransportsDeclarationRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.CertificateRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.CourseLoadRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentRequestType;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.ExternalCourseLoadRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.ExternalProgramCertificateRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.IDocumentRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.ProgramCertificateRequest;
import org.fenixedu.academic.domain.student.MobilityProgram;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.curriculum.ICurriculum;
import org.fenixedu.academic.domain.student.curriculum.ICurriculumEntry;
import org.fenixedu.academic.domain.studentCurriculum.ExternalEnrolment;
import org.fenixedu.academic.report.FenixReport;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.FenixStringTools;
import org.fenixedu.academic.util.HtmlToTextConverterUtil;
import org.fenixedu.academic.util.LocaleUtils;
import org.fenixedu.academic.util.Money;
import org.fenixedu.academic.util.StringFormatter;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.util.CoreConfiguration;
import org.fenixedu.commons.i18n.LocalizedString;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.google.common.base.Strings;

public class AdministrativeOfficeDocument extends FenixReport {

    static final protected int LINE_LENGTH = 64;

    static final protected int SUFFIX_LENGTH = 12;

    static final protected String[] identifiers = { "i) ", "ii) ", "iii) ", "iv) ", "v) ", "vi) ", "vii) ", "viii) ", "ix) ",
            "x) " };

    static final protected String LINE_BREAK = "\n";

    static final protected char END_CHAR = '-';

    protected IDocumentRequest documentRequestDomainReference;

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
                Set<Locale> definedLocales = new HashSet<Locale>(CoreConfiguration.supportedLocales());
                definedLocales.remove(Locale.getDefault());
                result.add((T) new DiplomaSupplement(documentRequest, Locale.getDefault()));
                for (Locale locale : definedLocales) {
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
        this(documentRequest, documentRequest.getLanguage());
    }

    public AdministrativeOfficeDocument(final IDocumentRequest documentRequest, final Locale locale) {
        super(locale);
        this.documentRequestDomainReference = documentRequest;

        fillReport();
    }

    public IDocumentRequest getDocumentRequest() {
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

    protected Person getPrincipal() {
        return getUniversity(getDocumentRequest().getRequestDate()).getCurrentPrincipal();
    }

    protected Person getPresident() {
        return getUniversity(getDocumentRequest().getRequestDate()).getCurrentPresident();
    }

    private String getDayOfMonthSuffixOfDate (int dayOfMonth) {
        switch ( (dayOfMonth < 20) ? dayOfMonth : dayOfMonth % 10 ) {
            case 1 : return "st";
            case 2 : return "nd";
            case 3 : return "rd";
            default : return "th";
        }
    }

    protected String getFormattedDate(LocalDate date) {
        final int dayOfMonth = date.getDayOfMonth();
        final String month = date.toString("MMMM", getLocale());
        final int year = date.getYear();

        return new StringBuilder()
                .append(dayOfMonth)
                .append( (getLocale().equals(LocaleUtils.PT) ? EMPTY_STR : getDayOfMonthSuffixOfDate(dayOfMonth)) )
                .append(SINGLE_SPACE)
                .append(BundleUtil.getString(Bundle.APPLICATION, getLocale(), "label.of"))
                .append(SINGLE_SPACE)
                .append( (getLocale().equals(LocaleUtils.PT)) ? month.toLowerCase() : month )
                .append(SINGLE_SPACE)
                .append(BundleUtil.getString(Bundle.APPLICATION, getLocale(), "label.of"))
                .append(SINGLE_SPACE)
                .append(year)
                .toString();
    }

    protected String getFormattedCurrentDate() {
        return getFormattedDate(new LocalDate());
    }

    @Override
    public String getReportFileName() {
        final StringBuilder result = new StringBuilder();

        result.append(getDocumentRequest().getPerson().getUsername());
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

        if (getDocumentRequest().isRequestForPhd()) {
            return ((PhdDocumentRequest) getDocumentRequest()).getPhdIndividualProgramProcess().getRegistration();
        }

        throw new DomainException("error.AdministrativeOfficeDocument.registration.not.found");
    }

    @Override
    protected void fillReport() {
        addParameter("bundle", ResourceBundle.getBundle(getBundle(), getLocale()));
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
            addParameter("situation", BundleUtil.getString(Bundle.ACADEMIC, getLocale(), situation));
        }

        addParameter("degreeDescription", getDegreeDescription());
        addParameter("day", new LocalDate().toString(DD_MMMM_YYYY, getLocale()));

        newFillReport();
    }

    protected String getBundle() {
        return Bundle.ACADEMIC;
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
        addParameter("printed",
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.academicDocument.certificate.printingPriceLabel"));
        addParameter("printPriceLabel",
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.academicDocument.certificate.issuingPriceLabel"));
        addParameter("urgency",
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.academicDocument.certificate.fastDeliveryPriceLabel"));
        addParameter("total",
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.academicDocument.certificate.totalsPriceLabel"));
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
                && !certificateRequest.isFree() || certificateRequest.getEvent() != null;
    }

    protected void addIntroParameters() {
        addParameter("administrativeOfficeCoordinator", getAdministrativeOffice().getCoordinator().getPerson());
        addParameter("administrativeOfficeName", getI18NText(getAdministrativeOffice().getName()));

        final Locality locality = getAdministrativeOffice().getCampus().getLocality();
        addParameter("employeeLocation", locality != null ? locality.getName() : null);
        addParameter("supervisingUnit",
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.academicDocument.direcaoAcademica"));

        addParameter("institutionName", getInstitutionName());
        addParameter("universityName", getUniversityName(getDocumentRequest().getRequestDate()));
    }

    protected void setDocumentTitle() {
    }

    protected void setPersonFields() {
        final Person person = getDocumentRequest().getPerson();

        StringBuilder builder1 = new StringBuilder();
        builder1.append(BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.with"));
        builder1.append(SINGLE_SPACE).append(person.getIdDocumentType().getLocalizedName(getLocale()));
        builder1.append(SINGLE_SPACE).append(BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.number.short"));
        builder1.append(SINGLE_SPACE).append(person.getDocumentIdNumber());

        StringBuilder builder2 = new StringBuilder();
        builder2.append(BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "documents.birthLocale"));
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
        final String nationality = person.getCountry().getFilteredNationality(getLocale()).toUpperCase();
        String labelNationality = BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "message.documents.nationality");

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
        final LocalizedString result;

        if (entry instanceof OptionalEnrolment) {
            final OptionalEnrolment optionalEnrolment = (OptionalEnrolment) entry;
            result = optionalEnrolment.getCurricularCourse().getNameI18N();
        } else {
            result = entry.getName();
        }

        return getMLSTextContent(result);
    }

    protected String getMLSTextContent(final LocalizedString mls) {
        return getMLSTextContent(mls, getLanguage());
    }

    protected String getMLSTextContent(final LocalizedString mls, final Locale language) {
        if (mls == null) {
            return EMPTY_STR;
        }
        final String content =
                mls.getContent(language) != null && !StringUtils.isEmpty(mls.getContent(language)) ? mls.getContent(language) : mls
                        .getContent();
        return content;
        // return convert(content);
    }

    protected String getI18NText(LocalizedString localized) {
        return getI18NText(localized, getLocale());
    }

    protected String getI18NText(LocalizedString localized, Locale language) {
        if (localized == null) {
            return "";
        }
        String result = localized.getContent(language);
        return !Strings.isNullOrEmpty(result) ? result : localized.getContent();
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

                final String remainingCreditsInfo =
                    BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "documents.remainingCreditsInfo");
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
            unit.append(SINGLE_SPACE).append(
                    BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "documents.external.curricular.courses.one"));
            unit.append(SINGLE_SPACE).append(getMLSTextContent(academicUnitId.getKey().getPartyName()).toUpperCase());
            if (mobilityProgram != null) {
                unit.append(SINGLE_SPACE).append(
                        BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "documents.external.curricular.courses.two"));
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
        result.append(StringUtils.rightPad("(" + BundleUtil.getString(Bundle.ENUMERATION, getLocale(), entry.getGradeValue())
                + ")", SUFFIX_LENGTH, ' '));

        result.append(SINGLE_SPACE);
        final String in = BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.in");
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
            student = BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.academicDocument.declaration.maleStudent");
        } else {
            student = BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.academicDocument.declaration.femaleStudent");
        }

        String stringTemplate =
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.academicDocument.declaration.footer.studentNumber");
        addParameter("studentNumber", MessageFormat.format(stringTemplate, student, registration.getNumber().toString()));

        stringTemplate =
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.academicDocument.declaration.footer.documentNumber");
        addParameter("documentNumber", MessageFormat.format(stringTemplate, documentRequest.getServiceRequestNumberYear()));
        addParameter("checked",
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.academicDocument.irs.declaration.checked"));
        addParameter("page", BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.academicDocument.declaration.footer.page"));
        addParameter("pageOf",
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.academicDocument.declaration.footer.pageOf"));
    }

    protected void fillInstitutionAndStaffFields() {
        final String institutionName = getInstitutionName();
        final Person coordinator = getAdministrativeOffice().getCoordinator().getPerson();

        String coordinatorTitle;
        coordinatorTitle = getCoordinatorGender(coordinator);
        String stringTemplate =
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.academicDocument.irs.declaration.signer");
        addParameter("signer",
                MessageFormat.format(stringTemplate, coordinatorTitle, getI18NText(getAdministrativeOffice().getName())));

        String departmentAndInstitute =
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.academicDocument.declaration.signer");
        addParameter("departmentAndInstitute",
                MessageFormat.format(departmentAndInstitute, getI18NText(getAdministrativeOffice().getName()), institutionName));

        addParameter("administrativeOfficeCoordinator", coordinator);
        String location = getLocation();
        String dateDD = new LocalDate().toString("dd", getLocale());
        String dateMMMM = new LocalDate().toString("MMMM", getLocale());
        String dateYYYY = new LocalDate().toString("yyyy", getLocale());

        stringTemplate = BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.academicDocument.declaration.signerLocation");
        addParameter("signerLocation",
                MessageFormat.format(stringTemplate, institutionName, location, dateDD, dateMMMM, dateYYYY));
    }

    protected String getLocation() {
        final Locality locality = getAdministrativeOffice().getCampus().getLocality();
        return locality != null ? locality.getName() : null;
    }

    protected String getCoordinatorGender(final Person coordinator) {
        if (coordinator.isMale()) {
            return BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.academicDocument.declaration.maleCoordinator");
        } else {
            return BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.academicDocument.declaration.femaleCoordinator");
        }
    }

}
