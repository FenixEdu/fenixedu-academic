/*
 * Created on Feb 4, 2005
 * 
 */
package ServidorAplicacao.Servico.teacher.inquiries;

import DataBeans.inquiries.InfoOldInquiriesTeachersRes;
import Dominio.inquiries.IOldInquiriesTeachersRes;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.inquiries.IPersistentOldInquiriesTeachersRes;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class ReadOldInquiryTeachersResById implements IServico {

    private static ReadOldInquiryTeachersResById service = new ReadOldInquiryTeachersResById();
    
    private ReadOldInquiryTeachersResById() {
    }
    
    public String getNome() {
        return "ReadOldInquiryTeachersResById";
    }

    /**
     * @return Returns the service.
     */
    public static ReadOldInquiryTeachersResById getService() {
        return service;
    }
    
    public InfoOldInquiriesTeachersRes run(Integer internalId) throws FenixServiceException {
        InfoOldInquiriesTeachersRes oldInquiriesTeachersRes = null;

        try {
            if (internalId == null) {
                throw new FenixServiceException("nullInternalId");
            }
            SuportePersistenteOJB sp = SuportePersistenteOJB.getInstance();
            IPersistentOldInquiriesTeachersRes poits = sp.getIPersistentOldInquiriesTeachersRes();
        
            IOldInquiriesTeachersRes oits = poits.readByInternalId(internalId);
            
            oldInquiriesTeachersRes = new InfoOldInquiriesTeachersRes();
            oldInquiriesTeachersRes.copyFromDomain(oits);
            
                            
        } catch (Exception e) {
            e.printStackTrace();
            throw new FenixServiceException(e);
        }

        return oldInquiriesTeachersRes;
    }  
    
}
