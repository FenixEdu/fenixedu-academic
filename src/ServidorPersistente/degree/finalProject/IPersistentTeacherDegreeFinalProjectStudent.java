/*
 * Created on Nov 24, 2003 by jpvl
 *  
 */
package ServidorPersistente.degree.finalProject;

import java.util.List;

import Dominio.IExecutionYear;
import Dominio.IStudent;
import Dominio.ITeacher;
import Dominio.degree.finalProject.ITeacherDegreeFinalProjectStudent;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;

/**
 * @author jpvl
 */
public interface IPersistentTeacherDegreeFinalProjectStudent extends IPersistentObject
{

    List readByTeacherAndExecutionYear(ITeacher teacher, IExecutionYear executionYear)
            throws ExcepcaoPersistencia;

    ITeacherDegreeFinalProjectStudent readByUnique(
            ITeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudent) throws ExcepcaoPersistencia;

    List readByStudent(IStudent student) throws ExcepcaoPersistencia;

}