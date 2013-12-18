package net.sourceforge.fenixedu.domain.space;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.FrequencyType;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.resource.Resource;
import net.sourceforge.fenixedu.domain.resource.ResourceAllocation;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.predicates.SpacePredicates;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.HourMinuteSecond;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.YearMonthDay;

public abstract class AllocatableSpace extends AllocatableSpace_Base {

    public final static Comparator<AllocatableSpace> ROOM_COMPARATOR_BY_NAME = new ComparatorChain();
    static {
        ((ComparatorChain) ROOM_COMPARATOR_BY_NAME).addComparator(new BeanComparator("identification", Collator.getInstance()));
        ((ComparatorChain) ROOM_COMPARATOR_BY_NAME).addComparator(new BeanComparator("externalId"));
    }

    public abstract Collection<ResourceAllocation> getResourceAllocationsForCheck();

    public abstract String getIdentification();

    public abstract RoomClassification getRoomClassification();

    @Override
    public abstract Integer getNormalCapacity();

    @Override
    public abstract Integer getExamCapacity();

    protected abstract void setNormalCapacity(Integer capacidadeNormal);

    protected abstract void setExamCapacity(Integer capacidadeExame);

    @Deprecated
    public abstract Integer getCapacidadeNormal();

    @Deprecated
    public abstract Integer getCapacidadeExame();

    @Deprecated
    public abstract RoomClassification getTipo();

    protected AllocatableSpace() {
        super();
    }

    @Deprecated
    public String getNome() {
        return getIdentification();
    }

    @Deprecated
    public String getName() {
        return getIdentification();
    }

    @Deprecated
    public Building getBuilding() {
        return getSpaceBuilding();
    }

    @Deprecated
    public Integer getPiso() {
        Floor floor = getSpaceFloor();
        return floor != null ? floor.getSpaceInformation().getLevel() : null;
    }

    @Override
    public boolean isAllocatableSpace() {
        return true;
    }

    public boolean containsIdentification() {
        return !StringUtils.isEmpty(getIdentification());
    }

    public void editCapacities(Integer capacidadeNormal, Integer capacidadeExame) {
        check(this, SpacePredicates.checkPermissionsToManageRoomCapacities);
        setNormalCapacity(capacidadeNormal);
        setExamCapacity(capacidadeExame);
    }

    public String getCompleteIdentification() {
        StringBuilder builder = new StringBuilder();
        if (containsIdentification()) {
            Building building = getSpaceBuilding();
            builder.append(getIdentification()).append(" - ");
            builder.append(building != null ? building.getNameWithCampus() : "");
            builder.append(String.format(" [%d,%d]", getNormalCapacity(), getExamCapacity()));
        }
        return builder.toString();
    }

    public static List<AllocatableSpace> readAllAllocatableSpacesByName(String name) {
        List<AllocatableSpace> result = new ArrayList<AllocatableSpace>();
        String[] identificationWords = getIdentificationWords(name);
        for (Resource resource : Bennu.getInstance().getResourcesSet()) {
            if (resource.isAllocatableSpace() && ((Space) resource).verifyNameEquality(identificationWords)) {
                result.add((AllocatableSpace) resource);
            }
        }
        return result;
    }

