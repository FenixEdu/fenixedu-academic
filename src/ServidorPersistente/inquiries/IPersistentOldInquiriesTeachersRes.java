/*
 * Created on Feb 4, 2005
 * 
 */
package ServidorPersistente.inquiries;

import java.util.List;

import Dominio.inquiries.IOldInquiriesTeachersRes;
import ServidorPersistente.ExcepcaoPersistencia;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public interface IPersistentOldInquiriesTeachersRes {
    public List readByTeacherNumber(Integer teacherNumber)
            throws ExcepcaoPersistencia;

    public List readByTeacherNumberAndExecutionPeriod(Integer teacherNumber,
            Integer executionPeriodId) throws ExcepcaoPersistencia;
    
    public IOldInquiriesTeachersRes readByInternalId(Integer oldInquiryId)
    		throws ExcepcaoPersistencia;

    public List readByExecutionPeriodAndDegreeIdAndCurricularYearAndCourseCodeAndTeacherNumber(
            Integer executionPeriodId, Integer degreeId, Integer curricularYear, String courseCode, Integer teacherNumber)
    		throws ExcepcaoPersistencia;
    
    public List readByExecutionPeriodAndDegreeIdAndCurricularYearAndCourseCode(
            Integer executionPeriodId, Integer degreeId, Integer curricularYear, String courseCode)
    		throws ExcepcaoPersistencia;

        public List readByDegreeId(Integer degreeId)
    		throws ExcepcaoPersistencia;

}