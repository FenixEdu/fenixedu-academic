package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeWorkSheet;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.YearMonth;
import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.joda.time.Partial;

public class AssiduousnessClosedMonth extends AssiduousnessClosedMonth_Base {

    public AssiduousnessClosedMonth(AssiduousnessStatusHistory assiduousnessStatusHistory, ClosedMonth closedMonth,
	    EmployeeWorkSheet employeeWorkSheet, LocalDate beginDate, LocalDate endDate) {
	init(employeeWorkSheet);
	setAssiduousnessStatusHistory(assiduousnessStatusHistory);
	setClosedMonth(closedMonth);
	setBeginDate(beginDate);
	setEndDate(endDate);
	setLastModifiedDate(new DateTime());
	setIsCorrection(Boolean.FALSE);
    }

    public AssiduousnessClosedMonth(EmployeeWorkSheet employeeWorkSheet, ClosedMonth correctionClosedMonth,
	    AssiduousnessClosedMonth oldAssiduousnessClosedMonth) {
	init(employeeWorkSheet);
	setCorrectedOnClosedMonth(correctionClosedMonth);
	setAssiduousnessStatusHistory(oldAssiduousnessClosedMonth.getAssiduousnessStatusHistory());
	setClosedMonth(oldAssiduousnessClosedMonth.getClosedMonth());
	setBeginDate(oldAssiduousnessClosedMonth.getBeginDate());
	setEndDate(oldAssiduousnessClosedMonth.getEndDate());
	setIsCorrection(Boolean.TRUE);
    }

    private void init(EmployeeWorkSheet employeeWorkSheet) {
	setLastModifiedDate(new DateTime());
	setRootDomainObject(RootDomainObject.getInstance());
	setBalance(employeeWorkSheet.getTotalBalance());
	setBalanceToDiscount(employeeWorkSheet.getBalanceToCompensate());
	setSaturdayBalance(employeeWorkSheet.getComplementaryWeeklyRest());
	setSundayBalance(employeeWorkSheet.getWeeklyRest());
	setHolidayBalance(employeeWorkSheet.getHolidayRest());
	setVacations(employeeWorkSheet.getVacations().doubleValue());
	setTolerance(employeeWorkSheet.getTolerance().doubleValue());
	setArticle17(employeeWorkSheet.getArticle17().doubleValue());
	setArticle66(employeeWorkSheet.getArticle66().doubleValue());
	setMaximumWorkingDays(employeeWorkSheet.getMaximumWorkingDays());
	setWorkedDaysWithBonusDaysDiscount(employeeWorkSheet.getWorkedDaysWithBonusDaysDiscount());
	setWorkedDaysWithA17VacationsDaysDiscount(employeeWorkSheet.getWorkedDaysWithA17VacationsDaysDiscount());
	setFinalBalance(employeeWorkSheet.getEmployeeBalanceResume().getFinalAnualBalance());
	setFinalBalanceToCompensate(employeeWorkSheet.getEmployeeBalanceResume().getFutureBalanceToCompensate());
	setTotalWorkedTime(employeeWorkSheet.getTotalWorkedTime());
	setUnjustifiedDays(employeeWorkSheet.getUnjustifiedDays() + employeeWorkSheet.getUnjustifiedDaysLeave());
	setAccumulatedArticle66(employeeWorkSheet.getAccumulatedArticle66().doubleValue());
	setAccumulatedUnjustified(employeeWorkSheet.getAccumulatedUnjustified().doubleValue());
	setAccumulatedArticle66Days(employeeWorkSheet.getAccumulatedArticle66Days());
	setAccumulatedUnjustifiedDays(employeeWorkSheet.getAccumulatedUnjustifiedDays());
    }

    public void correct(EmployeeWorkSheet employeeWorkSheet) {
	init(employeeWorkSheet);
	setIsCorrection(Boolean.TRUE);
    }

