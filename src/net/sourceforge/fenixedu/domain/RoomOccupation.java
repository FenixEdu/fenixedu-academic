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
        if (calendar != null) {
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
        if (calendar != null) {
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
                            return periodQuinzenalContainsDay(period.getStartDate(), period.getEndDate(), week.intValue(), dayOfWeek, this
                                    .getPeriod().getStartDate());
                        }
                        return true;

                    }
                    if (this.getFrequency().intValue() == SEMANAL) {
                        if (frequency.intValue() == QUINZENAL) {
                            return periodQuinzenalContainsWeekPeriod(period.getStartDate(), period.getEndDate(), week.intValue(), dayOfWeek,
                                    this.getPeriod().getStartDate(), this.getPeriod().getEndDate(),
                                    this.getPeriod().getNextPeriod());
                        }
                        return true;
                    }
                    if (this.getFrequency().intValue() == QUINZENAL) {
                        if (frequency.intValue() == QUINZENAL) {
                            return periodQuinzenalContainsQuinzenalPeriod(this.getPeriod().getStartDate(), this.getPeriod().getEndDate(),
                                    this.getWeekOfQuinzenalStart().intValue(), dayOfWeek, period.getStartDate(), period.getEndDate(), week
                                    .intValue());
                        }
                        if (frequency.intValue() == SEMANAL) {
                            return periodQuinzenalContainsWeekPeriod(this.getPeriod().getStartDate(), this.getPeriod().getEndDate(), this
                                    .getWeekOfQuinzenalStart().intValue(), dayOfWeek,
                                    this.getPeriod().getStartDate(), this.getPeriod().getEndDate(),
                                    this.getPeriod().getNextPeriod());
                        }
                        if (frequency.intValue() == DIARIA) {
                            return periodQuinzenalContainsDay(this.getPeriod().getStartDate(), this.getPeriod().getEndDate(), this
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

    public boolean roomOccupationForDateAndTime(Calendar startDate, Calendar endDate, Calendar startTime, Calendar endTime,
            DiaSemana dayOfWeek, Integer frequency, Integer week) {
        if (this.getPeriod().intersectPeriods(startDate, endDate)) {
            if (dayOfWeek.equals(this.getDayOfWeek())) {
                if (CalendarUtil.intersectTimes(this.getStartTime(), this.getEndTime(), startTime,
                        endTime)) {
                    if (this.getFrequency().intValue() == DIARIA) {
                        if (frequency.intValue() == QUINZENAL) {
                            return periodQuinzenalContainsDay(startDate, endDate, week.intValue(), dayOfWeek, this
                                    .getPeriod().getStartDate());
                        }
                        return true;
                    }
                    if (this.getFrequency().intValue() == SEMANAL) {
                        if (frequency.intValue() == QUINZENAL) {
                            return periodQuinzenalContainsWeekPeriod(startDate, endDate, week.intValue(), dayOfWeek,
                                    this.getPeriod().getStartDate(), this.getPeriod().getEndDate(),
                                    this.getPeriod().getNextPeriod());
                        }
                        return true;

                    }
                    if (this.getFrequency().intValue() == QUINZENAL) {
                        if (frequency.intValue() == QUINZENAL) {
                            return periodQuinzenalContainsQuinzenalPeriod(this.getPeriod().getStartDate(), this.getPeriod().getEndDate(),
                                    this.getWeekOfQuinzenalStart().intValue(), dayOfWeek, startDate, endDate, week.intValue());
                        }
                        if (frequency.intValue() == SEMANAL) {
                            return periodQuinzenalContainsWeekPeriod(this.getPeriod().getStartDate(), this.getPeriod().getEndDate(), this
                                    .getWeekOfQuinzenalStart().intValue(), dayOfWeek,
                                    this.getPeriod().getStartDate(), this.getPeriod().getEndDate(),
                                    this.getPeriod().getNextPeriod());
                        }
                        if (frequency.intValue() == DIARIA) {
                            return periodQuinzenalContainsDay(this.getPeriod().getStartDate(), this.getPeriod().getEndDate(), this
                                    .getWeekOfQuinzenalStart().intValue(), dayOfWeek, startDate);
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
        return roomOccupationForDateAndTime(period.getStartDate(), period.getEndDate(), startTime, endTime, dayOfWeek, frequency, week);
    }

    public static boolean periodQuinzenalContainsDay(Calendar startDate, Calendar endDate, int startWeek, DiaSemana weekDay,
            Calendar date) {
        ArrayList list = quinzenalDatesInPeriod(startDate, endDate, startWeek, weekDay);
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

    public static boolean periodQuinzenalContainsWeekPeriod(Calendar startDate, Calendar endDate, int startWeek,
            DiaSemana weekDay, Calendar day, Calendar endDay, IPeriod nextPeriod) {
        ArrayList listWeekly = weeklyDatesInPeriod(day, endDay, weekDay, nextPeriod);
        ArrayList listQuinzenal = quinzenalDatesInPeriod(startDate, endDate, startWeek, weekDay);
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

    public static boolean periodQuinzenalContainsQuinzenalPeriod(Calendar startDatePeriod1, Calendar endDatePeriod1,
            int startWeek1, DiaSemana weekDay, Calendar startDatePeriod2, Calendar endDatePeriod2, int startWeek2) {
        ArrayList list1 = quinzenalDatesInPeriod(startDatePeriod1, endDatePeriod1, startWeek1, weekDay);
        ArrayList list2 = quinzenalDatesInPeriod(startDatePeriod2, endDatePeriod2, startWeek2, weekDay);
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

    public static ArrayList quinzenalDatesInPeriod(Calendar startDate, Calendar endDate, int startWeek, DiaSemana weekDay) {
        ArrayList list = new ArrayList();
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(startDate.getTimeInMillis());
        date.add(Calendar.DATE, weekDay.getDiaSemana().intValue() - date.get(Calendar.DAY_OF_WEEK));
        if (startWeek == 2) {
            date.add(Calendar.DATE, 7);
        }

        if (!date.after(endDate)) {
            list.add(date);
        }
        return list;
    }

    public static ArrayList weeklyDatesInPeriod(Calendar day, Calendar endDay, DiaSemana weekDay, IPeriod nextPeriod) {
        ArrayList list = new ArrayList();
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(day.getTimeInMillis());
        date.add(Calendar.DATE, weekDay.getDiaSemana().intValue() - date.get(Calendar.DAY_OF_WEEK));
        while (true) {
            if (date.after(endDay)) {
                if (nextPeriod == null) {
                    break;
                }
                int interval = nextPeriod.getStartDate().get(Calendar.DAY_OF_YEAR)
                        - endDay.get(Calendar.DAY_OF_YEAR) - 1;
                if (interval < 0) {
                    interval = nextPeriod.getStartDate().get(Calendar.DAY_OF_YEAR) - 1;
                    interval += (endDay.getActualMaximum(Calendar.DAY_OF_YEAR) - endDay.get(Calendar.DAY_OF_YEAR));
                }

                day = nextPeriod.getStartDate();
                endDay = nextPeriod.getEndDate();
                nextPeriod = nextPeriod.getNextPeriod();

                int weeksToJump = interval / 7;
                date.add(Calendar.DATE, 7 * weeksToJump);
                if (date.before(day)) {
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

    public void delete() {
        final IPeriod period = getPeriod();

        removeLesson();
        removeWrittenEvaluation();
        removeRoom();
        removePeriod();

        period.deleteIfEmpty();

        super.deleteDomainObject();
    }

}
