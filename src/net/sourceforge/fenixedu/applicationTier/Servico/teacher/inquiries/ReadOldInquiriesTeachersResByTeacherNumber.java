/*
 * Created on Feb 4, 2005
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.inquiries;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoOldInquiriesTeachersRes;
import net.sourceforge.fenixedu.domain.inquiries.IOldInquiriesTeachersRes;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.persistenceTier.inquiries.IPersistentOldInquiriesTeachersRes;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class ReadOldInquiriesTeachersResByTeacherNumber implements IServico {

    private static ReadOldInquiriesTeachersResByTeacherNumber service = new ReadOldInquiriesTeachersResByTeacherNumber();
    
    private ReadOldInquiriesTeachersResByTeacherNumber() {
    }
    
    public String getNome() {
        return "ReadOldInquiriesTeachersResByTeacherNumber";
    }

    /**
     * @return Returns the service.
     */
    public static ReadOldInquiriesTeachersResByTeacherNumber getService() {
        return service;
    }
    
    public List run(Integer teacherNumber) throws FenixServiceException {
        List oldInquiriesTeachersResList = null;

        try {
            if (teacherNumber == null) {
                throw new FenixServiceException("nullTeacherNumber");
            }
            SuportePersistenteOJB sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentOldInquiriesTeachersRes poits = sp.getIPersistentOldInquiriesTeachersRes();
        
            oldInquiriesTeachersResList = poits.readByTeacherNumber(teacherNumber);
            
            CollectionUtils.transform(oldInquiriesTeachersResList,new Transformer(){

                public Object transform(Object oldInquiriesTeachersRes) {
                    InfoOldInquiriesTeachersRes ioits = new InfoOldInquiriesTeachersRes();
                    try {
                        ioits.copyFromDomain((IOldInquiriesTeachersRes) oldInquiriesTeachersRes);

                    } catch (Exception ex) {
                    }
                                        
                    return ioits;
                }
             	});
                            
        } catch (Exception e) {
            e.printStackTrace();
            throw new FenixServiceException(e);
        }

        return oldInquiriesTeachersResList;
    }  
    
}
