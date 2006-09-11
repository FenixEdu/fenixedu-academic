package net.sourceforge.fenixedu.domain;

/**
 * 
 * @author tfc130
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.domain.space.RoomOccupation;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.date.TimePeriod;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.joda.time.YearMonthDay;

public class Lesson extends Lesson_Base {

    public static final Comparator LESSON_COMPARATOR_BY_WEEKDAY_AND_STARTTIME = new ComparatorChain();
    static {
        ((ComparatorChain) LESSON_COMPARATOR_BY_WEEKDAY_AND_STARTTIME).addComparator(new BeanComparator(
                "diaSemana.diaSemana"));
        ((ComparatorChain) LESSON_COMPARATOR_BY_WEEKDAY_AND_STARTTIME).addComparator(new BeanComparator(
                "beginHourMinuteSecond"));
    }

    public Lesson() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public Lesson(DiaSemana diaSemana, Calendar inicio, Calendar fim, ShiftType tipo, OldRoom sala,
            RoomOccupation roomOccupation, Shift shift, Integer weekOfQuinzenalStart, Integer frequency) {

        this();
        setDiaSemana(diaSemana);
        setInicio(inicio);
        setFim(fim);
        setTipo(tipo);
        setSala(sala);
        setRoomOccupation(roomOccupation);
        setShift(shift);
        setFrequency(frequency);
        setWeekOfQuinzenalStart(weekOfQuinzenalStart);
    }

    public void edit(DiaSemana diaSemana, Calendar inicio, Calendar fim, ShiftType tipo,
            OldRoom salaNova, Integer frequency, Integer weekOfQuinzenalStart) {

        setDiaSemana(diaSemana);
        setInicio(inicio);
        setFim(fim);
        setTipo(tipo);
        setSala(salaNova);
        setFrequency(frequency);
        setWeekOfQuinzenalStart(weekOfQuinzenalStart);
    }

    public void delete() {
        removeExecutionPeriod();
        removeSala();
        removeShift();
        getRoomOccupation().delete();

        removeRootDomainObject();
        deleteDomainObject();
    }

    public Calendar getInicio() {
        if (this.getBegin() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getBegin());
            return result;
        }
        return null;
    }

    public void setInicio(Calendar inicio) {
        if (inicio != null) {
            this.setBegin(inicio.getTime());
        } else {
            this.setBegin(null);
        }
    }

    public Calendar getFim() {
        if (this.getEnd() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getEnd());
            return result;
        }
        return null;
    }

    public void setFim(Calendar fim) {
        if (fim != null) {
            this.setEnd(fim.getTime());
        } else {
            this.setEnd(null);
        }
    }

    public double hours() {
        TimePeriod timePeriod = new TimePeriod(this.getInicio(), this.getFim());
        return timePeriod.hours().doubleValue();
    }

    public String getInicioString() {
        return String.valueOf(getInicio().get(Calendar.HOUR_OF_DAY));
    }

    public double hoursAfter(int hour) {

        final Calendar start = this.getInicio();
        final Calendar end = this.getFim();

        final Calendar specifiedHour = Calendar.getInstance();
        specifiedHour.setTime(this.getEnd());
        specifiedHour.set(Calendar.HOUR_OF_DAY, hour);
        specifiedHour.set(Calendar.MINUTE, 0);
        specifiedHour.set(Calendar.SECOND, 0);
        specifiedHour.set(Calendar.MILLISECOND, 0);

        if (!start.before(specifiedHour)) {
            TimePeriod timePeriod = new TimePeriod(start, end);
            return timePeriod.hours().doubleValue();

        } else if (end.after(specifiedHour)) {
            TimePeriod timePeriod = new TimePeriod(specifiedHour, end);
            return timePeriod.hours().doubleValue();
        }

        return 0.0;
    }
   
    public List<Summary> getSummaries() {
        List<Summary> lessonSummaries = new ArrayList<Summary>();
        Set<Summary> shiftSummaries = new TreeSet<Summary>(new ReverseComparator(Summary.COMPARATOR_BY_DATE_AND_HOUR));
        shiftSummaries.addAll(getShift().getAssociatedSummariesSet());
        for (Summary summary : shiftSummaries) {
            if (summary.getSummaryHourHourMinuteSecond().isEqual(getBeginHourMinuteSecond())) {
                lessonSummaries.add(summary);
            }
        }
        return lessonSummaries;
    }
    
    public boolean isDateValid(YearMonthDay date) {
	YearMonthDay lessonEndDay = getLessonEndDay();
	YearMonthDay currentDate = new YearMonthDay();

	YearMonthDay startDateToSearch = getLessonStartDay();
	YearMonthDay endDateToSearch = (lessonEndDay.isAfter(currentDate)) ? currentDate : lessonEndDay;

	if (!startDateToSearch.isAfter(endDateToSearch)) {
	    while (true) {
		if (startDateToSearch.isEqual(date)) {
		    return true;
		}
		startDateToSearch = startDateToSearch.plusDays(7);
		if (startDateToSearch.isAfter(endDateToSearch)) {
		    return false;
		}
	    }
	}
	return false;
    }

    private YearMonthDay getLessonStartDay() {
        YearMonthDay periodStart = getRoomOccupation().getPeriod().getStartYearMonthDay();
        int weekOfQuinzenalStart = (getWeekOfQuinzenalStart() != null) ? getWeekOfQuinzenalStart().intValue() : 0;
        YearMonthDay lessonStart = periodStart.plusDays(7 * weekOfQuinzenalStart);      
        int lessonStartDayOfWeek = lessonStart.toDateTimeAtMidnight().getDayOfWeek();
        return lessonStart.plusDays(getLessonWeekDay() - lessonStartDayOfWeek);
    }

    private YearMonthDay getLessonEndDay() {
        YearMonthDay periodEnd = getRoomOccupation().getPeriod().getEndYearMonthDay();   
        int lessonEndDayOfWeek = periodEnd.toDateTimeAtMidnight().getDayOfWeek();
        return periodEnd.minusDays(lessonEndDayOfWeek - getLessonWeekDay());
    }

    private int getLessonWeekDay() {
        return (getDiaSemana().getDiaSemana().intValue() == 1) ? 7 : (getDiaSemana()
                .getDiaSemana().intValue() - 1);
    }
    
    public List<YearMonthDay> getPossibleDatesToInsertSummary() {
        List<Summary> summaries = getSummaries();
        List<YearMonthDay> datesToInsert = new ArrayList<YearMonthDay>();

        YearMonthDay lessonEndDay = getLessonEndDay();
        YearMonthDay currentDate = new YearMonthDay();

        YearMonthDay startDateToSearch = getLessonStartDay();
        YearMonthDay endDateToSearch = (lessonEndDay.isAfter(currentDate)) ? currentDate : lessonEndDay;

        if (!startDateToSearch.isAfter(endDateToSearch)) {
            if (summaries.isEmpty()) {
                while(true) {
                    datesToInsert.add(startDateToSearch);
                    startDateToSearch = startDateToSearch.plusDays(7);
                    if (startDateToSearch.isAfter(endDateToSearch)) {
                        break;
                    }
                }
            } else {
                for (Summary summary : summaries) {
                    while (true) {
                        if (startDateToSearch.isBefore(summary.getSummaryDateYearMonthDay())) {
                            datesToInsert.add(startDateToSearch);
                            startDateToSearch = startDateToSearch.plusDays(7);
                        } else if (startDateToSearch.isEqual(summary.getSummaryDateYearMonthDay())) {
                            startDateToSearch = startDateToSearch.plusDays(7);
                            break;
                        } else {
                            // ERROR
                            System.out.println("Não é suposto entrar aqui....");
                            startDateToSearch = startDateToSearch.plusDays(7);
                        }
                        if (startDateToSearch.isAfter(endDateToSearch)) {
                            break;
                        }
                    }                    
                }
                if (!startDateToSearch.isAfter(endDateToSearch)) {
                    while(true) {
                        datesToInsert.add(startDateToSearch);
                        startDateToSearch = startDateToSearch.plusDays(7);
                        if (startDateToSearch.isAfter(endDateToSearch)) {
                            break;
                        }
                    }
                }
            }
        }
        return datesToInsert;
    }
}
