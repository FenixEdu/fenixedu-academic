package net.sourceforge.fenixedu.dataTransferObject.assiduousness;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.Employee;
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
import net.sourceforge.fenixedu.domain.assiduousness.Schedule;
import net.sourceforge.fenixedu.domain.assiduousness.WorkSchedule;
import net.sourceforge.fenixedu.domain.assiduousness.WorkScheduleType;
import net.sourceforge.fenixedu.domain.assiduousness.util.ScheduleClockingType;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.EmployeeContract;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.util.Month;
import net.sourceforge.fenixedu.util.NumberUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Duration;
import org.joda.time.Hours;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.MutablePeriod;
import org.joda.time.PeriodType;
import org.joda.time.YearMonthDay;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public class EmployeeWorkSheet implements Serializable {

    private static final DecimalFormat UNIT_FORMAT = new DecimalFormat("0000");

    public final static Duration midHourDuration = new Duration(1800000);

    private Employee employee;

    private LocalDate beginDate;

    private LocalDate endDate;

    private String unitCode;

    private Unit unit;

    private List<WorkDaySheet> workDaySheetList;

    private Duration totalBalance;

    private Duration unjustifiedBalance;

    private Duration weeklyRest;

    private Duration complementaryWeeklyRest;

    private Duration holidayRest;

    private Duration nightWork;

    private Duration balanceToCompensate;

    private Duration multipleMonthBalance;

    private AssiduousnessStatusHistory lastAssiduousnessStatusHistory;

    private Integer unjustifiedDays;

    private Integer unjustifiedDaysLeave;

    private BigDecimal accumulatedArticle66;

    private BigDecimal accumulatedUnjustified;

    private Integer accumulatedUnjustifiedDays;

    private Integer accumulatedArticle66Days;

    private BigDecimal vacations;

    private BigDecimal tolerance;

    private BigDecimal article17;

    private BigDecimal article66;

    private Integer maximumWorkingDays;
    private Integer workedDaysWithBonusDaysDiscount;
    private Integer workedDaysWithA17DaysDiscount;
    private Integer workedDaysWithA17VacationsDaysDiscount;
    private Duration totalWorkedTimeForA17Vacations;
    private Duration totalWorkedTime;

    // Duration finalBalance;
    // Duration finalBalanceToCompensate;

    private HashMap<WorkScheduleType, Duration> extra125Map;
    private HashMap<WorkScheduleType, Duration> extra150Map;
    private HashMap<WorkScheduleType, Duration> extra150WithLimitsMap;
    private HashMap<WorkScheduleType, Duration> extraNight160Map;
    private HashMap<WorkScheduleType, Duration> extraNight190Map;
    private HashMap<WorkScheduleType, Duration> extraNight190WithLimitsMap;
    private HashMap<WorkScheduleType, Integer> extraWorkNightsMap;
    private HashMap<WorkScheduleType, Duration> extra25Map;
    private HashMap<WorkScheduleType, Duration> unjustifiedMap;
    private HashMap<JustificationMotive, Duration> justificationsDuration;

    private EmployeeBalanceResume employeeBalanceResume;

    public EmployeeWorkSheet(Employee employee) {
	setEmployee(employee);
	intMaps();

    }

    private void intMaps() {
	extra125Map = new HashMap<WorkScheduleType, Duration>();
	extra150Map = new HashMap<WorkScheduleType, Duration>();
	extra150WithLimitsMap = new HashMap<WorkScheduleType, Duration>();
	extraNight160Map = new HashMap<WorkScheduleType, Duration>();
	extraNight190Map = new HashMap<WorkScheduleType, Duration>();
	extraNight190WithLimitsMap = new HashMap<WorkScheduleType, Duration>();
	extraWorkNightsMap = new HashMap<WorkScheduleType, Integer>();
	extra25Map = new HashMap<WorkScheduleType, Duration>();
	unjustifiedMap = new HashMap<WorkScheduleType, Duration>();
	justificationsDuration = new HashMap<JustificationMotive, Duration>();
    }

    // Para um mês já fechado
    public EmployeeWorkSheet(Employee employee, ClosedMonth closedMonth, LocalDate beginDate, LocalDate endDate) {
	setEmployee(employee);
	intMaps();
	setUnit();
	setBeginDate(beginDate);
	setEndDate(endDate);
	List<AssiduousnessStatusHistory> assiduousnessStatusHistories = employee.getAssiduousness().getStatusBetween(
		closedMonth.getClosedMonthFirstDay(), closedMonth.getClosedMonthLastDay());
	Collections.sort(assiduousnessStatusHistories, new BeanComparator("beginDate"));
	for (AssiduousnessStatusHistory assiduousnessStatusHistory : assiduousnessStatusHistories) {
	    AssiduousnessClosedMonth assiduousnessClosedMonth = closedMonth
		    .getAssiduousnessClosedMonth(assiduousnessStatusHistory);
	    addWorkDaySheets(assiduousnessClosedMonth, beginDate, endDate);
	}
	if (!assiduousnessStatusHistories.isEmpty()) {
	    setLastAssiduousnessStatusHistory(assiduousnessStatusHistories.get(assiduousnessStatusHistories.size() - 1));
	}
    }

    // Para um mês aberto
    public EmployeeWorkSheet(Employee employee, LocalDate beginDate, LocalDate endDate) {
	setEmployee(employee);
	intMaps();
	setUnit();
	setBeginDate(beginDate);
	setEndDate(endDate);
	setLastAssiduousnessStatusHistory(beginDate, endDate);
    }

    public void calculateWorkSheet(HashMap<LocalDate, WorkSchedule> workScheduleMap,
	    HashMap<LocalDate, List<AssiduousnessRecord>> clockingsMap, HashMap<LocalDate, List<Leave>> leavesMap,
	    Boolean closeMonth) {
	for (LocalDate thisDay = beginDate; thisDay.isBefore(endDate.plusDays(1)); thisDay = thisDay.plusDays(1)) {
	    final Schedule schedule = getEmployee().getAssiduousness().getSchedule(thisDay);
	    if (schedule != null && getEmployee().getAssiduousness().isStatusActive(thisDay, thisDay)) {
		WorkDaySheet workDaySheet = new WorkDaySheet(getEmployee().getAssiduousness(), thisDay, workScheduleMap
			.get(thisDay), clockingsMap.get(thisDay), leavesMap.get(thisDay));
		workDaySheet.calculateWorkDaySheet(closeMonth);
		addWorkDaySheet(workDaySheet);
	    }
	}
	Collections.sort(getWorkDaySheetList(), new BeanComparator("date"));
	setEmployeeBalanceResume(new EmployeeBalanceResume(getTotalBalance(), getMultipleMonthBalance(), getBeginDate(),
		getLastAssiduousnessStatusHistory()));
	setAccumulatedUnjustifiedAndArticle66();

	Integer finalWorkedDaysWithBonusDaysDiscount = getWorkedDaysWithBonusDaysDiscount() - getAccumulatedUnjustifiedDays();
	Integer finalWorkedDaysWithA17VacationsDaysDiscount = getWorkedDaysWithA17VacationsDaysDiscount()
		- getAccumulatedUnjustifiedDays();
	setWorkedDaysWithBonusDaysDiscount(Math.max(finalWorkedDaysWithBonusDaysDiscount, 0));
	setWorkedDaysWithA17VacationsDaysDiscount(Math.max(finalWorkedDaysWithA17VacationsDaysDiscount, 0));
    }

    public String getFormattedDate() {
	ResourceBundle bundleEnumeration = ResourceBundle.getBundle("resources.EnumerationResources", Language.getLocale());
	WorkDaySheet workDaySheet = workDaySheetList.get(0);
	StringBuilder stringBuilder = new StringBuilder();
	if (workDaySheet != null) {
	    String month = bundleEnumeration.getString(Month.values()[workDaySheet.getDate().getMonthOfYear() - 1].getName());
	    stringBuilder.append(month).append(" ").append(workDaySheet.getDate().getYear());
	}
	return stringBuilder.toString();
    }

    public String getTotalBalanceString() {
	PeriodFormatter fmt = new PeriodFormatterBuilder().printZeroAlways().appendHours().appendSeparator(":")
		.minimumPrintedDigits(2).appendMinutes().toFormatter();
	MutablePeriod finalTotalBalance = new MutablePeriod(getTotalBalance().getMillis(), PeriodType.time());
	if (getTotalBalance().toPeriod().getMinutes() < 0) {
	    finalTotalBalance.setMinutes(-getTotalBalance().toPeriod().getMinutes());
	    if (getTotalBalance().toPeriod().getHours() == 0) {
		fmt = new PeriodFormatterBuilder().printZeroAlways().appendLiteral("-").appendHours().appendSeparator(":")
			.minimumPrintedDigits(2).appendMinutes().toFormatter();
	    }
	}
	return fmt.print(finalTotalBalance);
    }

    public String getUnjustifiedBalanceString() {
	PeriodFormatter fmt = new PeriodFormatterBuilder().printZeroAlways().appendHours().appendSeparator(":")
		.minimumPrintedDigits(2).appendMinutes().toFormatter();
	MutablePeriod finalUnjustifiedBalance = new MutablePeriod(getUnjustifiedBalance().getMillis(), PeriodType.time());
	return fmt.print(finalUnjustifiedBalance);
    }

    public Employee getEmployee() {
	return employee;
    }

    public void setEmployee(Employee employee) {
	this.employee = employee;
    }

    public String getUnitCode() {
	return unitCode;
    }

    public void setUnitCode(String unitCode) {
	this.unitCode = unitCode;
    }

    public Unit getUnit() {
	return unit;
    }

    public void setUnit() {
	if (getEmployee() != null) {
	    Unit unit = getEmployee().getLastWorkingPlace(new YearMonthDay(getBeginDate()), new YearMonthDay(getEndDate()));
	    EmployeeContract lastMailingContract = (EmployeeContract) getEmployee().getLastContractByContractType(
		    AccountabilityTypeEnum.MAILING_CONTRACT);
	    if (lastMailingContract != null && lastMailingContract.getMailingUnit() != null) {
		unit = lastMailingContract.getMailingUnit();
	    }
	    this.unit = unit;
	    if (unit != null) {
		setUnitCode(UNIT_FORMAT.format(unit.getCostCenterCode()));
	    } else {
		setUnitCode("");
	    }
	}
    }

    public Duration getTotalBalance() {
	return totalBalance == null ? Duration.ZERO : totalBalance;
    }

    public void setTotalBalance(Duration totalBalance) {
	this.totalBalance = totalBalance;
    }

    public Duration getUnjustifiedBalance() {
	return unjustifiedBalance == null ? Duration.ZERO : unjustifiedBalance;
    }

    public void setUnjustifiedBalance(Duration unjustifiedBalance) {
	this.unjustifiedBalance = unjustifiedBalance;
    }

    public List<WorkDaySheet> getWorkDaySheetList() {
	if (workDaySheetList == null) {
	    workDaySheetList = new ArrayList<WorkDaySheet>();
	}
	return workDaySheetList;
    }

    public void setWorkDaySheetList(List<WorkDaySheet> workDaySheetList) {
	this.workDaySheetList = workDaySheetList;
    }

    public void addWorkDaySheet(WorkDaySheet workDaySheet) {
	getWorkDaySheetList().add(workDaySheet);
	if (workDaySheet.getUnjustifiedDay()) {
	    setUnjustifiedDays(getUnjustifiedDays() + 1);
	}
	if (workDaySheet.getUnjustifiedDayLeave()) {
	    setUnjustifiedDaysLeave(getUnjustifiedDaysLeave() + 1);
	}

	if (workDaySheet.getWorkSchedule() != null
		&& !workDaySheet.getWorkSchedule().getWorkScheduleType().getScheduleClockingType().equals(
			ScheduleClockingType.NOT_MANDATORY_CLOCKING)) {
	    setTotalBalance(getTotalBalance().plus(workDaySheet.getBalanceTime()));
	}
	setUnjustifiedBalance(getUnjustifiedBalance().plus(workDaySheet.getUnjustifiedTime()));
	setBalanceToCompensate(getBalanceToCompensate().plus(workDaySheet.getBalanceToCompensate()));
	setMultipleMonthBalance(getMultipleMonthBalance().plus(workDaySheet.getMultipleMonthBalance()));
	setVacations(getVacations().add(workDaySheet.getVacation()));
	setTolerance(getTolerance().add(workDaySheet.getTolerance()));
	setArticle17(getArticle17().add(workDaySheet.getArticle17()));
	setArticle66(getArticle66().add(workDaySheet.getArticle66()));
	if (workDaySheet.isWorkingDay()) {
	    setMaximumWorkingDays(getMaximumWorkingDays() + 1);
	}
	setTotalWorkedTime(getTotalWorkedTime().plus(workDaySheet.getWorkedTime()));
	setTotalWorkedTimeForA17Vacations(getTotalWorkedTimeForA17Vacations().plus(workDaySheet.getWorkedTimeForA17Vacations()));
	if (workDaySheet.isDiscountBonus()) {
	    setWorkedDaysWithBonusDaysDiscount(getWorkedDaysWithBonusDaysDiscount() + 1);
	}
	if (workDaySheet.isWorkingDayWithDiscountA17()) {
	    setWorkedDaysWithA17DaysDiscount(getWorkedDaysWithA17DaysDiscount() + 1);
	}
	if (workDaySheet.isWorkingDayWithDiscountA17Vacations()) {
	    setWorkedDaysWithA17VacationsDaysDiscount(getWorkedDaysWithA17VacationsDaysDiscount() + 1);
	}
	setComplementaryWeeklyRest(getComplementaryWeeklyRest().plus(
		new Duration(Math.min(roundToHalfHour(workDaySheet.getComplementaryWeeklyRest()).getMillis(),
			Assiduousness.normalWorkDayDuration.getMillis()))));

	setWeeklyRest(getWeeklyRest().plus(
		new Duration(Math.min(roundToHalfHour(workDaySheet.getWeeklyRest()).getMillis(),
			Assiduousness.normalWorkDayDuration.getMillis()))));
	setHolidayRest(getHolidayRest().plus(
		new Duration(Math.min(roundToHalfHour(workDaySheet.getHolidayRest()).getMillis(),
			Assiduousness.normalWorkDayDuration.getMillis()))));

	if (workDaySheet.getWorkSchedule() != null) {
	    addRoundedValueToMap(extra125Map, workDaySheet.getWorkSchedule().getWorkScheduleType(), workDaySheet
		    .getOvertimeFirstLevel());
	    addRoundedValueToMap(extra150Map, workDaySheet.getWorkSchedule().getWorkScheduleType(), workDaySheet
		    .getOvertimeSecondLevel());
	    addRoundedValueToMap(extra150WithLimitsMap, workDaySheet.getWorkSchedule().getWorkScheduleType(), workDaySheet
		    .getOvertimeSecondLevelWithLimit());
	    addRoundedValueToMap(extraNight160Map, workDaySheet.getWorkSchedule().getWorkScheduleType(), workDaySheet
		    .getNightlyOvertimeFirstLevel());
	    addRoundedValueToMap(extraNight190Map, workDaySheet.getWorkSchedule().getWorkScheduleType(), workDaySheet
		    .getNightlyOvertimeSecondLevel());
	    addRoundedValueToMap(extraNight190WithLimitsMap, workDaySheet.getWorkSchedule().getWorkScheduleType(), workDaySheet
		    .getNightlyOvertimeSecondLevelWithLimit());
	    addRoundedValueToMap(extra25Map, workDaySheet.getWorkSchedule().getWorkScheduleType(), workDaySheet
		    .getNightWorkBalance());
	    addToMap(unjustifiedMap, workDaySheet.getWorkSchedule().getWorkScheduleType(), workDaySheet.getUnjustifiedTime());
	}
	if (workDaySheet.hasNightWork()) {
	    Integer value = extraWorkNightsMap.get(workDaySheet.getWorkSchedule().getWorkScheduleType());
	    if (value == null) {
		value = 0;
	    }
	    extraWorkNightsMap.put(workDaySheet.getWorkSchedule().getWorkScheduleType(), value + 1);
	}

	for (JustificationMotive justificationMotive : workDaySheet.getJustificationsDuration().keySet()) {
	    Duration duration = workDaySheet.getJustificationsDuration().get(justificationMotive);
	    Duration oldDuration = justificationsDuration.get(justificationMotive);
	    if (oldDuration != null) {
		duration = duration.plus(oldDuration);
	    }
	    justificationsDuration.put(justificationMotive, duration);
	}
    }

    public void setAccumulatedUnjustifiedAndArticle66() {
	AssiduousnessClosedMonth lastAssiduousnessClosedMonth = getPreviousAssiduousnessClosedMonth();
	double unjustified = 0;
	double a66 = 0;
	double previousAccumulatedA66 = 0;
	double previousUnjustified = 0;
	double previousNotCompleteA66 = 0;
	double previousNotCompleteUnjustified = 0;

	if (lastAssiduousnessClosedMonth != null) {
	    previousAccumulatedA66 = lastAssiduousnessClosedMonth.getAccumulatedArticle66();
	    previousNotCompleteA66 = previousAccumulatedA66 - (int) previousAccumulatedA66;
	    previousUnjustified = lastAssiduousnessClosedMonth.getAccumulatedUnjustified();
	    previousNotCompleteUnjustified = previousUnjustified - (int) previousUnjustified;
	}
	unjustified = getTotalUnjustifiedPercentage(beginDate, endDate);

	unjustified = NumberUtils.formatDoubleWithoutRound(unjustified, 1);

	double anualRemaining = Assiduousness.MAX_A66_PER_YEAR - previousAccumulatedA66;
	double monthRemaining = anualRemaining > Assiduousness.MAX_A66_PER_MONTH ? Assiduousness.MAX_A66_PER_MONTH
		: anualRemaining;
	if (getArticle66().doubleValue() < monthRemaining && unjustified > 0) {
	    monthRemaining = monthRemaining - getArticle66().doubleValue();
	    if (unjustified <= monthRemaining) {
		a66 = unjustified;
		unjustified = 0;
	    } else {
		unjustified -= monthRemaining;
		a66 = monthRemaining;
	    }
	}
	a66 = NumberUtils.formatDoubleWithoutRound(a66, 1);
	setAccumulatedArticle66(new BigDecimal(previousAccumulatedA66 + a66));
	setAccumulatedUnjustified(new BigDecimal(previousUnjustified + unjustified));

	int A66ToDiscount = (int) (a66 + previousNotCompleteA66);
	int unjustifiedToDiscount = (int) (unjustified + previousNotCompleteUnjustified);
	setAccumulatedUnjustifiedDays((int) Math.floor(unjustifiedToDiscount));
	setAccumulatedArticle66Days((int) Math.floor(A66ToDiscount));
    }

    public AssiduousnessClosedMonth getPreviousAssiduousnessClosedMonth() {
	int previousMonth = getBeginDate().get(DateTimeFieldType.monthOfYear()) - 1;
	if (previousMonth <= 0) {
	    return null;
	}
	ClosedMonth previousClosedMonth = ClosedMonth.getClosedMonthForBalance(new YearMonth(getBeginDate().get(
		DateTimeFieldType.year()), previousMonth));
	if (previousClosedMonth != null) {
	    return previousClosedMonth.getAssiduousnessClosedMonth(getLastAssiduousnessStatusHistory());
	}
	return null;
    }

    private double getTotalUnjustifiedPercentage(LocalDate beginDate, LocalDate endDate) {
	double unjustified = 0;
	Duration balanceWithoutDiscount = getTotalBalance().plus(getBalanceToCompensate());
	Duration averageWorkTimeDuration = getEmployee().getAssiduousness().getAverageWorkTimeDuration(beginDate, endDate);
	if (!balanceWithoutDiscount.isShorterThan(Duration.ZERO)) {
	    unjustified = getUnjustifiedPercentage();
	} else {
	    Duration unjustifiedTotalDuration = Duration.ZERO;
	    for (Duration duration : getUnjustifiedMap().values()) {
		unjustifiedTotalDuration = unjustifiedTotalDuration.plus(duration);
	    }
	    long balanceToProcess = Math.abs(balanceWithoutDiscount.getMillis()) > unjustifiedTotalDuration.getMillis() ? Math
		    .abs(balanceWithoutDiscount.getMillis()) : unjustifiedTotalDuration.getMillis();
	    long balanceAfterTolerance = balanceToProcess - Assiduousness.IST_TOLERANCE_TIME.getMillis();
	    if (balanceAfterTolerance > 0) {
		unjustified = (double) balanceAfterTolerance / (double) averageWorkTimeDuration.getMillis();
	    }
	}

	if (beginDate.getMonthOfYear() == 12
		&& getEmployeeBalanceResume().getFutureBalanceToCompensate().isLongerThan(Duration.ZERO)) {
	    unjustified = unjustified
		    + (getEmployeeBalanceResume().getFutureBalanceToCompensate().getMillis() / (double) averageWorkTimeDuration
			    .getMillis());
	}
	return unjustified;
    }

    private double getUnjustifiedPercentage() {
	double unjustified = 0;
	long tempIstTorelanceTime = Assiduousness.IST_TOLERANCE_TIME.getMillis();
	for (WorkScheduleType workScheduleType : getUnjustifiedMap().keySet()) {
	    Duration unjustifiedDuration = getUnjustifiedMap().get(workScheduleType);
	    if (unjustifiedDuration.isLongerThan(Duration.ZERO)) {
		long unjustifiedAfterTolerance = unjustifiedDuration.getMillis() - tempIstTorelanceTime;
		if (unjustifiedAfterTolerance > 0) {
		    unjustified += (unjustifiedAfterTolerance / (double) workScheduleType.getNormalWorkPeriod()
			    .getWorkPeriodDuration().getMillis());
		}
		tempIstTorelanceTime = unjustifiedAfterTolerance > 0 ? 0 : tempIstTorelanceTime - unjustifiedDuration.getMillis();
	    }
	}
	return unjustified;
    }

    private void addToMap(HashMap<WorkScheduleType, Duration> map, WorkScheduleType key, Duration duration) {
	if (duration != null) {
	    Duration oldDuration = map.get(key);
	    if (oldDuration != null) {
		duration = duration.plus(oldDuration);
	    }
	    map.put(key, duration);
	}
    }

    private void addRoundedValueToMap(HashMap<WorkScheduleType, Duration> map, WorkScheduleType key, Duration duration) {
	if (duration != null) {
	    Duration oldDuration = map.get(key);
	    if (oldDuration == null) {
		oldDuration = Duration.ZERO;
	    }
	    map.put(key, roundToHalfHour(duration).plus(oldDuration));
	}
    }

    private Duration roundToHalfHour(Duration duration) {
	if (duration.toPeriod(PeriodType.dayTime()).getMinutes() >= 30) {
	    return new Duration(Hours.hours(duration.toPeriod(PeriodType.dayTime()).getHours() + 1).toStandardDuration());
	}
	Duration result = Hours.hours(duration.toPeriod(PeriodType.dayTime()).getHours()).toStandardDuration();
	if (duration.toPeriod(PeriodType.dayTime()).getMinutes() != 0) {
	    result = result.plus(midHourDuration);
	}
	return result;
    }

    public Duration getComplementaryWeeklyRest() {
	return complementaryWeeklyRest == null ? Duration.ZERO : complementaryWeeklyRest;
    }

    public void setComplementaryWeeklyRest(Duration complementaryWeeklyRest) {
	this.complementaryWeeklyRest = complementaryWeeklyRest;
    }

    public Duration getWeeklyRest() {
	return weeklyRest == null ? Duration.ZERO : weeklyRest;
    }

    public void setWeeklyRest(Duration weeklyRest) {
	this.weeklyRest = weeklyRest;
    }

    public Duration getNightWork() {
	return nightWork;
    }

    public void setNightWork(Duration nightWork) {
	this.nightWork = nightWork;
    }

    public String getComplementaryWeeklyRestString() {
	PeriodFormatter fmt = new PeriodFormatterBuilder().printZeroAlways().appendHours().appendSeparator(":")
		.minimumPrintedDigits(2).appendMinutes().toFormatter();
	MutablePeriod finalComplementaryWeeklyRestWork = new MutablePeriod(getComplementaryWeeklyRest().getMillis(), PeriodType
		.time());
	return fmt.print(finalComplementaryWeeklyRestWork);
    }

    public String getWeeklyRestString() {
	PeriodFormatter fmt = new PeriodFormatterBuilder().printZeroAlways().appendHours().appendSeparator(":")
		.minimumPrintedDigits(2).appendMinutes().toFormatter();
	MutablePeriod finalWeeklyRestExtraWork = new MutablePeriod(getWeeklyRest().getMillis(), PeriodType.time());
	return fmt.print(finalWeeklyRestExtraWork);
    }

    public String getHolidayRestString() {
	PeriodFormatter fmt = new PeriodFormatterBuilder().printZeroAlways().appendHours().appendSeparator(":")
		.minimumPrintedDigits(2).appendMinutes().toFormatter();
	MutablePeriod finalHolidayRestExtraWork = new MutablePeriod(getHolidayRest().getMillis(), PeriodType.time());
	return fmt.print(finalHolidayRestExtraWork);
    }

    public Duration getHolidayRest() {
	return holidayRest == null ? Duration.ZERO : holidayRest;
    }

    public void setHolidayRest(Duration holidayWork) {
	this.holidayRest = holidayWork;
    }

    public Duration getBalanceToCompensate() {
	return balanceToCompensate == null ? Duration.ZERO : balanceToCompensate;
    }

    public void setBalanceToCompensate(Duration balanceToCompensate) {
	this.balanceToCompensate = balanceToCompensate;
    }

    public AssiduousnessStatusHistory getLastAssiduousnessStatusHistory() {
	return lastAssiduousnessStatusHistory;
    }

    public void setLastAssiduousnessStatusHistory(AssiduousnessStatusHistory lastAssiduousnessStatusHistory) {
	this.lastAssiduousnessStatusHistory = lastAssiduousnessStatusHistory;
    }

    public void setLastAssiduousnessStatusHistory(LocalDate beginDate, LocalDate endDate) {
	for (AssiduousnessStatusHistory assiduousnessStatusHistory : getEmployee().getAssiduousness().getStatusBetween(beginDate,
		endDate)) {
	    if (assiduousnessStatusHistory.getEndDate() != null) {
		if (lastAssiduousnessStatusHistory == null
			|| assiduousnessStatusHistory.getEndDate().isAfter(lastAssiduousnessStatusHistory.getEndDate())) {
		    this.lastAssiduousnessStatusHistory = assiduousnessStatusHistory;
		}
	    } else {
		this.lastAssiduousnessStatusHistory = assiduousnessStatusHistory;
		break;
	    }
	}
    }

    public void addWorkDaySheets(AssiduousnessClosedMonth assiduousnessClosedMonth, LocalDate beginDate, LocalDate endDate) {
	if (assiduousnessClosedMonth != null) {
	    Interval interval = new Interval(beginDate.toDateTimeAtStartOfDay(), endDate.toDateTimeAtStartOfDay().plusMillis(1));
	    for (AssiduousnessClosedDay assiduousnessClosedDay : assiduousnessClosedMonth.getAssiduousnessClosedDays()) {
		if (interval.contains(assiduousnessClosedDay.getDay().toDateTimeAtStartOfDay())) {
		    getWorkDaySheetList().add(new WorkDaySheet(assiduousnessClosedDay));
		}
	    }
	    Collections.sort(getWorkDaySheetList(), new BeanComparator("date"));
	    setTotalBalance(getTotalBalance().plus(assiduousnessClosedMonth.getBalance()));
	    setUnjustifiedBalance(getUnjustifiedBalance().plus(assiduousnessClosedMonth.getTotalUnjustifiedBalance()));
	    setComplementaryWeeklyRest(getComplementaryWeeklyRest().plus(assiduousnessClosedMonth.getSaturdayBalance()));
	    setWeeklyRest(getWeeklyRest().plus(assiduousnessClosedMonth.getSundayBalance()));
	    setHolidayRest(getHolidayRest().plus(assiduousnessClosedMonth.getHolidayBalance()));
	    setUnjustifiedDays(getUnjustifiedDays() + assiduousnessClosedMonth.getUnjustifiedDays());
	    if (assiduousnessClosedMonth.getAccumulatedUnjustifiedDays() != null) {
		setAccumulatedUnjustifiedDays(getAccumulatedUnjustifiedDays()
			+ assiduousnessClosedMonth.getAccumulatedUnjustifiedDays());
	    }
	    if (assiduousnessClosedMonth.getAccumulatedArticle66Days() != null) {
		setAccumulatedArticle66Days(getAccumulatedArticle66Days()
			+ assiduousnessClosedMonth.getAccumulatedArticle66Days());
	    }
	    setBalanceToCompensate(getBalanceToCompensate().plus(assiduousnessClosedMonth.getBalanceToDiscount()));
	    setMaximumWorkingDays(getMaximumWorkingDays() + assiduousnessClosedMonth.getMaximumWorkingDays());
	    setTotalWorkedTime(getTotalWorkedTime().plus(assiduousnessClosedMonth.getTotalWorkedTime()));
	    setTotalWorkedTimeForA17Vacations(getTotalWorkedTimeForA17Vacations().plus(
		    assiduousnessClosedMonth.getTotalWorkedTimeForA17Vacations()));
	    setWorkedDaysWithBonusDaysDiscount(getWorkedDaysWithBonusDaysDiscount()
		    + assiduousnessClosedMonth.getWorkedDaysWithBonusDaysDiscount());
	    setWorkedDaysWithA17DaysDiscount(getWorkedDaysWithA17DaysDiscount()
		    + assiduousnessClosedMonth.getWorkedDaysWithA17DaysDiscount());
	    setWorkedDaysWithA17VacationsDaysDiscount(getWorkedDaysWithA17VacationsDaysDiscount()
		    + assiduousnessClosedMonth.getWorkedDaysWithA17VacationsDaysDiscount());

	    setMultipleMonthBalance(assiduousnessClosedMonth.getBalanceToDiscount());
	    setVacations(getVacations().add(new BigDecimal(assiduousnessClosedMonth.getVacations())));
	    setTolerance(getTolerance().add(new BigDecimal(assiduousnessClosedMonth.getTolerance())));
	    setArticle17(getArticle17().add(new BigDecimal(assiduousnessClosedMonth.getArticle17())));
	    setArticle66(getArticle66().add(new BigDecimal(assiduousnessClosedMonth.getArticle66())));

	    for (AssiduousnessExtraWork assiduousnessExtraWork : assiduousnessClosedMonth.getAssiduousnessExtraWorks()) {
		addToMap(extra125Map, assiduousnessExtraWork.getWorkScheduleType(), assiduousnessExtraWork.getFirstLevelBalance());
		addToMap(extra150Map, assiduousnessExtraWork.getWorkScheduleType(), assiduousnessExtraWork
			.getSecondLevelBalance());
		addToMap(extra150WithLimitsMap, assiduousnessExtraWork.getWorkScheduleType(), assiduousnessExtraWork
			.getSecondLevelBalanceWithLimit());
		addToMap(extraNight160Map, assiduousnessExtraWork.getWorkScheduleType(), assiduousnessExtraWork
			.getFirstLevelNightBalance());
		addToMap(extraNight190Map, assiduousnessExtraWork.getWorkScheduleType(), assiduousnessExtraWork
			.getSecondLevelNightBalance());
		addToMap(extraNight190WithLimitsMap, assiduousnessExtraWork.getWorkScheduleType(), assiduousnessExtraWork
			.getSecondLevelNightBalanceWithLimit());
		addToMap(extra25Map, assiduousnessExtraWork.getWorkScheduleType(), assiduousnessExtraWork.getNightBalance());
		addToMap(unjustifiedMap, assiduousnessExtraWork.getWorkScheduleType(), assiduousnessExtraWork.getUnjustified());

		if (assiduousnessExtraWork.getNightExtraWorkDays() != 0) {
		    Integer value = extraWorkNightsMap.get(extraWorkNightsMap);
		    if (value == null) {
			value = 0;
		    }
		    extraWorkNightsMap.put(assiduousnessExtraWork.getWorkScheduleType(), value
			    + assiduousnessExtraWork.getNightExtraWorkDays());
		}
	    }

	    for (ClosedMonthJustification closedMonthJustification : assiduousnessClosedMonth.getClosedMonthJustifications()) {
		Duration duration = closedMonthJustification.getJustificationDuration();
		Duration oldDuration = justificationsDuration.get(closedMonthJustification.getJustificationMotive());
		if (oldDuration != null) {
		    duration = duration.plus(oldDuration);
		}
		justificationsDuration.put(closedMonthJustification.getJustificationMotive(), duration);
	    }
	}
    }

    public Integer getUnjustifiedDays() {
	return unjustifiedDays == null ? 0 : unjustifiedDays;
    }

    public void setUnjustifiedDays(Integer unjustifiedDays) {
	this.unjustifiedDays = unjustifiedDays;
    }

    public Integer getAccumulatedUnjustifiedDays() {
	return accumulatedUnjustifiedDays == null ? 0 : accumulatedUnjustifiedDays;
    }

    public void setAccumulatedUnjustifiedDays(Integer accumulatedUnjustifiedDays) {
	this.accumulatedUnjustifiedDays = accumulatedUnjustifiedDays;
    }

    public Integer getAccumulatedArticle66Days() {
	return accumulatedArticle66Days == null ? 0 : accumulatedArticle66Days;
    }

    public void setAccumulatedArticle66Days(Integer accumulatedArticle66Days) {
	this.accumulatedArticle66Days = accumulatedArticle66Days;
    }

    public WorkDaySheet getWorkDaySheet(LocalDate date) {
	for (WorkDaySheet workDaySheet : getWorkDaySheetList()) {
	    if (workDaySheet.getDate().equals(date)) {
		return workDaySheet;
	    }
	}
	return null;
    }

    public BigDecimal getVacations() {
	return vacations == null ? BigDecimal.ZERO : vacations;
    }

    public void setVacations(BigDecimal vacations) {
	this.vacations = vacations;
    }

    public BigDecimal getTolerance() {
	return tolerance == null ? BigDecimal.ZERO : tolerance;
    }

    public void setTolerance(BigDecimal tolerance) {
	this.tolerance = tolerance;
    }

    public BigDecimal getArticle17() {
	return article17 == null ? BigDecimal.ZERO : article17;
    }

    public void setArticle17(BigDecimal article17) {
	this.article17 = article17;
    }

    public BigDecimal getArticle66() {
	return article66 == null ? BigDecimal.ZERO : article66;
    }

    public void setArticle66(BigDecimal article66) {
	this.article66 = article66;
    }

    public Duration getTotalWorkedTime() {
	return totalWorkedTime == null ? Duration.ZERO : totalWorkedTime;
    }

    public void setTotalWorkedTime(Duration totalWorkedTime) {
	this.totalWorkedTime = totalWorkedTime;
    }

    public Duration getTotalWorkedTimeForA17Vacations() {
	return totalWorkedTimeForA17Vacations == null ? Duration.ZERO : totalWorkedTimeForA17Vacations;
    }

    public void setTotalWorkedTimeForA17Vacations(Duration totalWorkedTimeForA17Vacations) {
	this.totalWorkedTimeForA17Vacations = totalWorkedTimeForA17Vacations;
    }

    public Integer getMaximumWorkingDays() {
	return maximumWorkingDays == null ? 0 : maximumWorkingDays;
    }

    public void setMaximumWorkingDays(Integer maximumWorkingDays) {
	this.maximumWorkingDays = maximumWorkingDays;
    }

    public Integer getWorkedDaysWithBonusDaysDiscount() {
	return workedDaysWithBonusDaysDiscount == null ? 0 : workedDaysWithBonusDaysDiscount;
    }

    public void setWorkedDaysWithBonusDaysDiscount(Integer workedDaysWithBonusDaysDiscount) {
	this.workedDaysWithBonusDaysDiscount = workedDaysWithBonusDaysDiscount;
    }

    public Integer getWorkedDaysWithA17VacationsDaysDiscount() {
	return workedDaysWithA17VacationsDaysDiscount == null ? 0 : workedDaysWithA17VacationsDaysDiscount;
    }

    public void setWorkedDaysWithA17VacationsDaysDiscount(Integer workedDaysWithA17VacationsDaysDiscount) {
	this.workedDaysWithA17VacationsDaysDiscount = workedDaysWithA17VacationsDaysDiscount;
    }

    public Integer getWorkedDaysWithA17DaysDiscount() {
	return workedDaysWithA17DaysDiscount == null ? 0 : workedDaysWithA17DaysDiscount;
    }

    public void setWorkedDaysWithA17DaysDiscount(Integer workedDaysWithA17DaysDiscount) {
	this.workedDaysWithA17DaysDiscount = workedDaysWithA17DaysDiscount;
    }

    public HashMap<WorkScheduleType, Duration> getExtra125Map() {
	return extra125Map;
    }

    public void setExtra125Map(HashMap<WorkScheduleType, Duration> extra125Map) {
	this.extra125Map = extra125Map;
    }

    public HashMap<WorkScheduleType, Duration> getExtra150Map() {
	return extra150Map;
    }

    public void setExtra150Map(HashMap<WorkScheduleType, Duration> extra150Map) {
	this.extra150Map = extra150Map;
    }

    public HashMap<WorkScheduleType, Duration> getExtra150WithLimitsMap() {
	return extra150WithLimitsMap;
    }

    public void setExtra150WithLimitsMap(HashMap<WorkScheduleType, Duration> extra150WithLimitsMap) {
	this.extra150WithLimitsMap = extra150WithLimitsMap;
    }

    public HashMap<WorkScheduleType, Duration> getExtraNight160Map() {
	return extraNight160Map;
    }

    public void setExtraNight160Map(HashMap<WorkScheduleType, Duration> extraNight160Map) {
	this.extraNight160Map = extraNight160Map;
    }

    public HashMap<WorkScheduleType, Duration> getExtraNight190Map() {
	return extraNight190Map;
    }

    public void setExtraNight190Map(HashMap<WorkScheduleType, Duration> extraNight190Map) {
	this.extraNight190Map = extraNight190Map;
    }

    public HashMap<WorkScheduleType, Duration> getExtraNight190WithLimitsMap() {
	return extraNight190WithLimitsMap;
    }

    public void setExtraNight190WithLimitsMap(HashMap<WorkScheduleType, Duration> extraNight190WithLimitsMap) {
	this.extraNight190WithLimitsMap = extraNight190WithLimitsMap;
    }

    public HashMap<WorkScheduleType, Integer> getExtraWorkNightsMap() {
	return extraWorkNightsMap;
    }

    public void setExtraWorkNightsMap(HashMap<WorkScheduleType, Integer> extraWorkNightsMap) {
	this.extraWorkNightsMap = extraWorkNightsMap;
    }

    public HashMap<WorkScheduleType, Duration> getExtra25Map() {
	return extra25Map;
    }

    public void setExtra25Map(HashMap<WorkScheduleType, Duration> extra25Map) {
	this.extra25Map = extra25Map;
    }

    public HashMap<WorkScheduleType, Duration> getUnjustifiedMap() {
	return unjustifiedMap;
    }

    public void setUnjustifiedMap(HashMap<WorkScheduleType, Duration> unjustifiedMap) {
	this.unjustifiedMap = unjustifiedMap;
    }

    public HashMap<JustificationMotive, Duration> getJustificationsDuration() {
	return justificationsDuration;
    }

    public void setJustificationsDuration(HashMap<JustificationMotive, Duration> justificationsDuration) {
	this.justificationsDuration = justificationsDuration;
    }

    public Duration getMultipleMonthBalance() {
	return multipleMonthBalance == null ? Duration.ZERO : multipleMonthBalance;
    }

    public void setMultipleMonthBalance(Duration multipleMonthBalance) {
	this.multipleMonthBalance = multipleMonthBalance;
    }

    public BigDecimal getAccumulatedArticle66() {
	return accumulatedArticle66;
    }

    public void setAccumulatedArticle66(BigDecimal accumulatedArticle66) {
	this.accumulatedArticle66 = accumulatedArticle66;
    }

    public BigDecimal getAccumulatedUnjustified() {
	return accumulatedUnjustified;
    }

    public void setAccumulatedUnjustified(BigDecimal accumulatedUnjustified) {
	this.accumulatedUnjustified = accumulatedUnjustified;
    }

    public Integer getUnjustifiedDaysLeave() {
	return unjustifiedDaysLeave == null ? 0 : unjustifiedDaysLeave;
    }

    public void setUnjustifiedDaysLeave(Integer unjustifiedDaysLeave) {
	this.unjustifiedDaysLeave = unjustifiedDaysLeave;
    }

    public EmployeeBalanceResume getEmployeeBalanceResume() {
	return employeeBalanceResume;
    }

    public void setEmployeeBalanceResume(EmployeeBalanceResume employeeBalanceResume) {
	this.employeeBalanceResume = employeeBalanceResume;
    }

    public LocalDate getBeginDate() {
	return beginDate;
    }

    public void setBeginDate(LocalDate beginDate) {
	this.beginDate = beginDate;
    }

    public LocalDate getEndDate() {
	return endDate;
    }

    public void setEndDate(LocalDate endDate) {
	this.endDate = endDate;
    }

}
