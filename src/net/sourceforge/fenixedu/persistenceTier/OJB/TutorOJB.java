/*
 * Created on 2/Fev/2004
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.Teacher;
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
     * @see ServidorPersistente.IPersistentTutor#readTutorByTeacherAndStudent(Dominio.Teacher,
     *      Dominio.Student)
     */
    public Tutor readTutorByTeacherAndStudent(Teacher teacher, Student student)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("teacher.idInternal", teacher.getIdInternal());
        criteria.addEqualTo("student.idInternal", student.getIdInternal());

        return (Tutor) queryObject(Tutor.class, criteria);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentTutor#readTeachersByStudent(Dominio.Student)
     */
    public Tutor readTeachersByStudent(Student student) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        if (student != null && student.getIdInternal() != null) {
            criteria.addEqualTo("student.idInternal", student.getIdInternal());
        }
        if (student != null && student.getNumber() != null) {
            criteria.addEqualTo("student.number", student.getNumber());
        }
        return (Tutor) queryObject(Tutor.class, criteria);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentTutor#readStudentsByTeacher(Dominio.Teacher)
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