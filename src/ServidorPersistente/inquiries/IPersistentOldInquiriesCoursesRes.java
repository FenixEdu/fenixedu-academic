/*
 * Created on Mar 10, 2005
 * 
 */
package ServidorPersistente.inquiries;

import Dominio.inquiries.IOldInquiriesCoursesRes;
import ServidorPersistente.ExcepcaoPersistencia;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public interface IPersistentOldInquiriesCoursesRes {
    public IOldInquiriesCoursesRes readByExecutionPeriodAndDegreeIdAndCourseCode(
            Integer executionPeriodId, Integer degreeId, String courseCode)
            throws ExcepcaoPersistencia;
}