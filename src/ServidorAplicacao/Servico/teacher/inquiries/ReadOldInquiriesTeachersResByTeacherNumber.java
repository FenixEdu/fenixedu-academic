/*
 * Created on Feb 4, 2005
 * 
 */
package ServidorAplicacao.Servico.teacher.inquiries;

import java.util.List;

import org.apache.commons.collections.Transformer;

import DataBeans.inquiries.InfoOldInquiriesTeachersRes;
import Dominio.inquiries.IOldInquiriesTeachersRes;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.inquiries.IPersistentOldInquiriesTeachersRes;

import commons.CollectionUtils;

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
            SuportePersistenteOJB sp = SuportePersistenteOJB.getInstance();
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
