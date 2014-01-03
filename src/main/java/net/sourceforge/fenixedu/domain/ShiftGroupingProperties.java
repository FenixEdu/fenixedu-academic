package net.sourceforge.fenixedu.domain;

import org.fenixedu.bennu.core.domain.Bennu;

public class ShiftGroupingProperties extends ShiftGroupingProperties_Base {

    public ShiftGroupingProperties(Shift shift, Grouping grouping, Integer capacity) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setCapacity(capacity);
        setShift(shift);
        setGrouping(grouping);
    }

    public void delete() {
        setGrouping(null);
        setShift(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasShift() {
        return getShift() != null;
    }

    @Deprecated
    public boolean hasCapacity() {
        return getCapacity() != null;
    }

    @Deprecated
    public boolean hasGrouping() {
        return getGrouping() != null;
    }

}
