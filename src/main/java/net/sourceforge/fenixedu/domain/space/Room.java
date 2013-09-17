package net.sourceforge.fenixedu.domain.space;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.resource.ResourceAllocation;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import net.sourceforge.fenixedu.injectionCode.FenixDomainObjectActionLogAnnotation;
import net.sourceforge.fenixedu.predicates.SpacePredicates;

import org.joda.time.YearMonthDay;

public class Room extends Room_Base {

    public Room(Space suroundingSpace, String blueprintNumber, String identification, String description,
            RoomClassification roomClassification, BigDecimal area, Boolean heightQuality, Boolean illuminationQuality,
            Boolean distanceFromSanitaryInstalationsQuality, Boolean securityQuality, Boolean ageQuality, String observations,
            YearMonthDay begin, YearMonthDay end, String doorNumber, Integer normalCapacity, Integer examCapacity) {

        super();

        setSuroundingSpace(suroundingSpace);
        setNormalCapacity(normalCapacity);
        setExamCapacity(examCapacity);

        new RoomInformation(this, blueprintNumber, identification, description, roomClassification, area, heightQuality,
                illuminationQuality, distanceFromSanitaryInstalationsQuality, securityQuality, ageQuality, observations, begin,
                end, doorNumber);
    }

    @Override
    public void setSuroundingSpace(Space suroundingSpace) {
        if (suroundingSpace == null || suroundingSpace.isRoomSubdivision()) {
            throw new DomainException("error.Space.invalid.suroundingSpace");
        }
        super.setSuroundingSpace(suroundingSpace);
    }

    @Override
    public RoomInformation getSpaceInformation() {
        return (RoomInformation) super.getSpaceInformation();
    }

    @Override
    public RoomInformation getSpaceInformation(final YearMonthDay when) {
        return (RoomInformation) super.getSpaceInformation(when);
    }

    @Override
    @FenixDomainObjectActionLogAnnotation(actionName = "Deleted room", parameters = {})
    public void delete() {
        check(this, SpacePredicates.checkPermissionsToManageSpace);
        if (!canBeDeleted()) {
            throw new DomainException("error.room.cannot.be.deleted");
        }
        super.delete();
    }

    private boolean canBeDeleted() {
        return !hasAnyAssociatedSummaries() && !hasAnyWrittenEvaluationEnrolments() && !hasAnyAssociatedInquiriesRooms();
    }

    @Override
    public boolean isRoom() {
        return true;
    }

    @Override
    public Collection<ResourceAllocation> getResourceAllocationsForCheck() {
        List<RoomSubdivision> roomSubdivisions = getRoomSubdivisions();
        if (roomSubdivisions.isEmpty()) {
            return getResourceAllocations();
        } else {
            List<ResourceAllocation> result = new ArrayList<ResourceAllocation>();
            result.addAll(getResourceAllocations());
            for (RoomSubdivision roomSubdivision : getRoomSubdivisions()) {
                result.addAll(roomSubdivision.getResourceAllocations());
            }
            return result;
        }
    }

    private List<RoomSubdivision> getRoomSubdivisions() {
        List<RoomSubdivision> result = new ArrayList<RoomSubdivision>();
        for (Space subSpace : getContainedSpaces()) {
            if (subSpace.isRoomSubdivision()) {
                result.add((RoomSubdivision) subSpace);
            }
        }
        return result;
    }

    @Override
    @Deprecated
    public RoomClassification getTipo() {
        return getSpaceInformation().getRoomClassification();
    }

    @Override
    public RoomClassification getRoomClassification() {
        return getSpaceInformation().getRoomClassification();
    }

    @Override
    @Deprecated
    public Integer getCapacidadeNormal() {
        return getNormalCapacity();
    }

    @Override
    @Deprecated
    public Integer getCapacidadeExame() {
        return getExamCapacity();
    }

    @Override
    public Integer getNormalCapacity() {
        return super.getNormalCapacity() != null ? super.getNormalCapacity() : Integer.valueOf(0);
    }

    @Override
    public Integer getExamCapacity() {
        return super.getExamCapacity() != null ? super.getExamCapacity() : Integer.valueOf(0);
    }

    @Override
    public String getIdentification() {
        return getSpaceInformation().getIdentification();
    }

    public static abstract class RoomFactory implements Serializable, FactoryExecutor {

        private String blueprintNumber;

        private String identification;

        private String description;

        private BigDecimal area;

        private Boolean heightQuality;

        private Boolean illuminationQuality;

        private Boolean distanceFromSanitaryInstalationsQuality;

        private Boolean securityQuality;

        private Boolean ageQuality;

        private String observations;

        private YearMonthDay begin;

        private YearMonthDay end;

        private String doorNumber;

        private RoomClassification roomClassificationReference;

        private Integer examCapacity;

        private Integer normalCapacity;

