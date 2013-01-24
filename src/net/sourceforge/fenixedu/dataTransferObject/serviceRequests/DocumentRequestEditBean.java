package net.sourceforge.fenixedu.dataTransferObject.serviceRequests;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.CertificateRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;

public class DocumentRequestEditBean implements Serializable {
    private Person responsible;

    private DocumentRequest documentRequest;

    private AcademicServiceRequestSituationType academicServiceRequestSituationType;

    private Integer numberOfPages;

    private String justification;

    public DocumentRequestEditBean() {

    }

    public DocumentRequestEditBean(DocumentRequest documentRequest, Person responsible) {
	setDocumentRequest(documentRequest);
	setResponsible(responsible);
	setAcademicServiceRequestSituationType(documentRequest.getAcademicServiceRequestSituationType());
	setJustification(documentRequest.getActiveSituation().getJustification());

	if (documentRequest.isCertificate()) {
	    setNumberOfPages(((CertificateRequest) documentRequest).getNumberOfPages());
	}
    }

    public DocumentRequest getDocumentRequest() {
	return this.documentRequest;
    }

    public void setDocumentRequest(DocumentRequest documentRequest) {
	this.documentRequest = documentRequest;
    }

    public Person getResponsible() {
	return responsible;
    }

    public void setResponsible(Person responsible) {
	this.responsible = responsible;
    }

    public AcademicServiceRequestSituationType getAcademicServiceRequestSituationType() {
	return academicServiceRequestSituationType;
    }

    public void setAcademicServiceRequestSituationType(AcademicServiceRequestSituationType academicServiceRequestSituationType) {
	this.academicServiceRequestSituationType = academicServiceRequestSituationType;
    }

    public Integer getNumberOfPages() {
	return numberOfPages;
    }

    public void setNumberOfPages(Integer numberOfPages) {
	this.numberOfPages = numberOfPages;
    }

    public String getJustification() {
	return justification;
    }

    public void setJustification(String justification) {
	this.justification = justification;
    }

}
