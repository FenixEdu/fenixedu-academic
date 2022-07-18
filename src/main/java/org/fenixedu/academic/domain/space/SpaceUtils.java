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

    // public static final String LABORATORY_FOR_EDUCATION_CODE = "2.1", LABORATORY_FOR_RESEARCHER_CODE = "2.2";

//    public final static Comparator<Space> ROOM_COMPARATOR_BY_NAME = new ComparatorChain();
//    static {
//        ((ComparatorChain) ROOM_COMPARATOR_BY_NAME).addComparator(new BeanComparator("name", Collator.getInstance()));
//        ((ComparatorChain) ROOM_COMPARATOR_BY_NAME).addComparator(new BeanComparator("externalId"));
//    }

//    public static final Comparator<SpaceClassification> COMPARATORY_BY_PARENT_ROOM_CLASSIFICATION_AND_CODE =
//            new BeanComparator("absoluteCode");

//    public static Space findAllocatableSpaceForEducationByName(String name) {
//        return allocatableSpacesForEducation().filter(space -> space.getName().equalsIgnoreCase(name)).findAny().orElse(null);
//    }

    public static Stream<Space> allocatableSpaces() {
        return Space.getSpaces().filter(space -> isAllocatable(space)).sorted();
    }

//    public static Stream<Space> allocatableSpacesForEducation() {
//        return allocatableSpaces().filter(space -> isRoom(space));
//    }

//    public static List<InfoRoom> allocatableSpace(YearMonthDay startDate, YearMonthDay endDate, HourMinuteSecond startTimeHMS,
//            HourMinuteSecond endTimeHMS, DiaSemana dayOfWeek, Integer normalCapacity, FrequencyType frequency, boolean withLabs) {
//        return allocatableSpace(normalCapacity, withLabs)
//                .filter(space -> isFree(space, startDate, endDate, startTimeHMS, endTimeHMS, dayOfWeek, frequency, true, true))
//                .map(space -> InfoRoom.newInfoFromDomain(space)).collect(Collectors.toList());
//    }

//    public static List<InfoRoom> allocatableSpace(Integer normalCapacity, boolean withLabs, List<Interval> intervals) {
//        return allocatableSpace(normalCapacity, withLabs).filter(space -> isFree(space, intervals))
//                .map(space -> InfoRoom.newInfoFromDomain(space)).collect(Collectors.toList());
//    }
//
//    public static List<Space> allocatableSpace(final Integer normalCapacity, final boolean withLabs,
//            final Interval... intervals) {
//        return allocatableSpace(normalCapacity, withLabs).filter(space -> space.isFree(intervals)).collect(Collectors.toList());
//    }
//
//    public static Stream<Space> allocatableSpace(final Integer normalCapacity, final boolean withLabs) {
//        Stream<Space> stream = allocatableSpacesForEducation();
//        if (normalCapacity != null) {
//            stream = stream.filter(space -> space.getAllocatableCapacity() >= normalCapacity);
//        }
//        if (!withLabs) {
//            stream = stream.filter(space -> !isLaboratory(space));
//        }
//        return stream;
//    }

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

//    public static boolean isLaboratory(Space space) {
//        SpaceClassification classification = space.getClassification();
//        return classification.equals(SpaceClassification.get(LABORATORY_FOR_EDUCATION_CODE))
//                || classification.equals(SpaceClassification.get(LABORATORY_FOR_RESEARCHER_CODE));
//    }

//    public static boolean isForEducation(Space space) {
//        return isForEducation(space, Authenticate.getUser());
//    }
//
//    private static boolean isForEducation(Space space, User user) {
//
//        if (!space.isActive() || !(isRoom(space) || isRoomSubdivision(space))) {
//            return false;
//        }
//
//        final Group lessonGroup = space.getOccupationsGroupWithChainOfResponsability();
//        if (lessonGroup != null && lessonGroup.getMembers().findAny().isPresent()
//                && (user == null || lessonGroup.isMember(user))) {
//            return true;
//        }
//        return false;
//    }

//    public static boolean isFree(Space space, YearMonthDay startDate, YearMonthDay endDate, HourMinuteSecond startTime,
//            HourMinuteSecond endTime, DiaSemana dayOfWeek, FrequencyType frequency, Boolean dailyFrequencyMarkSaturday,
//            Boolean dailyFrequencyMarkSunday) {
//        return isFree(space, startDate, endDate, startTime, endTime, dayOfWeek, frequency, dailyFrequencyMarkSaturday,
//                dailyFrequencyMarkSunday, null);
//    }

