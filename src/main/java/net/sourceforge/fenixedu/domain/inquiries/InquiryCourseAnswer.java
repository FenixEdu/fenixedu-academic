package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;

import org.joda.time.DateTime;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
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
        courseAnswer.setStudentType(inquiryRegistry.getRegistration().getRegistrationAgreement());
        courseAnswer.setEntryGrade(InquiryGradesInterval.getInterval(inquiryRegistry.getRegistration().getEntryGrade()));
        courseAnswer.setGrade(inquiryRegistry.getLastGradeInterval());

        inquiryRegistry.setState(InquiriesRegistryState.NOT_ANSWERED);

        return courseAnswer;
    }

    public static int getNumberOfEnrolments(final StudentInquiryRegistry inquiryRegistry) {
        final StudentCurricularPlan studentCurricularPlan =
                inquiryRegistry.getRegistration().getStudentCurricularPlan(
                        inquiryRegistry.getExecutionPeriod().getExecutionYear());
        final int numberOfEnrolments = studentCurricularPlan.getEnrolments(inquiryRegistry.getCurricularCourse()).size();
        return numberOfEnrolments;
    }
    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Shift> getEnrolledShifts() {
        return getEnrolledShiftsSet();
    }

    @Deprecated
    public boolean hasAnyEnrolledShifts() {
        return !getEnrolledShiftsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.inquiries.InquiryStudentTeacherAnswer> getAssociatedInquiryStudentTeacherAnswers() {
        return getAssociatedInquiryStudentTeacherAnswersSet();
    }

    @Deprecated
    public boolean hasAnyAssociatedInquiryStudentTeacherAnswers() {
        return !getAssociatedInquiryStudentTeacherAnswersSet().isEmpty();
    }

    @Deprecated
    public boolean hasCommittedFraud() {
        return getCommittedFraud() != null;
    }

    @Deprecated
    public boolean hasExecutionCourse() {
        return getExecutionCourse() != null;
    }

    @Deprecated
    public boolean hasStudyDaysSpentInExamsSeason() {
        return getStudyDaysSpentInExamsSeason() != null;
    }

    @Deprecated
    public boolean hasNotAnsweredJustification() {
        return getNotAnsweredJustification() != null;
    }

    @Deprecated
    public boolean hasAnswerDuration() {
        return getAnswerDuration() != null;
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
    public boolean hasStudentType() {
        return getStudentType() != null;
    }

    @Deprecated
    public boolean hasEntryGrade() {
        return getEntryGrade() != null;
    }

    @Deprecated
    public boolean hasWeeklyHoursSpentInAutonomousWork() {
        return getWeeklyHoursSpentInAutonomousWork() != null;
    }

    @Deprecated
    public boolean hasGrade() {
        return getGrade() != null;
    }

    @Deprecated
    public boolean hasNotAnsweredOtherJustification() {
        return getNotAnsweredOtherJustification() != null;
    }

    @Deprecated
    public boolean hasExecutionDegreeStudent() {
        return getExecutionDegreeStudent() != null;
    }

    @Deprecated
    public boolean hasAttendenceClassesPercentage() {
        return getAttendenceClassesPercentage() != null;
    }

    @Deprecated
    public boolean hasExecutionDegreeCourse() {
        return getExecutionDegreeCourse() != null;
    }

    @Deprecated
    public boolean hasNumberOfEnrolments() {
        return getNumberOfEnrolments() != null;
    }

}
