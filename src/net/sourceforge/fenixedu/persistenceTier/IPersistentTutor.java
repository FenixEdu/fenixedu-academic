/*
 * Created on 2/Fev/2004
 *
 */
package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.ITutor;

/**
 * @author Tânia Pousão
 *  
 */
public interface IPersistentTutor extends IPersistentObject {
    public ITutor readTutorByTeacherAndStudent(ITeacher teacher, IStudent student)
            throws ExcepcaoPersistencia;

    public ITutor readTeachersByStudent(IStudent student) throws ExcepcaoPersistencia;

    public List readStudentsByTeacher(ITeacher teacher) throws ExcepcaoPersistencia;
}