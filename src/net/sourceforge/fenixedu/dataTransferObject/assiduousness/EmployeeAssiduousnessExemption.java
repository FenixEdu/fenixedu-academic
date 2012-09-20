package net.sourceforge.fenixedu.dataTransferObject.assiduousness;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.assiduousness.Assiduousness;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessExemption;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessStatusHistory;
import net.sourceforge.fenixedu.domain.assiduousness.JustificationMotive;
import net.sourceforge.fenixedu.domain.assiduousness.Leave;
import net.sourceforge.fenixedu.domain.assiduousness.util.AssiduousnessState;
import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationGroup;
import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationType;

import org.joda.time.DateTimeFieldType;
import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.joda.time.Partial;

public class EmployeeAssiduousnessExemption implements Serializable {

    private static final BigDecimal HALF_DAY = BigDecimal.valueOf(0.5);

    protected static final int ART_17_MAXIMUM = 12;

    private Employee employee;
    private Integer year;
    private Integer efectiveWorkDays;
    private Integer efectiveWorkYear;
    private Integer art17And18MaximumLimitDays;
    private Integer art17And18LimitDays;
    private Integer numberOfArt17;
    private Integer numberOfArt18;
    private Integer usedArt17;
    private BigDecimal usedArt18;

    private Integer monthsToDiscountInArt17Vacations;

    public EmployeeAssiduousnessExemption(Employee employee, Integer year) {
	setEmployee(employee);
	setYear(year);
	setMonthsToDiscountInArt17Vacations(0);
	int pastYear = year - 1;

	LocalDate beginDate = new LocalDate(year, 1, 1);
	LocalDate endDate = new LocalDate(year, 12, 31);
	LocalDate pastYearBeginDate = new LocalDate(pastYear, 1, 1);
	LocalDate pastYearEndDate = new LocalDate(pastYear, 12, 31);

	Assiduousness assiduousness = employee.getAssiduousness();
	if (assiduousness.isStatusActive(beginDate, endDate)) {

	    Integer efectiveWorkDays = 0;

	    List<AssiduousnessStatusHistory> assiduousnessStatusHistories = assiduousness.getStatusBetween(pastYearBeginDate,
		    pastYearEndDate);

	    if (hasAnyAssiduousnessStatusHistoryBefore(assiduousnessStatusHistories, beginDate.minusMonths(6))) {
		setEfectiveWorkYear(year - 1);
		// contracto há + de 6 meses
		for (AssiduousnessStatusHistory assiduousnessStatusHistory : assiduousnessStatusHistories) {
		    efectiveWorkDays = efectiveWorkDays
			    + calculateEfectiveWorkDays(pastYearBeginDate, pastYearEndDate, assiduousnessStatusHistory);
		}
		addMonthsToDiscountInArt17Vacations(getMonthsToDiscountInArt17Vacations(assiduousness, beginDate, endDate));
		if (efectiveWorkDays != 0) {
		    int exemptionDaysQuantity = AssiduousnessExemption.getAssiduousnessExemptionDaysQuantity(year);
		    int maxDays = Math.round((float) (efectiveWorkDays * new Double(0.08)));
		    setArt17And18MaximumLimitDays(Math.max(maxDays, exemptionDaysQuantity));
		    setNumberOfArt18(Math.min(exemptionDaysQuantity, art17And18MaximumLimitDays));
		    setArt17And18LimitDays(Math.min((numberOfArt18 + ART_17_MAXIMUM), art17And18MaximumLimitDays));
		    setNumberOfArt17(Math.max((art17And18LimitDays - numberOfArt18), 0));
		}
	    } else {
		setEfectiveWorkYear(year);
		// menos de 6 meses de contrato
		assiduousnessStatusHistories = assiduousness.getStatusBetween(beginDate, endDate);
		LocalDate begin = beginDate;
		for (AssiduousnessStatusHistory assiduousnessStatusHistory : assiduousnessStatusHistories) {
		    if (assiduousnessStatusHistory.getBeginDate().isAfter(beginDate)) {
			begin = assiduousnessStatusHistory.getBeginDate();
		    }
		    efectiveWorkDays = efectiveWorkDays
			    + calculateEfectiveWorkDays(beginDate, endDate, assiduousnessStatusHistory);
		}
		addMonthsToDiscountInArt17Vacations(getMonthsToDiscountInArt17Vacations(assiduousness, beginDate, endDate));
		if (efectiveWorkDays != 0) {
		    int exemptionDaysQuantity = AssiduousnessExemption.getAssiduousnessExemptionDaysQuantityByDate(begin);
		    int maxDays = (int) (efectiveWorkDays / 22 * 1.5);
		    setArt17And18MaximumLimitDays(Math.max(maxDays, exemptionDaysQuantity));
		    setNumberOfArt18(Math.min(exemptionDaysQuantity, art17And18MaximumLimitDays));
		    setArt17And18LimitDays(Math.min((numberOfArt18 + ART_17_MAXIMUM), art17And18MaximumLimitDays));
		    setNumberOfArt17(Math.max((art17And18LimitDays - numberOfArt18), 0));
		}
	    }
	    setEfectiveWorkDays(efectiveWorkDays);
	}
    }

