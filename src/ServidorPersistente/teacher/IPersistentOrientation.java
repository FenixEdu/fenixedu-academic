/*
 * Created on 21/Nov/2003
 *
 */
package ServidorPersistente.teacher;

import java.util.List;

import Dominio.ITeacher;
import Dominio.teacher.IOrientation;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import Util.OrientationType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *
 */
public interface IPersistentOrientation extends IPersistentObject
{
    public IOrientation readByTeacherAndOrientationType(ITeacher teacher, OrientationType orientationType) throws ExcepcaoPersistencia;
    public List readAllByTeacher(ITeacher teacher) throws ExcepcaoPersistencia;
}