    public HashMap<String, Duration> getPastJustificationsDurations() {
	HashMap<String, Duration> pastJustificationsDurations = new HashMap<String, Duration>();
	for (AssiduousnessClosedMonth assiduousnessClosedMonth : getAssiduousnessStatusHistory().getAssiduousnessClosedMonths()) {
	    if (assiduousnessClosedMonth.getClosedMonth().getClosedYearMonth().get(DateTimeFieldType.year()) == getClosedMonth()
		    .getClosedYearMonth().get(DateTimeFieldType.year())
		    && assiduousnessClosedMonth.getClosedMonth().getClosedYearMonth().get(DateTimeFieldType.monthOfYear()) < getClosedMonth()
			    .getClosedYearMonth().get(DateTimeFieldType.monthOfYear())) {
		for (ClosedMonthJustification closedMonthJustification : assiduousnessClosedMonth.getClosedMonthJustifications()) {
		    if (closedMonthJustification.getJustificationMotive().getActive()
			    && closedMonthJustification.getJustificationMotive().getAccumulate()) {
			String code = closedMonthJustification.getJustificationMotive().getGiafCode(
				assiduousnessClosedMonth.getAssiduousnessStatusHistory());
			Duration duration = pastJustificationsDurations.get(code);
			if (duration == null) {
			    duration = Duration.ZERO;
			}
			duration = duration.plus(closedMonthJustification.getJustificationDuration());
			pastJustificationsDurations.put(code, duration);
		    }
		}
	    }
	}
	return pastJustificationsDurations;
    }

    public HashMap<String, Duration> getClosedMonthJustificationsMap() {
	HashMap<String, Duration> closedMonthJustificationscodesMap = new HashMap<String, Duration>();
	for (ClosedMonthJustification closedMonthJustification : getClosedMonthJustifications()) {
	    String code = closedMonthJustification.getJustificationMotive().getGiafCode(getAssiduousnessStatusHistory());
	    Duration duration = closedMonthJustificationscodesMap.get(code);
	    if (duration == null) {
		duration = Duration.ZERO;
	    }
	    duration = duration.plus(closedMonthJustification.getJustificationDuration());
	    closedMonthJustificationscodesMap.put(code, duration);
	}
	return closedMonthJustificationscodesMap;
    }

    public void delete() {
	removeRootDomainObject();
	removeAssiduousnessStatusHistory();
	List<AssiduousnessExtraWork> assiduousnessExtraWorks = new ArrayList<AssiduousnessExtraWork>(super
		.getAssiduousnessExtraWorks());
	for (AssiduousnessExtraWork assiduousnessExtraWork : assiduousnessExtraWorks) {
	    super.getAssiduousnessExtraWorks().remove(assiduousnessExtraWork);
	    assiduousnessExtraWork.delete();
	}
	List<ClosedMonthJustification> closedMonthJustifications = new ArrayList<ClosedMonthJustification>(super
		.getClosedMonthJustifications());
	for (ClosedMonthJustification closedMonthJustification : closedMonthJustifications) {
	    super.getClosedMonthJustifications().remove(closedMonthJustification);
	    closedMonthJustification.delete();
	}

	List<AssiduousnessClosedDay> assiduousnessClosedDays = new ArrayList<AssiduousnessClosedDay>(super
		.getAssiduousnessClosedDays());
	for (AssiduousnessClosedDay assiduousnessClosedDay : assiduousnessClosedDays) {
	    super.getAssiduousnessClosedDays().remove(assiduousnessClosedDay);
	    assiduousnessClosedDay.delete();
	}
	removeClosedMonth();
	removeCorrectedOnClosedMonth();
	deleteDomainObject();
    }

    public AssiduousnessClosedMonth getPreviousAssiduousnessClosedMonth() {
	Partial partial = getClosedMonth().getClosedYearMonth();
	int previousMonth = partial.get(DateTimeFieldType.monthOfYear()) - 1;
	if (previousMonth <= 0) {
	    return null;
	}
	ClosedMonth previousClosedMonth = ClosedMonth.getClosedMonthForBalance(new YearMonth(partial
		.get(DateTimeFieldType.year()), previousMonth));
	if (previousClosedMonth != null) {
	    AssiduousnessClosedMonth assiduousnessClosedMonth = previousClosedMonth
		    .getAssiduousnessClosedMonth(getAssiduousnessStatusHistory());
	    if (assiduousnessClosedMonth != null
		    && assiduousnessClosedMonth.getAssiduousnessStatusHistory().equals(getAssiduousnessStatusHistory())) {
		return assiduousnessClosedMonth;
	    }
	}
	return null;
    }

