package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Calendar;

import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.HourMinuteSecond;

/**
 * @author Ana e Ricardo
 * 
 *         19/7/2004
 */

public abstract class InfoShowOccupation extends InfoObject implements ISiteComponent {

    public abstract InfoShift getInfoShift();

    public abstract ShiftType getTipo();

    public abstract InfoRoomOccupation getInfoRoomOccupation();

    public abstract DiaSemana getDiaSemana();

    public abstract Calendar getInicio();

    public abstract Calendar getFim();

    public int getFirstHourOfDay() {
        final Calendar start = getInicio();
        return start.get(Calendar.HOUR_OF_DAY);
    }

    public int getLastHourOfDay() {
        final Calendar end = getFim();
        final int endHour = end.get(Calendar.HOUR_OF_DAY);
        return end.get(Calendar.MINUTE) > 0 ? endHour + 1 : endHour;
    }

    public HourMinuteSecond getBeginHourMinuteSecond() {
        final Calendar start = getInicio();
        return new HourMinuteSecond(start.get(Calendar.HOUR_OF_DAY), start.get(Calendar.MINUTE), start.get(Calendar.SECOND));
    }

    public HourMinuteSecond getEndHourMinuteSecond() {
        final Calendar end = getFim();
        return new HourMinuteSecond(end.get(Calendar.HOUR_OF_DAY), end.get(Calendar.MINUTE), end.get(Calendar.SECOND));
    }

    public AllocatableSpace getAllocatableSpace() {
        final InfoRoomOccupation infoOccupation = getInfoRoomOccupation();
        final InfoRoom infoRoom = infoOccupation == null ? null : infoOccupation.getInfoRoom();
        return infoRoom == null ? null : infoRoom.getRoom();
    }

}
