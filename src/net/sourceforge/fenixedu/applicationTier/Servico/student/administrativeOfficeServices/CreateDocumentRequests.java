package net.sourceforge.fenixedu.applicationTier.Servico.student.administrativeOfficeServices;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.serviceRequest.documentRequest.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.CertificateRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType;

public class CreateDocumentRequests extends Service {

    public List<String> run(final List<DocumentRequestCreateBean> documentRequestCreateBeans) {
        List<String> messages = new ArrayList<String>();
        
        for (final DocumentRequestCreateBean documentRequestCreateBean : documentRequestCreateBeans) {
            
            try {
        	final DocumentRequestType documentRequestType = documentRequestCreateBean.getChosenDocumentRequestType();
        	if (documentRequestType.isCertificate()) {
                    CertificateRequest.create(documentRequestCreateBean.getRegistration(),
                            documentRequestCreateBean.getChosenDocumentRequestType(), 
                            documentRequestCreateBean.getChosenDocumentPurposeType(),
                            documentRequestCreateBean.getOtherPurpose(), 
                            documentRequestCreateBean.getNotes(),
                            documentRequestCreateBean.getUrgentRequest(),
                            documentRequestCreateBean.getAverage(), 
                            documentRequestCreateBean.getDetailed(),
                            documentRequestCreateBean.getExecutionYear());
        	} else if (documentRequestType.isDeclaration()) {
//                    DeclarationRequest.create(documentRequestCreateBean.getRegistration(),
//                            documentRequestCreateBean.getChosenDocumentRequestType(), 
//                            documentRequestCreateBean.getChosenDocumentPurposeType(),
//                            documentRequestCreateBean.getOtherPurpose(), 
//                            documentRequestCreateBean.getNotes(),
//                            documentRequestCreateBean.getAverage(), 
//                            documentRequestCreateBean.getDetailed(),
//                            documentRequestCreateBean.getExecutionYear(),
//                            documentRequestCreateBean.getYear());
        	}
            } catch (DomainException e) {
                messages.add(e.getMessage());
            }
        }
        
        return messages;
    }

}
