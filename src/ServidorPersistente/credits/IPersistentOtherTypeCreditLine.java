/*
 * Created on 29/Fev/2004
 */
package ServidorPersistente.credits;

import java.util.List;

import Dominio.IExecutionPeriod;
import Dominio.ITeacher;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;

/**
 * @author jpvl
 */
public interface IPersistentOtherTypeCreditLine extends IPersistentObject
{
    List readByTeacherAndExecutionPeriod(ITeacher teacher, IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia;
}
