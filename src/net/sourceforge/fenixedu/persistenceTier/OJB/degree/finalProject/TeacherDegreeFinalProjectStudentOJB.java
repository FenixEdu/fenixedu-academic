/*
 * Created on Nov 24, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.degree.finalProject;

import java.util.List;

import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.degree.finalProject.ITeacherDegreeFinalProjectStudent;
import net.sourceforge.fenixedu.domain.degree.finalProject.TeacherDegreeFinalProjectStudent;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.degree.finalProject.IPersistentTeacherDegreeFinalProjectStudent;

import org.apache.ojb.broker.query.Criteria;

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