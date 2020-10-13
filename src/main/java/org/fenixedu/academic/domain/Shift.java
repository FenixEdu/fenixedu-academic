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

import java.math.BigDecimal;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.schedule.shiftCapacity.ShiftCapacity;
import org.fenixedu.academic.domain.schedule.shiftCapacity.ShiftCapacityType;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.DiaSemana;
import org.fenixedu.academic.util.WeekDay;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.dml.runtime.RelationAdapter;

public class Shift extends Shift_Base {

    private static final Logger LOG = LoggerFactory.getLogger(Shift.class);

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

    public Shift(final ExecutionCourse executionCourse, Collection<ShiftType> types, final Integer lotacao,
            final String shiftName) {
        super();
        setRootDomainObject(Bennu.getInstance());
        shiftTypeManagement(types, executionCourse);
        setLotacao(lotacao);
        setNome(shiftName);

        if (StringUtils.isBlank(shiftName)) {
            executionCourse.setShiftNames();
        }

        if (getCourseLoadsSet().isEmpty()) {
            throw new DomainException("error.Shift.empty.courseLoads");
        }
    }

    public Shift(final ExecutionCourse executionCourse, Collection<ShiftType> types, final Integer lotacao) {
        this(executionCourse, types, lotacao, null);
    }

    public void edit(List<ShiftType> newTypes, ExecutionCourse newExecutionCourse, String newName, String comment) {

        ExecutionCourse beforeExecutionCourse = getExecutionCourse();

        if (newExecutionCourse.getAssociatedShifts().stream().anyMatch(s -> s.getNome().equals(newName) && s != this)) {
            throw new DomainException("error.Shift.with.this.name.already.exists");
        }

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
        final String cleanSigla = getExecutionCourse().getSigla().replaceAll("\\(", "\\\\(").replaceAll("\\)", "\\\\)");
        return StringUtils.isNotBlank(getNome()) && !getNome().matches(cleanSigla + "[a-zA-Z]+[0-9]+");
    }

    public void delete() {
        DomainException.throwWhenDeleteBlocked(getDeletionBlockers());
        final ExecutionCourse executionCourse = getExecutionCourse();

        for (; !getAssociatedLessonsSet().isEmpty(); getAssociatedLessonsSet().iterator().next().delete()) {
            ;
        }
        for (; !getAssociatedShiftProfessorshipSet().isEmpty(); getAssociatedShiftProfessorshipSet().iterator().next().delete()) {
            ;
        }

        getShiftCapacitiesSet().forEach(sc -> sc.delete());

        getAssociatedClassesSet().clear();
        getCourseLoadsSet().clear();
        setRootDomainObject(null);
        super.deleteDomainObject();

        executionCourse.setShiftNames();
    }

