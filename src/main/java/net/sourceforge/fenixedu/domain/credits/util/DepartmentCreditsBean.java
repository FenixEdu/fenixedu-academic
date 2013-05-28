package net.sourceforge.fenixedu.domain.credits.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;

public class DepartmentCreditsBean implements Serializable {

    protected Department department;

    protected ExecutionSemester executionSemester;

    protected ExecutionYear executionYear;

    protected List<Department> availableDepartments;

    public DepartmentCreditsBean() {
        setExecutionSemester(ExecutionSemester.readActualExecutionSemester());
        setExecutionYear(getExecutionSemester().getExecutionYear());
    }

    public DepartmentCreditsBean(Department department, ArrayList<Department> availableDepartments) {
        setExecutionSemester(ExecutionSemester.readActualExecutionSemester());
        setExecutionYear(getExecutionSemester().getExecutionYear());
        setDepartment(department);
        setAvailableDepartments(availableDepartments);
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public ExecutionSemester getExecutionSemester() {
        return executionSemester;
    }

    public void setExecutionSemester(ExecutionSemester executionSemester) {
        this.executionSemester = executionSemester;
    }

    public ExecutionYear getExecutionYear() {
        return executionYear;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
        this.executionYear = executionYear;
    }

    public List<Department> getAvailableDepartments() {
        return availableDepartments;
    }

    public void setAvailableDepartments(List<Department> availableDepartments) {
        this.availableDepartments = availableDepartments;
        if (availableDepartments.size() == 1) {
            setDepartment(availableDepartments.get(0));
        }
    }

    public List<ExecutionCourse> getDepartmentExecutionCourses() {
        List<ExecutionCourse> result = new ArrayList<ExecutionCourse>();
        if (department != null) {
            for (CompetenceCourse competenceCourse : department.getDepartmentUnit().getCompetenceCourses()) {
                for (ExecutionCourse executionCourse : competenceCourse
                        .getExecutionCoursesByExecutionPeriod(getExecutionSemester())) {
                    if (!executionCourse.isDissertation()) {
                        result.add(executionCourse);
                    }
                }
            }
        }
        return result;
    }

    public List<PersonFunction> getDepartmentPersonFunctions() {
        List<PersonFunction> result = new ArrayList<PersonFunction>();
        if (department != null) {
            for (Teacher teacher : department.getAllTeachers(getExecutionSemester().getAcademicInterval())) {
                result.addAll(teacher.getPerson().getPersonFuntions(getExecutionSemester().getBeginDateYearMonthDay(),
                        getExecutionSemester().getEndDateYearMonthDay()));
            }
        }
        return result;
    }

}