package net.sourceforge.fenixedu.dataTransferObject.assiduousness;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.Anulation;
import net.sourceforge.fenixedu.domain.assiduousness.Assiduousness;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessRecord;
import net.sourceforge.fenixedu.domain.assiduousness.ClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.Justification;
import net.sourceforge.fenixedu.domain.assiduousness.JustificationMotive;
import net.sourceforge.fenixedu.domain.assiduousness.Leave;
import net.sourceforge.fenixedu.domain.assiduousness.MissingClocking;
import net.sourceforge.fenixedu.domain.assiduousness.Schedule;
import net.sourceforge.fenixedu.domain.assiduousness.WorkSchedule;
import net.sourceforge.fenixedu.domain.assiduousness.WorkWeek;
import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationType;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import net.sourceforge.fenixedu.util.LanguageUtils;

import org.apache.struts.action.ActionMessage;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Duration;
import org.joda.time.DurationFieldType;
import org.joda.time.Partial;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;
import org.joda.time.chrono.GregorianChronology;

public abstract class EmployeeJustificationFactory implements Serializable, FactoryExecutor {

    public static Duration dayDuration = new Duration(86400000); // 24

    // hours

    public enum CorrectionType {
        JUSTIFICATION, REGULARIZATION;
    }

    private CorrectionType correctionType;

    private YearMonthDay date;

    private DomainReference<Employee> employee;

    private JustificationType justificationType;

    private DomainReference<JustificationMotive> justificationMotive;

    private WorkWeek aplicableWeekDays;

    private YearMonthDay beginDate;

    private YearMonthDay endDate;

    private TimeOfDay beginTime;

    private TimeOfDay endTime;

    private DomainReference<Employee> modifiedBy;

    public static class EmployeeJustificationFactoryCreator extends EmployeeJustificationFactory {

        public EmployeeJustificationFactoryCreator(Employee employee, YearMonthDay date,
                CorrectionType correctionType) {
            setEmployee(employee);
            setDate(date);
            setBeginDate(date);
            setCorrectionType(correctionType);
        }

