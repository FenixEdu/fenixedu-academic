package net.sourceforge.fenixedu.domain.assiduousness;

import java.math.BigDecimal;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.dataTransferObject.assiduousness.YearMonth;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.Month;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDate;
import org.joda.time.Partial;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public class ExtraWorkRequest extends ExtraWorkRequest_Base {

    public ExtraWorkRequest(Partial partialPayingDate, Unit unit, Employee employee, Integer nightHours, Integer extraNightHours,
	    Integer extraNightDays, Integer extraNightFirstLevelHours, Integer extraNightSecondLevelHours, Integer holidayHours,
	    Integer saturdayHours, Integer sundayHours, Integer workdayHours, Integer workdayFirstLevelHours,
	    Integer workdaySecondLevelHours, Boolean addToVacations, Boolean addToWeekRestTime, Employee modifiedBy,
	    boolean approved, Partial hoursDoneIn, Integer normalVacationsDays, Double normalVacationsAmount,
	    Double accumulatedNormalVacationsAmount, Integer nightVacationsDays, Double nightVacationsAmount,
	    Double accumulatedNightVacationsAmount, Integer requestedNightHours, Integer requestedExtraNightHours,
	    Integer requestedExtraNightDays, Integer requestedHolidayHours, Integer requestedSaturdayHours,
	    Integer requestedSundayHours, Integer requestedWorkdayHours, Double holidayAmount, Double saturdayAmount,
	    Double sundayAmount, Double workdayFirstLevelAmount, Double workdaySecondLevelAmount,
	    Double extraNightFirstLevelAmount, Double extraNightSecondLevelAmount, Double nightExtraWorkMealAmount) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setPartialPayingDate(partialPayingDate);
	setUnit(unit);
	setAssiduousness(employee.getAssiduousness());
	setNightHours(nightHours);
	setExtraNightHours(extraNightHours);
	setExtraNightHoursFirstLevel(extraNightFirstLevelHours);
	setExtraNightHoursSecondLevel(extraNightSecondLevelHours);
	setExtraNightDays(extraNightDays);
	setHolidayHours(holidayHours);
	setSaturdayHours(saturdayHours);
	setSundayHours(sundayHours);
	setWorkdayHours(workdayHours);
	setWorkdayFirstLevelHours(workdayFirstLevelHours);
	setWorkdaySecondLevelHours(workdaySecondLevelHours);
	setAddToVacations(addToVacations);
	setAddToWeekRestTime(addToWeekRestTime);
	setLastModifiedDate(new DateTime());
	setModifiedBy(modifiedBy);
	setApproved(approved);
	setHoursDoneInPartialDate(hoursDoneIn);
	setNormalVacationsDays(normalVacationsDays);
	setNormalVacationsAmount(normalVacationsAmount);
	setAccumulatedNormalVacationsAmount(accumulatedNormalVacationsAmount);
	setNightVacationsDays(nightVacationsDays);
	setNightVacationsAmount(nightVacationsAmount);
	setAccumulatedNightVacationsAmount(accumulatedNightVacationsAmount);
	setRequestedNightHours(requestedNightHours);
	setRequestedExtraNightHours(requestedExtraNightHours);
	setRequestedExtraNightDays(requestedExtraNightDays);
	setRequestedHolidayHours(requestedHolidayHours);
	setRequestedSaturdayHours(requestedSaturdayHours);
	setRequestedSundayHours(requestedSundayHours);
	setRequestedWorkdayHours(requestedWorkdayHours);
	setHolidayAmount(holidayAmount);
	setSaturdayAmount(saturdayAmount);
	setSundayAmount(sundayAmount);
	setWorkdayFirstLevelAmount(workdayFirstLevelAmount);
	setWorkdaySecondLevelAmount(workdaySecondLevelAmount);
	setExtraNightFirstLevelAmount(extraNightFirstLevelAmount);
	setExtraNightSecondLevelAmount(extraNightSecondLevelAmount);
	setExtraNightMealAmount(nightExtraWorkMealAmount);
	setAmount(holidayAmount + saturdayAmount + sundayAmount + workdayFirstLevelAmount + workdaySecondLevelAmount
		+ extraNightFirstLevelAmount + extraNightSecondLevelAmount + nightExtraWorkMealAmount);

    }

    public void edit(Integer nightHours, Integer extraNightHours, Integer extraNightDays, Integer extraNightFirstLevelHours,
	    Integer extraNightSecondLevelHours, Integer holidayHours, Integer saturdayHours, Integer sundayHours,
	    Integer workdayHours, Integer workdayFirstLevelHours, Integer workdaySecondLevelHours, Boolean addToVacations,
	    Boolean addToWeekRestTime, Employee modifiedBy, Partial hoursDoneInPartialDate, Integer normalVacationsDays,
	    Double normalVacationsAmount, Double accumulatedNormalVacationsAmount, Integer nightVacationsDays,
	    Double nightVacationsAmount, Double accumulatedNightVacationsAmount, Integer requestedNightHours,
	    Integer requestedExtraNightHours, Integer requestedExtraNightDays, Integer requestedHolidayHours,
	    Integer requestedSaturdayHours, Integer requestedSundayHours, Integer requestedWorkdayHours, Double holidayAmount,
	    Double saturdayAmount, Double sundayAmount, Double workdayFirstLevelAmount, Double workdaySecondLevelAmount,
	    Double extraNightFirstLevelAmount, Double extraNightSecondLevelAmount, Double nightExtraWorkMealAmount) {
	setNightHours(nightHours);
	setExtraNightHours(extraNightHours);
	setExtraNightDays(extraNightDays);
	setExtraNightHoursFirstLevel(extraNightFirstLevelHours);
	setExtraNightHoursSecondLevel(extraNightSecondLevelHours);
	setHolidayHours(holidayHours);
	setSaturdayHours(saturdayHours);
	setSundayHours(sundayHours);
	setWorkdayHours(workdayHours);
	setWorkdayFirstLevelHours(workdayFirstLevelHours);
	setWorkdaySecondLevelHours(workdaySecondLevelHours);
	setAddToVacations(addToVacations);
	setAddToWeekRestTime(addToWeekRestTime);
	setLastModifiedDate(new DateTime());
	setModifiedBy(modifiedBy);
	setHoursDoneInPartialDate(hoursDoneInPartialDate);
	setNormalVacationsDays(normalVacationsDays);
	setNormalVacationsAmount(normalVacationsAmount);
	setAccumulatedNormalVacationsAmount(accumulatedNormalVacationsAmount);
	setNightVacationsDays(nightVacationsDays);
	setNightVacationsAmount(nightVacationsAmount);
	setAccumulatedNightVacationsAmount(accumulatedNightVacationsAmount);
	setRequestedNightHours(requestedNightHours);
	setRequestedExtraNightHours(requestedExtraNightHours);
	setRequestedExtraNightDays(requestedExtraNightDays);
	setRequestedHolidayHours(requestedHolidayHours);
	setRequestedSaturdayHours(requestedSaturdayHours);
	setRequestedSundayHours(requestedSundayHours);
	setRequestedWorkdayHours(requestedWorkdayHours);
	setHolidayAmount(holidayAmount);
	setSaturdayAmount(saturdayAmount);
	setSundayAmount(sundayAmount);
	setWorkdayFirstLevelAmount(workdayFirstLevelAmount);
	setWorkdaySecondLevelAmount(workdaySecondLevelAmount);
	setExtraNightFirstLevelAmount(extraNightFirstLevelAmount);
	setExtraNightSecondLevelAmount(extraNightSecondLevelAmount);
	setExtraNightMealAmount(nightExtraWorkMealAmount);
	setAmount(holidayAmount + saturdayAmount + sundayAmount + workdayFirstLevelAmount + workdaySecondLevelAmount
		+ extraNightFirstLevelAmount + extraNightSecondLevelAmount + nightExtraWorkMealAmount);
    }

    public int getTotalHours() {
	int result = 0;
	if (getNightHours() != null) {
	    result = result + getNightHours();
	}
	if (getExtraNightHours() != null) {
	    result = result + getExtraNightHours();
	}
	if (getHolidayHours() != null) {
	    result = result + getHolidayHours();
	}
	if (getSaturdayHours() != null) {
	    result = result + getSaturdayHours();
	}
	if (getSundayHours() != null) {
	    result = result + getSundayHours();
	}
	if (getWorkdayHours() != null) {
	    result = result + getWorkdayHours();
	}
	return result;
    }

    public String getHoursDoneYearMonthString() {
	ResourceBundle bundleEnumeration = ResourceBundle.getBundle("resources.EnumerationResources", Language.getLocale());
	String month = bundleEnumeration.getString((Month.values()[getHoursDoneInPartialDate().get(
		DateTimeFieldType.monthOfYear()) - 1]).toString());
	return new StringBuilder(month).append(" ").append(getHoursDoneInPartialDate().get(DateTimeFieldType.year())).toString();
    }

    public void delete() {
	if (canBeDeleted()) {
	    removeRootDomainObject();
	    removeAssiduousness();
	    removeModifiedBy();
	    removeUnit();
	    deleteDomainObject();
	}
    }

    public boolean canBeDeleted() {
	ClosedMonth payingMonth = ClosedMonth.getClosedMonth(getPartialPayingDate());
	return payingMonth == null || (payingMonth != null && !payingMonth.getClosedForExtraWork());
    }

    public void updateAmount() {
	BigDecimal holidayAmount = new BigDecimal(getHolidayAmount().toString());
	BigDecimal saturdayAmount = new BigDecimal(getSaturdayAmount().toString());
	BigDecimal sundayAmount = new BigDecimal(getSundayAmount().toString());
	BigDecimal workdayFirstLevelAmount = new BigDecimal(getWorkdayFirstLevelAmount().toString());
	BigDecimal workdaySecondLevelAmount = new BigDecimal(getWorkdaySecondLevelAmount().toString());
	BigDecimal nightExtraWorkFirstLevelAmount = new BigDecimal(getExtraNightFirstLevelAmount().toString());
	BigDecimal nightExtraWorkSecondLevelAmount = new BigDecimal(getExtraNightSecondLevelAmount().toString());
	BigDecimal extraNightMealAmount = new BigDecimal(getExtraNightMealAmount().toString());

	setAmount(holidayAmount.add(saturdayAmount).add(sundayAmount).add(workdayFirstLevelAmount).add(workdaySecondLevelAmount)
		.add(nightExtraWorkFirstLevelAmount).add(nightExtraWorkSecondLevelAmount).add(extraNightMealAmount).doubleValue());
    }

    private YearMonth getPaymentYearMonth() {
	YearMonth yearMonth = new YearMonth(getPartialPayingDate());
	yearMonth.addMonth();
	return yearMonth;
    }

    public Integer getPaymentYear() {
	return getPaymentYearMonth().getYear();
    }

    public Double getWorkdayAmount() {
	return getWorkdayFirstLevelAmount() + getWorkdaySecondLevelAmount();
    }

    public Double getExtraNightWorkAmount() {
	return getExtraNightFirstLevelAmount() + getExtraNightSecondLevelAmount();
    }

    public EmployeeExtraWorkAuthorization getEmployeeExtraWorkAuthorization() {
	LocalDate begin = new LocalDate(getHoursDoneInPartialDate().get(DateTimeFieldType.year()), getHoursDoneInPartialDate()
		.get(DateTimeFieldType.monthOfYear()), 1);
	LocalDate end = new LocalDate(getHoursDoneInPartialDate().get(DateTimeFieldType.year()), getHoursDoneInPartialDate().get(
		DateTimeFieldType.monthOfYear()), begin.dayOfMonth().getMaximumValue());
	for (EmployeeExtraWorkAuthorization employeeExtraWorkAuthorization : getAssiduousness()
		.getEmployeeExtraWorkAuthorizations()) {
	    if (employeeExtraWorkAuthorization.getExtraWorkAuthorization().existsBetweenDates(begin, end)
		    && employeeExtraWorkAuthorization.getExtraWorkAuthorization().getPayingUnit().equals(getUnit())) {
		return employeeExtraWorkAuthorization;
	    }
	}
	return null;
    }

    public BigDecimal getEmployeeSalary() throws ExcepcaoPersistencia {
	LocalDate date = new LocalDate(getHoursDoneInPartialDate().get(DateTimeFieldType.year()), getHoursDoneInPartialDate()
		.get(DateTimeFieldType.monthOfYear()), 1);
	return getAssiduousness().getEmployeeSalary(date);
    }

    public Integer getTotalWeekDaysHoursPayedInYear() {
	int result = 0;
	YearMonth yearMonth = getPaymentYearMonth();
	for (ExtraWorkRequest extraWorkRequest : getAssiduousness().getExtraWorkRequests()) {
	    if (extraWorkRequest.getPaymentYear().equals(yearMonth.getYear())
		    && extraWorkRequest.getPaymentYearMonth().getNumberOfMonth() <= yearMonth.getNumberOfMonth()) {
		if (extraWorkRequest.getWorkdayHours() != null) {
		    result = result + extraWorkRequest.getWorkdayHours();
		}
		if (extraWorkRequest.getExtraNightHours() != null) {
		    result = result + extraWorkRequest.getExtraNightHours();
		}
	    }
	}
	return result;
    }

    public BigDecimal getTotalWeekDaysAmountPayedInYear() {
	BigDecimal result = new BigDecimal(0);
	YearMonth yearMonth = getPaymentYearMonth();
	for (ExtraWorkRequest extraWorkRequest : getAssiduousness().getExtraWorkRequests()) {
	    if (extraWorkRequest.getPaymentYear().equals(yearMonth.getYear())
		    && extraWorkRequest.getPaymentYearMonth().getNumberOfMonth() <= yearMonth.getNumberOfMonth()) {
		if (extraWorkRequest.getWorkdayAmount() != null) {
		    result = result.add(new BigDecimal(extraWorkRequest.getWorkdayAmount()));
		}
		if (extraWorkRequest.getExtraNightWorkAmount() != null) {
		    result = result.add(new BigDecimal(extraWorkRequest.getExtraNightWorkAmount()));
		}
	    }
	}
	return result;
    }

    public Integer getTotalVacationsDaysInYear() {
	int result = 0;
	YearMonth yearMonth = getPaymentYearMonth();
	for (ExtraWorkRequest extraWorkRequest : getAssiduousness().getExtraWorkRequests()) {
	    if (extraWorkRequest.getPaymentYear().equals(yearMonth.getYear())
		    && extraWorkRequest.getPaymentYearMonth().getNumberOfMonth() <= yearMonth.getNumberOfMonth()) {
		if (extraWorkRequest.getNormalVacationsDays() != null) {
		    result = result + extraWorkRequest.getNormalVacationsDays();
		}
		if (extraWorkRequest.getNightVacationsAmount() != null) {
		    result = result + extraWorkRequest.getNightVacationsDays();
		}
	    }
	}
	return result;
    }

}
