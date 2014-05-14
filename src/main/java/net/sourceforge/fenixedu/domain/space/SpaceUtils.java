package net.sourceforge.fenixedu.domain.space;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.FrequencyType;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.HourMinuteSecond;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.spaces.domain.Space;
import org.fenixedu.spaces.domain.SpaceClassification;
import org.fenixedu.spaces.domain.UnavailableException;
import org.fenixedu.spaces.domain.occupation.Occupation;
import org.joda.time.Interval;
import org.joda.time.YearMonthDay;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

public class SpaceUtils {

    public final static Comparator<Space> ROOM_COMPARATOR_BY_NAME = new ComparatorChain();
    static {
        ((ComparatorChain) ROOM_COMPARATOR_BY_NAME).addComparator(new BeanComparator("name", Collator.getInstance()));
        ((ComparatorChain) ROOM_COMPARATOR_BY_NAME).addComparator(new BeanComparator("externalId"));
    }

    public static final Comparator<SpaceClassification> COMPARATORY_BY_PARENT_ROOM_CLASSIFICATION_AND_CODE = new BeanComparator(
            "absoluteCode");

    public static Space findAllocatableSpaceForEducationByName(String name) {
        for (Space space : SpaceUtils.getActiveSpaces()) {
            try {
                if ((SpaceUtils.isRoom(space) || SpaceClassification.getByName("Room Subdivision").equals(
                        space.getClassification()))
                        && space.getName().equalsIgnoreCase(name)) {
                    return space;
                }
            } catch (UnavailableException e) {
            }
        }
        return null;
    }

    private static boolean isForEducation(Space space) {
        return isForEducation(space, null);
    }

    private static boolean isForEducation(Space space, Person person) {
        final Group lessonGroup = space.getOccupationsAccessGroup() != null ? space.getOccupationsAccessGroup().toGroup() : null;

        final boolean isForEducation = groupHasElements(lessonGroup, person);

        if (isForEducation) {
            return true;
        }

        final Space suroundingSpace = space.getParent();
        try {
            if (SpaceUtils.isRoom(suroundingSpace)
                    || SpaceClassification.getByName("Room Subdivision").equals(suroundingSpace.getClassification())) {
                return isForEducation(suroundingSpace, person);
            }
        } catch (UnavailableException e) {
        }

        return false;

    }

    private static boolean groupHasElements(final Group group) {
        return groupHasElements(group, null);
    }

    private static boolean groupHasElements(final Group group, Person person) {
        return group != null && group.getMembers().size() > 0 && (person == null || group.isMember(person.getUser()));
    }

    public static Space findActiveAllocatableSpaceForEducationByName(String name) {
        Space allocatableSpace = findAllocatableSpaceForEducationByName(name);
        if (allocatableSpace != null && allocatableSpace.isActive()) {
            return allocatableSpace;
        }
        return null;
    }

    public static List<Space> getAllActiveAllocatableSpacesExceptLaboratoriesForEducation() {
        return findAllocatableSpacesByPredicates(ACTIVE_FOR_EDUCATION_EXCEPT_LABS_PREDICATE);
    }

    public static List<Space> getAllActiveAllocatableSpacesForEducation() {
        return findAllocatableSpacesByPredicates(ACTIVE_FOR_EDUCATION_PREDICATE);
    }

    public static List<Space> getAllActiveAllocatableSpacesForEducationAndPunctualOccupations() {
        return getAllActiveAllocatableSpacesForEducationAndPunctualOccupations(null);
    }

    public static List<Space> getAllActiveAllocatableSpacesForEducationAndPunctualOccupations(Person person) {
        List<Space> result = new ArrayList<Space>();
        for (Space space : SpaceUtils.getActiveSpaces()) {
            try {
                if ((SpaceUtils.isRoom(space) || SpaceClassification.getByName("Room Subdivision").equals(
                        space.getClassification()))
                        && space.isActive() && isForEducation(space, person)) {
                    result.add((Space) space);
                }
            } catch (UnavailableException e) {
            }
        }
        return result;
    }

    public static interface AllocatableSpacePredicate {

        public boolean eval(final Space space);

    }

    public static interface AllocatableSpaceTransformer<T> {

        public T transform(final Space space);

    }

    public static final AllocatableSpaceTransformer<Space> NO_TRANSFORMER = new AllocatableSpaceTransformer<Space>() {
        @Override
        public Space transform(final Space space) {
            return space;
        }
    };

    public static <T> List<T> findAllocatableSpacesByPredicates(final AllocatableSpaceTransformer<T> transformer,
            final AllocatableSpacePredicate... predicates) {
        final List<T> result = new ArrayList<T>();
        for (final Space resource : SpaceUtils.getActiveSpaces()) {
            try {
                if (SpaceUtils.isRoom(resource)
                        || SpaceClassification.getByName("Room Subdivision").equals(resource.getClassification())) {
                    final Space allocatableSpace = (Space) resource;
                    if (matchesAllPredicates(allocatableSpace, predicates)) {
                        result.add(transformer.transform(allocatableSpace));
                    }
                }
            } catch (UnavailableException e) {
            }
        }
        return result;
    }

