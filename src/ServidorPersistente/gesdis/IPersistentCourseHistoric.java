/*
 * Created on 17/Fev/2004
 *  
 */
package ServidorPersistente.gesdis;

import java.util.List;

import Dominio.ICurricularCourse;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public interface IPersistentCourseHistoric extends IPersistentObject
{

    public List readByCurricularCourseAndSemester(ICurricularCourse curricularCourse, Integer semester) throws ExcepcaoPersistencia;
}
