/*
 * Created on Nov 24, 2003 by jpvl
 *  
 */
package ServidorPersistente.OJB.degree.finalProject;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IExecutionPeriod;
import Dominio.IStudent;
import Dominio.ITeacher;
import Dominio.degree.finalProject.ITeacherDegreeFinalProjectStudent;
import Dominio.degree.finalProject.TeacherDegreeFinalProjectStudent;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.PersistentObjectOJB;
import ServidorPersistente.degree.finalProject.IPersistentTeacherDegreeFinalProjectStudent;

/**
 * @author jpvl
 */
public class TeacherDegreeFinalProjectStudentOJB extends PersistentObjectOJB implements
        IPersistentTeacherDegreeFinalProjectStudent {

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.degree.finalProject.IPersistentTeacherDegreeFinalProjectStudent#readByTeacher(Dominio.ITeacher)
     */
    public List readByTeacherAndExecutionPeriod(ITeacher teacher, IExecutionPeriod executionPeriod)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("teacher.idInternal", teacher.getIdInternal());
        criteria.addEqualTo("executionPeriod.idInternal", executionPeriod.getIdInternal());
        return queryList(TeacherDegreeFinalProjectStudent.class, criteria);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.degree.finalProject.IPersistentTeacherDegreeFinalProjectStudent#readByUnique(Dominio.degree.finalProject.ITeacherDegreeFinalProjectStudent)
     */
    public ITeacherDegreeFinalProjectStudent readByUnique(
            ITeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudent)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("teacher.idInternal", teacherDegreeFinalProjectStudent.getTeacher()
                .getIdInternal());
        criteria.addEqualTo("executionPeriod.idInternal", teacherDegreeFinalProjectStudent
                .getExecutionPeriod().getIdInternal());
        criteria.addEqualTo("student.idInternal", teacherDegreeFinalProjectStudent.getStudent()
                .getIdInternal());
        return (ITeacherDegreeFinalProjectStudent) queryObject(TeacherDegreeFinalProjectStudent.class,
                criteria);

    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.degree.finalProject.IPersistentTeacherDegreeFinalProjectStudent#readByStudentAndExecutionPeriod(Dominio.IStudent,
     *      Dominio.IExecutionPeriod)
     */
    public List readByStudentAndExecutionPeriod(IStudent student, IExecutionPeriod executionPeriod)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("student.idInternal", student.getIdInternal());
        criteria.addEqualTo("executionPeriod.idInternal", executionPeriod.getIdInternal());
        return queryList(TeacherDegreeFinalProjectStudent.class, criteria);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.degree.finalProject.IPersistentTeacherDegreeFinalProjectStudent#readByExecutionPeriod(Dominio.IExecutionPeriod)
     */
    public List readByExecutionPeriod(IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("executionPeriod.idInternal", executionPeriod.getIdInternal());
        return queryList(TeacherDegreeFinalProjectStudent.class, criteria);
    }

}