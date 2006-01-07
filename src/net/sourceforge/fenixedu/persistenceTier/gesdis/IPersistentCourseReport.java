/*
 * Created on 11/Nov/2003
 * 
 */
package net.sourceforge.fenixedu.persistenceTier.gesdis;

import net.sourceforge.fenixedu.domain.gesdis.CourseReport;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public interface IPersistentCourseReport extends IPersistentObject {

    public CourseReport readCourseReportByExecutionCourse(Integer executionCourseOID)
            throws ExcepcaoPersistencia;
}