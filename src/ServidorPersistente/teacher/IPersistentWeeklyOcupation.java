/*
 * Created on 15/Nov/2003
 *
 */
package ServidorPersistente.teacher;

import Dominio.ITeacher;
import Dominio.teacher.IWeeklyOcupation;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public interface IPersistentWeeklyOcupation extends IPersistentObject {
    public IWeeklyOcupation readByTeacher(ITeacher teacher) throws ExcepcaoPersistencia;
}