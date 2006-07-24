package net.sourceforge.fenixedu.applicationTier.Servico.serviceRequests.documentRequests;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.serviceRequest.documentRequest.DocumentRequestEditBean;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;

public class EditDocumentRequest extends Service {

    public void run(final DocumentRequestEditBean documentRequestEditBean) {
        final DocumentRequest documentRequest = documentRequestEditBean.getDocumentRequest();
        documentRequest.edit(documentRequestEditBean.getAcademicServiceRequestSituationType(),
                documentRequestEditBean.getEmployee(), documentRequestEditBean.getJustification(),
                documentRequestEditBean.getNumberOfPages());
    }

}