//    public static boolean isFree(Space space, YearMonthDay startDate, YearMonthDay endDate, HourMinuteSecond startTime,
//            HourMinuteSecond endTime, DiaSemana dayOfWeek, FrequencyType frequency, Boolean dailyFrequencyMarkSaturday,
//            Boolean dailyFrequencyMarkSunday) {
//        List<Interval> intervals = null;
//
//        for (Occupation spaceOccupation : getResourceAllocationsForCheck(space)) {
//            if (spaceOccupation instanceof EventSpaceOccupation) {
//                EventSpaceOccupation occupation = (EventSpaceOccupation) spaceOccupation;
//                if (occupation.alreadyWasOccupiedIn(startDate, endDate, startTime, endTime, dayOfWeek, frequency,
//                        dailyFrequencyMarkSaturday, dailyFrequencyMarkSunday)) {
//                    return false;
//                }
//            }
//
//            if (spaceOccupation.getClass().equals(Occupation.class)) {
//                if (intervals == null) {
//                    intervals = EventSpaceOccupation.generateEventSpaceOccupationIntervals(startDate, endDate, startTime, endTime,
//                            frequency, dayOfWeek, dailyFrequencyMarkSaturday, dailyFrequencyMarkSunday, null, null);
//                }
//                if (spaceOccupation.overlaps(intervals)) {
//                    return false;
//                }
//            }
//        }
//
//        return true;
//    }

//    public static boolean isFree(Space space, List<Interval> intervals) {
//        for (Occupation spaceOccupation : getResourceAllocationsForCheck(space)) {
//            if (spaceOccupation.overlaps(intervals)) {
//                return false;
//            }
//        }
//        return true;
//    }

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

//    public static SortedSet<SpaceClassification> sortByRoomClassificationAndCode(
//            final Collection<SpaceClassification> roomClassifications) {
//        return ImmutableSortedSet.orderedBy(COMPARATORY_BY_PARENT_ROOM_CLASSIFICATION_AND_CODE).addAll(roomClassifications)
//                .build();
//    }

//    public static List<Space> getAllActiveSubRoomsForEducation(Space space) {
//        List<Space> result = new ArrayList<Space>();
//        final Set<Space> containedSpaces = space.getChildren();
//        for (Space subSpace : containedSpaces) {
//            if (isRoom(subSpace) && subSpace.isActive()) {
//                result.add(subSpace);
//            }
//        }
//        for (Space subSpace : containedSpaces) {
//            Space subSpaceSpace = subSpace;
//            result.addAll(getAllActiveSubRoomsForEducation(subSpaceSpace));
//        }
//        return result;
//    }

//    public static Integer countAllAvailableSeatsForExams() {
//        return allocatableSpaces().mapToInt(space -> space.<Integer> getMetadata("examCapacity").orElse(0)).sum();
//    }

//    public final static Comparator<Space> COMPARATOR_BY_PRESENTATION_NAME = new Comparator<Space>() {
//        @Override
//        public int compare(Space o1, Space o2) {
//            if (isFloor(o1) && isFloor(o2)) {
//                Integer level = o1.<Integer> getMetadata("level").orElse(-1);
//                Integer otherLevel = o2.<Integer> getMetadata("level").orElse(-1);
//                int compareTo1 = level.compareTo(otherLevel);
//                if (compareTo1 == 0) {
//                    return o1.getExternalId().compareTo(o2.getExternalId());
//                }
//                return compareTo1;
//            }
//
//            int compareTo = o1.getName().compareTo(o2.getName());
//            if (compareTo == 0) {
//                return o1.getExternalId().compareTo(o2.getExternalId());
//            }
//            return compareTo;
//        }
//    };
//    public final static Comparator<Space> COMPARATOR_BY_NAME_FLOOR_BUILDING_AND_CAMPUS = new Comparator<Space>() {
//        @Override
//        public int compare(Space o1, Space o2) {
//
//            Integer buildingCheck;
//            try {
//                buildingCheck = checkObjects(getSpaceBuilding(o1), getSpaceBuilding(o2));
//                if (buildingCheck != null) {
//                    return buildingCheck.intValue();
//                }
//
//                Integer campusCheck = checkObjects(getSpaceCampus(o1), getSpaceCampus(o2));
//                if (campusCheck != null) {
//                    return campusCheck.intValue();
//                }
//
//                Integer floorCheck = checkObjects(getSpaceFloorWithIntermediary(o1), getSpaceFloorWithIntermediary(o2));
//                if (floorCheck != null) {
//                    return floorCheck.intValue();
//                }
//
//                int compareTo = o1.getName().compareTo(o2.getName());
//                if (compareTo == 0) {
//                    return o1.getExternalId().compareTo(o2.getExternalId());
//                }
//                return compareTo;
//            } catch (UnavailableException e1) {
//                return -1;
//            }
//        }
//
//        private Integer checkObjects(Space space1, Space space2) throws UnavailableException {
//
//            if (space1 != null && space2 == null) {
//                return Integer.valueOf(1);
//
//            } else if (space1 == null && space2 != null) {
//                return Integer.valueOf(-1);
//
//            } else if (space1 == null && space2 == null) {
//                return null;
//
//            } else if (!space1.equals(space2)) {
//                if (isFloor(space1) && isFloor(space2)) {
//                    Integer level = space1.<Integer> getMetadata("level").orElse(-1);
//                    Integer otherLevel = space2.<Integer> getMetadata("level").orElse(-1);
//                    int compareTo1 = level.compareTo(otherLevel);
//                    if (compareTo1 == 0) {
//                        return space1.getExternalId().compareTo(space2.getExternalId());
//                    }
//                    return compareTo1;
//                }
//                int compareTo = space1.getName().compareTo(space2.getName());
//                if (compareTo == 0) {
//                    return space1.getExternalId().compareTo(space2.getExternalId());
//                }
//                return compareTo;
//            }
//
//            return null;
//        }
//    };

