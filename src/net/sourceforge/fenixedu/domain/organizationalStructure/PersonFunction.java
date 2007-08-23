package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.YearMonthDay;

public class PersonFunction extends PersonFunction_Base {

    public final static Comparator<PersonFunction> COMPARATOR_BY_BEGIN_DATE = new ComparatorChain();
    public final static Comparator<PersonFunction> COMPARATOR_BY_PERSON_NAME = new ComparatorChain();
    static {
	((ComparatorChain) COMPARATOR_BY_BEGIN_DATE).addComparator(new BeanComparator("beginDate"));
	((ComparatorChain) COMPARATOR_BY_BEGIN_DATE).addComparator(DomainObject.COMPARATOR_BY_ID);
	
	((ComparatorChain) COMPARATOR_BY_PERSON_NAME).addComparator(new BeanComparator("person.name"));
	((ComparatorChain) COMPARATOR_BY_PERSON_NAME).addComparator(DomainObject.COMPARATOR_BY_ID);
    }    

    public PersonFunction(Party parentParty, Party childParty, Function function, YearMonthDay begin,
	    YearMonthDay end, Double credits) {
	super();
	setParentParty(parentParty);
	setChildParty(childParty);
	setAccountabilityType(function);
	setCredits(credits);
	setOccupationInterval(begin, end);
    }

    public PersonFunction(Party parentParty, Party childParty, Function function, YearMonthDay begin, YearMonthDay end) {
    	this(parentParty, childParty, function, begin, end, 0.0);
    }
    
    public void edit(YearMonthDay begin, YearMonthDay end, Double credits) {	
	setCredits(credits);
	setOccupationInterval(begin, end);
    }
    
    @Override
    public void setChildParty(Party childParty) {
        if(childParty == null || !childParty.isPerson()) {
            throw new DomainException("error.invalid.child.party");
        }
	super.setChildParty(childParty);
    }
    
    @Override
    public void setParentParty(Party parentParty) {
	if(parentParty == null || !parentParty.isUnit()) {
            throw new DomainException("error.invalid.parent.party");
        }
	super.setParentParty(parentParty);
    }
      
    public void setOccupationInterval(YearMonthDay beginDate, YearMonthDay endDate) {
	checkPersonFunctionDatesIntersection(getPerson(), getUnit(), getFunction(), beginDate, endDate);
	super.setBeginDate(beginDate);
	super.setEndDate(endDate);
    }     

    @Override
    public void setBeginDate(YearMonthDay beginDate) {
        throw new DomainException("error.invalid.operation");
    }
    
    @Override
    public void setEndDate(YearMonthDay endDate) {
	throw new DomainException("error.invalid.operation");
    }
    
    @Override
    public Double getCredits() {
	if (super.getCredits() == null) {
	    return 0d;
	}
	return super.getCredits();
    }    
    
    public Person getPerson() {
	return (Person) getChildParty();
    }

    public Unit getUnit() {
	return (Unit) getParentParty();
    }

    public Function getFunction() {
	return (Function) getAccountabilityType();
    }
    
    @Override
    public boolean isPersonFunction() {
        return true;
    }
        
    private void checkPersonFunctionDatesIntersection(Person person, Unit unit, Function function, YearMonthDay begin, YearMonthDay end) {
	checkBeginDateAndEndDate(begin, end);
	for (PersonFunction personFunction : person.getPersonFunctions(unit)) {
	    if (!personFunction.equals(this) && personFunction.getFunction().equals(function)
		    && personFunction.checkDatesIntersections(begin, end)) {
		throw new DomainException("error.personFunction.dates.intersection.for.same.function");
	    }
	}
    }

    private boolean checkDatesIntersections(YearMonthDay begin, YearMonthDay end) {
	return ((end == null || !getBeginDate().isAfter(end)) && (getEndDate() == null || !getEndDate().isBefore(begin)));
    }

    private void checkBeginDateAndEndDate(YearMonthDay begin, YearMonthDay end) {
	if (begin == null) {
	    throw new DomainException("error.personFunction.no.beginDate");
	}
	if (end == null) {
	    throw new DomainException("error.personFunction.no.endDate");
	}
	if (end != null && begin.isAfter(end)) {
	    throw new DomainException("error.personFunction.endDateBeforeBeginDate");
	}
    }

    public boolean hasCredits() {
	return getCredits() > 0d;
    }
}