    public BigDecimal getUsedArt18() {
	if (usedArt18 == null) {
	    usedArt18 = BigDecimal.ZERO;
	    List<Leave> leaves = getEmployee().getAssiduousness().getLeaves(new LocalDate(getYear(), 1, 3),
		    new LocalDate(getYear() + 1, 1, 3));
	    List<JustificationMotive> justificationMotives = JustificationMotive
		    .getJustificationMotivesByGroup(JustificationGroup.TOLERANCES);
	    for (Leave leave : leaves) {
		if (justificationMotives.contains(leave.getJustificationMotive())) {
		    if (JustificationType.getHalfDayJustificationTypes().contains(
			    leave.getJustificationMotive().getJustificationType())) {
			usedArt18 = usedArt18.add(HALF_DAY);
		    } else {
			usedArt18 = usedArt18.add(BigDecimal.valueOf(leave.getWorkDaysBetween(getYear())));
		    }
		}
	    }
	}
	return usedArt18;
    }

    public int getUsedArt17() {
	if (usedArt17 == null) {
	    usedArt17 = 0;
	    List<Leave> leaves = getEmployee().getAssiduousness().getLeavesByYear(getYear());
	    for (Leave leave : leaves) {
		if (leave.getJustificationMotive().getAcronym().equalsIgnoreCase("A17")
			|| leave.getJustificationMotive().getAcronym().equalsIgnoreCase("A1306")) {
		    usedArt17 = usedArt17 + leave.getWorkDaysBetween(getYear());
		}
	    }
	}
	return usedArt17;
    }

    private int calculateEfectiveWorkDays(LocalDate beginDate, LocalDate endDate,
	    AssiduousnessStatusHistory assiduousnessStatusHistory) {
	Duration totalWorkedTime = getTotalWorkedTime(assiduousnessStatusHistory, beginDate.getYear());
	int efectiveWorkDays = 0;
	if (!totalWorkedTime.equals(Duration.ZERO)) {
	    Duration averageWorkPeriodDuration = assiduousnessStatusHistory.getSheculeWeightedAverage(beginDate,
		    endDate.plusDays(1));
	    efectiveWorkDays = Math.round((float) (totalWorkedTime.getMillis() * Math.pow(averageWorkPeriodDuration.getMillis(),
		    -1)));
	}
	return efectiveWorkDays;
    }

    private Duration getTotalWorkedTime(AssiduousnessStatusHistory assiduousnessStatusHistory, int year) {
	Duration totalWorkedTime = Duration.ZERO;
	for (AssiduousnessClosedMonth assiduousnessClosedMonth : assiduousnessStatusHistory.getAssiduousnessClosedMonths()) {
	    if (assiduousnessClosedMonth.getClosedMonth() != null
		    && assiduousnessClosedMonth.getClosedMonth().getClosedYearMonth().get(DateTimeFieldType.year()) == year) {
		totalWorkedTime = totalWorkedTime.plus(assiduousnessClosedMonth.getTotalWorkedTime());
	    }
	}
	return totalWorkedTime;
    }

