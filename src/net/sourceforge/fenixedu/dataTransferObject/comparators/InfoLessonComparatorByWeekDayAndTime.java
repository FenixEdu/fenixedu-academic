/*
 * Created on Mar 14, 2003
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.comparators;

import java.util.Calendar;
import java.util.Comparator;

import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;

/**
 * @author Luis Cruz e Sara Ribeiro
 *  
 */
public class InfoLessonComparatorByWeekDayAndTime implements Comparator {

    public int compare(Object obj1, Object obj2) {
        InfoLesson infoLesson1 = (InfoLesson) obj1;
        InfoLesson infoLesson2 = (InfoLesson) obj2;

        int startHourLesson1 = infoLesson1.getInicio().get(Calendar.HOUR_OF_DAY);
        int startMinuteLesson1 = infoLesson1.getInicio().get(Calendar.MINUTE);
        int startHourLesson2 = infoLesson2.getInicio().get(Calendar.HOUR_OF_DAY);
        int startMinuteLesson2 = infoLesson2.getInicio().get(Calendar.MINUTE);
        int endHourLesson1 = infoLesson1.getFim().get(Calendar.HOUR_OF_DAY);
        int endMinuteLesson1 = infoLesson1.getFim().get(Calendar.MINUTE);
        int endHourLesson2 = infoLesson2.getFim().get(Calendar.HOUR_OF_DAY);
        int endMinuteLesson2 = infoLesson2.getFim().get(Calendar.MINUTE);

        String timeValueLesson1 = "" + infoLesson1.getDiaSemana().getDiaSemana()
                + (startHourLesson1 < 10 ? "0" + startHourLesson1 : "" + startHourLesson1)
                + (startMinuteLesson1 < 10 ? "0" + startMinuteLesson1 : "" + startMinuteLesson1)
                + (endHourLesson1 < 10 ? "0" + endHourLesson1 : "" + endHourLesson1)
                + (endMinuteLesson1 < 10 ? "0" + endMinuteLesson1 : "" + endMinuteLesson1);
        String timeValueLesson2 = "" + infoLesson2.getDiaSemana().getDiaSemana()
                + (startHourLesson2 < 10 ? "0" + startHourLesson2 : "" + startHourLesson2)
                + (startMinuteLesson2 < 10 ? "0" + startMinuteLesson2 : "" + startMinuteLesson2)
                + (endHourLesson2 < 10 ? "0" + endHourLesson2 : "" + endHourLesson2)
                + (endMinuteLesson2 < 10 ? "0" + endMinuteLesson2 : "" + endMinuteLesson2);
        return ((new Integer(timeValueLesson1)).intValue() - (new Integer(timeValueLesson2)).intValue());
    }

}