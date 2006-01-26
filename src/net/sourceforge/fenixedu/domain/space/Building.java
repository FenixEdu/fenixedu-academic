package net.sourceforge.fenixedu.domain.space;

import org.joda.time.YearMonthDay;

public class Building extends Building_Base {

	/** @deprecated */
    public Building() {
    }

    public Building(final String buildingName) {
        super();
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

}
