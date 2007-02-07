package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeScheduleFactory;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeWorkWeekScheduleBean;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.util.DateInterval;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.Month;

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Duration;
import org.joda.time.Partial;
import org.joda.time.YearMonthDay;

public class Schedule extends Schedule_Base {

    public Schedule(Assiduousness assiduousness, YearMonthDay beginDate, YearMonthDay endDate,
            DateTime lastModifiedDate, Employee modifiedBy) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setAssiduousness(assiduousness);
        setBeginDate(beginDate);
        setEndDate(endDate);
        setException(false);
        setLastModifiedDate(lastModifiedDate);
        setModifiedBy(modifiedBy);
    }

    public Schedule(Assiduousness assiduousness, YearMonthDay beginDate, YearMonthDay endDate,
            Boolean exception, DateTime lastModifiedDate, Employee modifiedBy) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setAssiduousness(assiduousness);
        setBeginDate(beginDate);
        setEndDate(endDate);
        setException(exception);
        setLastModifiedDate(lastModifiedDate);
        setModifiedBy(modifiedBy);
    }

    public Schedule(EmployeeScheduleFactory employeeScheduleFactory, boolean workWeekByCheckedBox) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setAssiduousness(employeeScheduleFactory.getEmployee().getAssiduousness());
        setBeginDate(employeeScheduleFactory.getBeginDate());
        setEndDate(employeeScheduleFactory.getEndDate());
        setModifiedBy(employeeScheduleFactory.getModifiedBy());
        setLastModifiedDate(new DateTime());
        setException(false);
        for (EmployeeWorkWeekScheduleBean workWeekScheduleBean : employeeScheduleFactory
                .getEmployeeWorkWeekScheduleList()) {
            WorkWeek workWeek = null;
            Periodicity periodicity = getPeriodicity(workWeekScheduleBean.getWorkWeekNumber());
            if (workWeekByCheckedBox) {
                workWeek = workWeekScheduleBean.getWorkWeekByCheckedBox();
                WorkSchedule workSchedule = new WorkSchedule(employeeScheduleFactory
                        .getChoosenWorkSchedule(), workWeek, periodicity);
                getWorkSchedules().add(workSchedule);
            } else {
                getWorkSchedules().addAll(workWeekScheduleBean.getWorkSchedules(periodicity));
            }
        }
    }

    public Schedule(EmployeeScheduleFactory employeeScheduleFactory) {
        super();
        setAssiduousness(employeeScheduleFactory.getEmployee().getAssiduousness());
        setBeginDate(employeeScheduleFactory.getBeginDate());
        setEndDate(employeeScheduleFactory.getEndDate());
        ClosedMonth closedMonth = ClosedMonth.getLastMonthClosed();
        if (isCloseMonthInsideScheduleInterval(closedMonth) && closedMonth.getClosedForBalance()) {
            Month month = Month.values()[closedMonth.getClosedYearMonth().get(
                    DateTimeFieldType.monthOfYear()) - 1];
            throw new DomainException("error.schedule.monthClose",
                    ResourceBundle
                            .getBundle("resources.EnumerationResources", LanguageUtils.getLocale())
                            .getString(month.name()), ((Integer) closedMonth.getClosedYearMonth().get(
                            DateTimeFieldType.year())).toString());
        }
        setRootDomainObject(RootDomainObject.getInstance());
        setModifiedBy(employeeScheduleFactory.getModifiedBy());
        setLastModifiedDate(new DateTime());
        setException(false);
        for (EmployeeWorkWeekScheduleBean workWeekScheduleBean : employeeScheduleFactory
                .getEmployeeWorkWeekScheduleList()) {
            WorkWeek workWeek = workWeekScheduleBean.getWorkWeekByCheckedBox();
            if (workWeek != null) {
                Periodicity periodicity = getPeriodicity(workWeekScheduleBean.getWorkWeekNumber());
                WorkSchedule workSchedule = new WorkSchedule(employeeScheduleFactory
                        .getChoosenWorkSchedule(), workWeek, periodicity);
                getWorkSchedules().add(workSchedule);
            }
        }
    }

    public void deleteDays(EmployeeScheduleFactory employeeScheduleFactory) {
        ClosedMonth closedMonth = ClosedMonth.getLastMonthClosed();
        if (isCloseMonthInsideScheduleInterval(closedMonth) && closedMonth.getClosedForBalance()) {
            closeScheduleAndMakeNew(employeeScheduleFactory, closedMonth, false);
        } else {
            if (isCloseMonthInsideInterval(closedMonth, employeeScheduleFactory.getBeginDate(),
                    employeeScheduleFactory.getEndDate())
                    && closedMonth.getClosedForBalance()) {
                Month month = Month.values()[closedMonth.getClosedYearMonth().get(
                        DateTimeFieldType.monthOfYear()) - 1];
                throw new DomainException("error.schedule.monthClose", ResourceBundle.getBundle(
                        "resources.EnumerationResources", LanguageUtils.getLocale()).getString(
                        month.name()), ((Integer) closedMonth.getClosedYearMonth().get(
                        DateTimeFieldType.year())).toString());
            }
            eliminateDays(employeeScheduleFactory);
        }
    }

    private void eliminateDays(EmployeeScheduleFactory employeeScheduleFactory) {
        for (EmployeeWorkWeekScheduleBean workWeekScheduleBean : employeeScheduleFactory
                .getEmployeeWorkWeekScheduleList()) {
            WorkWeek workWeek = workWeekScheduleBean.getWorkWeekByCheckedBox();
            if (workWeek != null) {
                adjustExistingWorkSchedules(workWeek, workWeekScheduleBean.getWorkWeekNumber());
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

    public void edit(EmployeeScheduleFactory employeeScheduleFactory) {
        ClosedMonth closedMonth = ClosedMonth.getLastMonthClosed();
        if (isCloseMonthInsideScheduleInterval(closedMonth) && closedMonth.getClosedForBalance()) {
            closeScheduleAndMakeNew(employeeScheduleFactory, closedMonth, true);
        } else {
            if (isCloseMonthInsideInterval(closedMonth, employeeScheduleFactory.getBeginDate(),
                    employeeScheduleFactory.getEndDate())
                    && closedMonth.getClosedForBalance()) {
                Month month = Month.values()[closedMonth.getClosedYearMonth().get(
                        DateTimeFieldType.monthOfYear()) - 1];
                throw new DomainException("error.schedule.monthClose", ResourceBundle.getBundle(
                        "resources.EnumerationResources", LanguageUtils.getLocale()).getString(
                        month.name()), ((Integer) closedMonth.getClosedYearMonth().get(
                        DateTimeFieldType.year())).toString());
            }
            if (employeeScheduleFactory.isDifferencesInDates()
                    && !employeeScheduleFactory.isDifferencesInWorkSchedules()) {
                setBeginDate(employeeScheduleFactory.getBeginDate());
                setEndDate(employeeScheduleFactory.getEndDate());
                setModifiedBy(employeeScheduleFactory.getModifiedBy());
                setLastModifiedDate(new DateTime());
            } else {
                updateEverything(employeeScheduleFactory);
            }
        }
    }

    private void closeScheduleAndMakeNew(EmployeeScheduleFactory employeeScheduleFactory,
            ClosedMonth closedMonth, boolean workWeekByCheckedBox) {
        YearMonthDay endDate = new YearMonthDay(closedMonth.getClosedYearMonth().get(
                DateTimeFieldType.year()), closedMonth.getClosedYearMonth().get(
                DateTimeFieldType.monthOfYear()), 1);
        endDate = endDate.plusDays(endDate.dayOfMonth().getMaximumValue()).minusDays(1);
        setModifiedBy(employeeScheduleFactory.getModifiedBy());
        setLastModifiedDate(new DateTime());
        if (endDate.plusDays(1).isBefore(employeeScheduleFactory.getBeginDate())) {
            setEndDate(employeeScheduleFactory.getBeginDate().minusDays(1));
        } else {
            employeeScheduleFactory.setBeginDate(endDate.plusDays(1));
            setEndDate(endDate);
        }
        new Schedule(employeeScheduleFactory, workWeekByCheckedBox);
    }

    private void updateEverything(EmployeeScheduleFactory employeeScheduleFactory) {
        int subtract = 0;
        for (EmployeeWorkWeekScheduleBean workWeekScheduleBean : employeeScheduleFactory
                .getEmployeeWorkWeekScheduleList()) {
            WorkWeek workWeek = workWeekScheduleBean.getWorkWeekByCheckedBox();
            if (workWeek != null) {
                int workWeekNumber = workWeekScheduleBean.getWorkWeekNumber() - subtract;
                adjustExistingWorkSchedules(workWeek, workWeekNumber);
                Periodicity periodicity = getPeriodicity(workWeekNumber);
                WorkSchedule workSchedule = getWorkSchedule(employeeScheduleFactory
                        .getChoosenWorkSchedule(), workWeek, periodicity);
                getWorkSchedules().add(workSchedule);
            } else if (workWeekScheduleBean.getIsEmptyWeek()) {
                subtract = 1;
            }
        }
        setBeginDate(employeeScheduleFactory.getBeginDate());
        setEndDate(employeeScheduleFactory.getEndDate());
        setModifiedBy(employeeScheduleFactory.getModifiedBy());
        setLastModifiedDate(new DateTime());
    }

    private WorkSchedule getWorkSchedule(WorkScheduleType choosenWorkSchedule, WorkWeek workWeek,
            Periodicity periodicity) {
        for (WorkSchedule workSchedule : RootDomainObject.getInstance().getWorkSchedules()) {
            if (workSchedule.getWorkScheduleType().getAcronym().equals(choosenWorkSchedule.getAcronym())
                    && workSchedule.getWorkWeek().equals(workWeek)
                    && workSchedule.getPeriodicity() == periodicity) {
                return workSchedule;
            }
        }
        return new WorkSchedule(choosenWorkSchedule, workWeek, periodicity);
    }

    private void adjustExistingWorkSchedules(WorkWeek workWeek, Integer weekNumber) {
        List<WorkSchedule> workScheduleToRemove = new ArrayList<WorkSchedule>();
        for (WorkSchedule workSchedule : getWorkSchedules()) {
            if (workSchedule.getPeriodicity().getWorkWeekNumber().equals(weekNumber)) {
                workSchedule.getWorkWeek().removeOverLayedDays(workWeek);
                if (workSchedule.getWorkWeek().getDays().size() == 0) {
                    workScheduleToRemove.add(workSchedule);
                }
            }
        }
        getWorkSchedules().removeAll(workScheduleToRemove);
        for (WorkSchedule workSchedule : workScheduleToRemove) {
            workSchedule.delete();
        }
    }

    private boolean isCloseMonthInsideScheduleInterval(ClosedMonth closedMonth) {
        Partial beginPartial = new Partial().with(DateTimeFieldType.year(), getBeginDate().getYear())
                .with(DateTimeFieldType.monthOfYear(), getBeginDate().getMonthOfYear());
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

    private boolean isCloseMonthInsideInterval(ClosedMonth closedMonth, YearMonthDay beginDate,
            YearMonthDay endDate) {
        Partial beginPartial = new Partial().with(DateTimeFieldType.year(), beginDate.getYear()).with(
                DateTimeFieldType.monthOfYear(), beginDate.getMonthOfYear());
        Partial endPartial = null;
        if (endDate != null) {
            endPartial = new Partial().with(DateTimeFieldType.year(), endDate.getYear()).with(
                    DateTimeFieldType.monthOfYear(), endDate.getMonthOfYear());
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

    public DateInterval getValidInterval() {
        return new DateInterval(getBeginDate(), getEndDate());
    }

    // Return true if the Schedule is valid in the interval
    public boolean isDefinedInInterval(DateInterval interval) {
        return getValidInterval().containsInterval(interval);
    }

    // Return true if the Schedule valid interval constains date
    public boolean isDefinedInDate(YearMonthDay date) {
        return getValidInterval().containsDate(date);
    }

    // Returns the valid interval week number of a given YearMonthDay date
    public int getValidIntervalWeekNumberOfDate(YearMonthDay date) {
        return (new DateInterval(getBeginDate(), date)).numberOfWeeks() + 1;
    }

    // Returns the Employee's work schedule for a particular date
    public WorkSchedule workScheduleWithDate(YearMonthDay date) {
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
                workPeriodDuration = workSchedule.getWorkScheduleType().getNormalWorkPeriod()
                        .getWorkPeriodDuration();
            } else if (!workSchedule.getWorkScheduleType().getNormalWorkPeriod().getWorkPeriodDuration()
                    .equals(workPeriodDuration)) {
                return Duration.ZERO;
            }
        }
        return workPeriodDuration;
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
