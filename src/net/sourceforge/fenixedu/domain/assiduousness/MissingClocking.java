package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Servico.assiduousness.ReadAssiduousnessWorkSheet;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeWorkSheet;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.WorkDaySheet;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class MissingClocking extends MissingClocking_Base {

    public MissingClocking(Assiduousness assiduousness, DateTime date, JustificationMotive justificationMotive,
	    DateTime lastModifiedDate, Employee modifiedBy, Integer oracleSequence) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setAssiduousness(assiduousness);
	setDate(date);
	setJustificationMotive(justificationMotive);
	setLastModifiedDate(lastModifiedDate);
	setModifiedBy(modifiedBy);
	setOracleSequence(oracleSequence);
    }

    public MissingClocking(Assiduousness assiduousness, DateTime date, JustificationMotive justificationMotive,
	    Employee modifiedBy) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setAssiduousness(assiduousness);
	setDate(date);
	setJustificationMotive(justificationMotive);
	setLastModifiedDate(new DateTime());
	setModifiedBy(modifiedBy);
	setOracleSequence(0);
	correctAssiduousnessClosedMonth(null);
    }

    public void modify(DateTime date, JustificationMotive justificationMotive, Employee modifiedBy) {
	ClosedMonth closedMonth = ClosedMonth.getClosedMonthForBalance(date.toLocalDate());
	if (closedMonth != null && closedMonth.getClosedForBalance()
		&& (getLastModifiedDate() == null || getLastModifiedDate().isBefore(closedMonth.getClosedForBalanceDate()))) {
	    anulate(modifiedBy);
	    new MissingClocking(getAssiduousness(), date, justificationMotive, modifiedBy);
	    correctAssiduousnessClosedMonth(null);
	} else {
	    DateTime oldDate = getDate();
	    setDate(date);
	    setJustificationMotive(justificationMotive);
	    setLastModifiedDate(new DateTime());
	    setModifiedBy(modifiedBy);
	    correctAssiduousnessClosedMonth(oldDate);
	}
    }

    @Override
    public void anulate(Employee modifiedBy) {
	super.anulate(modifiedBy);
	correctAssiduousnessClosedMonth(null);
    }

    private void correctAssiduousnessClosedMonth(DateTime oldDay) {
	ClosedMonth correctionClosedMonth = ClosedMonth.getNextClosedMonth();
	Boolean correctNext = false;
	LocalDate date = getDate().toLocalDate();
	AssiduousnessStatusHistory assiduousnessStatusHistory = getAssiduousness().getLastAssiduousnessStatusHistoryBetween(date,
		date);
	EmployeeWorkSheet oldEmployeeWorkSheet = null;

	do {
	    correctNext = false;
	    ClosedMonth closedMonth = ClosedMonth.getClosedMonthForBalance(date);
	    if (closedMonth != null) {
		AssiduousnessClosedMonth oldAssiduousnessClosedMonth = closedMonth
			.getAssiduousnessClosedMonth(assiduousnessStatusHistory);
		if (oldAssiduousnessClosedMonth != null) {

		    EmployeeWorkSheet employeeWorkSheet = ReadAssiduousnessWorkSheet.run(getAssiduousness().getEmployee(),
			    oldAssiduousnessClosedMonth.getBeginDate(), oldAssiduousnessClosedMonth.getEndDate());

		    if (oldDay != null) {
			oldEmployeeWorkSheet = ReadAssiduousnessWorkSheet.run(getAssiduousness().getEmployee(), oldDay
				.toLocalDate(), oldDay.toLocalDate());
		    }

		    AssiduousnessClosedMonth newAssiduousnessClosedMonth = oldAssiduousnessClosedMonth;

		    if (!oldAssiduousnessClosedMonth.hasEqualValues(employeeWorkSheet)) {
			correctNext = true;
			if (oldAssiduousnessClosedMonth.getIsCorrection()
				&& (!oldAssiduousnessClosedMonth.getCorrectedOnClosedMonth().getClosedYearMonth().isBefore(
					correctionClosedMonth.getClosedYearMonth()))) {
			    oldAssiduousnessClosedMonth.correct(employeeWorkSheet);
			} else {
			    newAssiduousnessClosedMonth = new AssiduousnessClosedMonth(employeeWorkSheet, correctionClosedMonth,
				    oldAssiduousnessClosedMonth);
			}
		    }

		    List<ClosedMonthJustification> closedMonthJustifications = oldAssiduousnessClosedMonth
			    .getClosedMonthJustifications();
		    for (ClosedMonthJustification closedMonthJustification : closedMonthJustifications) {
			if (!closedMonthJustification.hasEqualValues(employeeWorkSheet)) {
			    correctNext = true;
			    if (closedMonthJustification.getIsCorrection()
				    && (!closedMonthJustification.getCorrectedOnClosedMonth().getClosedYearMonth().isBefore(
					    correctionClosedMonth.getClosedYearMonth()))) {
				closedMonthJustification.correct(employeeWorkSheet);
			    } else {
				new ClosedMonthJustification(employeeWorkSheet, correctionClosedMonth,
					oldAssiduousnessClosedMonth, closedMonthJustification.getJustificationMotive());
			    }
			}
		    }

		    Set<JustificationMotive> notCorrectedJustifications = new HashSet<JustificationMotive>();
		    notCorrectedJustifications.addAll(getNotCorrected(employeeWorkSheet.getJustificationsDuration(),
			    closedMonthJustifications));
		    for (JustificationMotive justificationMotive : notCorrectedJustifications) {
			new ClosedMonthJustification(employeeWorkSheet, correctionClosedMonth, oldAssiduousnessClosedMonth,
				justificationMotive);
		    }

		    if (getDate().toLocalDate().equals(date)) {
			List<AssiduousnessExtraWork> assiduousnessExtraWorks = oldAssiduousnessClosedMonth
				.getAssiduousnessExtraWorks();
			for (AssiduousnessExtraWork assiduousnessExtraWork : assiduousnessExtraWorks) {
			    if (!assiduousnessExtraWork.hasEqualValues(employeeWorkSheet)) {
				if (assiduousnessExtraWork.getIsCorrection()
					&& (!assiduousnessExtraWork.getCorrectedOnClosedMonth().getClosedYearMonth().isBefore(
						correctionClosedMonth.getClosedYearMonth()))) {
				    assiduousnessExtraWork.correct(employeeWorkSheet);
				} else {
				    new AssiduousnessExtraWork(employeeWorkSheet, correctionClosedMonth,
					    oldAssiduousnessClosedMonth, assiduousnessExtraWork.getWorkScheduleType());
				}
			    }
			}

			Set<WorkScheduleType> notCorrected = new HashSet<WorkScheduleType>();
			notCorrected
				.addAll(getNotCorrectedExtraWork(employeeWorkSheet.getExtra125Map(), assiduousnessExtraWorks));
			notCorrected
				.addAll(getNotCorrectedExtraWork(employeeWorkSheet.getExtra150Map(), assiduousnessExtraWorks));
			notCorrected.addAll(getNotCorrectedExtraWork(employeeWorkSheet.getExtra150WithLimitsMap(),
				assiduousnessExtraWorks));
			notCorrected.addAll(getNotCorrectedExtraWork(employeeWorkSheet.getExtraNight160Map(),
				assiduousnessExtraWorks));
			notCorrected.addAll(getNotCorrectedExtraWork(employeeWorkSheet.getExtraNight190Map(),
				assiduousnessExtraWorks));
			notCorrected.addAll(getNotCorrectedExtraWork(employeeWorkSheet.getExtraNight190WithLimitsMap(),
				assiduousnessExtraWorks));
			notCorrected.addAll(getNotCorrectedExtraWork(employeeWorkSheet.getExtra25Map(), assiduousnessExtraWorks));
			notCorrected.addAll(getNotCorrectedExtraWork(employeeWorkSheet.getUnjustifiedMap(),
				assiduousnessExtraWorks));
			notCorrected.addAll(getNotCorrectedExtraWorkDays(employeeWorkSheet.getExtraWorkNightsMap(),
				assiduousnessExtraWorks));
			for (WorkScheduleType workScheduleType : notCorrected) {
			    new AssiduousnessExtraWork(employeeWorkSheet, correctionClosedMonth, oldAssiduousnessClosedMonth,
				    workScheduleType);
			}

			if (oldDay != null) {
			    correctOldAssiduousnessClosedDay(oldDay, correctionClosedMonth, oldEmployeeWorkSheet,
				    newAssiduousnessClosedMonth);

			    correctAssiduousnessClosedDay(correctionClosedMonth, oldAssiduousnessClosedMonth, employeeWorkSheet,
				    newAssiduousnessClosedMonth);
			} else {
			    correctAssiduousnessClosedDay(correctionClosedMonth, oldAssiduousnessClosedMonth, employeeWorkSheet,
				    newAssiduousnessClosedMonth);
			}
		    }
		}
	    }
	    date = date.plusMonths(1);
	} while (correctNext);
    }

    private void correctOldAssiduousnessClosedDay(DateTime oldDay, ClosedMonth correctionClosedMonth,
	    EmployeeWorkSheet employeeWorkSheet, AssiduousnessClosedMonth newAssiduousnessClosedMonth) {
	LocalDate oldPreviousDay = oldDay.toLocalDate().minusDays(1);
	HashMap<LocalDate, WorkSchedule> oldWorkScheduleMap = getAssiduousness().getWorkSchedulesBetweenDates(oldPreviousDay,
		oldDay.toLocalDate());
	DateTime oldInit = getInit(oldPreviousDay, oldWorkScheduleMap);
	DateTime oldEnd = getEnd(oldDay.toLocalDate(), oldWorkScheduleMap);

	HashMap<LocalDate, List<AssiduousnessRecord>> oldClockingsMap = getAssiduousness().getClockingsMap(oldWorkScheduleMap,
		oldInit, oldEnd.plusDays(1));

	LocalDate clockDay = oldDay.toLocalDate();
	int overlapsSchedule = WorkSchedule.overlapsSchedule(oldDay, oldWorkScheduleMap);
	if (overlapsSchedule == 0) {
	    if (oldClockingsMap.get(clockDay.minusDays(1)) != null && oldClockingsMap.get(clockDay.minusDays(1)).size() % 2 != 0) {
		clockDay = clockDay.minusDays(1);
	    }
	} else if (overlapsSchedule < 0) {
	    clockDay = clockDay.minusDays(1);
	}

	ClosedMonth closedMonth = ClosedMonth.getClosedMonthForBalance(oldDay.toLocalDate());
	AssiduousnessStatusHistory assiduousnessStatusHistory = getAssiduousness().getLastAssiduousnessStatusHistoryBetween(
		oldDay.toLocalDate(), oldDay.toLocalDate());
	AssiduousnessClosedMonth oldAssiduousnessClosedMonth = closedMonth
		.getAssiduousnessClosedMonth(assiduousnessStatusHistory);

	LocalDate oldDayToUpdate = clockDay;

	AssiduousnessClosedDay oldAssiduousnessClosedDay = oldAssiduousnessClosedMonth.getAssiduousnessClosedDay(oldDayToUpdate);
	WorkDaySheet oldWorkDaySheet = employeeWorkSheet.getWorkDaySheet(oldDayToUpdate);
	if (oldAssiduousnessClosedDay != null
		&& oldAssiduousnessClosedDay.getIsCorrection()
		&& (!oldAssiduousnessClosedDay.getCorrectedOnClosedMonth().getClosedYearMonth().isBefore(
			correctionClosedMonth.getClosedYearMonth()))) {
	    oldAssiduousnessClosedDay.correct(oldWorkDaySheet);
	} else {
	    new AssiduousnessClosedDay(newAssiduousnessClosedMonth, oldWorkDaySheet, correctionClosedMonth);
	}
    }

    private void correctAssiduousnessClosedDay(ClosedMonth correctionClosedMonth,
	    AssiduousnessClosedMonth oldAssiduousnessClosedMonth, EmployeeWorkSheet employeeWorkSheet,
	    AssiduousnessClosedMonth newAssiduousnessClosedMonth) {
	LocalDate previousDay = getDate().toLocalDate().minusDays(1);
	HashMap<LocalDate, WorkSchedule> workScheduleMap = getAssiduousness().getWorkSchedulesBetweenDates(previousDay,
		getDate().toLocalDate());
	DateTime init = getInit(previousDay, workScheduleMap);
	DateTime end = getEnd(getDate().toLocalDate(), workScheduleMap);
	HashMap<LocalDate, List<AssiduousnessRecord>> clockingsMap = getAssiduousness().getClockingsMap(workScheduleMap, init,
		end.plusDays(1));

	LocalDate dayToUpdate = getDate().toLocalDate();
	if (clockingsMap.get(previousDay) != null && clockingsMap.get(previousDay).contains(this)) {
	    dayToUpdate = previousDay;
	}

	AssiduousnessClosedDay assiduousnessClosedDay = oldAssiduousnessClosedMonth.getAssiduousnessClosedDay(dayToUpdate);
	WorkDaySheet workDaySheet = employeeWorkSheet.getWorkDaySheet(dayToUpdate);
	if (assiduousnessClosedDay != null
		&& assiduousnessClosedDay.getIsCorrection()
		&& (!assiduousnessClosedDay.getCorrectedOnClosedMonth().getClosedYearMonth().isBefore(
			correctionClosedMonth.getClosedYearMonth()))) {
	    assiduousnessClosedDay.correct(workDaySheet);
	} else {
	    new AssiduousnessClosedDay(newAssiduousnessClosedMonth, workDaySheet, correctionClosedMonth);
	}
    }

    @Override
    public boolean isMissingClocking() {
	return true;
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
