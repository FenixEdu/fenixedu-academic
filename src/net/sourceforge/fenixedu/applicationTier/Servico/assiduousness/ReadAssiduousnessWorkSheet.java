package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeWorkSheet;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.WorkDaySheet;
import net.sourceforge.fenixedu.domain.assiduousness.Assiduousness;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessRecord;
import net.sourceforge.fenixedu.domain.assiduousness.Leave;
import net.sourceforge.fenixedu.domain.assiduousness.Schedule;
import net.sourceforge.fenixedu.domain.assiduousness.WorkSchedule;
import net.sourceforge.fenixedu.domain.assiduousness.util.DayType;
import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationGroup;
import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationType;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.EmployeeContract;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public class ReadAssiduousnessWorkSheet extends Service {

    public EmployeeWorkSheet run(Assiduousness assiduousness, LocalDate beginDate, LocalDate endDate) {
	if (assiduousness == null) {
	    return null;
	}
	LocalDate lastActiveStatus = assiduousness.getLastActiveStatusBetween(beginDate, endDate);
	if (lastActiveStatus == null) {
	    return getEmployeeWorkSheetBalanceFree(assiduousness, beginDate, endDate);
	}
	endDate = lastActiveStatus;
	LocalDate lowerBeginDate = beginDate.minusDays(8);
	HashMap<LocalDate, WorkSchedule> workScheduleMap = assiduousness.getWorkSchedulesBetweenDates(lowerBeginDate, endDate);
	DateTime init = getInit(lowerBeginDate, workScheduleMap);
	DateTime end = getEnd(endDate, workScheduleMap);
	HashMap<LocalDate, List<AssiduousnessRecord>> clockingsMap = assiduousness.getClockingsMap(workScheduleMap, init, end);
	HashMap<LocalDate, List<Leave>> leavesMap = assiduousness.getLeavesMap(beginDate, endDate);

	return getEmployeeWorkSheet(assiduousness, workScheduleMap, clockingsMap, leavesMap, beginDate, endDate, new LocalDate());
    }

    private EmployeeWorkSheet getEmployeeWorkSheetBalanceFree(Assiduousness assiduousness, LocalDate beginDate, LocalDate endDate) {
	EmployeeWorkSheet employeeWorkSheet = new EmployeeWorkSheet();
	employeeWorkSheet.setEmployee(assiduousness.getEmployee());
	Unit unit = assiduousness.getEmployee().getLastWorkingPlace(new YearMonthDay(beginDate), new YearMonthDay(endDate));
	EmployeeContract lastMailingContract = (EmployeeContract) assiduousness.getEmployee().getLastContractByContractType(
		AccountabilityTypeEnum.MAILING_CONTRACT);
	if (lastMailingContract != null && lastMailingContract.getMailingUnit() != null) {
	    unit = lastMailingContract.getMailingUnit();
	}
	employeeWorkSheet.setUnit(unit);
	if (unit != null) {
	    employeeWorkSheet.setUnitCode((new DecimalFormat("0000")).format(unit.getCostCenterCode()));
	} else {
	    employeeWorkSheet.setUnitCode("");
	}
	return employeeWorkSheet;
    }

    public EmployeeWorkSheet run(Assiduousness assiduousness, HashMap<LocalDate, WorkSchedule> workScheduleMap,
	    HashMap<LocalDate, List<AssiduousnessRecord>> clockingsMap, HashMap<LocalDate, List<Leave>> leavesMap,
	    LocalDate beginDate, LocalDate endDate, LocalDate today) {
	if (assiduousness == null) {
	    return null;
	}
	LocalDate lastActiveStatus = assiduousness.getLastActiveStatusBetween(beginDate, endDate);
	if (lastActiveStatus == null) {
	    return getEmployeeWorkSheetBalanceFree(assiduousness, beginDate, endDate);
	}
	return getEmployeeWorkSheet(assiduousness, workScheduleMap, clockingsMap, leavesMap, beginDate, lastActiveStatus, today);
    }

    private EmployeeWorkSheet getEmployeeWorkSheet(Assiduousness assiduousness, HashMap<LocalDate, WorkSchedule> workScheduleMap,
	    HashMap<LocalDate, List<AssiduousnessRecord>> clockingsMap, HashMap<LocalDate, List<Leave>> leavesMap,
	    LocalDate beginDate, LocalDate endDate, LocalDate today) {
	final List<WorkDaySheet> workSheet = new ArrayList<WorkDaySheet>();
	Duration totalBalance = Duration.ZERO;
	Duration totalUnjustified = Duration.ZERO;

	Duration totalComplementaryWeeklyRestBalance = Duration.ZERO;
	Duration totalWeeklyRestBalance = Duration.ZERO;
	Duration totalHolidayBalance = Duration.ZERO;
	Duration totalBalanceToCompensate = Duration.ZERO;

	for (LocalDate thisDay = beginDate; thisDay.isBefore(endDate.plusDays(1)); thisDay = thisDay.plusDays(1)) {
	    WorkDaySheet workDaySheet = new WorkDaySheet();
	    workDaySheet.setDate(thisDay);

	    final Schedule schedule = assiduousness.getSchedule(thisDay);

	    if (schedule == null || !assiduousness.isStatusActive(thisDay, thisDay)) {
		if (workSheet.size() != 0) {
		    workDaySheet.setBalanceTime(new Period(0));
		    workDaySheet.setUnjustifiedTime(Duration.ZERO);
		    workSheet.add(workDaySheet);
		}
	    } else {
		final StringBuilder notes = new StringBuilder();
		final boolean isDayHoliday = assiduousness.isHoliday(thisDay);
		final WorkSchedule workSchedule = workScheduleMap.get(thisDay);
		workDaySheet.setWorkSchedule(workSchedule);
		List<AssiduousnessRecord> clockingsList = clockingsMap.get(thisDay);
		if (clockingsList == null) {
		    clockingsList = new ArrayList<AssiduousnessRecord>();
		}
		Collections.sort(clockingsList, AssiduousnessRecord.COMPARATOR_BY_DATE);
		workDaySheet.setAssiduousnessRecords(clockingsList);
		List<Leave> leavesList = leavesMap.get(thisDay);
		if (leavesList == null) {
		    leavesList = new ArrayList<Leave>();
		}
		Collections.sort(leavesList, Leave.COMPARATORY_BY_DATE);
		workDaySheet.setLeaves(leavesList);
		if (workSchedule != null && !isDayHoliday) {
		    for (final Leave leave : leavesList) {
			if (notes.length() != 0) {
			    notes.append(" / ");
			}
			notes.append(leave.getJustificationMotive().getAcronym());
		    }
		    workDaySheet.setNotes(notes.toString());

		    if (thisDay.isBefore(today)) {
			workDaySheet = assiduousness.calculateDailyBalance(workDaySheet, isDayHoliday);
			totalBalance = totalBalance.plus(workDaySheet.getBalanceTime().toDurationFrom(new DateMidnight()));
			totalUnjustified = totalUnjustified.plus(workDaySheet.getUnjustifiedTime());
		    }
		    workDaySheet.setWorkScheduleAcronym(workSchedule.getWorkScheduleType().getAcronym());
		    workSheet.add(workDaySheet);
		} else {
		    if (!thisDay.equals(today)) {
			workDaySheet = assiduousness.calculateDailyBalance(workDaySheet, isDayHoliday);
			totalComplementaryWeeklyRestBalance = totalComplementaryWeeklyRestBalance.plus(workDaySheet
				.getComplementaryWeeklyRest());
			totalWeeklyRestBalance = totalWeeklyRestBalance.plus(workDaySheet.getWeeklyRest());
			totalHolidayBalance = totalHolidayBalance.plus(workDaySheet.getHolidayRest());
		    }
		    for (final Leave leave : leavesList) {
			if (leave.getJustificationMotive().getJustificationType() == JustificationType.OCCURRENCE
				&& leave.getJustificationMotive().getDayType() != DayType.WORKDAY
				&& leave.getJustificationMotive().getJustificationGroup() != JustificationGroup.CURRENT_YEAR_HOLIDAYS
				&& leave.getJustificationMotive().getJustificationGroup() != JustificationGroup.LAST_YEAR_HOLIDAYS
				&& leave.getJustificationMotive().getJustificationGroup() != JustificationGroup.NEXT_YEAR_HOLIDAYS) {
			    if (notes.length() != 0) {
				notes.append(" / ");
			    }
			    notes.append(leave.getJustificationMotive().getAcronym());
			}
		    }
		    workDaySheet.setNotes(notes.toString());
		    if (isDayHoliday) {
			ResourceBundle bundle = ResourceBundle
				.getBundle("resources.AssiduousnessResources", Language.getLocale());
			workDaySheet.setWorkScheduleAcronym(bundle.getString("label.holiday"));
		    }
		    workDaySheet.setUnjustifiedTime(Duration.ZERO);
		    workSheet.add(workDaySheet);
		}
		totalBalanceToCompensate = totalBalanceToCompensate.plus(workDaySheet.getBalanceToCompensate());
	    }
	}
	EmployeeWorkSheet employeeWorkSheet = new EmployeeWorkSheet();
	employeeWorkSheet.setWorkDaySheetList(workSheet);
	employeeWorkSheet.setEmployee(assiduousness.getEmployee());
	employeeWorkSheet.setLastAssiduousnessStatusHistory(beginDate, endDate);
	Unit unit = assiduousness.getLastMailingUnitInDate(beginDate, endDate);
	employeeWorkSheet.setUnit(unit);
	if (unit != null) {
	    employeeWorkSheet.setUnitCode((new DecimalFormat("0000")).format(unit.getCostCenterCode()));
	} else {
	    employeeWorkSheet.setUnitCode("");
	}
	employeeWorkSheet.setTotalBalance(totalBalance);
	employeeWorkSheet.setUnjustifiedBalance(totalUnjustified);

	employeeWorkSheet.setComplementaryWeeklyRest(totalComplementaryWeeklyRestBalance);
	employeeWorkSheet.setWeeklyRest(totalWeeklyRestBalance);
	employeeWorkSheet.setHolidayRest(totalHolidayBalance);

	employeeWorkSheet.setBalanceToCompensate(totalBalanceToCompensate);
	return employeeWorkSheet;
    }

    private DateTime getEnd(LocalDate endDate, HashMap<LocalDate, WorkSchedule> workScheduleMap) {
	DateTime end = endDate.toDateTime(Assiduousness.defaultEndWorkDay.toLocalTime());
	WorkSchedule endWorkSchedule = workScheduleMap.get(endDate);
	if (endWorkSchedule != null) {
	    end = endDate.toDateTime(endWorkSchedule.getWorkScheduleType().getWorkTime().toLocalTime()).plus(
		    endWorkSchedule.getWorkScheduleType().getWorkTimeDuration());
	    if (endWorkSchedule.getWorkScheduleType().isWorkTimeNextDay()) {
		end = end.plusDays(2);
	    }
	}
	return end;
    }

    private DateTime getInit(LocalDate lowerBeginDate, HashMap<LocalDate, WorkSchedule> workScheduleMap) {
	DateTime init = lowerBeginDate.toDateTime(Assiduousness.defaultStartWorkDay.toLocalTime());
	WorkSchedule beginWorkSchedule = workScheduleMap.get(lowerBeginDate);
	if (beginWorkSchedule != null) {
	    init = lowerBeginDate.toDateTime(beginWorkSchedule.getWorkScheduleType().getWorkTime().toLocalTime());
	}
	return init;
    }

}
