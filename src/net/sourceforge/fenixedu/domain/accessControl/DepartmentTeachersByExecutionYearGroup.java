package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collection;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;

public class DepartmentTeachersByExecutionYearGroup extends DepartmentByExecutionYearGroup {

    /**
     * 
     */
    private static final long serialVersionUID = 8466471514890333054L;

    public DepartmentTeachersByExecutionYearGroup(ExecutionYear executionYear, Department department) {
        super(executionYear, department);

    }

    @Override
    public Set<Person> getElements() {
        Set<Person> elements = super.buildSet();
        Collection<Teacher> departmentTeachers = getDepartment().getAllTeachers(
                getExecutionYear().getBeginDateYearMonthDay(), getExecutionYear().getEndDateYearMonthDay());

        for (Teacher teacher : departmentTeachers) {
            elements.add(teacher.getPerson());
        }

        return super.freezeSet(elements);
    }
}
