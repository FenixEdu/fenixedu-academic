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

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.Grade;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.postingRules.serviceRequests.CertificateRequestPR;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.CertificateRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DegreeFinalizationCertificateRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.IDocumentRequest;
import org.fenixedu.academic.domain.student.curriculum.ICurriculumEntry;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.FenixStringTools;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.I18N;
import org.joda.time.DateTime;

public class DegreeFinalizationCertificate extends AdministrativeOfficeDocument {

    protected DegreeFinalizationCertificate(final IDocumentRequest documentRequest) {
        super(documentRequest);
    }

    @Override
    protected DegreeFinalizationCertificateRequest getDocumentRequest() {
        return (DegreeFinalizationCertificateRequest) super.getDocumentRequest();
    }

    @Override
    protected void fillReport() {
        super.fillReport();

        fillFirstParagraph();

        fillSecondParagraph();
        final DegreeFinalizationCertificateRequest req = getDocumentRequest();

        addParameter("degreeFinalizationInfo", getDegreeFinalizationInfo(req));

        fillInstitutionAndStaffFields();
        setFooter(req);
        setBranchField();
    }

    private void fillSecondParagraph() {

        final DegreeFinalizationCertificateRequest req = getDocumentRequest();
        final StringBuilder result = new StringBuilder();

        result.append(BundleUtil.getString(Bundle.ACADEMIC, getDocumentRequest().getLanguage(),
                "conclusion.document.concluded.lowercase"));
        result.append(" ");
        result.append(BundleUtil.getString(Bundle.ACADEMIC, getDocumentRequest().getLanguage(), "label.the.male"));
        result.append(" ");
        result.append(getDegreeDescription());
        result.append(",");

        if (getDocumentRequest().getBranch() == null || getDocumentRequest().getBranch().isEmpty()) {
            result.append("");
        } else {
            result.append(",");
        }

        result.append(getDegreeFinalizationDate(req));
        result.append(getExceptionalConclusionInfo(req));

        if (req.getAverage()) {
            result.append(getDegreeFinalizationGrade(req.getFinalAverage(), getLocale()));
        }

        result.append(getDegreeFinalizationEcts(req));
        result.append(req.getGraduateTitle(req.getLanguage()));
        result.append(getDiplomaDescription());
        result.append(getDetailedInfoIntro(req));

        addParameter("secondParagraph", result.toString());

    }

    protected void fillFirstParagraph() {

        Person coordinator = getAdministrativeOffice().getCoordinator().getPerson();

        String coordinatorTitle = getCoordinatorGender(coordinator);

        String adminOfficeName = getI18NText(getAdministrativeOffice().getName());
        String institutionName = getInstitutionName();
        String universityName = getUniversityName(new DateTime());

        String stringTemplate =
                BundleUtil.getString(Bundle.ACADEMIC, getDocumentRequest().getLanguage(),
                        "label.academicDocument.declaration.firstParagraph");

        addParameter(
                "firstParagraph",
                "     "
                        + MessageFormat.format(stringTemplate, coordinator.getName(), coordinatorTitle,
                                adminOfficeName.toUpperCase(getLocale()), institutionName.toUpperCase(getLocale()),
                                universityName.toUpperCase(getLocale())));

        addParameter("certificate", BundleUtil.getString(Bundle.ACADEMIC, getDocumentRequest().getLanguage(),
                "label.academicDocument.standaloneEnrolmentCertificate.secondParagraph"));
    }

    private void setBranchField() {
        String branch = getDocumentRequest().getBranch();
        if ((branch == null) || (branch.isEmpty())) {
            addParameter("branch", "");
            return;
        }
        addParameter("branch", SINGLE_SPACE + getDocumentRequest().getBranch());
    }

    @Override
    protected String getDegreeDescription() {
        return getRegistration().getDegreeDescription(getDocumentRequest().getConclusionYear(),
                getDocumentRequest().getProgramConclusion(), getDocumentRequest().getLanguage());
    }

    private String getDegreeFinalizationDate(final DegreeFinalizationCertificateRequest request) {
        final StringBuilder result = new StringBuilder();

        if (!request.mustHideConclusionDate()) {
            result.append(SINGLE_SPACE).append(
                    BundleUtil.getString(Bundle.ACADEMIC, getDocumentRequest().getLanguage(), "label.onThe"));
            result.append(SINGLE_SPACE).append(request.getConclusionDate().toString(DD_MMMM_YYYY, getLocale()));
        }

        return result.toString();
    }

