package net.sourceforge.fenixedu.domain.assiduousness;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.dataTransferObject.assiduousness.YearMonth;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Holiday;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.util.AssiduousnessState;
import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationType;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.EmployeeContract;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.GiafInterface;
import net.sourceforge.fenixedu.util.Month;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.util.Region;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.Partial;
import org.joda.time.Period;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.excel.StyledExcelSpreadsheet;

public class Assiduousness extends Assiduousness_Base {

    public static final LocalTime defaultStartWorkDay = new LocalTime(7, 30, 0, 0);

    public static final LocalTime defaultEndWorkDay = new LocalTime(23, 59, 59, 99);

    public static final LocalTime defaultStartWeeklyRestDay = new LocalTime(7, 0, 0, 0);

    public static final LocalTime defaultStartNightWorkDay = new LocalTime(20, 0, 0, 0);

    public static final LocalTime defaultEndNightWorkDay = new LocalTime(7, 0, 0, 0);

    public static final Duration normalWorkDayDuration = new Duration(25200000); // 7

    // hours

    public static final Duration IST_TOLERANCE_TIME = new Duration(3540000); // 59

    // minutes

    public static final int MAX_A66_PER_MONTH = 2;

    public static final int MAX_A66_PER_YEAR = 13;

    public static final double nightExtraWorkPercentage = 0.25;

    public static final double firstHourPercentage = 1.25;

    public static final double secondHourPercentage = 1.50;

    public static final double weeklyRestPercentage = 2;

