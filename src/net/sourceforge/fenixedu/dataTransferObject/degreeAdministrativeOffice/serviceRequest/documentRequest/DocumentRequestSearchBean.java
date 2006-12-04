package net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.serviceRequest.documentRequest;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.candidacy.DFACandidacy;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;

public class DocumentRequestSearchBean implements Serializable {

    private DocumentRequestType chosenDocumentRequestType;

    private AcademicServiceRequestSituationType academicServiceRequestSituationType;

    private Boolean urgentRequest;

    private DomainReference<Registration> registration;

    public DocumentRequestSearchBean() {

    }
    
    public Registration getRegistration() {
        return (this.registration == null) ? null : this.registration.getObject();
    }

    public void setRegistration(Registration registration) {
        this.registration = (registration != null) ? new DomainReference<Registration>(registration)
                : null;
    }

    public DocumentRequestType getChosenDocumentRequestType() {
        return chosenDocumentRequestType;
    }

    public void setChosenDocumentRequestType(DocumentRequestType chosenDocumentRequestType) {
        this.chosenDocumentRequestType = chosenDocumentRequestType;
    }

    public AcademicServiceRequestSituationType getAcademicServiceRequestSituationType() {
        return academicServiceRequestSituationType;
    }

    public void setAcademicServiceRequestSituationType(
            AcademicServiceRequestSituationType academicServiceRequestSituationType) {
        this.academicServiceRequestSituationType = academicServiceRequestSituationType;
    }

    public Boolean getUrgentRequest() {
        return urgentRequest;
    }

    public void setUrgentRequest(Boolean urgentRequest) {
        this.urgentRequest = urgentRequest;
    }

}