        public YearMonthDay getBegin() {
            return begin;
        }

        public void setBegin(YearMonthDay begin) {
            this.begin = begin;
        }

        public YearMonthDay getEnd() {
            return end;
        }

        public void setEnd(YearMonthDay end) {
            this.end = end;
        }

        public Boolean getAgeQuality() {
            return ageQuality;
        }

        public void setAgeQuality(Boolean ageQuality) {
            this.ageQuality = ageQuality;
        }

        public BigDecimal getArea() {
            return area;
        }

        public void setArea(BigDecimal area) {
            this.area = area;
        }

        public String getBlueprintNumber() {
            return blueprintNumber;
        }

        public void setBlueprintNumber(String blueprintNumber) {
            this.blueprintNumber = blueprintNumber;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Boolean getDistanceFromSanitaryInstalationsQuality() {
            return distanceFromSanitaryInstalationsQuality;
        }

        public void setDistanceFromSanitaryInstalationsQuality(Boolean distanceFromSanitaryInstalationsQuality) {
            this.distanceFromSanitaryInstalationsQuality = distanceFromSanitaryInstalationsQuality;
        }

        public Boolean getHeightQuality() {
            return heightQuality;
        }

        public void setHeightQuality(Boolean heightQuality) {
            this.heightQuality = heightQuality;
        }

        public String getIdentification() {
            return identification;
        }

        public void setIdentification(String identification) {
            this.identification = identification;
        }

        public Boolean getIlluminationQuality() {
            return illuminationQuality;
        }

        public void setIlluminationQuality(Boolean illuminationQuality) {
            this.illuminationQuality = illuminationQuality;
        }

        public String getObservations() {
            return observations;
        }

        public void setObservations(String observations) {
            this.observations = observations;
        }

        public Boolean getSecurityQuality() {
            return securityQuality;
        }

        public void setSecurityQuality(Boolean securityQuality) {
            this.securityQuality = securityQuality;
        }

        public RoomClassification getRoomClassification() {
            return this.roomClassificationReference;
        }

        public void setRoomClassification(RoomClassification roomClassification) {
            this.roomClassificationReference = roomClassification;
        }

        public String getDoorNumber() {
            return doorNumber;
        }

        public void setDoorNumber(String doorNumber) {
            this.doorNumber = doorNumber;
        }

        public Integer getExamCapacity() {
            return examCapacity;
        }

        public void setExamCapacity(Integer examCapacity) {
            this.examCapacity = examCapacity;
        }

        public Integer getNormalCapacity() {
            return normalCapacity;
        }

        public void setNormalCapacity(Integer normalCapacity) {
            this.normalCapacity = normalCapacity;
        }
    }

    public static class RoomFactoryCreator extends RoomFactory {

        private Space surroundingSpaceReference;

        public Space getSurroundingSpace() {
            return surroundingSpaceReference;
        }

        public void setSurroundingSpace(Space surroundingSpace) {
            if (surroundingSpace != null) {
                this.surroundingSpaceReference = surroundingSpace;
            }
        }

        @Override
        public Room execute() {
            return new Room(getSurroundingSpace(), getBlueprintNumber(), getIdentification(), getDescription(),
                    getRoomClassification(), getArea(), getHeightQuality(), getIlluminationQuality(),
                    getDistanceFromSanitaryInstalationsQuality(), getSecurityQuality(), getAgeQuality(), getObservations(),
                    getBegin(), getEnd(), getDoorNumber(), getNormalCapacity(), getExamCapacity());
        }
    }

    public static class RoomFactoryEditor extends RoomFactory {

        private Room roomReference;

        public Room getSpace() {
            return roomReference;
        }

        public void setSpace(Room room) {
            if (room != null) {
                this.roomReference = room;
            }
        }

        @Override
        public RoomInformation execute() {

            Room space = getSpace();
            space.setNormalCapacity(getNormalCapacity());
            space.setExamCapacity(getExamCapacity());

            RoomInformation roomInformation =
                    new RoomInformation(getSpace(), getBlueprintNumber(), getIdentification(), getDescription(),
                            getRoomClassification(), getArea(), getHeightQuality(), getIlluminationQuality(),
                            getDistanceFromSanitaryInstalationsQuality(), getSecurityQuality(), getAgeQuality(),
                            getObservations(), getBegin(), getEnd(), getDoorNumber());

            return roomInformation;
        }
    }

    public static Integer countAllAvailableSeatsForExams() {
        int countAllSeatsForExams = 0;
        for (AllocatableSpace room : getAllRoomsForAlameda()) {
            countAllSeatsForExams += room.getExamCapacity();
        }
        return countAllSeatsForExams;
    }

    @Deprecated
    public boolean hasExamCapacity() {
        return getExamCapacity() != null;
    }

    @Deprecated
    public boolean hasNormalCapacity() {
        return getNormalCapacity() != null;
    }

}