        public Object execute() {
            ResourceBundle bundle = ResourceBundle.getBundle("resources.AssiduousnessResources",
                    LanguageUtils.getLocale());
            if (getCorrectionType() != null && getCorrectionType().equals(CorrectionType.JUSTIFICATION)) {
                if (getJustificationMotive() != null
                        && getJustificationMotive().getJustificationType() != null) {
                    final GregorianChronology gregorianChronology = GregorianChronology.getInstanceUTC();
                    if (getJustificationMotive().getJustificationType().equals(JustificationType.TIME)) {
                        if (getBeginDate() == null) {
                            return new ActionMessage("errors.required", bundle.getString("label.date"));
                        }
                        if (ClosedMonth.isMonthClosed(getBeginDate())) {
                            return new ActionMessage("errors.datesInClosedMonth");
                        }
                        if (getBeginTime() == null) {
                            return new ActionMessage("errors.required", bundle
                                    .getString("label.beginHour"));
                        }
                        if (getEndTime() == null) {
                            return new ActionMessage("errors.required", bundle
                                    .getString("label.endHour"));
                        }
                        if (!getBeginTime().isBefore(getEndTime())) {
                            return new ActionMessage("error.invalidTimeInterval");
                        }
                        ActionMessage actionMessage = canInsertTimeJustification();
                        if (actionMessage != null) {
                            return actionMessage;
                        }
                        new Leave(getEmployee().getAssiduousness(), getBeginDate().toDateTime(
                                getBeginTime()), getDuration(getBeginTime(), getEndTime()),
                                getJustificationMotive(), null, null, new DateTime(), getModifiedBy());
                    } else if (getJustificationMotive().getJustificationType().equals(
                            JustificationType.OCCURRENCE)) {
                        if (getBeginDate() == null) {
                            return new ActionMessage("errors.required", bundle
                                    .getString("label.beginDate"));
                        }

                        Duration duration = Duration.ZERO;
                        if (getEndDate() != null) {
                            if (getBeginDate().isAfter(getEndDate())) {
                                return new ActionMessage("error.invalidDateInterval");
                            }
                            duration = getDuration(getBeginDate(), getEndDate());
                        }
                        if (isDateIntervalInClosedMonth(getBeginDate(), duration)) {
                            return new ActionMessage("errors.datesInClosedMonth");
                        }

                        if (!hasScheduleAndActive(getBeginDate(), duration)) {
                            return new ActionMessage("errors.employeeHasNoScheduleOrInactive");
                        }
                        if (isOverlapingOtherJustification(duration,null)) {
                            return new ActionMessage("error.overlapingOtherJustification");
                        }
                        new Leave(getEmployee().getAssiduousness(), getBeginDate().toDateTime(
                                new TimeOfDay(0, 0, 0, gregorianChronology)), duration,
                                getJustificationMotive(), null, null, new DateTime(), getModifiedBy());
                    } else if (getJustificationMotive().getJustificationType().equals(
                            JustificationType.BALANCE)) {
                        if (getBeginDate() == null) {
                            return new ActionMessage("errors.required", bundle.getString("label.date"));
                        }
                        if (ClosedMonth.isMonthClosed(getBeginDate())) {
                            return new ActionMessage("errors.datesInClosedMonth");
                        }
                        if (!hasScheduleAndActive(getBeginDate(), dayDuration)) {
                            return new ActionMessage("errors.employeeHasNoScheduleOrInactive");
                        }
                        Duration numberOfHours = getDuration(TimeOfDay.MIDNIGHT, getBeginTime());
                        if (numberOfHours == null) {
                            return new ActionMessage("errors.required", bundle
                                    .getString("label.hoursNumber"));
                        }
                        if (!numberOfHours.isShorterThan(dayDuration)) {
                            return new ActionMessage("errors.numberOfHoursLongerThanDay");
                        }
                        new Leave(getEmployee().getAssiduousness(), getBeginDate().toDateTime(
                                new TimeOfDay(0, 0, 0, gregorianChronology)), numberOfHours,
                                getJustificationMotive(), null, null, new DateTime(), getModifiedBy());
                    } else {
                        // error - regul
                        return new ActionMessage("errors.error");
                    }
                } else {
                    return new ActionMessage("errors.required", bundle.getString("label.justification"));
                }
            } else if (getCorrectionType() != null
                    && getCorrectionType().equals(CorrectionType.REGULARIZATION)) {
                if (getJustificationMotive() != null) {
                    if (getJustificationMotive().getJustificationType() == null) {
                        if (getBeginDate() == null) {
                            return new ActionMessage("errors.required", bundle.getString("label.date"));
                        }
                        if (ClosedMonth.isMonthClosed(getBeginDate())) {
                            return new ActionMessage("errors.datesInClosedMonth");
                        }
                        if (getBeginTime() == null) {
                            return new ActionMessage("errors.required", bundle.getString("label.hour"));
                        }

                        if (isOverlapingAnyTimeJustification()) {
                            return new ActionMessage("errors.regularizationInsideTimeJustification");
                        }
                        new MissingClocking(getEmployee().getAssiduousness(), getBeginDate().toDateTime(
                                getBeginTime()), getJustificationMotive(), new DateTime(),
                                getModifiedBy());
                    } else {
                        // erro - just
                        return new ActionMessage("errors.error");
                    }
                } else {
                    return new ActionMessage("errors.required", bundle.getString("label.regularization"));
                }
            }
            return null;
        }
    }

    public static class EmployeeJustificationFactoryEditor extends EmployeeJustificationFactory {
        private DomainReference<Justification> justification;

        public EmployeeJustificationFactoryEditor(Justification justification) {
            setJustification(justification);
            setEmployee(justification.getAssiduousness().getEmployee());
            setBeginDate(justification.getDate().toYearMonthDay());
            setJustificationMotive(justification.getJustificationMotive());
            if (justification instanceof Leave) {
                setCorrectionType(CorrectionType.JUSTIFICATION);
                setJustificationType(justification.getJustificationMotive().getJustificationType());
                setBeginTime(((Leave) justification).getDate().toTimeOfDay());
                setEndDate(((Leave) justification).getEndYearMonthDay());
                setEndTime(((Leave) justification).getEndTimeOfDay());

            } else {
                setCorrectionType(CorrectionType.REGULARIZATION);
                setBeginTime(justification.getDate().toTimeOfDay());
            }
        }

