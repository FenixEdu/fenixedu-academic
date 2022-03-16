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

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Curriculum;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.degreeStructure.BibliographicReferences;
import org.fenixedu.academic.domain.degreeStructure.Context;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.organizationalStructure.UniversityUnit;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.ProgramCertificateRequest;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.HtmlToTextConverterUtil;
import org.fenixedu.academic.util.NumberToWordsConverter;
import org.fenixedu.bennu.core.i18n.BundleUtil;

public class ProgramCertificateRequestDocument extends AdministrativeOfficeDocument {

    private static final long serialVersionUID = 12L;

    protected ProgramCertificateRequestDocument(final ProgramCertificateRequest documentRequest) {
        super(documentRequest);
    }

    @Override
    public ProgramCertificateRequest getDocumentRequest() {
        return (ProgramCertificateRequest) super.getDocumentRequest();
    }

    @Override
    protected void fillReport() {

        addParameter("certification", BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.certification").toUpperCase());
        addParameter("certificationMessage",
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.program.certificate.certification"));
        setPersonFields();
        addParametersInformation();
        fillInstitutionAndStaffFields();
        setFooter(getDocumentRequest());
        addParameter("enrolment", BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.serviceRequests.enrolment"));
    }

    private void addParametersInformation() {

        AdministrativeOffice administrativeOffice = getAdministrativeOffice();
        Person coordinator = administrativeOffice.getCoordinator().getPerson();
        Person student = getDocumentRequest().getPerson();
        final UniversityUnit university = UniversityUnit.getInstitutionsUniversityUnit();

        String coordinatorGender = getCoordinatorGender(coordinator);

        String labelStudent;
        if (student.isMale()) {
            labelStudent = BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.the.student.male");
        } else {
            labelStudent = BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.the.student.female");
        }

        String coordinatorName = coordinator.getName();
        String adminOfficeUnitName = getI18NText(administrativeOffice.getName()).toUpperCase();
        String universityName = getMLSTextContent(university.getPartyName()).toUpperCase();

        String institutionName = getInstitutionName().toUpperCase();

        String template = BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.program.certificate.personalData.first");
        String firstPart =
                MessageFormat.format(template, coordinatorName, coordinatorGender, adminOfficeUnitName, institutionName,
                        universityName, labelStudent);
        addParameter("firstPart", firstPart);
        addParameter("secondPart", student.getName());
        addParameter("thirdPart", BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.with.number"));
        addParameter("fourthPart", getStudentNumber());
        addParameter("fifthPart", BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.of.male"));
        addParameter("sixthPart", getDegreeDescription());
        addParameter("seventhPart", getProgramsDescription());

        createProgramsList(getLanguage());
    }

    @Override
    protected String getDegreeDescription() {
        final CycleType requestedCycle = getDocumentRequest().getRequestedCycle();
        if (requestedCycle == null) {
            final Registration registration = getDocumentRequest().getRegistration();
            final DegreeType degreeType = registration.getDegreeType();
            final CycleType cycleType =
                    degreeType.hasExactlyOneCycleType() ? degreeType.getCycleType() : registration
                            .getCycleType(getExecutionYear());
            return registration.getDegreeDescription(getExecutionYear(), cycleType, getLocale());
        }
        return getDocumentRequest().getRegistration().getDegreeDescription(getExecutionYear(), requestedCycle, getLocale());
    }

    public boolean isBolonha() {
        return getDocumentRequest().isBolonha();
    }

    @Override
    protected void setPersonFields() {
        addParameter("name", getDocumentRequest().getPerson().getName());
    }

    private String numberOfPrograms() {
        return NumberToWordsConverter.convert(getDocumentRequest().getEnrolmentsSet().size(), getLocale());
    }

    private String getProgramsDescription() {
        if (getDocumentRequest().getEnrolmentsSet().size() == 1) {
            return BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.program.certificate.program");
        } else {
            return MessageFormat.format(BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.program.certificate.programs"),
                    numberOfPrograms());
        }
    }

    @Override
    protected boolean showPriceFields() {
        return false;
    }

    private void createProgramsList(Locale language) {
        final List<ProgramInformation> bolonha = new ArrayList<ProgramInformation>();
        final List<ProgramInformation> preBolonha = new ArrayList<ProgramInformation>();

        addParameter("bolonhaList", bolonha);
        addParameter("preBolonhaList", preBolonha);

        addLabelsToMultiLanguage();

        for (final Enrolment enrolment : getDocumentRequest().getEnrolmentsSet()) {
            if (enrolment.isBolonhaDegree()) {
                bolonha.add(new BolonhaProgramInformation(enrolment, language));
            } else {
                preBolonha.add(new PreBolonhaProgramInformation(enrolment));
            }
        }
    }

    private String getStudentNumber() {
        final Registration registration = getDocumentRequest().getRegistration();
        if (registration.getRegistrationProtocol().isMilitaryAgreement()) {
            final String agreementInformation = registration.getAgreementInformation();
            if (!StringUtils.isEmpty(agreementInformation)) {
                return registration.getRegistrationProtocol().getCode() + SINGLE_SPACE + agreementInformation;
            }
        }
        return registration.getStudent().getNumber().toString();
    }

    private void addLabelsToMultiLanguage() {
        addParameter("enrolment", BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.serviceRequests.enrolment"));
        addParameter("degreeLabel", BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.degree"));
        addParameter("degreeCurricularPlanLabel",
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.degreeCurricularPlan"));
        addParameter("weightLabel", BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.set.evaluation.enrolment.weight"));
        addParameter("contexts", BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.contexts"));
        addParameter("prerequisites", BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.prerequisites"));
        addParameter("objectives", BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.objectives"));
        addParameter("program", BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.program"));
        addParameter("evaluationMethod", BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.evaluationMethod"));
        addParameter("bibliography", BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.bibliography"));
        addParameter("averageGrade", BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.average.grade"));
        addParameter("generalObjectives", BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.generalObjectives"));
        addParameter("operationalObjectives", BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.operationalObjectives"));
    }

    abstract public class ProgramInformation implements Serializable {
        private final String degree;
        private final String degreeCurricularPlan;
        private final String curricularCourse;
        private final List<ContextInformation> contexts;

        public ProgramInformation(final Enrolment enrolment) {
            this.degree =
                    getMLSTextContent(enrolment.getCurricularCourse().getDegree().getNameI18N(enrolment.getExecutionYear()));
            this.degreeCurricularPlan = enrolment.getCurricularCourse().getDegreeCurricularPlan().getName();
            this.curricularCourse = buildCurricularCourseName(enrolment);
            this.contexts = buildContextsInformation(enrolment.getCurricularCourse());
        }

        private List<ContextInformation> buildContextsInformation(final CurricularCourse curricularCourse) {
            final List<ContextInformation> result = new ArrayList<ContextInformation>();
            for (final Context context : curricularCourse.getParentContextsSet()) {
                result.add(new ContextInformation(context, getLanguage(), getLocale()));
            }
            return result;
        }

        private String buildCurricularCourseName(final Enrolment enrolment) {
            final CurricularCourse curricularCourse = enrolment.getCurricularCourse();
            final ExecutionSemester executionPeriod = enrolment.getExecutionPeriod();
            return getMLSTextContent(curricularCourse.getNameI18N(executionPeriod))
                    + (StringUtils.isEmpty(curricularCourse.getAcronym(executionPeriod)) ? EMPTY_STR : " (" + curricularCourse.getAcronym(executionPeriod)
                            + ")");
        }

        public String getDegree() {
            return degree;
        }

        public String getDegreeCurricularPlan() {
            return this.degreeCurricularPlan;
        }

        public String getCurricularCourse() {
            return this.curricularCourse;
        }

        public List<ContextInformation> getContexts() {
            return contexts;
        }
    }

    public class BolonhaProgramInformation extends ProgramInformation {
        private final String program;
        private final String weigth;
        private final String prerequisites;
        private final String objectives;
        private final String evaluationMethod;
        private final List<BibliographicInformation> bibliographics;

        public BolonhaProgramInformation(final Enrolment enrolment, Locale language) {
            super(enrolment);

            final ExecutionSemester executionSemester = enrolment.getExecutionPeriod();
            final CurricularCourse curricularCourse = enrolment.getCurricularCourse();

            this.program =
                    HtmlToTextConverterUtil.convertToText(getMLSTextContent(curricularCourse.getProgramI18N(executionSemester)));
            this.weigth = curricularCourse.getWeight(executionSemester).toString();
            this.prerequisites = getMLSTextContent(curricularCourse.getPrerequisitesI18N());
            this.objectives =
                    HtmlToTextConverterUtil
                            .convertToText(getMLSTextContent(curricularCourse.getObjectivesI18N(executionSemester)));
            this.evaluationMethod =
                    HtmlToTextConverterUtil.convertToText(getMLSTextContent(curricularCourse
                            .getEvaluationMethodI18N(executionSemester)));

            this.bibliographics = buildBibliographicInformation(curricularCourse, executionSemester);
        }

        private List<BibliographicInformation> buildBibliographicInformation(final CurricularCourse curricularCourse,
                final ExecutionSemester executionSemester) {
            final List<BibliographicInformation> result = new ArrayList<BibliographicInformation>();

            if (curricularCourse.getCompetenceCourse() != null) {
                for (final BibliographicReferences.BibliographicReference reference : curricularCourse.getCompetenceCourse()
                        .getAllBibliographicReferences(executionSemester)) {
                    result.add(new BibliographicInformation(reference.getAuthors(), reference.getTitle(), reference
                            .getReference(), reference.getYear()));
                }
            }

            return result;
        }

        public String getProgram() {
            return program;
        }

        public String getWeigth() {
            return this.weigth;
        }

        public String getPrerequisites() {
            return this.prerequisites;
        }

        public String getObjectives() {
            return objectives;
        }

        public String getEvaluationMethod() {
            return evaluationMethod;
        }

        public List<BibliographicInformation> getBibliographics() {
            return this.bibliographics;
        }
    }

    public class PreBolonhaProgramInformation extends ProgramInformation {
        private String program;
        private String generalObjectives;
        private String operationalObjectives;

        public PreBolonhaProgramInformation(Enrolment enrolment) {
            super(enrolment);
            final Curriculum curriculum = enrolment.getCurricularCourse().findLatestCurriculum();
            if (curriculum != null) {
                this.program = HtmlToTextConverterUtil.convertToText(getMLSTextContent(curriculum.getProgramI18N()));
                this.generalObjectives =
                        HtmlToTextConverterUtil.convertToText(getMLSTextContent(curriculum.getGeneralObjectivesI18N()));
                this.operationalObjectives =
                        HtmlToTextConverterUtil.convertToText(getMLSTextContent(curriculum.getOperacionalObjectivesI18N()));
            } else {
                this.program = this.generalObjectives = this.operationalObjectives = EMPTY_STR;
            }
        }

        public String getProgram() {
            return program;
        }

        public String getGeneralObjectives() {
            return generalObjectives;
        }

        public String getOperationalObjectives() {
            return operationalObjectives;
        }
    }

    static public class ContextInformation {
        private final String name;
        private final String period;

        public ContextInformation(final Context context, final Locale language, final Locale locale) {
            this.name = context.getParentCourseGroup().getOneFullNameI18N(language);
            this.period = context.getCurricularPeriod().getFullLabel(locale);

        }

        public String getName() {
            return name;
        }

        public String getPeriod() {
            return period;
        }
    }

    static public class BibliographicInformation {
        private final String authors;
        private final String title;
        private final String reference;
        private final String year;

        public BibliographicInformation(final String authors, final String title, final String reference, final String year) {
            this.authors = authors;
            this.title = title;
            this.reference = reference;
            this.year = year;
        }

        public String getAuthors() {
            return this.authors;
        }

        public String getTitle() {
            return this.title;
        }

        public String getReference() {
            return this.reference;
        }

        public String getYear() {
            return this.year;
        }
    }

}
