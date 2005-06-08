package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.degree.finalProject;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.degree.finalProject.ITeacherDegreeFinalProjectStudent;
import net.sourceforge.fenixedu.domain.degree.finalProject.TeacherDegreeFinalProjectStudent;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.degree.finalProject.IPersistentTeacherDegreeFinalProjectStudent;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author lmre
 */
public class TeacherDegreeFinalProjectStudentVO extends VersionedObjectsBase implements
        IPersistentTeacherDegreeFinalProjectStudent {

    public List readByTeacherAndExecutionPeriod(Integer teacherId, Integer executionPeriodId)
            throws ExcepcaoPersistencia {

        List<ITeacherDegreeFinalProjectStudent> tdfpStudents = (List<ITeacherDegreeFinalProjectStudent>) readAll(TeacherDegreeFinalProjectStudent.class);
        List<ITeacherDegreeFinalProjectStudent> result = new ArrayList();

        for (ITeacherDegreeFinalProjectStudent student : tdfpStudents) {
            if (student.getTeacher().getIdInternal().equals(teacherId)
                    && student.getExecutionPeriod().getIdInternal().equals(executionPeriodId)) {
                result.add(student);
            }
        }

        return result;
    }

    public ITeacherDegreeFinalProjectStudent readByUnique(Integer teacherId, Integer executionPeriodId,
            Integer studentId) throws ExcepcaoPersistencia {

        List<ITeacherDegreeFinalProjectStudent> tdfpStudents = (List<ITeacherDegreeFinalProjectStudent>) readAll(TeacherDegreeFinalProjectStudent.class);

        for (ITeacherDegreeFinalProjectStudent student : tdfpStudents) {
            if (student.getTeacher().getIdInternal().equals(teacherId)
                    && student.getExecutionPeriod().getIdInternal().equals(executionPeriodId)
                    && student.getStudent().getIdInternal().equals(studentId)) {
                return student;
            }
        }

        return null;
    }

    public List readByStudentAndExecutionPeriod(Integer studentId, Integer executionPeriodId)
            throws ExcepcaoPersistencia {

        List<ITeacherDegreeFinalProjectStudent> tdfpStudents = (List<ITeacherDegreeFinalProjectStudent>) readAll(TeacherDegreeFinalProjectStudent.class);
        List<ITeacherDegreeFinalProjectStudent> result = new ArrayList();

        for (ITeacherDegreeFinalProjectStudent student : tdfpStudents) {
            if (student.getStudent().getIdInternal().equals(studentId)
                    && student.getExecutionPeriod().getIdInternal().equals(executionPeriodId)) {
                result.add(student);
            }
        }

        return result;
    }

}
