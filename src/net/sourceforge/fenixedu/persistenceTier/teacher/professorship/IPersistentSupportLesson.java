/*
 * Created on Nov 23, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.teacher.professorship;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.ISupportLesson;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.util.DiaSemana;

/**
 * @author jpvl
 */
public interface IPersistentSupportLesson extends IPersistentObject {

    public List readByProfessorship(IProfessorship professorship) throws ExcepcaoPersistencia;

    /**
     * @param lesson
     * @return
     */
    public ISupportLesson readByUnique(ISupportLesson lesson) throws ExcepcaoPersistencia;

    /**
     * @param teacher
     * @param executionPeriod
     * @param weekDay
     * @param startTime
     * @param endTime
     * @return
     */
    public List readOverlappingPeriod(ITeacher teacher, IExecutionPeriod executionPeriod,
            DiaSemana weekDay, Date startTime, Date endTime) throws ExcepcaoPersistencia;

    /**
     * @param teacher
     * @param executionPeriod
     * @return
     */
    public List readByTeacherAndExecutionPeriod(ITeacher teacher, IExecutionPeriod executionPeriod)
            throws ExcepcaoPersistencia;

    public void delete(ISupportLesson supportLesson) throws ExcepcaoPersistencia;

    /**
     * @param executionPeriod
     * @return
     */
    public List readByExecutionPeriod(IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia;
}