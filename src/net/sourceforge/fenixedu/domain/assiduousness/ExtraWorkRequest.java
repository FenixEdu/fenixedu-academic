package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.Month;
import net.sourceforge.fenixedu.util.report.StyledExcelSpreadsheet;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;

public class ExtraWorkRequest extends ExtraWorkRequest_Base {

    public ExtraWorkRequest(Partial partialPayingDate, Unit unit, Employee employee, Integer nightHours,
	    Integer extraNightHours, Integer extraNightDays, Integer holidayHours,
	    Integer saturdayHours, Integer sundayHours, Integer workdayHours, Boolean addToVacations,
	    Boolean addToWeekRestTime, Employee modifiedBy, boolean approved, Partial hoursDoneIn,
	    Integer normalVacationsDays, Double normalVacationsAmount,
	    Double accumulatedNormalVacationsAmount, Integer nightVacationsDays,
	    Double nightVacationsAmount, Double accumulatedNightVacationsAmount,
	    Integer requestedNightHours, Integer requestedExtraNightHours,
	    Integer requestedHolidayHours, Integer requestedSaturdayHours, Integer requestedSundayHours,
	    Integer requestedWorkdayHours, Double holidayAmount, Double saturdayAmount,
	    Double sundayAmount) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setPartialPayingDate(partialPayingDate);
	setUnit(unit);
	setAssiduousness(employee.getAssiduousness());
	setNightHours(nightHours);
	setExtraNightHours(extraNightHours);
	setExtraNightDays(extraNightDays);
	setHolidayHours(holidayHours);
	setSaturdayHours(saturdayHours);
	setSundayHours(sundayHours);
	setWorkdayHours(workdayHours);
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
	setRequestedHolidayHours(requestedHolidayHours);
	setRequestedSaturdayHours(requestedSaturdayHours);
	setRequestedSundayHours(requestedSundayHours);
	setRequestedWorkdayHours(requestedWorkdayHours);
	setHolidayAmount(holidayAmount);
	setSaturdayAmount(saturdayAmount);
	setSundayAmount(sundayAmount);
	setAmount(holidayAmount + saturdayAmount + sundayAmount);

    }

    public void edit(Integer nightHours, Integer extraNightHours, Integer extraNightDays,
	    Integer holidayHours, Integer saturdayHours, Integer sundayHours, Integer workdayHours,
	    Boolean addToVacations, Boolean addToWeekRestTime, Employee modifiedBy,
	    Partial hoursDoneInPartialDate, Integer normalVacationsDays, Double normalVacationsAmount,
	    Double accumulatedNormalVacationsAmount, Integer nightVacationsDays,
	    Double nightVacationsAmount, Double accumulatedNightVacationsAmount,
	    Integer requestedNightHours, Integer requestedExtraNightHours,
	    Integer requestedHolidayHours, Integer requestedSaturdayHours, Integer requestedSundayHours,
	    Integer requestedWorkdayHours, Double holidayAmount, Double saturdayAmount,
	    Double sundayAmount) {
	setNightHours(nightHours);
	setExtraNightHours(extraNightHours);
	setExtraNightDays(extraNightDays);
	setHolidayHours(holidayHours);
	setSaturdayHours(saturdayHours);
	setSundayHours(sundayHours);
	setWorkdayHours(workdayHours);
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
	setRequestedHolidayHours(requestedHolidayHours);
	setRequestedSaturdayHours(requestedSaturdayHours);
	setRequestedSundayHours(requestedSundayHours);
	setRequestedWorkdayHours(requestedWorkdayHours);
	setHolidayAmount(holidayAmount);
	setSaturdayAmount(saturdayAmount);
	setSundayAmount(sundayAmount);
	setAmount(holidayAmount + saturdayAmount + sundayAmount);
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
	ResourceBundle bundleEnumeration = ResourceBundle.getBundle("resources.EnumerationResources",
		LanguageUtils.getLocale());
	String month = bundleEnumeration.getString((Month.values()[getHoursDoneInPartialDate().get(
		DateTimeFieldType.monthOfYear()) - 1]).toString());
	return new StringBuilder(month).append(" ").append(
		getHoursDoneInPartialDate().get(DateTimeFieldType.year())).toString();
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
	setAmount(getHolidayAmount() + getSaturdayAmount() + getSundayAmount());
    }
}
