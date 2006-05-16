package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class Periodicity extends Periodicity_Base {

    public Periodicity(Integer workWeekNumber) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setWorkWeekNumber(workWeekNumber);
    }

    // If weekNumber is multiple of workWeekNumber then it occurs in that week...
    public boolean occur(int weekNumber) {
        return (weekNumber % getWorkWeekNumber() == 0);
    }
    
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
