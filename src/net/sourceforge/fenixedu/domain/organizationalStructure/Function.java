package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.YearMonthDay;

public class Function extends Function_Base {

    public Function(String functionName, Date beginDate, Date endDate, FunctionType type, Unit unit,
            Function parentInherentFunction) {
        
        super();
        checkParameters(functionName, beginDate, endDate);
        checkFunctionUnit(unit);                    
        setName(functionName);        
        setBeginDate(beginDate);        
        setEndDate(endDate);
        setFunctionType(type);
        setUnit(unit);
        setParentInherentFunction(parentInherentFunction);
        setType(AccountabilityTypeEnum.MANAGEMENT_FUNCTION);
    }    
    
    public void edit(String functionName, Date beginDate, Date endDate, FunctionType type, Function parentInherentFunction) {       
        checkParameters(functionName, beginDate, endDate);        
        this.setName(functionName);        
        this.setBeginDate(beginDate);        
        this.setEndDate(endDate);
        this.setFunctionType(type);        
        this.setParentInherentFunction(parentInherentFunction);        
    }       

    private void checkParameters(String functionName, Date beginDate, Date endDate) {
        if(functionName == null && functionName.equals("")){
            throw new DomainException("error.no.function.name");
        }
        if (endDate != null && endDate.before(beginDate)) {
            throw new DomainException("error.endDateBeforeBeginDate");
        }
    }
    
    private void checkFunctionUnit(Unit unit) {
        if(unit == null) {
            throw new DomainException("error.function.no.unit");
        }
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
    
    public boolean isInherentFunction() {
        return (this.getParentInherentFunction() != null);
    }
}
