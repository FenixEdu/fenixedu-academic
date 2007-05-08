/*
 * Created on Sep 26, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.text.Collator;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.YearMonthDay;

public abstract class Contract extends Contract_Base {

    public static final Comparator<Contract> CONTRACT_COMPARATOR_BY_BEGIN_DATE = new ComparatorChain();
    public static final Comparator<Contract> CONTRACT_COMPARATOR_BY_PERSON_NAME = new ComparatorChain();
    static {
	((ComparatorChain) CONTRACT_COMPARATOR_BY_BEGIN_DATE).addComparator(new BeanComparator("beginDate"));
	((ComparatorChain) CONTRACT_COMPARATOR_BY_BEGIN_DATE).addComparator(DomainObject.COMPARATOR_BY_ID);
	
	((ComparatorChain) CONTRACT_COMPARATOR_BY_PERSON_NAME).addComparator(new BeanComparator("person.name", Collator.getInstance()));
	((ComparatorChain) CONTRACT_COMPARATOR_BY_PERSON_NAME).addComparator(DomainObject.COMPARATOR_BY_ID);
    }

    protected Contract() {
	super();	
    }
   
    protected void init(Person person, YearMonthDay beginDate, YearMonthDay endDate, Unit unit) {
	setParentParty(unit);
	setChildParty(person);		
	setOccupationInterval(beginDate, endDate);
    }
    
    @Override
    public void setBeginDate(YearMonthDay beginDate) {
	checkBeginDateAndEndDate(beginDate, getEndDate());	
	super.setBeginDate(beginDate);
    }

    @Override
    public void setEndDate(YearMonthDay endDate) {
	checkBeginDateAndEndDate(getBeginDate(), endDate);
	super.setEndDate(endDate);
    }

    public void setOccupationInterval(YearMonthDay beginDate, YearMonthDay endDate) {
	checkBeginDateAndEndDate(beginDate, endDate);
	super.setBeginDate(beginDate);
	super.setEndDate(endDate);
    }

    public Person getPerson() {
	return (Person) getChildParty();
    }

    public Unit getUnit() {
	return (Unit) getParentParty();
    }
    
    public Unit getMailingUnit() {
	return null;
    }
    
    public Unit getWorkingUnit() {
	return null;
    } 
    
    public Employee getEmployee() {
	return null;
    }
    
    private void checkBeginDateAndEndDate(YearMonthDay beginDate, YearMonthDay endDate) {
	if (beginDate == null) {
	    throw new DomainException("error.contract.no.beginDate");
	}
	if (endDate != null && endDate.isBefore(beginDate)) {
	    throw new DomainException("error.contract.endDateBeforeBeginDate");
	}
    }       
}
