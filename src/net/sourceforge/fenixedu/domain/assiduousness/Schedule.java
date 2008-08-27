package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeExceptionScheduleBean;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeScheduleFactory;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeWorkWeekScheduleBean;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Month;
import net.sourceforge.fenixedu.util.WeekDay;

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.Partial;
import org.joda.time.PeriodType;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public class Schedule extends Schedule_Base {

    public Schedule(Assiduousness assiduousness, LocalDate beginDate, LocalDate endDate, DateTime lastModifiedDate,
	    Employee modifiedBy) {
	super();
	init(assiduousness, beginDate, endDate, false, lastModifiedDate, modifiedBy);
    }

    public Schedule(Assiduousness assiduousness, LocalDate beginDate, LocalDate endDate, Boolean exception,
	    DateTime lastModifiedDate, Employee modifiedBy) {
	super();
	init(assiduousness, beginDate, endDate, exception, lastModifiedDate, modifiedBy);
    }

    public Schedule(EmployeeScheduleFactory employeeScheduleFactory, boolean deletedDays) {
	super();
	init(employeeScheduleFactory.getEmployee().getAssiduousness(), employeeScheduleFactory.getBeginDate(),
		employeeScheduleFactory.getEndDate(), false, new DateTime(), employeeScheduleFactory.getModifiedBy());

	for (EmployeeWorkWeekScheduleBean workWeekScheduleBean : employeeScheduleFactory.getEmployeeWorkWeekScheduleList()) {
	    Periodicity periodicity = getPeriodicity(workWeekScheduleBean.getWorkWeekNumber());
	    if (!deletedDays) {
		getWorkSchedules().addAll(workWeekScheduleBean.getWorkSchedules(periodicity));
	    } else {
		getWorkSchedules().addAll(workWeekScheduleBean.getWorkSchedulesForNonDeletedDays(periodicity));
	    }
	}
    }

    public Schedule(EmployeeScheduleFactory employeeScheduleFactory) {
	super();
	if (!validDates(employeeScheduleFactory.getBeginDate(), employeeScheduleFactory.getEndDate())) {
	    throw new DomainException("error.invalidDateInterval");
	}

	ClosedMonth closedMonth = ClosedMonth.getLastMonthClosed();
	if (isCloseMonthInsideScheduleInterval(closedMonth, employeeScheduleFactory.getBeginDate())) {
	    Month month = Month.values()[closedMonth.getClosedYearMonth().get(DateTimeFieldType.monthOfYear()) - 1];
	    throw new DomainException("error.schedule.monthClose", ResourceBundle.getBundle("resources.EnumerationResources",
		    Language.getLocale()).getString(month.name()), ((Integer) closedMonth.getClosedYearMonth().get(
		    DateTimeFieldType.year())).toString());
	}
	init(employeeScheduleFactory.getEmployee().getAssiduousness(), employeeScheduleFactory.getBeginDate(),
		employeeScheduleFactory.getEndDate(), false, new DateTime(), employeeScheduleFactory.getModifiedBy());
	if (overLapsAnotherSchedule(employeeScheduleFactory.getBeginDate(), employeeScheduleFactory.getEndDate(), false)) {
	    throw new DomainException("error.schedule.overlapsAnotherSchedule");
	}
	for (EmployeeWorkWeekScheduleBean workWeekScheduleBean : employeeScheduleFactory.getEmployeeWorkWeekScheduleList()) {
	    WorkWeek workWeek = workWeekScheduleBean.getWorkWeekByCheckedBox();
	    if (workWeek != null) {
		Periodicity periodicity = getPeriodicity(workWeekScheduleBean.getWorkWeekNumber());
		WorkSchedule workSchedule = new WorkSchedule(employeeScheduleFactory.getChoosenWorkSchedule(), workWeek,
			periodicity);
		getWorkSchedules().add(workSchedule);
	    }
	}
    }

    public void init(Assiduousness assiduousness, LocalDate beginDate, LocalDate endDate, Boolean exception,
	    DateTime lastModifiedDate, Employee modifiedBy) {
	if (!validDates(beginDate, endDate)) {
	    throw new DomainException("error.invalidDateInterval");
	}
	setRootDomainObject(RootDomainObject.getInstance());
	setAssiduousness(assiduousness);
	setBeginDate(beginDate);
	setEndDate(endDate);
	setException(exception);
	setLastModifiedDate(lastModifiedDate);
	setModifiedBy(modifiedBy);
    }

    // creates a new Exception Schedule
    public Schedule(EmployeeExceptionScheduleBean employeeExceptionScheduleBean) {
	super();
	ClosedMonth closedMonth = ClosedMonth.getLastMonthClosed();
	if (startsBeforeClosedMonth(closedMonth, employeeExceptionScheduleBean.getBeginDate())) {
	    Month month = Month.values()[closedMonth.getClosedYearMonth().get(DateTimeFieldType.monthOfYear()) - 1];
	    throw new DomainException("error.schedule.monthClose", ResourceBundle.getBundle("resources.EnumerationResources",
		    Language.getLocale()).getString(month.name()), ((Integer) closedMonth.getClosedYearMonth().get(
		    DateTimeFieldType.year())).toString());
	}
	setAssiduousness(employeeExceptionScheduleBean.getEmployee().getAssiduousness());
	if (overLapsAnotherSchedule(employeeExceptionScheduleBean.getBeginDate(), employeeExceptionScheduleBean.getEndDate(),
		true)) {
	    throw new DomainException("error.schedule.overlapsAnotherException");
	}
	init(employeeExceptionScheduleBean.getEmployee().getAssiduousness(), employeeExceptionScheduleBean.getBeginDate(),
		employeeExceptionScheduleBean.getEndDate(), true, new DateTime(), employeeExceptionScheduleBean.getModifiedBy());
	addWorkScheduleForExceptionSchedule(employeeExceptionScheduleBean);
    }

    public void editScheduleDates(LocalDate beginDate, LocalDate endDate) {
	if (endDate != null) {
	    if (!validDates(beginDate, endDate)) {
		throw new DomainException("error.invalidDateInterval");
	    }
	    DateTime endDateTime = endDate.plusDays(1).toDateTimeAtStartOfDay();
	    LocalDate closedMonthEndDate = ClosedMonth.getLastClosedLocalDate();
	    if (!endDateTime.isAfter(closedMonthEndDate.toDateTimeAtStartOfDay())) {
		throw new DomainException("error.schedule.monthClose", ResourceBundle.getBundle("resources.EnumerationResources",
			Language.getLocale()).getString(Month.values()[closedMonthEndDate.getMonthOfYear() - 1].name()),
			new Integer(closedMonthEndDate.get(DateTimeFieldType.year())).toString());
	    }
	}
	if (overLapsAnotherSchedule(beginDate, endDate, getException())) {
	    throw new DomainException("error.schedule.overlapsAnotherSchedule");
	} else {
	    setBeginDate(beginDate);
	    setEndDate(endDate);
	}
    }

    private boolean validDates(LocalDate beginDate, LocalDate endDate) {
	if (endDate != null) {
	    return endDate.isAfter(beginDate);
	}
	return true;
    }

    public void editException(EmployeeExceptionScheduleBean employeeExceptionScheduleBean) {
	setBeginDate(employeeExceptionScheduleBean.getBeginDate());
	setEndDate(employeeExceptionScheduleBean.getEndDate());
	ClosedMonth closedMonth = ClosedMonth.getLastMonthClosed();
	if (startsBeforeClosedMonth(closedMonth, getBeginDate())) {
	    Month month = Month.values()[closedMonth.getClosedYearMonth().get(DateTimeFieldType.monthOfYear()) - 1];
	    throw new DomainException("error.schedule.monthClose", ResourceBundle.getBundle("resources.EnumerationResources",
		    Language.getLocale()).getString(month.name()), ((Integer) closedMonth.getClosedYearMonth().get(
		    DateTimeFieldType.year())).toString());
	}
	if (overLapsAnotherSchedule(getBeginDate(), getEndDate(), true)) {
	    throw new DomainException("error.schedule.overlapsAnotherException");
	}
	setModifiedBy(employeeExceptionScheduleBean.getModifiedBy());
	setLastModifiedDate(new DateTime());
	if (!employeeExceptionScheduleBean.getOnlyChangeDates() && employeeExceptionScheduleBean.getSelected() != null) {
	    for (WorkSchedule workSchedule : getWorkSchedules()) {
		removeWorkSchedules(workSchedule);
		workSchedule.delete();
	    }
	    addWorkScheduleForExceptionSchedule(employeeExceptionScheduleBean);
	}
    }

    private void addWorkScheduleForExceptionSchedule(EmployeeExceptionScheduleBean employeeExceptionScheduleBean) {
	Periodicity periodicity = getPeriodicity(1);
	WorkSchedule workSchedule = new WorkSchedule((WorkScheduleType) employeeExceptionScheduleBean.getSelected(),
		getFullWorkWeek(), periodicity);
	getWorkSchedules().add(workSchedule);
    }

    private WorkWeek getFullWorkWeek() {
	List<WeekDay> weekDays = new ArrayList<WeekDay>();
	weekDays.add(WeekDay.MONDAY);
	weekDays.add(WeekDay.TUESDAY);
	weekDays.add(WeekDay.WEDNESDAY);
	weekDays.add(WeekDay.THURSDAY);
	weekDays.add(WeekDay.FRIDAY);
	WeekDay array[] = {};
	return new WorkWeek(weekDays.toArray(array));
    }

    // private boolean overLapsAnotherSchedule(Interval scheduleInterval,
    // Boolean exception) {
    // for (Schedule schedule : getAssiduousness().getSchedules()) {
    // if (schedule != this && schedule.getException().equals(exception) &&
    // schedule.isDefinedInInterval(scheduleInterval)) {
    // return true;
    // }
    // }
    // return false;
    // }

    private boolean overLapsAnotherSchedule(LocalDate beginDate, LocalDate endDate, Boolean exception) {
	for (Schedule schedule : getAssiduousness().getSchedules()) {
	    if (schedule != this && schedule.getException().equals(exception) && schedule.isDefinedInInterval(beginDate, endDate)) {
		return true;
	    }
	}
	return false;
    }

    public Schedule deleteDays(EmployeeScheduleFactory employeeScheduleFactory) {
	ClosedMonth closedMonth = ClosedMonth.getLastMonthClosed();
	if (isCloseMonthInsideScheduleInterval(closedMonth, getBeginDate())) {
	    LocalDate endDate = new LocalDate(closedMonth.getClosedYearMonth().get(DateTimeFieldType.year()), closedMonth
		    .getClosedYearMonth().get(DateTimeFieldType.monthOfYear()), 1);
	    endDate = endDate.plusDays(endDate.dayOfMonth().getMaximumValue()).minusDays(1);
	    return closeScheduleAndMakeNew(employeeScheduleFactory, endDate, true);
	} else {
	    if (isCloseMonthInsideInterval(closedMonth, employeeScheduleFactory.getBeginDate(), employeeScheduleFactory
		    .getEndDate())) {
		Month month = Month.values()[closedMonth.getClosedYearMonth().get(DateTimeFieldType.monthOfYear()) - 1];
		throw new DomainException("error.schedule.monthClose", ResourceBundle.getBundle("resources.EnumerationResources",
			Language.getLocale()).getString(month.name()), ((Integer) closedMonth.getClosedYearMonth().get(
			DateTimeFieldType.year())).toString());
	    }
	    eliminateDays(employeeScheduleFactory);
	}
	return this;
    }

    private void eliminateDays(EmployeeScheduleFactory employeeScheduleFactory) {
	for (EmployeeWorkWeekScheduleBean workWeekScheduleBean : employeeScheduleFactory.getEmployeeWorkWeekScheduleList()) {
	    WorkWeek workWeek = workWeekScheduleBean.getWorkWeekByCheckedBox();
	    if (workWeek != null) {
		updateWorkSchedules(workWeek, workWeekScheduleBean.getWorkWeekNumber());
	    }
	}
	setBeginDate(employeeScheduleFactory.getBeginDate());
	setEndDate(employeeScheduleFactory.getEndDate());
	setModifiedBy(employeeScheduleFactory.getModifiedBy());
	setLastModifiedDate(new DateTime());
	adjustWorkWeekNumbers();
    }

    private void adjustWorkWeekNumbers() {
	List<WorkSchedule> orderedWorkScheduleList = new ArrayList<WorkSchedule>(getWorkSchedules());
	Collections.sort(orderedWorkScheduleList, new BeanComparator("periodicity.workWeekNumber"));
	Iterator<WorkSchedule> iterator = orderedWorkScheduleList.iterator();
	int correctWorkWeek = 1;
	WorkSchedule firtWorkSchedule = iterator.next();
	int previousWorkWeek = firtWorkSchedule.getPeriodicity().getWorkWeekNumber().intValue();
	if (previousWorkWeek != correctWorkWeek) {
	    firtWorkSchedule.setPeriodicity(getPeriodicity(Integer.valueOf(correctWorkWeek)));
	}
	while (iterator.hasNext()) {
	    WorkSchedule workSchedule = iterator.next();
	    int currentWorkWeek = workSchedule.getPeriodicity().getWorkWeekNumber().intValue();
	    if (currentWorkWeek == previousWorkWeek) {
		workSchedule.setPeriodicity(getPeriodicity(Integer.valueOf(correctWorkWeek)));
	    } else {
		correctWorkWeek++;
		previousWorkWeek = currentWorkWeek;
		workSchedule.setPeriodicity(getPeriodicity(Integer.valueOf(correctWorkWeek)));
	    }
	}
    }

    public Schedule edit(EmployeeScheduleFactory employeeScheduleFactory) {
	ClosedMonth closedMonth = ClosedMonth.getLastMonthClosed();
	if (isCloseMonthInsideScheduleInterval(closedMonth, getBeginDate())) {
	    LocalDate endDate = new LocalDate(closedMonth.getClosedYearMonth().get(DateTimeFieldType.year()), closedMonth
		    .getClosedYearMonth().get(DateTimeFieldType.monthOfYear()), 1);
	    endDate = endDate.plusDays(endDate.dayOfMonth().getMaximumValue()).minusDays(1);
	    return closeScheduleAndMakeNew(employeeScheduleFactory, endDate, false);
	} else {
	    if (isCloseMonthInsideInterval(closedMonth, employeeScheduleFactory.getBeginDate(), employeeScheduleFactory
		    .getEndDate())) {
		Month month = Month.values()[closedMonth.getClosedYearMonth().get(DateTimeFieldType.monthOfYear()) - 1];
		throw new DomainException("error.schedule.monthClose", ResourceBundle.getBundle("resources.EnumerationResources",
			Language.getLocale()).getString(month.name()), ((Integer) closedMonth.getClosedYearMonth().get(
			DateTimeFieldType.year())).toString());
	    }
	    if (getBeginDate().isBefore(employeeScheduleFactory.getBeginDate())
		    && getAssiduousness().hasAnyRecordsBetweenDates(getBeginDate(), employeeScheduleFactory.getBeginDate())) {
		return closeScheduleAndMakeNew(employeeScheduleFactory, employeeScheduleFactory.getBeginDate().minusDays(1),
			false);
	    } else {
		if (employeeScheduleFactory.isDifferencesInDates() && !employeeScheduleFactory.isDifferencesInWorkSchedules()) {
		    setBeginDate(employeeScheduleFactory.getBeginDate());
		    setEndDate(employeeScheduleFactory.getEndDate());
		    setModifiedBy(employeeScheduleFactory.getModifiedBy());
		    setLastModifiedDate(new DateTime());
		} else {
		    updateEverything(employeeScheduleFactory);
		}
	    }
	}
	return this;
    }

    private Schedule closeScheduleAndMakeNew(EmployeeScheduleFactory employeeScheduleFactory, LocalDate endDate,
	    boolean deletedDays) {
	setModifiedBy(employeeScheduleFactory.getModifiedBy());
	setLastModifiedDate(new DateTime());
	if (endDate.plusDays(1).isBefore(employeeScheduleFactory.getBeginDate())) {
	    setEndDate(employeeScheduleFactory.getBeginDate().minusDays(1));
	} else {
	    employeeScheduleFactory.setBeginDate(endDate.plusDays(1));
	    setEndDate(endDate);
	}
	return new Schedule(employeeScheduleFactory, deletedDays);
    }

    private void updateEverything(EmployeeScheduleFactory employeeScheduleFactory) {
	for (EmployeeWorkWeekScheduleBean workWeekScheduleBean : employeeScheduleFactory.getEmployeeWorkWeekScheduleList()) {
	    WorkWeek workWeek = workWeekScheduleBean.getWorkWeekByCheckedBox();
	    if (workWeek != null) {
		int workWeekNumber = workWeekScheduleBean.getWorkWeekNumber();
		updateWorkSchedules(workWeek, workWeekNumber);
		Periodicity periodicity = getPeriodicity(workWeekNumber);
		WorkSchedule workSchedule = getWorkSchedule(employeeScheduleFactory.getChoosenWorkSchedule(), workWeek,
			periodicity);
		getWorkSchedules().add(workSchedule);
	    }
	}
	setBeginDate(employeeScheduleFactory.getBeginDate());
	setEndDate(employeeScheduleFactory.getEndDate());
	setModifiedBy(employeeScheduleFactory.getModifiedBy());
	setLastModifiedDate(new DateTime());
    }

    private WorkSchedule getWorkSchedule(WorkScheduleType choosenWorkSchedule, WorkWeek workWeek, Periodicity periodicity) {
	if (workWeek.getDays().isEmpty()) {
	    return null;
	}
	for (WorkSchedule workSchedule : RootDomainObject.getInstance().getWorkSchedules()) {
	    if (workSchedule.getWorkScheduleType().getAcronym().equals(choosenWorkSchedule.getAcronym())
		    && workSchedule.getWorkWeek().equals(workWeek) && workSchedule.getPeriodicity() == periodicity) {
		return workSchedule;
	    }
	}
	return new WorkSchedule(choosenWorkSchedule, workWeek, periodicity);
    }

    private void updateWorkSchedules(WorkWeek workWeek, Integer weekNumber) {
	List<WorkSchedule> workScheduleToRemove = new ArrayList<WorkSchedule>();
	List<WorkSchedule> workScheduleListToAdd = new ArrayList<WorkSchedule>();
	for (WorkSchedule workSchedule : getWorkSchedules()) {
	    if (workSchedule.getPeriodicity().getWorkWeekNumber().equals(weekNumber)) {
		WorkWeek newWorkWeek = getWorkWeekByRemovingOverLayedDays(workSchedule.getWorkWeek(), workWeek);
		if (!workSchedule.getWorkWeek().equals(newWorkWeek)) {
		    workScheduleToRemove.add(workSchedule);
		    WorkSchedule workScheduleToAdd = getWorkSchedule(workSchedule.getWorkScheduleType(), newWorkWeek,
			    workSchedule.getPeriodicity());
		    if (workScheduleToAdd != null) {
			workScheduleListToAdd.add(workScheduleToAdd);
		    }
		}
	    }
	}
	getWorkSchedules().removeAll(workScheduleToRemove);
	for (WorkSchedule workSchedule : workScheduleToRemove) {
	    workSchedule.delete();
	}
	if (!workScheduleListToAdd.isEmpty()) {
	    getWorkSchedules().addAll(workScheduleListToAdd);
	}
    }

    private WorkWeek getWorkWeekByRemovingOverLayedDays(WorkWeek workScheduleWorkWeek, WorkWeek workWeek) {
	WorkWeek newWorkWeek = new WorkWeek(workScheduleWorkWeek.getDays());
	for (WeekDay weekDay : workWeek.getDays()) {
	    newWorkWeek.getDays().remove(weekDay);
	}
	return newWorkWeek;
    }

    private boolean isCloseMonthInsideScheduleInterval(ClosedMonth closedMonth, LocalDate date) {
	LocalDate closedMonthEndDate = closedMonth.getClosedMonthLastDay();
	if (!closedMonthEndDate.isBefore(date)) {
	    return true;
	}
	return false;
    }

    private boolean isCloseMonthInsideInterval(ClosedMonth closedMonth, LocalDate beginDate, LocalDate endDate) {
	Partial beginPartial = new Partial().with(DateTimeFieldType.year(), beginDate.getYear()).with(
		DateTimeFieldType.monthOfYear(), beginDate.getMonthOfYear());
	Partial endPartial = null;
	if (endDate != null) {
	    endPartial = new Partial().with(DateTimeFieldType.year(), endDate.getYear()).with(DateTimeFieldType.monthOfYear(),
		    endDate.getMonthOfYear());
	}
	if (!closedMonth.getClosedYearMonth().isBefore(beginPartial)
		&& (endPartial == null || !closedMonth.getClosedYearMonth().isAfter(endPartial))) {
	    return true;
	}
	return false;
    }

    private Periodicity getPeriodicity(Integer workWeekNumber) {
	for (Periodicity periodicity : RootDomainObject.getInstance().getPeriodicities()) {
	    if (periodicity.getWorkWeekNumber().equals(workWeekNumber)) {
		return periodicity;
	    }
	}
	return new Periodicity(workWeekNumber);
    }

    private Interval getValidInterval() {
	DateTime endDateTime = null;
	if (getEndDate() != null) {
	    endDateTime = getEndDate().plusDays(1).toDateTimeAtStartOfDay();
	}
	return new Interval(getBeginDate().toDateTimeAtStartOfDay(), endDateTime);
    }

    // Return true if the Schedule is valid in the interval
    // public boolean isDefinedInInterval(Interval interval) {
    // if (getEndDate() != null) {
    // return interval.getEnd() == null ?
    // (!interval.getStart().isAfter(getBeginDate().toDateTimeAtStartOfDay()))
    // : getValidInterval().overlaps(interval);
    // }
    // return interval.getEnd() == null ? true :
    // interval.contains(getBeginDate().toDateTimeAtStartOfDay())
    // || getBeginDate().isBefore(interval.getStart().toLocalDate());
    // }

    public boolean isDefinedInInterval(LocalDate beginDate, LocalDate endDate) {
	DateTime beginDateTime = beginDate.toDateTimeAtStartOfDay();

	if (endDate == null) {
	    return getEndDate() != null ? (!beginDateTime.isAfter(getBeginDate().toDateTimeAtStartOfDay())) : true;
	} else {
	    Interval interval = new Interval(beginDateTime, endDate.toDateTimeAtStartOfDay());
	    return getEndDate() != null ? getValidInterval().overlaps(interval) : (interval.contains(getBeginDate()
		    .toDateTimeAtStartOfDay()) || getBeginDate().isBefore(interval.getStart().toLocalDate()));
	}
    }

    // Return true if the Schedule valid interval constains date
    public boolean isDefinedInDate(LocalDate date) {
	if (getEndDate() != null) {
	    return getValidInterval().contains(date.toDateTimeAtStartOfDay());
	}
	return !date.isBefore(getBeginDate());
    }

    // Returns the valid interval week number of a given YearMonthDay date
    public int getValidIntervalWeekNumberOfDate(LocalDate date) {
	return new Interval(getBeginDate().toDateTimeAtStartOfDay(), date.plusDays(1).toDateTimeAtStartOfDay()).toPeriod(
		PeriodType.weeks()).getWeeks();
    }

    // Returns the Employee's work schedule for a particular date
    public WorkSchedule workScheduleWithDate(LocalDate date) {
	int weekNumber = getValidIntervalWeekNumberOfDate(date);
	int maxWorkWeek = getMaximumWorkWeekNumber();
	for (WorkSchedule workSchedule : getWorkSchedules()) {
	    if (workSchedule.isDefinedInDate(date, weekNumber, maxWorkWeek)) {
		return workSchedule;
	    }
	}
	return null;
    }

    public int getMaximumWorkWeekNumber() {
	int maxWorkWeek = 0;
	for (WorkSchedule workSchedule : getWorkSchedules()) {
	    if (workSchedule.getPeriodicity().getWorkWeekNumber().intValue() > maxWorkWeek) {
		maxWorkWeek = workSchedule.getPeriodicity().getWorkWeekNumber().intValue();
	    }
	}
	return maxWorkWeek;
    }

    public boolean hasFixedPeriod() {
	for (WorkSchedule workSchedule : getWorkSchedulesSet()) {
	    if (workSchedule.getWorkScheduleType().getFixedWorkPeriod() != null) {
		return true;
	    }
	}
	return false;
    }

    public Duration getEqualWorkPeriodDuration() {
	Duration workPeriodDuration = Duration.ZERO;
	for (WorkSchedule workSchedule : getWorkSchedules()) {
	    if (workPeriodDuration.equals(Duration.ZERO)) {
		workPeriodDuration = workSchedule.getWorkScheduleType().getNormalWorkPeriod().getWorkPeriodDuration();
	    } else if (!workSchedule.getWorkScheduleType().getNormalWorkPeriod().getWorkPeriodDuration().equals(
		    workPeriodDuration)) {
		return Duration.ZERO;
	    }
	}
	return workPeriodDuration;
    }

    public Duration getAverageWorkPeriodDuration() {
	Map<Integer, Duration> weekDurations = new HashMap<Integer, Duration>();
	for (WorkSchedule workSchedule : getWorkSchedules()) {
	    Duration workScheduleTypeWorkDuration = workSchedule.getWorkScheduleType().getNormalWorkPeriod()
		    .getWorkPeriodDuration();
	    Duration weekDuration = weekDurations.get(workSchedule.getPeriodicity().getWorkWeekNumber());
	    if (weekDuration != null) {
		weekDuration = weekDuration.plus(workScheduleTypeWorkDuration.getMillis()
			* workSchedule.getWorkWeek().getDays().size());
		weekDurations.put(workSchedule.getPeriodicity().getWorkWeekNumber(), weekDuration);
	    } else {
		weekDurations.put(workSchedule.getPeriodicity().getWorkWeekNumber(), new Duration(workScheduleTypeWorkDuration
			.getMillis()
			* workSchedule.getWorkWeek().getDays().size()));
	    }
	}
	long average = 0;
	for (Duration weekDuration : weekDurations.values()) {
	    if (average < weekDuration.getMillis() / 5) {
		average = weekDuration.getMillis() / 5;
	    }
	}
	return new Duration(average);
    }

    private boolean startsBeforeClosedMonth(ClosedMonth closedMonth, LocalDate date) {
	LocalDate beginClosedMonth = new LocalDate(closedMonth.getClosedYearMonth().get(DateTimeFieldType.year()), closedMonth
		.getClosedYearMonth().get(DateTimeFieldType.monthOfYear()), 1);
	LocalDate endClosedMonth = new LocalDate(beginClosedMonth.getYear(), beginClosedMonth.getMonthOfYear(), beginClosedMonth
		.dayOfMonth().getMaximumValue());
	if (date.isBefore(endClosedMonth.plusDays(1))) {
	    return true;
	}
	return false;
    }

    public boolean getIsEditable() {
	ClosedMonth closedMonth = ClosedMonth.getLastMonthClosed();
	LocalDate endClosedMonth = closedMonth.getClosedMonthLastDay();
	if (getBeginDate().isBefore(endClosedMonth) && (getEndDate() != null && getEndDate().isBefore(endClosedMonth))) {
	    return false;
	}
	return true;
    }

    public boolean getCanChangeBeginDate() {
	ClosedMonth closedMonth = ClosedMonth.getLastMonthClosed();
	LocalDate endClosedMonth = closedMonth.getClosedMonthLastDay();
	if (getBeginDate().isBefore(endClosedMonth.plusDays(1))) {
	    return false;
	}
	return true;
    }

    public boolean getIsDeletable() {
	ClosedMonth closedMonth = ClosedMonth.getLastMonthClosed();
	LocalDate endClosedMonth = closedMonth.getClosedMonthLastDay();
	if (getBeginDate().isBefore(endClosedMonth.plusDays(1))) {
	    return true;
	}
	return false;
    }

    public void delete() {
	removeRootDomainObject();
	removeAssiduousness();
	removeModifiedBy();
	List<WorkSchedule> workSchedules = new ArrayList<WorkSchedule>(getWorkSchedules());
	for (WorkSchedule workSchedule : workSchedules) {
	    getWorkSchedules().remove(workSchedule);
	    workSchedule.delete();
	}
	deleteDomainObject();
    }
}
