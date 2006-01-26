package net.sourceforge.fenixedu.domain.space;

import org.joda.time.YearMonthDay;

public class Floor extends Floor_Base {

    protected Floor(final Space suroundingSpace) {
        super();

        if (suroundingSpace == null) {
            throw new NullPointerException("error.surrounding.space");
        }

        setSuroundingSpace(suroundingSpace);
        new FloorInformation(this);
    }

    @Override
    public FloorInformation getSpaceInformation() {
        return (FloorInformation) super.getSpaceInformation();
    }

    @Override
    public FloorInformation getSpaceInformation(final YearMonthDay when) {
        return (FloorInformation) super.getSpaceInformation(when);
    }

}
