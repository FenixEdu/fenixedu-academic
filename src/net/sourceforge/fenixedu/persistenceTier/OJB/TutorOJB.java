/*
 * Created on 2/Fev/2004
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.ITutor;
import net.sourceforge.fenixedu.domain.Tutor;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTutor;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author Tânia Pousão
 *  
 */
public class TutorOJB extends PersistentObjectOJB implements IPersistentTutor {
    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentTutor#readTutorByTeacherAndStudent(Dominio.ITeacher,
     *      Dominio.IStudent)
     */
    public ITutor readTutorByTeacherAndStudent(ITeacher teacher, IStudent student)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("teacher.idInternal", teacher.getIdInternal());
        criteria.addEqualTo("student.idInternal", student.getIdInternal());

        return (ITutor) queryObject(Tutor.class, criteria);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentTutor#readTeachersByStudent(Dominio.IStudent)
     */
    public ITutor readTeachersByStudent(IStudent student) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        if (student != null && student.getIdInternal() != null) {
            criteria.addEqualTo("student.idInternal", student.getIdInternal());
        }
        if (student != null && student.getNumber() != null) {
            criteria.addEqualTo("student.number", student.getNumber());
        }
        return (ITutor) queryObject(Tutor.class, criteria);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentTutor#readStudentsByTeacher(Dominio.ITeacher)
     */
    public List readStudentsByTeacher(Integer teacherId, Integer teacherNumber) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        if (teacherId != null) {
            criteria.addEqualTo("teacher.idInternal", teacherId);
        }
        if (teacherNumber != null) {
            criteria.addEqualTo("teacher.teacherNumber", teacherNumber);
        }
        return queryList(Tutor.class, criteria);
    }
}