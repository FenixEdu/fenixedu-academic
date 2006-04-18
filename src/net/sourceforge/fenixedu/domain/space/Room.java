package net.sourceforge.fenixedu.domain.space;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.YearMonthDay;

public class Room extends Room_Base {

	public Room() {
        super();
        setOjbConcreteClass(this.getClass().getName());
    }

    public Room(final Space suroundingSpace, final String name) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        if (suroundingSpace == null) {
            throw new NullPointerException("error.surrounding.space");
        }

        setSuroundingSpace(suroundingSpace);
        new RoomInformation(this, name);
    }

    @Override
    public RoomInformation getSpaceInformation() {
        return (RoomInformation) super.getSpaceInformation();
    }

    @Override
    public RoomInformation getSpaceInformation(final YearMonthDay when) {
        return (RoomInformation) super.getSpaceInformation(when);
    }

    public void edit(final String name) {
        new RoomInformation(this, name);
    }

}
