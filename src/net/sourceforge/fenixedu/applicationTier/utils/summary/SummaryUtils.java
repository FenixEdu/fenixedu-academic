/*
 * Created on Oct 19, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.utils.summary;

import java.util.Calendar;
import java.util.ListIterator;

import net.sourceforge.fenixedu.dataTransferObject.InfoSummary;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.IShift;

/**
 * @author João Mota
 *  
 */
public class SummaryUtils {

    /**
     *  
     */
    public SummaryUtils() {
        super();
      
    }

    /**
     * @param lesson2
     * @param summaryDate
     * @param summaryHour
     * @return
     */
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

    /**
     * @param shift
     * @param infoSummary
     * @return
     */
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
}