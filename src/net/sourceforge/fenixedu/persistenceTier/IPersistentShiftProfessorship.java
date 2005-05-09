/*
 * Created on 19/Mai/2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.persistenceTier;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IShiftProfessorship;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.util.DiaSemana;

/**
 * @author jpvl
 */
public interface IPersistentShiftProfessorship extends IPersistentObject {
    IShiftProfessorship readByUnique(IShiftProfessorship teacherShiftPercentage)
            throws ExcepcaoPersistencia;

    void delete(IShiftProfessorship teacherShiftPercentage) throws ExcepcaoPersistencia;

    IShiftProfessorship readByProfessorshipAndShift(IProfessorship professorship, IShift shift)
            throws ExcepcaoPersistencia;

    List readOverlappingPeriod(ITeacher teacher, IExecutionPeriod executionPeriod, DiaSemana weekDay,
            Date startTime, Date endTime) throws ExcepcaoPersistencia;

    List readByTeacherAndExecutionPeriodAndDegreeType(ITeacher teacher,
            IExecutionPeriod executionPeriod, DegreeType curso) throws ExcepcaoPersistencia;

    List readByProfessorship(IProfessorship professorship) throws ExcepcaoPersistencia;

    /**
     * @param executionPeriod
     * @return
     */
    List readByExecutionPeriod(IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia;

    List readByTeacherAndExecutionPeriodWithDifferentIds(ITeacher teacher, IExecutionPeriod period,
            List shiftProfessorShipsIds) throws ExcepcaoPersistencia;

    List readByTeacherAndExecutionPeriod(ITeacher teacher, IExecutionPeriod period)
            throws ExcepcaoPersistencia;

    List readByShift(IShift shift) throws ExcepcaoPersistencia;
}