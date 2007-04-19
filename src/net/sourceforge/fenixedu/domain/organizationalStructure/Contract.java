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
import net.sourceforge.fenixedu.util.ContractType;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.YearMonthDay;

public class Contract extends Contract_Base {

    public static final Comparator<Contract> CONTRACT_COMPARATOR_BY_BEGIN_DATE = new ComparatorChain();
    public static final Comparator<Contract> CONTRACT_COMPARATOR_BY_PERSON_NAME = new ComparatorChain();
    static {
	((ComparatorChain) CONTRACT_COMPARATOR_BY_BEGIN_DATE).addComparator(new BeanComparator("beginDate"));
	((ComparatorChain) CONTRACT_COMPARATOR_BY_BEGIN_DATE).addComparator(DomainObject.COMPARATOR_BY_ID);
	
	((ComparatorChain) CONTRACT_COMPARATOR_BY_PERSON_NAME).addComparator(new BeanComparator("person.name", Collator.getInstance()));
	((ComparatorChain) CONTRACT_COMPARATOR_BY_PERSON_NAME).addComparator(DomainObject.COMPARATOR_BY_ID);
    }

    public Contract(Person person, YearMonthDay beginDate, YearMonthDay endDate, Unit unit,
	    ContractType type) {

	super();
	AccountabilityType accountabilityType = AccountabilityType
		.readAccountabilityTypeByType(AccountabilityTypeEnum.EMPLOYEE_CONTRACT);

	setParentParty(unit);
	setChildParty(person);
	setAccountabilityType(accountabilityType);
	setContractType(type);
	setOccupationInterval(beginDate, endDate);	
    }

    @Override
    public void setContractType(ContractType contractType) {
	if (contractType == null) {
	    throw new DomainException("error.contract.empty.contractType");
	}
	super.setContractType(contractType);
    }

    @Override
    public void setBeginDate(YearMonthDay beginDate) {
	checkContractDatesIntersection(getEmployee(), beginDate, getEndDate());
	super.setBeginDate(beginDate);
    }

    @Override
    public void setEndDate(YearMonthDay endDate) {
	checkContractDatesIntersection(getEmployee(), getBeginDate(), endDate);
	super.setEndDate(endDate);
    }

    public void setOccupationInterval(YearMonthDay beginDate, YearMonthDay endDate) {
	checkContractDatesIntersection(getEmployee(), beginDate, endDate);
	super.setBeginDate(beginDate);
	super.setEndDate(endDate);
    }

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

    private void checkContractDatesIntersection(Employee employee, YearMonthDay begin, YearMonthDay end) {
	checkBeginDateAndEndDate(begin, end);
	// for (Contract contract : employee.getContracts()) {
	// if (contract.checkDatesIntersections(begin, end)) {
	// throw new
	// DomainException("error.employee.contract.dates.intersection");
	// }
	// }
    }

    private boolean checkDatesIntersections(YearMonthDay begin, YearMonthDay end) {
	return ((end == null || !this.getBeginDate().isAfter(end)) && (this.getEndDate() == null || !this
		.getEndDate().isBefore(begin)));
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
