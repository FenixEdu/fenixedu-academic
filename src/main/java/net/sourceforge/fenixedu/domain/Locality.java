package net.sourceforge.fenixedu.domain;

public class Locality extends Locality_Base {

    public Locality() {
        super();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Holiday> getHolidays() {
        return getHolidaysSet();
    }

    @Deprecated
    public boolean hasAnyHolidays() {
        return !getHolidaysSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.space.CampusInformation> getCampusInformation() {
        return getCampusInformationSet();
    }

    @Deprecated
    public boolean hasAnyCampusInformation() {
        return !getCampusInformationSet().isEmpty();
    }

    @Deprecated
    public boolean hasName() {
        return getName() != null;
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

}
