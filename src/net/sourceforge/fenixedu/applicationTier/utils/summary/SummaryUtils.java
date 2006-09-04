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
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.Summary;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Jo�o Mota
 * 
 */
public class SummaryUtils {

    public static boolean verifyValidDateSummary(Lesson lesson, Calendar summaryDate, Calendar summaryHour) {
        
        Calendar dateAndHourSummary = Calendar.getInstance();
        dateAndHourSummary.set(Calendar.DAY_OF_MONTH, summaryDate.get(Calendar.DAY_OF_MONTH));
        dateAndHourSummary.set(Calendar.MONTH, summaryDate.get(Calendar.MONTH));
        dateAndHourSummary.set(Calendar.YEAR, summaryDate.get(Calendar.YEAR));
        dateAndHourSummary.set(Calendar.HOUR_OF_DAY, summaryHour.get(Calendar.HOUR_OF_DAY));
        dateAndHourSummary.set(Calendar.MINUTE, summaryHour.get(Calendar.MINUTE));
        dateAndHourSummary.set(Calendar.SECOND, 00);
        dateAndHourSummary.set(Calendar.MILLISECOND, 00);

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
        beginLesson.set(Calendar.MILLISECOND, 00);

        endLesson.set(Calendar.HOUR_OF_DAY, lesson.getFim().get(Calendar.HOUR_OF_DAY));
        endLesson.set(Calendar.MINUTE, lesson.getFim().get(Calendar.MINUTE));
        endLesson.set(Calendar.SECOND, 00);
        endLesson.set(Calendar.MILLISECOND, 00);

        return dateAndHourSummary.get(Calendar.DAY_OF_WEEK) == lesson.getDiaSemana().getDiaSemana()
                .intValue()
                && !beginLesson.after(dateAndHourSummary) && !endLesson.before(dateAndHourSummary);

    }

    public static Lesson findlesson(Shift shift, InfoSummary infoSummary) {
        if (shift.getAssociatedLessons() != null && shift.getAssociatedLessons().size() > 0) {
            ListIterator iterator = shift.getAssociatedLessons().listIterator();
            while (iterator.hasNext()) {
                Lesson lesson = (Lesson) iterator.next();
                if (lesson.getIdInternal().equals(infoSummary.getLessonIdSelected())) {
                    return lesson;
                }
            }
        }
        return null;
    }

    public static Teacher getTeacher(final InfoSummary infoSummary) throws ExcepcaoPersistencia,
            FenixServiceException {
        if (infoSummary.getInfoTeacher() != null
                && infoSummary.getInfoTeacher().getTeacherNumber() != null) {
            final Teacher teacher = Teacher
                    .readByNumber(infoSummary.getInfoTeacher().getTeacherNumber());
            if (teacher == null) {
                throw new FenixServiceException("error.summary.no.teacher");
            }
            return teacher;
        }
        return null;
    }

    public static Professorship getProfessorship(final InfoSummary infoSummary)
            throws FenixServiceException, ExcepcaoPersistencia {
        if (infoSummary.getInfoProfessorship() != null
                && infoSummary.getInfoProfessorship().getIdInternal() != null) {
            final Professorship professorship = RootDomainObject.getInstance().readProfessorshipByOID(
                    infoSummary.getInfoProfessorship().getIdInternal());
            if (professorship == null) {
                throw new FenixServiceException("error.summary.no.teacher");
            }
            return professorship;
        }
        return null;
    }

    public static Shift getShift(final Summary summary, final InfoSummary infoSummary)
            throws ExcepcaoPersistencia, FenixServiceException {

        Shift shift = null;
        if (summary != null
                && summary.getShift().getIdInternal().equals(infoSummary.getInfoShift().getIdInternal())) {
            shift = summary.getShift();
        } else {
            shift = RootDomainObject.getInstance().readShiftByOID(
                    infoSummary.getInfoShift().getIdInternal());
        }
        if (shift == null) {
            throw new FenixServiceException("error.summary.no.shift");
        }
        return shift;
    }

