/*
 * Created on 9/Out/2003
 *
 */
package Dominio;

import java.util.Calendar;
import org.apache.commons.lang.time.DateFormatUtils;
import Util.DiaSemana;
import Util.CalendarUtil;

/**
 * @author Ana e Ricardo
 *  
 */
public class RoomOccupation extends DomainObject implements IRoomOccupation {
    protected Calendar startTime;

    protected Calendar endTime;

    protected DiaSemana dayOfWeek;

    protected ISala room;

    protected IPeriod period;

    // internal codes of the database
    private Integer keyRoom;

    private Integer keyPeriod;

    /**
     * Construtor
     */
    public RoomOccupation() {
    }

    public RoomOccupation(Integer idInternal) {
        setIdInternal(idInternal);
    }

    public RoomOccupation(ISala room, Calendar startTime, Calendar endTime,
            DiaSemana dayOfWeek) {
        this.room = room;
        this.startTime = startTime;
        this.endTime = endTime;
        this.dayOfWeek = dayOfWeek;
    }

    public boolean equals(Object obj) {
        if (obj instanceof IRoomOccupation) {
            IRoomOccupation roomOccupationObj = (IRoomOccupation) obj;
            if ((startTime.get(Calendar.HOUR_OF_DAY) == roomOccupationObj
                    .getStartTime().get(Calendar.HOUR_OF_DAY))
                    && (startTime.get(Calendar.MINUTE) == roomOccupationObj
                            .getStartTime().get(Calendar.MINUTE))
                    && (endTime.get(Calendar.HOUR_OF_DAY) == roomOccupationObj
                            .getEndTime().get(Calendar.HOUR_OF_DAY))
                    && (endTime.get(Calendar.MINUTE) == roomOccupationObj
                            .getEndTime().get(Calendar.MINUTE))
                    && room.equals(roomOccupationObj.getRoom())
                    && dayOfWeek.equals(roomOccupationObj.getDayOfWeek())
                    && period.equals(roomOccupationObj.getPeriod())) {
                return true;
            }
            return false;

        }
        return false;
    }

    public String toString() {
        String result = "[ROOM OCCUPATION";
        result += ", codInt=" + getIdInternal();
        result += ", startTime="
                + DateFormatUtils.format(startTime.getTime(), "HH:mm");
        result += ", endTime="
                + DateFormatUtils.format(endTime.getTime(), "HH:mm");
        result += ", dayOfWeek=" + dayOfWeek;
        result += ", periodId=" + period.getIdInternal();
        result += ", period="
                + DateFormatUtils.format(period.getStartDate().getTime(),
                        "yyyy-MM-dd");
        result += ", room=" + room.getNome();
        result += "]";
        return result;
    }

    /**
     * @return
     */
    public DiaSemana getDayOfWeek() {
        return dayOfWeek;
    }

    /**
     * @return
     */
    public Calendar getEndTime() {
        return endTime;
    }

    /**
     * @return
     */
    public Calendar getStartTime() {
        return startTime;
    }

    /**
     * @param semana
     */
    public void setDayOfWeek(DiaSemana semana) {
        dayOfWeek = semana;
    }

    /**
     * @param calendar
     */
    public void setEndTime(Calendar calendar) {
        endTime = calendar;
    }

    /**
     * @param calendar
     */
    public void setStartTime(Calendar calendar) {
        startTime = calendar;
    }

    /**
     * @return
     */
    public Integer getKeyRoom() {
        return keyRoom;
    }

    /**
     * @return
     */
    public ISala getRoom() {
        return room;
    }

    /**
     * @param integer
     */
    public void setKeyRoom(Integer integer) {
        keyRoom = integer;
    }

    /**
     * @param sala
     */
    public void setRoom(ISala sala) {
        room = sala;
    }

    /**
     * @return
     */
    public Integer getKeyPeriod() {
        return keyPeriod;
    }

    /**
     * @return
     */
    public IPeriod getPeriod() {
        return period;
    }

    /**
     * @param integer
     */
    public void setKeyPeriod(Integer integer) {
        keyPeriod = integer;
    }

    /**
     * @param period
     */
    public void setPeriod(IPeriod period) {
        this.period = period;
    }

    public boolean roomOccupationForDateAndTime(IRoomOccupation roomOccupation) {
        return roomOccupationForDateAndTime(roomOccupation.getPeriod()
                .getStartDate(), roomOccupation.getPeriod().getEndDate(),
                roomOccupation.getStartTime(), roomOccupation.getEndTime(),
                roomOccupation.getDayOfWeek(), roomOccupation.getRoom());
    }

    public boolean roomOccupationForDateAndTime(Calendar startDate,
            Calendar endDate, Calendar startTime, Calendar endTime,
            DiaSemana dayOfWeek, ISala room) {
        if (!room.equals(this.getRoom())) {
            return false;
        }
        if (CalendarUtil.intersectDates(this.getPeriod().getStartDate(), this
                .getPeriod().getEndDate(), startDate, endDate)) {
            if (dayOfWeek.equals(this.dayOfWeek)) {
                if (CalendarUtil.intersectTimes(this.getStartTime(), this
                        .getEndTime(), startTime, endTime)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean roomOccupationForDateAndTime(Calendar startDate,
            Calendar endDate, Calendar startTime, Calendar endTime,
            DiaSemana dayOfWeek) {
        if (CalendarUtil.intersectDates(this.getPeriod().getStartDate(), this
                .getPeriod().getEndDate(), startDate, endDate)) {
            if (dayOfWeek.equals(this.dayOfWeek)) {
                if (CalendarUtil.intersectTimes(this.getStartTime(), this
                        .getEndTime(), startTime, endTime)) {
                    return true;
                }
            }
        }
        return false;
    }
}