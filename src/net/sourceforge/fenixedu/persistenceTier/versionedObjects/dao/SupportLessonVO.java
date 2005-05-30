/*
 * Created on May 24, 2005
 *
 */
package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.ISupportLesson;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.teacher.professorship.IPersistentSupportLesson;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;
import net.sourceforge.fenixedu.util.DiaSemana;

/**
 * @author jdnf
 *  
 */
public class SupportLessonVO extends VersionedObjectsBase implements IPersistentSupportLesson {

    public List readByProfessorship(final Integer professorshipID) throws ExcepcaoPersistencia {
        final IProfessorship professorship = (IProfessorship) readByOID(Professorship.class,
                professorshipID);
        return (professorship != null) ? professorship.getSupportLessons() : new ArrayList();
    }

    public ISupportLesson readByUnique(final Integer professorshipID, final DiaSemana weekDay,
            final Date startTime, final Date endTime) throws ExcepcaoPersistencia {

        final IProfessorship professorship = (IProfessorship) readByOID(Professorship.class,
                professorshipID);
        if (professorship != null) {
            final List<ISupportLesson> supportLessons = professorship.getSupportLessons();
            for (final ISupportLesson supportLesson : supportLessons) {
                if (supportLesson.getWeekDay().equals(weekDay)
                        && supportLesson.getStartTime().equals(startTime)
                        && supportLesson.getEndTime().equals(endTime))
                    return supportLesson;
            }
        }
        return null;
    }

    public List readOverlappingPeriod(final Integer teacherID, final Integer executionPeriodID,
            final DiaSemana weekDay, final Date startTime, final Date endTime)
            throws ExcepcaoPersistencia {

        final List<ISupportLesson> result = new ArrayList<ISupportLesson>();

        final ITeacher teacher = (ITeacher) readByOID(Teacher.class, teacherID);
        if (teacher != null) {
            final List<IProfessorship> professorships = teacher.getProfessorships();
            for (final IProfessorship professorship : professorships) {
                if (professorship.getExecutionCourse().getExecutionPeriod().getIdInternal().equals(
                        executionPeriodID)) {
                    selectSupportLessons(professorship, weekDay, startTime, endTime, result);
                }
            }
        }
        return result;
    }

    private void selectSupportLessons(final IProfessorship professorship, final DiaSemana weekDay,
            final Date startTime, final Date endTime, List<ISupportLesson> result) {
        final List<ISupportLesson> supportLessons = professorship.getSupportLessons();
        for (final ISupportLesson supportLesson : supportLessons) {
            if (supportLesson.getWeekDay().equals(weekDay)
                    && (startCriteria(supportLesson, startTime, endTime)
                            || endCriteria(supportLesson, startTime, endTime) || equalCriteria(
                            supportLesson, startTime, endTime)))
                result.add(supportLesson);
        }
    }

    private boolean startCriteria(final ISupportLesson supportLesson, final Date startTime,
            final Date endTime) {
        return supportLesson.getStartTime().after(startTime)
                && supportLesson.getStartTime().before(endTime);
    }

    private boolean endCriteria(final ISupportLesson supportLesson, final Date startTime,
            final Date endTime) {
        return supportLesson.getEndTime().after(startTime)
                && supportLesson.getStartTime().before(endTime);
    }

    private boolean equalCriteria(final ISupportLesson supportLesson, final Date startTime,
            final Date endTime) {
        return supportLesson.getStartTime().equals(startTime)
                && supportLesson.getEndTime().equals(endTime);
    }

}
