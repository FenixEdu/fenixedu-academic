/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.oldInquiries;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;

import org.apache.struts.action.ActionForm;

import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class SearchInquiriesResultPageDTO extends ActionForm {

    private String method;

    private Long executionSemesterID;

    private ExecutionSemester executionSemester;

    private Long executionDegreeID;

    private ExecutionDegree executionDegree;

    private Long executionCourseID;

    private ExecutionCourse executionCourse;

    public Long getExecutionSemesterID() {
        return executionSemesterID;
    }

    public boolean isEmptyExecutionSemesterID() {
        return isNullOrZero(executionSemesterID);
    }

    public void setExecutionSemesterID(Long executionSemesterID) {
        this.executionSemesterID = executionSemesterID;
        this.executionSemester =
                isNullOrZero(executionSemesterID) ? null : (ExecutionSemester) AbstractDomainObject.fromOID(executionSemesterID);
    }

    private boolean isNullOrZero(Long id) {
        return id == null || id.intValue() == 0;
    }

    public Long getExecutionDegreeID() {
        return executionDegreeID;
    }

    public void setExecutionDegreeID(Long executionDegreeID) {
        this.executionDegreeID = executionDegreeID;
        this.executionDegree =
                isNullOrZero(executionDegreeID) ? null : (ExecutionDegree) AbstractDomainObject.fromOID(executionDegreeID);
    }

    public Long getExecutionCourseID() {
        return executionCourseID;
    }

    public void setExecutionCourseID(Long executionCourseID) {
        this.executionCourseID = executionCourseID;
        this.executionCourse =
                isNullOrZero(executionCourseID) ? null : (ExecutionCourse) AbstractDomainObject.fromOID(executionCourseID);
    }

    public ExecutionSemester getExecutionSemester() {
        return executionSemester;
    }

    public void setExecutionSemester(ExecutionSemester executionSemester) {
        this.executionSemester = executionSemester;
    }

    public ExecutionDegree getExecutionDegree() {
        return executionDegree;
    }

    public ExecutionCourse getExecutionCourse() {
        return executionCourse;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

}