    public static void mergeAllocatableSpaces(AllocatableSpace fromRoom, AllocatableSpace destinationRoom) {

        if (fromRoom == null || destinationRoom == null || fromRoom.equals(destinationRoom)) {
            throw new DomainException("error.room.invalid.rooms.for.merge");
        }

        Space destinationRoomParent = destinationRoom.getSuroundingSpace();
        while (destinationRoomParent != null) {
            if (destinationRoomParent.equals(fromRoom)) {
                throw new DomainException("error.merge.rooms.from.room.is.parent.of.destination.room");
            }
            destinationRoomParent = destinationRoomParent.getSuroundingSpace();
        }

        destinationRoom.getResourceResponsibility().addAll(fromRoom.getResourceResponsibility());
        destinationRoom.getResourceAllocations().addAll(fromRoom.getResourceAllocations());
        destinationRoom.getContainedSpaces().addAll(fromRoom.getContainedSpaces());
        destinationRoom.getAssociatedInquiriesRooms().addAll(fromRoom.getAssociatedInquiriesRooms());
        destinationRoom.getAssociatedSummaries().addAll(fromRoom.getAssociatedSummaries());
        destinationRoom.getWrittenEvaluationEnrolments().addAll(fromRoom.getWrittenEvaluationEnrolments());
        destinationRoom.setNormalCapacity(fromRoom.getNormalCapacity());
        destinationRoom.setExamCapacity(fromRoom.getExamCapacity());

        fromRoom.delete();
    }

    public boolean isForEducation() {
        return isForEducation(null);
    }

    public boolean isForEducation(Person person) {
        final Group lessonGroup = getLessonOccupationsAccessGroup();
        final Group writtenEvaluationGroup = getWrittenEvaluationOccupationsAccessGroup();

        final boolean isForEducation = groupHasElements(lessonGroup, person) || groupHasElements(writtenEvaluationGroup, person);

        if (isForEducation) {
            return true;
        }

        final Space suroundingSpace = getSuroundingSpace();
        if (suroundingSpace.isAllocatableSpace()) {
            final AllocatableSpace allocatableSpace = (AllocatableSpace) suroundingSpace;
            return allocatableSpace.isForEducation(person);
        }
        return false;
    }

    protected boolean groupHasElements(final Group group) {
        return groupHasElements(group, null);
    }

    protected boolean groupHasElements(final Group group, Person person) {
        return group != null && group.getElementsCount() > 0 && (person == null || group.isMember(person));
    }

    public boolean isForPunctualOccupation() {
        return isForPunctualOccupation(null);
    }

    public boolean isForPunctualOccupation(Person person) {
        Group group = getGenericEventOccupationsAccessGroup();
        return groupHasElements(group, person);
    }

    public static AllocatableSpace findAllocatableSpaceForEducationByName(String name) {
        for (Resource space : Bennu.getInstance().getResourcesSet()) {
            if (space.isAllocatableSpace() && ((AllocatableSpace) space).isForEducation()
                    && ((AllocatableSpace) space).getIdentification().equalsIgnoreCase(name)) {
                return (AllocatableSpace) space;
            }
        }
        return null;
    }

    public static AllocatableSpace findActiveAllocatableSpaceForEducationByName(String name) {
        AllocatableSpace allocatableSpace = findAllocatableSpaceForEducationByName(name);
        if (allocatableSpace != null && allocatableSpace.isActive()) {
            return allocatableSpace;
        }
        return null;
    }

    public static List<Room> getAllRoomsForAlameda() {
        List<Room> result = new ArrayList<Room>();
        for (Resource space : Bennu.getInstance().getResourcesSet()) {
            if (space.isAllocatableSpace() && space.isRoom()) {
                AllocatableSpace allocSpace = ((AllocatableSpace) space);
                if (allocSpace.isActive() && allocSpace.getSpaceBuilding().getSpaceCampus().isCampusAlameda()) {
                    result.add((Room) allocSpace);
                }
            }
        }
        return result;
    }

    public static List<AllocatableSpace> getAllActiveAllocatableSpacesExceptLaboratoriesForEducation() {
        return findAllocatableSpacesByPredicates(ACTIVE_FOR_EDUCATION_EXCEPT_LABS_PREDICATE);
    }

    public static List<AllocatableSpace> getAllActiveAllocatableSpacesForEducation() {
        return findAllocatableSpacesByPredicates(ACTIVE_FOR_EDUCATION_PREDICATE);
    }

    public static List<AllocatableSpace> getAllActiveAllocatableSpacesForEducationAndPunctualOccupations() {
        return getAllActiveAllocatableSpacesForEducationAndPunctualOccupations(null);
    }