    public Duration getTotalNightBalance() {
	Duration result = Duration.ZERO;
	for (AssiduousnessExtraWork assiduousnessExtraWork : getAssiduousnessExtraWorks()) {
	    result = result.plus(assiduousnessExtraWork.getNightBalance());
	}
	return result;
    }

    public Duration getTotalUnjustifiedBalance() {
	Duration result = Duration.ZERO;
	for (AssiduousnessExtraWork assiduousnessExtraWork : getAssiduousnessExtraWorks()) {
	    result = result.plus(assiduousnessExtraWork.getUnjustified());
	}
	return result;
    }

    public Duration getTotalFirstLevelBalance() {
	Duration result = Duration.ZERO;
	for (AssiduousnessExtraWork assiduousnessExtraWork : getAssiduousnessExtraWorks()) {
	    result = result.plus(assiduousnessExtraWork.getFirstLevelBalance());
	}
	return result;
    }

    public Duration getTotalSecondLevelBalance() {
	Duration result = Duration.ZERO;
	for (AssiduousnessExtraWork assiduousnessExtraWork : getAssiduousnessExtraWorks()) {
	    result = result.plus(assiduousnessExtraWork.getSecondLevelBalance());
	}
	return result;
    }

    public Map<WorkScheduleType, Duration> getNightWorkByWorkScheduleType() {
	Map<WorkScheduleType, Duration> nightWorkByWorkScheduleType = new HashMap<WorkScheduleType, Duration>();
	for (AssiduousnessExtraWork assiduousnessExtraWork : getAssiduousnessExtraWorks()) {
	    Duration duration = nightWorkByWorkScheduleType.get(assiduousnessExtraWork.getWorkScheduleType());
	    if (duration == null) {
		duration = Duration.ZERO;
	    }
	    duration = duration.plus(assiduousnessExtraWork.getNightBalance());
	    nightWorkByWorkScheduleType.put(assiduousnessExtraWork.getWorkScheduleType(), duration);
	}
	return nightWorkByWorkScheduleType;
    }

    public List<AssiduousnessClosedDay> getAllAssiduousnessClosedDaysWithoutCorrections() {
	return super.getAssiduousnessClosedDays();
    }

    public Set<AssiduousnessClosedDay> getAllAssiduousnessClosedDays() {
	Set<AssiduousnessClosedDay> all = new HashSet<AssiduousnessClosedDay>(getAllAssiduousnessClosedDaysWithoutCorrections());
	if (getCorrectedOnClosedMonth() != null) {
	    for (AssiduousnessClosedMonth assiduousnessClosedMonth : getClosedMonth().getAllAssiduousnessClosedMonths(
		    getAssiduousnessStatusHistory())) {
		all.addAll(assiduousnessClosedMonth.getAllAssiduousnessClosedDaysWithoutCorrections());
	    }
	}
	return all;
    }

    @Override
    public List<AssiduousnessClosedDay> getAssiduousnessClosedDays() {
	Set<AssiduousnessClosedDay> all = getAllAssiduousnessClosedDays();
	Map<LocalDate, AssiduousnessClosedDay> assiduousnessClosedDays = new HashMap<LocalDate, AssiduousnessClosedDay>();
	for (AssiduousnessClosedDay assiduousnessClosedDay : all) {
	    AssiduousnessClosedDay assiduousnessClosedDayFromMap = assiduousnessClosedDays.get(assiduousnessClosedDay.getDay());
	    if (assiduousnessClosedDayFromMap == null
		    || (assiduousnessClosedDay.getIsCorrection() && (!assiduousnessClosedDayFromMap.getIsCorrection()))
		    || isCorrectionMadeAfter(assiduousnessClosedDay, assiduousnessClosedDayFromMap)) {
		assiduousnessClosedDays.put(assiduousnessClosedDay.getDay(), assiduousnessClosedDay);
	    }
	}
	return new ArrayList<AssiduousnessClosedDay>(assiduousnessClosedDays.values());
    }