    public static OldRoom getRoom(final Summary summary, final Shift shift, InfoSummary infoSummary)
            throws ExcepcaoPersistencia, FenixServiceException {
        OldRoom room = null;
        if (infoSummary.getIsExtraLesson()) {
            if (summary != null
                    && summary.getRoom().getIdInternal().equals(
                            infoSummary.getInfoRoom().getIdInternal())) {
                room = summary.getRoom();
            } else {
                room = (OldRoom) RootDomainObject.getInstance().readSpaceByOID(
                        infoSummary.getInfoRoom().getIdInternal());
            }
        } else {
            Lesson lesson = findlesson(shift, infoSummary);
            if (lesson == null) {
                throw new FenixServiceException("error.summary.no.shift");
            }
            if (!verifyValidDateSummary(lesson, infoSummary.getSummaryDate(), lesson.getInicio())) {
                throw new FenixServiceException("error.summary.invalid.date");
            }
            room = lesson.getRoomOccupation().getRoom();
            infoSummary.setSummaryHour(lesson.getInicio());
        }
        if (room == null) {
            throw new FenixServiceException("error.summary.no.room");
        }
        return room;
    }

    public static Lesson getSummaryLesson(Summary summary) {
        if (summary.getIsExtraLesson() != null && summary.getIsExtraLesson().equals(Boolean.FALSE)) {
            if (summary.getSummaryDate() != null && summary.getSummaryHour() != null) {

                Calendar dateAndHourSummary = Calendar.getInstance();
                Calendar summaryDate = Calendar.getInstance();
                summaryDate.setTime(summary.getSummaryDate());
                dateAndHourSummary.set(Calendar.DAY_OF_MONTH, summaryDate.get(Calendar.DAY_OF_MONTH));
                dateAndHourSummary.set(Calendar.MONTH, summaryDate.get(Calendar.MONTH));
                dateAndHourSummary.set(Calendar.YEAR, summaryDate.get(Calendar.YEAR));

                Calendar summaryHour = Calendar.getInstance();
                summaryHour.setTime(summary.getSummaryHour());
                dateAndHourSummary.set(Calendar.HOUR_OF_DAY, summaryHour.get(Calendar.HOUR_OF_DAY));
                dateAndHourSummary.set(Calendar.MINUTE, summaryHour.get(Calendar.MINUTE));
                dateAndHourSummary.set(Calendar.SECOND, 00);
                dateAndHourSummary.set(Calendar.MILLISECOND, 00);

                Calendar beginLesson = Calendar.getInstance();
                beginLesson.set(Calendar.DAY_OF_MONTH, summaryDate.get(Calendar.DAY_OF_MONTH));
                beginLesson.set(Calendar.MONTH, summaryDate.get(Calendar.MONTH));
                beginLesson.set(Calendar.YEAR, summaryDate.get(Calendar.YEAR));

                Calendar endLesson = Calendar.getInstance();
                endLesson.set(Calendar.DAY_OF_MONTH, summaryDate.get(Calendar.DAY_OF_MONTH));
                endLesson.set(Calendar.MONTH, summaryDate.get(Calendar.MONTH));
                endLesson.set(Calendar.YEAR, summaryDate.get(Calendar.YEAR));

                for (Lesson lesson : summary.getShift().getAssociatedLessons()) {
                    beginLesson.set(Calendar.HOUR_OF_DAY, lesson.getInicio().get(Calendar.HOUR_OF_DAY));
                    beginLesson.set(Calendar.MINUTE, lesson.getInicio().get(Calendar.MINUTE));
                    beginLesson.set(Calendar.SECOND, 00);
                    beginLesson.set(Calendar.MILLISECOND, 00);

                    endLesson.set(Calendar.HOUR_OF_DAY, lesson.getFim().get(Calendar.HOUR_OF_DAY));
                    endLesson.set(Calendar.MINUTE, lesson.getFim().get(Calendar.MINUTE));
                    endLesson.set(Calendar.SECOND, 00);
                    endLesson.set(Calendar.MILLISECOND, 00);

                    if (summary.getShift().getTipo().equals(lesson.getTipo())
                            && dateAndHourSummary.get(Calendar.DAY_OF_WEEK) == lesson.getDiaSemana()
                                    .getDiaSemana().intValue() && !beginLesson.after(dateAndHourSummary)
                            && !endLesson.before(dateAndHourSummary)) {
                        return lesson;
                    }
                }
            }
        }

        return null;// extra lesson
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