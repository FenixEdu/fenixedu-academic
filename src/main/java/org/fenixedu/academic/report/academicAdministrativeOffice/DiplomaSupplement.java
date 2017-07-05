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
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeOfficialPublication;
import org.fenixedu.academic.domain.DegreeSpecializationArea;
import org.fenixedu.academic.domain.IEnrolment;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.degreeStructure.EctsGraduationGradeConversionTable;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.AcademicalInstitutionType;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.organizationalStructure.UniversityUnit;
import org.fenixedu.academic.domain.phd.serviceRequests.documentRequests.PhdDiplomaSupplementRequest;
import org.fenixedu.academic.domain.serviceRequests.IDiplomaSupplementRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DiplomaSupplementRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.IDocumentRequest;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.student.curriculum.ExtraCurricularActivity;
import org.fenixedu.academic.domain.student.curriculum.ExtraCurricularActivityType;
import org.fenixedu.academic.domain.student.curriculum.ICurriculumEntry;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.Dismissal;
import org.fenixedu.academic.domain.studentCurriculum.ExternalEnrolment;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.StringFormatter;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;

public class DiplomaSupplement extends AdministrativeOfficeDocument {

    protected DiplomaSupplement(final IDocumentRequest documentRequest, final Locale locale) {
        super(documentRequest, locale);
    }

    @Override
    public String getReportTemplateKey() {
        return getClass().getName();
    }

    @Override
    protected IDiplomaSupplementRequest getDocumentRequest() {
        return (IDiplomaSupplementRequest) documentRequestDomainReference;
    }

    @Override
    protected void fillReport() {
        addParameter("bundle", ResourceBundle.getBundle(getBundle(), getLocale()));
        addParameter("name", StringFormatter.prettyPrint(getDocumentRequest().getPerson().getName().trim()));

        // Group 1
        fillGroup1();

        // Group 2
        fillGroup2();

        // Group 3
        fillGroup3();

        // Group 4
        fillGroup4();

        // Group 5
        fillGroup5();

        // Group 6
        fillGroup6();

        // Group 7
        fillGroup7();

        // Group 8
        fillGroup8();
    }

    protected void fillGroup8() {
        addParameter("langSuffix", getLanguage().getLanguage());
    }

    protected void fillGroup7() {
        final UniversityUnit institutionsUniversityUnit = UniversityUnit.getInstitutionsUniversityUnit();
        addParameter("day", new YearMonthDay().toString(DD_SLASH_MM_SLASH_YYYY, getLocale()));
        addParameter("universityPrincipal", institutionsUniversityUnit.getCurrentPrincipal());
    }

    protected void fillGroup6() {
        addExtraCurricularActivities();
    }

