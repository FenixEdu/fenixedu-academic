package relations;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;


public class CurricularCourseExecutionCourse extends CurricularCourseExecutionCourse_Base {
	public static void add(net.sourceforge.fenixedu.domain.ICurricularCourse associatedCurricularCourses, net.sourceforge.fenixedu.domain.IExecutionCourse associatedExecutionCourses) {
		CurricularCourseExecutionCourse_Base.add(associatedCurricularCourses, associatedExecutionCourses);
		for(final IEnrolment enrolment: associatedCurricularCourses.getEnrolments()) {
			if(enrolment.getExecutionPeriod().equals(associatedExecutionCourses.getExecutionPeriod())) {
				associateAttend(enrolment, associatedExecutionCourses);
			}
		}
	}

	private static void associateAttend(IEnrolment enrolment, IExecutionCourse executionCourse) {
		if(!alreadyHasAttend(enrolment, executionCourse.getExecutionPeriod())) {
			IAttends attends = executionCourse.getAttendsByStudent(enrolment.getStudentCurricularPlan().getStudent());
			if(attends == null) {
				attends = new Attends(enrolment.getStudentCurricularPlan().getStudent(), executionCourse);
			}
			enrolment.addAttends(attends);
		}
	}

	private static boolean alreadyHasAttend(IEnrolment enrolment, IExecutionPeriod executionPeriod) {
		for (IAttends attends : enrolment.getAttends()) {
			if(attends.getDisciplinaExecucao().getExecutionPeriod().equals(executionPeriod)) {
				return true;
			}
		}
		return false;
	}
	
    public static void remove(net.sourceforge.fenixedu.domain.ICurricularCourse associatedCurricularCourses, net.sourceforge.fenixedu.domain.IExecutionCourse associatedExecutionCourses) {
    	CurricularCourseExecutionCourse_Base.remove(associatedCurricularCourses, associatedExecutionCourses);
    	for(IAttends attends: associatedExecutionCourses.getAttends()) {
    		if(attends.getEnrolment().getCurricularCourse().equals(associatedCurricularCourses)) {
    			attends.setEnrolment(null);
    		}
    	}
    }
}