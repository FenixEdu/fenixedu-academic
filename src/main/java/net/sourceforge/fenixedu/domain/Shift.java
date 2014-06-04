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
package net.sourceforge.fenixedu.domain;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.teacher.DegreeTeachingService;
import net.sourceforge.fenixedu.domain.util.email.ExecutionCourseSender;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.predicates.ResourceAllocationRolePredicates;
import net.sourceforge.fenixedu.util.Bundle;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.WeekDay;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.groups.UserGroup;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.Duration;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.dml.runtime.RelationAdapter;

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
            return cl == 0 ? DomainObjectUtil.COMPARATOR_BY_ID.compare(o1, o2) : cl;
        }

    };

    static {
        Registration.getRelationShiftStudent().addListener(new ShiftStudentListener());
    }

    public Shift(final ExecutionCourse executionCourse, Collection<ShiftType> types, final Integer lotacao) {
//        check(this, ResourceAllocationRolePredicates.checkPermissionsToManageShifts);
        super();
        setRootDomainObject(Bennu.getInstance());
        shiftTypeManagement(types, executionCourse);
        setLotacao(lotacao);
        executionCourse.setShiftNames();

        if (!hasAnyCourseLoads()) {
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

        if (newCapacity != null && getStudentsSet().size() > newCapacity.intValue()) {
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
    public Set<StudentGroup> getAssociatedStudentGroupsSet() {
        Set<StudentGroup> result = new HashSet<StudentGroup>();
        for (StudentGroup sg : super.getAssociatedStudentGroupsSet()) {
            if (sg.getValid()) {
                result.add(sg);
            }
        }
        return Collections.unmodifiableSet(result);
    }

    public void delete() {
        check(this, ResourceAllocationRolePredicates.checkPermissionsToManageShifts);
        if (canBeDeleted()) {

            final ExecutionCourse executionCourse = getExecutionCourse();

            for (; hasAnyAssociatedLessons(); getAssociatedLessons().iterator().next().delete()) {
                ;
            }
            for (; hasAnyAssociatedShiftProfessorship(); getAssociatedShiftProfessorship().iterator().next().delete()) {
                ;
            }
            for (; hasAnyShiftDistributionEntries(); getShiftDistributionEntries().iterator().next().delete()) {
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
        CourseLoad courseLoad = getCourseLoads().iterator().next();
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
        Collection<Lesson> lessons = getAssociatedLessons();
        BigDecimal lessonTotalHours = BigDecimal.ZERO;
        for (Lesson lesson : lessons) {
            lessonTotalHours = lessonTotalHours.add(lesson.getTotalHours());
        }
        return lessonTotalHours;
    }

    public Duration getTotalDuration() {
        Duration duration = Duration.ZERO;
        Collection<Lesson> lessons = getAssociatedLessons();
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
        Collection<Lesson> lessons = getAssociatedLessons();
        for (Lesson lesson : lessons) {
            hours = hours.add(lesson.getUnitHours());
        }
        return hours;
    }

    public double getHoursOnSaturdaysOrNightHours(int nightHour) {
        double hours = 0;
        Collection<Lesson> lessons = this.getAssociatedLessons();
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
        Collection<Lesson> lessons = getAssociatedLessons();
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
                    && (getCourseLoadsSet().size() != 1 || !containsType(ShiftType.LABORATORIAL))) {
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
        final boolean result = getLotacao().intValue() > getStudentsSet().size();
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
        List<Summary> result = new ArrayList<Summary>();
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
        for (SchoolClass schoolClass : getAssociatedClasses()) {
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

        ExecutionCourseSender sender = ExecutionCourseSender.newInstance(executionCourse);
        Collection<Recipient> recipients =
                Collections.singletonList(new Recipient(UserGroup.of(registration.getPerson().getUser())));
        final String subject =
                BundleUtil.getString(Bundle.APPLICATION, "label.shift.remove.subject");
        final String body =
                BundleUtil.getString(Bundle.APPLICATION, "label.shift.remove.body", getNome());

        new Message(sender, sender.getConcreteReplyTos(), recipients, subject, body, "");
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
        if (this.hasAnyAssociatedLessons()) {
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
        if (this.hasAnyAssociatedLessons()) {
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

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacy.degree.ShiftDistributionEntry> getShiftDistributionEntries() {
        return getShiftDistributionEntriesSet();
    }

    @Deprecated
    public boolean hasAnyShiftDistributionEntries() {
        return !getShiftDistributionEntriesSet().isEmpty();
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
    public java.util.Set<net.sourceforge.fenixedu.domain.inquiries.InquiryCourseAnswer> getInquiryCoursesAnswers() {
        return getInquiryCoursesAnswersSet();
    }

    @Deprecated
    public boolean hasAnyInquiryCoursesAnswers() {
        return !getInquiryCoursesAnswersSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.StudentGroup> getAssociatedStudentGroups() {
        return getAssociatedStudentGroupsSet();
    }

    @Deprecated
    public boolean hasAnyAssociatedStudentGroups() {
        return !getAssociatedStudentGroupsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.CourseLoad> getCourseLoads() {
        return getCourseLoadsSet();
    }

    @Deprecated
    public boolean hasAnyCourseLoads() {
        return !getCourseLoadsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.ShiftProfessorship> getAssociatedShiftProfessorship() {
        return getAssociatedShiftProfessorshipSet();
    }

    @Deprecated
    public boolean hasAnyAssociatedShiftProfessorship() {
        return !getAssociatedShiftProfessorshipSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Lesson> getAssociatedLessons() {
        return getAssociatedLessonsSet();
    }

    @Deprecated
    public boolean hasAnyAssociatedLessons() {
        return !getAssociatedLessonsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.ShiftEnrolment> getShiftEnrolments() {
        return getShiftEnrolmentsSet();
    }

    @Deprecated
    public boolean hasAnyShiftEnrolments() {
        return !getShiftEnrolmentsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.SchoolClass> getAssociatedClasses() {
        return getAssociatedClassesSet();
    }

    @Deprecated
    public boolean hasAnyAssociatedClasses() {
        return !getAssociatedClassesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.NonRegularTeachingService> getNonRegularTeachingServices() {
        return getNonRegularTeachingServicesSet();
    }

    @Deprecated
    public boolean hasAnyNonRegularTeachingServices() {
        return !getNonRegularTeachingServicesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.teacher.DegreeTeachingService> getDegreeTeachingServices() {
        return getDegreeTeachingServicesSet();
    }

    @Deprecated
    public boolean hasAnyDegreeTeachingServices() {
        return !getDegreeTeachingServicesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.Registration> getStudents() {
        return getStudentsSet();
    }

    @Deprecated
    public boolean hasAnyStudents() {
        return !getStudentsSet().isEmpty();
    }

    @Deprecated
    public boolean hasShiftGroupingProperties() {
        return getShiftGroupingProperties() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasComment() {
        return getComment() != null;
    }

    @Deprecated
    public boolean hasLotacao() {
        return getLotacao() != null;
    }

    @Deprecated
    public boolean hasNome() {
        return getNome() != null;
    }

}