    private String getExceptionalConclusionInfo(final DegreeFinalizationCertificateRequest request) {
        final StringBuilder result = new StringBuilder();

        if (request.hasExceptionalConclusionInfo()) {
            if (request.getTechnicalEngineer()) {
                result.append(SINGLE_SPACE);
                result.append(BundleUtil.getString(Bundle.ACADEMIC, getDocumentRequest().getLanguage(),
                        "documents.DegreeFinalizationCertificate.exceptionalConclusionInfo.technicalEngineer"));
            } else {
                final String date = request.getExceptionalConclusionDate().toString(DD_MMMM_YYYY, getLocale());
                if (request.getInternshipAbolished()) {
                    result.append(SINGLE_SPACE).append(
                            BundleUtil.getString(Bundle.ACADEMIC, getDocumentRequest().getLanguage(), "label.in"));
                    result.append(SINGLE_SPACE).append(date);
                    result.append(", ");
                    result.append(BundleUtil.getString(Bundle.ACADEMIC, getDocumentRequest().getLanguage(),
                            "documents.DegreeFinalizationCertificate.exceptionalConclusionInfo.internshipAbolished"));
                } else if (request.getInternshipApproved()) {
                    result.append(SINGLE_SPACE).append(
                            BundleUtil.getString(Bundle.ACADEMIC, getDocumentRequest().getLanguage(), "label.in"));
                    result.append(SINGLE_SPACE).append(date);
                    result.append(", ");
                    result.append(BundleUtil.getString(Bundle.ACADEMIC, getDocumentRequest().getLanguage(),
                            "documents.DegreeFinalizationCertificate.exceptionalConclusionInfo.internshipApproved"));
                } else if (request.getStudyPlan()) {
                    result.append(SINGLE_SPACE);
                    result.append(BundleUtil.getString(Bundle.ACADEMIC, getDocumentRequest().getLanguage(),
                            "documents.DegreeFinalizationCertificate.exceptionalConclusionInfo.studyPlan.one"));
                    result.append(SINGLE_SPACE).append(date);
                    result.append(SINGLE_SPACE);
                    result.append(BundleUtil.getString(Bundle.ACADEMIC, getDocumentRequest().getLanguage(),
                            "documents.DegreeFinalizationCertificate.exceptionalConclusionInfo.studyPlan.two"));
                }
            }
        }

        return result.toString();
    }

    static final public String getDegreeFinalizationGrade(final Integer finalGrade) {
        return getDegreeFinalizationGrade(finalGrade, I18N.getLocale());
    }

    static final public String getDegreeFinalizationGrade(final Integer finalGrade, final Locale locale) {
        final StringBuilder result = new StringBuilder();

        result.append(", ").append(BundleUtil.getString(Bundle.ACADEMIC, locale, "documents.registration.final.arithmetic.mean"));
        result.append(SINGLE_SPACE).append(BundleUtil.getString(Bundle.ACADEMIC, locale, "label.of.both"));
        result.append(SINGLE_SPACE).append(finalGrade);
        result.append(" (").append(BundleUtil.getString(Bundle.ENUMERATION, locale, finalGrade.toString()));
        result.append(") ").append(BundleUtil.getString(Bundle.ACADEMIC, locale, "values"));

        return result.toString();
    }

    final private String getDegreeFinalizationEcts(DegreeFinalizationCertificateRequest request) {
        final StringBuilder res = new StringBuilder();

        if (getDocumentRequest().isToShowCredits()) {
            res.append(SINGLE_SPACE).append(
                    BundleUtil.getString(Bundle.ACADEMIC, getDocumentRequest().getLanguage(),
                            "documents.DegreeFinalizationCertificate.creditsInfo"));
            res.append(SINGLE_SPACE).append(String.valueOf(request.getEctsCredits())).append(getCreditsDescription());
        }

        return res.toString();
    }

    final private String getDiplomaDescription() {
        final StringBuilder res = new StringBuilder();

        final Degree degree = getDocumentRequest().getDegree();
        final DegreeType degreeType = degree.getDegreeType();
        if (!getDocumentRequest().getProgramConclusion().getGraduationTitle().isEmpty()) {
            res.append(", ");
            if (getDocumentRequest().getRegistryCode() != null) {
                res.append(BundleUtil.getString(Bundle.ACADEMIC, getDocumentRequest().getLanguage(),
                        "documents.DegreeFinalizationCertificate.registryNumber"));
                res.append(SINGLE_SPACE);
                res.append(getDocumentRequest().getRegistryCode().getCode());
            } else {
                res.append(BundleUtil.getString(Bundle.ACADEMIC, getDocumentRequest().getLanguage(),
                        "documents.DegreeFinalizationCertificate.diplomaDescription.one"));
                if (degreeType.isAdvancedFormationDiploma()) {
                    // Do Nothing
                } else if (degreeType.isSpecializationDegree()) {
                    res.append(SINGLE_SPACE);
                    res.append(BundleUtil.getString(Bundle.ACADEMIC, getDocumentRequest().getLanguage(),
                            "documents.DegreeFinalizationCertificate.diplomaDescription.diploma"));
                } else {
                    res.append(SINGLE_SPACE);
                    res.append(BundleUtil.getString(Bundle.ACADEMIC, getDocumentRequest().getLanguage(),
                            "documents.DegreeFinalizationCertificate.diplomaDescription.letter"));
                }
            }
        }

        return res.toString();
    }

