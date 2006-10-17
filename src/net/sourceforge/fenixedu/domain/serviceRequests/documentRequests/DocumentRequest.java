package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public abstract class DocumentRequest extends DocumentRequest_Base {

    protected DocumentRequest() {
	super();
    }

    protected DocumentRequest(StudentCurricularPlan studentCurricularPlan, AdministrativeOffice administrativeOffice, DocumentRequestType documentRequestType) {
	this();
	init(studentCurricularPlan, administrativeOffice, documentRequestType);
    }

    protected void init(StudentCurricularPlan studentCurricularPlan, AdministrativeOffice administrativeOffice, DocumentRequestType documentRequestType) {
	super.init(studentCurricularPlan, administrativeOffice);

	if (documentRequestType == null) {
	    throw new DomainException(
		    "error.serviceRequests.DocumentRequest.studentCurricularPlan.cannot.be.null");
	}
	setDocumentRequestType(documentRequestType);
    }

    public abstract String getDocumentTemplateKey();

    public boolean isCertificate() {
	return this instanceof CertificateRequest;
    }

    public boolean isDeclaration() {
	return this instanceof DeclarationRequest;
    }

    public boolean isDegreeDiploma() {
	return this instanceof DegreeDiploma;
    }

}
