package net.sourceforge.fenixedu.dataTransferObject.assiduousness;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.assiduousness.Assiduousness;
import net.sourceforge.fenixedu.domain.assiduousness.ClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.EmployeeExtraWorkAuthorization;
import net.sourceforge.fenixedu.domain.assiduousness.ExtraWorkAuthorization;
import net.sourceforge.fenixedu.domain.assiduousness.ExtraWorkRequest;
import net.sourceforge.fenixedu.domain.assiduousness.UnitExtraWorkAmount;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import net.sourceforge.fenixedu.util.Month;

import org.apache.struts.action.ActionMessage;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDate;
import org.joda.time.Partial;

public class ExtraWorkRequestFactory implements Serializable, FactoryExecutor {
    private Integer year;

    private Month month;

    private YearMonth yearMonthHoursDone;

    private Integer unitCode;

    private boolean performPayment;

    private DomainReference<Unit> unit;

    private List<EmployeeExtraWorkRequestFactory> employeesExtraWorkRequests;

    public ExtraWorkRequestFactory() {
	super();
	LocalDate date = new LocalDate();
	setYear(date.getYear());
	setMonth(Month.values()[date.minusMonths(1).getMonthOfYear() - 1]);
	setYearMonthHoursDone(new YearMonth(date.getYear(), Month.values()[date.minusMonths(1).getMonthOfYear() - 1]));
    }

    public ExtraWorkRequestFactory(LocalDate date) {
	super();
	setYear(date.getYear());
	setMonth(Month.values()[date.getMonthOfYear() - 1]);
	setYearMonthHoursDone(new YearMonth(date.getYear(), Month.values()[date.getMonthOfYear() - 1]));
    }

    public ExtraWorkRequestFactory(Integer year, String month, Integer unitCode, Integer doneInYear, String doneInMonth) {
	super();
	setYear(year);
	setMonth(Month.valueOf(month));
	setYearMonthHoursDone(new YearMonth(doneInYear, Month.valueOf(doneInMonth)));
	setUnitCode(unitCode);
    }

    public Month getMonth() {
	return month;
    }

    public void setMonth(Month month) {
	this.month = month;
    }

    public Integer getYear() {
	return year;
    }

    public void setYear(Integer year) {
	this.year = year;
    }

    public Integer getUnitCode() {
	return unitCode;
    }

    public void setUnitCode(Integer unitCode) {
	this.unitCode = unitCode;
	setUnit(Unit.readByCostCenterCode(unitCode));
    }

    public Unit getUnit() {
	return unit == null ? null : unit.getObject();
    }

    public void setUnit(Unit unit) {
	if (unit != null) {
	    this.unit = new DomainReference<Unit>(unit);
	    setEmployeesExtraWorkRequests(getUnit().getExtraWorkRequests(getYear(), getMonth(),
		    getYearMonthHoursDone().getYear(), getYearMonthHoursDone().getMonth()));
	    addEmployeeExtraWorkRequest();
	}
    }

    public Object execute() {
	ActionMessage actionMessage = null;
	if (isPerformPayment()) {
	    actionMessage = checkUnitMoney();
	    if (actionMessage == null) {
		getUnitExtraWorkAmountOrNew().sumSpent(getNotConfirmedAmount());
		for (EmployeeExtraWorkRequestFactory employeeExtraWorkRequestFactory : getEmployeesExtraWorkRequests()) {
		    if (employeeExtraWorkRequestFactory.getExtraWorkRequest() != null) {
			employeeExtraWorkRequestFactory.getExtraWorkRequest().setApproved(true);
		    }
		}
	    }
	} else {
	    getUnitExtraWorkAmountOrNew().subtractSpent(getConfirmedAmount());
	    for (EmployeeExtraWorkRequestFactory employeeExtraWorkRequestFactory : getEmployeesExtraWorkRequests()) {
		if (employeeExtraWorkRequestFactory.getExtraWorkRequest() != null) {
		    employeeExtraWorkRequestFactory.getExtraWorkRequest().setApproved(false);
		}
	    }
	}
	return actionMessage;
    }

