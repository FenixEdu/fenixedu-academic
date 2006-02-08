package net.sourceforge.fenixedu.domain.space;

import org.joda.time.YearMonthDay;

public class Floor extends Floor_Base {

    public Floor(final Space suroundingSpace, final Integer level) {
        super();

        if (suroundingSpace == null) {
            throw new NullPointerException("error.surrounding.space");
        }

        setSuroundingSpace(suroundingSpace);
        new FloorInformation(this, level);
    }

    @Override
    public FloorInformation getSpaceInformation() {
        return (FloorInformation) super.getSpaceInformation();
    }

    @Override
    public FloorInformation getSpaceInformation(final YearMonthDay when) {
        return (FloorInformation) super.getSpaceInformation(when);
    }

    public void edit(final Integer level) {
        new FloorInformation(this, level);
    }

}