    public static List<Space> findAllocatableSpacesByPredicates(final AllocatableSpacePredicate... predicates) {
        return findAllocatableSpacesByPredicates(NO_TRANSFORMER, predicates);
    }

    private static boolean matchesAllPredicates(final Space space, final AllocatableSpacePredicate... predicates) {
        for (final AllocatableSpacePredicate predicate : predicates) {
            if (!predicate.eval(space)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isRoom(Space space) {
        final SpaceClassification roomSubdiv = SpaceClassification.getByName("Room Subdivision");
        final SpaceClassification floor = SpaceClassification.getByName("Floor");
        final SpaceClassification campus = SpaceClassification.getByName("Campus");
        final SpaceClassification building = SpaceClassification.getByName("Building");
        SpaceClassification classification;
        try {
            classification = space.getClassification();
            return !(classification.equals(floor) || classification.equals(campus) || classification.equals(building) || classification
                    .equals(roomSubdiv));
        } catch (UnavailableException e) {
            return false;
        }
    }

    public static class ActiveForEducationPredicate implements AllocatableSpacePredicate {

        @Override
        public boolean eval(final Space space) {
            return space.isActive() && isForEducation(space, null);
        }

    }

    public static final ActiveForEducationPredicate ACTIVE_FOR_EDUCATION_PREDICATE = new ActiveForEducationPredicate();

    public static class ActiveForEducationWithNormalCapacityPredicate extends ActiveForEducationPredicate {

        private final Integer normalCapacity;

        public ActiveForEducationWithNormalCapacityPredicate(final Integer normalCapacity) {
            this.normalCapacity = normalCapacity;
        }

        @Override
        public boolean eval(final Space space) {
            return normalCapacity != null && space.getAllocatableCapacity() != null
                    && space.getAllocatableCapacity().intValue() >= normalCapacity.intValue() && super.eval(space);
        }

    }

    public static class ActiveForEducationForRoomTypePredicate extends ActiveForEducationPredicate {

        private final SpaceClassification roomType;

        public ActiveForEducationForRoomTypePredicate(final SpaceClassification roomType) {
            this.roomType = roomType;
        }

        @Override
        public boolean eval(final Space space) {
            try {
                return roomType != null && roomType == space.getClassification() && super.eval(space);
            } catch (UnavailableException e) {
                return false;
            }
        }
    }

    public static class ActiveForEducationExceptLabsPredicate extends ActiveForEducationPredicate {

        @Override
        public boolean eval(final Space space) {
            return super.eval(space) && isNotALab(space);

        }

        public static final String LABORATORY_FOR_EDUCATION_CODE = "2.1", LABORATORY_FOR_RESEARCHER_CODE = "2.2";

        private boolean isNotALab(final Space space) {
            SpaceClassification roomClassification;
            try {
                roomClassification = space.getClassification();
                return roomClassification == null
                        || (!roomClassification.equals(SpaceClassification.get(LABORATORY_FOR_EDUCATION_CODE)) && !roomClassification
                                .equals(SpaceClassification.get(LABORATORY_FOR_RESEARCHER_CODE)));
            } catch (UnavailableException e) {
                return false;
            }

        }
    }

    public static final ActiveForEducationExceptLabsPredicate ACTIVE_FOR_EDUCATION_EXCEPT_LABS_PREDICATE =
            new ActiveForEducationExceptLabsPredicate();

    public static class IsFreePredicate implements AllocatableSpacePredicate {

        private final YearMonthDay startDate;
        private final YearMonthDay endDate;
        private final HourMinuteSecond startTime;
        private final HourMinuteSecond endTime;
        private final DiaSemana dayOfWeek;
        private final FrequencyType frequency;
        private final Boolean dailyFrequencyMarkSaturday;
        private final Boolean dailyFrequencyMarkSunday;

        public IsFreePredicate(YearMonthDay startDate, YearMonthDay endDate, HourMinuteSecond startTime,
                HourMinuteSecond endTime, DiaSemana dayOfWeek, FrequencyType frequency, Boolean dailyFrequencyMarkSaturday,
                Boolean dailyFrequencyMarkSunday) {
            this.startDate = startDate;
            this.endDate = endDate;
            this.startTime = startTime;
            this.endTime = endTime;
            this.dayOfWeek = dayOfWeek;
            this.frequency = frequency;
            this.dailyFrequencyMarkSaturday = dailyFrequencyMarkSaturday;
            this.dailyFrequencyMarkSunday = dailyFrequencyMarkSunday;
        }

        @Override
        public boolean eval(final Space space) {
            try {
                return isFree(space, startDate, endDate, startTime, endTime, dayOfWeek, frequency, dailyFrequencyMarkSaturday,
                        dailyFrequencyMarkSunday);
            } catch (UnavailableException e) {
                return false;
            }
        }

    }

    public static class IsFreeIntervalPredicate implements AllocatableSpacePredicate {

        private final Interval[] intervals;

        public IsFreeIntervalPredicate(final Interval[] intervals) {
            this.intervals = intervals;
        }

        @Override
        public boolean eval(final Space space) {
            return space.isFree(Lists.newArrayList(intervals));
        }

    }

    public static List<Space> findActiveAllocatableSpacesForEducationWithNormalCapacity(Integer normalCapacity) {
        return findAllocatableSpacesByPredicates(new ActiveForEducationWithNormalCapacityPredicate(normalCapacity));
    }

    public static List<Space> findActiveAllocatableSpacesForEducationByRoomType(SpaceClassification roomType) {
        return findAllocatableSpacesByPredicates(new ActiveForEducationForRoomTypePredicate(roomType));
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
            return Ordering.from(SpaceUtils.COMPARATOR_BY_PRESENTATION_NAME).min(Space.getAllCampus());
        }
        return Bennu.getInstance().getDefaultCampus();
    }

    public static List<Space> getAllActiveBuildings() {
        return getAllSpacesByClassification("Building", Boolean.TRUE);
    }

    private static List<Space> getAllSpacesByClassification(String classification, Boolean active) {
        SpaceClassification clazz = SpaceClassification.getByName(classification);
        List<Space> result = new ArrayList<Space>();
        for (Space space : SpaceUtils.getActiveSpaces()) {
            try {
                if (space.getClassification().equals(clazz) && (active == null || space.isActive() == active.booleanValue())) {
                    result.add((Space) space);
                }
            } catch (UnavailableException e) {
            }
        }
        return result;
    }

    public static SortedSet<SpaceClassification> sortByRoomClassificationAndCode(
            final Collection<SpaceClassification> roomClassifications) {
        return ImmutableSortedSet.orderedBy(COMPARATORY_BY_PARENT_ROOM_CLASSIFICATION_AND_CODE).addAll(roomClassifications)
                .build();
    }

    public static List<Space> getAllActiveSubRoomsForEducation(Space space) {
        List<Space> result = new ArrayList<Space>();
        final Set<org.fenixedu.spaces.domain.Space> containedSpaces = space.getChildrenSet();
        for (org.fenixedu.spaces.domain.Space subSpace : containedSpaces) {
            Space subSpaceSpace = (Space) subSpace;
            try {
                if ((SpaceUtils.isRoom(subSpaceSpace) || SpaceClassification.getByName("Room Subdivision").equals(
                        subSpaceSpace.getClassification()))
                        && subSpaceSpace.isActive() && isForEducation((Space) space)) {
                    result.add(subSpaceSpace);
                }
            } catch (UnavailableException e) {
            }
        }
        for (org.fenixedu.spaces.domain.Space subSpace : containedSpaces) {
            Space subSpaceSpace = (Space) subSpace;
            result.addAll(getAllActiveSubRoomsForEducation(subSpaceSpace));
        }
        return result;
    }

    public static Set<Space> getAllActiveCampus() {
        return Space.getAllCampus();
    }

    public static Integer countAllAvailableSeatsForExams() {
        int countAllSeatsForExams = 0;
        for (Space space : SpaceUtils.getActiveSpaces()) {
            try {
                if (SpaceUtils.isRoom(space)
                        && SpaceClassification.getByName("Room Subdivision").equals(space.getClassification())) {
                    if (space.isActive()) {
                        final Integer examCapacity = space.getMetadata("examCapacity");
                        if (examCapacity != null) {
                            countAllSeatsForExams += examCapacity;
                        }
                    }
                }
            } catch (UnavailableException e) {
            }
        }
        return countAllSeatsForExams;
    }

    public final static Comparator<Space> COMPARATOR_BY_PRESENTATION_NAME = new Comparator<Space>() {
        @Override
        public int compare(Space o1, Space o2) {

            try {
                if (SpaceClassification.getByName("Floor").equals(o1.getClassification())
                        && SpaceClassification.getByName("Floor").equals(o2.getClassification())) {
                    Integer level;
                    try {
                        level = o1.getMetadata("level");
                        int compareTo1 = level.compareTo((Integer) o2.getMetadata("level"));
                        if (compareTo1 == 0) {
                            return o1.getExternalId().compareTo(o2.getExternalId());
                        }
                        return compareTo1;
                    } catch (UnavailableException e) {
                        return -1;
                    }
                }
            } catch (UnavailableException e1) {
            }

            int compareTo;
            compareTo = o1.getName().compareTo(o2.getName());
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

                int compareTo;
                compareTo = o1.getName().compareTo(o2.getName());
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
                if (SpaceClassification.getByName("Floor").equals(space1.getClassification())
                        && SpaceClassification.getByName("Floor").equals(space2.getClassification())) {
                    Integer level;
                    try {
                        level = space1.getMetadata("level");
                        int compareTo1 = level.compareTo((Integer) space2.getMetadata("level"));
                        if (compareTo1 == 0) {
                            return space1.getExternalId().compareTo(space2.getExternalId());
                        }
                        return compareTo1;
                    } catch (UnavailableException e) {
                        return -1;
                    }
                }
                int compareTo;
                compareTo = space1.getName().compareTo(space2.getName());
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

    public static Space getSpaceBuilding(Space space) throws UnavailableException {
        if (SpaceClassification.getByName("Building").equals(space.getClassification())) {
            return space;
        }
        if (space.getParent() == null) {
            return space;
        }
        return getSpaceBuilding(space.getParent());
    }

    public static Space getSpaceFloor(Space space) throws UnavailableException {
        if (SpaceClassification.getByName("Floor").equals(space.getClassification())) {
            if (space.getParent() == null) {
                return space;
            } else if (SpaceClassification.getByName("Floor").equals(space.getParent().getClassification())) {
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
        if (SpaceClassification.getByName("Floor").equals(space.getClassification())) {
            return space;
        }
        if (space.getParent() == null) {
            return null;
        }
        return getSpaceFloorWithIntermediary(space.getParent());
    }

    public static Space getSpaceCampus(Space space) throws UnavailableException {
        if (SpaceClassification.getByName("Campus").equals(space.getClassification())) {
            return space;
        }
        if (space.getParent() == null) {
            return null;
        }
        return getSpaceCampus(space.getParent());
    }

    public static Collection<Occupation> getResourceAllocationsForCheck(Space space) throws UnavailableException {
        List<Space> roomSubdivisions = SpaceUtils.getRoomSubdivisions(space);
        if (roomSubdivisions.isEmpty()) {
            return space.getOccupationSet();
        } else {
            List<Occupation> result = new ArrayList<Occupation>();
            result.addAll(space.getOccupationSet());
            for (Space roomSubdivision : SpaceUtils.getRoomSubdivisions(space)) {
                result.addAll(roomSubdivision.getOccupationSet());
            }
            return result;
        }
    }

    public static List<Space> getRoomSubdivisions(Space space) throws UnavailableException {
        List<Space> result = new ArrayList<Space>();
        for (org.fenixedu.spaces.domain.Space subSpaceSpace : space.getValidChildrenSet()) {
            Space subSpace = (Space) subSpaceSpace;
            if (SpaceClassification.getByName("Room Subdivision").equals(subSpace.getClassification())) {
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
        int occupants = space.getCurrentAttendanceSet().size();
        for (org.fenixedu.spaces.domain.Space innerSpace : space.getValidChildrenSet()) {
            Space child = (Space) innerSpace;
            occupants += currentAttendaceCount(child);
        }
        return occupants;
    }

    public static Set<Space> getActiveChildrenSet(Space space) {
        return FluentIterable.from(space.getValidChildrenSet())
                .transform(new Function<org.fenixedu.spaces.domain.Space, Space>() {

                    @Override
                    public Space apply(org.fenixedu.spaces.domain.Space input) {
                        return (Space) input;
                    }

                }).toSet();
    }

    public static boolean isFree(Space space, YearMonthDay startDate, YearMonthDay endDate, HourMinuteSecond startTime,
            HourMinuteSecond endTime, DiaSemana dayOfWeek, FrequencyType frequency, Boolean dailyFrequencyMarkSaturday,
            Boolean dailyFrequencyMarkSunday) throws UnavailableException {

        for (Occupation spaceOccupation : getResourceAllocationsForCheck(space)) {
            if (spaceOccupation instanceof EventSpaceOccupation) {
                EventSpaceOccupation occupation = (EventSpaceOccupation) spaceOccupation;
                if (occupation.alreadyWasOccupiedIn(startDate, endDate, startTime, endTime, dayOfWeek, frequency,
                        dailyFrequencyMarkSaturday, dailyFrequencyMarkSunday)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static Set<Space> getActiveSpaces() {
        return FluentIterable.from(Bennu.getInstance().getSpaceSet()).filter(new Predicate<Space>() {

            @Override
            public boolean apply(Space input) {
                return input.isActive();
            }
        }).toSet();
    }

    public static Space getSpaceByName(String name) {
        for (Space space : SpaceUtils.getActiveSpaces()) {
            if (space.getName().equals(name)) {
                return space;
            }
        }
        return null;
    }

}