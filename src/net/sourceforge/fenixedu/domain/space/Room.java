package net.sourceforge.fenixedu.domain.space;

import org.joda.time.YearMonthDay;

public class Room extends Room_Base {

	public Room() {
        super();
        setOjbConcreteClass(this.getClass().getName());
    }

    protected Room(final Space suroundingSpace) {
        super();

        if (suroundingSpace == null) {
            throw new NullPointerException("error.surrounding.space");
        }

        setSuroundingSpace(suroundingSpace);
        new RoomInformation(this);
    }

    @Override
    public RoomInformation getSpaceInformation() {
        return (RoomInformation) super.getSpaceInformation();
    }

    @Override
    public RoomInformation getSpaceInformation(final YearMonthDay when) {
        return (RoomInformation) super.getSpaceInformation(when);
    }

}
