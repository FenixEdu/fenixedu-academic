/*
 * Created on Feb 4, 2005
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.inquiries;

import java.util.List;

import org.apache.commons.collections.Transformer;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoOldInquiriesTeachersRes;
import net.sourceforge.fenixedu.domain.inquiries.IOldInquiriesTeachersRes;
import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.persistenceTier.inquiries.IPersistentOldInquiriesTeachersRes;

import org.apache.commons.collections.CollectionUtils;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class ReadOldInquiriesTeachersResByExecutionPeriodAndDegreeIdAndCurricularYearAndCourseCodeAndTeacherNumber implements IServico {

    private static ReadOldInquiriesTeachersResByExecutionPeriodAndDegreeIdAndCurricularYearAndCourseCodeAndTeacherNumber service =
        new ReadOldInquiriesTeachersResByExecutionPeriodAndDegreeIdAndCurricularYearAndCourseCodeAndTeacherNumber();
    
    private ReadOldInquiriesTeachersResByExecutionPeriodAndDegreeIdAndCurricularYearAndCourseCodeAndTeacherNumber() {
    }
    
    public String getNome() {
        return "ReadOldInquiriesTeachersResByExecutionPeriodAndDegreeIdAndCurricularYearAndCourseCodeAndTeacherNumber";
    }

    /**
     * @return Returns the service.
     */
    public static ReadOldInquiriesTeachersResByExecutionPeriodAndDegreeIdAndCurricularYearAndCourseCodeAndTeacherNumber getService() {
        return service;
    }
    
    public List run(Integer executionPeriodId, Integer degreeId, Integer curricularYear, String courseCode, Integer teacherNumber)
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
            if (teacherNumber == null) {
                throw new FenixServiceException("nullTeacherNumber");
            }
            SuportePersistenteOJB sp = SuportePersistenteOJB.getInstance();
            IPersistentOldInquiriesTeachersRes poits = sp.getIPersistentOldInquiriesTeachersRes();
        
            oldInquiriesTeachersResList = poits.readByExecutionPeriodAndDegreeIdAndCurricularYearAndCourseCodeAndTeacherNumber(
                    executionPeriodId, degreeId, curricularYear, courseCode, teacherNumber);
            
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