    @jvstm.cps.ConsistencyPredicate
    protected boolean checkRequiredParameters() {
        return super.getLotacao() != null && !StringUtils.isEmpty(getNome());
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

    public ExecutionInterval getExecutionPeriod() {
        return getExecutionCourse().getExecutionInterval();
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
        List<ShiftType> result = new ArrayList<ShiftType>();
        for (CourseLoad courseLoad : getCourseLoadsSet()) {
            result.add(courseLoad.getType());
        }
        return result;
    }

    public SortedSet<ShiftType> getSortedTypes() {
        SortedSet<ShiftType> result = new TreeSet<ShiftType>();
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

    public boolean isFreeFor(final Registration registration) {
        return getShiftCapacitiesSet().stream().filter(ShiftCapacity::isFreeIncludingExtraCapacities)
                .anyMatch(sc -> sc.accepts(registration));
    }

    public static Stream<ShiftCapacity> findPossibleShiftsToEnrol(final Registration registration,
            final ExecutionCourse executionCourse, final ShiftType shiftType) {
        return executionCourse.getAssociatedShifts().stream().filter(s -> s.containsType(shiftType))
                .flatMap(s -> s.getShiftCapacitiesSet().stream()).filter(ShiftCapacity::isFreeIncludingExtraCapacities)
                .filter(sc -> sc.accepts(registration));
    }

    /**
     * Enrolls provided registration in this shift and its first suitable free capacity, and unenroll it from other shifts of same
     * type and course
     * 
     * @param registration registration to enroll in this shift
     * @return <code>true</code> if registration enrolled successfully in this shift
     */
    public boolean enrol(final Registration registration) {
        return getShiftCapacitiesSet().stream().sorted(ShiftCapacity.TYPE_EVALUATION_PRIORITY_COMPARATOR)
                .filter(ShiftCapacity::isFreeIncludingExtraCapacities).filter(sc -> sc.accepts(registration)).findFirst()
                .map(sc -> doEnrol(registration, sc)).orElse(false);
    }

    public static boolean enrol(final Registration registration, final ShiftCapacity shiftCapacity) {
        if (shiftCapacity != null && shiftCapacity.isFreeIncludingExtraCapacities() && shiftCapacity.accepts(registration)) {
            return doEnrol(registration, shiftCapacity);
        }
        return false;
    }

    private static boolean doEnrol(final Registration registration, final ShiftCapacity shiftCapacityParam) {

        // if shiftCapacity isn't free, check if is there an extra capacity configured
        final ShiftCapacity shiftCapacity = shiftCapacityParam.isFree() ? shiftCapacityParam : shiftCapacityParam
                .getExtraCapacitiesSet().stream().filter(sc -> sc.isFree())
                .sorted(ShiftCapacity.TYPE_EVALUATION_PRIORITY_COMPARATOR).findFirst().orElse(null);
        if (shiftCapacity == null) {
            return false;
        }

        final Shift shift = shiftCapacity.getShift();

        final ExecutionCourse executionCourse = shift.getExecutionCourse();

        // remove registration from shifts of the same type
        shift.getTypes().stream().map(st -> registration.getShiftFor(executionCourse, st)).filter(Objects::nonNull)
                .forEach(s -> s.unenrol(registration));

        if (ShiftEnrolment.find(shift, registration).isPresent()) {
            throw new DomainException("error.Shift.enrolment.alreadyEnrolled"); // this should never happen
        }

        new ShiftEnrolment(shiftCapacity, registration);

        shift.addStudents(registration); // to deprecate

        GroupsAndShiftsManagementLog.createLog(executionCourse, Bundle.MESSAGING,
                "log.executionCourse.groupAndShifts.shifts.attends.added", registration.getNumber().toString(), shift.getNome(),
                executionCourse.getNome(), executionCourse.getDegreePresentationString());

        LOG.info(
                "SHIFT ENROLMENT: student-{} degree-{} shift-{} course-{} shiftCapacity-{}"
                        + (shiftCapacityParam != shiftCapacity ? " originalShiftCapacity-{}" : ""),
                registration.getStudent().getNumber(), registration.getDegree().getCode(), shift.getNome(),
                executionCourse.getCode(), shiftCapacity.getType().getName().getContent(),
                shiftCapacityParam.getType().getName().getContent());
        return true;
    }

    public void unenrol(final Registration registration) {

        ShiftEnrolment.find(this, registration).ifPresent(se -> se.delete());

        removeStudents(registration); // to deprecate

        final ExecutionCourse executionCourse = getExecutionCourse();
        GroupsAndShiftsManagementLog.createLog(getExecutionCourse(), Bundle.MESSAGING,
                "log.executionCourse.groupAndShifts.shifts.attends.removed", registration.getNumber().toString(), getNome(),
                executionCourse.getNome(), executionCourse.getDegreePresentationString());

        LOG.info("SHIFT UNENROLMENT: student-{} degree-{} shift-{} course-{}", registration.getStudent().getNumber(),
                registration.getDegree().getCode(), getNome(), executionCourse.getCode());
    }

    public SortedSet<ShiftEnrolment> getShiftEnrolmentsOrderedByDate() {
        final SortedSet<ShiftEnrolment> shiftEnrolments = new TreeSet<ShiftEnrolment>(ShiftEnrolment.COMPARATOR_BY_DATE);
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
            ShiftEnrolment.find(shift, registration).orElseGet(() -> new ShiftEnrolment(shift, registration));
        }

        @Override
        public void afterRemove(Registration registration, Shift shift) {
            ShiftEnrolment.find(shift, registration).ifPresent(se -> se.delete());
        }
    }

    public int getCapacityBasedOnSmallestRoom() {
        int capacity = getAssociatedLessonsSet().stream().filter(Lesson::hasSala)
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

//    @Atomic
//    public void removeAttendFromShift(Registration registration, ExecutionCourse executionCourse) {
//
//        GroupsAndShiftsManagementLog.createLog(getExecutionCourse(), Bundle.MESSAGING,
//                "log.executionCourse.groupAndShifts.shifts.attends.removed", registration.getNumber().toString(), getNome(),
//                getExecutionCourse().getNome(), getExecutionCourse().getDegreePresentationString());
//        registration.removeShifts(this);
//
//        ExecutionCourseSender sender = ExecutionCourseSender.newInstance(executionCourse);
//        Collection<Recipient> recipients = Collections.singletonList(new Recipient(registration.getPerson().getUser().groupOf()));
//        final String subject = BundleUtil.getString(Bundle.APPLICATION, "label.shift.remove.subject");
//        final String body = BundleUtil.getString(Bundle.APPLICATION, "label.shift.remove.body", getNome());
//
//        new Message(sender, sender.getConcreteReplyTos(), recipients, subject, body, "");
//    }

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

    public boolean isTotalShiftLoadExceeded() {
        final BigDecimal totalHours = getTotalHours();
        for (final CourseLoad courseLoad : getCourseLoadsSet()) {
            if (totalHours.compareTo(courseLoad.getTotalQuantity()) == 1) {
                return true;
            }
        }
        return false;
    }

    public Integer getVacancies() {
//        return getLotacao() - getStudentsSet().size();
        return ShiftCapacity.getTotalCapacity(this) - ShiftEnrolment.getTotalEnrolments(this);
    }

    @Deprecated
    @Override
    public Integer getLotacao() {
        return ShiftCapacity.getTotalCapacity(this);
    }

    @Deprecated
    @Override
    public void setLotacao(Integer lotacao) {
        initDefaultShiftCapacity();
        getDefaultShiftCapacity().ifPresent(sc -> sc.setCapacity(lotacao));
        super.setLotacao(lotacao);
    }

    private Optional<ShiftCapacity> getDefaultShiftCapacity() {
        return getShiftCapacitiesSet().size() == 1 ? getShiftCapacitiesSet().stream().filter(sc -> sc.getType().isDefaultType())
                .findAny() : Optional.empty();
    }

    public boolean initDefaultShiftCapacity() {
        if (getShiftCapacitiesSet().isEmpty()) {
            final ShiftCapacity shiftCapacity =
                    new ShiftCapacity(this, ShiftCapacityType.findOrCreateDefault(), super.getLotacao());
            getShiftEnrolmentsSet().stream().filter(se -> se.getShiftCapacity() == null)
                    .forEach(se -> se.setShiftCapacity(shiftCapacity));
            return true;
        }
        return false;
    }

    @Override
    public void setName(String name) {
        super.setName(name);
        super.setNome(name);
    }

    @Deprecated
    @Override
    public void setNome(String nome) {
        super.setNome(nome);
        super.setName(nome);
    }

    public boolean migrateNomeToName() {
        if (StringUtils.isBlank(super.getName())) {
            super.setName(getNome());
            return true;
        }
        return false;
    }
}
