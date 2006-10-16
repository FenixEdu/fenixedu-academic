package net.sourceforge.fenixedu.applicationTier.Servico.serviceRequests.documentRequests;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.serviceRequest.documentRequest.DocumentRequestEditBean;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.CertificateRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DeclarationRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;

public class EditDocumentRequest extends Service {

    public void run(final DocumentRequestEditBean documentRequestEditBean) throws FenixServiceException {
        final DocumentRequest documentRequest = documentRequestEditBean.getDocumentRequest();
        if (documentRequest.isCertificate()) {
            ((CertificateRequest)documentRequest).edit(documentRequestEditBean.getAcademicServiceRequestSituationType(),
                    documentRequestEditBean.getEmployee(), documentRequestEditBean.getJustification(),
                    documentRequestEditBean.getNumberOfPages());    
        } else if (documentRequest.isDeclaration()) {
            ((DeclarationRequest)documentRequest).edit(documentRequestEditBean.getAcademicServiceRequestSituationType(),
                    documentRequestEditBean.getEmployee(), documentRequestEditBean.getJustification(),
                    documentRequestEditBean.getNumberOfPages());    
        }
        
    }

}
