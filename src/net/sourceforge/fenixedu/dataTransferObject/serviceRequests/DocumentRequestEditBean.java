package net.sourceforge.fenixedu.dataTransferObject.serviceRequests;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.CertificateRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;

public class DocumentRequestEditBean implements Serializable {

    private Employee employee;

    private DocumentRequest documentRequest;

    private AcademicServiceRequestSituationType academicServiceRequestSituationType;

    private Integer numberOfPages;

    private String justification;

    public DocumentRequestEditBean() {

    }

    public DocumentRequestEditBean(DocumentRequest documentRequest, Employee employee) {
	setDocumentRequest(documentRequest);
	setEmployee(employee);
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

    public Employee getEmployee() {
	return this.employee;
    }

    public void setEmployee(Employee employee) {
	this.employee = employee;
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
