package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.io.InputStream;
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
    private transient InputStream inputStream;

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
    public void saveComments() {
        getExecutionCourseAudit().setMeasuresToTake(getMeasuresToTake());
        getExecutionCourseAudit().setConclusions(getConclusions());
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public InputStream getInputStream() {
        return inputStream;
    }
}
