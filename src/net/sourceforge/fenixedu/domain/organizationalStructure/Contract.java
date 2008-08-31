/*
 * Created on Sep 26, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.YearMonthDay;

public abstract class Contract extends Contract_Base {

    public static final Comparator<Contract> CONTRACT_COMPARATOR_BY_BEGIN_DATE = new Comparator<Contract>() {

	@Override
	public int compare(Contract o1, Contract o2) {
	    final int c = o1.getBeginDate().compareTo(o2.getBeginDate());
	    return c == 0 ? COMPARATOR_BY_ID.compare(o1, o2) : c;
	}
	
    };
    public static final Comparator<Contract> CONTRACT_COMPARATOR_BY_PERSON_NAME = new Comparator<Contract>() {

	@Override
	public int compare(Contract o1, Contract o2) {
	    final int c = o1.getPerson().getName().compareTo(o2.getPerson().getName());
	    return c == 0 ? COMPARATOR_BY_ID.compare(o1, o2) : c;
	}
	
    };

    protected Contract() {
	super();
    }

    protected void init(Person person, YearMonthDay beginDate, YearMonthDay endDate, Unit unit) {
	setParentParty(unit);
	setChildParty(person);
	setBeginDate(beginDate);
	setEndDate(endDate);
    }

    @Override
    public void setChildParty(Party childParty) {
	if (childParty == null || !childParty.isPerson()) {
	    throw new DomainException("error.invalid.child.party");
	}
	super.setChildParty(childParty);
    }

    @Override
    public void setParentParty(Party parentParty) {
	if (parentParty == null || !parentParty.isUnit()) {
	    throw new DomainException("error.invalid.parent.party");
	}
	super.setParentParty(parentParty);
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
}