        public Object execute() {
            ResourceBundle bundle = ResourceBundle.getBundle("resources.AssiduousnessResources",
                    LanguageUtils.getLocale());
            if (getCorrectionType() != null && getCorrectionType().equals(CorrectionType.JUSTIFICATION)) {
                if (getJustificationMotive() != null) {
                    final GregorianChronology gregorianChronology = GregorianChronology.getInstanceUTC();
                    if (getJustificationMotive().getJustificationType().equals(JustificationType.TIME)) {
                        if (getBeginDate() == null) {
                            return new ActionMessage("errors.required", bundle.getString("label.date"));
                        }
                        if (ClosedMonth.isMonthClosed(getBeginDate())) {
                            return new ActionMessage("errors.datesInClosedMonth");
                        }
                        if (getBeginTime() == null) {
                            return new ActionMessage("errors.required", bundle
                                    .getString("label.beginHour"));
                        }
                        if (getEndTime() == null) {
                            return new ActionMessage("errors.required", bundle
                                    .getString("label.endHour"));
                        }
                        if (!getBeginTime().isBefore(getEndTime())) {
                            return new ActionMessage("error.invalidTimeInterval");
                        }
                        ActionMessage actionMessage = canInsertTimeJustification();
                        if (actionMessage != null) {
                            return actionMessage;
                        }

                        ((Leave) getJustification()).modify(getBeginDate().toDateTime(getBeginTime()),
                                getDuration(getBeginTime(), getEndTime()), getJustificationMotive(),
                                null, null, getModifiedBy());
                    } else if (getJustificationMotive().getJustificationType().equals(
                            JustificationType.OCCURRENCE)) {
                        if (getBeginDate() == null) {
                            return new ActionMessage("errors.required", bundle
                                    .getString("label.beginDate"));
                        }
                        Duration duration = Duration.ZERO;
                        if (getEndDate() != null) {
                            if (getBeginDate().isAfter(getEndDate())) {
                                return new ActionMessage("error.invalidDateInterval");
                            }
                            duration = getDuration(getBeginDate(), getEndDate());
                        }
                        if (isDateIntervalInClosedMonth(getBeginDate(), duration)) {
                            return new ActionMessage("errors.datesInClosedMonth");
                        }
                        if (!hasScheduleAndActive(getBeginDate(), duration)) {
                            return new ActionMessage("errors.employeeHasNoScheduleOrInactive");
                        }
                        if (isOverlapingOtherJustification(duration, getJustification())) {
                            return new ActionMessage("error.overlapingOtherJustification");
                        }
                        ((Leave) getJustification()).modify(getBeginDate().toDateTime(
                                new TimeOfDay(0, 0, 0, gregorianChronology)), duration,
                                getJustificationMotive(), null, null, getModifiedBy());
                    } else if (getJustificationMotive().getJustificationType().equals(
                            JustificationType.BALANCE)) {
                        if (getBeginDate() == null) {
                            return new ActionMessage("errors.required", bundle.getString("label.date"));
                        }
                        if (ClosedMonth.isMonthClosed(getBeginDate())) {
                            return new ActionMessage("errors.datesInClosedMonth");
                        }
                        if (!hasScheduleAndActive(getBeginDate(), dayDuration)) {
                            return new ActionMessage("errors.employeeHasNoScheduleOrInactive");
                        }
                        Duration numberOfHours = getDuration(TimeOfDay.MIDNIGHT, getBeginTime());
                        if (numberOfHours == null) {
                            return new ActionMessage("errors.required", bundle
                                    .getString("label.hoursNumber"));
                        }
                        if (!numberOfHours.isShorterThan(dayDuration)) {
                            return new ActionMessage("errors.numberOfHoursLongerThanDay");
                        }
                        ((Leave) getJustification()).modify(getBeginDate().toDateTime(
                                new TimeOfDay(0, 0, 0, gregorianChronology)), numberOfHours,
                                getJustificationMotive(), null, null, getModifiedBy());
                    } else {
                        // error - regul
                        return new ActionMessage("errors.error");
                    }
                } else {
                    return new ActionMessage("errors.required", bundle.getString("label.justification"));
                }
            } else if (getCorrectionType() != null
                    && getCorrectionType().equals(CorrectionType.REGULARIZATION)) {
                if (getJustificationMotive() != null) {
                    if (getJustificationMotive().getJustificationType() == null) {
                        if (getBeginDate() == null) {
                            return new ActionMessage("errors.required", bundle.getString("label.date"));
                        }
                        if (ClosedMonth.isMonthClosed(getBeginDate())) {
                            return new ActionMessage("errors.datesInClosedMonth");
                        }
                        if (getBeginTime() == null) {
                            return new ActionMessage("errors.required", bundle.getString("label.hour"));
                        }

                        if (isOverlapingAnyTimeJustification()) {
                            return new ActionMessage("errors.regularizationInsideTimeJustification");
                        }
                        ((MissingClocking) getJustification()).modify(getBeginDate().toDateTime(
                                getBeginTime()), getJustificationMotive(), getModifiedBy());
                    } else {
                        // erro - just
                        return new ActionMessage("errors.error");
                    }
                } else {
                    return new ActionMessage("errors.required", bundle.getString("label.regularization"));
                }

            }
            return null;
        }

