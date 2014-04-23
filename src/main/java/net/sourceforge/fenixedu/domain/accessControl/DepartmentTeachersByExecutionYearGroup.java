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

    public DepartmentTeachersByExecutionYearGroup(String executionYear, String department) {
        super(executionYear, department);

    }

    @Override
    public Set<Person> getElements() {
        Set<Person> elements = super.buildSet();
        Collection<Teacher> departmentTeachers =
                getDepartment().getAllTeachers(getExecutionYear().getBeginDateYearMonthDay(),
                        getExecutionYear().getEndDateYearMonthDay());

        for (Teacher teacher : departmentTeachers) {
            elements.add(teacher.getPerson());
        }

        return super.freezeSet(elements);
    }

    @Override
    public boolean isMember(Person person) {
        if (person != null && person.hasTeacher()) {
            final Department lastWorkingDepartment =
                    person.getTeacher().getLastWorkingDepartment(getExecutionYear().getBeginDateYearMonthDay(),
                            getExecutionYear().getEndDateYearMonthDay());
            return (lastWorkingDepartment != null && lastWorkingDepartment.equals(getDepartment()));
        }

        return false;
    }

    public static class Builder extends DepartmentByExecutionYearGroup.Builder {

        @Override
        protected DepartmentByExecutionYearGroup buildConcreteGroup(String year, String department) {
            return new DepartmentTeachersByExecutionYearGroup(year, department);
        }

    }

    @Override
    public String getPresentationNameKey() {
        return "label.name.teachers.by.department.and.execution.year";
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        return new String[] { getDepartment().getName(), getExecutionYear().getYear() };
    }

    @Override
    public PersistentTeacherGroup convert() {
        return PersistentTeacherGroup.getInstance(getDepartment(), getExecutionYear());
    }
}
