/*
 * Created on Feb 4, 2005
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.inquiries;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoOldInquiriesTeachersRes;
import net.sourceforge.fenixedu.domain.inquiries.IOldInquiriesTeachersRes;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.inquiries.IPersistentOldInquiriesTeachersRes;

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
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
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
