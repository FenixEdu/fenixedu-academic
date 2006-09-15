package net.sourceforge.fenixedu.domain;

/**
 * 
 * @author tfc130
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.domain.space.RoomOccupation;
import net.sourceforge.fenixedu.domain.space.Space;
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

    public Summary getSummaryByDate(YearMonthDay date) {
	for (Summary summary : getSummariesSortedByDate()) {
	    if (summary.getSummaryDateYearMonthDay().isEqual(date)) {
		return summary;
	    }
	}
	return null;
    }

    public SortedSet<Summary> getSummariesSortedByDate() {
	return getSummaries(new ReverseComparator(Summary.COMPARATOR_BY_DATE_AND_HOUR));
    }

    private SortedSet<Summary> getSummaries(Comparator comparator) {
	SortedSet<Summary> lessonSummaries = new TreeSet<Summary>(comparator);
	for (Summary summary : getShift().getAssociatedSummariesSet()) {
	    Lesson summaryLesson = summary.getLesson();
	    if (summaryLesson != null && summaryLesson.equals(this)) {
		lessonSummaries.add(summary);
	    }
	}
	return lessonSummaries;
    }

    public SortedSet<Summary> getSummariesSortedByNewestDate() {
	return getSummaries(Summary.COMPARATOR_BY_DATE_AND_HOUR);
    }

    private Summary getLastSummary() {
	SortedSet<Summary> summaries = getSummariesSortedByNewestDate();
	return (summaries.isEmpty()) ? null : summaries.first();
    }

    public boolean isDateValidToInsertSummary(YearMonthDay date) {
	YearMonthDay lessonEndDay = getLessonEndDay();
	YearMonthDay currentDate = new YearMonthDay();
	YearMonthDay startDateToSearch = getLessonStartDay();
	YearMonthDay endDateToSearch = (lessonEndDay.isAfter(currentDate)) ? currentDate : lessonEndDay;
	Campus lessonCampus = getLessonCampus();

	if (date != null && !Holiday.isHoliday(date, lessonCampus)
		&& !startDateToSearch.isAfter(endDateToSearch)) {
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
	int weekOfQuinzenalStart = (getWeekOfQuinzenalStart() != null) ? getWeekOfQuinzenalStart()
		.intValue() : 0;
	YearMonthDay lessonStart = periodStart.plusDays(7 * weekOfQuinzenalStart);
	int lessonStartDayOfWeek = lessonStart.toDateTimeAtMidnight().getDayOfWeek();
	return lessonStart.plusDays(getLessonWeekDayToYearMonthDayFormat() - lessonStartDayOfWeek);
    }

    private YearMonthDay getLessonEndDay() {
	YearMonthDay periodEnd = getRoomOccupation().getPeriod().getEndYearMonthDay();
	int lessonEndDayOfWeek = periodEnd.toDateTimeAtMidnight().getDayOfWeek();
	return periodEnd.minusDays(lessonEndDayOfWeek - getLessonWeekDayToYearMonthDayFormat());
    }

    private int getLessonWeekDayToYearMonthDayFormat() {
	return (getDiaSemana().getDiaSemana().intValue() == 1) ? 7 : (getDiaSemana().getDiaSemana()
		.intValue() - 1);
    }

    private Campus getLessonCampus() {
	net.sourceforge.fenixedu.domain.Campus oldCampus = getSala().getBuilding().getCampus();
	if (oldCampus != null) {
	    for (Campus campus : Space.getAllCampus()) {
		if (campus.getSpaceInformation().getName().equalsIgnoreCase(oldCampus.getName())) {
		    return campus;
		}
	    }
	}
	return null;
    }

    public YearMonthDay getNextPossibleSummaryDate() {
	YearMonthDay currentDate = new YearMonthDay();
	YearMonthDay lessonEndDay = getLessonEndDay();
	YearMonthDay endDateToSearch = (lessonEndDay.isAfter(currentDate)) ? currentDate : lessonEndDay;
	Campus lessonCampus = getLessonCampus();
	Summary lastSummary = getLastSummary();

	if (lastSummary != null) {
	    YearMonthDay summaryDateYearMonthDay = lastSummary.getSummaryDateYearMonthDay();
	    YearMonthDay nextSummaryDate = summaryDateYearMonthDay.plusDays(7);
	    while (true) {
		if (nextSummaryDate.isAfter(endDateToSearch)) {
		    break;
		}
		if (!Holiday.isHoliday(nextSummaryDate, lessonCampus)) {
		    return nextSummaryDate;
		}
		nextSummaryDate = nextSummaryDate.plusDays(7);
	    }
	} else {
	    YearMonthDay lessonStartDay = getLessonStartDay();
	    return (!lessonStartDay.isAfter(endDateToSearch)) ? lessonStartDay : null;
	}
	return null;
    }

    public List<YearMonthDay> getPossibleDatesToInsertSummary() {
	SortedSet<Summary> summaries = getSummariesSortedByDate();
	List<YearMonthDay> datesToInsert = new ArrayList<YearMonthDay>();

	YearMonthDay lessonEndDay = getLessonEndDay();
	YearMonthDay currentDate = new YearMonthDay();
	YearMonthDay startDateToSearch = getLessonStartDay();
	YearMonthDay endDateToSearch = (lessonEndDay.isAfter(currentDate)) ? currentDate : lessonEndDay;

	Campus lessonCampus = getLessonCampus();

	if (!startDateToSearch.isAfter(endDateToSearch)) {
	    if (summaries.isEmpty()) {
		while (true) {
		    if (!Holiday.isHoliday(startDateToSearch, lessonCampus)) {
			datesToInsert.add(startDateToSearch);
		    }
		    startDateToSearch = startDateToSearch.plusDays(7);
		    if (startDateToSearch.isAfter(endDateToSearch)) {
			break;
		    }
		}
	    } else {
		YearMonthDay dateBefore = null;
		for (Summary summary : summaries) {
		    if (dateBefore == null || !summary.getSummaryDateYearMonthDay().isEqual(dateBefore)) {
			while (true) {
			    if (startDateToSearch.isBefore(summary.getSummaryDateYearMonthDay())) {
				if (!Holiday.isHoliday(startDateToSearch, lessonCampus)) {
				    datesToInsert.add(startDateToSearch);
				}
				startDateToSearch = startDateToSearch.plusDays(7);
			    } else if (startDateToSearch.isEqual(summary.getSummaryDateYearMonthDay())) {
				startDateToSearch = startDateToSearch.plusDays(7);
				break;
			    } else {
				// ERROR
				System.out.println("3 2 1 ... BOOOOOOMMMMMMMMM!!!");
				startDateToSearch = startDateToSearch.plusDays(7);
			    }
			    if (startDateToSearch.isAfter(endDateToSearch)) {
				break;
			    }
			}
			dateBefore = summary.getSummaryDateYearMonthDay();
		    }
		}
		if (!startDateToSearch.isAfter(endDateToSearch)) {
		    while (true) {
			if (!Holiday.isHoliday(startDateToSearch, lessonCampus)) {
			    datesToInsert.add(startDateToSearch);
			}
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

    public List<YearMonthDay> getAllLessonDates() {
	List<YearMonthDay> dates = new ArrayList<YearMonthDay>();
	YearMonthDay startDateToSearch = getLessonStartDay();
	YearMonthDay endDateToSearch = getLessonEndDay();
	Campus lessonCampus = getLessonCampus();

	while (true) {
	    if (!Holiday.isHoliday(startDateToSearch, lessonCampus)) {
		dates.add(startDateToSearch);
	    }
	    startDateToSearch = startDateToSearch.plusDays(7);
	    if (startDateToSearch.isAfter(endDateToSearch)) {
		break;
	    }
	}

	return dates;
    }
}
