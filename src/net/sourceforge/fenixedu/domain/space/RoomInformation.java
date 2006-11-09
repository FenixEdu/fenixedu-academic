package net.sourceforge.fenixedu.domain.space;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.Room.RoomFactoryEditor;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.injectionCode.FenixDomainObjectActionLogAnnotation;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

public class RoomInformation extends RoomInformation_Base {

    @Checked("SpacePredicates.checkIfLoggedPersonHasPermissionsToManageSpaceInformation")
    @FenixDomainObjectActionLogAnnotation(actionName = "Created room information", parameters = {
	    "room", "blueprintNumber", "identification", "description", "roomClassification", "area",
	    "heightQuality", "illuminationQuality", "distanceFromSanitaryInstalationsQuality",
	    "securityQuality", "ageQuality", "observations", "begin", "end" })
    public RoomInformation(Room room, String blueprintNumber, String identification, String description,
	    RoomClassification roomClassification, BigDecimal area, Boolean heightQuality,
	    Boolean illuminationQuality, Boolean distanceFromSanitaryInstalationsQuality,
	    Boolean securityQuality, Boolean ageQuality, String observations, YearMonthDay begin,
	    YearMonthDay end) {

	super();
	super.setSpace(room);
	setFirstTimeInterval(begin, end);
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

    @Checked("SpacePredicates.checkIfLoggedPersonHasPermissionsToManageSpaceInformation")
    @FenixDomainObjectActionLogAnnotation(actionName = "Edited room information", parameters = {
	    "blueprintNumber", "identification", "description", "roomClassification", "area",
	    "heightQuality", "illuminationQuality", "distanceFromSanitaryInstalationsQuality",
	    "securityQuality", "ageQuality", "observations", "begin", "end" })
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

    @Checked("SpacePredicates.checkIfLoggedPersonHasPermissionsToManageSpaceInformation")
    @FenixDomainObjectActionLogAnnotation(actionName = "Deleted room information", parameters = {})
    public void delete() {
	super.delete();
    }

    @Override
    public void setBlueprintNumber(String blueprintNumber) {
	if (blueprintNumber == null || StringUtils.isEmpty(blueprintNumber.trim())) {
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
