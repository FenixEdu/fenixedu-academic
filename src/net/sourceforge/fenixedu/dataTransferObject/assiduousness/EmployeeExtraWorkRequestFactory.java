package net.sourceforge.fenixedu.dataTransferObject.assiduousness;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.assiduousness.ClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.EmployeeExtraWorkAuthorization;
import net.sourceforge.fenixedu.domain.assiduousness.ExtraWorkRequest;
import net.sourceforge.fenixedu.domain.assiduousness.Schedule;
import net.sourceforge.fenixedu.domain.assiduousness.WorkSchedule;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.struts.action.ActionMessage;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.joda.time.PeriodType;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public class EmployeeExtraWorkRequestFactory implements Serializable, FactoryExecutor {
    private Integer nightHours;

    private Integer extraNightHours;

    private Integer extraNightDays;

    private Integer holidayHours;

    private Integer saturdayHours;

    private Integer sundayHours;

    private Integer workdayHours;

    private Integer workdayHoursFirstLevel;

    private Integer workdayHoursSecondLevel;

    private Boolean addToVacations = Boolean.FALSE;

    private Boolean addToWeekRestTime = Boolean.FALSE;

    private Boolean approved;

    private Double amount = 0.0;

    private DomainReference<Employee> modifiedBy;

    private DomainReference<Employee> employee;

    private DomainReference<ExtraWorkRequest> extraWorkRequest;

    private ExtraWorkRequestFactory extraWorkRequestFactory;

    private Integer totalVacationDays;

    private Integer requestedNightHours;

    private Integer requestedExtraNightHours;

    private Integer requestedHolidayHours;

    private Integer requestedSaturdayHours;

    private Integer requestedSundayHours;

    private Integer requestedWorkdayHours;

    public static final int YEAR_HOUR_LIMIT = 100;

    public static final double nightExtraWorkPercentage = 0.25;

    public static final double firstHourPercentage = 1.25;

    public static final double secondHourPercentage = 1.50;

    public static final double weeklyRestPercentage = 2;

    public static final double extraWorkDayToVacationsCoefficient = 1.25;

    public static final double nightExtraWorkToVacationsCoefficient = 1.50;

    public static final double paymentLimitCoefficient = 3;

    public static final double executiveAuxiliarPaymentLimitPercentage = 0.6;

    protected DecimalFormat decimalFormat;

    public EmployeeExtraWorkRequestFactory(Employee employee, ExtraWorkRequestFactory extraWorkRequestFactory) {
	super();
	setExtraWorkRequestFactory(extraWorkRequestFactory);
	setEmployee(employee);
	setDecimalFormat();
    }

    public EmployeeExtraWorkRequestFactory(ExtraWorkRequest extraWorkRequest, ExtraWorkRequestFactory extraWorkRequestFactory) {
	super();
	setExtraWorkRequestFactory(extraWorkRequestFactory);
	setExtraWorkRequest(extraWorkRequest);
	setEmployee(extraWorkRequest.getAssiduousness().getEmployee());
	setNightHours(extraWorkRequest.getNightHours());
	setExtraNightHours(extraWorkRequest.getExtraNightHours());
	setExtraNightDays(extraWorkRequest.getExtraNightDays());
	setHolidayHours(extraWorkRequest.getHolidayHours());
	setSaturdayHours(extraWorkRequest.getSaturdayHours());
	setSundayHours(extraWorkRequest.getSundayHours());
	setWorkdayHours(extraWorkRequest.getWorkdayHours());
	setRequestedNightHours(extraWorkRequest.getRequestedNightHours());
	setRequestedExtraNightHours(extraWorkRequest.getRequestedExtraNightHours());
	setRequestedHolidayHours(extraWorkRequest.getRequestedHolidayHours());
	setRequestedSaturdayHours(extraWorkRequest.getRequestedSaturdayHours());
	setRequestedSundayHours(extraWorkRequest.getRequestedSundayHours());
	setRequestedWorkdayHours(extraWorkRequest.getRequestedWorkdayHours());
	setAddToVacations(extraWorkRequest.getAddToVacations());
	setAddToWeekRestTime(extraWorkRequest.getAddToWeekRestTime());
	setApproved(extraWorkRequest.getApproved());
	setAmount(extraWorkRequest.getAmount());
	setTotalVacationDays(extraWorkRequest.getNormalVacationsDays() + extraWorkRequest.getNightVacationsDays());
	setDecimalFormat();
    }

    private void setDecimalFormat() {
	decimalFormat = new DecimalFormat("0.00");
	DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
	decimalFormatSymbols.setDecimalSeparator('.');
	decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
    }

    public Employee getModifiedBy() {
	return modifiedBy == null ? null : modifiedBy.getObject();
    }

    public void setModifiedBy(Employee modifiedBy) {
	if (modifiedBy != null) {
	    this.modifiedBy = new DomainReference<Employee>(modifiedBy);
	}
    }

    public Employee getEmployee() {
	return employee == null ? null : employee.getObject();
    }

    public void setEmployee(Employee employee) {
	if (employee != null) {
	    this.employee = new DomainReference<Employee>(employee);
	}
    }

    public Boolean getAddToVacations() {
	return addToVacations;
    }

    public void setAddToVacations(Boolean addToVacations) {
	this.addToVacations = addToVacations;
    }

    public Boolean getAddToWeekRestTime() {
	return addToWeekRestTime;
    }

    public void setAddToWeekRestTime(Boolean addToWeekRestTime) {
	this.addToWeekRestTime = addToWeekRestTime;
    }

    public Integer getExtraNightDays() {
	return extraNightDays;
    }

    public void setExtraNightDays(Integer extraNightDays) {
	this.extraNightDays = extraNightDays;
    }

    public Integer getExtraNightHours() {
	return extraNightHours;
    }

    public void setExtraNightHours(Integer extraNightHours) {
	this.extraNightHours = extraNightHours;
    }

    public Integer getHolidayHours() {
	return holidayHours;
    }

    public void setHolidayHours(Integer holidayHours) {
	this.holidayHours = holidayHours;
    }

    public Integer getNightHours() {
	return nightHours;
    }

    public void setNightHours(Integer nightHours) {
	this.nightHours = nightHours;
    }

    public Integer getSaturdayHours() {
	return saturdayHours;
    }

    public void setSaturdayHours(Integer saturdayHours) {
	this.saturdayHours = saturdayHours;
    }

    public Integer getSundayHours() {
	return sundayHours;
    }

    public void setSundayHours(Integer sundayHours) {
	this.sundayHours = sundayHours;
    }

    public Integer getWorkdayHours() {
	return workdayHours;
    }

    public void setWorkdayHours(Integer workdayHours) {
	this.workdayHours = workdayHours;
    }

    public ExtraWorkRequest getExtraWorkRequest() {
	return extraWorkRequest == null ? null : extraWorkRequest.getObject();
    }

    public Integer getIdInternal() {
	return getExtraWorkRequest() != null ? getExtraWorkRequest().getIdInternal() : null;
    }

    public void setExtraWorkRequest(ExtraWorkRequest extraWorkRequest) {
	if (extraWorkRequest != null) {
	    this.extraWorkRequest = new DomainReference<ExtraWorkRequest>(extraWorkRequest);
	}
    }

    public Object execute() {
	if (getEmployee() != null) {
	    if (isEmptyPaymentRequest()) {
		return new ActionMessage("error.extraWorkRequest.emptyPaymentRequest");
	    }
	    if (!validateExtraNightWorkFields()) {
		return new ActionMessage("error.extraWorkRequest.invalidExtraNightWorkFields");
	    }
	    if (!validateRemunerationOptionFields()) {
		return new ActionMessage("error.extraWorkRequest.invalidRemunerationOptionFields");
	    }
	    if (!validateWorkingDaysExtraWorkNightHours()) {
		return new ActionMessage("error.extraWorkRequest.invalidWorkingDaysExtraWorkRemuneration");
	    }
	    if (validateNonWorkingDaysExtraWork()) {
		return new ActionMessage("error.extraWorkRequest.invalidNonWorkingDaysExtraWorkRemuneration");
	    }

	    BigDecimal hourValue = new BigDecimal(0);
	    BigDecimal monthValue = new BigDecimal(0);
	    LocalDate date = new LocalDate(
		    getExtraWorkRequestFactory().getHoursDoneInPartialDate().get(DateTimeFieldType.year()),
		    getExtraWorkRequestFactory().getHoursDoneInPartialDate().get(DateTimeFieldType.monthOfYear()), 1);
	    try {
		hourValue = getEmployee().getAssiduousness().getEmployeeHourValue(date);
		monthValue = getEmployee().getAssiduousness().getEmployeeSalary(date);
	    } catch (ExcepcaoPersistencia e) {
		e.printStackTrace();
		return new ActionMessage("error.connectionError");
	    }

	    ActionMessage actionMessage = validateBalances(hourValue, monthValue);
	    if (actionMessage != null) {
		return actionMessage;
	    }

	    Double holidayAmount = new Double(decimalFormat.format(hourValue.doubleValue() * weeklyRestPercentage
		    * (getHolidayHours() != null ? getHolidayHours() : 0)));
	    Double saturdayAmount = new Double(decimalFormat.format(hourValue.doubleValue() * weeklyRestPercentage
		    * (getSaturdayHours() != null ? getSaturdayHours() : 0)));
	    Double sundayAmount = new Double(decimalFormat.format(hourValue.doubleValue() * weeklyRestPercentage
		    * (getSundayHours() != null ? getSundayHours() : 0)));

	    Double workdayHoursFirstLevelAmount = new Double(0);
	    Double workdayHoursSecondLevelAmount = new Double(0);
	    if (!getAddToVacations() && getWorkdayHours() != null) {
		workdayHoursFirstLevelAmount = getAmount(hourValue, firstHourPercentage, getWorkdayHoursFirstLevel());
		workdayHoursSecondLevelAmount = getAmount(hourValue, secondHourPercentage, getWorkdayHoursSecondLevel());
	    }

	    Integer thisNormalVacationsDays = 0;
	    Double thisNormalVacationsAmount = 0.0;
	    Double accumulatedNormalVacationsAmount = 0.0;

	    Integer thisNightVacationsDays = 0;
	    Double thisNightVacationsAmount = 0.0;
	    Double accumulatedNightVacationsAmount = 0.0;

	    if (getAddToVacations() && (getWorkdayHours() != null || getExtraNightHours() != null)) {
		LocalDate day = new LocalDate(getExtraWorkRequestFactory().getHoursDoneInPartialDate().get(
			DateTimeFieldType.year()), getExtraWorkRequestFactory().getHoursDoneInPartialDate().get(
			DateTimeFieldType.monthOfYear()), 1);
		Schedule schedule = getEmployee().getAssiduousness().getSchedule(day);
		int dayHours = 7;
		if (schedule != null) {
		    dayHours = schedule.getEqualWorkPeriodDuration().toPeriod(PeriodType.dayTime()).getHours();
		}

		Double[] accumulated = getLastVacationsAccumulated();
		thisNormalVacationsAmount = getWorkdayHours() == null ? 0 : getWorkdayHours()
			* extraWorkDayToVacationsCoefficient;
		double total = thisNormalVacationsAmount + accumulated[0];
		thisNormalVacationsDays = ((int) total) / dayHours;
		accumulatedNormalVacationsAmount = (total - (thisNormalVacationsDays * dayHours));
		if (getExtraNightHours() != null && !hasNightSchedule()) {
		    thisNightVacationsAmount = getExtraNightHours() == null ? 0 : getExtraNightHours()
			    * nightExtraWorkToVacationsCoefficient;
		    total = thisNightVacationsAmount + accumulated[1];
		    thisNightVacationsDays = ((int) total) / dayHours;
		    accumulatedNightVacationsAmount = (total - (thisNightVacationsDays * dayHours));
		}
	    }

	    setTotalVacationDays(thisNormalVacationsDays + thisNightVacationsDays);

	    if (getExtraWorkRequest() == null) {
		new ExtraWorkRequest(getExtraWorkRequestFactory().getPartialPayingDate(), getExtraWorkRequestFactory().getUnit(),
			getEmployee(), getNightHours(), getExtraNightHours(), getExtraNightDays(), getHolidayHours(),
			getSaturdayHours(), getSundayHours(), getWorkdayHours(), getWorkdayHoursFirstLevel(),
			getWorkdayHoursSecondLevel(), getAddToVacations(), getAddToWeekRestTime(), getModifiedBy(), false,
			getExtraWorkRequestFactory().getHoursDoneInPartialDate(), thisNormalVacationsDays,
			thisNormalVacationsAmount, accumulatedNormalVacationsAmount, thisNightVacationsDays,
			thisNightVacationsAmount, accumulatedNightVacationsAmount, getRequestedNightHours(),
			getRequestedExtraNightHours(), getRequestedHolidayHours(), getRequestedSaturdayHours(),
			getRequestedSundayHours(), getRequestedWorkdayHours(), holidayAmount, saturdayAmount, sundayAmount,
			workdayHoursFirstLevelAmount, workdayHoursSecondLevelAmount);
	    } else {
		if (getExtraWorkRequest().getApproved() && getExtraWorkRequest().getAddToVacations() != getAddToVacations()) {
		    getExtraWorkRequestFactory().setPerformPayment(false);
		    getExtraWorkRequestFactory().execute();
		}
		getExtraWorkRequest().edit(getNightHours(), getExtraNightHours(), getExtraNightDays(), getHolidayHours(),
			getSaturdayHours(), getSundayHours(), getWorkdayHours(), getWorkdayHoursFirstLevel(),
			getWorkdayHoursSecondLevel(), getAddToVacations(), getAddToWeekRestTime(), getModifiedBy(),
			getExtraWorkRequestFactory().getHoursDoneInPartialDate(), thisNormalVacationsDays,
			thisNormalVacationsAmount, accumulatedNormalVacationsAmount, thisNightVacationsDays,
			thisNightVacationsAmount, accumulatedNightVacationsAmount, getRequestedNightHours(),
			getRequestedExtraNightHours(), getRequestedHolidayHours(), getRequestedSaturdayHours(),
			getRequestedSundayHours(), getRequestedWorkdayHours(), holidayAmount, saturdayAmount, sundayAmount,
			workdayHoursFirstLevelAmount, workdayHoursSecondLevelAmount);
		if (getExtraWorkRequest().getApproved() && getExtraWorkRequestFactory().checkUnitMoney() != null) {
		    getExtraWorkRequestFactory().setPerformPayment(false);
		    getExtraWorkRequestFactory().execute();
		}
	    }

	}
	return null;
    }

    private boolean hasNightSchedule() {
	HashMap<LocalDate, WorkSchedule> workSchedules = getEmployee().getAssiduousness().getWorkSchedulesBetweenDates(
		getExtraWorkRequestFactory().getPartialPayingDate());
	for (WorkSchedule workSchedule : workSchedules.values()) {
	    if (workSchedule != null && workSchedule.getWorkScheduleType().isNocturnal()) {
		return true;
	    }
	}
	return false;
    }

    private Double[] getLastVacationsAccumulated() {
	ExtraWorkRequest lastRequest = null;
	for (ExtraWorkRequest request : getEmployee().getAssiduousness().getExtraWorkRequests()) {
	    if (request.getHoursDoneInPartialDate().get(DateTimeFieldType.year()) == getExtraWorkRequestFactory()
		    .getHoursDoneInPartialDate().get(DateTimeFieldType.year())) {
		if (getExtraWorkRequest() == null
			|| (getExtraWorkRequest().getHoursDoneInPartialDate().get(DateTimeFieldType.monthOfYear()) > request
				.getHoursDoneInPartialDate().get(DateTimeFieldType.monthOfYear()))) {
		    if (lastRequest == null
			    || (lastRequest.getHoursDoneInPartialDate().get(DateTimeFieldType.monthOfYear()) < request
				    .getHoursDoneInPartialDate().get(DateTimeFieldType.monthOfYear()))) {
			lastRequest = request;
		    }
		}
	    }
	}

	Double[] result = new Double[] { 0.0, 0.0 };
	if (lastRequest != null) {
	    result[0] = result[0] + lastRequest.getAccumulatedNormalVacationsAmount();
	    result[1] = result[1] + lastRequest.getAccumulatedNightVacationsAmount();
	}
	return result;
    }

    private boolean validateWorkingDaysExtraWorkNightHours() {
	return (getRequestedExtraNightHours() == null) || getAddToVacations();
    }

    private boolean validateNonWorkingDaysExtraWork() {
	return ((getSaturdayHours() != null || getSundayHours() != null || getHolidayHours() != null) && getAddToVacations());
    }

    protected ActionMessage validateBalances(BigDecimal hourValue, BigDecimal monthValue) {
	ClosedMonth closedMonth = ClosedMonth.getClosedMonth(getExtraWorkRequestFactory().getHoursDoneInPartialDate());
	ResourceBundle bundle = ResourceBundle.getBundle("resources.AssiduousnessResources", Language.getLocale());

	AssiduousnessMonthlyResume assiduousnessMonthlyResume = new AssiduousnessMonthlyResume(getEmployee(), closedMonth,
		getExtraWorkRequest());
	Duration nightHoursWorked = assiduousnessMonthlyResume.getNightlyBalance();
	EmployeeExtraWorkAuthorization employeeExtraWorkAuthorization = getEmployeeExtraWorkAuthorization();

	if (getRequestedNightHours() != null) {
	    // só para quem tem horário nocturno:
	    if (!employeeExtraWorkAuthorization.getNightExtraWork()) {
		return new ActionMessage("error.extraWorkRequest.notAuthorized", bundle.getString("label.nightWork"));
	    }
	    if (nightHoursWorked.toPeriod(PeriodType.dayTime()).getHours() < getRequestedNightHours()) {
		setNightHours(nightHoursWorked.toPeriod(PeriodType.dayTime()).getHours());
	    } else {
		setNightHours(getRequestedNightHours());
	    }
	}

	if (getRequestedExtraNightHours() != null && getExtraNightDays() != null) {
	    // para quem não tem horário nocturno:
	    if (!employeeExtraWorkAuthorization.getNightExtraWork()) {
		return new ActionMessage("error.extraWorkRequest.notAuthorized", bundle.getString("label.nightWork"));
	    }
	    if (nightHoursWorked.toPeriod(PeriodType.dayTime()).getHours() < getRequestedExtraNightHours()) {
		setExtraNightHours(nightHoursWorked.toPeriod(PeriodType.dayTime()).getHours());
	    } else {
		setExtraNightHours(getRequestedExtraNightHours());
	    }
	}

	if (getRequestedSaturdayHours() != null) {
	    if (!employeeExtraWorkAuthorization.getWeeklyRestExtraWork()) {
		return new ActionMessage("error.extraWorkRequest.notAuthorized", bundle.getString("label.saturdayWork"));
	    }
	    int notPayedHours = assiduousnessMonthlyResume.getSaturdaysBalance().toPeriod(PeriodType.dayTime()).getHours()
		    - assiduousnessMonthlyResume.getPayedSaturdaysBalance();
	    if (notPayedHours < getRequestedSaturdayHours()) {
		setSaturdayHours(notPayedHours);
	    } else {
		setSaturdayHours(getRequestedSaturdayHours());
	    }
	}

	if (getRequestedSundayHours() != null) {
	    if (!employeeExtraWorkAuthorization.getWeeklyRestExtraWork()) {
		return new ActionMessage("error.extraWorkRequest.notAuthorized", bundle.getString("label.sundayWork"));
	    }
	    int notPayedHours = assiduousnessMonthlyResume.getSundaysBalance().toPeriod(PeriodType.dayTime()).getHours()
		    - assiduousnessMonthlyResume.getPayedSundaysBalance();
	    if (notPayedHours < getRequestedSundayHours()) {
		setSundayHours(notPayedHours);
	    } else {
		setSundayHours(getRequestedSundayHours());
	    }
	}

	if (getRequestedHolidayHours() != null) {
	    if (!employeeExtraWorkAuthorization.getWeeklyRestExtraWork()) {
		return new ActionMessage("error.extraWorkRequest.notAuthorized", bundle.getString("label.holidayWork"));
	    }
	    int notPayedHours = assiduousnessMonthlyResume.getHolidayBalance().toPeriod(PeriodType.dayTime()).getHours()
		    - assiduousnessMonthlyResume.getPayedHolidayBalance();
	    if (notPayedHours < getRequestedHolidayHours()) {
		setHolidayHours(notPayedHours);
	    } else {
		setHolidayHours(getRequestedHolidayHours());
	    }
	}

	if (getRequestedWorkdayHours() != null) {
	    if (!employeeExtraWorkAuthorization.getNormalExtraWork()
		    && !employeeExtraWorkAuthorization.getNormalExtraWorkPlusOneHundredHours()
		    && !employeeExtraWorkAuthorization.getNormalExtraWorkPlusTwoHours()
		    && !employeeExtraWorkAuthorization.getAuxiliarPersonel()
		    && !employeeExtraWorkAuthorization.getExecutiveAuxiliarPersonel()) {
		return new ActionMessage("error.extraWorkRequest.notAuthorized", bundle.getString("label.normalExtraWork"));
	    }

	    int secondLevel = assiduousnessMonthlyResume.getSecondLevelBalance().toPeriod(PeriodType.dayTime()).getHours();
	    if (employeeExtraWorkAuthorization.getNormalExtraWorkPlusTwoHours()
		    || employeeExtraWorkAuthorization.getAuxiliarPersonel()) {
		secondLevel = assiduousnessMonthlyResume.getSecondLevelBalanceWithoutLimits().toPeriod(PeriodType.dayTime())
			.getHours();
	    }

	    int diference = secondLevel - assiduousnessMonthlyResume.getPayedWorkWeekBalance();

	    int notPayedSecondLevel = Math.max(diference, 0);
	    int notPayedFirstLevel = assiduousnessMonthlyResume.getFirstLevelBalance().toPeriod(PeriodType.dayTime()).getHours();
	    if (diference < 0) {
		notPayedFirstLevel = Math.max(notPayedFirstLevel + diference, 0);
	    }

	    if ((notPayedFirstLevel + notPayedSecondLevel) < getRequestedWorkdayHours()) {
		setWorkdayHours((notPayedFirstLevel + notPayedSecondLevel));
		setWorkdayHoursFirstLevel(notPayedFirstLevel);
		setWorkdayHoursSecondLevel(notPayedSecondLevel);
	    } else {
		setWorkdayHours(getRequestedWorkdayHours());
		setWorkdayHoursFirstLevel(Math.min(notPayedFirstLevel, getRequestedWorkdayHours()));
		setWorkdayHoursSecondLevel(Math
			.min(getRequestedWorkdayHours() - getWorkdayHoursFirstLevel(), notPayedSecondLevel));
	    }

	    if (!employeeExtraWorkAuthorization.getNormalExtraWorkPlusOneHundredHours()
		    && !employeeExtraWorkAuthorization.getAuxiliarPersonel()) {
		int hoursPerYear = getHourByYearExtraWork() + getWorkdayHours();
		if (hoursPerYear > YEAR_HOUR_LIMIT) {
		    diference = hoursPerYear - YEAR_HOUR_LIMIT;
		    int newSecondLevel = Math.max(getWorkdayHoursSecondLevel() - diference, 0);
		    diference = diference - getWorkdayHoursSecondLevel();
		    if (diference > 0) {
			setWorkdayHoursFirstLevel(Math.max(getWorkdayHoursFirstLevel() - diference, 0));
		    }
		    setWorkdayHoursSecondLevel(newSecondLevel);
		    setWorkdayHours(getWorkdayHoursFirstLevel() + getWorkdayHoursSecondLevel());
		}

	    }

	    if (!getAddToVacations()) {
		Double workdayHoursFirstLevelAmount = getAmount(hourValue, firstHourPercentage, getWorkdayHoursFirstLevel());
		Double workdayHoursSecondLevelAmount = getAmount(hourValue, secondHourPercentage, getWorkdayHoursSecondLevel());
		double limit = Math.pow(paymentLimitCoefficient, -1);
		if (getEmployeeExtraWorkAuthorization().getExecutiveAuxiliarPersonel()) {
		    limit = executiveAuxiliarPaymentLimitPercentage;
		}
		Double monthLimit = (monthValue.doubleValue() * limit);
		if ((workdayHoursFirstLevelAmount + workdayHoursSecondLevelAmount) > monthLimit) {
		    if (workdayHoursFirstLevelAmount > monthLimit) {
			setWorkdayHoursSecondLevel(0);
			int hourLimit = Math.max((int) ((monthLimit) / (hourValue.doubleValue() * firstHourPercentage)), 0);
			setWorkdayHoursFirstLevel(hourLimit);
		    } else {
			int hourLimit = (int) (((monthLimit) / (hourValue.doubleValue()) - getWorkdayHoursFirstLevel()
				* firstHourPercentage) / secondHourPercentage);
			setWorkdayHoursSecondLevel(hourLimit);
		    }
		    setWorkdayHours(getWorkdayHoursFirstLevel() + getWorkdayHoursSecondLevel());
		}
	    }
	}

	return null;
    }

    private Double getAmount(BigDecimal hourValue, double hourPercentage, Integer hours) {
	return new Double(decimalFormat.format(hourValue.doubleValue() * hourPercentage * (hours != null ? hours : 0)));
    }

    private Integer getHourByYearExtraWork() {
	int result = 0;
	for (ExtraWorkRequest request : getEmployee().getAssiduousness().getExtraWorkRequests()) {
	    if (getExtraWorkRequest() == null || !getExtraWorkRequest().equals(request)) {
		if (request.getHoursDoneInPartialDate().get(DateTimeFieldType.year()) == getExtraWorkRequestFactory()
			.getHoursDoneInPartialDate().get(DateTimeFieldType.year())
			&& request.getApproved() && request.getWorkdayHours() != null) {
		    result = result + request.getWorkdayHours();
		}
	    }
	}
	return result;
    }

    private EmployeeExtraWorkAuthorization getEmployeeExtraWorkAuthorization() {
	if (getExtraWorkRequestFactory().getUnit() != null) {
	    LocalDate begin = new LocalDate(getExtraWorkRequestFactory().getYearMonthHoursDone().getYear(),
		    getExtraWorkRequestFactory().getYearMonthHoursDone().getMonth().ordinal() + 1, 1);
	    LocalDate end = new LocalDate(getExtraWorkRequestFactory().getYearMonthHoursDone().getYear(),
		    getExtraWorkRequestFactory().getYearMonthHoursDone().getMonth().ordinal() + 1, begin.dayOfMonth()
			    .getMaximumValue());
	    for (EmployeeExtraWorkAuthorization employeeExtraWorkAuthorization : getEmployee().getAssiduousness()
		    .getEmployeeExtraWorkAuthorizations()) {
		if (employeeExtraWorkAuthorization.getExtraWorkAuthorization().existsBetweenDates(begin, end)
			&& employeeExtraWorkAuthorization.getExtraWorkAuthorization().getPayingUnit().equals(
				getExtraWorkRequestFactory().getUnit())) {
		    return employeeExtraWorkAuthorization;
		}
	    }
	}
	return null;
    }

    protected boolean isEmptyPaymentRequest() {
	return ((getRequestedNightHours() == null || getRequestedNightHours().equals(0))
		&& (getRequestedNightHours() == null || getRequestedNightHours().equals(0))
		&& (getRequestedExtraNightHours() == null || getRequestedExtraNightHours().equals(0))
		&& (getExtraNightDays() == null || getExtraNightDays().equals(0))
		&& (getRequestedHolidayHours() == null || getRequestedHolidayHours().equals(0))
		&& (getRequestedSaturdayHours() == null || getRequestedSaturdayHours().equals(0))
		&& (getRequestedSundayHours() == null || getRequestedSundayHours().equals(0)) && (getRequestedWorkdayHours() == null || getRequestedWorkdayHours()
		.equals(0))) ? true : false;
    }

    protected boolean validateRemunerationOptionFields() {
	return (getAddToVacations() == true && getAddToWeekRestTime() == true) ? false : true;
    }

    protected boolean validateExtraNightWorkFields() {
	return ((getRequestedExtraNightHours() == null && getExtraNightDays() == null) || getRequestedExtraNightHours() != null
		&& getExtraNightDays() != null) ? true : false;
    }

    public Boolean getCanBeDeleted() {
	ClosedMonth payingMonth = ClosedMonth.getClosedMonth(getExtraWorkRequestFactory().getPartialPayingDate());
	return getExtraWorkRequest() != null && getExtraWorkRequestFactory().getIsMonthClosed() && payingMonth != null
		&& !payingMonth.getClosedForExtraWork();
    }

    public Boolean getApproved() {
	return approved;
    }

    public void setApproved(Boolean approved) {
	this.approved = approved;
    }

    public ExtraWorkRequestFactory getExtraWorkRequestFactory() {
	return extraWorkRequestFactory;
    }

    public void setExtraWorkRequestFactory(ExtraWorkRequestFactory extraWorkRequestFactory) {
	this.extraWorkRequestFactory = extraWorkRequestFactory;
    }

    @Override
    public boolean equals(Object obj) {
	EmployeeExtraWorkRequestFactory employeeExtraWorkRequestFactory = (EmployeeExtraWorkRequestFactory) obj;
	return employeeExtraWorkRequestFactory.getEmployee() == getEmployee();
    }

    public Double getAmount() {
	return amount;
    }

    public void setAmount(Double amount) {
	this.amount = amount;
    }

    public Integer getTotalVacationDays() {
	return totalVacationDays;
    }

    public void setTotalVacationDays(Integer totalVacationDays) {
	this.totalVacationDays = totalVacationDays;
    }

    public Integer getRequestedExtraNightHours() {
	return requestedExtraNightHours;
    }

    public void setRequestedExtraNightHours(Integer requestedExtraNightHours) {
	this.requestedExtraNightHours = requestedExtraNightHours;
    }

    public Integer getRequestedHolidayHours() {
	return requestedHolidayHours;
    }

    public void setRequestedHolidayHours(Integer requestedHolidayHours) {
	this.requestedHolidayHours = requestedHolidayHours;
    }

    public Integer getRequestedNightHours() {
	return requestedNightHours;
    }

    public void setRequestedNightHours(Integer requestedNightHours) {
	this.requestedNightHours = requestedNightHours;
    }

    public Integer getRequestedSaturdayHours() {
	return requestedSaturdayHours;
    }

    public void setRequestedSaturdayHours(Integer requestedSaturdayHours) {
	this.requestedSaturdayHours = requestedSaturdayHours;
    }

    public Integer getRequestedSundayHours() {
	return requestedSundayHours;
    }

    public void setRequestedSundayHours(Integer requestedSundayHours) {
	this.requestedSundayHours = requestedSundayHours;
    }

    public Integer getRequestedWorkdayHours() {
	return requestedWorkdayHours;
    }

    public void setRequestedWorkdayHours(Integer requestedWorkdayHours) {
	this.requestedWorkdayHours = requestedWorkdayHours;
    }

    public Integer getWorkdayHoursFirstLevel() {
	return workdayHoursFirstLevel;
    }

    public void setWorkdayHoursFirstLevel(Integer workdayHoursFirstLevel) {
	this.workdayHoursFirstLevel = workdayHoursFirstLevel;
    }

    public Integer getWorkdayHoursSecondLevel() {
	return workdayHoursSecondLevel;
    }

    public void setWorkdayHoursSecondLevel(Integer workdayHoursSecondLevel) {
	this.workdayHoursSecondLevel = workdayHoursSecondLevel;
    }

}