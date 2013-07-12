/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.oldInquiries;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;

import org.apache.struts.action.ActionForm;

import pt.ist.fenixframework.FenixFramework;

public class SearchInquiriesResultPageDTO extends ActionForm {

    private String method;

    private String executionSemesterID;

    private ExecutionSemester executionSemester;

    private String executionDegreeID;

    private ExecutionDegree executionDegree;

    private String executionCourseID;

    private ExecutionCourse executionCourse;

    public String getExecutionSemesterID() {
        return executionSemesterID;
    }

    public boolean isEmptyExecutionSemesterID() {
        return isNullOrZero(executionSemesterID);
    }

    public void setExecutionSemesterID(String executionSemesterID) {
        this.executionSemesterID = executionSemesterID;
        this.executionSemester =
                isNullOrZero(executionSemesterID) ? null : (ExecutionSemester) FenixFramework
                        .getDomainObject(executionSemesterID);
    }

    private boolean isNullOrZero(String id) {
        return id == null;
    }

    public String getExecutionDegreeID() {
        return executionDegreeID;
    }

    public void setExecutionDegreeID(String executionDegreeID) {
        this.executionDegreeID = executionDegreeID;
        this.executionDegree =
                isNullOrZero(executionDegreeID) ? null : (ExecutionDegree) FenixFramework.getDomainObject(executionDegreeID);
    }

    public String getExecutionCourseID() {
        return executionCourseID;
    }

    public void setExecutionCourseID(String executionCourseID) {
        this.executionCourseID = executionCourseID;
        this.executionCourse =
                isNullOrZero(executionCourseID) ? null : (ExecutionCourse) FenixFramework.getDomainObject(executionCourseID);
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
