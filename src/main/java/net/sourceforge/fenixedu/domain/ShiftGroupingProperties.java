package net.sourceforge.fenixedu.domain;

public class ShiftGroupingProperties extends ShiftGroupingProperties_Base {

    public ShiftGroupingProperties(Shift shift, Grouping grouping, Integer capacity) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
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

}
