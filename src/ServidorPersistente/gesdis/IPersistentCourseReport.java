/*
 * Created on 11/Nov/2003
 * 
 */
package ServidorPersistente.gesdis;

import Dominio.IExecutionCourse;
import Dominio.gesdis.ICourseReport;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;

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