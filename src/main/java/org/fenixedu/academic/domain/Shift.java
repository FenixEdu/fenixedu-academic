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
package org.fenixedu.academic.domain;

import static org.fenixedu.academic.predicate.AccessControl.check;

import java.math.BigDecimal;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.predicate.ResourceAllocationRolePredicates;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.DiaSemana;
import org.fenixedu.academic.util.WeekDay;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.messaging.core.domain.Message;
import org.joda.time.Duration;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.dml.runtime.RelationAdapter;

public class Shift extends Shift_Base {

    public static final Comparator<Shift> SHIFT_COMPARATOR_BY_NAME =
            (o1, o2) -> Collator.getInstance().compare(o1.getNome(), o2.getNome());

    public static final Comparator<Shift> SHIFT_COMPARATOR_BY_TYPE_AND_ORDERED_LESSONS = (o1, o2) -> {
        final int ce = o1.getExecutionCourse().getNome().compareTo(o2.getExecutionCourse().getNome());
        if (ce != 0) {
            return ce;
        }
        final int cs = o1.getShiftTypesIntegerComparator().compareTo(o2.getShiftTypesIntegerComparator());
        if (cs != 0) {
            return cs;
        }
        final int cl = o1.getLessonsStringComparator().compareTo(o2.getLessonsStringComparator());
        return cl == 0 ? DomainObjectUtil.COMPARATOR_BY_ID.compare(o1, o2) : cl;
    };

    static {
        Registration.getRelationShiftStudent().addListener(new ShiftStudentListener());
    }

    public Shift(final ExecutionCourse executionCourse, Collection<ShiftType> types, final Integer lotacao) {
//      check(this, ResourceAllocationRolePredicates.checkPermissionsToManageShifts);
        super();
        init(executionCourse, types, lotacao);
        executionCourse.setShiftNames();
    }

    public Shift(final ExecutionCourse executionCourse, Collection<ShiftType> types, final Integer lotacao, final String shiftName) {
        super();
        init(executionCourse, types, lotacao);
        setNome(shiftName);
    }

    private void init(final ExecutionCourse executionCourse, Collection<ShiftType> types, final Integer lotacao) {
        setRootDomainObject(Bennu.getInstance());
        shiftTypeManagement(types, executionCourse);
        setLotacao(lotacao);
        if (getCourseLoadsSet().isEmpty()) {
            throw new DomainException("error.Shift.empty.courseLoads");
        }
    }

    public void edit(List<ShiftType> newTypes, Integer newCapacity, ExecutionCourse newExecutionCourse, String newName,
            String comment) {
        check(this, ResourceAllocationRolePredicates.checkPermissionsToManageShifts);

        ExecutionCourse beforeExecutionCourse = getExecutionCourse();

        final Shift otherShiftWithSameNewName = newExecutionCourse.findShiftByName(newName);
        if (otherShiftWithSameNewName != null && otherShiftWithSameNewName != this) {
            throw new DomainException("error.Shift.with.this.name.already.exists");
        }

        if (newCapacity != null && getStudentsSet().size() > newCapacity) {
            throw new DomainException("errors.exception.invalid.finalAvailability");
        }

        setLotacao(newCapacity);
        shiftTypeManagement(newTypes, newExecutionCourse);
        setNome(newName);

        beforeExecutionCourse.setShiftNames();
        if (!beforeExecutionCourse.equals(newExecutionCourse)) {
            newExecutionCourse.setShiftNames();
        }

        if (getCourseLoadsSet().isEmpty()) {
            throw new DomainException("error.Shift.empty.courseLoads");
        }

        setComment(comment);
    }
    
    public boolean isCustomName() {
        return StringUtils.isNotBlank(getNome()) && !getNome().matches(getExecutionCourse().getSigla() + "[a-zA-Z]+[0-9]+");
    }

