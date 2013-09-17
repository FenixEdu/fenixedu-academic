package net.sourceforge.fenixedu.domain.space;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.resource.ResourceAllocation;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import net.sourceforge.fenixedu.injectionCode.FenixDomainObjectActionLogAnnotation;
import net.sourceforge.fenixedu.predicates.SpacePredicates;

import org.joda.time.YearMonthDay;

public class RoomSubdivision extends RoomSubdivision_Base {

    public RoomSubdivision(Space suroundingSpace, String identification, YearMonthDay begin, YearMonthDay end) {
        super();
        setSuroundingSpace(suroundingSpace);
        new RoomSubdivisionInformation(identification, this, begin, end);
    }

    @Override
    @FenixDomainObjectActionLogAnnotation(actionName = "Deleted roomSubdivision", parameters = {})
    public void delete() {
        check(this, SpacePredicates.checkPermissionsToManageSpace);
        super.delete();
    }

    @Override
    public RoomSubdivisionInformation getSpaceInformation() {
        return (RoomSubdivisionInformation) super.getSpaceInformation();
    }

    @Override
    public RoomSubdivisionInformation getSpaceInformation(YearMonthDay when) {
        return (RoomSubdivisionInformation) super.getSpaceInformation(when);
    }

    @Override
    public void setSuroundingSpace(Space suroundingSpace) {
        if (suroundingSpace == null || !suroundingSpace.isRoom()) {
            throw new DomainException("error.Space.invalid.suroundingSpace");
        }
        super.setSuroundingSpace(suroundingSpace);
    }

    @Override
    public List<ResourceAllocation> getResourceAllocationsForCheck() {
        List<ResourceAllocation> result = new ArrayList<ResourceAllocation>();
        result.addAll(getResourceAllocations());
        Room suroundingSpace = (Room) getSuroundingSpace();
        result.addAll(suroundingSpace.getResourceAllocations());
        return result;
    }

    @Override
    public boolean isRoomSubdivision() {
        return true;
    }

    @Override
    public Integer getExamCapacity() {
        return Integer.valueOf(0);
    }

    @Override
    public Integer getNormalCapacity() {
        return Integer.valueOf(0);
    }

    @Override
    public Integer getCapacidadeExame() {
        return Integer.valueOf(0);
    }

    @Override
    public Integer getCapacidadeNormal() {
        return Integer.valueOf(0);
    }

    @Override
    public void setExamCapacity(Integer capacidadeExame) {
        // Do nothing !!
    }

    @Override
    public void setNormalCapacity(Integer capacidadeNormal) {
        // Do nothing !!
    }

    @Override
    public String getIdentification() {
        return getSpaceInformation().getIdentification();
    }

    @Override
    public RoomClassification getRoomClassification() {
        return ((AllocatableSpace) getSuroundingSpace()).getRoomClassification();
    }

    @Override
    public RoomClassification getTipo() {
        return ((AllocatableSpace) getSuroundingSpace()).getRoomClassification();
    }

    public static abstract class RoomSubdivisionFactory implements Serializable, FactoryExecutor {

        private String identification;

        private YearMonthDay begin;

        private YearMonthDay end;

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

        public String getIdentification() {
            return identification;
        }

        public void setIdentification(String identification) {
            this.identification = identification;
        }
    }

    public static class RoomSubdivisionFactoryCreator extends RoomSubdivisionFactory {

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
        public RoomSubdivision execute() {
            return new RoomSubdivision(getSurroundingSpace(), getIdentification(), getBegin(), getEnd());
        }
    }

    public static class RoomSubdivisionFactoryEditor extends RoomSubdivisionFactory {

        private RoomSubdivision roomSubdivisionReference;

        public RoomSubdivision getSpace() {
            return roomSubdivisionReference;
        }

        public void setSpace(RoomSubdivision roomSubdivision) {
            if (roomSubdivision != null) {
                this.roomSubdivisionReference = roomSubdivision;
            }
        }

        @Override
        public RoomSubdivisionInformation execute() {
            return new RoomSubdivisionInformation(getIdentification(), getSpace(), getBegin(), getEnd());
        }
    }
}
