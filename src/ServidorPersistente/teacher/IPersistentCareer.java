/*
 * Created on 13/Nov/2003
 *  
 */
package ServidorPersistente.teacher;

import java.util.List;

import Dominio.ITeacher;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import Util.CareerType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public interface IPersistentCareer extends IPersistentObject {

    List readAllByTeacherAndCareerType(ITeacher teacher, CareerType careerType)
            throws ExcepcaoPersistencia;
}