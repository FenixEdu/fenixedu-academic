package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import java.util.HashMap;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeWorkSheet;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.assiduousness.Assiduousness;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessRecord;
import net.sourceforge.fenixedu.domain.assiduousness.ClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.Leave;
import net.sourceforge.fenixedu.domain.assiduousness.WorkSchedule;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.ist.fenixWebFramework.services.Service;

public class ReadAssiduousnessWorkSheet extends FenixService {

    @Service
    public static EmployeeWorkSheet run(Assiduousness assiduousness, LocalDate beginDate, LocalDate endDate) {
	return run(assiduousness, beginDate, endDate, false);
    }

    @Service
    public static EmployeeWorkSheet run(Assiduousness assiduousness, LocalDate beginDate, LocalDate endDate, Boolean extraWork) {
	if (assiduousness == null) {
	    return null;
	}

	ClosedMonth closedMonth = ClosedMonth.getClosedMonthForBalance(beginDate);
	if (closedMonth != null && closedMonth.getClosedForBalance()) {
	    return new EmployeeWorkSheet(assiduousness.getEmployee(), closedMonth, beginDate, endDate);
	} else {
	    EmployeeWorkSheet employeeWorkSheet = new EmployeeWorkSheet(assiduousness.getEmployee(), beginDate, endDate);
	    LocalDate lastActiveStatus = assiduousness.getLastActiveStatusBetween(beginDate, endDate);
	    if (lastActiveStatus == null) {
		return employeeWorkSheet;
	    }
	    endDate = lastActiveStatus;
	    LocalDate lowerBeginDate = beginDate.minusDays(8);
	    HashMap<LocalDate, WorkSchedule> workScheduleMap = assiduousness.getWorkSchedulesBetweenDates(lowerBeginDate, endDate
		    .plusDays(2));
	    DateTime init = getInit(lowerBeginDate, workScheduleMap);
	    DateTime end = getEnd(endDate, workScheduleMap);
	    HashMap<LocalDate, List<AssiduousnessRecord>> clockingsMap = assiduousness.getClockingsMap(workScheduleMap, init, end
		    .plusDays(1));
	    HashMap<LocalDate, List<Leave>> leavesMap = assiduousness.getLeavesMap(beginDate, endDate);

	    employeeWorkSheet.calculateWorkSheet(workScheduleMap, clockingsMap, leavesMap, extraWork);
	    return employeeWorkSheet;
	}
    }

    @Service
    public static EmployeeWorkSheet run(Employee employee, LocalDate beginDate, LocalDate endDate) {
	if (employee.getAssiduousness() == null) {
	    return null;
	}
	EmployeeWorkSheet employeeWorkSheet = new EmployeeWorkSheet(employee, beginDate, endDate);
	LocalDate lastActiveStatus = employee.getAssiduousness().getLastActiveStatusBetween(beginDate, endDate);
	if (lastActiveStatus == null) {
	    return employeeWorkSheet;
	}
	endDate = lastActiveStatus;
	LocalDate lowerBeginDate = beginDate.minusDays(8);
	HashMap<LocalDate, WorkSchedule> workScheduleMap = employee.getAssiduousness().getWorkSchedulesBetweenDates(
		lowerBeginDate, endDate.plusDays(2));
	DateTime init = getInit(lowerBeginDate, workScheduleMap);
	DateTime end = getEnd(endDate, workScheduleMap);
	HashMap<LocalDate, List<AssiduousnessRecord>> clockingsMap = employee.getAssiduousness().getClockingsMap(workScheduleMap,
		init, end.plusDays(1));
	HashMap<LocalDate, List<Leave>> leavesMap = employee.getAssiduousness().getLeavesMap(beginDate, endDate);

	employeeWorkSheet.calculateWorkSheet(workScheduleMap, clockingsMap, leavesMap, true);
	return employeeWorkSheet;
    }

    private static DateTime getEnd(LocalDate endDate, HashMap<LocalDate, WorkSchedule> workScheduleMap) {
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

    private static DateTime getInit(LocalDate lowerBeginDate, HashMap<LocalDate, WorkSchedule> workScheduleMap) {
	DateTime init = lowerBeginDate.toDateTime(Assiduousness.defaultStartWorkDay);
	WorkSchedule beginWorkSchedule = workScheduleMap.get(lowerBeginDate);
	if (beginWorkSchedule != null) {
	    init = lowerBeginDate.toDateTime(beginWorkSchedule.getWorkScheduleType().getWorkTime());
	}
	return init;
    }

}