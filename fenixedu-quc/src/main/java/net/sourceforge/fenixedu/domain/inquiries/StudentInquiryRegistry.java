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
package org.fenixedu.academic.domain.inquiries;

import static org.fenixedu.academic.predicate.AccessControl.check;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.fenixedu.academic.dto.inquiries.CurricularCourseInquiriesRegistryDTO;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.EnrolmentEvaluation;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Grade;
import org.fenixedu.academic.domain.GradeScale;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.predicate.RolePredicates;

import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.Atomic;

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

        setState(InquiriesRoot.getAvailableForInquiries(executionCourse) ? InquiriesRegistryState.ANSWER_LATER : InquiriesRegistryState.UNAVAILABLE);
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
        return InquiriesRoot.getAvailableForInquiries(getExecutionCourse()) && !isUnavailable();
    }

    public boolean isOpenToAnswer() {

        if (isAnswered() || isNotAnswered()) {
            return false;
        }
        if (isCreatedAfterWeeklySpentHoursSubmission()) {
            return false;
        }
        if (StudentInquiryExecutionPeriod.isWeeklySpentHoursSubmittedForOpenInquiry(getRegistration().getStudent())
                && !isAvailableToInquiries()) {
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
        return StudentInquiryExecutionPeriod.isWeeklySpentHoursSubmittedForOpenInquiry(getRegistration().getStudent())
                && (getWeeklyHoursSpentPercentage() == null || getStudyDaysSpentInExamsSeason() == null);
    }

    public StudentInquiryExecutionPeriod getStudentInquiryExecutionPeriod() {
        return StudentInquiryExecutionPeriod.getStudentInquiryExecutionPeriod(getRegistration().getStudent(),
                getExecutionPeriod());
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

    public static boolean hasInquiriesToRespond(Student student) {
        StudentInquiryTemplate currentTemplate = StudentInquiryTemplate.getCurrentTemplate();
        if (currentTemplate == null) {
            return false;
        }

        final ExecutionSemester executionSemester = currentTemplate.getExecutionPeriod();

        final Set<CurricularCourse> inquiryCurricularCourses = new HashSet<CurricularCourse>();
        // first collect all studentInquiryRegistries from all registrations
        for (Registration registration : student.getRegistrationsSet()) {
            if (!InquiriesRoot.isAvailableDegreeTypeForInquiries(registration)) {
                continue;
            }
            for (final StudentInquiryRegistry inquiriesRegistry : registration.getStudentsInquiryRegistriesSet()) {
                if (inquiriesRegistry.getExecutionPeriod() == executionSemester) {
                    if (inquiriesRegistry.isOpenToAnswer()) {
                        return true;
                    } else {
                        inquiryCurricularCourses.add(inquiriesRegistry.getCurricularCourse());
                    }
                }
            }
        }

        for (Registration registration : student.getRegistrationsSet()) {
            if (!InquiriesRoot.isAvailableDegreeTypeForInquiries(registration)) {
                continue;
            }
            for (Enrolment enrolment : registration.getEnrolments(executionSemester)) {
                final ExecutionCourse executionCourse = enrolment.getExecutionCourseFor(executionSemester);
                if (executionCourse != null && !inquiryCurricularCourses.contains(enrolment.getCurricularCourse())) {
                    return true;
                }
            }

            for (final Enrolment enrolment : getPreviousAnnualEnrolmentsForInquiries(executionSemester, registration)) {
                ExecutionCourse executionCourse = getQUCExecutionCourseForAnnualCC(executionSemester, enrolment);
                if (executionCourse != null && !inquiryCurricularCourses.contains(enrolment.getCurricularCourse())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Atomic
    public static Collection<StudentInquiryRegistry> retrieveAndCreateMissingInquiryRegistriesForPeriod(Student student,
            ExecutionSemester executionSemester) {
        check(student, RolePredicates.STUDENT_PREDICATE);
        final Map<ExecutionCourse, StudentInquiryRegistry> coursesToAnswer =
                getExistingStudentInquiryRegistryMap(student, executionSemester);

        for (Registration registration : student.getRegistrationsSet()) {
            if (!InquiriesRoot.isAvailableDegreeTypeForInquiries(registration)) {
                continue;
            }
            for (final Enrolment enrolment : registration.getEnrolments(executionSemester)) {
                createMissingInquiryRegistry(student, executionSemester, coursesToAnswer, registration, enrolment, false);
            }

            for (final Enrolment enrolment : getPreviousAnnualEnrolmentsForInquiries(executionSemester, registration)) {
                createMissingInquiryRegistry(student, executionSemester, coursesToAnswer, registration, enrolment, true);
            }
        }
        return coursesToAnswer.values();
    }

    private static Map<ExecutionCourse, StudentInquiryRegistry> getExistingStudentInquiryRegistryMap(Student student,
            ExecutionSemester executionSemester) {
        final Map<ExecutionCourse, StudentInquiryRegistry> coursesToAnswer =
                new HashMap<ExecutionCourse, StudentInquiryRegistry>();
        for (Registration registration : student.getRegistrationsSet()) {
            if (!InquiriesRoot.isAvailableDegreeTypeForInquiries(registration)) {
                continue;
            }
            for (final StudentInquiryRegistry studentInquiryRegistry : registration.getStudentsInquiryRegistriesSet()) {
                if (studentInquiryRegistry.getExecutionPeriod() == executionSemester) {
                    coursesToAnswer.put(studentInquiryRegistry.getExecutionCourse(), studentInquiryRegistry);
                }
            }
        }
        return coursesToAnswer;
    }

    private static void createMissingInquiryRegistry(Student student, final ExecutionSemester executionSemester,
            final Map<ExecutionCourse, StudentInquiryRegistry> coursesToAnswer, Registration registration,
            final Enrolment enrolment, boolean isAnnual) {
        ExecutionCourse executionCourse = enrolment.getExecutionCourseFor(executionSemester);
        if (isAnnual) {
            executionCourse = getQUCExecutionCourseForAnnualCC(executionSemester, enrolment);
        }
        if (executionCourse != null && !coursesToAnswer.containsKey(executionCourse)) {
            coursesToAnswer
                    .put(executionCourse,
                            new StudentInquiryRegistry(executionCourse, executionSemester, enrolment.getCurricularCourse(),
                                    registration));
        }
    }

    private static List<Enrolment> getPreviousAnnualEnrolmentsForInquiries(ExecutionSemester executionSemester,
            Registration registration) {
        List<Enrolment> result = new ArrayList<Enrolment>();
        if (executionSemester.getPreviousExecutionPeriod().getExecutionYear() == executionSemester.getExecutionYear()) {
            for (final Enrolment enrolment : registration.getEnrolments(executionSemester.getPreviousExecutionPeriod())) {
                if (enrolment.getCurricularCourse().isAnual()) {
                    result.add(enrolment);
                }
            }
        }
        return result;
    }

    private static ExecutionCourse getQUCExecutionCourseForAnnualCC(final ExecutionSemester executionSemester,
            final Enrolment enrolment) {
        ExecutionCourse executionCourse = enrolment.getExecutionCourseFor(executionSemester);
        if (executionCourse == null) { // some annual courses only have one
            // execution in the 1st semester
            executionCourse = enrolment.getExecutionCourseFor(executionSemester.getPreviousExecutionPeriod());
        }
        return executionCourse;
    }

    public static void setSpentTimeInPeriodForInquiry(Student student, List<CurricularCourseInquiriesRegistryDTO> courses,
            Integer weeklySpentHours, ExecutionSemester executionSemester) {
        check(student, RolePredicates.STUDENT_PREDICATE);

        if (!checkTotalPercentageDistribution(courses)) {
            throw new DomainException("error.weeklyHoursSpentPercentage.is.not.100.percent");
        }
        if (!checkTotalStudyDaysSpentInExamsSeason(courses)) {
            throw new DomainException("error.studyDaysSpentInExamsSeason.exceedsMaxDaysLimit");
        }

        StudentInquiryExecutionPeriod studentInquiryExecutionPeriod =
                StudentInquiryExecutionPeriod.getStudentInquiryExecutionPeriod(student, executionSemester);
        if (studentInquiryExecutionPeriod != null && studentInquiryExecutionPeriod.getWeeklyHoursSpentInClassesSeason() != 0) {
            return;
        }

        if (studentInquiryExecutionPeriod == null) {
            studentInquiryExecutionPeriod = new StudentInquiryExecutionPeriod(student, executionSemester);
        }
        studentInquiryExecutionPeriod.setWeeklyHoursSpentInClassesSeason(weeklySpentHours);

        for (CurricularCourseInquiriesRegistryDTO curricularCourseInquiriesRegistryDTO : courses) {
            StudentInquiryRegistry inquiryRegistry = curricularCourseInquiriesRegistryDTO.getInquiryRegistry();
            inquiryRegistry.setStudyDaysSpentInExamsSeason(curricularCourseInquiriesRegistryDTO.getStudyDaysSpentInExamsSeason());
            inquiryRegistry.setWeeklyHoursSpentPercentage(curricularCourseInquiriesRegistryDTO.getWeeklyHoursSpentPercentage());
            inquiryRegistry.setAttendenceClassesPercentage(curricularCourseInquiriesRegistryDTO.getAttendenceClassesPercentage());
            inquiryRegistry.setEstimatedECTS(curricularCourseInquiriesRegistryDTO.getCalculatedECTSCredits());
        }
    }

}
