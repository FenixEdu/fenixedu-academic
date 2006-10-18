package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Set;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.student.Registration;

public class DepartmentStudentsByExecutionYearGroup extends DepartmentByExecutionYearGroup {

    /**
         * 
         */
    private static final long serialVersionUID = 8466471514890333054L;

    public DepartmentStudentsByExecutionYearGroup(ExecutionYear executionYear, Department department) {
	super(executionYear, department);

    }

    @Override
    public Set<Person> getElements() {
	Set<Person> elements = super.buildSet();

	for (CompetenceCourse competenceCourse : getDepartment().getCompetenceCourses()) {
	    for (CurricularCourse curricularCourse : competenceCourse.getAssociatedCurricularCourses()) {
		for (Enrolment enrolment : curricularCourse
			.getEnrolmentsByExecutionYear(getExecutionYear())) {
		    elements.add(enrolment.getStudentCurricularPlan().getStudent().getPerson());
		}
	    }
	}

	return super.freezeSet(elements);

    }

    @Override
    public boolean isMember(Person person) {
	if (person != null && person.hasStudent()) {
	    for (final Registration registration : person.getStudent().getRegistrationsSet()) {
		for (final Enrolment enrolment : registration.getLastStudentCurricularPlan()
			.getEnrolmentsByExecutionYear(getExecutionYear())) {
		    if (enrolment.getCurricularCourse().hasCompetenceCourse()
			    && enrolment.getCurricularCourse().getCompetenceCourse().getDepartmentsSet()
				    .contains(getDepartment())) {
			return true;
		    }
		}

	    }
	}

	return false;
    }
}
