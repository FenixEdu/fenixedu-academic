package net.sourceforge.fenixedu.applicationTier.factoryExecutors;

import net.sourceforge.fenixedu.applicationTier.Servico.serviceRequests.documentRequests.CreatePastDiplomaRequest;
import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.CertificateRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DeclarationRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DiplomaRequest;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import pt.ist.fenixWebFramework.services.Service;

final public class DocumentRequestCreator extends DocumentRequestCreateBean implements FactoryExecutor {

    public DocumentRequestCreator(Registration registration) {
	super(registration);
    }

    @Service
    public Object execute() {
	if (getChosenDocumentRequestType().isCertificate()) {
	    return CertificateRequest.create(this);

	} else if (getChosenDocumentRequestType().isDeclaration()) {
	    this.setExecutionYear(ExecutionYear.readCurrentExecutionYear());
	    return DeclarationRequest.create(this);

	} else if (getChosenDocumentRequestType().isDiploma()) {
	    return new DiplomaRequest(this);

	} else if (getChosenDocumentRequestType().isPastDiploma()) {
	    return CreatePastDiplomaRequest.create(this);
	}

	return null;
    }

}
