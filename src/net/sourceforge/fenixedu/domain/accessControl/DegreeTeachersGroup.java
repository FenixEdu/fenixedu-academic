package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Set;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;

public class DegreeTeachersGroup extends DegreeGroup {

    /**
         * 
         */
    private static final long serialVersionUID = 8466471514890333054L;

    public DegreeTeachersGroup(Degree degree) {
	super(degree);
    }

    @Override
    public Set<Person> getElements() {
	Set<Person> elements = super.buildSet();

	for (DegreeCurricularPlan degreeCurricularPlan : getDegree().getActiveDegreeCurricularPlans()) {
	    for (CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCourses()) {
		for (ExecutionCourse executionCourse : curricularCourse.getAssociatedExecutionCourses()) {
		    for (Professorship professorship : executionCourse.getProfessorships()) {
			elements.add(professorship.getTeacher().getPerson());
		    }
		}
	    }
	}

	return super.freezeSet(elements);
    }

    @Override
    public boolean isMember(Person person) {
	if (person != null && person.hasTeacher()) {
	    for (final Professorship professorship : person.getTeacher().getProfessorshipsSet()) {
		for (final CurricularCourse curricularCourse : professorship.getExecutionCourse()
			.getAssociatedCurricularCoursesSet()) {
		    if (curricularCourse.getDegree() == getDegree()) {
			return true;
		    }
		}
	    }
	}

	return false;

    }
}
