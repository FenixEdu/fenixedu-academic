/*
 * Created on Sep 26, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.util.ContractType;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.YearMonthDay;

public class Contract extends Contract_Base {

    public static final Comparator<Contract> CONTRACT_COMPARATOR_BY_BEGIN_DATE = new ComparatorChain();
    static {
	((ComparatorChain) CONTRACT_COMPARATOR_BY_BEGIN_DATE).addComparator(new BeanComparator(
		"beginDate"));
	((ComparatorChain) CONTRACT_COMPARATOR_BY_BEGIN_DATE).addComparator(new BeanComparator(
		"idInternal"));
    }

    public static final Comparator<Contract> CONTRACT_COMPARATOR_BY_PERSON_NAME = new ComparatorChain();
    static {
	((ComparatorChain) CONTRACT_COMPARATOR_BY_PERSON_NAME).addComparator(new BeanComparator(
		"person.name"));
	((ComparatorChain) CONTRACT_COMPARATOR_BY_PERSON_NAME).addComparator(new BeanComparator(
		"idInternal"));
    }

    private Contract() {
	super();
    }

    public Contract(Person person, YearMonthDay beginDate, YearMonthDay endDate, Unit unit,
	    ContractType type) {

	this();
	AccountabilityType accountabilityType = AccountabilityType
		.readAccountabilityTypeByType(AccountabilityTypeEnum.EMPLOYEE_CONTRACT);
	checkParameters(person, beginDate, endDate, unit, accountabilityType);
	setParentParty(unit);
	setChildParty(person);
	setContractType(type);
	setBeginDate(beginDate);
	setEndDate(endDate);
	setAccountabilityType(accountabilityType);
    }

    private void checkParameters(Person person, YearMonthDay beginDate, YearMonthDay endDate, Unit unit,
	    AccountabilityType accountabilityType) {
	if (unit == null) {
	    throw new DomainException("error.contract.no.unit");
	}
	if (person == null) {
	    throw new DomainException("error.contract.no.employee");
	}
	if (beginDate == null) {
	    throw new DomainException("error.contract.no.beginDate");
	}
	if (endDate != null && endDate.isBefore(beginDate)) {
	    throw new DomainException("error.contract.endDateBeforeBeginDate");
	}
	if (accountabilityType == null) {
	    throw new DomainException("error.contract.no.accountabilityType");
	}
	// checkContractDatesIntersection(employee, beginDate, endDate);
    }

    // private void checkContractDatesIntersection(Employee employee, Date
    // begin, Date end) {
    // for (Contract contract : employee.getContracts()) {
    // if (contract.checkDatesIntersections(begin, end)) {
    // throw new
    // DomainException("error.employee.contract.dates.intersection");
    // }
    // }
    // }
    //    
    // private boolean checkDatesIntersections(Date begin, Date end) {
    // return ((end == null || !this.getBeginDate().after(end))
    // && (this.getEndDate() == null || !this.getEndDate().before(begin)));
    // }

    public Person getPerson() {
	return (Person) getChildParty();
    }

    public Unit getUnit() {
	return (Unit) getParentParty();
    }

    public Employee getEmployee() {
	return getPerson().getEmployee();
    }

    public Unit getWorkingUnit() {
	return getUnit();
    }

    public Unit getMailingUnit() {
	return getUnit();
    }

    public Unit getSalaryUnit() {
	return getUnit();
    }
}
