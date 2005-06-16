/*
 * Created on 9/Out/2003
 *
 */
package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Calendar;

import net.sourceforge.fenixedu.util.CalendarUtil;
import net.sourceforge.fenixedu.util.DiaSemana;

import org.apache.commons.lang.time.DateFormatUtils;

/**
 * @author Ana e Ricardo
 * 
 */
public class RoomOccupation extends RoomOccupation_Base {
    public static final int DIARIA = 1;

    public static final int SEMANAL = 2;

    public static final int QUINZENAL = 3;

    /**
     * Construtor
     */
    public RoomOccupation() {
    }

    public RoomOccupation(IRoom room, Calendar startTime, Calendar endTime, DiaSemana dayOfWeek,
            int frequency) {
        this.setRoom(room);
        this.setStartTime(startTime);
        this.setEndTime(endTime);
        this.setDayOfWeek(dayOfWeek);
        this.setFrequency(new Integer(frequency));
    }

    /**
     * @return
     */
    public Calendar getStartTime() {
        if (this.getStartTimeDate() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getStartTimeDate());
            return result;
        }
        return null;
    }

    /**
     * @param calendar
     */
    public void setStartTime(Calendar calendar) {
        if (calendar.getTime() != null) {
            this.setStartTimeDate(calendar.getTime());            
        } else {
            this.setStartTimeDate(null);
        }
    }

    /**
     * @return
     */
    public Calendar getEndTime() {
        if (this.getEndTimeDate() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getEndTimeDate());
            return result;
        }
        return null;
    }

    /**
     * @param calendar
     */
    public void setEndTime(Calendar calendar) {
        if (calendar.getTime() != null) {
            this.setEndTimeDate(calendar.getTime());            
        } else {
            this.setEndTimeDate(null);
        }
    }

    public String toString() {
        String result = "[ROOM OCCUPATION";
        result += ", codInt=" + getIdInternal();
        result += ", startTime=" + DateFormatUtils.format(this.getStartTime().getTime(), "HH:mm");
        result += ", endTime=" + DateFormatUtils.format(this.getEndTime().getTime(), "HH:mm");
        result += ", dayOfWeek=" + getDayOfWeek();
        result += ", periodId=" + getPeriod().getIdInternal();
        result += ", period="
                + DateFormatUtils.format(getPeriod().getStartDate().getTime(), "yyyy-MM-dd");
        result += ", room=" + getRoom().getNome();
        result += "]";
        return result;
    }

    public boolean roomOccupationForDateAndTime(IRoomOccupation roomOccupation) {
        return roomOccupationForDateAndTime(roomOccupation.getPeriod(), roomOccupation.getStartTime(),
                roomOccupation.getEndTime(), roomOccupation.getDayOfWeek(), roomOccupation
                        .getFrequency(), roomOccupation.getWeekOfQuinzenalStart(), roomOccupation
                        .getRoom());
    }

    public boolean roomOccupationForDateAndTime(IPeriod period, Calendar startTime, Calendar endTime,
            DiaSemana dayOfWeek, Integer frequency, Integer week, IRoom room) {
        if (!room.equals(this.getRoom())) {
            return false;
        }
        if (this.getPeriod().intersectPeriods(period)) {
            if (dayOfWeek.equals(this.getDayOfWeek())) {
                if (CalendarUtil.intersectTimes(this.getStartTime(), this.getEndTime(), startTime,
                        endTime)) {
                    /** * */
                    if (this.getFrequency().intValue() == DIARIA) {
                        if (frequency.intValue() == QUINZENAL) {
                            return periodQuinzenalContainsDay(period, week.intValue(), dayOfWeek, this
                                    .getPeriod().getStartDate());
                        }
                        return true;

                    }
                    if (this.getFrequency().intValue() == SEMANAL) {
                        if (frequency.intValue() == QUINZENAL) {
                            return periodQuinzenalContainsWeekPeriod(period, week.intValue(), dayOfWeek,
                                    this.getPeriod());
                        }
                        return true;
                    }
                    if (this.getFrequency().intValue() == QUINZENAL) {
                        if (frequency.intValue() == QUINZENAL) {
                            return periodQuinzenalContainsQuinzenalPeriod(this.getPeriod(), this
                                    .getWeekOfQuinzenalStart().intValue(), dayOfWeek, period, week
                                    .intValue());
                        }
                        if (frequency.intValue() == SEMANAL) {
                            return periodQuinzenalContainsWeekPeriod(this.getPeriod(), this
                                    .getWeekOfQuinzenalStart().intValue(), dayOfWeek, period);
                        }
                        if (frequency.intValue() == DIARIA) {
                            return periodQuinzenalContainsDay(this.getPeriod(), this
                                    .getWeekOfQuinzenalStart().intValue(), dayOfWeek, period
                                    .getStartDate());
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public boolean roomOccupationForDateAndTime(IPeriod period, Calendar startTime, Calendar endTime,
            DiaSemana dayOfWeek, Integer frequency, Integer week) {
        if (this.getPeriod().intersectPeriods(period)) {
            if (dayOfWeek.equals(this.getDayOfWeek())) {
                if (CalendarUtil.intersectTimes(this.getStartTime(), this.getEndTime(), startTime,
                        endTime)) {
                    if (this.getFrequency().intValue() == DIARIA) {
                        if (frequency.intValue() == QUINZENAL) {
                            return periodQuinzenalContainsDay(period, week.intValue(), dayOfWeek, this
                                    .getPeriod().getStartDate());
                        }
                        return true;
                    }
                    if (this.getFrequency().intValue() == SEMANAL) {
                        if (frequency.intValue() == QUINZENAL) {
                            return periodQuinzenalContainsWeekPeriod(period, week.intValue(), dayOfWeek,
                                    this.getPeriod());
                        }
                        return true;

                    }
                    if (this.getFrequency().intValue() == QUINZENAL) {
                        if (frequency.intValue() == QUINZENAL) {
                            return periodQuinzenalContainsQuinzenalPeriod(this.getPeriod(), this
                                    .getWeekOfQuinzenalStart().intValue(), dayOfWeek, period, week
                                    .intValue());
                        }
                        if (frequency.intValue() == SEMANAL) {
                            return periodQuinzenalContainsWeekPeriod(this.getPeriod(), this
                                    .getWeekOfQuinzenalStart().intValue(), dayOfWeek, period);
                        }
                        if (frequency.intValue() == DIARIA) {
                            return periodQuinzenalContainsDay(this.getPeriod(), this
                                    .getWeekOfQuinzenalStart().intValue(), dayOfWeek, period
                                    .getStartDate());
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean periodQuinzenalContainsDay(IPeriod period, int startWeek, DiaSemana weekDay,
            Calendar date) {
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

    public boolean containedIn(Calendar day, Calendar beginning, Calendar end) {
        return this.getStartTime().equals(beginning) && this.getEndTime().equals(end)
                && (getDayOfWeek().getDiaSemana().intValue() == day.get(Calendar.DAY_OF_WEEK))
                && getPeriod().containsDay(day);
    }

}
