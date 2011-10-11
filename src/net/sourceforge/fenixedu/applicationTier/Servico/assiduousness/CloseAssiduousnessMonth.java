package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeWorkSheet;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.WorkDaySheet;
import net.sourceforge.fenixedu.domain.assiduousness.Assiduousness;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessClosedDay;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessExtraWork;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessRecord;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessStatusHistory;
import net.sourceforge.fenixedu.domain.assiduousness.ClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.ClosedMonthJustification;
import net.sourceforge.fenixedu.domain.assiduousness.JustificationMotive;
import net.sourceforge.fenixedu.domain.assiduousness.Leave;
import net.sourceforge.fenixedu.domain.assiduousness.WorkSchedule;
import net.sourceforge.fenixedu.domain.assiduousness.WorkScheduleType;
import net.sourceforge.fenixedu.domain.assiduousness.util.AssiduousnessState;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Hours;
import org.joda.time.LocalDate;

public class CloseAssiduousnessMonth extends FenixService {

    public final static Duration DAY_HOUR_LIMIT = Hours.TWO.toStandardDuration();
    public final static Duration hourDuration = new Duration(3600000);
    public final static Duration midHourDuration = new Duration(1800000);

    public List<AssiduousnessClosedMonth> run(HashMap<Assiduousness, List<AssiduousnessRecord>> assiduousnessRecords,
	    LocalDate beginDate, LocalDate endDate) {
	ClosedMonth closedMonth = getClosedMonth(beginDate);
	System.out.println("Vou fechar o mes: " + new DateTime());
	List<AssiduousnessClosedMonth> negativeAssiduousnessClosedMonths = new ArrayList<AssiduousnessClosedMonth>();
	for (Assiduousness assiduousness : rootDomainObject.getAssiduousnesss()) {
	    List<AssiduousnessStatusHistory> assiduousnessStatusHistoryList = assiduousness.getStatusBetween(beginDate, endDate);
	    for (AssiduousnessStatusHistory assiduousnessStatusHistory : assiduousnessStatusHistoryList) {
		if (assiduousnessStatusHistory.getAssiduousnessStatus().getState() == AssiduousnessState.ACTIVE) {
		    LocalDate thisBeginDate = beginDate;
		    if (assiduousnessStatusHistory.getBeginDate().isAfter(beginDate)) {
			thisBeginDate = assiduousnessStatusHistory.getBeginDate();
		    }
		    LocalDate thisEndDate = endDate;
		    if (assiduousnessStatusHistory.getEndDate() != null
			    && assiduousnessStatusHistory.getEndDate().isBefore(endDate)) {
			thisEndDate = assiduousnessStatusHistory.getEndDate();
		    }
		    AssiduousnessClosedMonth assiduousnessClosedMonth = getMonthAssiduousnessBalance(assiduousnessStatusHistory,
			    assiduousnessRecords.get(assiduousness), closedMonth, thisBeginDate, thisEndDate);
		    if (assiduousnessClosedMonth != null) {
			negativeAssiduousnessClosedMonths.add(assiduousnessClosedMonth);
		    }
		}
	    }
	}
	return negativeAssiduousnessClosedMonths;
    }

    private ClosedMonth getClosedMonth(LocalDate beginDate) {
	ClosedMonth closedMonth = ClosedMonth.getOrCreateOpenClosedMonth(beginDate);
	if (closedMonth != null) {
	    closedMonth.setClosedForBalance(Boolean.TRUE);
	    closedMonth.setClosedForBalanceDate(new DateTime());
	}
	return closedMonth;
    }

