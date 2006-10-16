package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public abstract class DocumentRequest extends DocumentRequest_Base {

    protected DocumentRequest() {
	super();
    }

    protected DocumentRequest(StudentCurricularPlan studentCurricularPlan, DocumentRequestType documentRequestType) {
	this();
	init(studentCurricularPlan, documentRequestType);
    }

    protected void init(StudentCurricularPlan studentCurricularPlan, DocumentRequestType documentRequestType) {
	super.init(studentCurricularPlan);

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
