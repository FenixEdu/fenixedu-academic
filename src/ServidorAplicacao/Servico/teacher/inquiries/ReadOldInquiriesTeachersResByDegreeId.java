/*
 * Created on Feb 4, 2005
 * 
 */
package ServidorAplicacao.Servico.teacher.inquiries;

import java.util.List;

import org.apache.commons.collections.Transformer;

import commons.CollectionUtils;

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
public class ReadOldInquiriesTeachersResByDegreeId implements IServico {

    private static ReadOldInquiriesTeachersResByDegreeId service = new ReadOldInquiriesTeachersResByDegreeId();
    
    private ReadOldInquiriesTeachersResByDegreeId() {
    }
    
    public String getNome() {
        return "ReadOldInquiriesTeachersResByDegreeId";
    }

    /**
     * @return Returns the service.
     */
    public static ReadOldInquiriesTeachersResByDegreeId getService() {
        return service;
    }
    
    public List run(Integer degreeId) throws FenixServiceException {
        List oldInquiriesTeachersResList = null;

        try {
            if (degreeId == null) {
                throw new FenixServiceException("nullDegreeId");
            }
            SuportePersistenteOJB sp = SuportePersistenteOJB.getInstance();
            IPersistentOldInquiriesTeachersRes poits = sp.getIPersistentOldInquiriesTeachersRes();
        
            oldInquiriesTeachersResList = poits.readByDegreeId(degreeId);
            
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
