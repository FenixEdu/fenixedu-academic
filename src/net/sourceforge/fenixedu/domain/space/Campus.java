package net.sourceforge.fenixedu.domain.space;

import org.joda.time.YearMonthDay;

public class Campus extends Campus_Base {

    protected Campus() {
        super();
    }

    public Campus(final String buildingName) {
        this();
        new CampusInformation(this, buildingName);
    }

    @Override
    public CampusInformation getSpaceInformation() {
        return (CampusInformation) super.getSpaceInformation();
    }

    @Override
    public CampusInformation getSpaceInformation(final YearMonthDay when) {
        return (CampusInformation) super.getSpaceInformation(when);
    }

    public void edit(final String name) {
    	new CampusInformation(this, name);
    }

}
