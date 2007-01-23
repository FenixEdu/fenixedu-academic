package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class Periodicity extends Periodicity_Base {

    public Periodicity(Integer workWeekNumber) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setWorkWeekNumber(workWeekNumber);
    }

    public boolean occur(int weekNumber, int maxWorkWeek) {
        int result = weekNumber % maxWorkWeek;
        if (result == 0) {
            result = maxWorkWeek;
        }
        return result == getWorkWeekNumber();
    }

    public void delete() {
        if (canBeDeleted()) {
            removeRootDomainObject();
            deleteDomainObject();
        }
    }

    public boolean canBeDeleted() {
        return !hasAnyWorkSchedules();
    }
}
