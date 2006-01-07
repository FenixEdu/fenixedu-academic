/*
 * Created on 2/Fev/2004
 *
 */
package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.Tutor;

/**
 * @author Tânia Pousão
 *  
 */
public interface IPersistentTutor extends IPersistentObject {
    public Tutor readTutorByTeacherAndStudent(Teacher teacher, Student student)
            throws ExcepcaoPersistencia;

    public Tutor readTeachersByStudent(Student student) throws ExcepcaoPersistencia;

    public List readStudentsByTeacher(Integer teacherId, Integer teacherNumber) throws ExcepcaoPersistencia;
}