    protected void fillGroup5() {
        final StringBuilder access = new StringBuilder();
        if (getDocumentRequest().getRequestedCycle() == CycleType.THIRD_CYCLE) {
            access.append(BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "diploma.supplement.five.one.three"));
        } else {
            String degreeDesignation = getDegreeDesignation(getLocale());
            access.append(BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "diploma.supplement.five.one.one")).append(
                    SINGLE_SPACE);
            access.append(degreeDesignation).append(SINGLE_SPACE);
            access.append(BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "diploma.supplement.five.one.two"));
        }
        addParameter("accessToHigherLevelOfEducation", access.toString());
        addProfessionalStatus();
    }

    protected void fillGroup4() {
        String degreeDesignation = getDegreeDesignation(getLocale());

        addProgrammeRequirements(degreeDesignation);
        addEntriesParameters();
        addParameter("classificationSystem",
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "diploma.supplement.four.four.one"));
        addParameter("finalAverage", getDocumentRequest().getFinalAverage());

        String finalAverageQualified = getDocumentRequest().getFinalAverageQualified(getLocale());

        if (finalAverageQualified != null) {
            addParameter("finalAverageQualified", BundleUtil.getString(Bundle.ACADEMIC, getLocale(), finalAverageQualified));
        }

        EctsGraduationGradeConversionTable table = getDocumentRequest().getGraduationConversionTable();

        if (getDocumentRequest().isRequestForPhd()) {
            addParameter("thesisFinalGrade",
                    ((PhdDiplomaSupplementRequest) getDocumentRequest()).getThesisFinalGrade(getLocale()));
        }

        addParameter("ectsGradeConversionTable", table.getEctsTable());
        addParameter("ectsGradePercentagesTable", table.getPercentages());
    }

    protected void fillGroup3() {
        addParameter(
                "qualificationLevel",
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "diploma.supplement.qualification."
                        + getDocumentRequest().getRequestedCycle()));
        addParameter("years", getDocumentRequest().getNumberOfCurricularYears());
        addParameter("semesters", getDocumentRequest().getNumberOfCurricularSemesters());
        addParameter(
                "weeksOfStudyPerYear",
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "diploma.supplement.weeksOfStudyPerYear."
                        + getDocumentRequest().getRequestedCycle()));
        double ectsCreditsValue = getDocumentRequest().getEctsCredits();
        String ectsCredits =
                (long) ectsCreditsValue == ectsCreditsValue ? "" + Long.toString((long) ectsCreditsValue) : Double
                        .toString(ectsCreditsValue);
        addParameter("ectsCredits", ectsCredits);
    }

    protected String fillGroup2() {

        Locale locale = Locale.getDefault();

        final UniversityUnit institutionsUniversityUnit = getUniversity(getDocumentRequest().getRequestDate());
        String degreeDesignation = getDegreeDesignation(locale);

        String graduateTitleNative = getDocumentRequest().getGraduateTitle(locale).split(" ")[0];

        addParameter("graduateTitle", degreeDesignation + "\n" + graduateTitleNative);
        addParameter("universityName", institutionsUniversityUnit.getName());
        addParameter(
                "universityStatus",
                BundleUtil.getString(Bundle.ENUMERATION, locale, AcademicalInstitutionType.class.getSimpleName() + "."
                        + institutionsUniversityUnit.getInstitutionType().getName()));
        addParameter("institutionName", Bennu.getInstance().getInstitutionUnit().getName());
        addParameter("institutionStatus",
                BundleUtil.getString(Bundle.ENUMERATION, locale, Bennu.getInstance().getInstitutionUnit().getType().getName())
                        + SINGLE_SPACE + BundleUtil.getString(Bundle.ACADEMIC, locale, "diploma.supplement.of") + SINGLE_SPACE
                        + institutionsUniversityUnit.getName());

        addParameter("prevailingScientificArea", getDocumentRequest().getPrevailingScientificArea(getLocale()));
        if (CycleType.FIRST_CYCLE.equals(getDocumentRequest().getRequestedCycle())) {
            addParameter("languages", BundleUtil.getString(Bundle.ENUMERATION, getLocale(), "pt"));
        } else {
            addParameter(
                    "languages",
                    BundleUtil.getString(Bundle.ENUMERATION, getLocale(), "pt") + SINGLE_SPACE
                            + BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.and").toLowerCase() + SINGLE_SPACE
                            + BundleUtil.getString(Bundle.ENUMERATION, getLocale(), "en"));
        }
        return degreeDesignation;
    }

    protected void fillGroup1() {
        /*
         * Oddly a subreport is only rendered if the specified data source is
         * not empty. All reports have "entries" parameter as data source.
         * "entries" may be empty in case of phd diploma supplement so add a
         * dummy data source
         */

        addParameter("dummyDataSource", Arrays.asList(Boolean.TRUE));

        Person person = getDocumentRequest().getPerson();

        addParameter("familyName", person.getFamilyNames());
        addParameter("givenName", person.getGivenNames());
        addParameter("birthDay", person.getDateOfBirthYearMonthDay().toString(DD_SLASH_MM_SLASH_YYYY, getLocale()));
        addParameter("nationality",
                StringFormatter.prettyPrint(person.getCountry().getCountryNationality().getContent(getLocale())));
        addParameter(
                "documentIdType",
                applyMessageArguments(BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "diploma.supplement.one.five.one"),
                        BundleUtil.getString(Bundle.ENUMERATION, getLocale(), person.getIdDocumentType().getName())));
        addParameter("documentIdNumber", person.getDocumentIdNumber());

        addParameter("registrationNumber", getDocumentRequest().getRegistrationNumber());
        addParameter("isExemptedFromStudy", getDocumentRequest().isExemptedFromStudy());
        addParameter("isForPhd", getDocumentRequest().isRequestForPhd());
        addParameter("isForRegistration", getDocumentRequest().isRequestForRegistration());
    }

    private String getDegreeDesignation(Locale locale) {

        IDiplomaSupplementRequest request = getDocumentRequest();

        ArrayList<String> result = new ArrayList<>();
        final String graduationLevel = request.getProgramConclusion().getGraduationLevel().getContent(locale);

        if (!Strings.isNullOrEmpty(graduationLevel)) {
            result.add(graduationLevel);
            result.add(BundleUtil.getString(Bundle.ACADEMIC, locale, "label.in"));
        }

        //TODO: phd-refactor registration should always be present in phd

        Degree degree;

        if (request.hasRegistration()) {
            degree = request.getRegistration().getDegree();
        } else {
            degree = ((PhdDiplomaSupplementRequest) request).getPhdIndividualProgramProcess().getPhdProgram().getDegree();
        }

        if (request.isRequestForPhd()) {
            result.add(degree.getNameI18N(request.getConclusionYear()).getContent(locale));
        } else {
            result.add(request.getProgramConclusion().groupFor(request.getRegistration()).map(CurriculumGroup::getDegreeModule)
                    .map(dm -> dm.getDegreeNameWithTitleSuffix(request.getConclusionYear(), locale))
                    .orElse(degree.getNameI18N(request.getConclusionYear()).getContent(locale)));
        }

        return Joiner.on(" ").join(result);
    }

    private void addProgrammeRequirements(String graduateDegree) {
        String labelThe =
                CycleType.FIRST_CYCLE.equals(getDocumentRequest().getRequestedCycle()) ? BundleUtil.getString(Bundle.ACADEMIC,
                        getLocale(), "label.the.female") : BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.the.male");
        double ectsCreditsValue = getDocumentRequest().getEctsCredits();
        String ectsCredits =
                (long) ectsCreditsValue == ectsCreditsValue ? "" + Long.toString((long) ectsCreditsValue) : Double
                        .toString(ectsCreditsValue);
        DegreeOfficialPublication dr = getDocumentRequest().getDegreeOfficialPublication();

        if (dr == null) {
            throw new DomainException("error.DiplomaSupplement.degreeOfficialPublicationNotFound");
        }

        String officialPublication = dr.getOfficialReference();

        String programmeRequirements;

        if (getDocumentRequest().isRequestForPhd()) {
            programmeRequirements =
                    applyMessageArguments(BundleUtil.getString(Bundle.ACADEMIC, getLocale(),
                            "diploma.supplement.four.two.programmerequirements.template.noareas.with.official.publication"),
                            labelThe, graduateDegree, ectsCredits, officialPublication);
        } else if (getDocumentRequest().getRequestedCycle().equals(CycleType.FIRST_CYCLE)
                || dr.getSpecializationAreaSet().size() == 0) {
            programmeRequirements =
                    applyMessageArguments(BundleUtil.getString(Bundle.ACADEMIC, getLocale(),
                            "diploma.supplement.four.two.programmerequirements.template.noareas"), labelThe, graduateDegree,
                            ectsCredits);
        } else {
            List<String> areas = new ArrayList<String>();
            for (DegreeSpecializationArea area : dr.getSpecializationAreaSet()) {
                areas.add(area.getName().getContent(getLanguage()));
            }
            programmeRequirements =
                    applyMessageArguments(BundleUtil.getString(Bundle.ACADEMIC, getLocale(),
                            "diploma.supplement.four.two.programmerequirements.template.withareas"), labelThe, graduateDegree,
                            ectsCredits, Integer.toString(areas.size()), StringUtils.join(areas, "; "), officialPublication);
        }
        programmeRequirements = programmeRequirements.substring(0, 1).toUpperCase() + programmeRequirements.substring(1);
        addParameter("programmeRequirements", programmeRequirements);
    }

    protected void addProfessionalStatus() {
        String professionalStatus;

        if (!CycleType.SECOND_CYCLE.equals(getDocumentRequest().getRequestedCycle())) {
            professionalStatus =
                    BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "diploma.supplement.professionalstatus.notapplicable");
        } else {
            DiplomaSupplementRequest request = (DiplomaSupplementRequest) getDocumentRequest();
            String degreeSigla = request.getRegistration().getDegree().getSigla();

            if (degreeSigla.equals("MA")) {
                professionalStatus =
                        BundleUtil.getString(Bundle.ACADEMIC, getLocale(),
                                "diploma.supplement.professionalstatus.credited.arquitect.withintership");
            } else if (degreeSigla.equals("MMA") || degreeSigla.equals("MQ") || degreeSigla.equals("MUOT")
                    || degreeSigla.equals("MPOT") || degreeSigla.equals("MFarm")) {
                professionalStatus =
                        BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "diploma.supplement.professionalstatus.notapplicable");
            } else {
                professionalStatus =
                        BundleUtil.getString(Bundle.ACADEMIC, getLocale(),
                                "diploma.supplement.professionalstatus.credited.engineer");
            }
        }
        addParameter("professionalStatus", professionalStatus);
    }

    private void addExtraCurricularActivities() {
        Student student = getDocumentRequest().getStudent();
        if (!student.getExtraCurricularActivitySet().isEmpty()) {
            List<String> activities = new ArrayList<String>();
            Map<ExtraCurricularActivityType, List<ExtraCurricularActivity>> activityMap =
                    new HashMap<ExtraCurricularActivityType, List<ExtraCurricularActivity>>();
            for (ExtraCurricularActivity activity : student.getExtraCurricularActivitySet()) {
                if (!activityMap.containsKey(activity.getType())) {
                    activityMap.put(activity.getType(), new ArrayList<ExtraCurricularActivity>());
                }
                activityMap.get(activity.getType()).add(activity);
            }
            for (Entry<ExtraCurricularActivityType, List<ExtraCurricularActivity>> entry : activityMap.entrySet()) {
                StringBuilder activityText = new StringBuilder();
                activityText.append(BundleUtil.getString(Bundle.ACADEMIC, getLocale(),
                        "diploma.supplement.six.one.extracurricularactivity.heading"));
                activityText.append(SINGLE_SPACE);
                activityText.append(entry.getKey().getName().getContent(getLanguage()));
                activityText.append(SINGLE_SPACE);
                List<String> activityTimings = new ArrayList<String>();
                for (ExtraCurricularActivity activity : entry.getValue()) {
                    activityTimings.add(BundleUtil.getString(Bundle.ACADEMIC, getLocale(),
                            "diploma.supplement.six.one.extracurricularactivity.time.heading")
                            + SINGLE_SPACE
                            + activity.getStart().toString("MM-yyyy")
                            + SINGLE_SPACE
                            + BundleUtil.getString(Bundle.ACADEMIC, getLocale(),
                                    "diploma.supplement.six.one.extracurricularactivity.time.separator")
                            + SINGLE_SPACE
                            + activity.getEnd().toString("MM-yyyy"));
                }
                activityText.append(StringUtils.join(activityTimings, ", "));
                activities.add(activityText.toString());
            }
            addParameter("extraCurricularActivities", StringUtils.join(activities, '\n') + ".");
        } else {
            addParameter("extraCurricularActivities",
                    BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "diploma.supplement.six.one.extracurricularactivity.none"));
        }
    }

    private String applyMessageArguments(String message, String... args) {
        for (int i = 0; i < args.length; i++) {
            message = message.replaceAll("\\{" + i + "\\}", args[i]);
        }
        return message;
    }

    private void addEntriesParameters() {
        final List<AcademicUnitEntry> identifiers = new ArrayList<AcademicUnitEntry>();

        if (getDocumentRequest().hasRegistration()) {
            Registration registration = getDocumentRequest().getRegistration();
            final List<DiplomaSupplementEntry> entries = new ArrayList<DiplomaSupplementEntry>();
            final Map<Unit, String> academicUnitIdentifiers = new HashMap<Unit, String>();

            for (ICurriculumEntry entry : registration.getCurriculum(getDocumentRequest().getRequestedCycle())
                    .getCurriculumEntries()) {
                entries.add(new DiplomaSupplementEntry(entry, academicUnitIdentifiers));
            }

            Collections.sort(entries);
            addParameter("entries", entries);

            for (final Entry<Unit, String> entry2 : academicUnitIdentifiers.entrySet()) {
                identifiers.add(new AcademicUnitEntry(entry2));
            }
        } else {
            addParameter("entries", new ArrayList<DiplomaSupplementEntry>());
        }

        addParameter("academicUnitIdentifiers", identifiers);
    }

    static final public Comparator<DiplomaSupplementEntry> COMPARATOR = new Comparator<DiplomaSupplementEntry>() {

        @Override
        public int compare(DiplomaSupplementEntry o1, DiplomaSupplementEntry o2) {
            final int c = o1.getExecutionYear().compareTo(o2.getExecutionYear());
            return c == 0 ? Collator.getInstance().compare(o1.getName(), o2.getName()) : c;
        }

    };

    public class DiplomaSupplementEntry implements Comparable<DiplomaSupplementEntry> {

        private final ICurriculumEntry entry;

        private final String executionYear;

        private final String name;

        private final String type;

        private final String duration;

        private final BigDecimal ectsCreditsForCurriculum;

        private final String gradeValue;

        private final String ectsScale;

        private final String academicUnitId;

        public DiplomaSupplementEntry(final ICurriculumEntry entry, final Map<Unit, String> academicUnitIdentifiers) {
            this.entry = entry;
            this.executionYear = entry.getExecutionYear().getYear();
            this.name = getMLSTextContent(entry.getPresentationName());
            DateTime processingDate = computeProcessingDateToLockECTSTableUse();
            if (entry instanceof IEnrolment) {
                IEnrolment enrolment = (IEnrolment) entry;
                this.type = BundleUtil.getString(Bundle.ENUMERATION, getLocale(), enrolment.getEnrolmentTypeName());
                this.duration =
                        BundleUtil.getString(Bundle.ACADEMIC, getLocale(),
                                enrolment.isAnual() ? "diploma.supplement.annual" : "diploma.supplement.semestral");

                this.ectsScale =
                        enrolment
                                .getEctsGrade(getDocumentRequest().getRegistration()
                                        .getStudentCurricularPlan(getDocumentRequest().getRequestedCycle()),
                                processingDate).getValue();
            } else if (entry instanceof Dismissal && ((Dismissal) entry).getCredits().isEquivalence()) {
                Dismissal dismissal = (Dismissal) entry;
                this.type = BundleUtil.getString(Bundle.ENUMERATION, getLocale(), dismissal.getEnrolmentTypeName());
                this.duration =
                        BundleUtil.getString(Bundle.ACADEMIC, getLocale(),
                                dismissal.isAnual() ? "diploma.supplement.annual" : "diploma.supplement.semestral");
                this.ectsScale = dismissal.getEctsGrade(processingDate).getValue();
            } else {
                throw new Error("The roof is on fire");
            }
            this.ectsCreditsForCurriculum = entry.getEctsCreditsForCurriculum();
            this.academicUnitId = obtainAcademicUnitIdentifier(academicUnitIdentifiers);
            this.gradeValue = entry.getGrade().getValue();
        }

        private DateTime computeProcessingDateToLockECTSTableUse() {
            DateTime date = documentRequestDomainReference.getProcessingDate();
            return date != null ? date : new DateTime();
        }

        public ICurriculumEntry getEntry() {
            return entry;
        }

        public String getExecutionYear() {
            return executionYear;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public String getDuration() {
            return duration;
        }

        public BigDecimal getEctsCreditsForCurriculum() {
            return ectsCreditsForCurriculum;
        }

        public String getGradeValue() {
            return gradeValue;
        }

        public String getEctsScale() {
            return ectsScale;
        }

        public String getAcademicUnitId() {
            return academicUnitId;
        }

        private String obtainAcademicUnitIdentifier(final Map<Unit, String> academicUnitIdentifiers) {
            final Unit unit =
                    entry instanceof ExternalEnrolment ? ((ExternalEnrolment) entry).getAcademicUnit() : Bennu.getInstance()
                            .getInstitutionUnit();
            return getAcademicUnitIdentifier(academicUnitIdentifiers, unit);
        }

        @Override
        public int compareTo(final DiplomaSupplementEntry o) {
            return COMPARATOR.compare(this, o);
        }

    }

    public class AcademicUnitEntry {
        private final String identifier;

        private final String name;

        public AcademicUnitEntry(final Entry<Unit, String> entry) {
            this.identifier = entry.getValue();
            Unit unit = entry.getKey();
            String name = getMLSTextContent(unit.getNameI18n());
            List<Unit> univs = unit.getParentUnits().stream().filter(u -> u.isUniversityUnit()).collect(Collectors.toList());
            if (!univs.isEmpty()) {
                final UniversityUnit institutionsUniversityUnit = getUniversity(getDocumentRequest().getRequestDate());
                if (univs.contains(institutionsUniversityUnit)) {
                    name = getMLSTextContent(institutionsUniversityUnit.getNameI18n()) + ", " + name;
                } else {
                    name = getMLSTextContent(univs.iterator().next().getNameI18n()) + ", " + name;
                }
            }
            this.name = name;
        }

        public String getIdentifier() {
            return identifier;
        }

        public String getName() {
            return name;
        }
    }

}
