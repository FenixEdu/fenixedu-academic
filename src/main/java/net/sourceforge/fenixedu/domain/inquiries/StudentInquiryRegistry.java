/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain.inquiries;

import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.CurricularCourseInquiriesRegistryDTO;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Grade;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.fenixedu.bennu.core.domain.Bennu;

public class StudentInquiryRegistry extends StudentInquiryRegistry_Base {

    public StudentInquiryRegistry() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public StudentInquiryRegistry(ExecutionCourse executionCourse, ExecutionSemester executionPeriod,
            CurricularCourse curricularCourse, Registration registration) {
        this();
        setExecutionCourse(executionCourse);
        setExecutionPeriod(executionPeriod);
        setCurricularCourse(curricularCourse);
        setRegistration(registration);
        setStudent(registration.getStudent());

        setState(executionCourse.getAvailableForInquiries() ? InquiriesRegistryState.ANSWER_LATER : InquiriesRegistryState.UNAVAILABLE);
        if (curricularCourse.isAnual()) {
            ExecutionSemester previousExecutionPeriod = executionPeriod.getPreviousExecutionPeriod();
            if (previousExecutionPeriod.getExecutionYear() != executionPeriod.getExecutionYear()) {
                setState(InquiriesRegistryState.UNAVAILABLE);
            }
        }
    }

    public boolean isAnswered() {
        return getState() == InquiriesRegistryState.ANSWERED;
    }

    public boolean isNotAnswered() {
        return getState() == InquiriesRegistryState.NOT_ANSWERED;
    }

    public boolean isToAnswerLater() {
        return isAvailableToInquiries() && getState() == InquiriesRegistryState.ANSWER_LATER;
    }

    public boolean isUnavailable() {
        return getState() == InquiriesRegistryState.UNAVAILABLE;
    }

    public boolean isToAnswerTeachers() {
        return getState() == InquiriesRegistryState.TEACHERS_TO_ANSWER;
    }

    public boolean isAvailableToInquiries() {
        return getExecutionCourse().getAvailableForInquiries() && !isUnavailable();
    }

    public boolean isOpenToAnswer() {

        if (isAnswered() || isNotAnswered()) {
            return false;
        }
        if (isCreatedAfterWeeklySpentHoursSubmission()) {
            return false;
        }
        if (getRegistration().getStudent().isWeeklySpentHoursSubmittedForOpenInquiry() && !isAvailableToInquiries()) {
            return false;
        }
        return true;
    }

    public InquiryGradesInterval getLastGradeInterval() {
        Collection<Enrolment> enrolments = getRegistration().getEnrolments(getExecutionPeriod());
        Grade grade = null;
        for (Enrolment enrolment : enrolments) {
            if (getExecutionCourse() == enrolment.getExecutionCourseFor(getExecutionPeriod())) {
                final EnrolmentEvaluation enrolmentEvaluation = enrolment.getLatestEnrolmentEvaluation();
                if (enrolmentEvaluation != null && (enrolmentEvaluation.isTemporary() || enrolmentEvaluation.isFinal())) {
                    grade = enrolmentEvaluation.getGrade();
                    break;
                }
            }
        }
        if (grade != null && grade.getGradeScale() == GradeScale.TYPE20) {
            int gradeValue = 0;
            if (grade.isApproved()) {
                gradeValue = grade.getIntegerValue();
            }
            return InquiryGradesInterval.getInterval(Double.valueOf(gradeValue) * 10);
        }
        return null;
    }

    public boolean isCreatedAfterWeeklySpentHoursSubmission() {
        return getRegistration().getStudent().isWeeklySpentHoursSubmittedForOpenInquiry()
                && (getWeeklyHoursSpentPercentage() == null || getStudyDaysSpentInExamsSeason() == null);
    }

    public StudentInquiryExecutionPeriod getStudentInquiryExecutionPeriod() {
        return getRegistration().getStudent().getStudentInquiryExecutionPeriod(getExecutionPeriod());
    }

    public ExecutionDegree getExecutionDegree() {
        final StudentCurricularPlan studentCurricularPlan = getRegistration().getActiveStudentCurricularPlan();
        if (studentCurricularPlan != null) {
            final DegreeCurricularPlan degreeCurricularPlan = studentCurricularPlan.getDegreeCurricularPlan();
            return degreeCurricularPlan.getExecutionDegreeByAcademicInterval(getExecutionPeriod().getAcademicInterval());
        }
        DegreeCurricularPlan lastDegreeCurricularPlan = getRegistration().getLastDegreeCurricularPlan();
        if (lastDegreeCurricularPlan != null) {
            return lastDegreeCurricularPlan.getExecutionDegreeByAcademicInterval(getExecutionPeriod().getAcademicInterval());
        }
        return null;
    }

    public static boolean checkTotalPercentageDistribution(List<CurricularCourseInquiriesRegistryDTO> courses) {
        Integer totalPercentage = 0;
        for (CurricularCourseInquiriesRegistryDTO curricularCourseInquiriesRegistryDTO : courses) {
            totalPercentage += curricularCourseInquiriesRegistryDTO.getWeeklyHoursSpentPercentage();
        }
        return totalPercentage == 100;
    }

    public static boolean checkTotalStudyDaysSpentInExamsSeason(List<CurricularCourseInquiriesRegistryDTO> courses) {
        int totalHours = 0;
        for (CurricularCourseInquiriesRegistryDTO curricularCourseInquiriesRegistryDTO : courses) {
            totalHours += curricularCourseInquiriesRegistryDTO.getStudyDaysSpentInExamsSeason();
        }
        return totalHours <= 60 * 24 * 8; //60 days in exam period 
    }

    public void delete() {
        setCurricularCourse(null);
        setExecutionCourse(null);
        setExecutionPeriod(null);
        setRegistration(null);
        setStudent(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    @Deprecated
    public boolean hasStudent() {
        return getStudent() != null;
    }

    @Deprecated
    public boolean hasExecutionCourse() {
        return getExecutionCourse() != null;
    }

    @Deprecated
    public boolean hasEstimatedECTS() {
        return getEstimatedECTS() != null;
    }

    @Deprecated
    public boolean hasRegistration() {
        return getRegistration() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasStudyDaysSpentInExamsSeason() {
        return getStudyDaysSpentInExamsSeason() != null;
    }

    @Deprecated
    public boolean hasState() {
        return getState() != null;
    }

    @Deprecated
    public boolean hasAttendenceClassesPercentage() {
        return getAttendenceClassesPercentage() != null;
    }

    @Deprecated
    public boolean hasExecutionPeriod() {
        return getExecutionPeriod() != null;
    }

    @Deprecated
    public boolean hasWeeklyHoursSpentPercentage() {
        return getWeeklyHoursSpentPercentage() != null;
    }

    @Deprecated
    public boolean hasCurricularCourse() {
        return getCurricularCourse() != null;
    }

}
