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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.Exam;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.ExamDateCertificateRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.IDocumentRequest;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.util.Season;

public class ExamDateCertificate extends AdministrativeOfficeDocument {

    public static class ExamDateEntry {

        private String curricularCourseName;

        private String firstSeasonDate;

        private String firstSeasonHour;

        private String secondSeasonDate;

        private String secondSeasonHour;

        private String specialSeasonDate;

        private String specialSeasonHour;

        public ExamDateEntry() {

        }

        public String getCurricularCourseName() {
            return curricularCourseName;
        }

        public void setCurricularCourseName(String curricularCourseName) {
            this.curricularCourseName = curricularCourseName;
        }

        public String getFirstSeasonDate() {
            return firstSeasonDate;
        }

        public void setFirstSeasonDate(String firstSeasonDate) {
            this.firstSeasonDate = firstSeasonDate;
        }

        public String getFirstSeasonHour() {
            return firstSeasonHour;
        }

        public void setFirstSeasonHour(String firstSeasonHour) {
            this.firstSeasonHour = firstSeasonHour;
        }

        public String getSecondSeasonDate() {
            return secondSeasonDate;
        }

        public void setSecondSeasonDate(String secondSeasonDate) {
            this.secondSeasonDate = secondSeasonDate;
        }

        public String getSecondSeasonHour() {
            return secondSeasonHour;
        }

        public void setSecondSeasonHour(String secondSeasonHour) {
            this.secondSeasonHour = secondSeasonHour;
        }

        public String getSpecialSeasonDate() {
            return specialSeasonDate;
        }

        public void setSpecialSeasonDate(String specialSeasonDate) {
            this.specialSeasonDate = specialSeasonDate;
        }

        public String getSpecialSeasonHour() {
            return specialSeasonHour;
        }

        public void setSpecialSeasonHour(String specialSeasonHour) {
            this.specialSeasonHour = specialSeasonHour;
        }

        public Boolean getAnyExamAvailable() {
            return (!StringUtils.isEmpty(this.specialSeasonDate) && !StringUtils.isEmpty(this.specialSeasonHour))
                    || (!StringUtils.isEmpty(this.secondSeasonDate) && !StringUtils.isEmpty(this.secondSeasonHour))
                    || (!StringUtils.isEmpty(this.firstSeasonDate) && !StringUtils.isEmpty(this.firstSeasonHour));
        }
    }

    private static final long serialVersionUID = 1L;

    protected ExamDateCertificate(final IDocumentRequest documentRequest) {
        super(documentRequest);
    }

    @Override
    protected DocumentRequest getDocumentRequest() {
        return (DocumentRequest) super.getDocumentRequest();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void fillReport() {
        super.fillReport();
        addDataSourceElements(getExamDateEntries());
        fillInstitutionAndStaffFields();
        addParameter("name", getDocumentRequest().getRegistration().getPerson().getName());
        addParameter("studentNumber", getStudentNumber());

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

    private List<ExamDateEntry> getExamDateEntries() {
        final List<ExamDateEntry> result = new ArrayList<ExamDateEntry>();
        final ExamDateCertificateRequest request = (ExamDateCertificateRequest) getDocumentRequest();

        for (final Enrolment enrolment : request.getEnrolmentsSet()) {
            final ExamDateEntry entry = new ExamDateEntry();
            entry.setCurricularCourseName(enrolment.getCurricularCourse().getName());
            fillFirstSeasonExam(request, enrolment, entry);
            fillSecondSeasonExam(request, enrolment, entry);
            fillSpecialSeasonExam(request, enrolment, entry);

            result.add(entry);
        }

        return result;
    }

    private void fillSpecialSeasonExam(final ExamDateCertificateRequest request, final Enrolment enrolment,
            final ExamDateEntry entry) {
        final Exam specialSeasonExam = request.getExamFor(enrolment, Season.SPECIAL_SEASON_OBJ);
        if (specialSeasonExam != null) {
            entry.setSpecialSeasonDate(specialSeasonExam.getDayDateYearMonthDay().toString(DD_SLASH_MM_SLASH_YYYY, getLocale()));
            entry.setSpecialSeasonHour(specialSeasonExam.getBeginningDateHourMinuteSecond().toString("HH:mm"));
        }
    }

    private void fillSecondSeasonExam(final ExamDateCertificateRequest request, final Enrolment enrolment,
            final ExamDateEntry entry) {
        final Exam secondSeasonExam = request.getExamFor(enrolment, Season.SEASON2_OBJ);
        if (secondSeasonExam != null) {
            entry.setSecondSeasonDate(secondSeasonExam.getDayDateYearMonthDay().toString(DD_SLASH_MM_SLASH_YYYY, getLocale()));
            entry.setSecondSeasonHour(secondSeasonExam.getBeginningDateHourMinuteSecond().toString("HH:mm"));
        }
    }

    private void fillFirstSeasonExam(final ExamDateCertificateRequest request, final Enrolment enrolment,
            final ExamDateEntry entry) {
        final Exam firstSeasonExam = request.getExamFor(enrolment, Season.SEASON1_OBJ);
        if (firstSeasonExam != null) {
            entry.setFirstSeasonDate(firstSeasonExam.getDayDateYearMonthDay().toString(DD_SLASH_MM_SLASH_YYYY, getLocale()));
            entry.setFirstSeasonHour(firstSeasonExam.getBeginningDateHourMinuteSecond().toString("HH:mm"));
        }
    }

    @Override
    protected String getDegreeDescription() {
        final Registration registration = getDocumentRequest().getRegistration();
        final DegreeType degreeType = registration.getDegreeType();
        final CycleType cycleType =
                degreeType.hasExactlyOneCycleType() ? degreeType.getCycleType() : registration.getCycleType(getExecutionYear());
        return registration.getDegreeDescription(getExecutionYear(), cycleType, getLocale());
    }

}
