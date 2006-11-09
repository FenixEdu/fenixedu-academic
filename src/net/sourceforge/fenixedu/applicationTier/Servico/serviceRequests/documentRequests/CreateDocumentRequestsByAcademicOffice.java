package net.sourceforge.fenixedu.applicationTier.Servico.serviceRequests.documentRequests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.serviceRequest.documentRequest.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.events.dfa.DFACandidacyEvent;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.CertificateRequestEvent;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituation;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.CertificateRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DeclarationRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType;

public class CreateDocumentRequestsByAcademicOffice extends Service {

    public List<String> run(final List<DocumentRequestCreateBean> documentRequestCreateBeans,AdministrativeOffice administrativeOffice) {
        List<String> messages = new ArrayList<String>();
        
        for (final DocumentRequestCreateBean documentRequestCreateBean : documentRequestCreateBeans) {
            
            try {
        	final DocumentRequestType documentRequestType = documentRequestCreateBean.getChosenDocumentRequestType();
  
            if (documentRequestType.isCertificate()) {
                CertificateRequest certificateRequest = CertificateRequest.create(documentRequestCreateBean.getStudentCurricularPlan(),
                        documentRequestCreateBean.getChosenDocumentRequestType(), 
                        documentRequestCreateBean.getChosenDocumentPurposeType(),
                        documentRequestCreateBean.getOtherPurpose(), 
                        documentRequestCreateBean.getNotes(),
                        documentRequestCreateBean.getUrgentRequest(),
                        documentRequestCreateBean.getAverage(), 
                        documentRequestCreateBean.getDetailed(),
                        documentRequestCreateBean.getExecutionYear());
                if(documentRequestType.equals(DocumentRequestType.ENROLMENT_CERTIFICATE)){
                    new CertificateRequestEvent(administrativeOffice, EventType.ENROLMENT_CERTIFICATE_REQUEST,documentRequestCreateBean.getStudentCurricularPlan().getRegistration().getPerson(), certificateRequest);
                }
            } else if (documentRequestType.isDeclaration()) {
                  DeclarationRequest.create(documentRequestCreateBean.getStudentCurricularPlan(),
                            documentRequestCreateBean.getChosenDocumentRequestType(), 
                            documentRequestCreateBean.getChosenDocumentPurposeType(),
                            documentRequestCreateBean.getOtherPurpose(), 
                            documentRequestCreateBean.getNotes(),
                            documentRequestCreateBean.getUrgentRequest(), 
                            documentRequestCreateBean.getAverage(), 
                            documentRequestCreateBean.getDetailed(),
                            documentRequestCreateBean.getExecutionYear());
                    
                    
            }
            } catch (DomainException e) {
                messages.add(e.getMessage());
            }   
        }
        
        return messages;
    }

}
