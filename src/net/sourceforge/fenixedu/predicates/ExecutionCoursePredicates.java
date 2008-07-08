package net.sourceforge.fenixedu.predicates;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

public class ExecutionCoursePredicates {

    public static final AccessControlPredicate<ExecutionCourse> executionCourseLecturingTeacherOrDegreeCoordinator = new AccessControlPredicate<ExecutionCourse>() {

	public boolean evaluate(ExecutionCourse executionCourse) {
	    Person person = AccessControl.getPerson();

	    Teacher teacher = person.getTeacher();

	    if (executionCourse.teacherLecturesExecutionCourse(teacher)) {
		return true;
	    }

	    if (person.hasRole(RoleType.COORDINATOR)) {
		Collection<DegreeCurricularPlan> degreeCurricularPlans = executionCourse.getAssociatedDegreeCurricularPlans();
		for (DegreeCurricularPlan degreeCurricularPlan : degreeCurricularPlans) {
		    Collection<ExecutionDegree> coordinatedExecutionDegrees = person
			    .getCoordinatedExecutionDegrees(degreeCurricularPlan);
		    for (ExecutionDegree executionDegree : coordinatedExecutionDegrees) {
			if (executionCourse.getExecutionYear().equals(executionCourse.getExecutionYear())) {
			    return true;
			}
		    }

		}
	    }
	    return false;
	}

    };

}
