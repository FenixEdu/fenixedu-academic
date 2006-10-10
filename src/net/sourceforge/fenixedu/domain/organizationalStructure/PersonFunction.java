package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.YearMonthDay;

public class PersonFunction extends PersonFunction_Base {

    public final static Comparator<PersonFunction> COMPARATOR_BY_BEGIN_DATE = new ComparatorChain();
    static {
	((ComparatorChain) COMPARATOR_BY_BEGIN_DATE).addComparator(new BeanComparator("beginDate"));
	((ComparatorChain) COMPARATOR_BY_BEGIN_DATE).addComparator(new BeanComparator("idInternal"));
    }

    public PersonFunction(Party parentParty, Party childParty, Function function, YearMonthDay begin,
	    YearMonthDay end, Double credits) {
	super();
	checkParameters(parentParty, childParty, function, begin, end);
	checkPersonFunctionDatesIntersection((Person) childParty, (Unit) parentParty, function, begin,
		end);
	super.setBeginDate(begin);
	super.setEndDate(end);
	setParentParty(parentParty);
	setChildParty(childParty);
	setAccountabilityType(function);
	setCredits(credits);
    }

    public void edit(YearMonthDay begin, YearMonthDay end, Double credits) {
	checkParameters(getUnit(), getPerson(), getFunction(), begin, end);
	checkPersonFunctionDatesIntersection(getPerson(), getUnit(), getFunction(), begin, end);
	super.setBeginDate(begin);
	super.setEndDate(end);
	setCredits(credits);
    }

    @Override
    public void setBeginDate(YearMonthDay beginDate) {
	checkPersonFunctionDatesIntersection(getPerson(), getUnit(), getFunction(), beginDate,
		getEndDate());
	super.setBeginDate(beginDate);
    }

    @Override
    public void setEndDate(YearMonthDay endDate) {
	checkPersonFunctionDatesIntersection(getPerson(), getUnit(), getFunction(), getBeginDate(),
		endDate);
	super.setEndDate(endDate);
    }

    private void checkParameters(Party parentParty, Party childParty, Function function,
	    YearMonthDay begin, YearMonthDay end) {
	if (parentParty == null) {
	    throw new DomainException("error.personFunction.no.unit");
	}
	if (childParty == null) {
	    throw new DomainException("error.personFunction.no.person");
	}
	if (function == null) {
	    throw new DomainException("error.personFunction.no.function");
	}
	if (begin == null) {
	    throw new DomainException("error.personFunction.no.beginDate");
	}
	if (end == null) {
	    throw new DomainException("error.personFunction.no.endDate");
	}
	if (end != null && end.isBefore(begin)) {
	    throw new DomainException("error.personFunction.endDateBeforeBeginDate");
	}
    }

    private void checkPersonFunctionDatesIntersection(Person person, Unit unit, Function function,
	    YearMonthDay begin, YearMonthDay end) {

	for (PersonFunction personFunction : person.getPersonFunctions(unit)) {
	    if (!personFunction.equals(this) && personFunction.getFunction().equals(function)
		    && personFunction.checkDatesIntersections(begin, end)) {
		throw new DomainException("error.personFunction.dates.intersection.for.same.function");
	    }
	}
    }

    private boolean checkDatesIntersections(YearMonthDay begin, YearMonthDay end) {
	return ((end == null || !this.getBeginDate().isAfter(end)) && (this.getEndDate() == null || !this
		.getEndDate().isBefore(begin)));
    }

    public Person getPerson() {
	return (Person) this.getChildParty();
    }

    public Unit getUnit() {
	return (Unit) this.getParentParty();
    }

    public Function getFunction() {
	return (Function) this.getAccountabilityType();
    }

    @Override
    public Double getCredits() {
	if (super.getCredits() == null) {
	    return 0d;
	}
	return super.getCredits();
    }
}
