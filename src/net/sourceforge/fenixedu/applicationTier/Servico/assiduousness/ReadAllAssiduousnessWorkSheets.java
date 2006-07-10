package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

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
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.YearMonthDay;

public class ReadAllAssiduousnessWorkSheets extends Service {

    public List<EmployeeWorkSheet> run(YearMonthDay beginDate, YearMonthDay endDate) {
        final List<EmployeeWorkSheet> employeeWorkSheetList = new ArrayList<EmployeeWorkSheet>();
        final YearMonthDay today = new YearMonthDay();
        final List defaultList = new ArrayList();
        for (Assiduousness assiduousness : rootDomainObject.getAssiduousnesss()) {
            if (assiduousness.isStatusActive(beginDate, endDate)) {

                final List<WorkDaySheet> workSheet = new ArrayList<WorkDaySheet>();

                Duration totalBalance = new Duration(0);
                Duration totalUnjustified = new Duration(0);

                final Schedule beginDaySchedule = assiduousness.getSchedule(beginDate);
                DateTime init = beginDate.toDateTime(Assiduousness.startTimeOfDay);
                if (beginDaySchedule != null) {
                    WorkSchedule beginWorkSchedule = beginDaySchedule.workScheduleWithDate(beginDate);
                    if (beginWorkSchedule != null) {
                        init = beginDate.toDateTime(beginWorkSchedule.getWorkScheduleType()
                                .getWorkTime());
                    }
                }
                final Schedule endDaySchedule = assiduousness.getSchedule(endDate);
                DateTime end = endDate.toDateTime(Assiduousness.endTimeOfDay);
                if (endDaySchedule != null) {
                    WorkSchedule endWorkSchedule = endDaySchedule.workScheduleWithDate(endDate);
                    if (endWorkSchedule != null) {
                        end = endDate.toDateTime(endWorkSchedule.getWorkScheduleType().getWorkTime());
                        if (endWorkSchedule.getWorkScheduleType().isWorkTimeNextDay()) {
                            end = end.plusDays(1);
                        }
                    }
                }

                final List<AssiduousnessRecord> clockings = assiduousness
                        .getClockingsAndMissingClockings(init, end);
                HashMap<YearMonthDay, List<AssiduousnessRecord>> clockingsMap = new HashMap<YearMonthDay, List<AssiduousnessRecord>>();
                for (AssiduousnessRecord record : clockings) {
                    List<AssiduousnessRecord> clocks = clockingsMap.get(record.getDate()
                            .toYearMonthDay());
                    if (clocks == null) {
                        clocks = new ArrayList<AssiduousnessRecord>();
                    }
                    clocks.add(record);
                    clockingsMap.put(record.getDate().toYearMonthDay(), clocks);
                }

                final List<Leave> leaves = assiduousness.getLeaves(beginDate, endDate);
                HashMap<YearMonthDay, List<Leave>> leavesMap = new HashMap<YearMonthDay, List<Leave>>();
                for (Leave record : leaves) {
                    YearMonthDay endLeaveDay = record.getDate().toYearMonthDay().plusDays(1);
                    if (record.getEndYearMonthDay() != null) {
                        endLeaveDay = record.getEndYearMonthDay().plusDays(1);
                    }
                    for (YearMonthDay leaveDay = record.getDate().toYearMonthDay(); leaveDay
                            .isBefore(endLeaveDay); leaveDay = leaveDay.plusDays(1)) {
                        List<Leave> leaveList = leavesMap.get(leaveDay);
                        if (leaveList == null) {
                            leaveList = new ArrayList<Leave>();
                        }
                        leaveList.add(record);
                        leavesMap.put(leaveDay, leaveList);
                    }
                }

                for (YearMonthDay thisDay = beginDate; thisDay.isBefore(endDate.plusDays(1)); thisDay = thisDay
                        .plusDays(1)) {
                    WorkDaySheet workDaySheet = new WorkDaySheet();
                    workDaySheet.setDate(thisDay);

                    final Schedule schedule = assiduousness.getSchedule(thisDay);

                    if (schedule == null) {
                        workDaySheet.setNotes("");
                        workDaySheet.setBalanceTime(new Period(0));
                        workDaySheet.setUnjustifiedTime(new Duration(0));
                        workSheet.add(workDaySheet);
                    } else {
                        final boolean isDayHoliday = assiduousness.isHoliday(thisDay);
                        final WorkSchedule workSchedule = schedule.workScheduleWithDate(thisDay);
                        workDaySheet.setWorkSchedule(workSchedule);
                        if (workSchedule != null && !isDayHoliday) {

                            List<AssiduousnessRecord> clockingsList = clockingsMap.get(thisDay);
                            if (clockingsList == null) {
                                clockingsList = defaultList;
                            }
                            Collections.sort(clockingsList, AssiduousnessRecord.COMPARATORY_BY_DATE);
                            workDaySheet.setAssiduousnessRecords(clockingsList);
                            List<Leave> leavesList = leavesMap.get(thisDay);
                            if (leavesList == null) {
                                leavesList = defaultList;
                            }
                            Collections.sort(leavesList, Leave.COMPARATORY_BY_DATE);
                            workDaySheet.setLeaves(leavesList);

                            final StringBuilder notes = new StringBuilder();
                            for (final Leave leave : leavesList) {
                                if (notes.length() != 0) {
                                    notes.append(" / ");
                                }
                                notes.append(leave.getJustificationMotive().getAcronym());
                            }
                            workDaySheet.setNotes(notes.toString());
                            workDaySheet = assiduousness.calculateDailyBalance(workDaySheet, thisDay,
                                    isDayHoliday);
                            if (!thisDay.equals(today)) {
                                totalBalance = totalBalance.plus(workDaySheet.getBalanceTime()
                                        .toDurationFrom(new DateMidnight()));
                            }
                            totalUnjustified = totalUnjustified.plus(workDaySheet.getUnjustifiedTime());
                            workDaySheet.setWorkScheduleAcronym(workSchedule.getWorkScheduleType()
                                    .getAcronym());
                            workSheet.add(workDaySheet);
                        } else {
                            List<AssiduousnessRecord> clockingsList = clockingsMap.get(thisDay);
                            if (clockingsList == null) {
                                clockingsList = defaultList;
                            }
                            Collections.sort(clockingsList, AssiduousnessRecord.COMPARATORY_BY_DATE);
                            workDaySheet.setAssiduousnessRecords(clockingsList);
                            workDaySheet.setLeaves(defaultList);
                            workDaySheet = assiduousness.calculateDailyBalance(workDaySheet, thisDay,
                                    isDayHoliday);
                            workDaySheet.setNotes("");
                            if (isDayHoliday) {
                                ResourceBundle bundle = ResourceBundle
                                        .getBundle("resources.AssiduousnessResources");
                                workDaySheet.setWorkScheduleAcronym(bundle.getString("label.holiday"));
                            }
                            workDaySheet.setUnjustifiedTime(new Duration(0));
                            workSheet.add(workDaySheet);
                        }
                    }
                }
                EmployeeWorkSheet employeeWorkSheet = new EmployeeWorkSheet();
                employeeWorkSheet.setWorkDaySheetList(workSheet);
                employeeWorkSheet.setEmployee(assiduousness.getEmployee());
                Unit unit = assiduousness.getEmployee().getLastWorkingPlaceByPeriod(beginDate, endDate);
                employeeWorkSheet.setUnit(unit);
                if (unit != null) {
                    employeeWorkSheet.setUnitCode(unit.getCostCenterCode().toString());
                } else {
                    employeeWorkSheet.setUnitCode("");
                }
                employeeWorkSheet.setTotalBalance(totalBalance);
                employeeWorkSheet.setUnjustifiedBalance(totalUnjustified);
                employeeWorkSheetList.add(employeeWorkSheet);
            }
        }

        return employeeWorkSheetList;
    }

}