//    public static boolean personIsSpacesAdministrator(Person person) {
//        return (RoleType.SPACE_MANAGER_SUPER_USER.isMember(person.getUser()))
//                && RoleType.SPACE_MANAGER.isMember(person.getUser());
//    }

    public static Space getSpaceBuilding(Space space) {
        if (isBuilding(space)) {
            return space;
        }
        return space.getParent() == null || !space.getParent().isActive() ? null : getSpaceBuilding(space.getParent());
    }

//    public static Space getSpaceFloor(Space space) {
//        if (isFloor(space)) {
//            if (space.getParent() == null) {
//                return space;
//            } else if (isFloor(space.getParent())) {
//                return getSpaceFloor(space.getParent());
//            } else {
//                return space;
//            }
//        }
//        if (space.getParent() == null) {
//            return null;
//        }
//        return getSpaceFloor(space.getParent());
//    }

//    public static Space getSpaceFloorWithIntermediary(Space space) throws UnavailableException {
//        if (isFloor(space)) {
//            return space;
//        }
//        return space.getParent() == null ? null : getSpaceFloorWithIntermediary(space.getParent());
//    }

    public static Space getSpaceCampus(Space space) {
        if (isCampus(space)) {
            return space;
        }
        return space.getParent() == null ? null : getSpaceCampus(space.getParent());
    }

//    public static Collection<Occupation> getResourceAllocationsForCheck(Space space) {
//        List<Space> roomSubdivisions = getRoomSubdivisions(space);
//        if (roomSubdivisions.isEmpty()) {
//            return space.getOccupationSet();
//        } else {
//            List<Occupation> result = new ArrayList<Occupation>();
//            result.addAll(space.getOccupationSet());
//            for (Space roomSubdivision : getRoomSubdivisions(space)) {
//                result.addAll(roomSubdivision.getOccupationSet());
//            }
//            return result;
//        }
//    }

//    public static List<Space> getRoomSubdivisions(Space space) {
//        List<Space> result = new ArrayList<Space>();
//        for (Space subSpaceSpace : space.getChildren()) {
//            Space subSpace = subSpaceSpace;
//            if (isRoomSubdivision(subSpace)) {
//                result.add(subSpace);
//            }
//        }
//        return result;
//    }

//    public static String[] getIdentificationWords(String name) {
//        String[] identificationWords = null;
//        if (name != null && !Strings.isNullOrEmpty(name.trim())) {
//            identificationWords = StringNormalizer.normalize(name).trim().split(" ");
//        }
//        return identificationWords;
//    }

//    public static List<Space> getSpaceFullPath(Space space) {
//        List<Space> result = new ArrayList<Space>();
//        result.add(space);
//        Space suroundingSpace = space.getParent();
//        while (suroundingSpace != null) {
//            result.add(0, suroundingSpace);
//            suroundingSpace = suroundingSpace.getParent();
//        }
//        return result;
//    }

}