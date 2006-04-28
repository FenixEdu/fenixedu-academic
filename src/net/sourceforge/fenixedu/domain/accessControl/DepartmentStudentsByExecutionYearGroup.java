package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Set;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;

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
}
