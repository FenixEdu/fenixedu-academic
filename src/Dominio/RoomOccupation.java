/*
 * Created on 9/Out/2003
 *
 */
package Dominio;

import java.util.ArrayList;
import java.util.Calendar;
import org.apache.commons.lang.time.DateFormatUtils;
import Util.DiaSemana;
import Util.CalendarUtil;

/**
 * @author Ana e Ricardo
 *  
 */
public class RoomOccupation extends DomainObject implements IRoomOccupation {

    public static final int DIARIA = 1;

    public static final int SEMANAL = 2;

    public static final int QUINZENAL = 3;

    protected Calendar startTime;

    protected Calendar endTime;

    protected DiaSemana dayOfWeek;

    protected ISala room;

    protected IPeriod period;

    protected Integer frequency;

    //weekOfQuinzenalStart should always be 1 or 2
    protected Integer weekOfQuinzenalStart;

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

    public RoomOccupation(ISala room, Calendar startTime, Calendar endTime, DiaSemana dayOfWeek,
            int frequency) {
        this.room = room;
        this.startTime = startTime;
        this.endTime = endTime;
        this.dayOfWeek = dayOfWeek;
        this.frequency = new Integer(frequency);
    }

    public boolean equals(Object obj) {
        if (obj instanceof IRoomOccupation) {
            IRoomOccupation roomOccupationObj = (IRoomOccupation) obj;            
            if ((startTime.get(Calendar.HOUR_OF_DAY) == roomOccupationObj.getStartTime().get(
                    Calendar.HOUR_OF_DAY))
                    && (startTime.get(Calendar.MINUTE) == roomOccupationObj.getStartTime().get(
                            Calendar.MINUTE))
                    && (endTime.get(Calendar.HOUR_OF_DAY) == roomOccupationObj.getEndTime().get(
                            Calendar.HOUR_OF_DAY))
                    && (endTime.get(Calendar.MINUTE) == roomOccupationObj.getEndTime().get(
                            Calendar.MINUTE))
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
         result += ", startTime=" + DateFormatUtils.format(startTime.getTime(), "HH:mm");
        result += ", endTime=" + DateFormatUtils.format(endTime.getTime(), "HH:mm");
        result += ", dayOfWeek=" + dayOfWeek;
        result += ", periodId=" + period.getIdInternal();
       result += ", period=" + DateFormatUtils.format(period.getStartDate().getTime(), "yyyy-MM-dd");
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
        return roomOccupationForDateAndTime(roomOccupation.getPeriod(), roomOccupation.getStartTime(),
                roomOccupation.getEndTime(), roomOccupation.getDayOfWeek(), roomOccupation
                        .getFrequency(), roomOccupation.getWeekOfQuinzenalStart(), roomOccupation
                        .getRoom());
    }

    public boolean roomOccupationForDateAndTime(IPeriod period, Calendar startTime, Calendar endTime,
            DiaSemana dayOfWeek, Integer frequency, Integer week, ISala room) {
        if (!room.equals(this.getRoom())) {
            return false;
        }
        if (this.getPeriod().intersectPeriods(period)) {
            if (dayOfWeek.equals(this.dayOfWeek)) {
                if (CalendarUtil.intersectTimes(this.getStartTime(), this.getEndTime(), startTime,
                        endTime)) {
                    /** * */
                    if (this.frequency.intValue() == DIARIA) {
                        if (frequency.intValue() == QUINZENAL) {
                            return periodQuinzenalContainsDay(period, week.intValue(), dayOfWeek,
                                    this.period.getStartDate());
                        }
                        return true;
                        
                    }
                    if (this.frequency.intValue() == SEMANAL) {
                        if (frequency.intValue() == QUINZENAL) {
                            return periodQuinzenalContainsWeekPeriod(period, week.intValue(), dayOfWeek,
                                    this.period);
                        }
                        return true;
                    }
                    if (this.frequency.intValue() == QUINZENAL) {
                        if (frequency.intValue() == QUINZENAL) {
                            return periodQuinzenalContainsQuinzenalPeriod(this.period,
                                    this.weekOfQuinzenalStart.intValue(), dayOfWeek, period, week
                                            .intValue());
                        }
                        if (frequency.intValue() == SEMANAL) {
                            return periodQuinzenalContainsWeekPeriod(this.period,
                                    this.weekOfQuinzenalStart.intValue(), dayOfWeek, period);
                        }
                        if (frequency.intValue() == DIARIA) {
                            return periodQuinzenalContainsDay(this.period, this.weekOfQuinzenalStart
                                    .intValue(), dayOfWeek, period.getStartDate());
                        }
                    }
                    return true;
                    /** * */
                }
            }
        }
        return false;
    }

    public boolean roomOccupationForDateAndTime(IPeriod period, Calendar startTime, Calendar endTime,
            DiaSemana dayOfWeek, Integer frequency, Integer week) {
        if (this.getPeriod().intersectPeriods(period)) {
            if (dayOfWeek.equals(this.dayOfWeek)) {
                if (CalendarUtil.intersectTimes(this.getStartTime(), this.getEndTime(), startTime,
                        endTime)) {
                    /** * */
                    if (this.frequency.intValue() == DIARIA) {
                        if (frequency.intValue() == QUINZENAL) {
                            return periodQuinzenalContainsDay(period, week.intValue(), dayOfWeek,
                                    this.period.getStartDate());
                        }
                        return true;
                    }
                    if (this.frequency.intValue() == SEMANAL) {
                        if (frequency.intValue() == QUINZENAL) {
                            return periodQuinzenalContainsWeekPeriod(period, week.intValue(), dayOfWeek,
                                    this.period);
                        }
                        return true;
                        
                    }
                    if (this.frequency.intValue() == QUINZENAL) {
                        if (frequency.intValue() == QUINZENAL) {
                            return periodQuinzenalContainsQuinzenalPeriod(this.period,
                                    this.weekOfQuinzenalStart.intValue(), dayOfWeek, period, week
                                            .intValue());
                        }
                        if (frequency.intValue() == SEMANAL) {
                            return periodQuinzenalContainsWeekPeriod(this.period,
                                    this.weekOfQuinzenalStart.intValue(), dayOfWeek, period);
                        }
                        if (frequency.intValue() == DIARIA) {
                            return periodQuinzenalContainsDay(this.period, this.weekOfQuinzenalStart
                                    .intValue(), dayOfWeek, period.getStartDate());
                        }
                    }
                    return true;
                    /** * */
                }
            }
        }
        return false;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = new Integer(frequency);
    }

    public Integer getWeekOfQuinzenalStart() {
        return weekOfQuinzenalStart;
    }

    public void setWeekOfQuinzenalStart(Integer weekOfQuinzenalStart) {
        this.weekOfQuinzenalStart = weekOfQuinzenalStart;
    }

    public static boolean periodQuinzenalContainsDay(IPeriod period, int startWeek, DiaSemana weekDay,
            Calendar date) {
        /*
         Calendar periodDate = Calendar.getInstance();
         periodDate.setTimeInMillis(period.getStartDate().getTimeInMillis());
         periodDate.add(Calendar.DATE, weekDay.getDiaSemana().intValue()
         - periodDate.get(Calendar.DAY_OF_WEEK));
         if (startWeek == 2) {
         periodDate.add(Calendar.DATE, 7);
         }
         while (periodDate.before(period.getEndDate())) {
         if (CalendarUtil.equalDates(periodDate, date)) {
         return true;
         }
         if (periodDate.after(date)) {
         return false;
         }
         periodDate.add(Calendar.DATE, 14);
         }
         return false;
         */
        ArrayList list = quinzenalDatesInPeriod(period, startWeek, weekDay);
        for (int i = 0; i < list.size(); i++) {
            Calendar quinzenalDate = (Calendar) list.get(i);
            if (CalendarUtil.equalDates(quinzenalDate, date)) {
                return true;
            }
            if (quinzenalDate.after(date)) {
                return false;
            }
        }
        return false;
    }

    public static boolean periodQuinzenalContainsWeekPeriod(IPeriod periodQuinzenal, int startWeek,
            DiaSemana weekDay, IPeriod period) {

        /*        
         Calendar startInterception = Calendar.getInstance();
         startInterception.setTimeInMillis(periodQuinzenal.getStartDate().getTimeInMillis());
         Calendar endInterception = Calendar.getInstance();
         endInterception.setTimeInMillis(periodQuinzenal.getEndDate().getTimeInMillis());
         boolean quinzenalBefore = false;
         if (startInterception.before(period.getStartDate())) {
         startInterception.setTimeInMillis(period.getStartDate().getTimeInMillis());
         quinzenalBefore = true;
         }
         if (endInterception.after(period.getEndDate())) {
         endInterception.setTimeInMillis(period.getEndDate().getTimeInMillis());
         }
         if (!quinzenalBefore) {
         startInterception.add(Calendar.DATE, weekDay.getDiaSemana().intValue()
         - startInterception.get(Calendar.DAY_OF_WEEK));
         if (startWeek == 2) {
         startInterception.add(Calendar.DATE, 7);
         }
         if (startInterception.before(endInterception)) {
         return true;
         } else {
         return false;
         }

         } else {
         Calendar quinzenalDate = Calendar.getInstance();
         quinzenalDate.setTimeInMillis(periodQuinzenal.getStartDate().getTimeInMillis());
         quinzenalDate.add(Calendar.DATE, weekDay.getDiaSemana().intValue()
         - quinzenalDate.get(Calendar.DAY_OF_WEEK));
         if (startWeek == 2) {
         quinzenalDate.add(Calendar.DATE, 7);
         }
         while (true) {
         if (quinzenalDate.after(startInterception)) {
         break;
         }
         quinzenalDate.add(Calendar.DATE, 14);
         }
         if (quinzenalDate.before(endInterception)) {
         return true;
         } else {
         return false;
         }
         }
         */
        ArrayList listWeekly = weeklyDatesInPeriod(period, weekDay);
        ArrayList listQuinzenal = quinzenalDatesInPeriod(periodQuinzenal, startWeek, weekDay);
        for (int i = 0; i < listQuinzenal.size(); i++) {
            Calendar quinzenalDate = (Calendar) listQuinzenal.get(i);
            for (int j = 0; j < listWeekly.size(); j++) {
                Calendar date = (Calendar) listWeekly.get(j);
                if (CalendarUtil.equalDates(quinzenalDate, date)) {
                    return true;
                }
                if (date.after(quinzenalDate)) {
                    break;
                }
            }
        }
        return false;

    }

    public static boolean periodQuinzenalContainsQuinzenalPeriod(IPeriod period1, int startWeek1,
            DiaSemana weekDay, IPeriod period2, int startWeek2) {
        /*        
         Calendar quinzenalDate1 = Calendar.getInstance();
         quinzenalDate1.setTimeInMillis(period1.getStartDate().getTimeInMillis());
         quinzenalDate1.add(Calendar.DATE, weekDay.getDiaSemana().intValue()
         - quinzenalDate1.get(Calendar.DAY_OF_WEEK));
         if (startWeek1 == 2) {
         quinzenalDate1.add(Calendar.DATE, 7);
         }

         Calendar quinzenalDate2 = Calendar.getInstance();
         quinzenalDate2.setTimeInMillis(period2.getStartDate().getTimeInMillis());
         quinzenalDate2.add(Calendar.DATE, weekDay.getDiaSemana().intValue()
         - quinzenalDate2.get(Calendar.DAY_OF_WEEK));
         if (startWeek2 == 2) {
         quinzenalDate2.add(Calendar.DATE, 7);
         }
         int week1 = quinzenalDate1.get(Calendar.WEEK_OF_YEAR);
         int week2 = quinzenalDate2.get(Calendar.WEEK_OF_YEAR);
         if (week1 % 2 != week2 % 2) {
         return false;
         }
         if (quinzenalDate1.after(quinzenalDate2)) {
         return periodQuinzenalContainsDay(period2, startWeek2, weekDay, quinzenalDate1);
         } else {
         return periodQuinzenalContainsDay(period1, startWeek1, weekDay, quinzenalDate2);
         }
         */
        ArrayList list1 = quinzenalDatesInPeriod(period1, startWeek1, weekDay);
        ArrayList list2 = quinzenalDatesInPeriod(period2, startWeek2, weekDay);
        for (int i = 0; i < list1.size(); i++) {
            Calendar quinzenalDate1 = (Calendar) list1.get(i);
            for (int j = 0; j < list2.size(); j++) {
                Calendar quinzenalDate2 = (Calendar) list2.get(j);
                if (CalendarUtil.equalDates(quinzenalDate1, quinzenalDate2)) {
                    return true;
                }
                if (quinzenalDate2.after(quinzenalDate1)) {
                    break;
                }
            }
        }
        return false;
    }

    public static ArrayList quinzenalDatesInPeriod(IPeriod period, int startWeek, DiaSemana weekDay) {
        ArrayList list = new ArrayList();
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(period.getStartDate().getTimeInMillis());
        date.add(Calendar.DATE, weekDay.getDiaSemana().intValue() - date.get(Calendar.DAY_OF_WEEK));
        if (startWeek == 2) {
            date.add(Calendar.DATE, 7);
        }
        while (true) {
            if (date.after(period.getEndDate())) {
                if (period.getNextPeriod() == null) {
                    break;
                }
                int interval = period.getNextPeriod().getStartDate().get(Calendar.DAY_OF_YEAR)
                        - period.getEndDate().get(Calendar.DAY_OF_YEAR) - 1;
                if (interval < 0) {
                    interval = period.getNextPeriod().getStartDate().get(Calendar.DAY_OF_YEAR) - 1;
                    interval += (period.getEndDate().getActualMaximum(Calendar.DAY_OF_YEAR) - period
                            .getEndDate().get(Calendar.DAY_OF_YEAR));
                }

                period = period.getNextPeriod();
                int weeksToJump = 0;
                if (interval % 7 > 3) {
                    weeksToJump++;
                }
                weeksToJump += interval / 7;
                date.add(Calendar.DATE, 7 * weeksToJump);
                if (date.before(period.getStartDate())) {
                    date.add(Calendar.DATE, 7);
                }
            } else {
                Calendar dateToAdd = Calendar.getInstance();
                dateToAdd.setTimeInMillis(date.getTimeInMillis());
                list.add(dateToAdd);
                date.add(Calendar.DATE, 14);
            }
        }
        return list;
    }

    public static ArrayList weeklyDatesInPeriod(IPeriod period, DiaSemana weekDay) {
        ArrayList list = new ArrayList();
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(period.getStartDate().getTimeInMillis());
        date.add(Calendar.DATE, weekDay.getDiaSemana().intValue() - date.get(Calendar.DAY_OF_WEEK));
        while (true) {
            if (date.after(period.getEndDate())) {
                if (period.getNextPeriod() == null) {
                    break;
                }
                int interval = period.getNextPeriod().getStartDate().get(Calendar.DAY_OF_YEAR)
                        - period.getEndDate().get(Calendar.DAY_OF_YEAR) - 1;
                if (interval < 0) {
                    interval = period.getNextPeriod().getStartDate().get(Calendar.DAY_OF_YEAR) - 1;
                    interval += (period.getEndDate().getActualMaximum(Calendar.DAY_OF_YEAR) - period
                            .getEndDate().get(Calendar.DAY_OF_YEAR));
                }
                period = period.getNextPeriod();
                int weeksToJump = interval / 7;
                date.add(Calendar.DATE, 7 * weeksToJump);
                if (date.before(period.getStartDate())) {
                    date.add(Calendar.DATE, 7);
                }
            } else {
                Calendar dateToAdd = Calendar.getInstance();
                dateToAdd.setTimeInMillis(date.getTimeInMillis());
                list.add(dateToAdd);
                date.add(Calendar.DATE, 7);
            }
            
        }
        return list;
    }
}