    private AssiduousnessClosedMonth getMonthAssiduousnessBalance(AssiduousnessStatusHistory assiduousnessStatusHistory,
	    List<AssiduousnessRecord> assiduousnessRecords, ClosedMonth closedMonth, LocalDate beginDate, LocalDate endDate) {
	LocalDate lowerBeginDate = beginDate.minusDays(8);
	HashMap<LocalDate, WorkSchedule> workScheduleMap = assiduousnessStatusHistory.getAssiduousness()
		.getWorkSchedulesBetweenDates(lowerBeginDate, endDate.plusDays(2));
	DateTime init = getInit(lowerBeginDate, workScheduleMap);
	DateTime end = getEnd(endDate, workScheduleMap);
	HashMap<LocalDate, List<AssiduousnessRecord>> clockingsMap = getClockingsMap(assiduousnessRecords, workScheduleMap, init,
		end);
	HashMap<LocalDate, List<Leave>> leavesMap = getLeavesMap(assiduousnessRecords, beginDate, endDate);

	EmployeeWorkSheet employeeWorkSheet = new EmployeeWorkSheet(assiduousnessStatusHistory.getAssiduousness().getEmployee(),
		beginDate, endDate);
	employeeWorkSheet.calculateWorkSheet(workScheduleMap, clockingsMap, leavesMap, true);

	AssiduousnessClosedMonth assiduousnessClosedMonth = new AssiduousnessClosedMonth(assiduousnessStatusHistory, closedMonth,
		employeeWorkSheet, beginDate, endDate);

	for (WorkDaySheet workDaySheet : employeeWorkSheet.getWorkDaySheetList()) {
	    new AssiduousnessClosedDay(assiduousnessClosedMonth, workDaySheet);
	}
	for (JustificationMotive justificationMotive : employeeWorkSheet.getJustificationsDuration().keySet()) {
	    Duration duration = employeeWorkSheet.getJustificationsDuration().get(justificationMotive);
	    new ClosedMonthJustification(assiduousnessClosedMonth, justificationMotive, duration);
	}

	Set<WorkScheduleType> workScheduleTypeSet = new HashSet<WorkScheduleType>(employeeWorkSheet.getExtra25Map().keySet());
	workScheduleTypeSet.addAll(employeeWorkSheet.getExtra125Map().keySet());
	workScheduleTypeSet.addAll(employeeWorkSheet.getExtra150Map().keySet());
	workScheduleTypeSet.addAll(employeeWorkSheet.getExtra150WithLimitsMap().keySet());
	workScheduleTypeSet.addAll(employeeWorkSheet.getExtraNight160Map().keySet());
	workScheduleTypeSet.addAll(employeeWorkSheet.getExtraNight190Map().keySet());
	workScheduleTypeSet.addAll(employeeWorkSheet.getExtraNight190WithLimitsMap().keySet());
	workScheduleTypeSet.addAll(employeeWorkSheet.getUnjustifiedMap().keySet());
	workScheduleTypeSet.addAll(employeeWorkSheet.getExtraWorkNightsMap().keySet());

	for (WorkScheduleType workScheduleType : workScheduleTypeSet) {
	    Duration totalExtra25 = employeeWorkSheet.getExtra25Map().get(workScheduleType) == null ? Duration.ZERO
		    : employeeWorkSheet.getExtra25Map().get(workScheduleType);
	    Duration totalExtra125 = employeeWorkSheet.getExtra125Map().get(workScheduleType) == null ? Duration.ZERO
		    : employeeWorkSheet.getExtra125Map().get(workScheduleType);
	    Duration totalExtra150 = employeeWorkSheet.getExtra150Map().get(workScheduleType) == null ? Duration.ZERO
		    : employeeWorkSheet.getExtra150Map().get(workScheduleType);
	    Duration totalExtra150WithLimit = employeeWorkSheet.getExtra150WithLimitsMap().get(workScheduleType) == null ? Duration.ZERO
		    : employeeWorkSheet.getExtra150WithLimitsMap().get(workScheduleType);
	    Duration totalNightExtra160 = employeeWorkSheet.getExtraNight160Map().get(workScheduleType) == null ? Duration.ZERO
		    : employeeWorkSheet.getExtraNight160Map().get(workScheduleType);
	    Duration totalNightExtra190 = employeeWorkSheet.getExtraNight190Map().get(workScheduleType) == null ? Duration.ZERO
		    : employeeWorkSheet.getExtraNight190Map().get(workScheduleType);
	    Duration totalNightExtra190WithLimit = employeeWorkSheet.getExtraNight190WithLimitsMap().get(workScheduleType) == null ? Duration.ZERO
		    : employeeWorkSheet.getExtraNight190WithLimitsMap().get(workScheduleType);
	    Duration totalUnjustified = employeeWorkSheet.getUnjustifiedMap().get(workScheduleType) == null ? Duration.ZERO
		    : employeeWorkSheet.getUnjustifiedMap().get(workScheduleType);
	    Integer extraWorkNights = employeeWorkSheet.getExtraWorkNightsMap().get(workScheduleType) == null ? 0
		    : employeeWorkSheet.getExtraWorkNightsMap().get(workScheduleType);
	    new AssiduousnessExtraWork(assiduousnessClosedMonth, workScheduleType, totalExtra25, totalExtra125, totalExtra150,
		    totalExtra150WithLimit, totalNightExtra160, totalNightExtra190, totalNightExtra190WithLimit, extraWorkNights,
		    totalUnjustified);
	}

	if (employeeWorkSheet.getUnjustifiedDays() > 0 || assiduousnessClosedMonth.getAccumulatedUnjustifiedDays() > 0
		|| assiduousnessClosedMonth.getAccumulatedArticle66Days() > 0
		|| assiduousnessClosedMonth.getArticle66().doubleValue() > 0) {
	    return assiduousnessClosedMonth;
	}
	return null;
    }

