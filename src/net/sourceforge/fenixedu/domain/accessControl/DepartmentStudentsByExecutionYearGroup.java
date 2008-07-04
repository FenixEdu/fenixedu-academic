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

    public DepartmentStudentsByExecutionYearGroup(String executionYear, String department) {
	super(executionYear, department);

    }

    @Override
    public Set<Person> getElements() {
	final Set<Person> elements = super.buildSet();

	for (CompetenceCourse competenceCourse : getDepartment().getCompetenceCourses()) {
	    addPersonsFromCompetenceCourse(elements, competenceCourse);
	}

	if (getDepartment().hasDepartmentUnit()) {
	    for (final CompetenceCourse competenceCourse : getDepartment().getDepartmentUnit().getCompetenceCourses()) {
		addPersonsFromCompetenceCourse(elements, competenceCourse);
	    }
	}

	return super.freezeSet(elements);

    }

    private void addPersonsFromCompetenceCourse(final Set<Person> elements, final CompetenceCourse competenceCourse) {
	for (CurricularCourse curricularCourse : competenceCourse.getAssociatedCurricularCourses()) {
	    for (Enrolment enrolment : curricularCourse.getEnrolmentsByExecutionYear(getExecutionYear())) {
		elements.add(enrolment.getStudentCurricularPlan().getRegistration().getPerson());
	    }
	}
    }

    @Override
    public boolean isMember(Person person) {
	if (person != null && person.hasStudent()) {
	    for (final Registration registration : person.getStudent().getRegistrationsSet()) {
		for (final Enrolment enrolment : registration.getLastStudentCurricularPlan().getEnrolmentsByExecutionYear(
			getExecutionYear())) {
		    if (enrolment.getCurricularCourse().hasCompetenceCourse()) {
			final CompetenceCourse competenceCourse = enrolment.getCurricularCourse().getCompetenceCourse();
			if (competenceCourse.getDepartmentsSet().contains(getDepartment())) {
			    return true;

			}

			if (competenceCourse.hasDepartmentUnit()
				&& competenceCourse.getDepartmentUnit().getDepartment() == getDepartment()) {
			    return true;
			}
		    }
		}

	    }
	}

	return false;
    }

    public static class Builder extends DepartmentByExecutionYearGroup.Builder {

	@Override
	protected DepartmentByExecutionYearGroup buildConcreteGroup(String year, String department) {
	    return new DepartmentStudentsByExecutionYearGroup(year, department);
	}

    }
}
