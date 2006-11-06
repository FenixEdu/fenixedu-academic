package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.Date;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.YearMonthDay;

public class Accountability extends Accountability_Base {

    protected Accountability() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setOjbConcreteClass(getClass().getName());
    }

    protected Accountability(Party parentParty, Party childParty, AccountabilityType accountabilityType) {
	this();
	setParentParty(parentParty);
	setChildParty(childParty);
	setAccountabilityType(accountabilityType);
    }

    public void delete() {
	super.setAccountabilityType(null);
	super.setChildParty(null);
	super.setParentParty(null);
	removeRootDomainObject();
	super.deleteDomainObject();
    }

    public boolean belongsToPeriod(YearMonthDay begin, YearMonthDay end) {
	return ((end == null || !this.getBeginDate().isAfter(end)) && (this.getEndDate() == null || !this
		.getEndDate().isBefore(begin)));
    }

    public boolean isActive(YearMonthDay currentDate) {
	return belongsToPeriod(currentDate, currentDate);
    }

    public Boolean isPersonFunction() {
	return this.getAccountabilityType() instanceof Function && this.getParentParty() instanceof Unit
		&& this instanceof PersonFunction;
    }

    public Date getBeginDateInDateType() {
	return (this.getBeginDate() != null) ? this.getBeginDate().toDateTimeAtCurrentTime().toDate()
		: null;
    }

    public Date getEndDateInDateType() {
	return (this.getEndDate() != null) ? this.getEndDate().toDateTimeAtCurrentTime().toDate() : null;
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

    @Override
    public void setBeginDate(YearMonthDay beginDate) {
	if (beginDate == null) {
	    throw new DomainException("error.accountability.inexistent.beginDate");
	}
	super.setBeginDate(beginDate);
    }

    @Override
    public void setEndDate(YearMonthDay endDate) {
	if (getBeginDate() == null || (endDate != null && endDate.isBefore(getBeginDate()))) {
	    throw new DomainException("error.accountability.endDate.before.beginDate");
	}
	super.setEndDate(endDate);
    }
}
