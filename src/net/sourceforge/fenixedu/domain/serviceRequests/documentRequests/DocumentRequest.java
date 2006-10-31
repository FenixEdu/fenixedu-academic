package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.util.LanguageUtils;

public abstract class DocumentRequest extends DocumentRequest_Base {

    protected DocumentRequest() {
	super();
    }

    protected DocumentRequest(StudentCurricularPlan studentCurricularPlan, AdministrativeOffice administrativeOffice) {
	this();
	init(studentCurricularPlan, administrativeOffice);
    }

    @Override
    public String getDescription() {
	return getDescription("AcademicServiceRequestType.DOCUMENT", getDocumentRequestType().getQualifiedName());
    }

    public abstract DocumentRequestType getDocumentRequestType();    
    
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