        public Justification getJustification() {
            return justification == null ? null : justification.getObject();
        }

        public void setJustification(Justification justification) {
            if (justification != null) {
                this.justification = new DomainReference<Justification>(justification);
            }
        }
    }

    public static class EmployeeAnulateJustificationFactory extends EmployeeJustificationFactory {
        private DomainReference<Justification> justification;

        public EmployeeAnulateJustificationFactory(Justification justification, Employee employee) {
            setJustification(justification);
            setModifiedBy(employee);
        }

        public Object execute() {
            if (getJustification().getJustificationMotive().getJustificationType() != null
                    && getJustification().getJustificationMotive().getJustificationType().equals(
                            JustificationType.OCCURRENCE)) {
                if (isDateIntervalInClosedMonth(getJustification().getDate().toYearMonthDay(),
                        ((Leave) getJustification()).getDuration())) {
                    return new ActionMessage("errors.cantDelete.datesInClosedMonth");
                }
            } else {
                if (ClosedMonth.isMonthClosed(getJustification().getDate().toYearMonthDay())) {
                    return new ActionMessage("errors.cantDelete.datesInClosedMonth");
                }
            }
            new Anulation(getJustification(), getModifiedBy());
            return null;
        }

        public Justification getJustification() {
            return justification == null ? null : justification.getObject();
        }

        public void setJustification(Justification justification) {
            if (justification != null) {
                this.justification = new DomainReference<Justification>(justification);
            }
        }
    }

    protected Duration getDuration(YearMonthDay begin, YearMonthDay end) {
        if (begin == null || end == null) {
            return null;
        }
        return new Duration(begin.toDateMidnight(), end.toDateMidnight());
    }

    protected boolean isDateIntervalInClosedMonth(YearMonthDay day, Duration duration) {
        YearMonthDay endDate = (day.plus(duration.toPeriod()));
        Partial end = new Partial().with(DateTimeFieldType.monthOfYear(), endDate.getMonthOfYear())
                .with(DateTimeFieldType.year(), endDate.getYear());
        for (Partial yearMonth = new Partial().with(DateTimeFieldType.monthOfYear(),
                day.getMonthOfYear()).with(DateTimeFieldType.year(), day.getYear()); !yearMonth
                .isAfter(end); yearMonth = yearMonth.withFieldAdded(DurationFieldType.months(), 1)) {
            if (ClosedMonth.isMonthClosed(yearMonth)) {
                return true;
            }
        }
        return false;
    }

    protected boolean isOverlapingAnyTimeJustification() {
        final List<Leave> leaves = getEmployee().getAssiduousness().getLeaves(getBeginDate(),
                getBeginDate());
        if (!leaves.isEmpty()) {
            for (Leave leave : leaves) {
                if (leave.getJustificationMotive().getJustificationType().equals(
                        JustificationType.OCCURRENCE)) {
                    return true;
                } else if (leave.getJustificationMotive().getJustificationType().equals(
                        JustificationType.TIME)
                        && leave.getTotalInterval().contains(getBeginDate().toDateTime(getBeginTime()))) {
                    return true;
                }
            }
        }
        return false;
    }