    public static List<AllocatableSpace> getAllActiveAllocatableSpacesForEducationAndPunctualOccupations(Person person) {
        List<AllocatableSpace> result = new ArrayList<AllocatableSpace>();
        for (Resource space : Bennu.getInstance().getResourcesSet()) {
            if (space.isAllocatableSpace()
                    && ((AllocatableSpace) space).isActive()
                    && (((AllocatableSpace) space).isForEducation(person) || ((AllocatableSpace) space)
                            .isForPunctualOccupation(person))) {
                result.add((AllocatableSpace) space);
            }
        }
        return result;
    }

    public static interface AllocatableSpacePredicate {

        public boolean eval(final AllocatableSpace space);

    }

    public static interface AllocatableSpaceTransformer<T> {

        public T transform(final AllocatableSpace space);

    }

    public static final AllocatableSpaceTransformer<AllocatableSpace> NO_TRANSFORMER =
            new AllocatableSpaceTransformer<AllocatableSpace>() {
                @Override
                public AllocatableSpace transform(final AllocatableSpace space) {
                    return space;
                }
            };

    public static <T> List<T> findAllocatableSpacesByPredicates(final AllocatableSpaceTransformer<T> transformer,
            final AllocatableSpacePredicate... predicates) {
        final List<T> result = new ArrayList<T>();
        for (final Resource resource : Bennu.getInstance().getResourcesSet()) {
            if (resource.isAllocatableSpace()) {
                final AllocatableSpace allocatableSpace = (AllocatableSpace) resource;
                if (allocatableSpace.matchesAllPredicates(predicates)) {
                    result.add(transformer.transform(allocatableSpace));
                }
            }
        }
        return result;
    }

    public static List<AllocatableSpace> findAllocatableSpacesByPredicates(final AllocatableSpacePredicate... predicates) {
        return findAllocatableSpacesByPredicates(NO_TRANSFORMER, predicates);
    }

    private boolean matchesAllPredicates(final AllocatableSpacePredicate... predicates) {
        for (final AllocatableSpacePredicate predicate : predicates) {
            if (!predicate.eval(this)) {
                return false;
            }
        }
        return true;
    }

    public static class ActiveForEducationPredicate implements AllocatableSpacePredicate {

        @Override
        public boolean eval(final AllocatableSpace space) {
            return space.isActive() && space.isForEducation();
        }

    }

    public static final ActiveForEducationPredicate ACTIVE_FOR_EDUCATION_PREDICATE = new ActiveForEducationPredicate();

    public static class ActiveForEducationWithNormalCapacityPredicate extends ActiveForEducationPredicate {

        private final Integer normalCapacity;

        public ActiveForEducationWithNormalCapacityPredicate(final Integer normalCapacity) {
            this.normalCapacity = normalCapacity;
        }

        @Override
        public boolean eval(final AllocatableSpace space) {
            return normalCapacity != null && space.getNormalCapacity() != null
                    && space.getNormalCapacity().intValue() >= normalCapacity.intValue() && super.eval(space);
        }

    }

    public static class ActiveForEducationForRoomTypePredicate extends ActiveForEducationPredicate {

        private final RoomClassification roomType;

        public ActiveForEducationForRoomTypePredicate(final RoomClassification roomType) {
            this.roomType = roomType;
        }

        @Override
        public boolean eval(final AllocatableSpace space) {
            return roomType != null && roomType == space.getRoomClassification() && super.eval(space);
        }

    }

    public static class ActiveForEducationExceptLabsPredicate extends ActiveForEducationPredicate {

        @Override
        public boolean eval(final AllocatableSpace space) {
            return super.eval(space) && isNotALab(space);

        }