    private UnitExtraWorkAmount getUnitExtraWorkAmountOrNew() {
	YearMonth yearMonthToPay = new YearMonth(getYear(), getMonth());
	yearMonthToPay.addMonth();
	UnitExtraWorkAmount result = getUnit().getUnitExtraWorkAmountByYear(yearMonthToPay.getYear());
	if (result == null) {
	    result = new UnitExtraWorkAmount(yearMonthToPay.getYear(), getUnit());
	}
	return result;
    }

    public void reloadEmployeeExtraWorkRequest() {
	setEmployeesExtraWorkRequests(getUnit().getExtraWorkRequests(getYear(), getMonth(), getYearMonthHoursDone().getYear(),
		getYearMonthHoursDone().getMonth()));
	addEmployeeExtraWorkRequest();
    }

    private void addEmployeeExtraWorkRequest() {
	if (getUnit() != null) {
	    LocalDate begin = new LocalDate(getYearMonthHoursDone().getYear(), getYearMonthHoursDone().getMonth().ordinal() + 1,
		    1);
	    LocalDate end = new LocalDate(getYearMonthHoursDone().getYear(), getYearMonthHoursDone().getMonth().ordinal() + 1,
		    begin.dayOfMonth().getMaximumValue());
	    Map<Assiduousness, ExtraWorkRequest> assiduousnessExtraWorkRequests = new HashMap<Assiduousness, ExtraWorkRequest>();
	    for (ExtraWorkRequest extraWorkRequest : getUnit().getExtraWorkRequestsByDoneAndPayingDates(
		    getYearMonthHoursDone().getYear(), getYearMonthHoursDone().getMonth(), getYear(), getMonth())) {
		assiduousnessExtraWorkRequests.put(extraWorkRequest.getAssiduousness(), extraWorkRequest);
	    }
	    for (ExtraWorkAuthorization extraWorkAuthorization : getUnit().getExtraPayingUnitAuthorizations()) {
		if (extraWorkAuthorization.existsBetweenDates(begin, end)) {
		    for (EmployeeExtraWorkAuthorization employeeExtraWorkAuthorization : extraWorkAuthorization
			    .getEmployeeExtraWorkAuthorizations()) {
			EmployeeExtraWorkRequestFactory employeeExtraWorkRequestFactory = new EmployeeExtraWorkRequestFactory(
				employeeExtraWorkAuthorization.getAssiduousness().getEmployee(), this);
			if (!getEmployeesExtraWorkRequests().contains(employeeExtraWorkRequestFactory)) {
			    ExtraWorkRequest extraWorkRequest = assiduousnessExtraWorkRequests.get(employeeExtraWorkAuthorization
				    .getAssiduousness());
			    if (extraWorkRequest != null) {
				continue;
			    }
			    getEmployeesExtraWorkRequests().add(employeeExtraWorkRequestFactory);
			}
		    }
		}
	    }
	}
    }

    public Partial getPartialPayingDate() {
	DateTimeFieldType[] types = new DateTimeFieldType[] { DateTimeFieldType.year(), DateTimeFieldType.monthOfYear() };
	int[] values = new int[] { getYear(), getMonth().ordinal() + 1 };
	return new Partial(types, values);
    }

    public Partial getHoursDoneInPartialDate() {
	DateTimeFieldType[] types = new DateTimeFieldType[] { DateTimeFieldType.year(), DateTimeFieldType.monthOfYear() };
	int[] values = new int[] { getYearMonthHoursDone().getYear(), getYearMonthHoursDone().getMonth().ordinal() + 1 };
	return new Partial(types, values);
    }

    public void setPartialPayingDate(Partial partial) {
	setYear(partial.get(DateTimeFieldType.year()));
	setMonth(Month.values()[partial.get(DateTimeFieldType.monthOfYear()) - 1]);
    }

    public Boolean getIsMonthClosed() {
	return ClosedMonth.isMonthClosed(getHoursDoneInPartialDate());
    }

    public Boolean getIsMonthClosedForExtraWork() {
	return ClosedMonth.isMonthClosedForExtraWork(getHoursDoneInPartialDate());
    }

