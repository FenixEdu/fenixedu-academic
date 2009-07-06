/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;

import org.apache.struts.action.ActionForm;

public class SearchInquiriesResultPageDTO extends ActionForm {

    private String method;

    private Long executionSemesterID;

    private DomainReference<ExecutionSemester> executionSemester;

    private Long executionDegreeID;

    private DomainReference<ExecutionDegree> executionDegree;

    private Long executionCourseID;

    private DomainReference<ExecutionCourse> executionCourse;

    public Long getExecutionSemesterID() {
	return executionSemesterID;
    }

    public boolean isEmptyExecutionSemesterID() {
	return isNullOrZero(executionSemesterID);
    }

    public void setExecutionSemesterID(Long executionSemesterID) {
	this.executionSemesterID = executionSemesterID;
	this.executionSemester = isNullOrZero(executionSemesterID) ? null : new DomainReference<ExecutionSemester>(
		(ExecutionSemester) DomainObject.fromOID(executionSemesterID));
    }

    private boolean isNullOrZero(Long id) {
	return id == null || id.intValue() == 0;
    }

    public Long getExecutionDegreeID() {
	return executionDegreeID;
    }

    public void setExecutionDegreeID(Long executionDegreeID) {
	this.executionDegreeID = executionDegreeID;
	this.executionDegree = isNullOrZero(executionDegreeID) ? null : new DomainReference<ExecutionDegree>(
		(ExecutionDegree) DomainObject.fromOID(executionDegreeID));
    }

    public Long getExecutionCourseID() {
	return executionCourseID;
    }

    public void setExecutionCourseID(Long executionCourseID) {
	this.executionCourseID = executionCourseID;
	this.executionCourse = isNullOrZero(executionCourseID) ? null : new DomainReference<ExecutionCourse>(
		(ExecutionCourse) DomainObject.fromOID(executionCourseID));
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
