package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class Periodicity extends Periodicity_Base {

    public Periodicity(Integer workWeekNumber) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setWorkWeekNumber(workWeekNumber);
    }

    // Return true if definedInterval contains the date and the date's day of
    // week is in WorkWeek
    // public boolean isDefinedInDate(YearMonthDay date) {
    // DateTime dateAtMidnight = date.toDateTimeAtMidnight();
    // if (getDefinedInterval().contains(dateAtMidnight) && getWorkWeek().contains(dateAtMidnight)) {
    // return true;
    // }
    // return false;
    // }
    //
    // public Interval getDefinedInterval() {
    // return new Interval(getBeginDate(), getEndDate());
    // }
    public void delete() {
        if (canBeDeleted()) {
            removeRootDomainObject();
            deleteDomainObject();
        }
    }

    public boolean canBeDeleted() {
        return !hasAnyWorkSchedule();
    }
}
