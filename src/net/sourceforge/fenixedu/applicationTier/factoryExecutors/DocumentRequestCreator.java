package net.sourceforge.fenixedu.applicationTier.factoryExecutors;

import net.sourceforge.fenixedu.applicationTier.Servico.serviceRequests.documentRequests.CreatePastDiplomaRequest;
import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.Under23TransportsDeclarationRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.CertificateRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DeclarationRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DiplomaRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DiplomaSupplementRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.RegistryDiplomaRequest;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import pt.ist.fenixWebFramework.services.Service;

final public class DocumentRequestCreator extends DocumentRequestCreateBean implements FactoryExecutor {

    static private final long serialVersionUID = 1L;

    public DocumentRequestCreator(Registration registration) {
	super(registration);
    }

    @Service
    public Object execute() {

	if (getChosenDocumentRequestType().isCertificate()) {
	    return CertificateRequest.create(this);

	} else if (getChosenDocumentRequestType().isDeclaration()) {
	    return DeclarationRequest.create(this);

	} else if (getChosenDocumentRequestType().isDiploma()) {
	    return new DiplomaRequest(this);

	} else if (getChosenDocumentRequestType().isRegistryDiploma()) {
	    return new RegistryDiplomaRequest(this);

	} else if (getChosenDocumentRequestType().isPastDiploma()) {
	    return CreatePastDiplomaRequest.create(this);

	} else if (getChosenDocumentRequestType().isDiplomaSupplement()) {
	    return new DiplomaSupplementRequest(this);

	} else if (getChosenDocumentRequestType() == DocumentRequestType.UNDER_23_TRANSPORTS_REQUEST) {
	    return new Under23TransportsDeclarationRequest(this);
	}

	throw new DomainException("error.DocumentRequestCreator.unexpected.document.request.type");
    }

}