    protected ActionMessage canInsertTimeJustification() {
        if (getBeginDate().equals(new YearMonthDay())) {
            return new ActionMessage("errors.cannotInsertTimeJustificationsInCurrentDay");
        }
        YearMonthDay lowerBeginDate = getBeginDate().minusDays(8);
        HashMap<YearMonthDay, WorkSchedule> workScheduleMap = new HashMap<YearMonthDay, WorkSchedule>();
        for (YearMonthDay thisDay = lowerBeginDate; thisDay.isBefore(getBeginDate().plusDays(1)); thisDay = thisDay
                .plusDays(1)) {
            final Schedule schedule = getEmployee().getAssiduousness().getSchedule(thisDay);
            if (schedule != null) {
                workScheduleMap.put(thisDay, schedule.workScheduleWithDate(thisDay));
            } else {
                workScheduleMap.put(thisDay, null);
            }
        }

        if (workScheduleMap.get(getBeginDate()) == null
                || !getEmployee().getAssiduousness().isStatusActive(getBeginDate(), getBeginDate())) {
            return new ActionMessage("errors.employeeHasNoScheduleOrInactive");
        }

        DateTime init = lowerBeginDate.toDateTime(Assiduousness.defaultStartWorkDay);
        WorkSchedule beginWorkSchedule = workScheduleMap.get(lowerBeginDate);
        if (beginWorkSchedule != null) {
            init = lowerBeginDate.toDateTime(beginWorkSchedule.getWorkScheduleType().getWorkTime());
        }

        DateTime end = getBeginDate().toDateTime(Assiduousness.defaultEndWorkDay);
        WorkSchedule endWorkSchedule = workScheduleMap.get(getBeginDate());
        if (endWorkSchedule != null) {
            end = getBeginDate().toDateTime(endWorkSchedule.getWorkScheduleType().getWorkTime()).plus(
                    endWorkSchedule.getWorkScheduleType().getWorkTimeDuration());
            if (endWorkSchedule.getWorkScheduleType().isWorkTimeNextDay()) {
                end = end.plusDays(2);
            }
        }

        final List<AssiduousnessRecord> clockings = getEmployee().getAssiduousness()
                .getClockingsAndMissingClockings(init.minusDays(1), end);
        Collections.sort(clockings, AssiduousnessRecord.COMPARATORY_BY_DATE);
        HashMap<YearMonthDay, List<AssiduousnessRecord>> clockingsMap = new HashMap<YearMonthDay, List<AssiduousnessRecord>>();
        for (AssiduousnessRecord record : clockings) {
            YearMonthDay clockDay = record.getDate().toYearMonthDay();
            if (WorkSchedule.overlapsSchedule(record.getDate(), workScheduleMap)) {
                if (clockingsMap.get(clockDay.minusDays(1)) != null
                        && clockingsMap.get(clockDay.minusDays(1)).size() % 2 != 0) {
                    clockDay = clockDay.minusDays(1);
                }
            }

            List<AssiduousnessRecord> clocks = clockingsMap.get(clockDay);
            if (clocks == null) {
                clocks = new ArrayList<AssiduousnessRecord>();
            }
            clocks.add(record);
            clockingsMap.put(clockDay, clocks);
        }

        List<AssiduousnessRecord> clocks = clockingsMap.get(getBeginDate());
        if (clocks != null && clocks.size() % 2 != 0) {
            return new ActionMessage("errors.irregularClockings");
        }
        if (clocks != null) {
            Collections.sort(clocks, AssiduousnessRecord.COMPARATORY_BY_DATE);
            int before = 0;
            int middle = 0;
            for (AssiduousnessRecord assiduousnessRecord : clocks) {
		if (!assiduousnessRecord.getDateWithoutSeconds().toTimeOfDay().isAfter(getBeginTime())) {
                    before++;
		} else if (assiduousnessRecord.getDateWithoutSeconds().toTimeOfDay().isBefore(
			getEndTime())) {
                    middle++;
                } else {
                    break;
                }
            }
            if (before % 2 != 0 || middle != 0) {
                return new ActionMessage("errors.timeJustificationOverlapsWorkPeriod");
            }
        }
        return null;
    }

