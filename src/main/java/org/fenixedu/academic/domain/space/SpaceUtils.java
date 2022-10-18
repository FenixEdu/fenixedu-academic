/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.space;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.academic.domain.Lesson;
import org.fenixedu.academic.domain.LessonInstance;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.spaces.domain.Space;
import org.fenixedu.spaces.domain.SpaceClassification;
import org.fenixedu.spaces.domain.occupation.Occupation;

public class SpaceUtils {
    public static final String SCHOOL_SPACES = "School Spaces";
    public static final String CAMPUS = "Campus";
    public static final String BUILDING = "Building";
    public static final String FLOOR = "Floor";
    public static final String ROOM_SUBDIVISION = "Room Subdivision";
    public static final String ROOM = "Room";

    public static Stream<Space> allocatableSpaces() {
        return Space.getSpaces().filter(space -> isAllocatable(space)).sorted();
    }

    public static List<Space> buildings() {
        return Space.getSpaces().filter(space -> isBuilding(space)).sorted().collect(Collectors.toList());
    }

    public static boolean isAllocatable(Space space) {
        return space.getClassification().isAllocatable();
    }

    private static boolean isCampus(Space space) {
        return SpaceClassification.getByName(CAMPUS).equals(space.getClassification());
    }

    private static boolean isBuilding(Space space) {
        return SpaceClassification.getByName(BUILDING).equals(space.getClassification());
    }

    private static boolean isFloor(Space space) {
        return SpaceClassification.getByName(FLOOR).equals(space.getClassification());
    }

    private static boolean isRoomSubdivision(Space space) {
        return SpaceClassification.getByName(ROOM_SUBDIVISION).equals(space.getClassification());
    }

    public static boolean isRoom(Space space) {
        return !(isCampus(space) || isBuilding(space) || isFloor(space) || isRoomSubdivision(space));
    }

    public static Stream<Space> findSpaceByName(String name) {
        return Space.getSpaces().filter(space -> name.equals(space.getName()));
    }

    // This method is a hack because of a bad refactor. The new Occupation API does not allow sharing of occupation for different events.
    // TODO : remove this stuff in fenix v4 after refactor is complete.
    public static Occupation getFirstOccurrenceOfResourceAllocationByClass(final Space space, final Lesson lesson) {
        for (final Occupation resourceAllocation : space.getOccupationSet()) {
            if (resourceAllocation instanceof LessonInstanceSpaceOccupation) {
                final LessonInstanceSpaceOccupation lessonInstanceSpaceOccupation =
                        (LessonInstanceSpaceOccupation) resourceAllocation;
                final Set<LessonInstance> instancesSet = lessonInstanceSpaceOccupation.getLessonInstancesSet();
                if (!instancesSet.isEmpty() && instancesSet.iterator().next().getLesson() == lesson) {
                    return resourceAllocation;
                }
            }
        }
        return null;
    }

    public static Space getDefaultCampus() {
        if (Bennu.getInstance().getDefaultCampus() == null) {
            return Space.getTopLevelSpaces().stream().sorted(Comparator.comparing(Space::getPresentationName)).findFirst()
                    .orElse(null);
        }
        return Bennu.getInstance().getDefaultCampus();
    }

    public static Space getSpaceBuilding(Space space) {
        if (isBuilding(space)) {
            return space;
        }
        return space.getParent() == null || !space.getParent().isActive() ? null : getSpaceBuilding(space.getParent());
    }

    public static Space getSpaceCampus(Space space) {
        if (isCampus(space)) {
            return space;
        }
        return space.getParent() == null ? null : getSpaceCampus(space.getParent());
    }

}