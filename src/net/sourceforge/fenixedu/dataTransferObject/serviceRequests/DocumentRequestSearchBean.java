package net.sourceforge.fenixedu.dataTransferObject.serviceRequests;

import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType;
import net.sourceforge.fenixedu.domain.student.Registration;

public class DocumentRequestSearchBean extends AcademicServiceRequestSearchBean implements IDocumentRequestBean {

    private DocumentRequestType chosenDocumentRequestType;

    private Registration registration;

    public DocumentRequestSearchBean() {

    }

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

    public boolean hasRegistration() {
	return getRegistration() != null;
    }

}
