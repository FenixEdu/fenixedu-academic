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
public class ReadOldInquiriesTeachersResByExecutionPeriodAndDegreeIdAndCurricularYearAndCourseCode implements IServico {

    private static ReadOldInquiriesTeachersResByExecutionPeriodAndDegreeIdAndCurricularYearAndCourseCode service =
        new ReadOldInquiriesTeachersResByExecutionPeriodAndDegreeIdAndCurricularYearAndCourseCode();
    
    private ReadOldInquiriesTeachersResByExecutionPeriodAndDegreeIdAndCurricularYearAndCourseCode() {
    }
    
    public String getNome() {
        return "ReadOldInquiriesTeachersResByExecutionPeriodAndDegreeIdAndCurricularYearAndCourseCode";
    }

    /**
     * @return Returns the service.
     */
    public static ReadOldInquiriesTeachersResByExecutionPeriodAndDegreeIdAndCurricularYearAndCourseCode getService() {
        return service;
    }
    
    public List run(Integer executionPeriodId, Integer degreeId, Integer curricularYear, String courseCode)
    throws FenixServiceException {
        List oldInquiriesTeachersResList = null;

        try {
            if (executionPeriodId == null) {
                throw new FenixServiceException("nullExecutionPeriodId");
            }
            if (degreeId == null) {
                throw new FenixServiceException("nullDegreeId");
            }
            if (curricularYear == null) {
                throw new FenixServiceException("nullCurricularYear");
            }
            if (courseCode == null) {
                throw new FenixServiceException("nullCourseCode");
            }
            SuportePersistenteOJB sp = SuportePersistenteOJB.getInstance();
            IPersistentOldInquiriesTeachersRes poits = sp.getIPersistentOldInquiriesTeachersRes();
        
            oldInquiriesTeachersResList = poits.readByExecutionPeriodAndDegreeIdAndCurricularYearAndCourseCode(
                    executionPeriodId, degreeId, curricularYear, courseCode);
            
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
