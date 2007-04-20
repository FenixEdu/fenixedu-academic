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
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.util.ContractType;
import net.sourceforge.fenixedu.util.LanguageUtils;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.YearMonthDay;

public class ReadAssiduousnessWorkSheet extends Service {

    public EmployeeWorkSheet run(Assiduousness assiduousness, YearMonthDay beginDate,
            YearMonthDay endDate) {
        if (assiduousness == null) {
            return null;
        }
        YearMonthDay lastActiveStatus = assiduousness.getLastActiveStatusBetween(beginDate, endDate);
        if (lastActiveStatus == null) {
            return getEmployeeWorkSheetBalanceFree(assiduousness, beginDate, endDate);
        }
        endDate = lastActiveStatus;
        YearMonthDay lowerBeginDate = beginDate.minusDays(8);
        HashMap<YearMonthDay, WorkSchedule> workScheduleMap = assiduousness
                .getWorkSchedulesBetweenDates(lowerBeginDate, endDate);
        DateTime init = getInit(lowerBeginDate, workScheduleMap);
        DateTime end = getEnd(endDate, workScheduleMap);
        HashMap<YearMonthDay, List<AssiduousnessRecord>> clockingsMap = assiduousness.getClockingsMap(
                workScheduleMap, init, end);
        HashMap<YearMonthDay, List<Leave>> leavesMap = assiduousness.getLeavesMap(beginDate, endDate);

        return getEmployeeWorkSheet(assiduousness, workScheduleMap, clockingsMap, leavesMap, beginDate,
                endDate, new YearMonthDay());
    }

    private EmployeeWorkSheet getEmployeeWorkSheetBalanceFree(Assiduousness assiduousness,
            YearMonthDay beginDate, YearMonthDay endDate) {
        EmployeeWorkSheet employeeWorkSheet = new EmployeeWorkSheet();
        employeeWorkSheet.setEmployee(assiduousness.getEmployee());
        Unit unit = assiduousness.getEmployee().getLastWorkingPlace(beginDate, endDate);
        if (assiduousness.getEmployee().getLastContractByContractType(ContractType.MAILING) != null
                && assiduousness.getEmployee().getLastContractByContractType(ContractType.MAILING)
                        .getMailingUnit() != null) {
            unit = assiduousness.getEmployee().getLastContractByContractType(ContractType.MAILING)
                    .getMailingUnit();
        }
        employeeWorkSheet.setUnit(unit);
        if (unit != null) {
            employeeWorkSheet.setUnitCode((new DecimalFormat("0000")).format(unit.getCostCenterCode()));
        } else {
            employeeWorkSheet.setUnitCode("");
        }
        return employeeWorkSheet;
    }

    public EmployeeWorkSheet run(Assiduousness assiduousness,
            HashMap<YearMonthDay, WorkSchedule> workScheduleMap,
            HashMap<YearMonthDay, List<AssiduousnessRecord>> clockingsMap,
            HashMap<YearMonthDay, List<Leave>> leavesMap, YearMonthDay beginDate, YearMonthDay endDate,
            YearMonthDay today) {
        if (assiduousness == null) {
            return null;
        }
        YearMonthDay lastActiveStatus = assiduousness.getLastActiveStatusBetween(beginDate, endDate);
        if (lastActiveStatus == null) {
            return getEmployeeWorkSheetBalanceFree(assiduousness, beginDate, endDate);
        }
        return getEmployeeWorkSheet(assiduousness, workScheduleMap, clockingsMap, leavesMap, beginDate,
                lastActiveStatus, today);
    }

