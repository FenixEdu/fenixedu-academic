package net.sourceforge.fenixedu.domain.space;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.YearMonthDay;

public class Building extends Building_Base {

    protected Building() {
        super();
    }

    public Building(final String buildingName) {
        this();
        new BuildingInformation(this, buildingName);
    }

    @Override
    public BuildingInformation getSpaceInformation() {
        return (BuildingInformation) super.getSpaceInformation();
    }

    @Override
    public BuildingInformation getSpaceInformation(final YearMonthDay when) {
        return (BuildingInformation) super.getSpaceInformation(when);
    }

    public void edit(final String name) {
    	new BuildingInformation(this, name);
    }

}
