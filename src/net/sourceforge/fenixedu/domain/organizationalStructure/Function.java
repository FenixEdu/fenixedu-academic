package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.YearMonthDay;

public class Function extends Function_Base {

    public boolean isInherentFunction() {
        return (this.getParentInherentFunction() != null);
    }

    public void edit(String functionName, Date beginDate, Date endDate, FunctionType type, Unit unit,
            Function parentInherentFunction) {

        if(functionName == null && functionName.equals("")){
            throw new DomainException("error.no.function.name");
        }
        this.setName(functionName);        
        this.setBeginDate(beginDate);
        if (endDate != null && endDate.before(beginDate)) {
            throw new DomainException("error.endDateBeforeBeginDate");
        }
        this.setEndDate(endDate);
        this.setFunctionType(type);
        this.setUnit(unit);
        this.setParentInherentFunction(parentInherentFunction);
        this.setType(AccountabilityTypeEnum.MANAGEMENT_FUNCTION);
    }

    public boolean isActive(YearMonthDay currentDate) {
        return (!this.getBeginDateYearMonthDay().isAfter(currentDate) &&
                (this.getEndDateYearMonthDay() == null || !this.getEndDateYearMonthDay().isBefore(currentDate)));
    }

    public boolean belongsToPeriod(YearMonthDay beginDate, YearMonthDay endDate) {
        return (!this.getBeginDateYearMonthDay().isAfter(endDate) && (this.getEndDateYearMonthDay() == null || !this.getEndDateYearMonthDay()
                .isBefore(beginDate)));
    }

    public void delete() {
        if (!hasAnyAccountabilities() && !hasAnyInherentFunctions()) {
            removeParentInherentFunction();
            removeUnit();
            removeRootDomainObject();
            deleteDomainObject();
        } else {
            throw new DomainException("error.delete.function");
        }
    }

    public List<PersonFunction> getPersonFunctions() {
        List<PersonFunction> personFunctions = new ArrayList();
        for (Accountability accountability : getAccountabilities()) {
            personFunctions.add((PersonFunction) accountability);
        }
        return personFunctions;
    }
}
