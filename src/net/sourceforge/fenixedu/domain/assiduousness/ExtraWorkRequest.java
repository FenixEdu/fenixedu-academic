package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.joda.time.DateTime;
import org.joda.time.Partial;

public class ExtraWorkRequest extends ExtraWorkRequest_Base {

    public ExtraWorkRequest(Partial partial, Unit unit, Employee employee, Integer nightHours,
            Integer extraNightHours, Integer extraNightDays, Integer holidayHours,
            Integer saturdayHours, Integer sundayHours, Integer workdayHours, Boolean addToVacations,
            Boolean addToWeekRestTime, Employee modifiedBy) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setPartialDate(partial);
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
    }

    public void change(Integer nightHours, Integer extraNightHours, Integer extraNightDays,
            Integer holidayHours, Integer saturdayHours, Integer sundayHours, Integer workdayHours,
            Boolean addToVacations, Boolean addToWeekRestTime, Employee modifiedBy) {
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
    }

}
