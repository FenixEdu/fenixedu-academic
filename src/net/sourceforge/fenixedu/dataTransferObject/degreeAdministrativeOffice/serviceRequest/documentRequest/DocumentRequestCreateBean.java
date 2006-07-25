package net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.serviceRequest.documentRequest;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentPurposeType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType;

public class DocumentRequestCreateBean implements Serializable {

    private DomainReference<StudentCurricularPlan> studentCurricularPlan;

    private DocumentRequestType chosenDocumentRequestType;

    private DocumentPurposeType chosenDocumentPurposeType;

    private String otherPurpose;

    private String notes;

    private Boolean urgentRequest;

    private Boolean average;

    private Boolean detailed;

    private DomainReference<ExecutionYear> executionYear;

    private Boolean toBeCreated;
    
    public DocumentRequestCreateBean() {
    }

    public StudentCurricularPlan getStudentCurricularPlan() {
        return (this.studentCurricularPlan != null) ? this.studentCurricularPlan.getObject() : null;
    }

    public void setStudentCurricularPlan(StudentCurricularPlan studentCurricularPlan) {
        this.studentCurricularPlan = (studentCurricularPlan != null) ? new DomainReference<StudentCurricularPlan>(
                studentCurricularPlan)
                : null;
    }

    public DocumentRequestType getChosenDocumentRequestType() {
        return chosenDocumentRequestType;
    }

    public void setChosenDocumentRequestType(DocumentRequestType chosenDocumentRequestType) {
        this.chosenDocumentRequestType = chosenDocumentRequestType;
    }

    public DocumentPurposeType getChosenDocumentPurposeType() {
        return chosenDocumentPurposeType;
    }

    public void setChosenDocumentPurposeType(DocumentPurposeType chosenDocumentPurposeType) {
        this.chosenDocumentPurposeType = chosenDocumentPurposeType;
    }

    public String getOtherPurpose() {
        return otherPurpose;
    }

    public void setOtherPurpose(String otherPurpose) {
        this.otherPurpose = otherPurpose;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getUrgentRequest() {
        return urgentRequest;
    }

    public void setUrgentRequest(Boolean urgentRequest) {
        this.urgentRequest = urgentRequest;
    }

    public Boolean getAverage() {
        return average;
    }

    public void setAverage(Boolean average) {
        this.average = average;
    }

    public Boolean getDetailed() {
        return detailed;
    }

    public void setDetailed(Boolean detailed) {
        this.detailed = detailed;
    }

    public ExecutionYear getExecutionYear() {
        return (this.executionYear != null) ? this.executionYear.getObject() : null;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
        this.executionYear = (executionYear != null) ? new DomainReference<ExecutionYear>(executionYear)
                : null;
    }
    
    public Boolean getToBeCreated() {
        return toBeCreated;
    }

    public void setToBeCreated(Boolean toBeCreated) {
        this.toBeCreated = toBeCreated;
    }

}
