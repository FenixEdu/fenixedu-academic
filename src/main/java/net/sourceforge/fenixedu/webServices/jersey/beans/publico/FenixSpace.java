package net.sourceforge.fenixedu.webServices.jersey.beans.publico;

import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.domain.space.Space;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @JsonSubTypes.Type(value = FenixSpace.Campus.class, name = "CAMPUS"),
        @JsonSubTypes.Type(value = FenixSpace.Building.class, name = "BUILDING"),
        @JsonSubTypes.Type(value = FenixSpace.Floor.class, name = "FLOOR"),
        @JsonSubTypes.Type(value = FenixSpace.Room.class, name = "ROOM") })
public class FenixSpace {

    public static class Campus extends FenixSpace {

        private Campus(Space space, boolean withParentAndContainedSpaces) {
            super(space, withParentAndContainedSpaces);
        }

        private Campus(Space space) {
            super(space);
        }

    }

    public static class Building extends FenixSpace {

        private Building(Space space, boolean withParentAndContainedSpaces) {
            super(space, withParentAndContainedSpaces);
            // TODO Auto-generated constructor stub
        }

        private Building(Space space) {
            super(space);
            // TODO Auto-generated constructor stub
        }

    }

    public static class Floor extends FenixSpace {

        private Floor(Space space, boolean withParentAndContainedSpaces) {
            super(space, withParentAndContainedSpaces);
        }

        private Floor(Space space) {
            super(space);
        }

    }

    public static class Room extends FenixSpace {

        public static class RoomCapacity {
            Integer normal;
            Integer exam;

            public RoomCapacity(Integer normal, Integer exam) {
                super();
                this.normal = normal;
                this.exam = exam;
            }

            public Integer getNormal() {
                return normal;
            }

            public void setNormal(Integer normalCapacity) {
                this.normal = normalCapacity;
            }

            public Integer getExam() {
                return exam;
            }

            public void setExam(Integer examCapacity) {
                this.exam = examCapacity;
            }
        }

        public String description;
        public RoomCapacity capacity;
        @JsonInclude(Include.NON_NULL)
        public List<FenixRoomEvent> events;

        public Room(AllocatableSpace allocationSpace) {
            this(allocationSpace, null);
            description = allocationSpace.getCompleteIdentificationWithoutCapacities();
        }

        public Room(AllocatableSpace allocationSpace, List<FenixRoomEvent> events) {
            super(allocationSpace);
            this.capacity = new RoomCapacity(allocationSpace.getNormalCapacity(), allocationSpace.getExamCapacity());
            this.events = events;
        }
    }

    public String id;
    public String name;

    @JsonInclude(Include.NON_NULL)
    public Set<FenixSpace> containedSpaces = null;

    @JsonInclude(Include.NON_NULL)
    public FenixSpace parentSpace = null;

    public FenixSpace(Space space) {
        this(space, false);
    }

    public FenixSpace(Space space, boolean withParentAndContainedSpaces) {
        this.id = space.getExternalId();
        this.name = space.getSpaceInformation().getPresentationName();
        if (withParentAndContainedSpaces) {
            setParentSpace(space);
            setContainedSpaces(space);
        }
    }

    private void setContainedSpaces(Space space) {
        this.containedSpaces = FluentIterable.from(space.getActiveContainedSpaces()).transform(new Function<Space, FenixSpace>() {

            @Override
            public FenixSpace apply(Space input) {
                return new FenixSpace(input);
            }
        }).toSet();
    }

    private void setParentSpace(Space space) {
        this.parentSpace = space.getSuroundingSpace() == null ? null : new FenixSpace(space.getSuroundingSpace());
    }

    public static FenixSpace getSpace(Space space, boolean withParentAndContainedSpaces) {
        if (space == null) {
            return null;
        }
        if (space.isCampus()) {
            return new FenixSpace.Campus(space, withParentAndContainedSpaces);
        }
        if (space.isBuilding()) {
            return new FenixSpace.Building(space, withParentAndContainedSpaces);
        }
        if (space.isFloor()) {
            return new FenixSpace.Floor(space, withParentAndContainedSpaces);
        }

        return null;
    }

    public static FenixSpace getSimpleSpace(Space space) {
        return getSpace(space, false);
    }

    public static FenixSpace getSpace(Space space) {
        return getSpace(space, true);
    }

}