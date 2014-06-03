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
package net.sourceforge.fenixedu.domain.space;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.UnavailableException;

import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.spaceManager.FindSpacesBean.SpacesSearchCriteriaType;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.FrequencyType;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.HourMinuteSecond;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.commons.StringNormalizer;
import org.fenixedu.spaces.domain.Space;
import org.fenixedu.spaces.domain.SpaceClassification;
import org.fenixedu.spaces.domain.occupation.Occupation;
import org.joda.time.Interval;
import org.joda.time.YearMonthDay;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Ordering;

public class SpaceUtils {
    public static final String CAMPUS = "Campus";
    public static final String BUILDING = "Building";
    public static final String FLOOR = "Floor";
    public static final String ROOM_SUBDIVISION = "Room Subdivision";

    public static final String LABORATORY_FOR_EDUCATION_CODE = "2.1", LABORATORY_FOR_RESEARCHER_CODE = "2.2";

    public final static Comparator<Space> ROOM_COMPARATOR_BY_NAME = new ComparatorChain();
    static {
        ((ComparatorChain) ROOM_COMPARATOR_BY_NAME).addComparator(new BeanComparator("name", Collator.getInstance()));
        ((ComparatorChain) ROOM_COMPARATOR_BY_NAME).addComparator(new BeanComparator("externalId"));
    }

    public static final Comparator<SpaceClassification> COMPARATORY_BY_PARENT_ROOM_CLASSIFICATION_AND_CODE = new BeanComparator(
            "absoluteCode");

