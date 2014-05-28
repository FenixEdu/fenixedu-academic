/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.webServices.jersey.beans.publico;

import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.space.SpaceUtils;

import org.fenixedu.spaces.domain.Space;

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
                this.description = allocationSpace.getName();
                this.capacity =
                        new RoomCapacity(allocationSpace.getAllocatableCapacity(), allocationSpace.<Integer> getMetadata(
                                "examCapacity").orElse(0));
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
        this.containedSpaces = FluentIterable.from(space.getChildren()).transform(new Function<Space, FenixSpace>() {

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
        if (SpaceUtils.isCampus(space)) {
            return new FenixSpace.Campus(space, withParentAndContainedSpaces);
        }
        if (SpaceUtils.isBuilding(space)) {
            return new FenixSpace.Building(space, withParentAndContainedSpaces);
        }
        if (SpaceUtils.isFloor(space)) {
            return new FenixSpace.Floor(space, withParentAndContainedSpaces);
        }

        if (SpaceUtils.isRoom(space)) {
            return new FenixSpace.Room(space, withParentAndContainedSpaces);
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