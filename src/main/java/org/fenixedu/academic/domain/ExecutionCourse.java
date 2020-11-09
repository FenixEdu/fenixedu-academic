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
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.degreeStructure.CompetenceCourseInformation;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicInterval;
import org.fenixedu.academic.dto.GenericPair;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.LocaleUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.signals.DomainObjectEvent;
import org.fenixedu.bennu.core.signals.Signal;
import org.fenixedu.commons.i18n.I18N;
import org.fenixedu.commons.i18n.LocalizedString;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.dml.runtime.RelationAdapter;

public class ExecutionCourse extends ExecutionCourse_Base {
    public static final String CREATED_SIGNAL = "academic.executionCourse.create";
    public static final String EDITED_SIGNAL = "academic.executionCourse.create";
    public static final String ACRONYM_CHANGED_SIGNAL = "academic.executionCourse.acronym.edit";

    public static final Comparator<ExecutionCourse> EXECUTION_COURSE_EXECUTION_PERIOD_COMPARATOR =
            Comparator.comparing(ExecutionCourse::getExecutionInterval);

    public static final Comparator<ExecutionCourse> EXECUTION_COURSE_NAME_COMPARATOR =
            Comparator.comparing(ExecutionCourse::getName, Collator.getInstance()).thenComparing(ExecutionCourse::getExternalId);

    static {
        getRelationCurricularCourseExecutionCourse().addListener(new CurricularCourseExecutionCourseListener());
    }

    public ExecutionCourse(final String name, final String initials, final ExecutionInterval executionInterval) {
        super();

        setRootDomainObject(Bennu.getInstance());

        setNome(name);
        setExecutionPeriod(executionInterval);
        setSigla(initials);

        Signal.emit(ExecutionCourse.CREATED_SIGNAL, new DomainObjectEvent<ExecutionCourse>(this));
    }

    @Deprecated
    public ExecutionCourse(final String nome, final String sigla, final ExecutionInterval executionInterval,
            EntryPhase entryPhase) {
        this(nome, sigla, executionInterval);
    }

    public List<Professorship> responsibleFors() {
        return getProfessorshipsSet().stream().filter(Professorship::getResponsibleFor).collect(Collectors.toList());
    }

    public Attends getAttendsByStudent(final Registration registration) {
        for (final Attends attends : getAttendsSet()) {
            if (attends.getRegistration() == registration) {
                return attends;
            }
        }
        return null;
    }

    public Attends getAttendsByStudent(final Student student) {
        for (final Attends attends : getAttendsSet()) {
            if (attends.isFor(student)) {
                return attends;
            }
        }
        return null;
    }

