package relations;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;

public class CurricularCourseExecutionCourse extends CurricularCourseExecutionCourse_Base {
    public static void add(net.sourceforge.fenixedu.domain.CurricularCourse associatedCurricularCourses,
            net.sourceforge.fenixedu.domain.ExecutionCourse associatedExecutionCourses) {
        CurricularCourseExecutionCourse_Base
                .add(associatedCurricularCourses, associatedExecutionCourses);
        for (final Enrolment enrolment : associatedCurricularCourses.getEnrolments()) {
            if (enrolment.getExecutionPeriod().equals(associatedExecutionCourses.getExecutionPeriod())) {
                associateAttend(enrolment, associatedExecutionCourses);
            }
        }
    }

    private static void associateAttend(Enrolment enrolment, ExecutionCourse executionCourse) {
        if (!alreadyHasAttend(enrolment, executionCourse.getExecutionPeriod())) {
            Attends attends = executionCourse.getAttendsByStudent(enrolment.getStudentCurricularPlan()
                    .getStudent());
            if (attends == null) {
                attends = new Attends(enrolment.getStudentCurricularPlan().getStudent(), executionCourse);
            }
            enrolment.addAttends(attends);
        }
    }

    private static boolean alreadyHasAttend(Enrolment enrolment, ExecutionPeriod executionPeriod) {
        for (Attends attends : enrolment.getAttends()) {
            if (attends.getDisciplinaExecucao().getExecutionPeriod().equals(executionPeriod)) {
                return true;
            }
        }
        return false;
    }

    public static void remove(
            net.sourceforge.fenixedu.domain.CurricularCourse associatedCurricularCourses,
            net.sourceforge.fenixedu.domain.ExecutionCourse associatedExecutionCourses) {
        CurricularCourseExecutionCourse_Base.remove(associatedCurricularCourses,
                associatedExecutionCourses);
        for (Attends attends : associatedExecutionCourses.getAttends()) {
            if ((attends.getEnrolment() != null)
                    && (attends.getEnrolment().getCurricularCourse().equals(associatedCurricularCourses))) {
                attends.setEnrolment(null);
            }
        }
    }
}