    final private String getDetailedInfoIntro(final DegreeFinalizationCertificateRequest request) {
        final StringBuilder res = new StringBuilder();

        if (request.getDetailed()) {
            res.append(SINGLE_SPACE)
                    .append(BundleUtil.getString(Bundle.ACADEMIC, getDocumentRequest().getLanguage(),
                            "documents.DegreeFinalizationCertificate.detailedInfoIntro")).append(":");
        } else {
            res.append(".");
        }

        return res.toString();
    }

    final private String getDegreeFinalizationInfo(final DegreeFinalizationCertificateRequest request) {
        final StringBuilder result = new StringBuilder();

        if (request.getDetailed()) {
            final SortedSet<ICurriculumEntry> entries =
                    new TreeSet<ICurriculumEntry>(ICurriculumEntry.COMPARATOR_BY_EXECUTION_PERIOD_AND_NAME_AND_ID);
            entries.addAll(request.getEntriesToReport());

            final Map<Unit, String> academicUnitIdentifiers = new HashMap<Unit, String>();
            reportEntries(result, entries, academicUnitIdentifiers);

            if (getDocumentRequest().isToShowCredits()) {
                result.append(getRemainingCreditsInfo(request.getCurriculum()));
            }

            result.append(generateEndLine());

            if (!academicUnitIdentifiers.isEmpty()) {
                result.append(LINE_BREAK).append(getAcademicUnitInfo(academicUnitIdentifiers, request.getMobilityProgram()));
            }
        }

        return result.toString();
    }

    final private void reportEntries(final StringBuilder result, final SortedSet<ICurriculumEntry> entries,
            final Map<Unit, String> academicUnitIdentifiers) {
        for (final ICurriculumEntry entry : entries) {
            reportEntry(result, entry, academicUnitIdentifiers);
        }
    }

    final private void reportEntry(final StringBuilder result, final ICurriculumEntry entry, final Map<Unit, String> unitIDs) {
        result.append(
                FenixStringTools.multipleLineRightPadWithSuffix(getCurriculumEntryName(unitIDs, entry), LINE_LENGTH, END_CHAR,
                        getCreditsAndGradeInfo(entry))).append(LINE_BREAK);
    }

    final private String getCreditsAndGradeInfo(final ICurriculumEntry entry) {
        final StringBuilder result = new StringBuilder();

        if (getDocumentRequest().isToShowCredits()) {
            getCreditsInfo(result, entry);
        }
        result.append(BundleUtil.getString(Bundle.ACADEMIC, getDocumentRequest().getLanguage(), "label.with"));

        final Grade grade = entry.getGrade();
        result.append(SINGLE_SPACE).append(grade.getValue());
        result.append(StringUtils.rightPad("(" + grade.getExtendedValue().getContent(getDocumentRequest().getLanguage()) + ")",
                SUFFIX_LENGTH, ' '));
        String values = BundleUtil.getString(Bundle.ACADEMIC, getDocumentRequest().getLanguage(), "values");
        result.append(grade.isNumeric() ? values : StringUtils.rightPad(EMPTY_STR, values.length(), ' '));

        return result.toString();
    }

    @Override
    protected void addPriceFields() {
        final CertificateRequest certificateRequest = getDocumentRequest();
        final CertificateRequestPR certificateRequestPR = (CertificateRequestPR) getPostingRule();

        final Money amountPerPage = certificateRequestPR.getAmountPerPage();
        final Money baseAmountPlusAmountForUnits =
                certificateRequestPR.getBaseAmount().add(
                        certificateRequestPR.getAmountForUnits(certificateRequest.getNumberOfUnits()));
        final Money urgencyAmount = certificateRequest.getUrgentRequest() ? certificateRequestPR.getBaseAmount() : Money.ZERO;

        addParameter("amountPerPage", amountPerPage);
        addParameter("baseAmountPlusAmountForUnits", baseAmountPlusAmountForUnits);
        addParameter("urgencyAmount", urgencyAmount);
        addParameter("printPriceFields", printPriceParameters(certificateRequest));
    }

}
