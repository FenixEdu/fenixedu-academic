package net.sourceforge.fenixedu.persistenceTier.degree.finalProject;

import java.util.List;

import net.sourceforge.fenixedu.domain.degree.finalProject.ITeacherDegreeFinalProjectStudent;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author jpvl
 */
public interface IPersistentTeacherDegreeFinalProjectStudent extends IPersistentObject {

    List readByTeacherAndExecutionPeriod(Integer teacherId, Integer executionPeriodId)
            throws ExcepcaoPersistencia;

    ITeacherDegreeFinalProjectStudent readByUnique(Integer teacherId, Integer executionPeriodId,
            Integer studentId) throws ExcepcaoPersistencia;

    List readByStudentAndExecutionPeriod(Integer studentId, Integer executionPeriodId)
            throws ExcepcaoPersistencia;

}