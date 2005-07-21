/*
 * Created on Oct 19, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.utils.summary;

import java.util.Calendar;
import java.util.Date;
import java.util.ListIterator;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoSummary;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.IRoom;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.ISummary;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Room;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISalaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;

/**
 * @author João Mota
 *  
 */
public class SummaryUtils {

    public static boolean verifyValidDateSummary(ILesson lesson, Calendar summaryDate, Calendar summaryHour) {
        Calendar dateAndHourSummary = Calendar.getInstance();
        dateAndHourSummary.set(Calendar.DAY_OF_MONTH, summaryDate.get(Calendar.DAY_OF_MONTH));
        dateAndHourSummary.set(Calendar.MONTH, summaryDate.get(Calendar.MONTH));
        dateAndHourSummary.set(Calendar.YEAR, summaryDate.get(Calendar.YEAR));
        dateAndHourSummary.set(Calendar.HOUR_OF_DAY, summaryHour.get(Calendar.HOUR_OF_DAY));
        dateAndHourSummary.set(Calendar.MINUTE, summaryHour.get(Calendar.MINUTE));
        dateAndHourSummary.set(Calendar.SECOND, 00);

        Calendar beginLesson = Calendar.getInstance();
        beginLesson.set(Calendar.DAY_OF_MONTH, summaryDate.get(Calendar.DAY_OF_MONTH));
        beginLesson.set(Calendar.MONTH, summaryDate.get(Calendar.MONTH));
        beginLesson.set(Calendar.YEAR, summaryDate.get(Calendar.YEAR));

        Calendar endLesson = Calendar.getInstance();
        endLesson.set(Calendar.DAY_OF_MONTH, summaryDate.get(Calendar.DAY_OF_MONTH));
        endLesson.set(Calendar.MONTH, summaryDate.get(Calendar.MONTH));
        endLesson.set(Calendar.YEAR, summaryDate.get(Calendar.YEAR));

        beginLesson.set(Calendar.HOUR_OF_DAY, lesson.getInicio().get(Calendar.HOUR_OF_DAY));
        beginLesson.set(Calendar.MINUTE, lesson.getInicio().get(Calendar.MINUTE));
        beginLesson.set(Calendar.SECOND, 00);

        endLesson.set(Calendar.HOUR_OF_DAY, lesson.getFim().get(Calendar.HOUR_OF_DAY));
        endLesson.set(Calendar.MINUTE, lesson.getFim().get(Calendar.MINUTE));
        endLesson.set(Calendar.SECOND, 00);

        return dateAndHourSummary.get(Calendar.DAY_OF_WEEK) == lesson.getDiaSemana().getDiaSemana()
                .intValue()
                && !beginLesson.after(dateAndHourSummary) && !endLesson.before(dateAndHourSummary);

    }

    public static ILesson findlesson(IShift shift, InfoSummary infoSummary) {
        if (shift.getAssociatedLessons() != null && shift.getAssociatedLessons().size() > 0) {
            ListIterator iterator = shift.getAssociatedLessons().listIterator();
            while (iterator.hasNext()) {
                ILesson lesson = (ILesson) iterator.next();
                if (lesson.getIdInternal().equals(infoSummary.getLessonIdSelected())) {
                    return lesson;
                }
            }
        }
        return null;
    }
    
    public static ITeacher getTeacher(final ISuportePersistente persistentSupport,
            final InfoSummary infoSummary) throws ExcepcaoPersistencia, FenixServiceException {
        if (infoSummary.getInfoTeacher() != null
                && infoSummary.getInfoTeacher().getTeacherNumber() != null) {
            final IPersistentTeacher persistentTeacher = persistentSupport.getIPersistentTeacher();
            final ITeacher teacher = persistentTeacher.readByNumber(infoSummary.getInfoTeacher()
                    .getTeacherNumber());
            if (teacher == null) {
                throw new FenixServiceException("error.summary.no.teacher");
            }
            return teacher;
        }
        return null;
    }

    public static IProfessorship getProfessorship(final ISuportePersistente persistentSupport,
            final InfoSummary infoSummary) throws FenixServiceException, ExcepcaoPersistencia {
        if (infoSummary.getInfoProfessorship() != null
                && infoSummary.getInfoProfessorship().getIdInternal() != null) {
            final IPersistentProfessorship persistentProfessorship = persistentSupport
                    .getIPersistentProfessorship();
            final IProfessorship professorship = (IProfessorship) persistentProfessorship.readByOID(
                    Professorship.class, infoSummary.getInfoProfessorship().getIdInternal());
            if (professorship == null) {
                throw new FenixServiceException("error.summary.no.teacher");
            }
            return professorship;
        }
        return null;
    }

    public static IShift getShift(final ISuportePersistente persistentSupport, final ISummary summary,
            final InfoSummary infoSummary) throws ExcepcaoPersistencia, FenixServiceException {
        
        IShift shift = null;
        if (summary != null && summary.getShift().getIdInternal().equals(infoSummary.getInfoShift().getIdInternal())) {
            shift = summary.getShift();
        } else {
            final ITurnoPersistente persistentShift = persistentSupport.getITurnoPersistente();
            shift = (IShift) persistentShift.readByOID(Shift.class, infoSummary.getInfoShift()
                    .getIdInternal());
        }
        if (shift == null) {
            throw new FenixServiceException("error.summary.no.shift");
        }
        return shift;
    }

    public static IRoom getRoom(final ISuportePersistente persistentSupport, final ISummary summary,
            final IShift shift, InfoSummary infoSummary) throws ExcepcaoPersistencia,
            FenixServiceException {
        IRoom room = null;
        if (infoSummary.getIsExtraLesson()) {
            if (summary != null && summary.getRoom().getIdInternal().equals(infoSummary.getInfoRoom().getIdInternal())) {
                room = summary.getRoom();
            } else {
                final ISalaPersistente persistentRoom = persistentSupport.getISalaPersistente();
                room = (IRoom) persistentRoom.readByOID(Room.class, infoSummary.getInfoRoom()
                        .getIdInternal());
            }
        } else {
            ILesson lesson = findlesson(shift, infoSummary);
            if (lesson == null) {
                throw new FenixServiceException("error.summary.no.shift");
            }
            if (!verifyValidDateSummary(lesson, infoSummary.getSummaryDate(), lesson
                    .getInicio())) {
                throw new FenixServiceException("error.summary.invalid.date");
            }
            infoSummary.setSummaryHour(lesson.getInicio());
        }
        if (room == null) {
            throw new FenixServiceException("error.summary.no.room");
        }
        return room;
    }
    
    
    public static Date createSummaryDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar.getTime();
    }

    public static Date createSummaryHour(int hour, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, hour);
        calendar.set(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }

}