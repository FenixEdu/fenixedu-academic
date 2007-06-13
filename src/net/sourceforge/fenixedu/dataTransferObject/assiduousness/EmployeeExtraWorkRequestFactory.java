package net.sourceforge.fenixedu.dataTransferObject.assiduousness;

import java.io.Serializable;
import java.util.HashMap;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.assiduousness.Assiduousness;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.ClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.EmployeeExtraWorkAuthorization;
import net.sourceforge.fenixedu.domain.assiduousness.ExtraWorkRequest;
import net.sourceforge.fenixedu.domain.assiduousness.Schedule;
import net.sourceforge.fenixedu.domain.assiduousness.WorkSchedule;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.GiafInterface;
import net.sourceforge.fenixedu.util.LanguageUtils;

import org.apache.struts.action.ActionMessage;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Duration;
import org.joda.time.PeriodType;
import org.joda.time.YearMonthDay;

public class EmployeeExtraWorkRequestFactory implements Serializable, FactoryExecutor {
    private Integer nightHours;

    private Integer extraNightHours;

    private Integer extraNightDays;

    private Integer holidayHours;

    private Integer saturdayHours;

    private Integer sundayHours;

    private Integer workdayHours;

    private Boolean addToVacations;

    private Boolean addToWeekRestTime;

    private Boolean approved;

    private Double amount = 0.0;

    private DomainReference<Employee> modifiedBy;

    private DomainReference<Employee> employee;

    private DomainReference<ExtraWorkRequest> extraWorkRequest;

    private ExtraWorkRequestFactory extraWorkRequestFactory;

    private Integer totalVacationDays;

    public static final int YEAR_HOUR_LIMIT = 100;

    public static final double nightExtraWorkPercentage = 0.25;

    public static final double firstHourPercentage = 1.25;

    public static final double secondHourPercentage = 1.50;

    public static final double weeklyRestPercentage = 2;

    public static final double extraWorkDayCoefficient = 1.25;

    public static final double nightExtraWorkCoefficient = 1.50;

    public EmployeeExtraWorkRequestFactory(Employee employee,
	    ExtraWorkRequestFactory extraWorkRequestFactory) {
	super();
	setExtraWorkRequestFactory(extraWorkRequestFactory);
	setEmployee(employee);
    }

    public EmployeeExtraWorkRequestFactory(ExtraWorkRequest extraWorkRequest,
	    ExtraWorkRequestFactory extraWorkRequestFactory) {
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
	setAddToVacations(extraWorkRequest.getAddToVacations());
	setAddToWeekRestTime(extraWorkRequest.getAddToWeekRestTime());
	setApproved(extraWorkRequest.getApproved());
	setAmount(extraWorkRequest.getAmount());
	setTotalVacationDays(extraWorkRequest.getNormalVacationsDays()
		+ extraWorkRequest.getNightVacationsDays());
    }