    public Boolean getIsPayingMonthClosedForExtraWork() {
	return ClosedMonth.isMonthClosedForExtraWork(getPartialPayingDate());
    }

    public YearMonth getYearMonthHoursDone() {
	return yearMonthHoursDone;
    }

    public void setYearMonthHoursDone(YearMonth yearMonthHoursDone) {
	this.yearMonthHoursDone = yearMonthHoursDone;
    }

    public ActionMessage checkUnitMoney() {
	Double unitBalance = getFinalUnitBalance();
	if (unitBalance < 0) {
	    return new ActionMessage("error.extraWorkRequest.notEnoughMoney");
	}
	return null;
    }

    public Double getFinalUnitBalance() {
	YearMonth yearMonth = new YearMonth(getPartialPayingDate());
	yearMonth.addMonth();
	double balance = getUnit().getUnitExtraWorkAmountByYear(yearMonth.getYear()) == null ? 0 : getUnit()
		.getUnitExtraWorkAmountByYear(yearMonth.getYear()).getBalance();
	return balance - getNotConfirmedAmount();
    }

    public Double getInitialUnitBalance() {
	YearMonth yearMonth = new YearMonth(getPartialPayingDate());
	yearMonth.addMonth();
	UnitExtraWorkAmount unitExtraWorkAmount = getUnit().getUnitExtraWorkAmountByYear(yearMonth.getYear());
	double balance = unitExtraWorkAmount == null ? 0 : unitExtraWorkAmount.getBalance();
	return balance + getConfirmedAmount();
    }

    public boolean isPaymentConfirmed() {
	for (EmployeeExtraWorkRequestFactory employeeExtraWorkRequestFactory : getEmployeesExtraWorkRequests()) {
	    if (employeeExtraWorkRequestFactory.getExtraWorkRequest() != null
		    && (!employeeExtraWorkRequestFactory.getExtraWorkRequest().getApproved())) {
		return false;
	    }
	}
	return true;
    }

    public Double getMonthAmount() {
	Double monthAmount = 0.0;
	for (ExtraWorkRequest extraWorkRequest : getUnit().getExtraWorkRequests(getYear(), getMonth(),
		getYearMonthHoursDone().getYear(), getYearMonthHoursDone().getMonth())) {
	    monthAmount += extraWorkRequest.getAmount();
	}
	return monthAmount;
    }

    public Double getConfirmedAmount() {
	Double monthAmount = 0.0;
	for (ExtraWorkRequest extraWorkRequest : getUnit().getExtraWorkRequests(getYear(), getMonth(),
		getYearMonthHoursDone().getYear(), getYearMonthHoursDone().getMonth())) {
	    if (extraWorkRequest.getApproved()) {
		monthAmount += extraWorkRequest.getAmount();
	    }
	}
	return monthAmount;
    }

    public Double getNotConfirmedAmount() {
	Double monthAmount = 0.0;
	for (ExtraWorkRequest extraWorkRequest : getUnit().getExtraWorkRequests(getYear(), getMonth(),
		getYearMonthHoursDone().getYear(), getYearMonthHoursDone().getMonth())) {
	    if (!extraWorkRequest.getApproved()) {
		monthAmount += extraWorkRequest.getAmount();
	    }
	}
	return monthAmount;
    }

    public List<EmployeeExtraWorkRequestFactory> getEmployeesExtraWorkRequests() {
	return employeesExtraWorkRequests;
    }

    public void setEmployeesExtraWorkRequests(List<ExtraWorkRequest> extraWorkRequests) {
	this.employeesExtraWorkRequests = new ArrayList<EmployeeExtraWorkRequestFactory>();
	for (ExtraWorkRequest extraWorkRequest : extraWorkRequests) {
	    this.employeesExtraWorkRequests.add(new EmployeeExtraWorkRequestFactory(extraWorkRequest, this));
	}
    }

    public boolean isPerformPayment() {
	return performPayment;
    }

    public void setPerformPayment(boolean performPayment) {
	this.performPayment = performPayment;
    }
}