package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.Date;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.YearMonthDay;

public class Accountability extends Accountability_Base {

    protected Accountability() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public Accountability(Party parentParty, Party childParty, AccountabilityType accountabilityType) {
	this();
	setParentParty(parentParty);
	setChildParty(childParty);
	setAccountabilityType(accountabilityType);
	setBeginDate(new YearMonthDay());
    }

    public void delete() {
	super.setAccountabilityType(null);
	super.setChildParty(null);
	super.setParentParty(null);
	removeRootDomainObject();
	super.deleteDomainObject();
    }

    public boolean belongsToPeriod(YearMonthDay begin, YearMonthDay end) {
	return ((end == null || !getBeginDate().isAfter(end)) && (getEndDate() == null || !getEndDate().isBefore(begin)));
    }

    public boolean isActive(YearMonthDay currentDate) {
	return belongsToPeriod(currentDate, currentDate);
    }

    public boolean isFinished() {
	return getEndDate() != null && getEndDate().isBefore(new YearMonthDay());
    }

    public boolean isPersonFunction() {
	return false;
    }

    public Date getBeginDateInDateType() {
	return (getBeginDate() != null) ? getBeginDate().toDateTimeAtCurrentTime().toDate() : null;
    }

    public Date getEndDateInDateType() {
	return (getEndDate() != null) ? getEndDate().toDateTimeAtCurrentTime().toDate() : null;
    }

    @Override
    public void setChildParty(Party childParty) {
	if (childParty == null) {
	    throw new DomainException("error.accountability.inexistent.childParty");
	}
	super.setChildParty(childParty);
    }

    @Override
    public void setParentParty(Party parentParty) {
	if (parentParty == null) {
	    throw new DomainException("error.accountability.inexistent.parentParty");
	}
	super.setParentParty(parentParty);
    }

    @Override
    public void setAccountabilityType(AccountabilityType accountabilityType) {
	if (accountabilityType == null) {
	    throw new DomainException("error.accountability.inexistent.accountabilityType");
	}
	super.setAccountabilityType(accountabilityType);
    }

    @jvstm.cps.ConsistencyPredicate
    protected boolean checkDateInterval() {
	final YearMonthDay start = getBeginDate();
	final YearMonthDay end = getEndDate();
	return start != null && (end == null || !start.isAfter(end));
    }
}
