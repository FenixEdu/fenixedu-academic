package net.sourceforge.fenixedu.dataTransferObject.assiduousness;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.assiduousness.ClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.EmployeeExtraWorkAuthorization;
import net.sourceforge.fenixedu.domain.assiduousness.ExtraWorkAuthorization;
import net.sourceforge.fenixedu.domain.assiduousness.ExtraWorkRequest;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import net.sourceforge.fenixedu.util.Month;

import org.apache.struts.action.ActionMessage;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;
import org.joda.time.YearMonthDay;

public class ExtraWorkRequestFactory implements Serializable, FactoryExecutor {
    private Integer year;

    private Month month;

    private Integer unitCode;

    private DomainReference<Unit> unit;

    private DomainReference<Employee> employee;

    private Integer nightHours;

    private Integer extraNightHours;

    private Integer extraNightDays;

    private Integer holidayHours;

    private Integer saturdayHours;

    private Integer sundayHours;

    private Integer workdayHours;

    private Boolean addToVacations;

    private Boolean addToWeekRestTime;

    private DomainReference<Employee> modifiedBy;

    private DomainReference<ExtraWorkRequest> extraWorkRequest;

    public ExtraWorkRequestFactory() {
        super();
        YearMonthDay date = new YearMonthDay();
        setYear(date.getYear());
        setMonth(Month.values()[date.getMonthOfYear() - 1]);
    }

    public ExtraWorkRequestFactory(YearMonthDay date) {
        super();
        setYear(date.getYear());
        setMonth(Month.values()[date.getMonthOfYear() - 1]);
    }

    public ExtraWorkRequestFactory(Integer year, String month, Integer unitCode, Employee employee) {
        setYear(year);
        setMonth(Month.valueOf(month));
        setUnitCode(unitCode);
        setEmployee(employee);
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
    }

    public Unit getUnit() {
        return unit == null ? (getUnitCode() == null ? null : Unit.readByCostCenterCode(getUnitCode()))
                : unit.getObject();
    }

    public void setUnit(Unit unit) {
        if (unit != null) {
            this.unit = new DomainReference<Unit>(unit);
        }
    }

    public Employee getEmployee() {
        return employee == null ? null : employee.getObject();
    }

    public void setEmployee(Employee employee) {
        if (employee != null) {
            this.employee = new DomainReference<Employee>(employee);
            ExtraWorkRequest extraWorkRequest = getEmployeeExtraWorkRequest();
            if (extraWorkRequest != null) {
                setExtraWorkRequest(extraWorkRequest);
            }

        }
    }

    public Employee getModifiedBy() {
        return modifiedBy == null ? null : modifiedBy.getObject();
    }

    public void setModifiedBy(Employee modifiedBy) {
        if (modifiedBy != null) {
            this.modifiedBy = new DomainReference<Employee>(modifiedBy);
        }
    }

    public List<Employee> getEmployeeList() {
        List<Employee> employeeList = new ArrayList<Employee>();
        if (getUnit() != null) {
            YearMonthDay begin = new YearMonthDay(getYear(), getMonth().ordinal() + 1, 1);
            YearMonthDay end = new YearMonthDay(getYear(), getMonth().ordinal() + 1, begin.dayOfMonth()
                    .getMaximumValue());
            for (ExtraWorkAuthorization extraWorkAuthorization : getUnit()
                    .getExtraPayingUnitAuthorizations()) {
                if (extraWorkAuthorization.existsBetweenDates(begin, end)) {
                    for (EmployeeExtraWorkAuthorization employeeExtraWorkAuthorization : extraWorkAuthorization
                            .getEmployeeExtraWorkAuthorizations()) {
                        if (!employeeList.contains(employeeExtraWorkAuthorization.getAssiduousness()
                                .getEmployee())) {
                            employeeList.add(employeeExtraWorkAuthorization.getAssiduousness()
                                    .getEmployee());
                        }
                    }
                }
            }
        }

        return employeeList;
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

    public static class ExtraWorkRequestFactoryEditor extends ExtraWorkRequestFactory {

        public ExtraWorkRequestFactoryEditor(ExtraWorkRequest extraWorkRequest) {

            setPartialDate(extraWorkRequest.getPartialDate());

            setUnitCode(extraWorkRequest.getUnit().getCostCenterCode());

            setUnit(extraWorkRequest.getUnit());

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

        }

        public Object execute() {
            if (isEmptyPaymentRequest()) {
                return new ActionMessage("error.extraWorkRequest.emptyPaymentRequest");
            }
            if (!validateExtraNightWorkFields()) {
                return new ActionMessage("error.extraWorkRequest.invalidExtraNightWorkFields");
            }
            if (!validateRemunerationOptionFields()) {
                return new ActionMessage("error.extraWorkRequest.invalidRemunerationOptionFields");
            }
            getExtraWorkRequest().change(getNightHours(), getExtraNightHours(), getExtraNightDays(),
                    getHolidayHours(), getSaturdayHours(), getSundayHours(), getWorkdayHours(),
                    getAddToVacations(), getAddToWeekRestTime(), getModifiedBy());
            return null;
        }
    }

    public Partial getPartialDate() {
        DateTimeFieldType[] types = new DateTimeFieldType[] { DateTimeFieldType.year(),
                DateTimeFieldType.monthOfYear() };
        int[] values = new int[] { getYear(), getMonth().ordinal() + 1 };
        return new Partial(types, values);
    }

    public void setPartialDate(Partial partial) {
        setYear(partial.get(DateTimeFieldType.year()));
        setMonth(Month.values()[partial.get(DateTimeFieldType.monthOfYear()) - 1]);
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
            if (getNightHours() != null || getExtraNightHours() != null || getExtraNightDays() != null
                    || getHolidayHours() != null || getSaturdayHours() != null
                    || getSundayHours() != null || getWorkdayHours() != null) {
                new ExtraWorkRequest(getPartialDate(), getUnit(), getEmployee(), getNightHours(),
                        getExtraNightHours(), getExtraNightDays(), getHolidayHours(),
                        getSaturdayHours(), getSundayHours(), getWorkdayHours(), getAddToVacations(),
                        getAddToWeekRestTime(), getModifiedBy());
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
        for (ExtraWorkRequest extraWorkRequest : getUnit().getExtraWorkRequests()) {
            if (extraWorkRequest.getPartialDate().equals(getPartialDate())
                    && extraWorkRequest.getAssiduousness().equals(getEmployee().getAssiduousness())) {
                return extraWorkRequest;
            }
        }
        return null;
    }

    public Object getExtraWorkRequestFactoryEditor() {
        return new ExtraWorkRequestFactoryEditor(getExtraWorkRequest());
    }

    public Object getIsMonthClosedForExtraWork() {
        return ClosedMonth.isMonthClosedForExtraWork(getPartialDate());
    }
    
}
