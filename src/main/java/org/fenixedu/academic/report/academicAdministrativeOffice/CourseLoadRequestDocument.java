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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.organizationalStructure.UniversityUnit;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.CourseLoadRequest;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;

public class CourseLoadRequestDocument extends AdministrativeOfficeDocument {

    private static final long serialVersionUID = 10L;

    protected CourseLoadRequestDocument(final CourseLoadRequest documentRequest) {
        super(documentRequest);
    }

    @Override
    public CourseLoadRequest getDocumentRequest() {
        return (CourseLoadRequest) super.getDocumentRequest();
    }

    @Override
    protected void fillReport() {

        addParameter("certification", BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.certification").toUpperCase());
        addParameter("certificationMessage",
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.program.certificate.certification"));
        setPersonFields();
        fillInstitutionAndStaffFields();
        setFooter(getDocumentRequest());
        addParametersInformation();
    }

    private void addParametersInformation() {

        AdministrativeOffice administrativeOffice = getAdministrativeOffice();
        Person coordinator = administrativeOffice.getCoordinator().getPerson();
        Person student = getDocumentRequest().getPerson();
        final UniversityUnit university = UniversityUnit.getInstitutionsUniversityUnit();

        String coordinatorGender = getCoordinatorGender(coordinator);

        String labelStudent;
        if (student.isMale()) {
            labelStudent = BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.of.student.male");
        } else {
            labelStudent = BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.of.student.female");
        }

        String coordinatorName = coordinator.getName();
        String adminOfficeUnitName = getI18NText(administrativeOffice.getName()).toUpperCase();
        String universityName = getMLSTextContent(university.getPartyName()).toUpperCase();

        String institutionName = getInstitutionName().toUpperCase();

        String template = BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.courseLoad.personalData.first");
        String firstPart =
                MessageFormat.format(template, coordinatorName, coordinatorGender, adminOfficeUnitName, institutionName,
                        universityName, labelStudent);
        addParameter("firstPart", firstPart);
        addParameter("secondPart", student.getName());
        addParameter("thirdPart", BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.with.number"));
        addParameter("fourthPart", getStudentNumber());
        addParameter("fifthPart", BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.of.male"));
        addParameter("sixthPart", getDegreeDescription());
        addParameter("seventhPart", BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.courseLoad.endMessage"));

        addLabelsToMultiLanguage();
        createCourseLoadsList();
    }

    private void addLabelsToMultiLanguage() {
        addParameter("enrolment", BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.serviceRequests.enrolment"));
        addParameter("year", BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.year"));
        addParameter("autonomousWork", BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.autonomousWork"));
        addParameter("courseLoad", BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.courseLoad"));
        addParameter("total", BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.total.amount"));
        addParameter("tTotal", BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.total"));
        addParameter("lectures", BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.lectures"));
        addParameter("practices", BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.patrice"));
        addParameter("lecturesPractice", BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.lecturesPractice"));
        addParameter("laboratory", BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.laboratory"));
        addParameter("dissertations", BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.courseLoad.dissertations"));
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

    private void createCourseLoadsList() {
        final List<CourseLoadEntry> bolonha = new ArrayList<CourseLoadEntry>();
        final List<CourseLoadEntry> preBolonha = new ArrayList<CourseLoadEntry>();
        final List<CourseLoadEntry> dissertations = new ArrayList<CourseLoadEntry>();

        addParameter("bolonhaList", bolonha);
        addParameter("preBolonhaList", preBolonha);
        addParameter("dissertationsList", dissertations);

        for (final Enrolment enrolment : getDocumentRequest().getEnrolmentsSet()) {

            if (enrolment.isBolonhaDegree()) {

                if (enrolment.isDissertation()) {
                    dissertations.add(new BolonhaCourseLoadEntry(enrolment));
                } else {
                    bolonha.add(new BolonhaCourseLoadEntry(enrolment));
                }

            } else {
                preBolonha.add(new PreBolonhaCourseLoadEntry(enrolment));
            }

        }

        Collections.sort(bolonha);
        Collections.sort(preBolonha);
        Collections.sort(dissertations);
    }

    static final protected String DD = "dd";
    static final protected String MMMM_YYYY = "MMMM yyyy";

    @Override
    protected boolean showPriceFields() {
        return false;
    }

    @Override
    protected void setPersonFields() {
        addParameter("name", getDocumentRequest().getPerson().getName());
    }

    abstract public class CourseLoadEntry implements Comparable<CourseLoadEntry> {
        private String curricularCourseName;
        private String year;
        private Double total;

        protected CourseLoadEntry(final String name, final String year) {
            this.curricularCourseName = name;
            this.year = year;
        }

        public String getCurricularCourseName() {
            return curricularCourseName;
        }

        public void setCurricularCourseName(String curricularCourseName) {
            this.curricularCourseName = curricularCourseName;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        @Override
        public int compareTo(CourseLoadEntry other) {
            return getCurricularCourseName().compareTo(other.getCurricularCourseName());
        }

        public Double getTotal() {
            return total;
        }

        public void setTotal(Double total) {
            this.total = total;
        }

        public Boolean getCourseLoadCorrect() {
            return Boolean.valueOf(total.doubleValue() != 0d);
        }

        CourseLoadEntry create(final Enrolment enrolment) {
            if (enrolment.isBolonhaDegree()) {
                return new BolonhaCourseLoadEntry(enrolment);
            } else {
                return new PreBolonhaCourseLoadEntry(enrolment);
            }
        }
    }

    public class BolonhaCourseLoadEntry extends CourseLoadEntry {
        private Double contactLoad;
        private Double autonomousWork;

        public BolonhaCourseLoadEntry(final Enrolment enrolment) {
            super(enrolment.getCurricularCourse().getName(), enrolment.getExecutionYear().getYear());

            final CurricularCourse curricularCourse = enrolment.getCurricularCourse();
            setCurricularCourseName(curricularCourse.getNameI18N(enrolment.getExecutionYear()).getContent(getLanguage()));
            setContactLoad(curricularCourse.getContactLoad(enrolment.getExecutionPeriod()));
            setAutonomousWork(curricularCourse.getAutonomousWorkHours(enrolment.getExecutionPeriod()));
            setTotal(curricularCourse.getTotalLoad(enrolment.getExecutionPeriod()));
        }

        public Double getAutonomousWork() {
            return autonomousWork;
        }

        public void setAutonomousWork(Double autonomousWork) {
            this.autonomousWork = autonomousWork;
        }

        public Double getContactLoad() {
            return contactLoad;
        }

        public void setContactLoad(Double contactLoad) {
            this.contactLoad = contactLoad;
        }
    }

    public class PreBolonhaCourseLoadEntry extends CourseLoadEntry {
        private Double theoreticalHours;
        private Double praticalHours;
        private Double labHours;
        private Double theoPratHours;

        public PreBolonhaCourseLoadEntry(final Enrolment enrolment) {
            super(enrolment.getCurricularCourse().getName(), enrolment.getExecutionYear().getYear());
            initInformation(enrolment.getCurricularCourse(), enrolment.getExecutionYear());
        }

        private void initInformation(final CurricularCourse curricularCourse, ExecutionYear executionYear) {
            setCurricularCourseName(curricularCourse.getNameI18N(executionYear).getContent(getLanguage()));
            setTheoreticalHours(curricularCourse.getTheoreticalHours());
            setPraticalHours(curricularCourse.getPraticalHours());
            setLabHours(curricularCourse.getLabHours());
            setTheoPratHours(curricularCourse.getTheoPratHours());
            setTotal(calculateTotal(curricularCourse));
        }

        public Double getLabHours() {
            return labHours;
        }

        public void setLabHours(Double labHours) {
            this.labHours = labHours;
        }

        public Double getPraticalHours() {
            return praticalHours;
        }

        public void setPraticalHours(Double praticalHours) {
            this.praticalHours = praticalHours;
        }

        public Double getTheoPratHours() {
            return theoPratHours;
        }

        public void setTheoPratHours(Double theoPratHours) {
            this.theoPratHours = theoPratHours;
        }

        public Double getTheoreticalHours() {
            return theoreticalHours;
        }

        public void setTheoreticalHours(Double theoreticalHours) {
            this.theoreticalHours = theoreticalHours;
        }

        private Double calculateTotal(final CurricularCourse curricularCourse) {
            double result = 0d;
            result += curricularCourse.getTheoreticalHours();
            result += curricularCourse.getPraticalHours();
            result += curricularCourse.getLabHours();
            result += curricularCourse.getTheoPratHours();
            return Double.valueOf(result);
        }
    }

}
