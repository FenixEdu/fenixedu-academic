package net.sourceforge.fenixedu.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.accessControl.PersonGroup;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.teacher.DegreeTeachingService;
import net.sourceforge.fenixedu.domain.util.email.ExecutionCourseSender;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.BundleUtil;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.WeekDay;

import org.apache.commons.lang.StringUtils;
import org.joda.time.Duration;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class Shift extends Shift_Base {

    public static final Comparator<Shift> SHIFT_COMPARATOR_BY_NAME = new Comparator<Shift>() {

        @Override
        public int compare(Shift o1, Shift o2) {
            return Collator.getInstance().compare(o1.getNome(), o2.getNome());
        }

    };

    public static final Comparator<Shift> SHIFT_COMPARATOR_BY_TYPE_AND_ORDERED_LESSONS = new Comparator<Shift>() {

        @Override
        public int compare(Shift o1, Shift o2) {
            final int ce = o1.getExecutionCourse().getNome().compareTo(o2.getExecutionCourse().getNome());
            if (ce != 0) {
                return ce;
            }
            final int cs = o1.getShiftTypesIntegerComparator().compareTo(o2.getShiftTypesIntegerComparator());
            if (cs != 0) {
                return cs;
            }
            final int cl = o1.getLessonsStringComparator().compareTo(o2.getLessonsStringComparator());
            return cl == 0 ? AbstractDomainObject.COMPARATOR_BY_ID.compare(o1, o2) : cl;
        }

    };

    static {
        Registration.ShiftStudent.addListener(new ShiftStudentListener());
    }

    @Checked("ResourceAllocationRolePredicates.checkPermissionsToManageShifts")
    public Shift(final ExecutionCourse executionCourse, Collection<ShiftType> types, final Integer lotacao) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        shiftTypeManagement(types, executionCourse);
        setLotacao(lotacao);
        executionCourse.setShiftNames();

        if (!hasAnyCourseLoads()) {
            throw new DomainException("error.Shift.empty.courseLoads");
        }
    }

    @Checked("ResourceAllocationRolePredicates.checkPermissionsToManageShifts")
    public void edit(List<ShiftType> newTypes, Integer newCapacity, ExecutionCourse newExecutionCourse, String newName,
            String comment) {

        ExecutionCourse beforeExecutionCourse = getExecutionCourse();

        final Shift otherShiftWithSameNewName = newExecutionCourse.findShiftByName(newName);
        if (otherShiftWithSameNewName != null && otherShiftWithSameNewName != this) {
            throw new DomainException("error.Shift.with.this.name.already.exists");
        }

        if (newCapacity != null && getStudentsCount() > newCapacity.intValue()) {
            throw new DomainException("errors.exception.invalid.finalAvailability");
        }

        setLotacao(newCapacity);
        shiftTypeManagement(newTypes, newExecutionCourse);

        beforeExecutionCourse.setShiftNames();
        if (!beforeExecutionCourse.equals(newExecutionCourse)) {
            newExecutionCourse.setShiftNames();
        }

        if (!hasAnyCourseLoads()) {
            throw new DomainException("error.Shift.empty.courseLoads");
        }

        setComment(comment);
    }

    @Override
    public List<StudentGroup> getAssociatedStudentGroups() {
        List<StudentGroup> result = new ArrayList<StudentGroup>();
        for (StudentGroup sg : super.getAssociatedStudentGroups()) {
            if (sg.getValid()) {
                result.add(sg);
            }
        }
        return Collections.unmodifiableList(result);
    }

    @Override
    public int getAssociatedStudentGroupsCount() {
        return this.getAssociatedStudentGroups().size();
    }

    @Override
    public Iterator<StudentGroup> getAssociatedStudentGroupsIterator() {
        // TODO Auto-generated method stub
        return this.getAssociatedStudentGroups().iterator();
    }

    @Override
    public Set<StudentGroup> getAssociatedStudentGroupsSet() {
        // TODO Auto-generated method stub
        return new TreeSet<StudentGroup>(this.getAssociatedStudentGroups());
    }

    @Override
    public boolean hasAssociatedStudentGroups(StudentGroup associatedStudentGroups) {
        // TODO Auto-generated method stub
        return this.getAssociatedStudentGroups().contains(associatedStudentGroups);
    }

    @Checked("ResourceAllocationRolePredicates.checkPermissionsToManageShifts")
    public void delete() {
        if (canBeDeleted()) {

            final ExecutionCourse executionCourse = getExecutionCourse();

            for (; hasAnyAssociatedLessons(); getAssociatedLessons().get(0).delete()) {
                ;
            }
            for (; hasAnyAssociatedShiftProfessorship(); getAssociatedShiftProfessorship().get(0).delete()) {
                ;
            }
            for (; hasAnyShiftDistributionEntries(); getShiftDistributionEntries().get(0).delete()) {
                ;
            }

            getAssociatedClasses().clear();
            getCourseLoads().clear();
            if (getShiftGroupingProperties() != null) {
                getShiftGroupingProperties().delete();
            }
            setRootDomainObject(null);
            super.deleteDomainObject();

            executionCourse.setShiftNames();

        } else {
            throw new DomainException("shift.cannot.be.deleted");
        }
    }

    @jvstm.cps.ConsistencyPredicate
    protected boolean checkRequiredParameters() {
        return getLotacao() != null && !StringUtils.isEmpty(getNome());
    }

    @Deprecated
    public ExecutionCourse getDisciplinaExecucao() {
        return getExecutionCourse();
    }

    public ExecutionCourse getExecutionCourse() {
        CourseLoad courseLoad = getCourseLoads().get(0);
        if (courseLoad != null) {
            return courseLoad.getExecutionCourse();
        } else {
            return null;
        }
    }

    public ExecutionSemester getExecutionPeriod() {
        return getExecutionCourse().getExecutionPeriod();
    }

    private void shiftTypeManagement(Collection<ShiftType> types, ExecutionCourse executionCourse) {
        if (executionCourse != null) {
            getCourseLoads().clear();
            for (ShiftType shiftType : types) {
                CourseLoad courseLoad = executionCourse.getCourseLoadByShiftType(shiftType);
                if (courseLoad != null) {
                    addCourseLoads(courseLoad);
                }
            }
        }
    }

    public List<ShiftType> getTypes() {
        List<ShiftType> result = new ArrayList<ShiftType>();
        for (CourseLoad courseLoad : getCourseLoads()) {
            result.add(courseLoad.getType());
        }
        return result;
    }

    public SortedSet<ShiftType> getSortedTypes() {
        SortedSet<ShiftType> result = new TreeSet<ShiftType>();
        for (CourseLoad courseLoad : getCourseLoads()) {
            result.add(courseLoad.getType());
        }
        return result;
    }

    public boolean containsType(ShiftType shiftType) {
        if (shiftType != null) {
            for (CourseLoad courseLoad : getCourseLoads()) {
                if (courseLoad.getType().equals(shiftType)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean canBeDeleted() {
        if (hasAnyAssociatedStudentGroups()) {
            throw new DomainException("error.deleteShift.with.studentGroups", getNome());
        }
        if (hasAnyStudents()) {
            throw new DomainException("error.deleteShift.with.students", getNome());
        }
        if (hasAnyAssociatedSummaries()) {
            throw new DomainException("error.deleteShift.with.summaries", getNome());
        }
        if (hasAnyDegreeTeachingServices()) {
            throw new DomainException("error.deleteShift.with.degreeTeachingServices", getNome());
        }
        return true;
    }

    public BigDecimal getTotalHours() {
        List<Lesson> lessons = getAssociatedLessons();
        BigDecimal lessonTotalHours = BigDecimal.ZERO;
        for (Lesson lesson : lessons) {
            lessonTotalHours = lessonTotalHours.add(lesson.getTotalHours());
        }
        return lessonTotalHours;
    }

    public Duration getTotalDuration() {
        Duration duration = Duration.ZERO;
        List<Lesson> lessons = getAssociatedLessons();
        for (Lesson lesson : lessons) {
            duration = duration.plus(lesson.getTotalDuration());
        }
        return duration;
    }

    public BigDecimal getMaxLessonDuration() {
        BigDecimal maxHours = BigDecimal.ZERO;
        for (Lesson lesson : getAssociatedLessons()) {
            BigDecimal lessonHours = lesson.getUnitHours();
            if (maxHours.compareTo(lessonHours) == -1) {
                maxHours = lessonHours;
            }
        }
        return maxHours;
    }

    public BigDecimal getUnitHours() {
        BigDecimal hours = BigDecimal.ZERO;
        List<Lesson> lessons = getAssociatedLessons();
        for (int i = 0; i < lessons.size(); i++) {
            Lesson lesson = lessons.get(i);
            hours = hours.add(lesson.getUnitHours());
        }
        return hours;
    }

    public double getHoursOnSaturdaysOrNightHours(int nightHour) {
        double hours = 0;
        List<Lesson> lessons = this.getAssociatedLessons();
        for (int i = 0; i < lessons.size(); i++) {
            Lesson lesson = lessons.get(i);
            if (lesson.getDiaSemana().equals(new DiaSemana(DiaSemana.SABADO))) {
                hours += lesson.getUnitHours().doubleValue();
            } else {
                hours += lesson.hoursAfter(nightHour);
            }
        }
        return hours;
    }

    public int getNumberOfLessonInstances() {
        List<Lesson> lessons = getAssociatedLessons();
        int totalLessonsDates = 0;
        for (Lesson lesson : lessons) {
            totalLessonsDates += lesson.getFinalNumberOfLessonInstances();
        }
        return totalLessonsDates;
    }

    public BigDecimal getCourseLoadWeeklyAverage() {
        BigDecimal weeklyHours = BigDecimal.ZERO;
        for (CourseLoad courseLoad : getCourseLoads()) {
            weeklyHours = weeklyHours.add(courseLoad.getWeeklyHours());
        }
        return weeklyHours;
    }

    public BigDecimal getCourseLoadTotalHours() {
        BigDecimal weeklyHours = BigDecimal.ZERO;
        for (CourseLoad courseLoad : getCourseLoads()) {
            weeklyHours = weeklyHours.add(courseLoad.getTotalQuantity());
        }
        return weeklyHours;
    }

    public void associateSchoolClass(SchoolClass schoolClass) {
        if (schoolClass == null) {
            throw new NullPointerException();
        }
        if (!this.getAssociatedClasses().contains(schoolClass)) {
            this.getAssociatedClasses().add(schoolClass);
        }
        if (!schoolClass.getAssociatedShifts().contains(this)) {
            schoolClass.getAssociatedShifts().add(this);
        }
    }

    public Double getAvailableShiftPercentage(Professorship professorship) {
        Double availablePercentage = 100.0;
        for (DegreeTeachingService degreeTeachingService : getDegreeTeachingServices()) {
            if (degreeTeachingService.getProfessorship() != professorship) {
                availablePercentage -= degreeTeachingService.getPercentage();
            }
        }
        for (NonRegularTeachingService nonRegularTeachingService : getNonRegularTeachingServices()) {
            if (nonRegularTeachingService.getProfessorship() != professorship
                    && (getCourseLoadsCount() != 1 || !containsType(ShiftType.LABORATORIAL))) {
                availablePercentage -= nonRegularTeachingService.getPercentage();
            }
        }

        return new BigDecimal(availablePercentage).divide(new BigDecimal(1), 2, RoundingMode.HALF_EVEN).doubleValue();
    }

    public SortedSet<Lesson> getLessonsOrderedByWeekDayAndStartTime() {
        final SortedSet<Lesson> lessons = new TreeSet<Lesson>(Lesson.LESSON_COMPARATOR_BY_WEEKDAY_AND_STARTTIME);
        lessons.addAll(getAssociatedLessonsSet());
        return lessons;
    }

    public String getLessonsStringComparator() {
        final StringBuilder stringBuilder = new StringBuilder();
        for (final Lesson lesson : getLessonsOrderedByWeekDayAndStartTime()) {
            stringBuilder.append(lesson.getDiaSemana().getDiaSemana().toString());
            stringBuilder.append(lesson.getBeginHourMinuteSecond().toString());
        }
        return stringBuilder.toString();
    }

    public Integer getShiftTypesIntegerComparator() {
        final StringBuilder stringBuilder = new StringBuilder();
        for (ShiftType shiftType : getSortedTypes()) {
            stringBuilder.append(shiftType.ordinal() + 1);
        }
        return Integer.valueOf(stringBuilder.toString());
    }

    public boolean reserveForStudent(final Registration registration) {
        final boolean result = getLotacao().intValue() > getStudentsCount();
        if (result || isResourceAllocationManager()) {
            GroupsAndShiftsManagementLog.createLog(getExecutionCourse(), "resources.MessagingResources",
                    "log.executionCourse.groupAndShifts.shifts.attends.added", registration.getNumber().toString(), getNome(),
                    getExecutionCourse().getNome(), getExecutionCourse().getDegreePresentationString());
            addStudents(registration);
        }
        return result;
    }

    private boolean isResourceAllocationManager() {
        final Person person = AccessControl.getPerson();
        return person != null && person.hasRole(RoleType.RESOURCE_ALLOCATION_MANAGER);
    }

    public SortedSet<ShiftEnrolment> getShiftEnrolmentsOrderedByDate() {
        final SortedSet<ShiftEnrolment> shiftEnrolments = new TreeSet<ShiftEnrolment>(ShiftEnrolment.COMPARATOR_BY_DATE);
        shiftEnrolments.addAll(getShiftEnrolmentsSet());
        return shiftEnrolments;
    }

    public String getClassesPrettyPrint() {
        StringBuilder builder = new StringBuilder();
        int index = 0;
        for (SchoolClass schoolClass : getAssociatedClasses()) {
            builder.append(schoolClass.getNome());
            index++;
            if (index < getAssociatedClassesCount()) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }

    public String getShiftTypesPrettyPrint() {
        StringBuilder builder = new StringBuilder();
        int index = 0;
        SortedSet<ShiftType> sortedTypes = getSortedTypes();
        for (ShiftType shiftType : sortedTypes) {
            builder.append(BundleUtil.getStringFromResourceBundle("resources.EnumerationResources", shiftType.getName()));
            index++;
            if (index < sortedTypes.size()) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }

    public String getShiftTypesCapitalizedPrettyPrint() {
        StringBuilder builder = new StringBuilder();
        int index = 0;
        SortedSet<ShiftType> sortedTypes = getSortedTypes();
        for (ShiftType shiftType : sortedTypes) {
            builder.append(shiftType.getFullNameTipoAula());
            index++;
            if (index < sortedTypes.size()) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }

    public String getShiftTypesCodePrettyPrint() {
        StringBuilder builder = new StringBuilder();
        int index = 0;
        SortedSet<ShiftType> sortedTypes = getSortedTypes();
        for (ShiftType shiftType : sortedTypes) {
            builder.append(shiftType.getSiglaTipoAula());
            index++;
            if (index < sortedTypes.size()) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }

    public List<Summary> getExtraSummaries() {
        List<Summary> result = new ArrayList<Summary>();
        Set<Summary> summaries = getAssociatedSummariesSet();
        for (Summary summary : summaries) {
            if (summary.isExtraSummary()) {
                result.add(summary);
            }
        }
        return result;
    }

    private static class ShiftStudentListener extends dml.runtime.RelationAdapter<Registration, Shift> {

        @Override
        public void afterAdd(Registration registration, Shift shift) {
            if (!shift.hasShiftEnrolment(registration)) {
                new ShiftEnrolment(shift, registration);
            }
        }

        @Override
        public void afterRemove(Registration registration, Shift shift) {
            shift.unEnrolStudent(registration);
        }
    }

    private boolean hasShiftEnrolment(final Registration registration) {
        for (final ShiftEnrolment shiftEnrolment : getShiftEnrolmentsSet()) {
            if (shiftEnrolment.hasRegistration(registration)) {
                return true;
            }
        }
        return false;
    }

    public void unEnrolStudent(final Registration registration) {
        final ShiftEnrolment shiftEnrolment = findShiftEnrolment(registration);
        if (shiftEnrolment != null) {
            shiftEnrolment.delete();
        }
    }

    private ShiftEnrolment findShiftEnrolment(final Registration registration) {
        for (final ShiftEnrolment shiftEnrolment : getShiftEnrolmentsSet()) {
            if (shiftEnrolment.getRegistration() == registration) {
                return shiftEnrolment;
            }
        }
        return null;
    }

    public int getCapacityBasedOnSmallestRoom() {
        int capacity = 0;

        for (final Lesson lesson : getAssociatedLessonsSet()) {
            if (lesson.hasSala()) {
                if (capacity == 0) {
                    capacity = (lesson.getSala()).getNormalCapacity();
                } else {
                    capacity = Math.min(capacity, (lesson.getSala()).getNormalCapacity());
                }
            }
        }

        return capacity + (capacity / 10);
    }

    public boolean hasShiftType(final ShiftType shiftType) {
        for (CourseLoad courseLoad : getCourseLoadsSet()) {
            if (courseLoad.getType() == shiftType) {
                return true;
            }
        }
        return false;
    }

    public boolean hasSchoolClassForDegreeType(DegreeType degreeType) {
        for (SchoolClass schoolClass : getAssociatedClasses()) {
            if (schoolClass.getExecutionDegree().getDegreeType() == degreeType) {
                return true;
            }
        }
        return false;
    }

    @Atomic
    public void removeAttendFromShift(Registration registration, ExecutionCourse executionCourse) {

        GroupsAndShiftsManagementLog.createLog(getExecutionCourse(), "resources.MessagingResources",
                "log.executionCourse.groupAndShifts.shifts.attends.removed", registration.getNumber().toString(), getNome(),
                getExecutionCourse().getNome(), getExecutionCourse().getDegreePresentationString());
        registration.removeShifts(this);

        ExecutionCourseSender sender = ExecutionCourseSender.newInstance(executionCourse);
        Collection<Recipient> recipients = Collections.singletonList(new Recipient(new PersonGroup(registration.getPerson())));
        final String subject =
                BundleUtil.getStringFromResourceBundle("resources.ApplicationResources", "label.shift.remove.subject");
        final String body =
                BundleUtil.getStringFromResourceBundle("resources.ApplicationResources", "label.shift.remove.body", getNome());

        new Message(sender, sender.getConcreteReplyTos(), recipients, subject, body, "");
    }

    public boolean hasAnyStudentsInAssociatedStudentGroups() {
        for (final StudentGroup studentGroup : getAssociatedStudentGroupsSet()) {
            if (studentGroup.getAttendsCount() > 0) {
                return true;
            }
        }
        return false;
    }

    public String getPresentationName() {
        StringBuilder stringBuilder = new StringBuilder(this.getNome());
        if (this.hasAnyAssociatedLessons()) {
            stringBuilder.append(" ( ");

            for (Iterator<Lesson> iterator = this.getAssociatedLessonsIterator(); iterator.hasNext();) {
                Lesson lesson = iterator.next();
                stringBuilder.append(WeekDay.getWeekDay(lesson.getDiaSemana()).getLabelShort());
                stringBuilder.append(" ");
                stringBuilder.append(lesson.getBeginHourMinuteSecond().toString("HH:mm"));
                stringBuilder.append(" - ");
                stringBuilder.append(lesson.getEndHourMinuteSecond().toString("HH:mm"));
                if (lesson.hasSala()) {
                    stringBuilder.append(" - ");
                    stringBuilder.append(lesson.getSala().getIdentification());
                }
                if (iterator.hasNext()) {
                    stringBuilder.append(" ; ");
                }
            }
            stringBuilder.append(" ) ");
        }
        return stringBuilder.toString();
    }

    public String getLessonPresentationString() {
        StringBuilder stringBuilder = new StringBuilder(this.getNome());
        if (this.hasAnyAssociatedLessons()) {
            for (Iterator<Lesson> iterator = this.getAssociatedLessonsIterator(); iterator.hasNext();) {
                Lesson lesson = iterator.next();
                stringBuilder.append(" ");
                stringBuilder.append(WeekDay.getWeekDay(lesson.getDiaSemana()).getLabelShort());
                stringBuilder.append(" ");
                stringBuilder.append(lesson.getBeginHourMinuteSecond().toString("HH:mm"));
                stringBuilder.append(" - ");
                stringBuilder.append(lesson.getEndHourMinuteSecond().toString("HH:mm"));
                if (lesson.hasSala()) {
                    stringBuilder.append(" - ");
                    stringBuilder.append(lesson.getSala().getIdentification());
                }
                if (iterator.hasNext()) {
                    stringBuilder.append(" ; ");
                }
            }
        }
        return stringBuilder.toString();
    }

    public List<StudentGroup> getAssociatedStudentGroups(Grouping grouping) {
        List<StudentGroup> result = new ArrayList<StudentGroup>();
        for (StudentGroup studentGroup : getAssociatedStudentGroups()) {
            if (studentGroup.getGrouping() == grouping) {
                result.add(studentGroup);
            }
        }
        return result;
    }

    public boolean isTotalShiftLoadExceeded() {
        final BigDecimal totalHours = getTotalHours();
        for (final CourseLoad courseLoad : getCourseLoadsSet()) {
            if (totalHours.compareTo(courseLoad.getTotalQuantity()) == 1) {
                return true;
            }
        }
        return false;
    }

}