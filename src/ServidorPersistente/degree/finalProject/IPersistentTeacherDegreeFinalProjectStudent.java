/*
 * Created on Nov 24, 2003 by jpvl
 *  
 */
package ServidorPersistente.degree.finalProject;

import java.util.List;

import Dominio.IExecutionPeriod;
import Dominio.IStudent;
import Dominio.ITeacher;
import Dominio.degree.finalProject.ITeacherDegreeFinalProjectStudent;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;

/**
 * @author jpvl
 */
public interface IPersistentTeacherDegreeFinalProjectStudent extends IPersistentObject {

    List readByTeacherAndExecutionPeriod(ITeacher teacher, IExecutionPeriod executionPeriod)
            throws ExcepcaoPersistencia;

    ITeacherDegreeFinalProjectStudent readByUnique(
            ITeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudent)
            throws ExcepcaoPersistencia;

    /**
     * @param student
     * @param executionPeriod
     * @return
     */
    List readByStudentAndExecutionPeriod(IStudent student, IExecutionPeriod executionPeriod)
            throws ExcepcaoPersistencia;

    /**
     * @param executionPeriod
     * @return
     */
    List readByExecutionPeriod(IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia;

}