        private boolean isNotALab(final AllocatableSpace space) {
            final RoomClassification roomClassification = space.getRoomClassification();
            return roomClassification == null
                    || (!roomClassification.getPresentationCode().equals(RoomClassification.LABORATORY_FOR_EDUCATION_CODE) && !roomClassification
                            .getPresentationCode().equals(RoomClassification.LABORATORY_FOR_RESEARCHER_CODE));
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
        public boolean eval(final AllocatableSpace space) {
            return space.isFree(startDate, endDate, startTime, endTime, dayOfWeek, frequency, dailyFrequencyMarkSaturday,
                    dailyFrequencyMarkSunday);
        }

    }

    public static class IsFreeIntervalPredicate implements AllocatableSpacePredicate {

        private final Interval[] intervals;

        public IsFreeIntervalPredicate(final Interval[] intervals) {
            this.intervals = intervals;
        }

        @Override
        public boolean eval(final AllocatableSpace space) {
            return space.isFree(intervals);
        }

    }

    public static List<AllocatableSpace> findActiveAllocatableSpacesForEducationWithNormalCapacity(Integer normalCapacity) {
        return findAllocatableSpacesByPredicates(new ActiveForEducationWithNormalCapacityPredicate(normalCapacity));
    }

    public static List<AllocatableSpace> findActiveAllocatableSpacesForEducationByRoomType(RoomClassification roomType) {
        return findAllocatableSpacesByPredicates(new ActiveForEducationForRoomTypePredicate(roomType));
    }

