package org.fenixedu.academic.ui.spring.controller.teacher.authorization;

import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.ExecutionSemester;

public class SearchBean {

    private Department department;
    private ExecutionSemester period;

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public ExecutionSemester getPeriod() {
        return period;
    }

    public void setPeriod(ExecutionSemester period) {
        this.period = period;
    }

}