    private boolean isCorrectionMadeAfter(AssiduousnessClosedDay assiduousnessClosedDay,
	    AssiduousnessClosedDay assiduousnessClosedDayFromMap) {
	return assiduousnessClosedDay.getIsCorrection()
		&& assiduousnessClosedDayFromMap.getIsCorrection()
		&& (assiduousnessClosedDay.getCorrectedOnClosedMonth().getClosedYearMonth().isAfter(assiduousnessClosedDayFromMap
			.getCorrectedOnClosedMonth().getClosedYearMonth()));
    }

    public AssiduousnessClosedDay getAssiduousnessClosedDay(LocalDate date) {
	AssiduousnessClosedDay lastAssiduousnessClosedDay = null;
	for (AssiduousnessClosedDay assiduousnessClosedDay : getAssiduousnessClosedDays()) {
	    if (assiduousnessClosedDay.getDay().equals(date)
		    && (lastAssiduousnessClosedDay == null || (assiduousnessClosedDay.getIsCorrection() && ((!lastAssiduousnessClosedDay
			    .getIsCorrection()) || lastAssiduousnessClosedDay.getCorrectedOnClosedMonth().getClosedYearMonth()
			    .isBefore(assiduousnessClosedDay.getCorrectedOnClosedMonth().getClosedYearMonth()))))) {
		lastAssiduousnessClosedDay = assiduousnessClosedDay;
	    }
	}
	return lastAssiduousnessClosedDay;
    }

    public List<AssiduousnessExtraWork> getAllAssiduousnessExtraWorks() {
	return super.getAssiduousnessExtraWorks();
    }

    @Override
    public List<AssiduousnessExtraWork> getAssiduousnessExtraWorks() {
	Set<AssiduousnessExtraWork> all = new HashSet<AssiduousnessExtraWork>(getAllAssiduousnessExtraWorks());
	if (getCorrectedOnClosedMonth() != null) {
	    for (AssiduousnessClosedMonth assiduousnessClosedMonth : getClosedMonth().getAllAssiduousnessClosedMonths(
		    getAssiduousnessStatusHistory())) {
		all.addAll(assiduousnessClosedMonth.getAllAssiduousnessExtraWorks());
	    }
	}
	Map<WorkScheduleType, AssiduousnessExtraWork> assiduousnessExtraWorks = new HashMap<WorkScheduleType, AssiduousnessExtraWork>();
	for (AssiduousnessExtraWork assiduousnessExtraWork : all) {
	    AssiduousnessExtraWork assiduousnessExtraWorkFromMap = assiduousnessExtraWorks.get(assiduousnessExtraWork
		    .getWorkScheduleType());
	    if (assiduousnessExtraWorkFromMap == null
		    || (assiduousnessExtraWork.getIsCorrection() && (!assiduousnessExtraWorkFromMap.getIsCorrection()))
		    || (assiduousnessExtraWork.getIsCorrection() && assiduousnessExtraWorkFromMap.getIsCorrection() && assiduousnessExtraWork
			    .getCorrectedOnClosedMonth().getClosedYearMonth().isAfter(
				    assiduousnessExtraWorkFromMap.getCorrectedOnClosedMonth().getClosedYearMonth()))) {
		assiduousnessExtraWorks.put(assiduousnessExtraWork.getWorkScheduleType(), assiduousnessExtraWork);
	    }
	}
	return new ArrayList<AssiduousnessExtraWork>(assiduousnessExtraWorks.values());
    }

    public Set<ClosedMonthJustification> getAllClosedMonthJustifications() {
	Set<ClosedMonthJustification> all = new HashSet<ClosedMonthJustification>(
		getClosedMonthJustificationsWithoutCorrections());
	if (getCorrectedOnClosedMonth() != null) {
	    for (AssiduousnessClosedMonth assiduousnessClosedMonth : getClosedMonth().getAllAssiduousnessClosedMonths(
		    getAssiduousnessStatusHistory())) {
		all.addAll(assiduousnessClosedMonth.getClosedMonthJustificationsWithoutCorrections());
	    }
	}
	return all;
    }

    public List<ClosedMonthJustification> getClosedMonthJustificationsWithoutCorrections() {
	return super.getClosedMonthJustifications();
    }

