/*
 * Created on 21/Nov/2003
 *
 */
package ServidorPersistente.teacher;

import java.util.List;

import DataBeans.teacher.IPublicationsNumber;
import Dominio.ITeacher;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import Util.PublicationType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *
 */
public interface IPersistentPublicationsNumber extends IPersistentObject
{
    public IPublicationsNumber readByTeacherAndPublicationType(ITeacher teacher, PublicationType publicationType) throws ExcepcaoPersistencia;
    public List readAllByTeacher(ITeacher teacher) throws ExcepcaoPersistencia;
}
