/*
 * Created on May 24, 2005
 *
 */
package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.SupportLesson;
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
        final Professorship professorship = (Professorship) readByOID(Professorship.class,
                professorshipID);
        return (professorship != null) ? professorship.getSupportLessons() : new ArrayList();
    }

    public SupportLesson readByUnique(final Integer professorshipID, final DiaSemana weekDay,
            final Date startTime, final Date endTime) throws ExcepcaoPersistencia {

        final Professorship professorship = (Professorship) readByOID(Professorship.class,
                professorshipID);
        if (professorship != null) {
            final List<SupportLesson> supportLessons = professorship.getSupportLessons();
            for (final SupportLesson supportLesson : supportLessons) {
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

        final List<SupportLesson> result = new ArrayList<SupportLesson>();

        final Teacher teacher = (Teacher) readByOID(Teacher.class, teacherID);
        if (teacher != null) {
            final List<Professorship> professorships = teacher.getProfessorships();
            for (final Professorship professorship : professorships) {
                if (professorship.getExecutionCourse().getExecutionPeriod().getIdInternal().equals(
                        executionPeriodID)) {
                    selectSupportLessons(professorship, weekDay, startTime, endTime, result);
                }
            }
        }
        return result;
    }

    private void selectSupportLessons(final Professorship professorship, final DiaSemana weekDay,
            final Date startTime, final Date endTime, List<SupportLesson> result) {
        final List<SupportLesson> supportLessons = professorship.getSupportLessons();
        for (final SupportLesson supportLesson : supportLessons) {
            if (supportLesson.getWeekDay().equals(weekDay)
                    && (startCriteria(supportLesson, startTime, endTime)
                            || endCriteria(supportLesson, startTime, endTime) || equalCriteria(
                            supportLesson, startTime, endTime)))
                result.add(supportLesson);
        }
    }

    private boolean startCriteria(final SupportLesson supportLesson, final Date startTime,
            final Date endTime) {
        return supportLesson.getStartTime().after(startTime)
                && supportLesson.getStartTime().before(endTime);
    }

    private boolean endCriteria(final SupportLesson supportLesson, final Date startTime,
            final Date endTime) {
        return supportLesson.getEndTime().after(startTime)
                && supportLesson.getStartTime().before(endTime);
    }

    private boolean equalCriteria(final SupportLesson supportLesson, final Date startTime,
            final Date endTime) {
        return supportLesson.getStartTime().equals(startTime)
                && supportLesson.getEndTime().equals(endTime);
    }

}