    public Assiduousness(Employee employee) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setEmployee(employee);
    }

    public Schedule getCurrentSchedule() {
	LocalDate today = new LocalDate();
	for (final Schedule schedule : getSchedules()) {
	    if (!schedule.getException()) {
		if (schedule.getEndDate() == null) {
		    if (schedule.getBeginDate().isBefore(today) || schedule.getBeginDate().isEqual(today)) {
			return schedule;
		    }
		} else {
		    Interval interval = new Interval(schedule.getBeginDate().toDateTimeAtStartOfDay(), schedule.getEndDate()
			    .plusDays(1).toDateTimeAtStartOfDay());
		    if (interval.contains(today.toDateTimeAtStartOfDay())) {
			return schedule;
		    }
		}
	    }
	}
	return null;
    }

    public Schedule getLastSchedule() {
	Schedule lastSchedule = null;
	for (final Schedule schedule : getSchedules()) {
	    if (!schedule.getException()) {
		if (schedule.getEndDate() == null) {
		    return schedule;
		} else {
		    if (lastSchedule == null || schedule.getEndDate().isAfter(lastSchedule.getEndDate())) {
			lastSchedule = schedule;
		    }
		}
	    }
	}
	return lastSchedule;
    }

    public Schedule getSchedule(LocalDate date) {
	List<Schedule> scheduleList = new ArrayList<Schedule>();
	for (Schedule schedule : getSchedules()) {
	    if (schedule.isDefinedInDate(date)) {
		scheduleList.add(schedule);
	    }
	}
	if (scheduleList.size() == 1) {
	    return scheduleList.iterator().next();
	} else {
	    // if there are more than one, it's beacause there was an
	    // exception schedule in that specified date
	    for (Schedule schedule : scheduleList) {
		if (schedule.getException()) {
		    return schedule;
		}
	    }
	    return null;
	}
    }

    public List<Schedule> getSchedules(LocalDate beginDate, LocalDate endDate) {
	List<Schedule> scheduleList = new ArrayList<Schedule>();
	for (Schedule schedule : getSchedules()) {
	    if (schedule.isDefinedInInterval(beginDate, endDate.plusDays(1))) {
		scheduleList.add(schedule);
	    }
	}
	return scheduleList;
    }

    public HashMap<LocalDate, WorkSchedule> getWorkSchedulesBetweenDates(Partial partial) {
	LocalDate beginDate = new LocalDate(partial.get(DateTimeFieldType.year()), partial.get(DateTimeFieldType.monthOfYear()),
		1);
	LocalDate endDate = new LocalDate(partial.get(DateTimeFieldType.year()), partial.get(DateTimeFieldType.monthOfYear()),
		beginDate.dayOfMonth().getMaximumValue());
	return getWorkSchedulesBetweenDates(beginDate, endDate);
    }

    public HashMap<LocalDate, WorkSchedule> getWorkSchedulesBetweenDates(LocalDate beginDate, LocalDate endDate) {
	HashMap<LocalDate, WorkSchedule> workScheduleMap = new HashMap<LocalDate, WorkSchedule>();
	for (LocalDate thisDay = beginDate; thisDay.isBefore(endDate.plusDays(1)); thisDay = thisDay.plusDays(1)) {
	    final Schedule schedule = getSchedule(thisDay);
	    if (schedule != null) {
		workScheduleMap.put(thisDay, schedule.workScheduleWithDate(thisDay));
	    } else {
		workScheduleMap.put(thisDay, null);
	    }
	}
	return workScheduleMap;
    }

    public HashMap<LocalDate, List<AssiduousnessRecord>> getClockingsMap(HashMap<LocalDate, WorkSchedule> workScheduleMap,
	    DateTime init, DateTime end) {
	HashMap<LocalDate, List<AssiduousnessRecord>> clockingsMap = new HashMap<LocalDate, List<AssiduousnessRecord>>();
	final List<AssiduousnessRecord> clockings = getClockingsAndMissingClockings(init.minusDays(1), end);
	Collections.sort(clockings, AssiduousnessRecord.COMPARATOR_BY_DATE);
	for (AssiduousnessRecord record : clockings) {
	    LocalDate clockDay = record.getDate().toLocalDate();
	    int overlapsSchedule = WorkSchedule.overlapsSchedule(record.getDate(), workScheduleMap);
	    if (overlapsSchedule == 0) {
		if (clockingsMap.get(clockDay.minusDays(1)) != null && clockingsMap.get(clockDay.minusDays(1)).size() % 2 != 0) {
		    clockDay = clockDay.minusDays(1);
		}
	    } else if (overlapsSchedule < 0) {
		clockDay = clockDay.minusDays(1);
	    }

	    List<AssiduousnessRecord> clocks = clockingsMap.get(clockDay);
	    if (clocks == null) {
		clocks = new ArrayList<AssiduousnessRecord>();
	    }
	    clocks.add(record);
	    clockingsMap.put(clockDay, clocks);
	}
	return clockingsMap;
    }

    public HashMap<LocalDate, List<Leave>> getLeavesMap(LocalDate beginDate, LocalDate endDate) {
	HashMap<LocalDate, List<Leave>> leavesMap = new HashMap<LocalDate, List<Leave>>();
	final List<Leave> leaves = getLeaves(beginDate, endDate);
	for (Leave record : leaves) {
	    LocalDate endLeaveDay = record.getDate().toLocalDate().plusDays(1);
	    if (record.getEndLocalDate() != null) {
		endLeaveDay = record.getEndLocalDate().plusDays(1);
	    }
	    for (LocalDate leaveDay = record.getDate().toLocalDate(); leaveDay.isBefore(endLeaveDay); leaveDay = leaveDay
		    .plusDays(1)) {
		if (record.getAplicableWeekDays() == null
			|| record.getAplicableWeekDays().contains(leaveDay.toDateTimeAtStartOfDay())) {
		    List<Leave> leaveList = leavesMap.get(leaveDay);
		    if (leaveList == null) {
			leaveList = new ArrayList<Leave>();
		    }
		    leaveList.add(record);
		    leavesMap.put(leaveDay, leaveList);
		}
	    }
	}
	return leavesMap;
    }

    public List<Leave> getLeavesByType(List<Leave> leaves, JustificationType justificationType) {
	List<Leave> leavesByType = new ArrayList<Leave>();
	for (Leave leave : leaves) {
	    if (leave.getJustificationMotive().getJustificationType() == justificationType) {
		leavesByType.add(leave);
	    }
	}
	return leavesByType;
    }

    public List<Leave> getLeaves(LocalDate day) {
	List<Leave> leaves = new ArrayList<Leave>();
	final AssiduousnessRecordMonthIndex assiduousnessRecordMonthIndex = getAssiduousnessRecordMonthIndex(day);
	if (assiduousnessRecordMonthIndex != null) {
	    for (AssiduousnessRecord assiduousnessRecord : assiduousnessRecordMonthIndex.getAssiduousnessRecordsSet()) {
		if (assiduousnessRecord.isLeave() && !assiduousnessRecord.isAnulated()) {
		    Leave leave = (Leave) assiduousnessRecord;
		    if (leave.occuredInDate(day)) {
			leaves.add(leave);
		    }
		}
	    }
	}
	return leaves;
    }

    public AssiduousnessRecordMonthIndex getAssiduousnessRecordMonthIndex(LocalDate localDate) {
	for (final AssiduousnessRecordMonthIndex assiduousnessRecordMonthIndex : getAssiduousnessRecordMonthIndexsSet()) {
	    if (assiduousnessRecordMonthIndex.contains(localDate)) {
		return assiduousnessRecordMonthIndex;
	    }
	}
	return null;
    }

    public AssiduousnessRecordMonthIndex getAssiduousnessRecordMonthIndex(Partial partial) {
	for (final AssiduousnessRecordMonthIndex assiduousnessRecordMonthIndex : getAssiduousnessRecordMonthIndexsSet()) {
	    if (assiduousnessRecordMonthIndex.contains(partial)) {
		return assiduousnessRecordMonthIndex;
	    }
	}
	return null;
    }

    public List<AssiduousnessRecord> getAssiduousnessRecordBetweenDates(DateTime beginDate, DateTime endDate) {
	final List<AssiduousnessRecord> assiduousnessRecords = new ArrayList<AssiduousnessRecord>();
	for (final AssiduousnessRecordMonthIndex assiduousnessRecordMonthIndex : getAssiduousnessRecordMonthIndexsSet()) {
	    if (assiduousnessRecordMonthIndex.intersects(beginDate, endDate)) {
		assiduousnessRecords.addAll(assiduousnessRecordMonthIndex.getAssiduousnessRecordsSet());
	    }
	}
	return assiduousnessRecords;
    }

    public List<AssiduousnessRecord> getClockingsAndMissingClockings(DateTime beginDate, DateTime endDate) {
	final Interval interval = new Interval(beginDate, endDate);
	final List<AssiduousnessRecord> assiduousnessRecords = getAssiduousnessRecordBetweenDates(beginDate, endDate);
	for (final Iterator<AssiduousnessRecord> iterator = assiduousnessRecords.iterator(); iterator.hasNext();) {
	    final AssiduousnessRecord assiduousnessRecord = iterator.next();
	    if ((!assiduousnessRecord.isClocking() && !assiduousnessRecord.isMissingClocking())
		    || assiduousnessRecord.isAnulated() || !interval.contains(assiduousnessRecord.getDate())) {
		iterator.remove();
	    }
	}
	return assiduousnessRecords;
    }

    public List<Clocking> getClockings(LocalDate beginDate, LocalDate endDate) {
	Interval interval = new Interval(beginDate.toDateTimeAtStartOfDay(), endDate.toDateTimeAtStartOfDay().plusDays(1));
	final List<AssiduousnessRecord> assiduousnessRecords = getAssiduousnessRecordBetweenDates(interval.getStart(),
		interval.getEnd());
	List<Clocking> clockingsList = new ArrayList<Clocking>();
	for (AssiduousnessRecord assiduousnessRecord : assiduousnessRecords) {
	    if (assiduousnessRecord.isClocking() && !assiduousnessRecord.isAnulated()
		    && interval.contains(assiduousnessRecord.getDate())) {
		clockingsList.add((Clocking) assiduousnessRecord);
	    }
	}
	return clockingsList;
    }

    public List<Clocking> getClockingsAndAnulatedClockings(LocalDate beginDate, LocalDate endDate) {
	Interval interval = new Interval(beginDate.toDateTimeAtStartOfDay(), endDate.toDateTimeAtStartOfDay().plusDays(1));
	final List<AssiduousnessRecord> assiduousnessRecords = getAssiduousnessRecordBetweenDates(interval.getStart(),
		interval.getEnd());
	List<Clocking> clockingsList = new ArrayList<Clocking>();
	for (AssiduousnessRecord assiduousnessRecord : assiduousnessRecords) {
	    if (assiduousnessRecord.isClocking() && interval.contains(assiduousnessRecord.getDate())) {
		if (!assiduousnessRecord.isAnulated()) {
		    clockingsList.add((Clocking) assiduousnessRecord);
		} else {
		    clockingsList.add((Clocking) assiduousnessRecord.getAnulation().getAnulatedAssiduousnessRecord());
		}
	    }
	}
	return clockingsList;
    }

    public List<Leave> getLeavesByYear(int year) {
	return getLeaves(new LocalDate(year, 1, 1), new LocalDate(year, 12, 31));
    }

    public List<Leave> getLeaves(LocalDate beginDate, LocalDate endDate) {
	final DateTime beginDateTime = beginDate.toDateTimeAtStartOfDay();
	final DateTime endDateTime = endDate.toDateTime(defaultEndWorkDay);
	Interval interval = new Interval(beginDateTime, endDateTime);
	final List<Leave> leavesList = new ArrayList<Leave>();
	final List<AssiduousnessRecord> assiduousnessRecords = getAssiduousnessRecordBetweenDates(beginDateTime, endDateTime);
	for (final AssiduousnessRecord assiduousnessRecord : assiduousnessRecords) {
	    if (assiduousnessRecord.isLeave() && !assiduousnessRecord.isAnulated()) {
		final Leave leave = (Leave) assiduousnessRecord;
		Interval leaveInterval = new Interval(leave.getDate(), leave.getEndDate().plusSeconds(1));
		if (leaveInterval.overlaps(interval) && !leavesList.contains(leave)) {
		    leavesList.add(leave);
		}
	    }
	}
	return leavesList;
    }

    public List<Leave> getLeavesByType(LocalDate beginDate, LocalDate endDate, JustificationType justificationType) {
	List<Leave> leavesList = new ArrayList<Leave>();
	for (Leave leave : getLeaves(beginDate, endDate)) {
	    if (leave.getJustificationMotive().getJustificationType().equals(justificationType)) {
		leavesList.add(leave);
	    }
	}
	return leavesList;
    }

    public List<MissingClocking> getMissingClockings(LocalDate beginDate, LocalDate endDate) {
	final DateTime beginDateTime = beginDate.toDateTimeAtStartOfDay();
	final DateTime endDateTime = endDate.toDateTime(defaultEndWorkDay);
	Interval interval = new Interval(beginDateTime, endDateTime);
	final List<AssiduousnessRecord> assiduousnessRecords = getAssiduousnessRecordBetweenDates(interval.getStart(),
		interval.getEnd());
	List<MissingClocking> missingClockingsList = new ArrayList<MissingClocking>();
	for (AssiduousnessRecord assiduousnessRecord : assiduousnessRecords) {
	    if (assiduousnessRecord.isMissingClocking() && interval.contains(assiduousnessRecord.getDate())
		    && (!assiduousnessRecord.isAnulated())) {
		missingClockingsList.add((MissingClocking) assiduousnessRecord);
	    }
	}
	return missingClockingsList;
    }

    public boolean isHoliday(LocalDate thisDay) {
	Campus campus = getAssiduousnessCampus(thisDay);
	return Holiday.isHoliday(thisDay, campus);
    }

    private Campus getAssiduousnessCampus(LocalDate thisDay) {
	for (AssiduousnessCampusHistory assiduousnessCampusHistory : getAssiduousnessCampusHistories()) {
	    if (assiduousnessCampusHistory.getEndDate() != null) {
		Interval interval = new Interval(assiduousnessCampusHistory.getBeginDate().toDateTimeAtStartOfDay(),
			assiduousnessCampusHistory.getEndDate().toDateTimeAtStartOfDay().plusDays(1));
		if (interval.contains(thisDay.toDateTimeAtStartOfDay())) {
		    return assiduousnessCampusHistory.getCampus();
		}
	    } else if (!assiduousnessCampusHistory.getBeginDate().isAfter(thisDay)) {
		return assiduousnessCampusHistory.getCampus();
	    }
	}
	return null;
    }

    public LocalDate getLastActiveStatusBetween(LocalDate beginDate, LocalDate endDate) {
	LocalDate lastActiveStatus = null;
	for (AssiduousnessStatusHistory assiduousnessStatusHistory : getAssiduousnessStatusHistories()) {
	    if (assiduousnessStatusHistory.getEndDate() != null) {
		Interval statusInterval = new Interval(assiduousnessStatusHistory.getBeginDate().toDateMidnight(),
			assiduousnessStatusHistory.getEndDate().toDateMidnight().plusDays(1));
		Interval interval = new Interval(beginDate.toDateTimeAtStartOfDay(), endDate.toDateTimeAtStartOfDay().plusDays(1));
		if (interval.overlaps(statusInterval)
			&& assiduousnessStatusHistory.getAssiduousnessStatus().getState() == AssiduousnessState.ACTIVE) {
		    if (lastActiveStatus == null || !lastActiveStatus.isAfter(assiduousnessStatusHistory.getEndDate())) {
			lastActiveStatus = assiduousnessStatusHistory.getEndDate();
		    }
		}
	    } else {
		if ((assiduousnessStatusHistory.getBeginDate().isBefore(endDate) || assiduousnessStatusHistory.getBeginDate()
			.isEqual(endDate))
			&& assiduousnessStatusHistory.getAssiduousnessStatus().getState() == AssiduousnessState.ACTIVE) {
		    lastActiveStatus = endDate;
		}
	    }

	}
	if (lastActiveStatus != null && lastActiveStatus.isAfter(endDate)) {
	    lastActiveStatus = endDate;
	}
	return lastActiveStatus;
    }

    public AssiduousnessStatus getLastAssiduousnessStatusBetween(LocalDate beginDate, LocalDate endDate) {
	AssiduousnessStatusHistory lastActiveStatus = getLastAssiduousnessStatusHistoryBetween(beginDate, endDate);
	return lastActiveStatus == null ? null : lastActiveStatus.getAssiduousnessStatus();
    }

    public AssiduousnessStatusHistory getLastAssiduousnessStatusHistoryBetween(LocalDate beginDate, LocalDate endDate) {
	AssiduousnessStatusHistory lastActiveStatus = null;
	for (AssiduousnessStatusHistory assiduousnessStatusHistory : getAssiduousnessStatusHistories()) {
	    if (assiduousnessStatusHistory.getEndDate() != null) {
		Interval statusInterval = new Interval(assiduousnessStatusHistory.getBeginDate().toDateMidnight(),
			assiduousnessStatusHistory.getEndDate().toDateMidnight().plusDays(1));
		Interval interval = new Interval(beginDate.toDateTimeAtStartOfDay(), endDate.toDateTimeAtStartOfDay().plusDays(1));
		if (interval.overlaps(statusInterval)
			&& assiduousnessStatusHistory.getAssiduousnessStatus().getState() == AssiduousnessState.ACTIVE) {
		    if (lastActiveStatus == null
			    || !lastActiveStatus.getEndDate().isAfter(assiduousnessStatusHistory.getEndDate())) {
			lastActiveStatus = assiduousnessStatusHistory;
		    }
		}
	    } else {
		if ((assiduousnessStatusHistory.getBeginDate().isBefore(endDate) || assiduousnessStatusHistory.getBeginDate()
			.isEqual(endDate))
			&& assiduousnessStatusHistory.getAssiduousnessStatus().getState() == AssiduousnessState.ACTIVE) {
		    return assiduousnessStatusHistory;
		}
	    }

	}
	return lastActiveStatus;
    }

    public AssiduousnessStatusHistory getCurrentOrLastAssiduousnessStatusHistory() {
	LocalDate today = new LocalDate();
	AssiduousnessStatusHistory lastActiveStatus = null;
	for (AssiduousnessStatusHistory assiduousnessStatusHistory : getAssiduousnessStatusHistories()) {
	    if (assiduousnessStatusHistory.getEndDate() != null
		    && assiduousnessStatusHistory.getAssiduousnessStatus().getState() == AssiduousnessState.ACTIVE) {
		Interval statusInterval = new Interval(assiduousnessStatusHistory.getBeginDate().toDateMidnight(),
			assiduousnessStatusHistory.getEndDate().toDateMidnight().plusDays(1));
		if (statusInterval.containsNow()) {
		    return assiduousnessStatusHistory;
		} else {
		    if (lastActiveStatus == null
			    || !lastActiveStatus.getEndDate().isAfter(assiduousnessStatusHistory.getEndDate())) {
			lastActiveStatus = assiduousnessStatusHistory;
		    }

		}
	    } else {
		if ((!assiduousnessStatusHistory.getBeginDate().isAfter(today))
			&& assiduousnessStatusHistory.getAssiduousnessStatus().getState() == AssiduousnessState.ACTIVE) {
		    return assiduousnessStatusHistory;
		}
	    }

	}
	return lastActiveStatus;
    }

    public AssiduousnessStatusHistory getLastAssiduousnessStatusHistory() {
	AssiduousnessStatusHistory lastActiveStatus = null;
	for (AssiduousnessStatusHistory assiduousnessStatusHistory : getAssiduousnessStatusHistories()) {
	    if (assiduousnessStatusHistory.getEndDate() != null) {
		if (lastActiveStatus == null || !lastActiveStatus.getEndDate().isAfter(assiduousnessStatusHistory.getEndDate())) {
		    lastActiveStatus = assiduousnessStatusHistory;
		}
	    } else {
		return assiduousnessStatusHistory;
	    }
	}
	return lastActiveStatus;
    }

    public List<AssiduousnessStatusHistory> getStatusBetween(LocalDate beginDate, LocalDate endDate) {
	List<AssiduousnessStatusHistory> assiduousnessStatusList = new ArrayList<AssiduousnessStatusHistory>();
	for (AssiduousnessStatusHistory assiduousnessStatusHistory : getAssiduousnessStatusHistories()) {
	    if (assiduousnessStatusHistory.getEndDate() != null) {
		Interval statusInterval = new Interval(assiduousnessStatusHistory.getBeginDate().toDateTimeAtStartOfDay(),
			assiduousnessStatusHistory.getEndDate().toDateTimeAtStartOfDay().plusDays(1));
		Interval interval = new Interval(beginDate.toDateTimeAtStartOfDay(), endDate.toDateTimeAtStartOfDay().plusDays(1));
		if (interval.overlaps(statusInterval)) {
		    assiduousnessStatusList.add(assiduousnessStatusHistory);
		}
	    } else {
		if (assiduousnessStatusHistory.getBeginDate().isBefore(endDate)
			|| assiduousnessStatusHistory.getBeginDate().isEqual(endDate)) {
		    assiduousnessStatusList.add(assiduousnessStatusHistory);
		}
	    }
	}
	return assiduousnessStatusList;
    }

    public AssiduousnessStatus getCurrentStatus() {
	LocalDate today = new LocalDate();
	List<AssiduousnessStatusHistory> result = getStatusBetween(today, today);
	return result == null || result.isEmpty() ? null : result.get(0).getAssiduousnessStatus();
    }

    public boolean isStatusActive(LocalDate beginDate, LocalDate endDate) {
	for (AssiduousnessStatusHistory assiduousnessStatusHistory : getAssiduousnessStatusHistories()) {
	    if (assiduousnessStatusHistory.getEndDate() != null) {
		Interval statusInterval = new Interval(assiduousnessStatusHistory.getBeginDate().toDateMidnight(),
			assiduousnessStatusHistory.getEndDate().toDateMidnight().plusDays(1));
		Interval interval = new Interval(beginDate.toDateMidnight(), endDate.toDateMidnight().plusDays(1));
		if (interval.overlaps(statusInterval)
			&& assiduousnessStatusHistory.getAssiduousnessStatus().getState() == AssiduousnessState.ACTIVE) {
		    return true;
		}
	    } else {
		if ((assiduousnessStatusHistory.getBeginDate().isBefore(endDate) || assiduousnessStatusHistory.getBeginDate()
			.isEqual(endDate))
			&& assiduousnessStatusHistory.getAssiduousnessStatus().getState() == AssiduousnessState.ACTIVE) {
		    return true;
		}
	    }

	}
	return false;
    }

    public List<Campus> getCampusForInterval(LocalDate begin, LocalDate end) {

	List<AssiduousnessCampusHistory> histories = this.getAssiduousnessCampusHistories();
	List<Campus> campus = new ArrayList<Campus>();
	Interval targetInterval = new Interval(begin.toDateTimeAtStartOfDay(), end.toDateTimeAtStartOfDay().plusDays(1));
	for (AssiduousnessCampusHistory history : histories) {
	    if (history.getEndDate() != null) {
		Interval historyInterval = new Interval(history.getBeginDate().toDateTimeAtStartOfDay(), history.getEndDate()
			.toDateTimeAtStartOfDay().plusDays(1));
		if (historyInterval.contains(targetInterval)) {
		    campus.add(history.getCampus());
		}
	    } else {
		if (targetInterval.contains(history.getBeginDate().toDateTimeAtStartOfDay())
			|| history.getBeginDate().isBefore(targetInterval.getStart().toLocalDate())) {
		    campus.add(history.getCampus());
		}
	    }
	}
	return campus;
    }

    public Campus getCurrentCampus() {
	final LocalDate today = new LocalDate();
	final List<AssiduousnessCampusHistory> histories = this.getAssiduousnessCampusHistories();
	for (final AssiduousnessCampusHistory history : histories) {
	    if (history.getEndDate() == null) {
		if (history.getBeginDate().isBefore(today)) {
		    return history.getCampus();
		}
	    } else {
		if (history.getBeginDate().isBefore(today) && history.getEndDate().isAfter(today)) {
		    return history.getCampus();
		}
	    }
	}
	return null;
    }

    public boolean overlapsOtherSchedules(final Schedule schedule, LocalDate beginDate, LocalDate endDate) {
	for (final Schedule otherSchedule : getSchedules()) {
	    if ((!schedule.equals(otherSchedule)) && schedule.getException().equals(otherSchedule.getException())) {
		if (endDate == null) {
		    if (otherSchedule.getBeginDate().isAfter(beginDate) || otherSchedule.getEndDate() == null
			    || (otherSchedule.getEndDate().isAfter(beginDate))) {
			return true;
		    }
		} else {
		    Interval scheduleInterval = new Interval(beginDate.toDateTimeAtStartOfDay(), endDate.toDateTimeAtStartOfDay()
			    .plusDays(1));
		    if (otherSchedule.getEndDate() == null
			    || scheduleInterval.contains(otherSchedule.getBeginDate().toDateTimeAtStartOfDay())
			    || scheduleInterval.contains(otherSchedule.getEndDate().toDateTimeAtStartOfDay())) {
			return true;
		    }
		}
	    }
	}
	return false;
    }

    public boolean hasAnyRecordsBetweenDates(LocalDate begin, LocalDate end) {
	Interval dateInterval = new Interval(begin.toDateTimeAtStartOfDay(), end.toDateTimeAtStartOfDay().plusDays(1));
	final List<AssiduousnessRecord> assiduousnessRecords = getAssiduousnessRecordBetweenDates(dateInterval.getStart(),
		dateInterval.getEnd());
	for (AssiduousnessRecord assiduousnessRecord : assiduousnessRecords) {
	    if (dateInterval.contains(assiduousnessRecord.getDate()) && (!assiduousnessRecord.isAnulated())) {
		return true;
	    }
	}
	return false;
    }

    public Duration getAverageWorkTimeDuration(LocalDate beginDate, LocalDate endDate) {
	List<Schedule> schedules = getSchedules(beginDate, endDate);
	Duration averageWorkTimeDuration = Duration.ZERO;
	for (Schedule schedule : schedules) {
	    averageWorkTimeDuration = averageWorkTimeDuration.plus(schedule.getAverageWorkPeriodDuration());
	}
	return schedules.isEmpty() ? new Duration(0) : new Duration(averageWorkTimeDuration.getMillis() / schedules.size());
    }

    public int getLeavesNumberOfWorkDays(LocalDate beginDate, LocalDate endDate, String justificationAcronym) {
	int countWorkDays = 0;
	for (Leave leave : getLeaves(beginDate, endDate)) {
	    if (leave.getJustificationMotive().getAcronym().equalsIgnoreCase(justificationAcronym)) {
		countWorkDays += leave.getWorkDaysBetween(new Interval(beginDate.toDateTimeAtStartOfDay(), endDate
			.toDateTimeAtStartOfDay()));
	    }
	}
	return countWorkDays;
    }

    public AssiduousnessVacations getAssiduousnessVacationsByYear(Integer year) {
	for (AssiduousnessVacations assiduousnessVacations : getAssiduousnessVacations()) {
	    if (assiduousnessVacations.getYear().equals(year)) {
		return assiduousnessVacations;
	    }
	}
	return null;
    }

    public void delete() {
	if (canBeDeleted()) {
	    removeEmployee();
	    removeRootDomainObject();
	    deleteDomainObject();
	}
    }

    private boolean canBeDeleted() {
	return getAssiduousnessCampusHistories().isEmpty() && getAssiduousnessRecords().isEmpty()
		&& getAssiduousnessStatusHistories().isEmpty() && getAssiduousnessVacations().isEmpty()
		&& getSchedules().isEmpty() && getExtraWorkRequests().isEmpty() && getEmployeeExtraWorkAuthorizations().isEmpty();
    }

    public List<ExtraWorkRequest> getExtraWorkRequests(LocalDate date) {
	return getExtraWorkRequests(date.getYear(), date.getMonthOfYear());
    }

    public List<ExtraWorkRequest> getExtraWorkRequests(Partial partial) {
	return getExtraWorkRequests(partial.get(DateTimeFieldType.year()), partial.get(DateTimeFieldType.monthOfYear()));
    }

    public List<ExtraWorkRequest> getExtraWorkRequests(int year, int month) {
	List<ExtraWorkRequest> requests = new ArrayList<ExtraWorkRequest>();
	for (ExtraWorkRequest extraWorkRequest : getExtraWorkRequests()) {
	    Partial paymentDate = extraWorkRequest.getPartialPayingDate();
	    if (paymentDate.get(DateTimeFieldType.year()) == year && paymentDate.get(DateTimeFieldType.monthOfYear()) == month) {
		requests.add(extraWorkRequest);
	    }
	}
	return requests;
    }

    public List<ExtraWorkRequest> getYearExtraWorkRequests(Partial paymentPartial) {
	Partial paymentDate = paymentPartial;
	paymentDate.plus(Period.months(1));
	return getYearExtraWorkRequests(paymentDate.get(DateTimeFieldType.year()),
		paymentDate.get(DateTimeFieldType.monthOfYear()));
    }

    private List<ExtraWorkRequest> getYearExtraWorkRequests(int year, int month) {
	List<ExtraWorkRequest> requests = new ArrayList<ExtraWorkRequest>();
	for (ExtraWorkRequest extraWorkRequest : getExtraWorkRequests()) {
	    Partial requestPaymentDate = extraWorkRequest.getRealPaymentPartialDate();
	    if (requestPaymentDate.get(DateTimeFieldType.year()) == year
		    && requestPaymentDate.get(DateTimeFieldType.monthOfYear()) <= month) {
		requests.add(extraWorkRequest);
	    }
	}
	return requests;
    }

    public List<ExtraWorkRequest> getExtraWorkRequestsByUnit(Unit unit, int year) {
	List<ExtraWorkRequest> result = new ArrayList<ExtraWorkRequest>();
	for (ExtraWorkRequest request : getExtraWorkRequests()) {
	    if (request.getPaymentYear() == year && request.getUnit().equals(unit) && request.getApproved()) {
		result.add(request);
	    }
	}
	return result;
    }

    public Unit getLastMailingUnitInDate(LocalDate beginDate, LocalDate endDate) {
	Unit unit = getEmployee().getLastWorkingPlace(new YearMonthDay(beginDate), new YearMonthDay(endDate));
	EmployeeContract lastMailingContract = (EmployeeContract) getEmployee().getLastContractByContractType(
		AccountabilityTypeEnum.MAILING_CONTRACT);
	if (lastMailingContract != null && lastMailingContract.getMailingUnit() != null) {
	    unit = lastMailingContract.getMailingUnit();
	}
	return unit;
    }

    public List<AssiduousnessStatusHistory> getAssiduousnessStatusHistoriesOrdered() {
	List<AssiduousnessStatusHistory> employeeStatusList = new ArrayList<AssiduousnessStatusHistory>(
		getAssiduousnessStatusHistories());
	Collections.sort(employeeStatusList, new BeanComparator("beginDate"));
	return employeeStatusList;
    }

    public BigDecimal getEmployeeSalary(LocalDate date) throws ExcepcaoPersistencia {
	GiafInterface giafInterface = new GiafInterface();
	return giafInterface.getEmployeeSalary(getEmployee(), date);
    }

    public BigDecimal getEmployeeHourValue(LocalDate date) throws ExcepcaoPersistencia {
	GiafInterface giafInterface = new GiafInterface();
	return giafInterface.getEmployeeHourValue(getEmployee(), date);
    }

    public void updateAssiduousnessRecordMonthIndex(final AssiduousnessRecord assiduousnessRecord) {
	assiduousnessRecord.getAssiduousnessRecordMonthIndexSet().clear();
	for (final Partial partial : assiduousnessRecord.getYearMonths()) {
	    AssiduousnessRecordMonthIndex assiduousnessRecordMonthIndex = getAssiduousnessRecordMonthIndex(partial);
	    if (assiduousnessRecordMonthIndex == null) {
		assiduousnessRecordMonthIndex = new AssiduousnessRecordMonthIndex(this, partial);
	    }
	    assiduousnessRecord.addAssiduousnessRecordMonthIndex(assiduousnessRecordMonthIndex);
	}
    }

    private AssiduousnessRecordMonthIndex getAssiduousnessRecordMonthIndex(DateTime dateTime) {
	for (final AssiduousnessRecordMonthIndex assiduousnessRecordMonthIndex : getAssiduousnessRecordMonthIndexsSet()) {
	    final Partial partial = assiduousnessRecordMonthIndex.getPartialYearMonth();
	    if (partial.get(DateTimeFieldType.monthOfYear()) == dateTime.getMonthOfYear()
		    && partial.get(DateTimeFieldType.year()) == dateTime.getYear()) {
		return assiduousnessRecordMonthIndex;
	    }
	}
	return null;
    }

    public static void getExcelHeader(StyledExcelSpreadsheet spreadsheet, ResourceBundle bundle, ResourceBundle enumBundle) {
	spreadsheet.newHeaderRow();
	int firstHeaderRow = spreadsheet.getSheet().getLastRowNum();
	spreadsheet.addHeader(bundle.getString("label.number"), 1500);
	spreadsheet.addHeader(bundle.getString("label.employee.name"), 5000);
	int cellNum = 0;
	for (Month month : Month.values()) {
	    spreadsheet.addHeader(enumBundle.getString(month.getName()), spreadsheet.getExcelStyle().getVerticalHeaderStyle());
	    spreadsheet.addHeader("");
	    cellNum = spreadsheet.getSheet().getRow(firstHeaderRow).getLastCellNum() - 1;
	    spreadsheet.getSheet().addMergedRegion(
		    new Region(firstHeaderRow, (short) (cellNum - 1), firstHeaderRow, (short) cellNum));
	}
	spreadsheet.getRow().setHeight((short) 1000);
	spreadsheet.addHeader(bundle.getString("label.total"), 2000);
	spreadsheet.newHeaderRow();
	spreadsheet.getSheet().addMergedRegion(new Region(firstHeaderRow, (short) 0, firstHeaderRow + 1, (short) 0));
	spreadsheet.getSheet().addMergedRegion(new Region(firstHeaderRow, (short) 1, firstHeaderRow + 1, (short) 1));

	spreadsheet.getSheet().addMergedRegion(
		new Region(firstHeaderRow, (short) (cellNum + 1), firstHeaderRow + 1, (short) (cellNum + 1)));
	spreadsheet.addHeader("");
	spreadsheet.addHeader("");
	for (Month month : Month.values()) {
	    spreadsheet.addHeader(bundle.getString("label.hoursNum"), spreadsheet.getExcelStyle().getVerticalHeaderStyle(), 600);
	    spreadsheet.addHeader(bundle.getString("label.value"), spreadsheet.getExcelStyle().getVerticalHeaderStyle(), 1600);
	}
	spreadsheet.addHeader("");
    }

    public static void getExcelFooter(StyledExcelSpreadsheet spreadsheet, ResourceBundle bundle) {
	int firstRow = 10;
	int firstColumn = 3;
	int lastRow = spreadsheet.getSheet().getLastRowNum();
	int lastColumn = spreadsheet.getMaxiumColumnNumber() - 1;
	spreadsheet.newRow();
	spreadsheet.newRow();
	spreadsheet.addCell(bundle.getString("label.total").toUpperCase());

	for (int col = firstColumn; col <= lastColumn; col += 2) {
	    spreadsheet.sumColumn(firstRow, lastRow, col, col, spreadsheet.getExcelStyle().getDoubleStyle());
	}
	spreadsheet.newRow();
	spreadsheet.newRow();
	DecimalFormat decimalFormat = new DecimalFormat("0.00");
	DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
	decimalFormatSymbols.setDecimalSeparator('.');
	decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
	spreadsheet.sumRows(firstRow, lastRow, firstColumn, lastColumn, 2, spreadsheet.getExcelStyle().getDoubleStyle());
	spreadsheet.setRegionBorder(firstRow, spreadsheet.getSheet().getLastRowNum() - 1, 0,
		spreadsheet.getMaxiumColumnNumber() - 1);
    }

    public void getExcelRow(StyledExcelSpreadsheet spreadsheet, Unit unit, Integer year) {
	DecimalFormat decimalFormat = new DecimalFormat("0.00");
	DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
	decimalFormatSymbols.setDecimalSeparator('.');
	decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
	spreadsheet.newRow();
	spreadsheet.addCell(getEmployee().getEmployeeNumber().toString());
	spreadsheet.addCell(getEmployee().getPerson().getFirstAndLastName());
	List<ExtraWorkRequest> extraWorkRequests = getExtraWorkRequestsByUnit(unit, year);
	for (ExtraWorkRequest extraWorkRequest : extraWorkRequests) {
	    YearMonth yearMonth = new YearMonth(extraWorkRequest.getPartialPayingDate());
	    yearMonth.addMonth();
	    Integer oldValue = getOldValue(spreadsheet, yearMonth.getNumberOfMonth() * 2).intValue();
	    Double oldDouble = getOldValue(spreadsheet, yearMonth.getNumberOfMonth() * 2 + 1);

	    spreadsheet.addCell(Integer.valueOf(extraWorkRequest.getTotalHours() + oldValue), yearMonth.getNumberOfMonth() * 2);
	    spreadsheet.addCell(Double.valueOf(decimalFormat.format(extraWorkRequest.getAmount() + oldDouble)), spreadsheet
		    .getExcelStyle().getDoubleStyle(), yearMonth.getNumberOfMonth() * 2 + 1);
	}
    }

    private Double getOldValue(StyledExcelSpreadsheet spreadsheet, int colNumber) {
	HSSFCell cell = spreadsheet.getRow().getCell((short) colNumber);
	Double oldValue = 0.0;
	if (cell != null) {
	    oldValue = cell.getNumericCellValue();
	}
	return oldValue;
    }

}