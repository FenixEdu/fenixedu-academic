/*
 * Created on 18/Fev/2004
 *  
 */
package ServidorPersistente.gesdis;

import Dominio.ICurricularCourse;
import Dominio.gesdis.IStudentCourseReport;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public interface IPersistentStudentCourseReport extends IPersistentObject {

    public IStudentCourseReport readByCurricularCourse(ICurricularCourse curricularCourse)
            throws ExcepcaoPersistencia;
}