    public boolean isFree(final Interval[] intervals) {
        for (final ResourceAllocation spaceOccupation : getResourceAllocationsForCheck()) {
            if (spaceOccupation.isEventSpaceOccupation()) {
                final EventSpaceOccupation occupation = (EventSpaceOccupation) spaceOccupation;
                if (occupation.overlaps(intervals)) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isFree(YearMonthDay startDate, YearMonthDay endDate, HourMinuteSecond startTime, HourMinuteSecond endTime,
            DiaSemana dayOfWeek, FrequencyType frequency, Boolean dailyFrequencyMarkSaturday, Boolean dailyFrequencyMarkSunday) {

        for (ResourceAllocation spaceOccupation : getResourceAllocationsForCheck()) {
            if (spaceOccupation.isEventSpaceOccupation()) {
                EventSpaceOccupation occupation = (EventSpaceOccupation) spaceOccupation;
                if (occupation.alreadyWasOccupiedIn(startDate, endDate, startTime, endTime, dayOfWeek, frequency,
                        dailyFrequencyMarkSaturday, dailyFrequencyMarkSunday)) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isOccupiedByExecutionCourse(final ExecutionCourse executionCourse, final DateTime start, final DateTime end) {

        for (final ResourceAllocation spaceOccupation : getResourceAllocationsForCheck()) {
            if (spaceOccupation.isEventSpaceOccupation()) {
                final EventSpaceOccupation occupation = (EventSpaceOccupation) spaceOccupation;
                if (occupation.isOccupiedByExecutionCourse(executionCourse, start, end)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isFree(EventSpaceOccupation occupationToNotCheck) {
        for (ResourceAllocation spaceOccupation : getResourceAllocationsForCheck()) {
            if (spaceOccupation.isEventSpaceOccupation()) {
                EventSpaceOccupation occupation = (EventSpaceOccupation) spaceOccupation;
                if (!occupation.equals(occupationToNotCheck) && occupation.alreadyWasOccupiedBy(occupationToNotCheck)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Deprecated
    public List<Lesson> getAssociatedLessons(final ExecutionSemester executionSemester) {
        final List<Lesson> lessons = new ArrayList<Lesson>();
        for (ResourceAllocation spaceOccupation : getResourceAllocations()) {
            if (spaceOccupation.isLessonSpaceOccupation()) {
                LessonSpaceOccupation roomOccupation = (LessonSpaceOccupation) spaceOccupation;
                final Lesson lesson = roomOccupation.getLesson();
                if (lesson.getExecutionPeriod() == executionSemester) {
                    lessons.add(lesson);
                }
            }
        }
        return lessons;
    }

    public List<Lesson> getAssociatedLessons(AcademicInterval academicInterval) {
        final List<Lesson> lessons = new ArrayList<Lesson>();
        for (ResourceAllocation spaceOccupation : getResourceAllocations()) {
            if (spaceOccupation.isLessonSpaceOccupation()) {
                LessonSpaceOccupation roomOccupation = (LessonSpaceOccupation) spaceOccupation;
                final Lesson lesson = roomOccupation.getLesson();
                if (lesson.getAcademicInterval().equals(academicInterval)) {
                    lessons.add(lesson);
                }
            }
        }
        return lessons;
    }

    public ResourceAllocation getFirstOccurrenceOfResourceAllocationByClass(Class<? extends ResourceAllocation> clazz) {
        if (clazz != null) {
            Collection<ResourceAllocation> resourceAllocations = getResourceAllocations();
            for (ResourceAllocation resourceAllocation : resourceAllocations) {
                if (resourceAllocation.getClass().equals(clazz)) {
                    return resourceAllocation;
                }
            }
        }
        return null;
    }

    public static List<AllocatableSpace> findActiveAllocatableSpacesBySpecifiedArguments(String nome, String edificio,
            Integer piso, RoomClassification tipo, Integer capacidadeNormal, Integer capacidadeExame) {

        final List<AllocatableSpace> activeRoomsForEducation =
                AllocatableSpace.getAllActiveAllocatableSpacesForEducationAndPunctualOccupations();
        final List<AllocatableSpace> result = new ArrayList<AllocatableSpace>();

        for (AllocatableSpace room : activeRoomsForEducation) {

            if (nome != null) {
                String[] identificationWords = getIdentificationWords(nome);
                if (!room.verifyNameEquality(identificationWords)) {
                    continue;
                }
            }

            Building spaceBuilding = room.getSpaceBuilding();
            if (edificio != null && (spaceBuilding == null || !spaceBuilding.getName().equalsIgnoreCase(edificio))) {
                continue;
            }

            Integer spaceFloor = room.getPiso();
            if (piso != null && (spaceFloor == null || !spaceFloor.equals(piso))) {
                continue;
            }

            RoomClassification roomClassification = room.getRoomClassification();
            if (tipo != null && (roomClassification == null || !roomClassification.equals(tipo))) {
                continue;
            }

            if (capacidadeNormal != null
                    && (room.getNormalCapacity() == null || room.getNormalCapacity().intValue() < capacidadeNormal.intValue())) {
                continue;
            }

            if (capacidadeExame != null
                    && (room.getExamCapacity() == null || room.getExamCapacity().intValue() < capacidadeExame.intValue())) {
                continue;
            }

            result.add(room);
        }
        return result;
    }

    public boolean isActiveManager(Person person) {
        return getSpaceManagementAccessGroupWithChainOfResponsibility().isMember(person);
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.inquiries.InquiryRoomAnswer> getInquiryRoomAnswers() {
        return getInquiryRoomAnswersSet();
    }

    @Deprecated
    public boolean hasAnyInquiryRoomAnswers() {
        return !getInquiryRoomAnswersSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.WrittenEvaluationEnrolment> getWrittenEvaluationEnrolments() {
        return getWrittenEvaluationEnrolmentsSet();
    }

    @Deprecated
    public boolean hasAnyWrittenEvaluationEnrolments() {
        return !getWrittenEvaluationEnrolmentsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Summary> getAssociatedSummaries() {
        return getAssociatedSummariesSet();
    }

    @Deprecated
    public boolean hasAnyAssociatedSummaries() {
        return !getAssociatedSummariesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.oldInquiries.InquiriesRoom> getAssociatedInquiriesRooms() {
        return getAssociatedInquiriesRoomsSet();
    }

    @Deprecated
    public boolean hasAnyAssociatedInquiriesRooms() {
        return !getAssociatedInquiriesRoomsSet().isEmpty();
    }

}
