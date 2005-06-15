/*
 * Created on Dec 3, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.credits.validator;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.IShiftProfessorship;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentShiftProfessorship;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.teacher.professorship.IPersistentSupportLesson;
import net.sourceforge.fenixedu.persistenceTier.teacher.workingTime.IPersistentTeacherInstitutionWorkingTime;
import net.sourceforge.fenixedu.util.DiaSemana;

/**
 * @author jpvl
 */
public abstract class CreditsValidator {

    public static void validatePeriod(Integer teacherId, Integer executionPeriodId,
            DiaSemana weekDay, Date startTime, Date endTime, PeriodType periodType)
            throws OverlappingPeriodException, FenixServiceException {
        ISuportePersistente sp = null;
        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            switch (periodType.getValue()) {
            case PeriodType.INSTITUTION_WORKING_TIME_PERIOD_TYPE:
                verifyOverlappingSupportLesson(teacherId, executionPeriodId, weekDay, startTime, endTime, sp);
                verifyOverlappingLesson(teacherId, executionPeriodId, weekDay, startTime, endTime, sp);
                break;
            case PeriodType.LESSON_PERIOD_TYPE:
                verifyOverlappingSupportLesson(teacherId, executionPeriodId, weekDay, startTime, endTime, sp);
                verifyOverlappingInstitutionWorkingTime(teacherId, executionPeriodId, weekDay, startTime,
                        endTime, sp);
                break;
            case PeriodType.SUPPORT_LESSON_PERIOD_TYPE:
                verifyOverlappingInstitutionWorkingTime(teacherId, executionPeriodId, weekDay, startTime,
                        endTime, sp);
                verifyOverlappingLesson(teacherId, executionPeriodId, weekDay, startTime, endTime, sp);
                break;
            }
        } catch (ExcepcaoPersistencia e1) {
            throw new FenixServiceException("Problems on database!", e1);
        }
    }

    /**
     * @param teacher
     * @param weekDay
     * @param startTime
     * @param endTime
     */
    private static void verifyOverlappingInstitutionWorkingTime(Integer teacherId,
            Integer executionPeriodId, DiaSemana weekDay, Date startTime, Date endTime,
            ISuportePersistente sp) throws OverlappingInstitutionWorkingPeriod, ExcepcaoPersistencia {
        IPersistentTeacherInstitutionWorkingTime teacherInstitutionWorkingTimeDAO = sp
                .getIPersistentTeacherInstitutionWorkingTime();

        List list = teacherInstitutionWorkingTimeDAO.readOverlappingPeriod(teacherId, executionPeriodId,
                weekDay, startTime, endTime);
        if (!list.isEmpty()) {
            throw new OverlappingInstitutionWorkingPeriod();
        }
    }

    private static void verifyOverlappingSupportLesson(Integer teacherId,
            Integer executionPeriodId, DiaSemana weekDay, Date startTime, Date endTime,
            ISuportePersistente sp) throws ExcepcaoPersistencia, OverlappingSupportLessonPeriod {
        IPersistentSupportLesson supportLessonDAO = sp.getIPersistentSupportLesson();

        List list = supportLessonDAO.readOverlappingPeriod(teacherId, executionPeriodId, weekDay, startTime,
                endTime);
        if (!list.isEmpty()) {
            throw new OverlappingSupportLessonPeriod();
        }
    }

    /**
     * @param teacher
     * @param weekDay
     * @param startTime
     * @param endTime
     */
    private static void verifyOverlappingLesson(Integer teacherId, Integer executionPeriodId,
            DiaSemana weekDay, Date startTime, Date endTime, ISuportePersistente sp)
            throws OverlappingLessonPeriod, ExcepcaoPersistencia {
        IPersistentShiftProfessorship shiftProfessorshipDAO = sp.getIPersistentShiftProfessorship();

        List list = shiftProfessorshipDAO.readOverlappingPeriod(teacherId, executionPeriodId, weekDay,
                startTime, endTime);

        if (!list.isEmpty()) {
            throw new OverlappingLessonPeriod();
        }

    }

    /**
     * @param teacher
     * @param period
     * @param shift
     */
    public static void validatePeriod(Integer teacherId, Integer executionPeriodId,
            IShiftProfessorship shiftProfessorship) throws OverlappingPeriodException,
            FenixServiceException {
        if (shiftProfessorship.getPercentage().doubleValue() == 100) {
            Iterator iterator = shiftProfessorship.getShift().getAssociatedLessons().iterator();
            while (iterator.hasNext()) {
                ILesson lesson = (ILesson) iterator.next();
                validatePeriod(teacherId, executionPeriodId, lesson.getDiaSemana(), new Date(lesson.getInicio()
                        .getTimeInMillis()), new Date(lesson.getFim().getTimeInMillis()),
                        PeriodType.LESSON_PERIOD);
            }
        }

    }

}