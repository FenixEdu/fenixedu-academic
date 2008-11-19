/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;

import org.apache.struts.action.ActionForm;

public class SearchInquiriesResultPageDTO extends ActionForm {

    private String method;

    private Integer executionSemesterID;

    private DomainReference<ExecutionSemester> executionSemester;

    private Integer executionDegreeID;

    private DomainReference<ExecutionDegree> executionDegree;

    private Integer executionCourseID;

    private DomainReference<ExecutionCourse> executionCourse;

    public Integer getExecutionSemesterID() {
	return executionSemesterID;
    }

    public void setExecutionSemesterID(Integer executionSemesterID) {
	this.executionSemesterID = executionSemesterID;
	this.executionSemester = isNullOrZero(executionSemesterID) ? null : new DomainReference<ExecutionSemester>(
		ExecutionSemester.class, executionSemesterID);
    }

    private boolean isNullOrZero(Integer id) {
	return id == null || id.intValue() == 0;
    }

    public Integer getExecutionDegreeID() {
	return executionDegreeID;
    }

    public void setExecutionDegreeID(Integer executionDegreeID) {
	this.executionDegreeID = executionDegreeID;
	this.executionDegree = isNullOrZero(executionDegreeID) ? null : new DomainReference<ExecutionDegree>(
		ExecutionDegree.class, executionDegreeID);
    }

    public Integer getExecutionCourseID() {
	return executionCourseID;
    }

    public void setExecutionCourseID(Integer executionCourseID) {
	this.executionCourseID = executionCourseID;
	this.executionCourse = isNullOrZero(executionCourseID) ? null : new DomainReference<ExecutionCourse>(
		ExecutionCourse.class, executionCourseID);
    }

    public ExecutionSemester getExecutionSemester() {
	return executionSemester == null ? null : executionSemester.getObject();
    }

    public void setExecutionSemester(ExecutionSemester executionSemester) {
	this.executionSemester = new DomainReference<ExecutionSemester>(executionSemester);
    }

    public ExecutionDegree getExecutionDegree() {
	return executionDegree == null ? null : executionDegree.getObject();
    }

    public ExecutionCourse getExecutionCourse() {
	return executionCourse == null ? null : executionCourse.getObject();
    }

    public String getMethod() {
	return method;
    }

    public void setMethod(String method) {
	this.method = method;
    }

}
