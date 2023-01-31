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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.spaces.domain.Space;
import org.fenixedu.spaces.domain.SpaceClassification;

public class SpaceUtils {
    public static final String SCHOOL_SPACES = "School Spaces";
    public static final String CAMPUS = "Campus";
    public static final String BUILDING = "Building";
    public static final String FLOOR = "Floor";
    public static final String ROOM = "Room";

    public static Stream<Space> allocatableSpaces() {
        return Space.getSpaces().filter(space -> isAllocatable(space)).sorted();
    }

    public static List<Space> buildings() {
        return Space.getSpaces().filter(space -> isBuilding(space)).sorted().collect(Collectors.toList());
    }

    public static boolean isAllocatable(Space space) {
        return space != null && space.getClassification().isAllocatable();
    }

    private static boolean isCampus(Space space) {
        return space.getClassification().equals(SpaceClassification.getByName(CAMPUS));
    }

    private static boolean isBuilding(Space space) {
        return space.getClassification().equals(SpaceClassification.getByName(BUILDING));
    }

    private static boolean isFloor(Space space) {
        return space.getClassification().equals(SpaceClassification.getByName(FLOOR));
    }

    public static boolean isRoom(Space space) {
        return !(isCampus(space) || isBuilding(space) || isFloor(space));
    }

    public static Stream<Space> findSpaceByName(String name) {
        return Space.getSpaces().filter(space -> name.equals(space.getName()));
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