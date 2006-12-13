package net.sourceforge.fenixedu.applicationTier.Servico.student.administrativeOfficeServices;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.serviceRequest.documentRequest.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest.DocumentRequestCreator;

public class CreateDocumentRequests extends Service {

    public List<String> run(final List<DocumentRequestCreateBean> documentRequestCreateBeans) {
        List<String> messages = new ArrayList<String>();
        
        for (final DocumentRequestCreateBean documentRequestCreateBean : documentRequestCreateBeans) {
            
            try {
        	new DocumentRequestCreator(documentRequestCreateBean.getRegistration()).execute();
            } catch (DomainException e) {
                messages.add(e.getMessage());
            }
        }
        
        return messages;
    }

}