    private HashMap<LocalDate, List<Leave>> getLeavesMap(List<AssiduousnessRecord> assiduousnessRecords, LocalDate beginDate,
	    LocalDate endDate) {
	HashMap<LocalDate, List<Leave>> leavesMap = new HashMap<LocalDate, List<Leave>>();
	if (assiduousnessRecords != null) {
	    for (AssiduousnessRecord record : assiduousnessRecords) {
		if (record.isLeave() && !record.isAnulated()) {
		    LocalDate endLeaveDay = record.getDate().toLocalDate().plusDays(1);
		    if (((Leave) record).getEndLocalDate() != null) {
			endLeaveDay = ((Leave) record).getEndLocalDate().plusDays(1);
		    }
		    for (LocalDate leaveDay = record.getDate().toLocalDate(); leaveDay.isBefore(endLeaveDay); leaveDay = leaveDay
			    .plusDays(1)) {
			if (((Leave) record).getAplicableWeekDays() == null
				|| ((Leave) record).getAplicableWeekDays().contains(leaveDay.toDateTimeAtStartOfDay())) {
			    List<Leave> leaveList = leavesMap.get(leaveDay);
			    if (leaveList == null) {
				leaveList = new ArrayList<Leave>();
			    }
			    leaveList.add((Leave) record);
			    leavesMap.put(leaveDay, leaveList);
			}
		    }
		}
	    }
	}
	return leavesMap;
    }

    private HashMap<LocalDate, List<AssiduousnessRecord>> getClockingsMap(List<AssiduousnessRecord> assiduousnessRecords,
	    HashMap<LocalDate, WorkSchedule> workScheduleMap, DateTime init, DateTime end) {
	HashMap<LocalDate, List<AssiduousnessRecord>> clockingsMap = new HashMap<LocalDate, List<AssiduousnessRecord>>();
	if (assiduousnessRecords != null) {
	    final List<AssiduousnessRecord> clockings = new ArrayList<AssiduousnessRecord>(assiduousnessRecords);
	    Collections.sort(clockings, AssiduousnessRecord.COMPARATOR_BY_DATE);
	    for (AssiduousnessRecord record : clockings) {
		if (record.isClocking() || record.isMissingClocking()) {
		    LocalDate clockDay = record.getDate().toLocalDate();
		    if (WorkSchedule.overlapsSchedule(record.getDate(), workScheduleMap) == 0) {
			if (clockingsMap.get(clockDay.minusDays(1)) != null
				&& clockingsMap.get(clockDay.minusDays(1)).size() % 2 != 0) {
			    clockDay = clockDay.minusDays(1);
			}
		    } else if (WorkSchedule.overlapsSchedule(record.getDate(), workScheduleMap) < 0) {
			clockDay = clockDay.minusDays(1);
		    }
		    List<AssiduousnessRecord> clocks = clockingsMap.get(clockDay);
		    if (clocks == null) {
			clocks = new ArrayList<AssiduousnessRecord>();
		    }

		    clocks.add(record);
		    clockingsMap.put(clockDay, clocks);
		}
	    }
	}
	return clockingsMap;
    }

    private DateTime getEnd(LocalDate endDate, HashMap<LocalDate, WorkSchedule> workScheduleMap) {
	DateTime end = endDate.toDateTime(Assiduousness.defaultEndWorkDay);
	WorkSchedule endWorkSchedule = workScheduleMap.get(endDate);
	if (endWorkSchedule != null) {
	    end = endDate.toDateTime(endWorkSchedule.getWorkScheduleType().getWorkTime()).plus(
		    endWorkSchedule.getWorkScheduleType().getWorkTimeDuration());
	    if (endWorkSchedule.getWorkScheduleType().isWorkTimeNextDay()) {
		end = end.plusDays(2);
	    }
	}
	return end;
    }

    private DateTime getInit(LocalDate lowerBeginDate, HashMap<LocalDate, WorkSchedule> workScheduleMap) {
	DateTime init = lowerBeginDate.toDateTime(Assiduousness.defaultStartWorkDay);
	WorkSchedule beginWorkSchedule = workScheduleMap.get(lowerBeginDate);
	if (beginWorkSchedule != null) {
	    init = lowerBeginDate.toDateTime(beginWorkSchedule.getWorkScheduleType().getWorkTime());
	}
	return init;
    }

}