    protected boolean hasScheduleAndActive(YearMonthDay day, Duration duration) {
        if (!getEmployee().getAssiduousness().isStatusActive(day, day.plus(duration.toPeriod()))) {
            return false;
        }
        for (YearMonthDay thisday = day; !thisday.isAfter(day.plus(duration.toPeriod())); thisday = thisday
                .plusDays(1)) {
            Schedule schedule = getEmployee().getAssiduousness().getSchedule(thisday);
            if (schedule != null) {
                if (schedule.workScheduleWithDate(thisday) != null) {
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean isOverlapingOtherJustification(Duration duration,Justification justitication) {
        List<Leave> leaves = getEmployee().getAssiduousness().getLeaves(getBeginDate(),
                getBeginDate().toDateTimeAtMidnight().plus(duration).toYearMonthDay());
        return !leaves.isEmpty() && (justitication == null || !leaves.contains(justitication));
    }

    protected Duration getDuration(TimeOfDay begin, TimeOfDay end) {
        if (begin == null || end == null) {
            return null;
        }
        YearMonthDay now = new YearMonthDay();
        return new Duration(now.toDateTime(begin), now.toDateTime(end));
    }

    protected Duration getDuration(TimeOfDay time) {
        if (time == null) {
            return null;
        }
        return new Duration(time.toDateTimeToday().getMillis());
    }

    public List<JustificationMotive> getJustificationMotivesList() {
        List<JustificationMotive> justificationMotives = new ArrayList<JustificationMotive>();
        if (correctionType.equals(CorrectionType.JUSTIFICATION) && getJustificationType() != null) {
            for (JustificationMotive justificationMotive : RootDomainObject.getInstance()
                    .getJustificationMotives()) {
                if (justificationMotive.getJustificationType() != null
                        && justificationMotive.getJustificationType().equals(getJustificationType())) {
                    justificationMotives.add(justificationMotive);
                }
            }
        } else if (correctionType.equals(CorrectionType.REGULARIZATION)) {
            for (JustificationMotive justificationMotive : RootDomainObject.getInstance()
                    .getJustificationMotives()) {
                if (justificationMotive.getJustificationType() == null) {
                    justificationMotives.add(justificationMotive);
                }
            }
        }
        return justificationMotives;
    }

    public boolean isComplete() {
        if (getJustificationType() != null && getJustificationMotive() != null && getBeginDate() != null) {
            return true;
        }
        return false;
    }

    public YearMonthDay getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(YearMonthDay beginDate) {
        this.beginDate = beginDate;
    }

    public TimeOfDay getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(TimeOfDay beginTime) {
        this.beginTime = beginTime;
    }

    public YearMonthDay getEndDate() {
        return endDate;
    }

    public void setEndDate(YearMonthDay endDate) {
        this.endDate = endDate;
    }

    public TimeOfDay getEndTime() {
        return endTime;
    }

    public void setEndTime(TimeOfDay endTime) {
        this.endTime = endTime;
    }

    public JustificationMotive getJustificationMotive() {
        return justificationMotive == null ? null : justificationMotive.getObject();
    }

    public void setJustificationMotive(JustificationMotive justificationMotive) {
        if (justificationMotive != null) {
            this.justificationMotive = new DomainReference<JustificationMotive>(justificationMotive);
        }
    }

    public JustificationType getJustificationType() {
        return justificationType;
    }

    public void setJustificationType(JustificationType justificationType) {
        this.justificationType = justificationType;
    }

    public YearMonthDay getDate() {
        return date;
    }

    public void setDate(YearMonthDay date) {
        this.date = date;
    }

    public Employee getEmployee() {
        return employee == null ? null : employee.getObject();
    }

    public void setEmployee(Employee employee) {
        if (employee != null) {
            this.employee = new DomainReference<Employee>(employee);
        }
    }

    public Employee getModifiedBy() {
        return modifiedBy == null ? null : modifiedBy.getObject();
    }

    public void setModifiedBy(Employee modifiedBy) {
        if (modifiedBy != null) {
            this.modifiedBy = new DomainReference<Employee>(modifiedBy);
        }
    }

    public CorrectionType getCorrectionType() {
        return correctionType;
    }

    public void setCorrectionType(CorrectionType correctionType) {
        this.correctionType = correctionType;
    }

    public WorkWeek getAplicableWeekDays() {
        return aplicableWeekDays;
    }

    public void setAplicableWeekDays(WorkWeek aplicableWeekDays) {
        this.aplicableWeekDays = aplicableWeekDays;
    }

}