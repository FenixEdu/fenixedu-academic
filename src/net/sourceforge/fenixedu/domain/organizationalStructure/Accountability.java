package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.Date;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.YearMonthDay;

public class Accountability extends Accountability_Base {

    protected Accountability() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setOjbConcreteClass(getClass().getName());
    }

    Accountability(Party parentParty, Party childParty, AccountabilityType accountabilityType) {
        this();
        setParentParty(parentParty);
        setChildParty(childParty);
        setAccountabilityType(accountabilityType);
    }

    public void delete() {
        removeAccountabilityType();
        removeChildParty();
        removeParentParty();
        removeRootDomainObject();
        deleteDomainObject();
    }

    public boolean belongsToPeriod(YearMonthDay begin, YearMonthDay end) {
        return (!this.getBeginDate().isAfter(end) && (this.getEndDate() == null || !this.getEndDate()
                .isBefore(begin)));
    }

    public boolean isActive(YearMonthDay currentDate) {
        return (!this.getBeginDate().isAfter(currentDate) && (this.getEndDate() == null || !this
                .getEndDate().isBefore(currentDate)));
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
}
