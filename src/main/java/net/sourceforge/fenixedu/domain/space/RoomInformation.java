package net.sourceforge.fenixedu.domain.space;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.Room.RoomFactoryEditor;
import net.sourceforge.fenixedu.injectionCode.FenixDomainObjectActionLogAnnotation;
import net.sourceforge.fenixedu.predicates.SpacePredicates;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

public class RoomInformation extends RoomInformation_Base {

    @FenixDomainObjectActionLogAnnotation(actionName = "Created room information", parameters = { "room", "blueprintNumber",
            "identification", "description", "roomClassification", "area", "heightQuality", "illuminationQuality",
            "distanceFromSanitaryInstalationsQuality", "securityQuality", "ageQuality", "observations", "begin", "end",
            "doorNumber" })
    public RoomInformation(Room room, String blueprintNumber, String identification, String description,
            RoomClassification roomClassification, BigDecimal area, Boolean heightQuality, Boolean illuminationQuality,
            Boolean distanceFromSanitaryInstalationsQuality, Boolean securityQuality, Boolean ageQuality, String observations,
            YearMonthDay begin, YearMonthDay end, String doorNumber) {

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
        setDoorNumber(doorNumber);
    }

    @FenixDomainObjectActionLogAnnotation(actionName = "Edited room information", parameters = { "blueprintNumber",
            "identification", "description", "roomClassification", "area", "heightQuality", "illuminationQuality",
            "distanceFromSanitaryInstalationsQuality", "securityQuality", "ageQuality", "observations", "begin", "end",
            "doorNumber", "normalCapacity", "examCapacity" })
    public void editRoomCharacteristics(String blueprintNumber, String identification, String description,
            RoomClassification roomClassification, BigDecimal area, Boolean heightQuality, Boolean illuminationQuality,
            Boolean distanceFromSanitaryInstalationsQuality, Boolean securityQuality, Boolean ageQuality, String observations,
            YearMonthDay begin, YearMonthDay end, String doorNumber, Integer normalCapacity, Integer examCapacity, String emails) {

        check(this, SpacePredicates.checkIfLoggedPersonHasPermissionsToManageSpaceInformation);
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
        setDoorNumber(doorNumber);
        setNormalCapacity(normalCapacity);
        setExamCapacity(examCapacity);
        setEmails(emails);
    }

    @FenixDomainObjectActionLogAnnotation(actionName = "Edited room information", parameters = { "blueprintNumber",
            "identification", "description", "roomClassification", "observations", "begin", "end", "doorNumber",
            "normalCapacity", "examCapacity" })
    public void editLimitedRoomCharacteristics(String blueprintNumber, String identification, String description,
            RoomClassification roomClassification, String observations, YearMonthDay begin, YearMonthDay end, String doorNumber,
            Integer normalCapacity, Integer examCapacity) {

        check(this, SpacePredicates.checkIfLoggedPersonHasPermissionsToEditSpaceInformation);
        editTimeInterval(begin, end);
        setBlueprintNumber(blueprintNumber);
        setIdentification(identification);
        setDescription(description);
        setRoomClassification(roomClassification);
        setObservations(observations);
        setDoorNumber(doorNumber);
        setNormalCapacity(normalCapacity);
        setExamCapacity(examCapacity);
    }

    private void setNormalCapacity(Integer normalCapacity) {
        getRoom().setNormalCapacity(normalCapacity);
    }

    private void setExamCapacity(Integer examCapacity) {
        getRoom().setExamCapacity(examCapacity);
    }

    @Override
    @FenixDomainObjectActionLogAnnotation(actionName = "Deleted room information", parameters = {})
    public void delete() {
        check(this, SpacePredicates.checkIfLoggedPersonHasPermissionsToManageSpaceInformation);
        super.delete();
    }

    @Override
    public void deleteWithoutCheckNumberOfSpaceInformations() {
        setRoomClassification(null);
        super.deleteWithoutCheckNumberOfSpaceInformations();
    }

    @Override
    public void setRoomClassification(RoomClassification roomClassification) {
        if (roomClassification != null && !roomClassification.hasParentRoomClassification()) {
            throw new DomainException("error.roomInformation.invalid.roomClassification");
        }
        super.setRoomClassification(roomClassification);
    }

    @Override
    public void setSpace(final Space space) {
        throw new DomainException("error.incompatible.space");
    }

    public void setSpace(final Room room) {
        throw new DomainException("error.cannot.change.room");
    }

    public Integer getNormalCapacity() {
        return getRoom().getNormalCapacity();
    }

    public Integer getExamCapacity() {
        return getRoom().getExamCapacity();
    }

    @Override
    public RoomFactoryEditor getSpaceFactoryEditor() {
        final RoomFactoryEditor roomFactoryEditor = new RoomFactoryEditor();
        roomFactoryEditor.setSpace((Room) getSpace());
        roomFactoryEditor.setBlueprintNumber(getBlueprintNumber());
        roomFactoryEditor.setIdentification(getIdentification());
        roomFactoryEditor.setDescription(getDescription());
        roomFactoryEditor.setRoomClassification(getRoomClassification() != null ? getRoomClassification() : null);
        roomFactoryEditor.setArea(getArea());
        roomFactoryEditor.setHeightQuality(getHeightQuality());
        roomFactoryEditor.setIlluminationQuality(getIlluminationQuality());
        roomFactoryEditor.setDistanceFromSanitaryInstalationsQuality(getDistanceFromSanitaryInstalationsQuality());
        roomFactoryEditor.setSecurityQuality(getSecurityQuality());
        roomFactoryEditor.setAgeQuality(getAgeQuality());
        roomFactoryEditor.setObservations(getObservations());
        roomFactoryEditor.setDoorNumber(getDoorNumber());
        roomFactoryEditor.setBegin(getNextPossibleValidFromDate());
        return roomFactoryEditor;
    }

    @Override
    public String getPresentationName() {
        String name =
                !StringUtils.isEmpty(getIdentification()) ? (getIdentification() + (!StringUtils.isEmpty(getDescription()) ? " - "
                        + getDescription() : "")) : (!StringUtils.isEmpty(getDescription()) ? getDescription() : "");
        return name.trim();
    }

    public Room getRoom() {
        return (Room) getSpace();
    }

    @Deprecated
    public boolean hasIlluminationQuality() {
        return getIlluminationQuality() != null;
    }

    @Deprecated
    public boolean hasDescription() {
        return getDescription() != null;
    }

    @Deprecated
    public boolean hasDistanceFromSanitaryInstalationsQuality() {
        return getDistanceFromSanitaryInstalationsQuality() != null;
    }

    @Deprecated
    public boolean hasHeightQuality() {
        return getHeightQuality() != null;
    }

    @Deprecated
    public boolean hasDoorNumber() {
        return getDoorNumber() != null;
    }

    @Deprecated
    public boolean hasRoomClassification() {
        return getRoomClassification() != null;
    }

    @Deprecated
    public boolean hasArea() {
        return getArea() != null;
    }

    @Deprecated
    public boolean hasAgeQuality() {
        return getAgeQuality() != null;
    }

    @Deprecated
    public boolean hasObservations() {
        return getObservations() != null;
    }

    @Deprecated
    public boolean hasIdentification() {
        return getIdentification() != null;
    }

    @Deprecated
    public boolean hasSecurityQuality() {
        return getSecurityQuality() != null;
    }

}
