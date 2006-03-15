package net.sourceforge.fenixedu.domain.space;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class RoomInformation extends RoomInformation_Base {
    
    protected RoomInformation(final Room room, final String name) {
        super();
        setSpace(room);
        setName(name);
    }

    @Override
    public void createNewSpaceInformation() {
        final Room room = (Room) getSpace();
        final RoomInformation newRoomInformation = new RoomInformation(room, getName());
        newRoomInformation.setArea(getArea());
        newRoomInformation.setHight(getHight());
        newRoomInformation.setIllumination(getIllumination());
        // TODO
        //newRoomInformation.setDistribution(getDistribution());
        //newRoomInformation.setSalubrityArea(getSalubrityArea());
        newRoomInformation.setSecurity(getSecurity());
        //newRoomInformation.setVptoValue(getVptoValue());
        //newRoomInformation.setIndex(getIndex());
        //newRoomInformation.setTm(getTm());
        newRoomInformation.setObservations(getObservations());
        newRoomInformation.setCapacityForLessons(getCapacityForLessons());
        newRoomInformation.setCapacityForExams(getCapacityForExams());
    }

    @Override
    public void setSpace(final Space space) {
        throw new DomainException("error.incompatible.space");
    }

    public void setSpace(final Room room) {
        if (room == null) {
            throw new NullPointerException("error.room.cannot.be.null");
        } else if (getSpace() != null) {
            throw new DomainException("error.cannot.change.room");
        }
        super.setSpace(room);
    }

    public void edit(final String name) {
        setName(name);
    }

}
