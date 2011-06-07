package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.inquiries.ExecutionCourseAudit;
import pt.ist.fenixWebFramework.services.Service;

public class AuditProcessBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private ExecutionCourseAudit executionCourseAudit;
    private String measuresToTake;
    private String conclusions;
    private Boolean approvedByTeacher;
    private Boolean approvedByStudent;

    public AuditProcessBean(ExecutionCourseAudit executionCourseAudit) {
	setExecutionCourseAudit(executionCourseAudit);
	setMeasuresToTake(executionCourseAudit.getMeasuresToTake());
	setConclusions(executionCourseAudit.getConclusions());
	setApprovedByTeacher(executionCourseAudit.getApprovedByTeacher());
	setApprovedByStudent(executionCourseAudit.getApprovedByStudent());
    }

    public ExecutionCourseAudit getExecutionCourseAudit() {
	return executionCourseAudit;
    }

    public void setExecutionCourseAudit(ExecutionCourseAudit executionCourseAudit) {
	this.executionCourseAudit = executionCourseAudit;
    }

    public String getMeasuresToTake() {
	return measuresToTake;
    }

    public void setMeasuresToTake(String measuresToTake) {
	this.measuresToTake = measuresToTake;
    }

    public String getConclusions() {
	return conclusions;
    }

    public void setConclusions(String conclusions) {
	this.conclusions = conclusions;
    }

    public Boolean getApprovedByTeacher() {
	return approvedByTeacher;
    }

    public void setApprovedByTeacher(Boolean approvedByTeacher) {
	this.approvedByTeacher = approvedByTeacher;
    }

    public Boolean getApprovedByStudent() {
	return approvedByStudent;
    }

    public void setApprovedByStudent(Boolean approvedByStudent) {
	this.approvedByStudent = approvedByStudent;
    }

    @Service
    public void saveData(boolean isTeacher) {
	if (isTeacher) {
	    if (getExecutionCourseAudit().getApprovedByStudent() == null || !getExecutionCourseAudit().getApprovedByStudent()) {
		editText();
	    }
	    getExecutionCourseAudit().setApprovedByTeacher(getApprovedByTeacher());
	} else {
	    if (getExecutionCourseAudit().getApprovedByTeacher() == null || !getExecutionCourseAudit().getApprovedByTeacher()) {
		editText();
	    }
	    getExecutionCourseAudit().setApprovedByStudent(getApprovedByStudent());
	}
    }

    private void editText() {
	getExecutionCourseAudit().setMeasuresToTake(getMeasuresToTake());
	getExecutionCourseAudit().setConclusions(getConclusions());
    }
}
