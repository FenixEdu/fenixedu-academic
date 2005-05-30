/*
 * Created on Nov 23, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.teacher.professorship;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.ISupportLesson;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.util.DiaSemana;

/**
 * @author jpvl
 */
public interface IPersistentSupportLesson extends IPersistentObject {

    public List readByProfessorship(Integer professorshipID) throws ExcepcaoPersistencia;

    public ISupportLesson readByUnique(Integer professorshipID, DiaSemana weekDay, Date startTime,
            Date endTime) throws ExcepcaoPersistencia;

    public List readOverlappingPeriod(Integer teacherID, Integer executionPeriodID,
            DiaSemana weekDay, Date startTime, Date endTime) throws ExcepcaoPersistencia;
}