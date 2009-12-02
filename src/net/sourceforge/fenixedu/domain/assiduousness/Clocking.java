package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Servico.assiduousness.ReadAssiduousnessWorkSheet;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeWorkSheet;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.WorkDaySheet;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.YearMonth;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.util.AnulationState;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.joda.time.TimeOfDay;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public class Clocking extends Clocking_Base {

	public Clocking(Assiduousness assiduousness, ClockUnit clockUnit, DateTime date, Integer oracleSequence) {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
		setAssiduousness(assiduousness);
		setDate(date);
		setClockUnit(clockUnit);
		setOracleSequence(oracleSequence);
	}

	public TimeOfDay getTimeOfDay() {
		return getDate().toTimeOfDay();
	}

	public String getDeleteSlot() {
		return "(" + ResourceBundle.getBundle("resources.AssiduousnessResources", Language.getLocale()).getString("label.delete")
				+ ")";
	}

	public String getRestoreSlot() {
		return "("
				+ ResourceBundle.getBundle("resources.AssiduousnessResources", Language.getLocale()).getString("label.restore")
				+ ")";
	}

	@Override
	public boolean isClocking() {
		return true;
	}
	
	public void anulate(Employee modifiedBy) {
		if (getAnulation() != null) {
			getAnulation().setState(AnulationState.VALID);
			getAnulation().setLastModifiedDate(new DateTime());
			getAnulation().setModifiedBy(modifiedBy);
		} else {
			new Anulation(this, modifiedBy);
		}
		correctAssiduousnessClosedMonth();
	}

	public void restore(Employee modifiedBy) {
		if (getAnulation() != null) {
			getAnulation().setState(AnulationState.INVALID);
			getAnulation().setLastModifiedDate(new DateTime());
			getAnulation().setModifiedBy(modifiedBy);		
			correctAssiduousnessClosedMonth();
		}
	}
	
	private void correctAssiduousnessClosedMonth() {
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

					AssiduousnessClosedMonth newAssiduousnessClosedMonth = oldAssiduousnessClosedMonth;

					if (!oldAssiduousnessClosedMonth.hasEqualValues(employeeWorkSheet)) {
						correctNext = true;
						if (oldAssiduousnessClosedMonth.getIsCorrection()
								&& oldAssiduousnessClosedMonth.getCorrectedOnClosedMonth().equals(correctionClosedMonth)) {
							oldAssiduousnessClosedMonth.correct(employeeWorkSheet);
						} else {
							newAssiduousnessClosedMonth = new AssiduousnessClosedMonth(employeeWorkSheet, correctionClosedMonth,
									oldAssiduousnessClosedMonth);
						}
					}
					
					if (getDate().toLocalDate().equals(date)) {
						List<AssiduousnessExtraWork> assiduousnessExtraWorks = oldAssiduousnessClosedMonth
								.getAssiduousnessExtraWorks();
						for (AssiduousnessExtraWork assiduousnessExtraWork : assiduousnessExtraWorks) {
							if (!assiduousnessExtraWork.hasEqualValues(employeeWorkSheet)) {
								if (assiduousnessExtraWork.getIsCorrection()
										&& assiduousnessExtraWork.getCorrectedOnClosedMonth().equals(correctionClosedMonth)) {
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

						correctAssiduousnessClosedDay(correctionClosedMonth, oldAssiduousnessClosedMonth, employeeWorkSheet,
								newAssiduousnessClosedMonth);
					}
				}
			}
			date = date.plusMonths(1);
		} while (correctNext);
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
		if (assiduousnessClosedDay != null && assiduousnessClosedDay.getIsCorrection()
				&& assiduousnessClosedDay.getCorrectedOnClosedMonth().equals(correctionClosedMonth)) {
			assiduousnessClosedDay.correct(workDaySheet);
		} else {
			new AssiduousnessClosedDay(newAssiduousnessClosedMonth, workDaySheet, correctionClosedMonth);
		}
	}
	
	protected List<WorkScheduleType> getNotCorrectedExtraWork(HashMap<WorkScheduleType, Duration> map,
			List<AssiduousnessExtraWork> assiduousnessExtraWorks) {
		List<WorkScheduleType> notCorrected = new ArrayList<WorkScheduleType>();
		for (WorkScheduleType workScheduleType : map.keySet()) {
			boolean alreadyCorrected = false;
			Duration duration = map.get(workScheduleType);
			for (AssiduousnessExtraWork assiduousnessExtraWork : assiduousnessExtraWorks) {
				if (assiduousnessExtraWork.getWorkScheduleType().equals(workScheduleType)) {
					alreadyCorrected = true;
				}
			}
			if (!alreadyCorrected && duration != null && !duration.equals(Duration.ZERO)) {
				notCorrected.add(workScheduleType);
			}
		}
		return notCorrected;
	}

	protected List<WorkScheduleType> getNotCorrectedExtraWorkDays(HashMap<WorkScheduleType, Integer> map,
			List<AssiduousnessExtraWork> assiduousnessExtraWorks) {
		List<WorkScheduleType> notCorrected = new ArrayList<WorkScheduleType>();
		for (WorkScheduleType workScheduleType : map.keySet()) {
			boolean alreadyCorrected = false;
			Integer value = map.get(workScheduleType);
			for (AssiduousnessExtraWork assiduousnessExtraWork : assiduousnessExtraWorks) {
				if (assiduousnessExtraWork.getWorkScheduleType().equals(workScheduleType)) {
					alreadyCorrected = true;
				}
			}
			if (!alreadyCorrected && value != null && value != 0) {
				notCorrected.add(workScheduleType);
			}
		}
		return notCorrected;
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
