/*
 * Created on 7/Mar/2004
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
public interface IPersistentManagementPositionCreditLine extends IPersistentObject {
    /**
     * @param teacher
     * @param executionPeriod
     */
    List readByTeacherAndExecutionPeriod(ITeacher teacher, IExecutionPeriod executionPeriod)
            throws ExcepcaoPersistencia;

    List readByTeacher(ITeacher teacher) throws ExcepcaoPersistencia;

}