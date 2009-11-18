package net.sourceforge.fenixedu.domain.serviceRequests;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.documents.GeneratedDocument;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;

public abstract class RegistryCode extends RegistryCode_Base {
    protected RegistryCode() {
	super();
    }

    protected void init(InstitutionRegistryCodeGenerator generator, DocumentRequest request) {
	setRegistryCodeGenerator(generator);
	setRegistryCodeBag(generator.getCurrentBag());
	setDocumentRequest(request);
	setCode(generator.getNextNumber().toString());
    }

    public GeneratedDocument getDocument() {
	return getDocumentRequest().getLastGeneratedDocument();
    }

    @Override
    protected RootDomainObject getRootDomainObject() {
	return getRegistryCodeGenerator().getRootDomainObject();
    }
}