    public static Space findAllocatableSpaceForEducationByName(String name) {
        return allocatableSpacesForEducation().filter(space -> space.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public static Stream<Space> allocatableSpaces() {
        return Space.getSpaces().filter(space -> isRoom(space) || isRoomSubdivision(space));
    }

    public static Stream<Space> allocatableSpacesForEducation() {
        return allocatableSpaces().filter(space -> isForEducation(space));
    }

    public static List<InfoRoom> allocatableSpace(YearMonthDay startDate, YearMonthDay endDate, HourMinuteSecond startTimeHMS,
            HourMinuteSecond endTimeHMS, DiaSemana dayOfWeek, Integer normalCapacity, FrequencyType frequency, boolean withLabs) {
        return allocatableSpace(normalCapacity, withLabs)
                .filter(space -> isFree(space, startDate, endDate, startTimeHMS, endTimeHMS, dayOfWeek, frequency, true, true))
                .map(space -> InfoRoom.newInfoFromDomain(space)).collect(Collectors.toList());
    }

    public static List<Space> allocatableSpace(final Integer normalCapacity, final boolean withLabs, final Interval... intervals) {
        return allocatableSpace(normalCapacity, withLabs).filter(space -> space.isFree(intervals)).collect(Collectors.toList());
    }

    public static Stream<Space> allocatableSpace(final Integer normalCapacity, final boolean withLabs) {
        Stream<Space> stream = allocatableSpacesForEducation();
        if (normalCapacity != null) {
            stream = stream.filter(space -> space.getAllocatableCapacity() >= normalCapacity);
        }
        if (!withLabs) {
            stream = stream.filter(space -> !isLaboratory(space));
        }
        return stream;
    }

    public static List<Space> buildings() {
        return Space.getSpaces().filter(space -> isBuilding(space)).collect(Collectors.toList());
    }

    public static boolean isCampus(Space space) {
        return SpaceClassification.getByName(CAMPUS).equals(space.getClassification());
    }

    public static boolean isBuilding(Space space) {
        return SpaceClassification.getByName(BUILDING).equals(space.getClassification());
    }

    public static boolean isFloor(Space space) {
        return SpaceClassification.getByName(FLOOR).equals(space.getClassification());
    }

    public static boolean isRoomSubdivision(Space space) {
        return SpaceClassification.getByName(ROOM_SUBDIVISION).equals(space.getClassification());
    }

    public static boolean isRoom(Space space) {
        return !(isCampus(space) || isBuilding(space) || isFloor(space) || isRoomSubdivision(space));
    }

    public static boolean isLaboratory(Space space) {
        SpaceClassification classification = space.getClassification();
        return classification.equals(SpaceClassification.get(LABORATORY_FOR_EDUCATION_CODE))
                || classification.equals(SpaceClassification.get(LABORATORY_FOR_RESEARCHER_CODE));
    }

    public static boolean isForEducation(Space space) {
        return isForEducation(space, null);
    }

    private static boolean isForEducation(Space space, Person person) {
        final Group lessonGroup = space.getOccupationsGroup();

        if (lessonGroup != null && lessonGroup.getMembers().size() > 0
                && (person == null || lessonGroup.isMember(person.getUser()))) {
            return true;
        }

        final Space parent = space.getParent();
        if (parent != null && parent.isActive() && (isRoom(parent) || isRoomSubdivision(parent))) {
            return isForEducation(parent, person);
        }

        return false;

    }

    public static boolean isFree(Space space, YearMonthDay startDate, YearMonthDay endDate, HourMinuteSecond startTime,
            HourMinuteSecond endTime, DiaSemana dayOfWeek, FrequencyType frequency, Boolean dailyFrequencyMarkSaturday,
            Boolean dailyFrequencyMarkSunday) {
        return isFree(space, startDate, endDate, startTime, endTime, dayOfWeek, frequency, dailyFrequencyMarkSaturday,
                dailyFrequencyMarkSunday, null);
    }

    public static boolean isFree(Space space, YearMonthDay startDate, YearMonthDay endDate, HourMinuteSecond startTime,
            HourMinuteSecond endTime, DiaSemana dayOfWeek, FrequencyType frequency, Boolean dailyFrequencyMarkSaturday,
            Boolean dailyFrequencyMarkSunday, Set<Class<? extends EventSpaceOccupation>> eventSpaceOccupationClassesToSkip) {
        List<Interval> intervals = null;

        if (eventSpaceOccupationClassesToSkip == null) {
            eventSpaceOccupationClassesToSkip = Collections.emptySet();
        }

        for (Occupation spaceOccupation : getResourceAllocationsForCheck(space)) {
            if (spaceOccupation instanceof EventSpaceOccupation) {
                if (eventSpaceOccupationClassesToSkip.contains(spaceOccupation.getClass())) {
                    continue;
                }
                EventSpaceOccupation occupation = (EventSpaceOccupation) spaceOccupation;
                if (occupation.alreadyWasOccupiedIn(startDate, endDate, startTime, endTime, dayOfWeek, frequency,
                        dailyFrequencyMarkSaturday, dailyFrequencyMarkSunday)) {
                    return false;
                }
            }

            if (spaceOccupation.getClass().equals(Occupation.class)) {
                if (intervals == null) {
                    intervals =
                            EventSpaceOccupation.generateEventSpaceOccupationIntervals(startDate, endDate, startTime, endTime,
                                    frequency, dayOfWeek, dailyFrequencyMarkSaturday, dailyFrequencyMarkSunday, null, null);
                }
                if (spaceOccupation.overlaps(intervals)) {
                    return false;
                }
            }
        }

        return true;
    }

    public static Space getSpaceByName(String name) {
        return Space.getSpaces().filter(space -> name.equals(space.getName())).findFirst().get();
    }

    public static Occupation getFirstOccurrenceOfResourceAllocationByClass(Space space, Class<? extends Occupation> clazz) {
        if (clazz != null) {
            for (Occupation resourceAllocation : space.getOccupationSet()) {
                if (resourceAllocation.getClass().equals(clazz)) {
                    return resourceAllocation;
                }
            }
        }
        return null;
    }

    public static Space getDefaultCampus() {
        if (Bennu.getInstance().getDefaultCampus() == null) {
            return Ordering.from(COMPARATOR_BY_PRESENTATION_NAME).min(Space.getAllCampus());
        }
        return Bennu.getInstance().getDefaultCampus();
    }

    public static SortedSet<SpaceClassification> sortByRoomClassificationAndCode(
            final Collection<SpaceClassification> roomClassifications) {
        return ImmutableSortedSet.orderedBy(COMPARATORY_BY_PARENT_ROOM_CLASSIFICATION_AND_CODE).addAll(roomClassifications)
                .build();
    }

    public static List<Space> getAllActiveSubRoomsForEducation(Space space) {
        List<Space> result = new ArrayList<Space>();
        final Set<Space> containedSpaces = space.getChildren();
        for (Space subSpace : containedSpaces) {
            if ((isRoom(subSpace) || isRoomSubdivision(subSpace)) && subSpace.isActive() && isForEducation(subSpace)) {
                result.add(subSpace);
            }
        }
        for (Space subSpace : containedSpaces) {
            Space subSpaceSpace = subSpace;
            result.addAll(getAllActiveSubRoomsForEducation(subSpaceSpace));
        }
        return result;
    }

    public static Integer countAllAvailableSeatsForExams() {
        return allocatableSpaces().mapToInt(space -> space.<Integer> getMetadata("examCapacity").orElse(0)).sum();
    }

    public final static Comparator<Space> COMPARATOR_BY_PRESENTATION_NAME = new Comparator<Space>() {
        @Override
        public int compare(Space o1, Space o2) {
            if (isFloor(o1) && isFloor(o2)) {
                Integer level = o1.<Integer> getMetadata("level").orElse(-1);
                Integer otherLevel = o2.<Integer> getMetadata("level").orElse(-1);
                int compareTo1 = level.compareTo(otherLevel);
                if (compareTo1 == 0) {
                    return o1.getExternalId().compareTo(o2.getExternalId());
                }
                return compareTo1;
            }

            int compareTo = o1.getName().compareTo(o2.getName());
            if (compareTo == 0) {
                return o1.getExternalId().compareTo(o2.getExternalId());
            }
            return compareTo;
        }
    };
    public final static Comparator<Space> COMPARATOR_BY_NAME_FLOOR_BUILDING_AND_CAMPUS = new Comparator<Space>() {
        @Override
        public int compare(Space o1, Space o2) {

            Integer buildingCheck;
            try {
                buildingCheck = checkObjects(getSpaceBuilding(o1), getSpaceBuilding(o2));
                if (buildingCheck != null) {
                    return buildingCheck.intValue();
                }

                Integer campusCheck = checkObjects(getSpaceCampus(o1), getSpaceCampus(o2));
                if (campusCheck != null) {
                    return campusCheck.intValue();
                }

                Integer floorCheck = checkObjects(getSpaceFloorWithIntermediary(o1), getSpaceFloorWithIntermediary(o2));
                if (floorCheck != null) {
                    return floorCheck.intValue();
                }

                int compareTo = o1.getName().compareTo(o2.getName());
                if (compareTo == 0) {
                    return o1.getExternalId().compareTo(o2.getExternalId());
                }
                return compareTo;
            } catch (UnavailableException e1) {
                return -1;
            }
        }

        private Integer checkObjects(Space space1, Space space2) throws UnavailableException {

            if (space1 != null && space2 == null) {
                return Integer.valueOf(1);

            } else if (space1 == null && space2 != null) {
                return Integer.valueOf(-1);

            } else if (space1 == null && space2 == null) {
                return null;

            } else if (!space1.equals(space2)) {
                if (isFloor(space1) && isFloor(space2)) {
                    Integer level = space1.<Integer> getMetadata("level").orElse(-1);
                    Integer otherLevel = space2.<Integer> getMetadata("level").orElse(-1);
                    int compareTo1 = level.compareTo(otherLevel);
                    if (compareTo1 == 0) {
                        return space1.getExternalId().compareTo(space2.getExternalId());
                    }
                    return compareTo1;
                }
                int compareTo = space1.getName().compareTo(space2.getName());
                if (compareTo == 0) {
                    return space1.getExternalId().compareTo(space2.getExternalId());
                }
                return compareTo;
            }

            return null;
        }
    };

    public static boolean personIsSpacesAdministrator(Person person) {
        return (person.hasRole(RoleType.MANAGER) || person.hasRole(RoleType.SPACE_MANAGER_SUPER_USER))
                && person.hasRole(RoleType.SPACE_MANAGER);
    }

    public static Space getSpaceBuilding(Space space) {
        if (isBuilding(space)) {
            return space;
        }
        if (space.getParent() == null || !space.getParent().isActive()) {
            return null;
        }
        return getSpaceBuilding(space.getParent());
    }

    public static Space getSpaceFloor(Space space) {
        if (isFloor(space)) {
            if (space.getParent() == null) {
                return space;
            } else if (isFloor(space.getParent())) {
                return getSpaceFloor(space.getParent());
            } else {
                return space;
            }
        }
        if (space.getParent() == null) {
            return null;
        }
        return getSpaceFloor(space.getParent());
    }

    public static Space getSpaceFloorWithIntermediary(Space space) throws UnavailableException {
        if (isFloor(space)) {
            return space;
        }
        if (space.getParent() == null) {
            return null;
        }
        return getSpaceFloorWithIntermediary(space.getParent());
    }

    public static Space getSpaceCampus(Space space) {
        if (isCampus(space)) {
            return space;
        }
        if (space.getParent() == null) {
            return null;
        }
        return getSpaceCampus(space.getParent());
    }

    public static Collection<Occupation> getResourceAllocationsForCheck(Space space) {
        List<Space> roomSubdivisions = getRoomSubdivisions(space);
        if (roomSubdivisions.isEmpty()) {
            return space.getOccupationSet();
        } else {
            List<Occupation> result = new ArrayList<Occupation>();
            result.addAll(space.getOccupationSet());
            for (Space roomSubdivision : getRoomSubdivisions(space)) {
                result.addAll(roomSubdivision.getOccupationSet());
            }
            return result;
        }
    }

    public static List<Space> getRoomSubdivisions(Space space) {
        List<Space> result = new ArrayList<Space>();
        for (Space subSpaceSpace : space.getChildren()) {
            Space subSpace = subSpaceSpace;
            if (isRoomSubdivision(subSpace)) {
                result.add(subSpace);
            }
        }
        return result;
    }

    @Deprecated
    public static List<Lesson> getAssociatedLessons(final Space space, final ExecutionSemester executionSemester) {
        final List<Lesson> lessons = new ArrayList<Lesson>();
        for (Occupation spaceOccupation : space.getOccupationSet()) {
            if (spaceOccupation instanceof LessonSpaceOccupation) {
                LessonSpaceOccupation roomOccupation = (LessonSpaceOccupation) spaceOccupation;
                final Lesson lesson = roomOccupation.getLesson();
                if (lesson.getExecutionPeriod() == executionSemester) {
                    lessons.add(lesson);
                }
            }
        }
        return lessons;
    }

    public static List<Lesson> getAssociatedLessons(Space space, AcademicInterval academicInterval) {
        final List<Lesson> lessons = new ArrayList<Lesson>();
        for (Occupation spaceOccupation : space.getOccupationSet()) {
            if (spaceOccupation instanceof LessonSpaceOccupation) {
                LessonSpaceOccupation roomOccupation = (LessonSpaceOccupation) spaceOccupation;
                final Lesson lesson = roomOccupation.getLesson();
                if (lesson.getAcademicInterval().equals(academicInterval)) {
                    lessons.add(lesson);
                }
            }
        }
        return lessons;
    }

    public static int currentAttendaceCount(Space space) {
        return space.getCurrentAttendanceSet().size()
                + space.getChildren().stream().mapToInt(SpaceUtils::currentAttendaceCount).sum();
    }

    public static String[] getIdentificationWords(String name) {
        String[] identificationWords = null;
        if (name != null && !Strings.isNullOrEmpty(name.trim())) {
            identificationWords = StringNormalizer.normalize(name).trim().split(" ");
        }
        return identificationWords;
    }

    private static Set<ExecutionCourse> searchExecutionCoursesByName(SpacesSearchCriteriaType searchType, String[] labelWords) {
        Set<ExecutionCourse> executionCoursesToTest = null;
        if (labelWords != null
                && (searchType.equals(SpacesSearchCriteriaType.EXECUTION_COURSE) || searchType
                        .equals(SpacesSearchCriteriaType.WRITTEN_EVALUATION))) {
            executionCoursesToTest = new HashSet<ExecutionCourse>();
            for (ExecutionCourse executionCourse : ExecutionSemester.readActualExecutionSemester()
                    .getAssociatedExecutionCoursesSet()) {
                if (executionCourse.verifyNameEquality(labelWords)) {
                    executionCoursesToTest.add(executionCourse);
                }
            }
        }
        return executionCoursesToTest;
    }

//    private static Collection<Person> searchPersonsByName(SpacesSearchCriteriaType searchType, String labelToSearch) {
//        if (labelToSearch != null && !Strings.isNullOrEmpty(labelToSearch) && searchType.equals(SpacesSearchCriteriaType.PERSON)) {
//            return Person.findPerson(labelToSearch);
//        }
//        return Collections.EMPTY_LIST;
//    }

    private static boolean verifyNameEquality(Space space, String[] nameWords) {
        if (nameWords != null) {
            String spacePresentationName = space.getPresentationName();
            if (spacePresentationName != null) {
                String[] spaceIdentificationWords = StringNormalizer.normalize(spacePresentationName).trim().split(" ");
                int j, i;
                for (i = 0; i < nameWords.length; i++) {
                    if (!nameWords[i].equals("")) {
                        for (j = 0; j < spaceIdentificationWords.length; j++) {
                            if (spaceIdentificationWords[j].equals(nameWords[i])) {
                                break;
                            }
                        }
                        if (j == spaceIdentificationWords.length) {
                            return false;
                        }
                    }
                }
                if (i == nameWords.length) {
                    return true;
                }
            }
        }
        return false;
    }

    public static List<Space> getSpaceFullPath(Space space) {
        List<Space> result = new ArrayList<Space>();
        result.add(space);
        Space suroundingSpace = space.getParent();
        while (suroundingSpace != null) {
            result.add(0, suroundingSpace);
            suroundingSpace = suroundingSpace.getParent();
        }
        return result;
    }

    public static Set<Space> findSpaces(String labelToSearch, Space campus, Space building, SpacesSearchCriteriaType searchType) {

        Set<Space> result = new TreeSet<Space>(COMPARATOR_BY_NAME_FLOOR_BUILDING_AND_CAMPUS);

        if (searchType != null
                && (campus != null || building != null || (labelToSearch != null && !Strings.isNullOrEmpty(labelToSearch.trim())))) {

            String[] labelWords = getIdentificationWords(labelToSearch);
            Set<ExecutionCourse> executionCoursesToTest = searchExecutionCoursesByName(searchType, labelWords);
            //Collection<Person> personsToTest = searchPersonsByName(searchType, labelToSearch);

            for (Space space : Space.getSpaces().collect(Collectors.toList())) {

                if (!space.equals(campus) && !space.equals(building)) {

                    if (labelWords != null) {

                        boolean toAdd = false;

                        switch (searchType) {

                        case SPACE:
                            toAdd = verifyNameEquality(space, labelWords);
                            break;

//                        case PERSON:
//                            for (Person person : personsToTest) {
//                                if (person.getActivePersonSpaces().contains(resource)) {
//                                    toAdd = true;
//                                    break;
//                                }
//                            }
//                            break;

                        case EXECUTION_COURSE:
                            for (ExecutionCourse executionCourse : executionCoursesToTest) {
                                if (executionCourse.getAllRooms().contains(space)) {
                                    toAdd = true;
                                    break;
                                }
                            }
                            break;

                        case WRITTEN_EVALUATION:
                            for (ExecutionCourse executionCourse : executionCoursesToTest) {
                                SortedSet<WrittenEvaluation> writtenEvaluations = executionCourse.getWrittenEvaluations();
                                for (WrittenEvaluation writtenEvaluation : writtenEvaluations) {
                                    if (writtenEvaluation.getAssociatedRooms().contains(space)) {
                                        toAdd = true;
                                        break;
                                    }
                                }
                                if (toAdd) {
                                    break;
                                }
                            }
                            break;

                        default:
                            break;
                        }

                        if (!toAdd) {
                            continue;
                        }
                    }

                    if (building != null) {
                        Space spaceBuilding = getSpaceBuilding(space);
                        if (spaceBuilding == null || !spaceBuilding.equals(building)) {
                            continue;
                        }
                    } else if (campus != null) {
                        Space spaceCampus = getSpaceCampus(space);
                        if (spaceCampus == null || !spaceCampus.equals(campus)) {
                            continue;
                        }
                    }

                    result.add(space);
                }
            }
        }
        return result;
    }
}