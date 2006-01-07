package net.sourceforge.fenixedu.persistenceTier;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftProfessorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.util.DiaSemana;

public interface IPersistentShiftProfessorship extends IPersistentObject {
    ShiftProfessorship readByUnique(ShiftProfessorship teacherShiftPercentage)
            throws ExcepcaoPersistencia;

    ShiftProfessorship readByProfessorshipAndShift(Professorship professorship, Shift shift)
            throws ExcepcaoPersistencia;

    List readOverlappingPeriod(Integer teacherId, Integer executionPeriodId, DiaSemana weekDay,
            Date startTime, Date endTime) throws ExcepcaoPersistencia;

    List readByTeacherAndExecutionPeriodAndDegreeType(Teacher teacher,
            ExecutionPeriod executionPeriod, DegreeType curso) throws ExcepcaoPersistencia;

    List readByProfessorship(Professorship professorship) throws ExcepcaoPersistencia;

    List readByExecutionPeriod(ExecutionPeriod executionPeriod) throws ExcepcaoPersistencia;

    List readByTeacherAndExecutionPeriodWithDifferentIds(Teacher teacher, ExecutionPeriod period,
            List shiftProfessorShipsIds) throws ExcepcaoPersistencia;

    List readByTeacherAndExecutionPeriod(Teacher teacher, ExecutionPeriod period)
            throws ExcepcaoPersistencia;

    List readByShift(Shift shift) throws ExcepcaoPersistencia;
}