    @Override
    public Set<StudentGroup> getAssociatedStudentGroupsSet() {
        Set<StudentGroup> result = new HashSet<>();
        for (StudentGroup sg : super.getAssociatedStudentGroupsSet()) {
            if (sg.getValid()) {
                result.add(sg);
            }
        }
        return Collections.unmodifiableSet(result);
    }

    public void delete() {
        check(this, ResourceAllocationRolePredicates.checkPermissionsToManageShifts);
        DomainException.throwWhenDeleteBlocked(getDeletionBlockers());
        final ExecutionCourse executionCourse = getExecutionCourse();

        for (; !getAssociatedLessonsSet().isEmpty(); getAssociatedLessonsSet().iterator().next().delete()) {
            ;
        }
        for (; !getAssociatedShiftProfessorshipSet().isEmpty(); getAssociatedShiftProfessorshipSet().iterator().next().delete()) {
            ;
        }
        for (; !getShiftDistributionEntriesSet().isEmpty(); getShiftDistributionEntriesSet().iterator().next().delete()) {
            ;
        }

        getAssociatedClassesSet().clear();
        getCourseLoadsSet().clear();
        if (getShiftGroupingProperties() != null) {
            getShiftGroupingProperties().delete();
        }
        setRootDomainObject(null);
        super.deleteDomainObject();

        executionCourse.setShiftNames();
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
        CourseLoad courseLoad = getCourseLoadsSet().iterator().next();
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
            getCourseLoadsSet().clear();
            for (ShiftType shiftType : types) {
                CourseLoad courseLoad = executionCourse.getCourseLoadByShiftType(shiftType);
                if (courseLoad != null) {
                    addCourseLoads(courseLoad);
                }
            }
        }
    }

    public List<ShiftType> getTypes() {
        List<ShiftType> result = new ArrayList<>();
        for (CourseLoad courseLoad : getCourseLoadsSet()) {
            result.add(courseLoad.getType());
        }
        return result;
    }

    public SortedSet<ShiftType> getSortedTypes() {
        SortedSet<ShiftType> result = new TreeSet<>();
        for (CourseLoad courseLoad : getCourseLoadsSet()) {
            result.add(courseLoad.getType());
        }
        return result;
    }

    public boolean containsType(ShiftType shiftType) {
        if (shiftType != null) {
            for (CourseLoad courseLoad : getCourseLoadsSet()) {
                if (courseLoad.getType().equals(shiftType)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void checkForDeletionBlockers(Collection<String> blockers) {
        super.checkForDeletionBlockers(blockers);
        if (!getAssociatedStudentGroupsSet().isEmpty()) {
            blockers.add(BundleUtil.getString(Bundle.RESOURCE_ALLOCATION, "error.deleteShift.with.studentGroups", getNome()));
        }
        if (!getStudentsSet().isEmpty()) {
            blockers.add(BundleUtil.getString(Bundle.RESOURCE_ALLOCATION, "error.deleteShift.with.students", getNome()));
        }
        if (!getAssociatedSummariesSet().isEmpty()) {
            blockers.add(BundleUtil.getString(Bundle.RESOURCE_ALLOCATION, "error.deleteShift.with.summaries", getNome()));
        }
    }

    public BigDecimal getTotalHours() {
        Collection<Lesson> lessons = getAssociatedLessonsSet();
        BigDecimal lessonTotalHours = BigDecimal.ZERO;
        for (Lesson lesson : lessons) {
            lessonTotalHours = lessonTotalHours.add(lesson.getTotalHours());
        }
        return lessonTotalHours;
    }

    public Duration getTotalDuration() {
        Duration duration = Duration.ZERO;
        Collection<Lesson> lessons = getAssociatedLessonsSet();
        for (Lesson lesson : lessons) {
            duration = duration.plus(lesson.getTotalDuration());
        }
        return duration;
    }

    public BigDecimal getMaxLessonDuration() {
        BigDecimal maxHours = BigDecimal.ZERO;
        for (Lesson lesson : getAssociatedLessonsSet()) {
            BigDecimal lessonHours = lesson.getUnitHours();
            if (maxHours.compareTo(lessonHours) == -1) {
                maxHours = lessonHours;
            }
        }
        return maxHours;
    }

    public BigDecimal getUnitHours() {
        BigDecimal hours = BigDecimal.ZERO;
        Collection<Lesson> lessons = getAssociatedLessonsSet();
        for (Lesson lesson : lessons) {
            hours = hours.add(lesson.getUnitHours());
        }
        return hours;
    }

    public double getHoursOnSaturdaysOrNightHours(int nightHour) {
        double hours = 0;
        Collection<Lesson> lessons = this.getAssociatedLessonsSet();
        for (Lesson lesson : lessons) {
            if (lesson.getDiaSemana().equals(new DiaSemana(DiaSemana.SABADO))) {
                hours += lesson.getUnitHours().doubleValue();
            } else {
                hours += lesson.hoursAfter(nightHour);
            }
        }
        return hours;
    }

    public int getNumberOfLessonInstances() {
        Collection<Lesson> lessons = getAssociatedLessonsSet();
        int totalLessonsDates = 0;
        for (Lesson lesson : lessons) {
            totalLessonsDates += lesson.getFinalNumberOfLessonInstances();
        }
        return totalLessonsDates;
    }

    public BigDecimal getCourseLoadWeeklyAverage() {
        BigDecimal weeklyHours = BigDecimal.ZERO;
        for (CourseLoad courseLoad : getCourseLoadsSet()) {
            weeklyHours = weeklyHours.add(courseLoad.getWeeklyHours());
        }
        return weeklyHours;
    }

    public BigDecimal getCourseLoadTotalHours() {
        BigDecimal weeklyHours = BigDecimal.ZERO;
        for (CourseLoad courseLoad : getCourseLoadsSet()) {
            weeklyHours = weeklyHours.add(courseLoad.getTotalQuantity());
        }
        return weeklyHours;
    }

    public void associateSchoolClass(SchoolClass schoolClass) {
        if (schoolClass == null) {
            throw new NullPointerException();
        }
        if (!this.getAssociatedClassesSet().contains(schoolClass)) {
            this.getAssociatedClassesSet().add(schoolClass);
        }
        if (!schoolClass.getAssociatedShiftsSet().contains(this)) {
            schoolClass.getAssociatedShiftsSet().add(this);
        }
    }

    public SortedSet<Lesson> getLessonsOrderedByWeekDayAndStartTime() {
        final SortedSet<Lesson> lessons = new TreeSet<>(Lesson.LESSON_COMPARATOR_BY_WEEKDAY_AND_STARTTIME);
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
        final boolean result = getLotacao() > getStudentsSet().size();
        if (result || isResourceAllocationManager()) {
            GroupsAndShiftsManagementLog.createLog(getExecutionCourse(), Bundle.MESSAGING,
                    "log.executionCourse.groupAndShifts.shifts.attends.added", registration.getNumber().toString(), getNome(),
                    getExecutionCourse().getNome(), getExecutionCourse().getDegreePresentationString());
            addStudents(registration);
        }
        return result;
    }

    private boolean isResourceAllocationManager() {
        final Person person = AccessControl.getPerson();
        return person != null && RoleType.RESOURCE_ALLOCATION_MANAGER.isMember(person.getUser());
    }

    public SortedSet<ShiftEnrolment> getShiftEnrolmentsOrderedByDate() {
        final SortedSet<ShiftEnrolment> shiftEnrolments = new TreeSet<>(ShiftEnrolment.COMPARATOR_BY_DATE);
        shiftEnrolments.addAll(getShiftEnrolmentsSet());
        return shiftEnrolments;
    }

    public String getClassesPrettyPrint() {
        StringBuilder builder = new StringBuilder();
        int index = 0;
        for (SchoolClass schoolClass : getAssociatedClassesSet()) {
            builder.append(schoolClass.getNome());
            index++;
            if (index < getAssociatedClassesSet().size()) {
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
            builder.append(BundleUtil.getString(Bundle.ENUMERATION, shiftType.getName()));
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
        List<Summary> result = new ArrayList<>();
        Set<Summary> summaries = getAssociatedSummariesSet();
        for (Summary summary : summaries) {
            if (summary.isExtraSummary()) {
                result.add(summary);
            }
        }
        return result;
    }

    private static class ShiftStudentListener extends RelationAdapter<Registration, Shift> {

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
        int capacity =
                getAssociatedLessonsSet().stream().filter(Lesson::hasSala)
                        .mapToInt(lesson -> lesson.getSala().getAllocatableCapacity()).min().orElse(0);
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
        for (SchoolClass schoolClass : getAssociatedClassesSet()) {
            if (schoolClass.getExecutionDegree().getDegreeType() == degreeType) {
                return true;
            }
        }
        return false;
    }

    @Atomic
    public void removeAttendFromShift(Registration registration, ExecutionCourse executionCourse) {

        GroupsAndShiftsManagementLog.createLog(getExecutionCourse(), Bundle.MESSAGING,
                "log.executionCourse.groupAndShifts.shifts.attends.removed", registration.getNumber().toString(), getNome(),
                getExecutionCourse().getNome(), getExecutionCourse().getDegreePresentationString());
        registration.removeShifts(this);

        Message.from(executionCourse.getSender()).replyToSender()
                .to(registration.getPerson().getPersonGroup())
                .subject(BundleUtil.getString(Bundle.APPLICATION, "label.shift.remove.subject"))
                .textBody(BundleUtil.getString(Bundle.APPLICATION, "label.shift.remove.body", getNome()))
                .send();
    }

    public boolean hasAnyStudentsInAssociatedStudentGroups() {
        for (final StudentGroup studentGroup : getAssociatedStudentGroupsSet()) {
            if (studentGroup.getAttendsSet().size() > 0) {
                return true;
            }
        }
        return false;
    }

    public String getPresentationName() {
        StringBuilder stringBuilder = new StringBuilder(this.getNome());
        if (!this.getAssociatedLessonsSet().isEmpty()) {
            stringBuilder.append(" ( ");

            for (Iterator<Lesson> iterator = this.getAssociatedLessonsSet().iterator(); iterator.hasNext();) {
                Lesson lesson = iterator.next();
                stringBuilder.append(WeekDay.getWeekDay(lesson.getDiaSemana()).getLabelShort());
                stringBuilder.append(" ");
                stringBuilder.append(lesson.getBeginHourMinuteSecond().toString("HH:mm"));
                stringBuilder.append(" - ");
                stringBuilder.append(lesson.getEndHourMinuteSecond().toString("HH:mm"));
                if (lesson.hasSala()) {
                    stringBuilder.append(" - ");
                    stringBuilder.append(lesson.getSala().getName());
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
        if (!this.getAssociatedLessonsSet().isEmpty()) {
            for (Iterator<Lesson> iterator = this.getAssociatedLessonsSet().iterator(); iterator.hasNext();) {
                Lesson lesson = iterator.next();
                stringBuilder.append(" ");
                stringBuilder.append(WeekDay.getWeekDay(lesson.getDiaSemana()).getLabelShort());
                stringBuilder.append(" ");
                stringBuilder.append(lesson.getBeginHourMinuteSecond().toString("HH:mm"));
                stringBuilder.append(" - ");
                stringBuilder.append(lesson.getEndHourMinuteSecond().toString("HH:mm"));
                if (lesson.hasSala()) {
                    stringBuilder.append(" - ");
                    stringBuilder.append(lesson.getSala().getName());
                }
                if (iterator.hasNext()) {
                    stringBuilder.append(" ; ");
                }
            }
        }
        return stringBuilder.toString();
    }

    public List<StudentGroup> getAssociatedStudentGroups(Grouping grouping) {
        List<StudentGroup> result = new ArrayList<>();
        for (StudentGroup studentGroup : getAssociatedStudentGroupsSet()) {
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
