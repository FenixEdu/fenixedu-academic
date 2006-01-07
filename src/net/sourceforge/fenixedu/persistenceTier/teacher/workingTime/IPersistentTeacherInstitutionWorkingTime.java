/*
 * Created on Nov 25, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.teacher.workingTime;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.workTime.TeacherInstitutionWorkTime;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.util.DiaSemana;

/**
 * @author jpvl
 */
public interface IPersistentTeacherInstitutionWorkingTime extends IPersistentObject {

    TeacherInstitutionWorkTime readByUnique(TeacherInstitutionWorkTime teacherInstitutionWorkTime)
            throws ExcepcaoPersistencia;

    List readByTeacherAndExecutionPeriod(Teacher teacher, ExecutionPeriod executionPeriod)
            throws ExcepcaoPersistencia;

    /**
     * @param teacher
     * @param executionPeriod
     * @param weekDay
     * @param startTime
     * @param endTime
     */
    List readOverlappingPeriod(Integer teacherId, Integer executionPeriodId, DiaSemana weekDay,
            Date startTime, Date endTime) throws ExcepcaoPersistencia;

    /**
     * @param executionPeriod
     * @return
     */
    List readByExecutionPeriod(ExecutionPeriod executionPeriod) throws ExcepcaoPersistencia;

}