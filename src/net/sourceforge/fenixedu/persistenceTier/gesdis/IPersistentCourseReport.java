/*
 * Created on 11/Nov/2003
 * 
 */
package net.sourceforge.fenixedu.persistenceTier.gesdis;

import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.gesdis.ICourseReport;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public interface IPersistentCourseReport extends IPersistentObject {

    public ICourseReport readCourseReportByExecutionCourse(IExecutionCourse executionCourse)
            throws ExcepcaoPersistencia;

    public void delete(ICourseReport courseReport) throws ExcepcaoPersistencia;
}