    // Delete Method
    public void delete() {
        DomainException.throwWhenDeleteBlocked(getDeletionBlockers());
        if (getSender() != null) {
            getSender().getRecipientsSet().clear();
            getSender().delete();
        }

        for (; !getCourseLoadsSet().isEmpty(); getCourseLoadsSet().iterator().next().delete()) {
            ;
        }
        for (; !getProfessorshipsSet().isEmpty(); getProfessorshipsSet().iterator().next().delete()) {
            ;
        }
        for (; !getLessonPlanningsSet().isEmpty(); getLessonPlanningsSet().iterator().next().delete()) {
            ;
        }

        for (; !getAttendsSet().isEmpty(); getAttendsSet().iterator().next().delete()) {
            ;
        }
        for (; !getExecutionCourseLogsSet().isEmpty(); getExecutionCourseLogsSet().iterator().next().delete()) {
            ;
        }

        getAssociatedCurricularCoursesSet().clear();
        getTeacherGroupSet().clear();
        setExecutionPeriod(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    @Override
    protected void checkForDeletionBlockers(Collection<String> blockers) {
        super.checkForDeletionBlockers(blockers);
        if (!getAssociatedSummariesSet().isEmpty()) {
            blockers.add(BundleUtil.getString(Bundle.APPLICATION, "error.execution.course.cant.delete"));
        }
        if (!getAssociatedBibliographicReferencesSet().isEmpty()) {
            blockers.add(BundleUtil.getString(Bundle.APPLICATION, "error.execution.course.cant.delete"));
        }
        if (!getAssociatedEvaluationsSet().isEmpty()) {
            blockers.add(BundleUtil.getString(Bundle.APPLICATION, "error.execution.course.cant.delete"));
        }
        if (!getAssociatedShifts().isEmpty()) {
            blockers.add(BundleUtil.getString(Bundle.APPLICATION, "error.execution.course.cant.delete"));
        }
        if (!getAttendsSet().isEmpty()) {
            blockers.add(BundleUtil.getString(Bundle.APPLICATION, "error.execution.course.cant.delete"));
        }

        for (final Professorship professorship : getProfessorshipsSet()) {
            if (!professorship.isDeletable()) {
                blockers.add(BundleUtil.getString(Bundle.APPLICATION, "error.execution.course.cant.delete"));
            }
        }

        if (!getStudentGroupSet().isEmpty()) {
            blockers.add(BundleUtil.getString(Bundle.APPLICATION,
                    "error.executionCourse.cannotDeleteExecutionCourseUsedInAccessControl"));
        }
        if (!getSpecialCriteriaOverExecutionCourseGroupSet().isEmpty()) {
            blockers.add(BundleUtil.getString(Bundle.APPLICATION,
                    "error.executionCourse.cannotDeleteExecutionCourseUsedInAccessControl"));
        }

    }

    public Set<Shift> getAssociatedShifts() {
        Set<Shift> result = new HashSet<Shift>();
        for (CourseLoad courseLoad : getCourseLoadsSet()) {
            result.addAll(courseLoad.getShiftsSet());
        }
        return result;
    }

    private static class CurricularCourseExecutionCourseListener extends RelationAdapter<ExecutionCourse, CurricularCourse> {

        @Override
        public void afterAdd(ExecutionCourse execution, CurricularCourse curricular) {
            for (final Enrolment enrolment : curricular.getEnrolments()) {
                if (enrolment.getExecutionInterval().equals(execution.getExecutionInterval())) {
                    associateAttend(enrolment, execution);
                }
            }
            fillCourseLoads(execution, curricular);
        }

        @Override
        public void afterRemove(ExecutionCourse execution, CurricularCourse curricular) {
            if (execution != null) {
                for (Attends attends : execution.getAttendsSet()) {
                    if ((attends.getEnrolment() != null) && (attends.getEnrolment().getCurricularCourse().equals(curricular))) {
                        attends.setEnrolment(null);
                    }
                }
            }
        }

        private static void associateAttend(Enrolment enrolment, ExecutionCourse executionCourse) {
            if (!alreadyHasAttend(enrolment, executionCourse.getExecutionInterval())) {
                Attends attends = executionCourse.getAttendsByStudent(enrolment.getStudentCurricularPlan().getRegistration());
                if (attends == null) {
                    attends = new Attends(enrolment.getStudentCurricularPlan().getRegistration(), executionCourse);
                }
                enrolment.addAttends(attends);
            }
        }

        private static boolean alreadyHasAttend(Enrolment enrolment, ExecutionInterval executionInterval) {
            for (Attends attends : enrolment.getAttendsSet()) {
                if (attends.getExecutionCourse().getExecutionInterval().equals(executionInterval)) {
                    return true;
                }
            }
            return false;
        }

        private void fillCourseLoads(ExecutionCourse execution, CurricularCourse curricular) {
            for (ShiftType shiftType : ShiftType.values()) {
                BigDecimal totalHours = curricular.getTotalHoursByShiftType(shiftType, execution.getExecutionInterval());
                if (totalHours != null && totalHours.compareTo(BigDecimal.ZERO) == 1) {
                    CourseLoad courseLoad = execution.getCourseLoadByShiftType(shiftType);
                    if (courseLoad == null) {
                        new CourseLoad(execution, shiftType, null, totalHours);
                    }
                }
            }
        }
    }

    public SortedSet<Degree> getDegreesSortedByDegreeName() {
        final SortedSet<Degree> degrees = new TreeSet<Degree>(Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);
        for (final CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
            final DegreeCurricularPlan degreeCurricularPlan = curricularCourse.getDegreeCurricularPlan();
            degrees.add(degreeCurricularPlan.getDegree());
        }
        return degrees;
    }

    public Set<CompetenceCourse> getCompetenceCourses() {
        final Set<CompetenceCourse> competenceCourses = new HashSet<CompetenceCourse>();
        for (final CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
            final CompetenceCourse competenceCourse = curricularCourse.getCompetenceCourse();
            if (competenceCourse != null) {
                competenceCourses.add(competenceCourse);
            }
        }
        return competenceCourses;
    }

    public Set<CompetenceCourseInformation> getCompetenceCoursesInformations() {
        final Set<CompetenceCourseInformation> competenceCourseInformations = new HashSet<CompetenceCourseInformation>();
        for (final CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
            final CompetenceCourse competenceCourse = curricularCourse.getCompetenceCourse();
            if (competenceCourse != null) {
                final CompetenceCourseInformation competenceCourseInformation =
                        competenceCourse.findInformationMostRecentUntil(getExecutionInterval());
                if (competenceCourseInformation != null) {
                    competenceCourseInformations.add(competenceCourseInformation);
                }
            }
        }
        return competenceCourseInformations;
    }

    public String constructShiftName(final Shift shift, final int n) {
        final String number = n < 10 ? "0" + n : Integer.toString(n);
        StringBuilder typesName = new StringBuilder();
        for (ShiftType shiftType : shift.getSortedTypes()) {
            typesName.append(shiftType.getSiglaTipoAula());
        }
        return getSigla() + typesName.toString() + number;
    }

    public SortedSet<Shift> getShiftsByTypeOrderedByShiftName(final ShiftType shiftType) {
        final SortedSet<Shift> shifts = new TreeSet<Shift>(Shift.SHIFT_COMPARATOR_BY_NAME);
        for (final Shift shift : getAssociatedShifts()) {
            if (shift.containsType(shiftType)) {
                shifts.add(shift);
            }
        }
        return shifts;
    }

    public void setShiftNames() {

        final SortedSet<Shift> shifts = new TreeSet<>(Shift.SHIFT_COMPARATOR_BY_TYPE_AND_ORDERED_LESSONS);
        shifts.addAll(getAssociatedShifts());

        int counter = 0;
        for (final Shift shift : shifts) {
            if (shift.isCustomName()) {
                continue;
            }
            final String name = constructShiftName(shift, ++counter);
            shift.setName(name);
        }
    }

    private Set<SchoolClass> getAllSchoolClassesOrBy(DegreeCurricularPlan degreeCurricularPlan) {
        final Set<SchoolClass> result = new HashSet<SchoolClass>();
        for (final Shift shift : getAssociatedShifts()) {
            for (final SchoolClass schoolClass : shift.getAssociatedClassesSet()) {
                if (degreeCurricularPlan == null
                        || schoolClass.getExecutionDegree().getDegreeCurricularPlan() == degreeCurricularPlan) {
                    result.add(schoolClass);
                }
            }
        }
        return result;
    }

    public Set<SchoolClass> getSchoolClassesBy(DegreeCurricularPlan degreeCurricularPlan) {
        return getAllSchoolClassesOrBy(degreeCurricularPlan);
    }

    public Set<SchoolClass> getSchoolClasses() {
        return getAllSchoolClassesOrBy(null);
    }

    /**
     * @deprecated use {@link LessonPlanning#findOrdered(ExecutionCourse, ShiftType)}
     */
    @Deprecated
    public List<LessonPlanning> getLessonPlanningsOrderedByOrder(ShiftType lessonType) {
        return LessonPlanning.findOrdered(this, lessonType);
    }

    public Set<ShiftType> getShiftTypes() {
        Set<ShiftType> shiftTypes = new TreeSet<ShiftType>();
        for (CourseLoad courseLoad : getCourseLoadsSet()) {
            shiftTypes.add(courseLoad.getType());
        }
        return shiftTypes;
    }

    @Override
    public String getNome() {
        if (I18N.getLocale().getLanguage().equals(Locale.ENGLISH.getLanguage())) {
            return getNameEn();
        }
        return super.getNome();
    }

    private String getNameEn() {
        return getCompetenceCoursesInformations().stream().map(cci -> cci.getNameEn()).distinct().sorted()
                .collect(Collectors.joining(" / "));
    }

    public String getName() {
        return getNome();
    }

    public String getCode() {
        return getCompetenceCourses().stream().map(cc -> cc.getCode()).distinct().collect(Collectors.joining(", "));
    }

    public String getPrettyAcronym() {
        return getSigla().replaceAll("[0-9]", "");
    }

    public String getDegreePresentationString() {
        return getAssociatedCurricularCoursesSet().stream().map(cc -> cc.getDegree().getSigla()).distinct().sorted()
                .collect(Collectors.joining(", "));
    }

    public ExecutionYear getExecutionYear() {
        return getExecutionInterval().getExecutionYear();
    }

    public SortedSet<BibliographicReference> getOrderedBibliographicReferences() {
        TreeSet<BibliographicReference> references =
                new TreeSet<BibliographicReference>(BibliographicReference.COMPARATOR_BY_ORDER);
        references.addAll(getAssociatedBibliographicReferencesSet());
        return references;
    }

    public Set<ExecutionDegree> getExecutionDegrees() {
        Set<ExecutionDegree> result = new HashSet<ExecutionDegree>();
        for (CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
            ExecutionDegree executionDegree =
                    curricularCourse.getDegreeCurricularPlan().getExecutionDegreeByYear(getExecutionYear());
            if (executionDegree != null) {
                result.add(executionDegree);
            }
        }
        return result;
    }

    public Interval getMaxLessonsInterval() {

        final Collection<Interval> allIntervals =
                getAssociatedCurricularCoursesSet().stream().map(cc -> cc.getExecutionDegreeFor(getExecutionYear()))
                        .filter(Objects::nonNull).flatMap(ed -> ed.getPeriodLessons(getExecutionInterval()).stream())
                        .map(p -> p.getIntervalWithNextPeriods()).collect(Collectors.toSet());

        if (!allIntervals.isEmpty()) {
            final DateTime start = allIntervals.stream().map(i -> i.getStart()).min(Comparator.naturalOrder()).get();
            final DateTime end = allIntervals.stream().map(i -> i.getEnd()).max(Comparator.naturalOrder()).get();
            return new Interval(start, end);
        }

        return null;
    }

    /**
     * @deprecated use {@link #getMaxLessonsInterval()}
     * 
     *             Attention: still used in deprecated JSPs
     * 
     */
    @Deprecated
    public GenericPair<YearMonthDay, YearMonthDay> getMaxLessonsPeriod() {

        YearMonthDay minBeginDate = null;
        YearMonthDay maxEndDate = null;

        for (final CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
            final ExecutionDegree executionDegree = curricularCourse.getExecutionDegreeFor(getExecutionYear());

            for (final OccupationPeriod period : executionDegree.getPeriodLessons(getExecutionInterval())) {
                if (minBeginDate == null || minBeginDate.isAfter(period.getStartYearMonthDay())) {
                    minBeginDate = period.getStartYearMonthDay();
                }
                if (maxEndDate == null || maxEndDate.isBefore(period.getEndYearMonthDayWithNextPeriods())) {
                    maxEndDate = period.getEndYearMonthDayWithNextPeriods();
                }
            }
        }

        if (minBeginDate != null && maxEndDate != null) {
            return new GenericPair<YearMonthDay, YearMonthDay>(minBeginDate, maxEndDate);
        }

        return null;
    }

    public CourseLoad getCourseLoadByShiftType(ShiftType type) {
        if (type != null) {
            for (CourseLoad courseLoad : getCourseLoadsSet()) {
                if (courseLoad.getType().equals(type)) {
                    return courseLoad;
                }
            }
        }
        return null;
    }

    public AcademicInterval getAcademicInterval() {
        return getExecutionInterval().getAcademicInterval();
    }

    @Override
    public void setSigla(String sigla) {
        final String code = sigla.replace(' ', '_').replace('/', '-');
        final String uniqueCode = findUniqueCode(code);
        if (uniqueCode.equals(this.getSigla())) {
            return;
        }
        super.setSigla(uniqueCode);
        Signal.emit(ExecutionCourse.ACRONYM_CHANGED_SIGNAL, new DomainObjectEvent<>(this));
    }

    private String findUniqueCode(final String code) {
        if (!existsMatchingCode(code)) {
            return code;
        }
        int c;
        for (c = 0; existsMatchingCode(code + "-" + c); c++) {
            ;
        }
        return code + "-" + c;
    }

    private boolean existsMatchingCode(final String code) {
        for (final ExecutionCourse executionCourse : getExecutionInterval().getAssociatedExecutionCoursesSet()) {
            if (executionCourse != this && executionCourse.getSigla().equalsIgnoreCase(code)) {
                return true;
            }
        }
        return false;
    }

    public Collection<DegreeCurricularPlan> getAssociatedDegreeCurricularPlans() {
        Collection<DegreeCurricularPlan> result = new HashSet<DegreeCurricularPlan>();
        for (CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
            result.add(curricularCourse.getDegreeCurricularPlan());
        }
        return result;
    }

    public boolean isDeletable() {
        return getDeletionBlockers().isEmpty();
    }

    public Professorship getProfessorship(final Person person) {
        for (final Professorship professorship : getProfessorshipsSet()) {
            if (professorship.getPerson() == person) {
                return professorship;
            }
        }
        return null;
    }

    public boolean isHasSender() {
        return getSender() != null;
    }

    /*
     * This method returns the portuguese name and the english name
     */
    public LocalizedString getNameI18N() {
        LocalizedString nameI18N = new LocalizedString();
        nameI18N = nameI18N.with(LocaleUtils.PT, super.getNome());
        nameI18N = nameI18N.with(LocaleUtils.EN, getNameEn());
        return nameI18N;
    }

    public Professorship getProfessorshipForCurrentUser() {
        return this.getProfessorship(AccessControl.getPerson());
    }

    @Override
    public void addAssociatedCurricularCourses(final CurricularCourse curricularCourse) {
        Collection<ExecutionCourse> executionCourses = curricularCourse.getAssociatedExecutionCoursesSet();

        for (ExecutionCourse executionCourse : executionCourses) {
            if (this != executionCourse && executionCourse.getExecutionInterval() == getExecutionInterval()) {
                throw new DomainException("error.executionCourse.curricularCourse.already.associated");
            }
        }

        super.addAssociatedCurricularCourses(curricularCourse);
    }

    public ExecutionInterval getExecutionInterval() {
        return super.getExecutionPeriod();
    }

}
