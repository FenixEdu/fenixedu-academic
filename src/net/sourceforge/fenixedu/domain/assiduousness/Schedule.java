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
	setRootDomainObject(RootDomainObject.getInstance());
	setAssiduousness(assiduousness);
	setBeginDate(beginDate);
	setEndDate(endDate);
	setException(false);
	setLastModifiedDate(lastModifiedDate);
	setModifiedBy(modifiedBy);
    }

    public Schedule(Assiduousness assiduousness, LocalDate beginDate, LocalDate endDate, Boolean exception,
	    DateTime lastModifiedDate, Employee modifiedBy) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setAssiduousness(assiduousness);
	setBeginDate(beginDate);
	setEndDate(endDate);
	setException(exception);
	setLastModifiedDate(lastModifiedDate);
	setModifiedBy(modifiedBy);
    }

    public Schedule(EmployeeScheduleFactory employeeScheduleFactory, boolean deletedDays) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setAssiduousness(employeeScheduleFactory.getEmployee().getAssiduousness());
	setBeginDate(employeeScheduleFactory.getBeginDate());
	setEndDate(employeeScheduleFactory.getEndDate());
	setModifiedBy(employeeScheduleFactory.getModifiedBy());
	setLastModifiedDate(new DateTime());
	setException(false);
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
	setAssiduousness(employeeScheduleFactory.getEmployee().getAssiduousness());
	setBeginDate(employeeScheduleFactory.getBeginDate());
	setEndDate(employeeScheduleFactory.getEndDate());
	ClosedMonth closedMonth = ClosedMonth.getLastMonthClosed();
	if (isCloseMonthInsideScheduleInterval(closedMonth)) {
	    Month month = Month.values()[closedMonth.getClosedYearMonth().get(DateTimeFieldType.monthOfYear()) - 1];
	    throw new DomainException("error.schedule.monthClose", ResourceBundle.getBundle("resources.EnumerationResources",
		    Language.getLocale()).getString(month.name()), ((Integer) closedMonth.getClosedYearMonth().get(
		    DateTimeFieldType.year())).toString());
	}
	setRootDomainObject(RootDomainObject.getInstance());
	setModifiedBy(employeeScheduleFactory.getModifiedBy());
	setLastModifiedDate(new DateTime());
	setException(false);
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

    // creates a new Exception Schedule
    public Schedule(EmployeeExceptionScheduleBean employeeExceptionScheduleBean) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setAssiduousness(employeeExceptionScheduleBean.getEmployee().getAssiduousness());
	setBeginDate(employeeExceptionScheduleBean.getBeginDate());
	setEndDate(employeeExceptionScheduleBean.getEndDate());
	ClosedMonth closedMonth = ClosedMonth.getLastMonthClosed();
	if (startsBeforeClosedMonth(closedMonth)) {
	    Month month = Month.values()[closedMonth.getClosedYearMonth().get(DateTimeFieldType.monthOfYear()) - 1];
	    throw new DomainException("error.schedule.monthClose", ResourceBundle.getBundle("resources.EnumerationResources",
		    Language.getLocale()).getString(month.name()), ((Integer) closedMonth.getClosedYearMonth().get(
		    DateTimeFieldType.year())).toString());
	}
	if (overLapsAnotherExceptionSchedule()) {
	    throw new DomainException("error.schedule.overlapsAnotherException");
	}
	setException(Boolean.TRUE);
	setModifiedBy(employeeExceptionScheduleBean.getModifiedBy());
	setLastModifiedDate(new DateTime());
	addWorkScheduleForExceptionSchedule(employeeExceptionScheduleBean);
    }

    public void editException(EmployeeExceptionScheduleBean employeeExceptionScheduleBean) {
	setBeginDate(employeeExceptionScheduleBean.getBeginDate());
	setEndDate(employeeExceptionScheduleBean.getEndDate());
	ClosedMonth closedMonth = ClosedMonth.getLastMonthClosed();
	if (startsBeforeClosedMonth(closedMonth)) {
	    Month month = Month.values()[closedMonth.getClosedYearMonth().get(DateTimeFieldType.monthOfYear()) - 1];
	    throw new DomainException("error.schedule.monthClose", ResourceBundle.getBundle("resources.EnumerationResources",
		    Language.getLocale()).getString(month.name()), ((Integer) closedMonth.getClosedYearMonth().get(
		    DateTimeFieldType.year())).toString());
	}
	if (overLapsAnotherExceptionSchedule()) {
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

    private boolean overLapsAnotherExceptionSchedule() {
	for (Schedule schedule : getAssiduousness().getSchedules()) {
	    if (schedule != this && schedule.getException() && schedule.isDefinedInInterval(getValidInterval())) {
		return true;
	    }
	}
	return false;
    }

    public Schedule deleteDays(EmployeeScheduleFactory employeeScheduleFactory) {
	ClosedMonth closedMonth = ClosedMonth.getLastMonthClosed();
	if (isCloseMonthInsideScheduleInterval(closedMonth)) {
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
	    firtWorkSchedule.setPeriodicity(getPeriodicity(new Integer(correctWorkWeek)));
	}
	while (iterator.hasNext()) {
	    WorkSchedule workSchedule = iterator.next();
	    int currentWorkWeek = workSchedule.getPeriodicity().getWorkWeekNumber().intValue();
	    if (currentWorkWeek == previousWorkWeek) {
		workSchedule.setPeriodicity(getPeriodicity(new Integer(correctWorkWeek)));
	    } else {
		correctWorkWeek++;
		previousWorkWeek = currentWorkWeek;
		workSchedule.setPeriodicity(getPeriodicity(new Integer(correctWorkWeek)));
	    }
	}
    }

    public Schedule edit(EmployeeScheduleFactory employeeScheduleFactory) {
	ClosedMonth closedMonth = ClosedMonth.getLastMonthClosed();
	if (isCloseMonthInsideScheduleInterval(closedMonth)) {
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

    private boolean isCloseMonthInsideScheduleInterval(ClosedMonth closedMonth) {
	Partial beginPartial = new Partial().with(DateTimeFieldType.year(), getBeginDate().getYear()).with(
		DateTimeFieldType.monthOfYear(), getBeginDate().getMonthOfYear());
	Partial endPartial = null;
	if (getEndDate() != null) {
	    endPartial = new Partial().with(DateTimeFieldType.year(), getEndDate().getYear()).with(
		    DateTimeFieldType.monthOfYear(), getEndDate().getMonthOfYear());
	}
	if (!closedMonth.getClosedYearMonth().isBefore(beginPartial)
		&& (endPartial == null || !closedMonth.getClosedYearMonth().isAfter(endPartial))) {
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
    public boolean isDefinedInInterval(Interval interval) {
	if (getEndDate() != null) {
	    return getValidInterval().overlaps(interval);
	}
	return interval.contains(getBeginDate().toDateTimeAtStartOfDay())
		|| getBeginDate().isBefore(interval.getStart().toLocalDate());
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

    private boolean startsBeforeClosedMonth(ClosedMonth closedMonth) {
	LocalDate beginClosedMonth = new LocalDate(closedMonth.getClosedYearMonth().get(DateTimeFieldType.year()), closedMonth
		.getClosedYearMonth().get(DateTimeFieldType.monthOfYear()), 1);
	LocalDate endClosedMonth = new LocalDate(beginClosedMonth.getYear(), beginClosedMonth.getMonthOfYear(), beginClosedMonth
		.dayOfMonth().getMaximumValue());
	if (getBeginDate().isBefore(endClosedMonth.plusDays(1))) {
	    return true;
	}
	return false;
    }

    public boolean getIsEditable() {
	ClosedMonth closedMonth = ClosedMonth.getLastMonthClosed();
	LocalDate beginClosedMonth = new LocalDate(closedMonth.getClosedYearMonth().get(DateTimeFieldType.year()), closedMonth
		.getClosedYearMonth().get(DateTimeFieldType.monthOfYear()), 1);
	LocalDate endClosedMonth = new LocalDate(beginClosedMonth.getYear(), beginClosedMonth.getMonthOfYear(), beginClosedMonth
		.dayOfMonth().getMaximumValue());
	if (getBeginDate().isBefore(endClosedMonth.plusDays(1))
		&& (getEndDate() != null && getEndDate().isBefore(endClosedMonth.plusDays(1)))) {
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
