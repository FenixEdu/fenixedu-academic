package net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.ExecutionCourse;

public class GradesToSubmitExecutionCourseSendMailBean implements Serializable {

    private ExecutionCourse executionCourse;
    private boolean toSubmit;

    public GradesToSubmitExecutionCourseSendMailBean(ExecutionCourse executionCourse, boolean toSubmit) {
	setExecutionCourse(executionCourse);
	setToSubmit(toSubmit);
    }

    public ExecutionCourse getExecutionCourse() {
	return this.executionCourse;
    }

    public void setExecutionCourse(ExecutionCourse executionCourse) {
	this.executionCourse = executionCourse;
    }

    public boolean isToSubmit() {
	return toSubmit;
    }

    public void setToSubmit(boolean toSubmit) {
	this.toSubmit = toSubmit;
    }
}
