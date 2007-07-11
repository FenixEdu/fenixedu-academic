package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

public class Function extends Function_Base {

    public Function(String functionName, YearMonthDay beginDate, YearMonthDay endDate, FunctionType type, Unit unit) {
	super();
	edit(functionName, beginDate, endDate, type);
	setUnit(unit);
	setType(AccountabilityTypeEnum.MANAGEMENT_FUNCTION);
    }

    public void edit(String functionName, YearMonthDay beginDate, YearMonthDay endDate, FunctionType type) {
	setName(functionName);
	setFunctionType(type);
	setBeginDateYearMonthDay(beginDate);
	setEndDateYearMonthDay(endDate);
    }
      
    @Override
    public void setUnit(Unit unit) {
	if (unit == null) {
	    throw new DomainException("error.function.no.unit");
	}
	super.setUnit(unit);
    }

    @Override
    public void setName(String name) {
	if (name == null || StringUtils.isEmpty(name.trim())) {
	    throw new DomainException("error.no.function.name");
	}
	super.setName(name);
    }

    public boolean isActive(YearMonthDay currentDate) {
	return belongsToPeriod(currentDate, currentDate);
    }

    public boolean belongsToPeriod(YearMonthDay beginDate, YearMonthDay endDate) {
	return ((endDate == null || !getBeginDateYearMonthDay().isAfter(endDate)) 
		&& (getEndDateYearMonthDay() == null || !this.getEndDateYearMonthDay().isBefore(beginDate)));
    }

    public void delete() {
	if(!canBeDeleted()) {
	    throw new DomainException("error.delete.function");
	}	
	removeParentInherentFunction();
	super.setUnit(null);
	removeRootDomainObject();
	deleteDomainObject();
    }
    
    private boolean canBeDeleted() {
	return !hasAnyAccountabilities() && !hasAnyInherentFunctions();
    }

    public List<PersonFunction> getPersonFunctions() {
	List<PersonFunction> personFunctions = new ArrayList<PersonFunction>();
	for (Accountability accountability : getAccountabilities()) {
	    if (accountability.isPersonFunction()) {
		personFunctions.add((PersonFunction) accountability);
	    }
	}
	return personFunctions;
    }

    public boolean isInherentFunction() {
	return (this.getParentInherentFunction() != null);
    }

    public void addParentInherentFunction(Function parentInherentFunction) {
	if (parentInherentFunction.equals(this)) {
	    throw new DomainException("error.function.parentInherentFunction.equals.function");
	}
	removeParentInherentFunction();
	setParentInherentFunction(parentInherentFunction);
    }
    
    @jvstm.cps.ConsistencyPredicate
    protected boolean checkDateInterval() {
	final YearMonthDay start = getBeginDateYearMonthDay();
	final YearMonthDay end = getEndDateYearMonthDay();	
	return start != null && (end == null || !start.isAfter(end));
    }

    @jvstm.cps.ConsistencyPredicate
    protected boolean checkRequiredParameters() {
	return hasUnit() && !StringUtils.isEmpty(getName()); 	
    }
}
