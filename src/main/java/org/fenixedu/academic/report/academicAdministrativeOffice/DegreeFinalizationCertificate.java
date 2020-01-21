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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.Grade;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DegreeFinalizationCertificateRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.IDocumentRequest;
import org.fenixedu.academic.domain.student.MobilityProgram;
import org.fenixedu.academic.domain.student.curriculum.ICurriculum;
import org.fenixedu.academic.domain.student.curriculum.ICurriculumEntry;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.FenixStringTools;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.I18N;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class DegreeFinalizationCertificate extends AdministrativeOfficeDocument {

    protected DegreeFinalizationCertificate(final IDocumentRequest documentRequest) {
        super(documentRequest);
    }

    @Override
    public DegreeFinalizationCertificateRequest getDocumentRequest() {
        return (DegreeFinalizationCertificateRequest) super.getDocumentRequest();
    }

    @Override
    protected void fillReport() {
        super.fillReport();

        final Person person = getDocumentRequest().getPerson();

        getPayload().addProperty("today", getFormattedCurrentDate());
        getPayload().addProperty("studentNumber", getRegistration().getNumber().toString());
        getPayload().addProperty("documentNumber", getDocumentRequest().getServiceRequestNumberYear());
        getPayload().addProperty("institutionName", getInstitutionName());
        getPayload().addProperty("universityName", getUniversityName(new DateTime()));
        getPayload().addProperty("location", getLocation());
        getPayload().addProperty("administrativeOfficeCoordinator", getAdministrativeOffice().getCoordinator().getPerson().getName());
        getPayload().addProperty("administrativeOfficeName", getI18NText(getAdministrativeOffice().getName()));
        getPayload().addProperty("studentName", person.getName());
        getPayload().addProperty("studentDocumentIdType", person.getIdDocumentType().getLocalizedName(getLocale()));
        getPayload().addProperty("studentDocumentIdNumber", person.getDocumentIdNumber());
        getPayload().addProperty("studentBirthLocale", getBirthLocale(person, false));
        getPayload().addProperty("studentNationality", person.getCountry().getFilteredNationality(getLocale()));
        getPayload().addProperty("secondParagraph",hasGraduationTitle() ? getSecondParagraphGraduation() : getSecondParagraph());

        getPayload().addProperty("degreeFinalizationInfo", getDegreeFinalizationInfo());
        getPayload().addProperty("diplomaDescription", getDiplomaDescription());
        getPayload().addProperty("creditsDescription", getCreditsDescription());
        getPayload().add("degreeFinalizationInfoEntries", getDegreeFinalizationInfoEntries());
    }

    private String getSecondParagraph() {

        final StringBuilder result = new StringBuilder();

        result.append(BundleUtil.getString(Bundle.ACADEMIC, getLanguage(), "conclusion.document.concluded.lowercase"));
        result.append(SINGLE_SPACE);
        result.append(BundleUtil.getString(Bundle.ACADEMIC, getLanguage(), "label.the.male"));
        result.append(SINGLE_SPACE);
        result.append(getDegreeDescription());
        result.append(",");

        if (getDocumentRequest().getBranch() == null || getDocumentRequest().getBranch().isEmpty()) {
            result.append("");
        } else {
            result.append(",");
        }

        result.append(getDegreeFinalizationDate());
        result.append(getExceptionalConclusionInfo());

        if (getDocumentRequest().getAverage()) {
            result.append(getDegreeFinalizationGrade(getDocumentRequest().getFinalAverageGrade(), getLanguage()));
        }

        result.append(getDegreeFinalizationEcts());
        result.append(getDetailedInfoIntro());

        return result.toString();

    }

    public String getSecondParagraphGraduation() {

        final DegreeFinalizationCertificateRequest req = getDocumentRequest();
        final StringBuilder result = new StringBuilder();
        final Locale language = getDocumentRequest().getLanguage();

        Optional<String> associatedInstitutions = getDocumentRequest().getAssociatedInstitutionsContent();

        result.append(BundleUtil.getString(Bundle.ACADEMIC, language, "documents.DegreeFinalizationCertificate.graduateTitleInfo"));
        result.append(SINGLE_SPACE);
        result.append(req.getGraduateTitle(language));
        result.append(SINGLE_SPACE);
        final String labelOfMale = BundleUtil.getString(Bundle.ACADEMIC, language, "documents.DegreeFinalizationCertificate.label.of.male");
        if (!Strings.isNullOrEmpty(labelOfMale)) {
            result.append(labelOfMale);
            result.append(SINGLE_SPACE);
        }
        result.append(getDegreeDescriptionWithGraduation());
        result.append(",");
        
        associatedInstitutions.ifPresent(s ->
            result
                .append(SINGLE_SPACE)
                .append(BundleUtil.getString(Bundle.ACADEMIC, language, "documents.in.association.with"))
                .append(SINGLE_SPACE)
                .append(s)
                .append(",")
        );
        
        result.append(getDegreeFinalizationDate());
        result.append(getExceptionalConclusionInfo());

        if (req.getAverage()) {
            result.append(getDegreeFinalizationGradeWithGradeScale(req.getFinalAverageGrade(), getLocale()));
        }

        result.append(getDetailedInfoIntro());

        return result.toString();
    }

    @Override
    protected String getDegreeDescription() {
        return getRegistration()
                   .getDegreeDescription(getDocumentRequest().getConclusionYear(), getDocumentRequest().getProgramConclusion(),
                       getDocumentRequest().getLanguage());
    }

    public String getPrefix(DegreeType degreeType, final Locale locale) {
        if (degreeType.isAdvancedSpecializationDiploma() || degreeType.isAdvancedFormationDiploma()) {
            return degreeType.getPrefix(locale);
        }
        return BundleUtil.getString(Bundle.ACADEMIC, locale, "documents.DegreeFinalizationCertificate.degreeTypePrefix");
    }

    private String getDegreeDescriptionWithGraduation() {
        final Locale locale = getDocumentRequest().getLanguage();
        List<String> parts = new ArrayList<>();
        parts.add(getRegistration().getDegree().getDegreeType().getPrefix(locale));
        if (getDocumentRequest().getProgramConclusion() != null && !Strings.isNullOrEmpty(
            getDocumentRequest().getProgramConclusion().getDescription().getContent(locale))) {
            parts.add(getDocumentRequest().getProgramConclusion().getDescription().getContent(locale).toUpperCase());
            parts.add(BundleUtil.getString(Bundle.ACADEMIC, locale, "label.in"));
        }
        parts.add(getRegistration().getDegree().getFilteredName(getDocumentRequest().getConclusionYear(), locale).toUpperCase());
        return Joiner.on(SINGLE_SPACE).join(parts.stream().map(String::trim).collect(Collectors.toList()));
    }

    private String getDegreeFinalizationDate() {
        final StringBuilder result = new StringBuilder();

        if (!getDocumentRequest().mustHideConclusionDate()) {
            result.append(SINGLE_SPACE)
                .append(BundleUtil.getString(Bundle.ACADEMIC, getDocumentRequest().getLanguage(), "label.onThe"));
            result.append(SINGLE_SPACE).append(getFormattedDate(getDocumentRequest().getConclusionDate().toLocalDate()));
        }

        return result.toString();
    }

    private String getExceptionalConclusionInfo() {
        final StringBuilder result = new StringBuilder();

        if (getDocumentRequest().hasExceptionalConclusionInfo()) {
            if (getDocumentRequest().getTechnicalEngineer()) {
                result.append(SINGLE_SPACE);
                result.append(BundleUtil.getString(Bundle.ACADEMIC, getDocumentRequest().getLanguage(),
                    "documents.DegreeFinalizationCertificate.exceptionalConclusionInfo.technicalEngineer"));
            } else {
                final String date = getDocumentRequest().getExceptionalConclusionDate().toString(DD_MMMM_YYYY, getLocale());
                if (getDocumentRequest().getInternshipAbolished()) {
                    result.append(SINGLE_SPACE)
                        .append(BundleUtil.getString(Bundle.ACADEMIC, getDocumentRequest().getLanguage(), "label.in"));
                    result.append(SINGLE_SPACE).append(date);
                    result.append(", ");
                    result.append(BundleUtil.getString(Bundle.ACADEMIC, getDocumentRequest().getLanguage(),
                        "documents.DegreeFinalizationCertificate.exceptionalConclusionInfo.internshipAbolished"));
                } else if (getDocumentRequest().getInternshipApproved()) {
                    result.append(SINGLE_SPACE)
                        .append(BundleUtil.getString(Bundle.ACADEMIC, getDocumentRequest().getLanguage(), "label.in"));
                    result.append(SINGLE_SPACE).append(date);
                    result.append(", ");
                    result.append(BundleUtil.getString(Bundle.ACADEMIC, getDocumentRequest().getLanguage(),
                        "documents.DegreeFinalizationCertificate.exceptionalConclusionInfo.internshipApproved"));
                } else if (getDocumentRequest().getStudyPlan()) {
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

    private String getDegreeFinalizationGradeWithGradeScale(final Grade finalGrade, final Locale locale) {
        final StringBuilder result = new StringBuilder();
        result.append(getDegreeFinalizationGrade(finalGrade, locale));
        result.append(", ");
        result.append(BundleUtil.getString(Bundle.ACADEMIC, locale, "documents.DegreeFinalizationCertificate.onscale"));
        result.append(SINGLE_SPACE).append(finalGrade.getGradeScale().getDescription());
        return result.toString();
    }

    static final public String getDegreeFinalizationGrade(final Grade finalGrade) {
        return getDegreeFinalizationGrade(finalGrade, I18N.getLocale());
    }

    static final public String getDegreeFinalizationGrade(final Grade finalGrade, final Locale locale) {
        final StringBuilder result = new StringBuilder();

        result.append(", ").append(BundleUtil.getString(Bundle.ACADEMIC, locale, "documents.registration.final.arithmetic.mean"));
        result.append(SINGLE_SPACE).append(BundleUtil.getString(Bundle.ACADEMIC, locale, "label.of.both"));
        result.append(SINGLE_SPACE).append(finalGrade.getValue());
        result.append(" (").append(BundleUtil.getString(Bundle.ENUMERATION, locale, finalGrade.getValue()));
        result.append(") ").append(BundleUtil.getString(Bundle.ACADEMIC, locale, "values"));
        return result.toString();
    }

    final private String getDegreeFinalizationEcts() {
        final StringBuilder res = new StringBuilder();

        if (getDocumentRequest().isToShowCredits()) {
            res.append(SINGLE_SPACE).append(BundleUtil.getString(Bundle.ACADEMIC, getDocumentRequest().getLanguage(),
                "documents.DegreeFinalizationCertificate.creditsInfo"));
            res.append(SINGLE_SPACE).append(String.valueOf(getDocumentRequest().getEctsCredits()))
                .append(getCreditsDescription());
        }

        return res.toString();
    }

    final private String getDiplomaDescription() {
        final StringBuilder res = new StringBuilder();

        final Degree degree = getDocumentRequest().getDegree();
        final DegreeType degreeType = degree.getDegreeType();
        if (hasGraduationTitle()) {
            if (getDocumentRequest().getRegistryCode() != null) {
                res.append(BundleUtil.getString(Bundle.ACADEMIC, getDocumentRequest().getLanguage(),
                    "documents.DegreeFinalizationCertificate.registryNumber", getDocumentRequest().getRegistryCode().getCode()));
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

    private boolean hasGraduationTitle() {
        return !getDocumentRequest().getProgramConclusion().getGraduationTitle().isEmpty();
    }

    final private String getDetailedInfoIntro() {
        final StringBuilder res = new StringBuilder();

        if (getDocumentRequest().getDetailed()) {
            res.append(",");
            res.append(SINGLE_SPACE).append(BundleUtil.getString(Bundle.ACADEMIC, getDocumentRequest().getLanguage(),
                "documents.DegreeFinalizationCertificate.detailedInfoIntro"));
        } else {
            res.append(".");
        }

        return res.toString();
    }

    private Optional<String> getRemainingCreditsInfoValue(final ICurriculum curriculum) {
        final BigDecimal remainingCredits = curriculum.getRemainingCredits();

        if (!Objects.equals(remainingCredits, BigDecimal.ZERO)) {
            return Optional.of(remainingCredits.toString());
        }

        return Optional.empty();
    }

    final protected JsonArray getAcademicUnitInfoValues(final Map<Unit, String> unitIDs, final MobilityProgram mobilityProgram) {
        final JsonArray result = new JsonArray();

        for (final Entry<Unit, String> academicUnitId : unitIDs.entrySet()) {
            final JsonObject entry = new JsonObject();

            entry.addProperty("id", academicUnitId.getValue());
            entry.addProperty("name", getMLSTextContent(academicUnitId.getKey().getPartyName()));
            if (mobilityProgram != null) {
                entry.addProperty("mobilityProgram", mobilityProgram.getDescription(getLocale()));
            }

            result.add(entry);
        }
        return result;
    }

    private JsonArray getDegreeFinalizationInfoEntries() {
        if (getDocumentRequest().getDetailed()) {
            JsonArray result = new JsonArray();

            final SortedSet<ICurriculumEntry> entries =
                new TreeSet<ICurriculumEntry>(ICurriculumEntry.COMPARATOR_BY_EXECUTION_PERIOD_AND_NAME_AND_ID);
            entries.addAll(getDocumentRequest().getEntriesToReport());

            final Map<Unit, String> academicUnitIdentifiers = new HashMap<Unit, String>();
            reportEntries(result, entries, academicUnitIdentifiers);

            if (getDocumentRequest().isToShowCredits()) {
                getRemainingCreditsInfoValue(getDocumentRequest().getCurriculum()).ifPresent(value ->{
                    getPayload().addProperty("remainingCreditsInfoValue", value);
                });
            }

            if (!academicUnitIdentifiers.isEmpty()) {
                getPayload().add("academicUnitInfoValues", getAcademicUnitInfoValues(academicUnitIdentifiers, getDocumentRequest().getMobilityProgram()));
            }

            return result;
        }
        return null;
    }

    final private String getDegreeFinalizationInfo() {
        final StringBuilder result = new StringBuilder();

        if (getDocumentRequest().getDetailed()) {
            final SortedSet<ICurriculumEntry> entries =
                new TreeSet<ICurriculumEntry>(ICurriculumEntry.COMPARATOR_BY_EXECUTION_PERIOD_AND_NAME_AND_ID);
            entries.addAll(getDocumentRequest().getEntriesToReport());

            final Map<Unit, String> academicUnitIdentifiers = new HashMap<Unit, String>();
            reportEntries(result, entries, academicUnitIdentifiers);

            if (getDocumentRequest().isToShowCredits()) {
                result.append(getRemainingCreditsInfo(getDocumentRequest().getCurriculum()));
            }

            result.append(generateEndLine());

            if (!academicUnitIdentifiers.isEmpty()) {
                result.append(LINE_BREAK)
                    .append(getAcademicUnitInfo(academicUnitIdentifiers, getDocumentRequest().getMobilityProgram()));
            }
        }

        return result.toString();
    }

    final private void reportEntries(final JsonArray result, final SortedSet<ICurriculumEntry> entries,
                                        final Map<Unit, String> academicUnitIdentifiers) {
        for (final ICurriculumEntry entry : entries) {
            reportEntry(result, entry, academicUnitIdentifiers);
        }
    }

    final private void reportEntry(final JsonArray result, final ICurriculumEntry entry, final Map<Unit, String> unitIDs) {
        JsonObject json = new JsonObject();
        json.addProperty("name", getCurriculumEntryName(unitIDs, entry));
        json.addProperty("credits", entry.getEctsCreditsForCurriculum());
        JsonObject grade = new JsonObject();
        grade.addProperty("value", entry.getGradeValue());
        grade.addProperty("extended", entry.getGrade().getExtendedValue().getContent(getDocumentRequest().getLanguage()));
        json.add("grade", grade);
        result.add(json);
    }

    final private void reportEntries(final StringBuilder result, final SortedSet<ICurriculumEntry> entries,
                                        final Map<Unit, String> academicUnitIdentifiers) {
        for (final ICurriculumEntry entry : entries) {
            reportEntry(result, entry, academicUnitIdentifiers);
        }
    }

    final private void reportEntry(final StringBuilder result, final ICurriculumEntry entry, final Map<Unit, String> unitIDs) {
        result.append(FenixStringTools
                          .multipleLineRightPadWithSuffix(getCurriculumEntryName(unitIDs, entry), LINE_LENGTH, END_CHAR,
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
    }
}
