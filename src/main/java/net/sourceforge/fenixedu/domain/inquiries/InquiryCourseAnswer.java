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

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.student.RegistrationAgreement;
import net.sourceforge.fenixedu.domain.student.RegistrationProtocol;
import net.sourceforge.fenixedu.predicates.RolePredicates;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class InquiryCourseAnswer extends InquiryCourseAnswer_Base {

    public InquiryCourseAnswer() {
        super();
    }

    public InquiryCourseAnswer(StudentInquiryRegistry inquiryRegistry) {
        this();
        setExecutionPeriod(inquiryRegistry.getExecutionPeriod());
        setExecutionCourse(inquiryRegistry.getExecutionCourse());
        setExecutionDegreeStudent(inquiryRegistry.getExecutionDegree());

        setWeeklyHoursSpentPercentage(inquiryRegistry.getWeeklyHoursSpentPercentage());
        setStudyDaysSpentInExamsSeason(inquiryRegistry.getStudyDaysSpentInExamsSeason());
        setAttendenceClassesPercentage(inquiryRegistry.getAttendenceClassesPercentage());

        setExecutionDegreeCourse(ExecutionDegree.getByDegreeCurricularPlanAndExecutionYear(inquiryRegistry.getCurricularCourse()
                .getDegreeCurricularPlan(), inquiryRegistry.getExecutionPeriod().getExecutionYear()));
    }

    @Atomic
    public static InquiryCourseAnswer createNotAnsweredInquiryCourse(final StudentInquiryRegistry inquiryRegistry,
            final InquiryNotAnsweredJustification justification, final String otherJustification) {
        check(RolePredicates.STUDENT_PREDICATE);
        final InquiryCourseAnswer courseAnswer = new InquiryCourseAnswer(inquiryRegistry);
        final StudentInquiryExecutionPeriod studentInquiryExecutionPeriod =
                inquiryRegistry.getRegistration().getStudent()
                        .getStudentInquiryExecutionPeriod(inquiryRegistry.getExecutionPeriod());
        courseAnswer.setWeeklyHoursSpentInAutonomousWork(studentInquiryExecutionPeriod.getWeeklyHoursSpentInClassesSeason());
        courseAnswer.setNotAnsweredJustification(justification);
        courseAnswer.setNotAnsweredOtherJustification(otherJustification);
        courseAnswer.setResponseDateTime(new DateTime());

        courseAnswer.setNumberOfEnrolments(getNumberOfEnrolments(inquiryRegistry));
        courseAnswer.setCommittedFraud(Boolean.FALSE);//TODO actualmente não existe registo desta info no fenix
        courseAnswer.setRegistrationProtocol(inquiryRegistry.getRegistration().getRegistrationProtocol());
        courseAnswer.setEntryGrade(InquiryGradesInterval.getInterval(inquiryRegistry.getRegistration().getEntryGrade()));
        courseAnswer.setGrade(inquiryRegistry.getLastGradeInterval());

        inquiryRegistry.setState(InquiriesRegistryState.NOT_ANSWERED);

        return courseAnswer;
    }

    @Deprecated
    @Override
    public RegistrationAgreement getStudentType() {
        return super.getStudentType();
    }

    @Deprecated
    @Override
    public void setStudentType(RegistrationAgreement studentType) {
        super.setStudentType(studentType);
        super.setRegistrationProtocol(studentType == null ? null : RegistrationProtocol.serveRegistrationProtocol(studentType));
    }

    @Override
    public void setRegistrationProtocol(final RegistrationProtocol registrationProtocol) {
        super.setRegistrationProtocol(registrationProtocol);
        super.setStudentType(registrationProtocol == null ? null : registrationProtocol.getRegistrationAgreement());
    }

    public static int getNumberOfEnrolments(final StudentInquiryRegistry inquiryRegistry) {
        final StudentCurricularPlan studentCurricularPlan =
                inquiryRegistry.getRegistration().getStudentCurricularPlan(
                        inquiryRegistry.getExecutionPeriod().getExecutionYear());
        final int numberOfEnrolments = studentCurricularPlan.getEnrolments(inquiryRegistry.getCurricularCourse()).size();
        return numberOfEnrolments;
    }

    @Deprecated
    @Atomic
    public static void connectToRegistrationProtocols() {
        for (final ExecutionYear year : Bennu.getInstance().getExecutionYearsSet()) {
            for (final ExecutionSemester semester : year.getExecutionPeriodsSet()) {
                final Thread t = new Thread() {
                    @Override
                    @Atomic
                    public void run() {
                        for (final ExecutionCourse course : semester.getAssociatedExecutionCoursesSet()) {
                            for (final InquiryCourseAnswer inquiryCourseAnswer : course.getInquiryCourseAnswersSet()) {
                                final RegistrationAgreement studentType = inquiryCourseAnswer.getStudentType();
                                if (studentType != null && inquiryCourseAnswer.getRegistrationProtocol() == null) {
                                    inquiryCourseAnswer.setRegistrationProtocol(RegistrationProtocol
                                            .serveRegistrationProtocol(studentType));
                                }
                            }
                        }
                    }
                };
                t.start();
                try {
                    t.join();
                } catch (final InterruptedException e) {
                    throw new Error(e);
                }
            }
        }
    }

}
