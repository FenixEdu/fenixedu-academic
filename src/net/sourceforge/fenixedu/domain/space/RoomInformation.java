package net.sourceforge.fenixedu.domain.space;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.Room.RoomFactory;

public class RoomInformation extends RoomInformation_Base {

    protected RoomInformation(final Room room, final RoomFactory roomFactory) {
        super();
        super.setSpace(room);

        setBlueprintNumber(roomFactory.getBlueprintNumber());
        setIdentification(roomFactory.getIdentification());
        setDescription(roomFactory.getDescription());
        setClassification(roomFactory.getClassification());
        setArea(roomFactory.getArea());
        setHeightQuality(roomFactory.getHeightQuality());
        setIlluminationQuality(roomFactory.getIlluminationQuality());
        setDistanceFromSanitaryInstalationsQuality(roomFactory.getDistanceFromSanitaryInstalationsQuality());
        setSecurityQuality(roomFactory.getSecurityQuality());
        setAgeQuality(roomFactory.getAgeQuality());
        setObservations(roomFactory.getObservations());
    }

    @Override
    public void setSpace(final Space space) {
        throw new DomainException("error.incompatible.space");
    }

    public void setSpace(final Room room) {
//        if (room == null) {
//            throw new NullPointerException("error.room.cannot.be.null");
//        } else if (getSpace() != null) {
            throw new DomainException("error.cannot.change.room");
//        }
//        super.setSpace(room);
    }

    public void edit(final String name) {
//        setName(name);
    }

}
