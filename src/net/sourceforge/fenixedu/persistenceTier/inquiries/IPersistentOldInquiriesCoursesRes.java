/*
 * Created on Mar 10, 2005
 * 
 */
package net.sourceforge.fenixedu.persistenceTier.inquiries;

import net.sourceforge.fenixedu.domain.inquiries.IOldInquiriesCoursesRes;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public interface IPersistentOldInquiriesCoursesRes {
    public IOldInquiriesCoursesRes readByExecutionPeriodAndDegreeIdAndCourseCode(
            Integer executionPeriodId, Integer degreeId, String courseCode)
            throws ExcepcaoPersistencia;
}