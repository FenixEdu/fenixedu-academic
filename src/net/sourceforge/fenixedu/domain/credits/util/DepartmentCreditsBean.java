package net.sourceforge.fenixedu.domain.credits.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;

public class DepartmentCreditsBean implements Serializable {

	protected Department department;

	protected ExecutionSemester executionSemester;

	protected List<Department> availableDepartments;

	public DepartmentCreditsBean() {
		setExecutionSemester(ExecutionSemester.readActualExecutionSemester());
	}

	public DepartmentCreditsBean(Department department, ArrayList<Department> availableDepartments) {
		setExecutionSemester(ExecutionSemester.readActualExecutionSemester());
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

}