    private EmployeeWorkSheet getEmployeeWorkSheet(Assiduousness assiduousness,
            HashMap<YearMonthDay, WorkSchedule> workScheduleMap,
            HashMap<YearMonthDay, List<AssiduousnessRecord>> clockingsMap,
            HashMap<YearMonthDay, List<Leave>> leavesMap, YearMonthDay beginDate, YearMonthDay endDate,
            YearMonthDay today) {
        final List<WorkDaySheet> workSheet = new ArrayList<WorkDaySheet>();
        Duration totalBalance = Duration.ZERO;
        Duration totalUnjustified = Duration.ZERO;
        // TODO remove comment in 2007
        // Duration totalComplementaryWeeklyRestBalance = Duration.ZERO;
        // Duration totalWeeklyRestBalance = Duration.ZERO;

        for (YearMonthDay thisDay = beginDate; thisDay.isBefore(endDate.plusDays(1)); thisDay = thisDay
                .plusDays(1)) {
            WorkDaySheet workDaySheet = new WorkDaySheet();
            workDaySheet.setDate(thisDay);

            final Schedule schedule = assiduousness.getSchedule(thisDay);

            if (schedule == null || !assiduousness.isStatusActive(thisDay, thisDay)) {
                workDaySheet.setBalanceTime(new Period(0));
                workDaySheet.setUnjustifiedTime(Duration.ZERO);
                workSheet.add(workDaySheet);
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
                        totalBalance = totalBalance.plus(workDaySheet.getBalanceTime().toDurationFrom(
                                new DateMidnight()));
                        totalUnjustified = totalUnjustified.plus(workDaySheet.getUnjustifiedTime());
                    }
                    workDaySheet.setWorkScheduleAcronym(workSchedule.getWorkScheduleType().getAcronym());
                    workSheet.add(workDaySheet);
                } else {
                    // TODO remove comment in 2007
                    // if (!thisDay.equals(today)) {
                    // workDaySheet = assiduousness.calculateDailyBalance(workDaySheet, isDayHoliday);
                    // // totalComplementaryWeeklyRestBalance = totalComplementaryWeeklyRestBalance
                    // // .plus(workDaySheet.getComplementaryWeeklyRest());
                    // // totalWeeklyRestBalance = totalWeeklyRestBalance.plus(workDaySheet
                    // // .getWeeklyRest());
                    // }
                    for (final Leave leave : leavesList) {
                        if (leave.getJustificationMotive().getJustificationType() == JustificationType.OCCURRENCE
                                && leave.getJustificationMotive().getDayType() != DayType.WORKDAY
                                && leave.getJustificationMotive().getJustificationGroup() != JustificationGroup.CURRENT_YEAR_HOLIDAYS
                                && leave.getJustificationMotive().getJustificationGroup() != JustificationGroup.LAST_YEAR_HOLIDAYS) {
                            if (notes.length() != 0) {
                                notes.append(" / ");
                            }
                            notes.append(leave.getJustificationMotive().getAcronym());
                        }
                    }
                    workDaySheet.setNotes(notes.toString());
                    if (isDayHoliday) {
                        ResourceBundle bundle = ResourceBundle.getBundle(
                                "resources.AssiduousnessResources", LanguageUtils.getLocale());
                        workDaySheet.setWorkScheduleAcronym(bundle.getString("label.holiday"));
                    }
                    workDaySheet.setUnjustifiedTime(Duration.ZERO);
                    workSheet.add(workDaySheet);
                }
            }
        }
        EmployeeWorkSheet employeeWorkSheet = new EmployeeWorkSheet();
        employeeWorkSheet.setWorkDaySheetList(workSheet);
        employeeWorkSheet.setEmployee(assiduousness.getEmployee());
        Unit unit = assiduousness.getEmployee().getLastWorkingPlace(beginDate, endDate);
        if (assiduousness.getEmployee().getLastContractByContractType(ContractType.MAILING) != null
                && assiduousness.getEmployee().getLastContractByContractType(ContractType.MAILING)
                        .getMailingUnit() != null) {
            unit = assiduousness.getEmployee().getLastContractByContractType(ContractType.MAILING)
                    .getMailingUnit();
        }
        employeeWorkSheet.setUnit(unit);
        if (unit != null) {
            employeeWorkSheet.setUnitCode((new DecimalFormat("0000")).format(unit.getCostCenterCode()));
        } else {
            employeeWorkSheet.setUnitCode("");
        }
        employeeWorkSheet.setTotalBalance(totalBalance);
        employeeWorkSheet.setUnjustifiedBalance(totalUnjustified);

        // TODO remove comment in 2007
        // employeeWorkSheet.setComplementaryWeeklyRest(totalComplementaryWeeklyRestBalance);
        // employeeWorkSheet.setWeeklyRest(totalWeeklyRestBalance);
        return employeeWorkSheet;
    }

    private DateTime getEnd(YearMonthDay endDate, HashMap<YearMonthDay, WorkSchedule> workScheduleMap) {
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

    private DateTime getInit(YearMonthDay lowerBeginDate,
            HashMap<YearMonthDay, WorkSchedule> workScheduleMap) {
        DateTime init = lowerBeginDate.toDateTime(Assiduousness.defaultStartWorkDay);
        WorkSchedule beginWorkSchedule = workScheduleMap.get(lowerBeginDate);
        if (beginWorkSchedule != null) {
            init = lowerBeginDate.toDateTime(beginWorkSchedule.getWorkScheduleType().getWorkTime());
        }
        return init;
    }

}
