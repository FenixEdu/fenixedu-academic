/*
 * Created on Mar 10, 2005
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.course.inquiries;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoOldInquiriesCoursesRes;
import net.sourceforge.fenixedu.domain.inquiries.IOldInquiriesCoursesRes;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.persistenceTier.inquiries.IPersistentOldInquiriesCoursesRes;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class ReadOldInquiryCoursesResByExecutionPeriodAndDegreeIdAndCourseCode
        implements IServico {


    private static ReadOldInquiryCoursesResByExecutionPeriodAndDegreeIdAndCourseCode service = new ReadOldInquiryCoursesResByExecutionPeriodAndDegreeIdAndCourseCode();
    
    private ReadOldInquiryCoursesResByExecutionPeriodAndDegreeIdAndCourseCode() {
    }
    
    public String getNome() {
        return "ReadOldInquiryCoursesResByExecutionPeriodAndDegreeIdAndCourseCode";
    }

    /**
     * @return Returns the service.
     */
    public static ReadOldInquiryCoursesResByExecutionPeriodAndDegreeIdAndCourseCode getService() {
        return service;
    }
    
    public InfoOldInquiriesCoursesRes run(Integer executionPeriodId, Integer degreeId, String courseCode)
    throws FenixServiceException {
        InfoOldInquiriesCoursesRes oldInquiriesCoursesRes = null;

        try {
            if (executionPeriodId == null) {
                throw new FenixServiceException("nullExecutionPeriodId");
            }
            if (degreeId == null) {
                throw new FenixServiceException("nullDegreeId");
            }
            if (courseCode == null) {
                throw new FenixServiceException("nullCourseCode");
            }
            SuportePersistenteOJB sp = PersistenceSupportFactory.getOJBPersistenceSupport();
            
            IPersistentOldInquiriesCoursesRes poics = sp.getIPersistentOldInquiriesCoursesRes();
        
            IOldInquiriesCoursesRes oics = poics.readByExecutionPeriodAndDegreeIdAndCourseCode(
                    executionPeriodId, degreeId, courseCode);
            
            oldInquiriesCoursesRes = new InfoOldInquiriesCoursesRes();
            oldInquiriesCoursesRes.copyFromDomain(oics);
            
                            
        } catch (Exception e) {
            e.printStackTrace();
            throw new FenixServiceException(e);
        }

        return oldInquiriesCoursesRes;
    }  
}
