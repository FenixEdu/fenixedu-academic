package net.sourceforge.fenixedu.domain.space;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.Room.RoomFactory;
import net.sourceforge.fenixedu.domain.space.Room.RoomFactoryEditor;

public class RoomInformation extends RoomInformation_Base {

    protected RoomInformation(final Room room, final RoomFactory roomFactory) {
        super();
        super.setSpace(room);

        setBlueprintNumber(roomFactory.getBlueprintNumber());
        setIdentification(roomFactory.getIdentification());
        setDescription(roomFactory.getDescription());
        setRoomClassification(roomFactory.getRoomClassification());
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
    	throw new DomainException("error.cannot.change.room");
    }

    public RoomFactoryEditor getSpaceFactoryEditor() {
    	final RoomFactoryEditor roomFactoryEditor = new RoomFactoryEditor();
    	roomFactoryEditor.setSpace((Room) getSpace());
    	roomFactoryEditor.setBlueprintNumber(getBlueprintNumber());
    	roomFactoryEditor.setIdentification(getIdentification());
    	roomFactoryEditor.setDescription(getDescription());
    	roomFactoryEditor.setClassification(getRoomClassification().getPresentationCode());
    	roomFactoryEditor.setArea(getArea());
    	roomFactoryEditor.setHeightQuality(getHeightQuality());
    	roomFactoryEditor.setIlluminationQuality(getIlluminationQuality());
    	roomFactoryEditor.setDistanceFromSanitaryInstalationsQuality(getDistanceFromSanitaryInstalationsQuality());
    	roomFactoryEditor.setSecurityQuality(getSecurityQuality());
    	roomFactoryEditor.setAgeQuality(getAgeQuality());
    	roomFactoryEditor.setObservations(getObservations());
    	return roomFactoryEditor;
    }

    @Override
    public String getPresentationName() {
        return (getIdentification() != null) ? getIdentification() + ((getDescription() != null) ? " " + getDescription() : "") 
                : ((getDescription() != null) ? getDescription() : ""); 
    }

}
