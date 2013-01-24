package net.sourceforge.fenixedu.dataTransferObject.academicAdministration;

import java.io.Serializable;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.IDocumentRequestBean;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.AcademicServiceRequestType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType;
import net.sourceforge.fenixedu.domain.student.Registration;

public class DocumentRequestSearchBean implements Serializable, IDocumentRequestBean {

    private DocumentRequestType chosenDocumentRequestType;

    private Registration registration;

    private AcademicServiceRequestType academicServiceRequestType;

    private AcademicServiceRequestSituationType academicServiceRequestSituationType;

    private boolean urgentRequest;

    public DocumentRequestSearchBean() {
    }

    @Override
    public Registration getRegistration() {
	return registration;
    }

    public void setRegistration(Registration registration) {
	this.registration = registration;
    }

    public DocumentRequestType getChosenDocumentRequestType() {
	return chosenDocumentRequestType;
    }

    public void setChosenDocumentRequestType(DocumentRequestType chosenDocumentRequestType) {
	this.chosenDocumentRequestType = chosenDocumentRequestType;
    }

    @Override
    public boolean hasRegistration() {
	return getRegistration() != null;
    }

    public AcademicServiceRequestType getAcademicServiceRequestType() {
	return academicServiceRequestType;
    }

    public void setAcademicServiceRequestType(AcademicServiceRequestType academicServiceRequestType) {
	this.academicServiceRequestType = academicServiceRequestType;
    }

    public AcademicServiceRequestSituationType getAcademicServiceRequestSituationType() {
	return academicServiceRequestSituationType;
    }

    public void setAcademicServiceRequestSituationType(AcademicServiceRequestSituationType academicServiceRequestSituationType) {
	this.academicServiceRequestSituationType = academicServiceRequestSituationType;
    }

    public boolean getUrgentRequest() {
	return urgentRequest;
    }

    public void setUrgentRequest(boolean urgentRequest) {
	this.urgentRequest = urgentRequest;
    }

    public boolean isUrgentRequest() {
	return urgentRequest;
    }
}