    private int getMonthsToDiscountInArt17Vacations(Assiduousness assiduousness, LocalDate beginDate, LocalDate endDate) {
	Set<Partial> allMonths = new HashSet<Partial>();
	Set<Partial> monthsThatDontDiscountInArt17Vacations = new HashSet<Partial>();
	Partial yearBeginPartial = new Partial().with(DateTimeFieldType.year(), beginDate.getYear()).with(
		DateTimeFieldType.monthOfYear(), 01);
	Partial yearEndPartial = new Partial().with(DateTimeFieldType.year(), beginDate.getYear()).with(
		DateTimeFieldType.monthOfYear(), 12);

	for (AssiduousnessStatusHistory assiduousnessStatusHistory : assiduousness.getStatusBetween(beginDate, endDate)) {
	    for (AssiduousnessClosedMonth assiduousnessClosedMonth : assiduousnessStatusHistory.getAssiduousnessClosedMonths()) {
		if (assiduousnessClosedMonth.getClosedMonth() != null
			&& (!assiduousnessClosedMonth.getClosedMonth().getClosedYearMonth().isBefore(yearBeginPartial))
			&& (!assiduousnessClosedMonth.getClosedMonth().getClosedYearMonth().isAfter(yearEndPartial))) {
		    allMonths.add(assiduousnessClosedMonth.getClosedMonth().getClosedYearMonth());
		    if (assiduousnessClosedMonth.getUnjustifiedDays() == 0
			    && !assiduousnessClosedMonth.getTotalWorkedTimeForA17Vacations().equals(Duration.ZERO)) {
			monthsThatDontDiscountInArt17Vacations
				.add(assiduousnessClosedMonth.getClosedMonth().getClosedYearMonth());
		    }
		}
	    }
	}
	allMonths.removeAll(monthsThatDontDiscountInArt17Vacations);
	return allMonths.size();
    }

    private boolean hasAnyAssiduousnessStatusHistoryBefore(List<AssiduousnessStatusHistory> assiduousnessStatusHistories,
	    LocalDate date) {
	for (AssiduousnessStatusHistory assiduousnessStatusHistory : assiduousnessStatusHistories) {
	    if (assiduousnessStatusHistory.getAssiduousnessStatus().getState() == AssiduousnessState.ACTIVE
		    && assiduousnessStatusHistory.getBeginDate().isBefore(date)) {
		return true;
	    }
	}
	return false;
    }

    public Integer getEfectiveWorkDays() {
	return efectiveWorkDays == null ? 0 : efectiveWorkDays;
    }

    public void setEfectiveWorkDays(Integer efectiveWorkDays) {
	this.efectiveWorkDays = efectiveWorkDays;
    }

    public Integer getEfectiveWorkYear() {
	return efectiveWorkYear;
    }

    public void setEfectiveWorkYear(Integer efectiveWorkYear) {
	this.efectiveWorkYear = efectiveWorkYear;
    }

    public Integer getArt17And18MaximumLimitDays() {
	return art17And18MaximumLimitDays == null ? 0 : art17And18MaximumLimitDays;
    }

    public void setArt17And18MaximumLimitDays(Integer art17And18MaximumLimitDays) {
	this.art17And18MaximumLimitDays = art17And18MaximumLimitDays;
    }

    public Integer getArt17And18LimitDays() {
	return art17And18LimitDays == null ? 0 : art17And18LimitDays;
    }

    public void setArt17And18LimitDays(Integer art17And18LimitDays) {
	this.art17And18LimitDays = art17And18LimitDays;
    }

    public Integer getNumberOfArt17() {
	return numberOfArt17 == null ? 0 : numberOfArt17;
    }

    public void setNumberOfArt17(Integer numberOfArt17) {
	this.numberOfArt17 = numberOfArt17;
    }

    public Integer getNumberOfArt18() {
	return numberOfArt18 == null ? 0 : numberOfArt18;
    }

    public void setNumberOfArt18(Integer numberOfArt18) {
	this.numberOfArt18 = numberOfArt18;
    }

    public Employee getEmployee() {
	return employee;
    }

    public void setEmployee(Employee employee) {
	this.employee = employee;
    }

    public void setUsedArt17(Integer usedArt17) {
	this.usedArt17 = usedArt17;
    }

    public void setUsedArt18(BigDecimal usedArt18) {
	this.usedArt18 = usedArt18;
    }

    public Integer getArt17Vacations() {
	return Math.min(5, Math.max((getNumberOfArt17() - getMonthsToDiscountInArt17Vacations() - getUsedArt17()), 0));
    }

    public Integer getYear() {
	return year;
    }

    public void setYear(Integer year) {
	this.year = year;
    }

    public Integer getMonthsToDiscountInArt17Vacations() {
	return monthsToDiscountInArt17Vacations;
    }

    public void setMonthsToDiscountInArt17Vacations(Integer monthsToDiscountInArt17Vacations) {
	this.monthsToDiscountInArt17Vacations = monthsToDiscountInArt17Vacations;
    }

    public void addMonthsToDiscountInArt17Vacations(Integer monthsToDiscountInArt17Vacations) {
	this.monthsToDiscountInArt17Vacations += monthsToDiscountInArt17Vacations;
    }

}
