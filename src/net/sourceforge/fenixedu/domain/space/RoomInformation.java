package net.sourceforge.fenixedu.domain.space;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.Room.RoomFactory;
import net.sourceforge.fenixedu.domain.space.Room.RoomFactoryEditor;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

public class RoomInformation extends RoomInformation_Base {

    protected RoomInformation(final Room room, final RoomFactory roomFactory) {
	super();
	super.setSpace(room);	
	setFirstTimeInterval(roomFactory.getBegin(), roomFactory.getEnd());
	setBlueprintNumber(roomFactory.getBlueprintNumber());
	setIdentification(roomFactory.getIdentification());
	setDescription(roomFactory.getDescription());
	setRoomClassification(roomFactory.getRoomClassification());
	setArea(roomFactory.getArea());
	setHeightQuality(roomFactory.getHeightQuality());
	setIlluminationQuality(roomFactory.getIlluminationQuality());
	setDistanceFromSanitaryInstalationsQuality(roomFactory
		.getDistanceFromSanitaryInstalationsQuality());
	setSecurityQuality(roomFactory.getSecurityQuality());
	setAgeQuality(roomFactory.getAgeQuality());
	setObservations(roomFactory.getObservations());
    } 

    public void editRoomCharacteristics(String blueprintNumber, String identification,
	    String description, RoomClassification roomClassification, BigDecimal area,
	    Boolean heightQuality, Boolean illuminationQuality,
	    Boolean distanceFromSanitaryInstalationsQuality, Boolean securityQuality,
	    Boolean ageQuality, String observations, YearMonthDay begin, YearMonthDay end) {

	editTimeInterval(begin, end);
	setBlueprintNumber(blueprintNumber);
	setIdentification(identification);
	setDescription(description);
	setRoomClassification(roomClassification);
	setArea(area);
	setHeightQuality(heightQuality);
	setIlluminationQuality(illuminationQuality);
	setDistanceFromSanitaryInstalationsQuality(distanceFromSanitaryInstalationsQuality);
	setSecurityQuality(securityQuality);
	setAgeQuality(ageQuality);
	setObservations(observations);	
    }
      
    @Override
    public void setBlueprintNumber(String blueprintNumber) {
	if(StringUtils.isEmpty(blueprintNumber)) {
	    throw new DomainException("error.roomInformation.empty.blueprintNumber");
	}
	super.setBlueprintNumber(blueprintNumber);
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
	roomFactoryEditor
		.setRoomClassification(getRoomClassification() != null ? getRoomClassification() : null);
	roomFactoryEditor.setArea(getArea());
	roomFactoryEditor.setHeightQuality(getHeightQuality());
	roomFactoryEditor.setIlluminationQuality(getIlluminationQuality());
	roomFactoryEditor
		.setDistanceFromSanitaryInstalationsQuality(getDistanceFromSanitaryInstalationsQuality());
	roomFactoryEditor.setSecurityQuality(getSecurityQuality());
	roomFactoryEditor.setAgeQuality(getAgeQuality());
	roomFactoryEditor.setObservations(getObservations());
	roomFactoryEditor.setBegin(getNextPossibleValidFromDate());
	return roomFactoryEditor;
    }

    @Override
    public String getPresentationName() {
	String name = (getIdentification() != null) ? getIdentification()
		+ ((getDescription() != null) ? " " + getDescription() : "")
		: ((getDescription() != null) ? getDescription() : "");
	return name.trim();
    }
}
