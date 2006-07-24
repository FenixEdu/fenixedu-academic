package net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.MarkSheet;

public class GradesToSubmitExecutionCourseSendMailBean implements Serializable{
	
	private DomainReference<ExecutionCourse> executionCourse;
	private boolean toSubmit;
	
	public GradesToSubmitExecutionCourseSendMailBean(ExecutionCourse executionCourse, boolean toSubmit) {
		setExecutionCourse(executionCourse);
		setToSubmit(toSubmit);
	}
	
	public ExecutionCourse getExecutionCourse() {
		return (this.executionCourse != null) ? this.executionCourse.getObject() : null;
	}
	
	public void setExecutionCourse(ExecutionCourse executionCourse) {
		this.executionCourse = ( executionCourse != null ) ? new DomainReference<ExecutionCourse>(executionCourse) : null;
	}
	
	public boolean isToSubmit() {
		return toSubmit;
	}
	
	public void setToSubmit(boolean toSubmit) {
		this.toSubmit = toSubmit;
	} 	
}