    @Override
    public List<ClosedMonthJustification> getClosedMonthJustifications() {
	Set<ClosedMonthJustification> all = getAllClosedMonthJustifications();

	Map<JustificationMotive, ClosedMonthJustification> closedMonthJustifications = new HashMap<JustificationMotive, ClosedMonthJustification>();
	for (ClosedMonthJustification closedMonthJustification : all) {
	    ClosedMonthJustification closedMonthJustificationFromMap = closedMonthJustifications.get(closedMonthJustification
		    .getJustificationMotive());
	    if (closedMonthJustificationFromMap == null
		    || (closedMonthJustification.getIsCorrection() && (!closedMonthJustificationFromMap.getIsCorrection()))
		    || (closedMonthJustification.getIsCorrection() && closedMonthJustificationFromMap.getIsCorrection() && closedMonthJustification
			    .getCorrectedOnClosedMonth().getClosedYearMonth().isAfter(
				    closedMonthJustificationFromMap.getCorrectedOnClosedMonth().getClosedYearMonth()))) {
		closedMonthJustifications.put(closedMonthJustification.getJustificationMotive(), closedMonthJustification);
	    }
	}
	return new ArrayList<ClosedMonthJustification>(closedMonthJustifications.values());
    }

    public boolean hasEqualValues(EmployeeWorkSheet employeeWorkSheet) {
	return (employeeWorkSheet.getTotalBalance().equals(getBalance())
		&& employeeWorkSheet.getUnjustifiedBalance().equals(getTotalUnjustifiedBalance())
		&& employeeWorkSheet.getComplementaryWeeklyRest().equals(getSaturdayBalance())
		&& employeeWorkSheet.getWeeklyRest().equals(getSundayBalance())
		&& employeeWorkSheet.getHolidayRest().equals(getHolidayBalance())
		&& employeeWorkSheet.getUnjustifiedDays().equals(getUnjustifiedDays())
		&& employeeWorkSheet.getAccumulatedUnjustifiedDays().equals(getAccumulatedUnjustifiedDays())
		&& employeeWorkSheet.getAccumulatedArticle66Days().equals(getAccumulatedArticle66Days())
		&& employeeWorkSheet.getBalanceToCompensate().equals(getBalanceToDiscount())
		&& employeeWorkSheet.getMaximumWorkingDays().equals(getMaximumWorkingDays())
		&& employeeWorkSheet.getTotalWorkedTime().equals(getTotalWorkedTime())
		&& employeeWorkSheet.getWorkedDaysWithBonusDaysDiscount().equals(getWorkedDaysWithBonusDaysDiscount())
		&& employeeWorkSheet.getWorkedDaysWithA17VacationsDaysDiscount().equals(
			getWorkedDaysWithA17VacationsDaysDiscount())
		&& employeeWorkSheet.getMultipleMonthBalance().equals(getBalanceToDiscount())
		&& employeeWorkSheet.getVacations().equals(getVacations())
		&& employeeWorkSheet.getTolerance().equals(getTolerance())
		&& employeeWorkSheet.getArticle17().equals(getArticle17()) && employeeWorkSheet.getArticle66().equals(
		getArticle66()));
    }

    public AssiduousnessClosedMonth getOldAssiduousnessClosedMonth() {
	AssiduousnessClosedMonth oldAssiduousnessClosedMonth = null;
	if (getIsCorrection()) {
	    for (AssiduousnessClosedMonth assiduousnessClosedMonth : getClosedMonth().getAllAssiduousnessClosedMonths(
		    getAssiduousnessStatusHistory())) {
		if ((!assiduousnessClosedMonth.equals(this))
			&& (oldAssiduousnessClosedMonth == null || (!oldAssiduousnessClosedMonth.getIsCorrection() || (assiduousnessClosedMonth
				.getIsCorrection() && assiduousnessClosedMonth.getCorrectedOnClosedMonth().getClosedYearMonth()
				.isAfter(oldAssiduousnessClosedMonth.getCorrectedOnClosedMonth().getClosedYearMonth()))

			))) {
		    oldAssiduousnessClosedMonth = assiduousnessClosedMonth;
		}
	    }
	}
	return oldAssiduousnessClosedMonth;
    }
}
