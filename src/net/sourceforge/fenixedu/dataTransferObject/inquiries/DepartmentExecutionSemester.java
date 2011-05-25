package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.ExecutionSemester;

public class DepartmentExecutionSemester implements Serializable {

    private static final long serialVersionUID = 1L;
    private ExecutionSemester executionSemester;
    private String departmentUnitOID;

    public ExecutionSemester getExecutionSemester() {
	return executionSemester;
    }

    public void setExecutionSemester(ExecutionSemester executionSemester) {
	this.executionSemester = executionSemester;
    }

    public String getDepartmentUnitOID() {
	return departmentUnitOID;
    }

    public void setDepartmentUnitOID(String departmentUnitOID) {
	this.departmentUnitOID = departmentUnitOID;
    }

}
