package net.sourceforge.fenixedu.webServices.jersey.beans.publico;

import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.space.SpaceUtils;

import org.fenixedu.spaces.domain.Space;
import org.fenixedu.spaces.domain.SpaceClassification;
import org.fenixedu.spaces.domain.UnavailableException;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize.Typing;
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

        @JsonInclude(Include.NON_NULL)
        public String description;
        @JsonInclude(Include.NON_NULL)
        public RoomCapacity capacity;
        @JsonInclude(Include.NON_NULL)
        public List<FenixRoomEvent> events;

        /**
         * this is used to create a null object so that assignedRoom in evaluations can be null
         */
        public Room() {

        }

        public Room(Space allocationSpace) {
            this(allocationSpace, false, false, null);
        }

        public Room(Space allocationSpace, Boolean withParentAndContainedSpaces) {
            this(allocationSpace, withParentAndContainedSpaces, false, null);
        }

        public Room(Space allocationSpace, Boolean withParentAndContainedSpaces, Boolean withDescriptionAndCapacity,
                List<FenixRoomEvent> events) {
            super(allocationSpace, withParentAndContainedSpaces);
            if (withDescriptionAndCapacity) {
                try {
                    this.description = allocationSpace.getName();
                    this.capacity =
                            new RoomCapacity(allocationSpace.getAllocatableCapacity(),
                                    (Integer) allocationSpace.getMetadata("examCapacity"));
                } catch (UnavailableException e) {
                }
            }
            this.events = events;
        }

        public Room(Space allocationSpace, List<FenixRoomEvent> events) {
            this(allocationSpace, true, true, events);
        }
    }

    public String id;
    public String name;

    @JsonInclude(Include.NON_NULL)
    @JsonSerialize(typing = Typing.DYNAMIC)
    public Set<FenixSpace> containedSpaces = null;

    @JsonInclude(Include.NON_NULL)
    @JsonSerialize(typing = Typing.DYNAMIC)
    public FenixSpace parentSpace = null;

    protected FenixSpace() {

    }

    protected FenixSpace(Space space) {
        this(space, false);
    }

    protected FenixSpace(Space space, boolean withParentAndContainedSpaces) {
        this.id = space.getExternalId();
        this.name = space.getName();
        if (withParentAndContainedSpaces) {
            setParentSpace(space);
            setContainedSpaces(space);
        }
    }

    private void setContainedSpaces(Space space) {
        this.containedSpaces =
                FluentIterable.from(SpaceUtils.getActiveChildrenSet(space)).transform(new Function<Space, FenixSpace>() {

                    @Override
                    public FenixSpace apply(Space input) {
                        return getSimpleSpace(input);
                    }
                }).toSet();
    }

    private void setParentSpace(Space space) {
        this.parentSpace = space.getParent() == null ? null : getSimpleSpace(space.getParent());
    }

    public static FenixSpace getSpace(Space space, boolean withParentAndContainedSpaces) {
        if (space == null) {
            return null;
        }
        try {
            if (SpaceClassification.getByName("Campus").equals(space.getClassification())) {
                return new FenixSpace.Campus(space, withParentAndContainedSpaces);
            }
            if (SpaceClassification.getByName("Building").equals(space.getClassification())) {
                return new FenixSpace.Building(space, withParentAndContainedSpaces);
            }
            if (SpaceClassification.getByName("Floor").equals(space.getClassification())) {
                return new FenixSpace.Floor(space, withParentAndContainedSpaces);
            }

            if (SpaceUtils.isRoom(space)) {
                return new FenixSpace.Room((Space) space, withParentAndContainedSpaces);
            }

        } catch (UnavailableException e) {
            return null;
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