    public Integer getIdInternal() {
	if (getExtraWorkRequest() != null) {
	    return getExtraWorkRequest().getIdInternal();
	}
	return null;
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
	    if (!validateWorkingDaysExtraWork()) {
		return new ActionMessage(
			"error.extraWorkRequest.invalidWorkingDaysExtraWorkRemuneration");
	    }

	    ActionMessage actionMessage = validateBalances();
	    if (actionMessage != null) {
		return actionMessage;
	    }

	    Double totalValue = 0.0;
	    try {
		totalValue = getThisRequestValue();
	    } catch (ExcepcaoPersistencia e) {
		return new ActionMessage("error.connectionError");
	    }

	    Integer thisNormalVacationsDays = 0;
	    Double thisNormalVacationsAmount = 0.0;
	    Double accumulatedNormalVacationsAmount = 0.0;

	    Integer thisNightVacationsDays = 0;
	    Double thisNightVacationsAmount = 0.0;
	    Double accumulatedNightVacationsAmount = 0.0;
	    if (validateWorkingDaysExtraWork() && (getWorkdayHours() != null || getNightHours() != null)) {

		YearMonthDay day = new YearMonthDay(getExtraWorkRequest().getHoursDoneInPartialDate()
			.get(DateTimeFieldType.year()), getExtraWorkRequest()
			.getHoursDoneInPartialDate().get(DateTimeFieldType.monthOfYear()), 1);
		Schedule schedule = getEmployee().getAssiduousness().getSchedule(day);
		int dayHours = 7;
		if (schedule != null) {
		    dayHours = schedule.getEqualWorkPeriodDuration().toPeriod(PeriodType.dayTime())
			    .getHours();
		}

		Double[] accumulated = getLastVacationsAccumulated();
		thisNormalVacationsAmount = getWorkdayHours() == null ? 0 : getWorkdayHours()
			* extraWorkDayCoefficient;
		double total = thisNormalVacationsAmount + accumulated[0];
		thisNormalVacationsDays = ((int) total) / dayHours;
		accumulatedNormalVacationsAmount = (total - (thisNormalVacationsDays * dayHours));
		if (getNightHours() != null && !hasNightSchedule()) {
		    thisNightVacationsAmount = getNightHours() == null ? 0 : getNightHours()
			    * nightExtraWorkCoefficient;
		    total = thisNightVacationsAmount + accumulated[1];
		    thisNightVacationsDays = ((int) total) / dayHours;
		    accumulatedNightVacationsAmount = (total - (thisNightVacationsDays * dayHours));
		}

	    }
	    setTotalVacationDays(thisNormalVacationsDays + thisNightVacationsDays);
	    if (getExtraWorkRequest() == null) {
		new ExtraWorkRequest(getExtraWorkRequestFactory().getPartialPayingDate(),
			getExtraWorkRequestFactory().getUnit(), getEmployee(), getNightHours(),
			getExtraNightHours(), getExtraNightDays(), getHolidayHours(),
			getSaturdayHours(), getSundayHours(), getWorkdayHours(), getAddToVacations(),
			getAddToWeekRestTime(), getModifiedBy(), totalValue, false,
			getExtraWorkRequestFactory().getHoursDoneInPartialDate(),
			thisNormalVacationsDays, thisNormalVacationsAmount,
			accumulatedNormalVacationsAmount, thisNightVacationsDays,
			thisNightVacationsAmount, accumulatedNightVacationsAmount);
	    } else {
		getExtraWorkRequest().edit(getNightHours(), getExtraNightHours(), getExtraNightDays(),
			getHolidayHours(), getSaturdayHours(), getSundayHours(), getWorkdayHours(),
			getAddToVacations(), getAddToWeekRestTime(), getModifiedBy(),
			getExtraWorkRequestFactory().getHoursDoneInPartialDate(), totalValue,
			thisNormalVacationsDays, thisNormalVacationsAmount,
			accumulatedNormalVacationsAmount, thisNightVacationsDays,
			thisNightVacationsAmount, accumulatedNightVacationsAmount);
		if (getExtraWorkRequestFactory().checkUnitMoney() != null) {
		    getExtraWorkRequestFactory().setPerformPayment(false);
		    getExtraWorkRequestFactory().execute();
		}
	    }

	}
	return null;
    }

    private boolean hasNightSchedule() {
	HashMap<YearMonthDay, WorkSchedule> workSchedules = getEmployee().getAssiduousness()
		.getWorkSchedulesBetweenDates(getExtraWorkRequestFactory().getPartialPayingDate());
	for (WorkSchedule workSchedule : workSchedules.values()) {
	    if (workSchedule != null && workSchedule.getWorkScheduleType().isNocturnal()) {
		return true;
	    }
	}
	return false;
    }

    private Double[] getLastVacationsAccumulated() {
	Double[] result = new Double[] { 0.0, 0.0 };
	for (ExtraWorkRequest request : getEmployee().getAssiduousness().getExtraWorkRequests()) {
	    if (request.getHoursDoneInPartialDate().get(DateTimeFieldType.year()) == getExtraWorkRequestFactory()
		    .getHoursDoneInPartialDate().get(DateTimeFieldType.year())) {
		if (getExtraWorkRequest() == null
			|| (getExtraWorkRequest().getHoursDoneInPartialDate().get(
				DateTimeFieldType.monthOfYear()) != request.getHoursDoneInPartialDate()
				.get(DateTimeFieldType.monthOfYear()) && request.getApproved()))
		    result[0] = result[0] + request.getAccumulatedNormalVacationsAmount();
		result[1] = result[1] + request.getAccumulatedNightVacationsAmount();
	    }
	}
	return result;
    }

    private boolean validateWorkingDaysExtraWork() {
	return ((getWorkdayHours() == null && getNightHours() == null) || getAddToVacations());
    }

    private Double getThisRequestValue() throws ExcepcaoPersistencia {
	GiafInterface giafInterface = new GiafInterface();
	YearMonthDay day = new YearMonthDay(getExtraWorkRequestFactory().getHoursDoneInPartialDate()
		.get(DateTimeFieldType.year()), getExtraWorkRequestFactory().getHoursDoneInPartialDate()
		.get(DateTimeFieldType.monthOfYear()), 1);
	Double hourValue = new Double(0);
	hourValue = giafInterface.getEmployeeHourValue(getEmployee(), day);
	Double totalValue = new Double(0);
	totalValue += (hourValue * Assiduousness.weeklyRestPercentage * (getSaturdayHours() != null ? getSaturdayHours()
		: 0))
		+ (hourValue * Assiduousness.weeklyRestPercentage * (getSundayHours() != null ? getSundayHours()
			: 0))
		+ (hourValue * Assiduousness.weeklyRestPercentage * (getSaturdayHours() != null ? getSaturdayHours()
			: 0));
	return totalValue;
    }

    protected ActionMessage validateBalances() {
	ClosedMonth closedMonth = ClosedMonth.getClosedMonth(getExtraWorkRequestFactory()
		.getHoursDoneInPartialDate());
	ResourceBundle bundle = ResourceBundle.getBundle("resources.AssiduousnessResources",
		LanguageUtils.getLocale());

	AssiduousnessClosedMonth assiduousnessClosedMonth = getEmployee().getAssiduousness()
		.getAssiduousnessClosedMonth(closedMonth);

	Duration nightHoursWorked = assiduousnessClosedMonth.getTotalNightBalance();

	EmployeeExtraWorkAuthorization employeeExtraWorkAuthorization = getEmployeeExtraWorkAuthorization();

	if (getNightHours() != null) {// só para quem tem horário nocturno:
	    if (!employeeExtraWorkAuthorization.getNightExtraWork()) {
		return new ActionMessage("error.extraWorkRequest.notAuthorized", bundle
			.getString("label.nightWork"));
	    }
	    if (nightHoursWorked.toPeriod(PeriodType.dayTime()).getHours() < getNightHours()) {
		return new ActionMessage("error.extraWorkRequest.nonWorkedHours", bundle
			.getString("label.nightWork"));
	    }
	}

	if (getExtraNightHours() != null && getExtraNightDays() != null) {// para quem não tem horário nocturno:
	    if (!employeeExtraWorkAuthorization.getNightExtraWork()) {
		return new ActionMessage("error.extraWorkRequest.notAuthorized", bundle
			.getString("label.nightWork"));
	    }
	    if (nightHoursWorked.toPeriod(PeriodType.dayTime()).getHours() < getExtraNightHours()) {
		return new ActionMessage("error.extraWorkRequest.nonWorkedHours", bundle
			.getString("label.nightWork"));
	    }
	}

	if (getSaturdayHours() != null) {
	    if (!employeeExtraWorkAuthorization.getWeeklyRestExtraWork()) {
		return new ActionMessage("error.extraWorkRequest.notAuthorized", bundle
			.getString("label.saturdayWork"));
	    }
	    if (assiduousnessClosedMonth.getSaturdayBalance().toPeriod(PeriodType.dayTime()).getHours() < getSaturdayHours()) {
		return new ActionMessage("error.extraWorkRequest.nonWorkedHours", bundle
			.getString("label.saturdayWork"));
	    }
	}

	if (getSundayHours() != null) {
	    if (!employeeExtraWorkAuthorization.getWeeklyRestExtraWork()) {
		return new ActionMessage("error.extraWorkRequest.notAuthorized", bundle
			.getString("label.sundayWork"));
	    }
	    if (assiduousnessClosedMonth.getSundayBalance().toPeriod(PeriodType.dayTime()).getHours() < getSundayHours()) {
		return new ActionMessage("error.extraWorkRequest.nonWorkedHours", bundle
			.getString("label.sundayWork"));
	    }
	}

	if (getHolidayHours() != null) {
	    if (!employeeExtraWorkAuthorization.getWeeklyRestExtraWork()) {
		return new ActionMessage("error.extraWorkRequest.notAuthorized", bundle
			.getString("label.holidayWork"));
	    }
	    if (assiduousnessClosedMonth.getHolidayBalance().toPeriod(PeriodType.dayTime()).getHours() < getHolidayHours()) {
		return new ActionMessage("error.extraWorkRequest.nonWorkedHours", bundle
			.getString("label.holidayWork"));
	    }
	}

	if (getWorkdayHours() != null) {
	    if (!employeeExtraWorkAuthorization.getNormalExtraWork()
		    && !employeeExtraWorkAuthorization.getNormalExtraWorkPlusOneHundredHours()
		    && !employeeExtraWorkAuthorization.getNormalExtraWorkPlusTwoHours()
		    && !employeeExtraWorkAuthorization.getAuxiliarPersonel()
		    && !employeeExtraWorkAuthorization.getExecutiveAuxiliarPersonel()) {
		return new ActionMessage("error.extraWorkRequest.notAuthorized", bundle
			.getString("label.normalExtraWork"));
	    }
	    if (employeeExtraWorkAuthorization.getNormalExtraWork()) {
		//		if (getWorkdayHours() > DAY_HOUR_LIMIT) {
		//		    return new ActionMessage("error.extraWorkRequest.extraWorkLimitReached",
		//			    DAY_HOUR_LIMIT, bundle.getString("label.hoursPerDay"));
		//		}
		int hoursPerYear = getHourByYearExtraWork() + getWorkdayHours();
		if (hoursPerYear > YEAR_HOUR_LIMIT) {
		    return new ActionMessage("error.extraWorkRequest.extraWorkLimitReached",
			    YEAR_HOUR_LIMIT, bundle.getString("label.hoursPerYear"));
		}
	    }

	    if (assiduousnessClosedMonth.getBalance().toPeriod(PeriodType.dayTime()).getHours() < getWorkdayHours()) {
		return new ActionMessage("error.extraWorkRequest.nonWorkedHours", bundle
			.getString("label.normalExtraWork"));
	    }
	}

	return null;
    }

    private Integer getHourByYearExtraWork() {
	int result = 0;
	for (ExtraWorkRequest request : getEmployee().getAssiduousness().getExtraWorkRequests()) {
	    if (request.getHoursDoneInPartialDate().get(DateTimeFieldType.year()) == getExtraWorkRequestFactory()
		    .getHoursDoneInPartialDate().get(DateTimeFieldType.year())) {
		if (getExtraWorkRequest() == null
			|| (getExtraWorkRequest().getHoursDoneInPartialDate().get(
				DateTimeFieldType.monthOfYear()) != request.getHoursDoneInPartialDate()
				.get(DateTimeFieldType.monthOfYear()) && request.getApproved()))
		    result = result + request.getTotalHours();
	    }
	}
	return result;
    }

    private EmployeeExtraWorkAuthorization getEmployeeExtraWorkAuthorization() {
	if (getExtraWorkRequestFactory().getUnit() != null) {
	    YearMonthDay begin = new YearMonthDay(getExtraWorkRequestFactory().getYearMonthHoursDone()
		    .getYear(), getExtraWorkRequestFactory().getYearMonthHoursDone().getMonth()
		    .ordinal() + 1, 1);
	    YearMonthDay end = new YearMonthDay(getExtraWorkRequestFactory().getYearMonthHoursDone()
		    .getYear(), getExtraWorkRequestFactory().getYearMonthHoursDone().getMonth()
		    .ordinal() + 1, begin.dayOfMonth().getMaximumValue());
	    for (EmployeeExtraWorkAuthorization employeeExtraWorkAuthorization : getEmployee()
		    .getAssiduousness().getEmployeeExtraWorkAuthorizations()) {
		if (employeeExtraWorkAuthorization.getExtraWorkAuthorization().existsBetweenDates(begin,
			end)
			&& employeeExtraWorkAuthorization.getExtraWorkAuthorization().getPayingUnit()
				.equals(getExtraWorkRequestFactory().getUnit())) {
		    return employeeExtraWorkAuthorization;
		}
	    }
	}
	return null;
    }

    protected boolean isEmptyPaymentRequest() {
	return ((getNightHours() == null || getNightHours().equals(0))
		&& (getNightHours() == null || getNightHours().equals(0))
		&& (getExtraNightHours() == null || getExtraNightHours().equals(0))
		&& (getExtraNightDays() == null || getExtraNightDays().equals(0))
		&& (getHolidayHours() == null || getHolidayHours().equals(0))
		&& (getSaturdayHours() == null || getSaturdayHours().equals(0))
		&& (getSundayHours() == null || getSundayHours().equals(0)) && (getWorkdayHours() == null || getWorkdayHours()
		.equals(0))) ? true : false;
    }

    protected boolean validateRemunerationOptionFields() {
	return (getAddToVacations() == true && getAddToWeekRestTime() == true) ? false : true;
    }

    protected boolean validateExtraNightWorkFields() {
	return ((getExtraNightHours() == null && getExtraNightDays() == null) || getExtraNightHours() != null
		&& getExtraNightDays() != null) ? true : false;
    }

    private ExtraWorkRequest getEmployeeExtraWorkRequest() {
	for (ExtraWorkRequest extraWorkRequest : getExtraWorkRequestFactory().getUnit()
		.getExtraWorkRequests()) {
	    if (extraWorkRequest.getPartialPayingDate().equals(
		    getExtraWorkRequestFactory().getPartialPayingDate())
		    && extraWorkRequest.getAssiduousness().equals(getEmployee().getAssiduousness())) {
		return extraWorkRequest;
	    }
	}
	return null;
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

}