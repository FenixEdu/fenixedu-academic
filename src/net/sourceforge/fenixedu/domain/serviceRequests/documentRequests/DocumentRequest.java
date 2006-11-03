package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

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
    
    public String getDocumentFileName() {
	final StringBuilder result = new StringBuilder();
	
	
	result.append(getRegistration().getPerson().getIstUsername());
	result.append("-");
	result.append(new DateTime().toString(DateTimeFormat.forPattern("yyyyMMdd")));
	result.append("-");
	result.append(getDescription().replace(":","").replace(" ",""));
	
	return result.toString();
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
    
    public boolean isPagedDocument() {
	return isCertificate() || isDeclaration();
    }

}
