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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.degreeStructure.BibliographicReferences;
import org.fenixedu.academic.domain.degreeStructure.BibliographicReferences.BibliographicReferenceType;
import org.fenixedu.academic.domain.degreeStructure.CompetenceCourseInformation;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.messaging.ExecutionCourseForum;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicInterval;
import org.fenixedu.academic.dto.GenericPair;
import org.fenixedu.academic.dto.teacher.executionCourse.SearchExecutionCourseAttendsBean;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.DateFormatUtil;
import org.fenixedu.academic.util.LocaleUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.signals.DomainObjectEvent;
import org.fenixedu.bennu.core.signals.Signal;
import org.fenixedu.commons.StringNormalizer;
import org.fenixedu.commons.i18n.I18N;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.dml.runtime.RelationAdapter;

public class ExecutionCourse extends ExecutionCourse_Base {
    public static final String CREATED_SIGNAL = "academic.executionCourse.create";
    public static final String EDITED_SIGNAL = "academic.executionCourse.create";
    public static final String ACRONYM_CHANGED_SIGNAL = "academic.executionCourse.acronym.edit";

    public static List<ExecutionCourse> readNotEmptyExecutionCourses() {
        return new ArrayList<ExecutionCourse>(Bennu.getInstance().getExecutionCoursesSet());
    }

    public static final Comparator<ExecutionCourse> EXECUTION_COURSE_EXECUTION_PERIOD_COMPARATOR =
            new Comparator<ExecutionCourse>() {

                @Override
                public int compare(ExecutionCourse o1, ExecutionCourse o2) {
                    return o1.getExecutionInterval().compareTo(o2.getExecutionInterval());
                }

            };

    public static final Comparator<ExecutionCourse> EXECUTION_COURSE_NAME_COMPARATOR = new Comparator<ExecutionCourse>() {

        @Override
        public int compare(ExecutionCourse o1, ExecutionCourse o2) {
            final int c = Collator.getInstance().compare(o1.getNome(), o2.getNome());
            return c == 0 ? DomainObjectUtil.COMPARATOR_BY_ID.compare(o1, o2) : c;
        }

    };

    public static final Comparator<ExecutionCourse> EXECUTION_COURSE_COMPARATOR_BY_EXECUTION_PERIOD_AND_NAME =
            new Comparator<ExecutionCourse>() {

                @Override
                public int compare(ExecutionCourse o1, ExecutionCourse o2) {
                    final int cep = EXECUTION_COURSE_EXECUTION_PERIOD_COMPARATOR.compare(o1, o2);
                    if (cep != 0) {
                        return cep;
                    }
                    final int c = EXECUTION_COURSE_NAME_COMPARATOR.compare(o1, o2);
                    return c == 0 ? DomainObjectUtil.COMPARATOR_BY_ID.compare(o1, o2) : c;
                }

            };

    public static final Comparator<ExecutionCourse> EXECUTION_COURSE_COMPARATOR_BY_EXECUTION_PERIOD_REVERSED_AND_NAME =
            new Comparator<ExecutionCourse>() {

                @Override
                public int compare(ExecutionCourse o1, ExecutionCourse o2) {
                    final int cep = EXECUTION_COURSE_EXECUTION_PERIOD_COMPARATOR.compare(o2, o1);
                    if (cep != 0) {
                        return cep;
                    }
                    final int c = EXECUTION_COURSE_NAME_COMPARATOR.compare(o1, o2);
                    return c == 0 ? DomainObjectUtil.COMPARATOR_BY_ID.compare(o1, o2) : c;
                }

            };

    static {
        getRelationCurricularCourseExecutionCourse().addListener(new CurricularCourseExecutionCourseListener());

        getRelationCurricularCourseExecutionCourse().addListener(new RelationAdapter<ExecutionCourse, CurricularCourse>() {

            @Override
            public void beforeAdd(final ExecutionCourse executionCourse, final CurricularCourse curricularCourse) {
                if (executionCourse != null && curricularCourse != null
                        && executionCourse.getAssociatedCurricularCoursesSet().size() == 0) {
                    ExecutionCourse previous = null;
                    for (final ExecutionCourse otherExecutionCourse : curricularCourse.getAssociatedExecutionCoursesSet()) {
                        if (previous == null
                                || otherExecutionCourse.getExecutionInterval().isAfter(previous.getExecutionInterval())) {
                            previous = otherExecutionCourse;
                        }
                    }
                    if (previous != null) {
                        executionCourse.setProjectTutorialCourse(previous.getProjectTutorialCourse());
                    }
                }
            }

        });
    }

    public ExecutionCourse(final String nome, final String sigla, final ExecutionInterval executionInterval,
            EntryPhase entryPhase) {
        super();

        setRootDomainObject(Bennu.getInstance());
        setAvailableGradeSubmission(Boolean.TRUE);

        setNome(nome);
        setExecutionPeriod(executionInterval);
        setSigla(sigla);
        setComment("");

        if (entryPhase == null) {
            entryPhase = EntryPhase.FIRST_PHASE;
        }
        setEntryPhase(entryPhase);
        setProjectTutorialCourse(Boolean.FALSE);
        setUnitCreditValue(null);

        ExecutionCourseForum forum = new ExecutionCourseForum();
        forum.setName(getNameI18N());
        addForum(forum);

        Signal.emit(ExecutionCourse.CREATED_SIGNAL, new DomainObjectEvent<ExecutionCourse>(this));
    }

    public void editInformation(String nome, String sigla, String comment, Boolean availableGradeSubmission,
            EntryPhase entryPhase) {
        setNome(nome);
        setSigla(sigla);
        setComment(comment);
        setAvailableGradeSubmission(availableGradeSubmission);
        if (entryPhase != null) {
            setEntryPhase(entryPhase);
        }
        Signal.emit(ExecutionCourse.EDITED_SIGNAL, new DomainObjectEvent<ExecutionCourse>(this));

    }

    public void editCourseLoad(ShiftType type, BigDecimal unitQuantity, BigDecimal totalQuantity) {
        CourseLoad courseLoad = getCourseLoadByShiftType(type);
        if (courseLoad == null) {
            new CourseLoad(this, type, unitQuantity, totalQuantity);
        } else {
            courseLoad.edit(unitQuantity, totalQuantity);
        }
    }

    public void createBibliographicReference(final String title, final String authors, final String reference, final String year,
            final Boolean optional) {
        if (title == null || authors == null || reference == null || year == null || optional == null) {
            throw new NullPointerException();
        }

        final BibliographicReference bibliographicReference = new BibliographicReference();
        bibliographicReference.setTitle(title);
        bibliographicReference.setAuthors(authors);
        bibliographicReference.setReference(reference);
        bibliographicReference.setYear(year);
        bibliographicReference.setOptional(optional);
        bibliographicReference.setExecutionCourse(this);

        final String type;
        if (optional) {
            type = BundleUtil.getString(Bundle.APPLICATION, "option.bibliographicReference.optional");
        } else {
            type = BundleUtil.getString(Bundle.APPLICATION, "option.bibliographicReference.recommended");
        }
        CurricularManagementLog.createLog(this, Bundle.MESSAGING, "log.executionCourse.curricular.bibliographic.created", type,
                title, this.getName(), this.getDegreePresentationString());
    }

    public List<BibliographicReference> copyBibliographicReferencesFrom(final ExecutionCourse executionCourseFrom) {
        final List<BibliographicReference> notCopiedBibliographicReferences = new ArrayList<BibliographicReference>();

        for (final BibliographicReference bibliographicReference : executionCourseFrom
                .getAssociatedBibliographicReferencesSet()) {
            if (canAddBibliographicReference(bibliographicReference)) {
                this.createBibliographicReference(bibliographicReference.getTitle(), bibliographicReference.getAuthors(),
                        bibliographicReference.getReference(), bibliographicReference.getYear(),
                        bibliographicReference.getOptional());
            } else {
                notCopiedBibliographicReferences.add(bibliographicReference);
            }
        }

        return notCopiedBibliographicReferences;
    }

    private boolean canAddBibliographicReference(final BibliographicReference bibliographicReferenceToAdd) {
        for (final BibliographicReference bibliographicReference : this.getAssociatedBibliographicReferencesSet()) {
            if (bibliographicReference.getTitle().equals(bibliographicReferenceToAdd.getTitle())) {
                return false;
            }
        }
        return true;
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
        for (; !getForuns().isEmpty(); getForuns().iterator().next().delete()) {
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

        for (ExecutionCourseForum forum : getForuns()) {
            if (forum.getConversationThreadSet().size() != 0) {
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

    private int countAssociatedStudentsByEnrolmentNumber(int enrolmentNumber) {
        int executionCourseAssociatedStudents = 0;
        ExecutionInterval courseExecutionPeriod = getExecutionInterval();

        for (CurricularCourse curricularCourseFromExecutionCourseEntry : getAssociatedCurricularCoursesSet()) {
            for (Enrolment enrolment : curricularCourseFromExecutionCourseEntry.getEnrolments()) {

                if (enrolment.getExecutionInterval() == courseExecutionPeriod) {

                    StudentCurricularPlan studentCurricularPlanEntry = enrolment.getStudentCurricularPlan();
                    int numberOfEnrolmentsForThatExecutionCourse = 0;

                    for (Enrolment enrolmentsFromStudentCPEntry : studentCurricularPlanEntry.getEnrolmentsSet()) {
                        if (enrolmentsFromStudentCPEntry.getCurricularCourse() == curricularCourseFromExecutionCourseEntry
                                && (enrolmentsFromStudentCPEntry.getExecutionInterval().compareTo(courseExecutionPeriod) <= 0)) {
                            ++numberOfEnrolmentsForThatExecutionCourse;
                            if (numberOfEnrolmentsForThatExecutionCourse > enrolmentNumber) {
                                break;
                            }
                        }
                    }

                    if (numberOfEnrolmentsForThatExecutionCourse == enrolmentNumber) {
                        executionCourseAssociatedStudents++;
                    }
                }
            }
        }

        return executionCourseAssociatedStudents;
    }

    public Integer getTotalEnrolmentStudentNumber() {
        int executionCourseStudentNumber = 0;
        for (final CurricularCourse curricularCourseFromExecutionCourseEntry : getAssociatedCurricularCoursesSet()) {
            for (final Enrolment enrolment : curricularCourseFromExecutionCourseEntry.getEnrolments()) {
                if (enrolment.getExecutionInterval() == getExecutionInterval()) {
                    executionCourseStudentNumber++;
                }
            }
        }
        return executionCourseStudentNumber;
    }

    public Integer getFirstTimeEnrolmentStudentNumber() {
        return countAssociatedStudentsByEnrolmentNumber(1);
    }

    public Integer getSecondOrMoreTimeEnrolmentStudentNumber() {
        return getTotalEnrolmentStudentNumber() - getFirstTimeEnrolmentStudentNumber();
    }

    public Duration getTotalShiftsDuration() {
        Duration totalDuration = Duration.ZERO;
        for (Shift shift : getAssociatedShifts()) {
            totalDuration = totalDuration.plus(shift.getTotalDuration());
        }
        return totalDuration;
    }

    public BigDecimal getAllShiftUnitHours(ShiftType shiftType) {
        BigDecimal totalTime = BigDecimal.ZERO;
        for (Shift shift : getAssociatedShifts()) {
            if (shift.containsType(shiftType)) {
                totalTime = totalTime.add(shift.getUnitHours());
            }
        }
        return totalTime;
    }

    public BigDecimal getWeeklyCourseLoadTotalQuantityByShiftType(ShiftType type) {
        CourseLoad courseLoad = getCourseLoadByShiftType(type);
        return courseLoad != null ? courseLoad.getWeeklyHours() : BigDecimal.ZERO;
    }

    public Set<Shift> getAssociatedShifts() {
        Set<Shift> result = new HashSet<Shift>();
        for (CourseLoad courseLoad : getCourseLoadsSet()) {
            result.addAll(courseLoad.getShiftsSet());
        }
        return result;
    }

    public Set<LessonInstance> getAssociatedLessonInstances() {
        Set<LessonInstance> result = new HashSet<LessonInstance>();
        for (CourseLoad courseLoad : getCourseLoadsSet()) {
            result.addAll(courseLoad.getLessonInstancesSet());
        }

        return result;
    }

    public List<Enrolment> getActiveEnrollments() {
        List<Enrolment> results = new ArrayList<Enrolment>();

        for (CurricularCourse curricularCourse : this.getAssociatedCurricularCoursesSet()) {
            List<Enrolment> enrollments =
                    curricularCourse.getEnrolmentsByAcademicInterval(this.getExecutionInterval().getAcademicInterval());

            results.addAll(enrollments);
        }
        return results;
    }

    public static final Comparator<Evaluation> EVALUATION_COMPARATOR = new Comparator<Evaluation>() {

        @Override
        public int compare(Evaluation evaluation1, Evaluation evaluation2) {
            final String evaluation1ComparisonString = evaluationComparisonString(evaluation1);
            final String evaluation2ComparisonString = evaluationComparisonString(evaluation2);
            return evaluation1ComparisonString.compareTo(evaluation2ComparisonString);
        }

        private String evaluationComparisonString(final Evaluation evaluation) {
            return evaluation.getEvaluationDate() != null ? DateFormatUtil.format("yyyy/MM/dd", evaluation.getEvaluationDate())
                    + evaluation.getExternalId() : evaluation.getExternalId();
        }
    };

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

    public void createForum(LocalizedString name, LocalizedString description) {
        if (hasForumWithName(name)) {
            throw new DomainException("executionCourse.already.existing.forum");
        }
        this.addForum(new ExecutionCourseForum(name, description));
    }

    @Override
    public void addForum(ExecutionCourseForum executionCourseForum) {
        checkIfCanAddForum(executionCourseForum.getNormalizedName());
        super.addForum(executionCourseForum);
    }

    public void checkIfCanAddForum(LocalizedString name) {
        if (hasForumWithName(name)) {
            throw new DomainException("executionCourse.already.existing.forum");
        }
    }

    public boolean hasForumWithName(LocalizedString name) {
        return getForumByName(name) != null;
    }

    public ExecutionCourseForum getForumByName(LocalizedString name) {
        for (final ExecutionCourseForum executionCourseForum : getForuns()) {
            if (LocaleUtils.equalInAnyLanguage(executionCourseForum.getNormalizedName(), name)) {
                return executionCourseForum;
            }
        }

        return null;
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
            shift.setNome(name);
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

    public Set<Lesson> getLessons() {
        final Set<Lesson> lessons = new HashSet<Lesson>();
        for (final Shift shift : getAssociatedShifts()) {
            lessons.addAll(shift.getAssociatedLessonsSet());
        }
        return lessons;
    }

    public SortedSet<Shift> getShiftsOrderedByLessons() {
        final SortedSet<Shift> shifts = new TreeSet<Shift>(Shift.SHIFT_COMPARATOR_BY_TYPE_AND_ORDERED_LESSONS);
        shifts.addAll(getAssociatedShifts());
        return shifts;
    }

    public Map<CompetenceCourse, Set<CurricularCourse>> getCurricularCoursesIndexedByCompetenceCourse() {
        final Map<CompetenceCourse, Set<CurricularCourse>> curricularCourseMap =
                new HashMap<CompetenceCourse, Set<CurricularCourse>>();
        for (final CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
            if (curricularCourse.isBolonhaDegree()) {
                final CompetenceCourse competenceCourse = curricularCourse.getCompetenceCourse();
                if (competenceCourse != null) {
                    final Set<CurricularCourse> curricularCourses;
                    if (curricularCourseMap.containsKey(competenceCourse)) {
                        curricularCourses = curricularCourseMap.get(competenceCourse);
                    } else {
                        curricularCourses =
                                new TreeSet<CurricularCourse>(CurricularCourse.CURRICULAR_COURSE_COMPARATOR_BY_DEGREE_AND_NAME);
                        curricularCourseMap.put(competenceCourse, curricularCourses);
                    }
                    curricularCourses.add(curricularCourse);
                }
            }
        }
        return curricularCourseMap;
    }

    public boolean getHasAnySecondaryBibliographicReference() {
        return hasAnyBibliographicReferenceByBibliographicReferenceType(BibliographicReferenceType.SECONDARY);
    }

    public boolean getHasAnyMainBibliographicReference() {
        return hasAnyBibliographicReferenceByBibliographicReferenceType(BibliographicReferenceType.MAIN);
    }

    private boolean hasAnyBibliographicReferenceByBibliographicReferenceType(BibliographicReferenceType referenceType) {
        for (final BibliographicReference bibliographicReference : getAssociatedBibliographicReferencesSet()) {
            if ((referenceType.equals(BibliographicReferenceType.SECONDARY)
                    && bibliographicReference.getOptional().booleanValue())
                    || (referenceType.equals(BibliographicReferenceType.MAIN)
                            && !bibliographicReference.getOptional().booleanValue())) {
                return true;
            }
        }
        for (final CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
            final CompetenceCourse competenceCourse = curricularCourse.getCompetenceCourse();
            if (competenceCourse != null) {
                final CompetenceCourseInformation competenceCourseInformation =
                        competenceCourse.findInformationMostRecentUntil(getExecutionInterval());
                if (competenceCourseInformation != null) {
                    final org.fenixedu.academic.domain.degreeStructure.BibliographicReferences bibliographicReferences =
                            competenceCourseInformation.getBibliographicReferences();
                    if (bibliographicReferences != null) {
                        for (final org.fenixedu.academic.domain.degreeStructure.BibliographicReferences.BibliographicReference bibliographicReference : bibliographicReferences
                                .getBibliographicReferencesList()) {
                            if (bibliographicReference.getType() == referenceType) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public List<LessonPlanning> getLessonPlanningsOrderedByOrder(ShiftType lessonType) {
        final List<LessonPlanning> lessonPlannings = new ArrayList<LessonPlanning>();
        for (LessonPlanning planning : getLessonPlanningsSet()) {
            if (planning.getLessonType().equals(lessonType)) {
                lessonPlannings.add(planning);
            }
        }
        Collections.sort(lessonPlannings, LessonPlanning.COMPARATOR_BY_ORDER);
        return lessonPlannings;
    }

    public LessonPlanning getLessonPlanning(ShiftType lessonType, Integer order) {
        for (LessonPlanning planning : getLessonPlanningsSet()) {
            if (planning.getLessonType().equals(lessonType) && planning.getOrderOfPlanning().equals(order)) {
                return planning;
            }
        }
        return null;
    }

    public Set<ShiftType> getShiftTypes() {
        Set<ShiftType> shiftTypes = new TreeSet<ShiftType>();
        for (CourseLoad courseLoad : getCourseLoadsSet()) {
            shiftTypes.add(courseLoad.getType());
        }
        return shiftTypes;
    }

    public void copyLessonPlanningsFrom(ExecutionCourse executionCourseFrom) {
        Set<ShiftType> shiftTypes = getShiftTypes();
        for (ShiftType shiftType : executionCourseFrom.getShiftTypes()) {
            if (shiftTypes.contains(shiftType)) {
                List<LessonPlanning> lessonPlanningsFrom = executionCourseFrom.getLessonPlanningsOrderedByOrder(shiftType);
                if (!lessonPlanningsFrom.isEmpty()) {
                    for (LessonPlanning planning : lessonPlanningsFrom) {
                        new LessonPlanning(planning.getTitle(), planning.getPlanning(), planning.getLessonType(), this);
                    }
                }
            }
        }
    }

    /**
     * Tells if all the associated Curricular Courses load are the same
     */
    public String getEqualLoad() {
        for (final CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
            for (ShiftType type : ShiftType.values()) {
                if (!getEqualLoad(type, curricularCourse)) {
                    return Boolean.FALSE.toString();
                }
            }
        }
        return Boolean.TRUE.toString();
    }

    public boolean getEqualLoad(ShiftType type, CurricularCourse curricularCourse) {
        if (type != null) {
            if (type.equals(ShiftType.DUVIDAS) || type.equals(ShiftType.RESERVA)) {
                return true;
            }
            BigDecimal ccTotalHours = curricularCourse.getTotalHoursByShiftType(type, getExecutionInterval());
            CourseLoad courseLoad = getCourseLoadByShiftType(type);
            if ((courseLoad == null && ccTotalHours == null)
                    || (courseLoad == null && ccTotalHours != null && ccTotalHours.compareTo(BigDecimal.ZERO) == 0)
                    || (courseLoad != null && ccTotalHours != null
                            && courseLoad.getTotalQuantity().compareTo(ccTotalHours) == 0)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getNome() {
        if (I18N.getLocale().getLanguage().equals("en") && !getAssociatedCurricularCoursesSet().isEmpty()) {
            final StringBuilder stringBuilder = new StringBuilder();

            final Set<String> names = new HashSet<String>();

            for (final CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
                if (curricularCourse.isActive(getExecutionInterval())) {
                    final String name = curricularCourse.getNameEn();
                    if (!names.contains(name)) {
                        names.add(name);
                        if (stringBuilder.length() > 0) {
                            stringBuilder.append(" / ");
                        }
                        stringBuilder.append(name);
                    }
                }
            }

            if (stringBuilder.length() > 0) {
                return stringBuilder.toString();
            }

            boolean unique = true;
            final String nameEn = getAssociatedCurricularCoursesSet().iterator().next().getNameEn();

            for (final CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
                if (curricularCourse.getNameEn() == null || !curricularCourse.getNameEn().equals(nameEn)) {
                    unique = false;
                    break;
                }
            }

            if (unique) {
                return nameEn;
            } else {
                return super.getNome();
            }
        }
        return super.getNome();
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
        SortedSet<Degree> degrees = this.getDegreesSortedByDegreeName();
        String result = "";
        int i = 0;
        for (Degree degree : degrees) {
            if (i > 0) {
                result += ", ";
            }
            result += degree.getSigla();
            i++;
        }
        return result;
    }

    public ExecutionYear getExecutionYear() {
        return getExecutionInterval().getExecutionYear();
    }

    public CurricularCourse getCurricularCourseFor(final DegreeCurricularPlan degreeCurricularPlan) {
        for (final CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
            if (curricularCourse.getDegreeCurricularPlan() == degreeCurricularPlan) {
                return curricularCourse;
            }
        }

        return null;
    }

    public SortedSet<BibliographicReference> getOrderedBibliographicReferences() {
        TreeSet<BibliographicReference> references =
                new TreeSet<BibliographicReference>(BibliographicReference.COMPARATOR_BY_ORDER);
        references.addAll(getAssociatedBibliographicReferencesSet());
        return references;
    }

    public void setBibliographicReferencesOrder(List<BibliographicReference> references) {
        for (int i = 0; i < references.size(); i++) {
            references.get(i).setReferenceOrder(i);
        }
    }

    public List<BibliographicReference> getMainBibliographicReferences() {
        List<BibliographicReference> references = new ArrayList<BibliographicReference>();

        for (BibliographicReference reference : getAssociatedBibliographicReferencesSet()) {
            if (!reference.isOptional()) {
                references.add(reference);
            }
        }

        return references;
    }

    public List<BibliographicReference> getSecondaryBibliographicReferences() {
        List<BibliographicReference> references = new ArrayList<BibliographicReference>();

        for (BibliographicReference reference : getAssociatedBibliographicReferencesSet()) {
            if (reference.isOptional()) {
                references.add(reference);
            }
        }

        return references;
    }

    public boolean isCompentenceCourseMainBibliographyAvailable() {
        for (CompetenceCourseInformation information : getCompetenceCoursesInformations()) {
            BibliographicReferences bibliographicReferences = information.getBibliographicReferences();
            if (bibliographicReferences != null && !bibliographicReferences.getMainBibliographicReferences().isEmpty()) {
                return true;
            }
        }

        return false;
    }

    public boolean isCompentenceCourseSecondaryBibliographyAvailable() {
        for (CompetenceCourseInformation information : getCompetenceCoursesInformations()) {
            BibliographicReferences bibliographicReferences = information.getBibliographicReferences();
            if (bibliographicReferences != null && !bibliographicReferences.getSecondaryBibliographicReferences().isEmpty()) {
                return true;
            }
        }

        return false;
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

    @Override
    public Boolean getAvailableGradeSubmission() {
        if (super.getAvailableGradeSubmission() != null) {
            return super.getAvailableGradeSubmission();
        }
        return Boolean.TRUE;
    }

    @Override
    public void setUnitCreditValue(BigDecimal unitCreditValue) {
        setUnitCreditValue(unitCreditValue, getUnitCreditValueNotes());
    }

    public void setUnitCreditValue(BigDecimal unitCreditValue, String justification) {
        if (unitCreditValue != null
                && (unitCreditValue.compareTo(BigDecimal.ZERO) < 0 || unitCreditValue.compareTo(BigDecimal.ONE) > 0)) {
            throw new DomainException("error.executionCourse.unitCreditValue.range");
        }
        if (unitCreditValue != null && unitCreditValue.compareTo(BigDecimal.ZERO) != 0 && getEffortRate() == null) {
            throw new DomainException("error.executionCourse.unitCreditValue.noEffortRate");
        }
        if (getEffortRate() != null && (unitCreditValue != null
                && unitCreditValue.compareTo(BigDecimal.valueOf(Math.min(getEffortRate().doubleValue(), 1.0))) < 0
                && StringUtils.isBlank(justification))) {
            throw new DomainException("error.executionCourse.unitCreditValue.lower.effortRate.withoutJustification");
        }
        super.setUnitCreditValueNotes(justification);
        super.setUnitCreditValue(unitCreditValue);
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

    public Map<ShiftType, CourseLoad> getCourseLoadsMap() {
        Map<ShiftType, CourseLoad> result = new HashMap<ShiftType, CourseLoad>();
        Collection<CourseLoad> courseLoads = getCourseLoadsSet();
        for (CourseLoad courseLoad : courseLoads) {
            result.put(courseLoad.getType(), courseLoad);
        }
        return result;
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

    public Set<ExecutionCourseForum> getForuns() {
        return getForumSet();
    }

    public AcademicInterval getAcademicInterval() {
        return getExecutionInterval().getAcademicInterval();
    }

    public static ExecutionCourse readBySiglaAndExecutionPeriod(final String sigla, ExecutionInterval executionInterval) {
        for (ExecutionCourse executionCourse : executionInterval.getAssociatedExecutionCoursesSet()) {
            if (sigla.equalsIgnoreCase(executionCourse.getSigla())) {
                return executionCourse;
            }
        }
        return null;
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

    public boolean functionsAt(final Space campus) {
        final ExecutionYear executionYear = getExecutionYear();
        for (final CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
            final DegreeCurricularPlan degreeCurricularPlan = curricularCourse.getDegreeCurricularPlan();
            for (final ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegreesSet()) {
                if (executionDegree.getCampus() == campus && executionDegree.getExecutionYear() == executionYear) {
                    return true;
                }
            }
        }
        return false;
    }

    public Set<DegreeCurricularPlan> getAttendsDegreeCurricularPlans() {
        final Set<DegreeCurricularPlan> dcps = new HashSet<DegreeCurricularPlan>();
        for (final Attends attends : this.getAttendsSet()) {
            dcps.add(attends.getStudentCurricularPlanFromAttends().getDegreeCurricularPlan());
        }
        return dcps;
    }

    public void searchAttends(SearchExecutionCourseAttendsBean attendsBean) {
        final Predicate<Attends> filter = attendsBean.getFilters();
        final Collection<Attends> validAttends = new HashSet<Attends>();
        final Map<Integer, Integer> enrolmentNumberMap = new HashMap<Integer, Integer>();
        for (final Attends attends : getAttendsSet()) {
            if (filter.test(attends)) {
                validAttends.add(attends);
                addAttendsToEnrolmentNumberMap(attends, enrolmentNumberMap);
            }
        }
        attendsBean.setAttendsResult(validAttends);
        attendsBean.setEnrolmentsNumberMap(enrolmentNumberMap);
    }

    public void addAttendsToEnrolmentNumberMap(final Attends attends, Map<Integer, Integer> enrolmentNumberMap) {
        Integer enrolmentsNumber;
        if (attends.getEnrolment() == null) {
            enrolmentsNumber = 0;
        } else {
            enrolmentsNumber =
                    attends.getEnrolment().getNumberOfTotalEnrolmentsInThisCourse(attends.getEnrolment().getExecutionInterval());
        }

        Integer mapValue = enrolmentNumberMap.get(enrolmentsNumber);
        if (mapValue == null) {
            mapValue = 1;
        } else {
            mapValue += 1;
        }
        enrolmentNumberMap.put(enrolmentsNumber, mapValue);
    }

    public Collection<DegreeCurricularPlan> getAssociatedDegreeCurricularPlans() {
        Collection<DegreeCurricularPlan> result = new HashSet<DegreeCurricularPlan>();
        for (CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
            result.add(curricularCourse.getDegreeCurricularPlan());
        }
        return result;
    }

    public static List<ExecutionCourse> filterByAcademicIntervalAndDegreeCurricularPlanAndCurricularYearAndName(
            AcademicInterval academicInterval, DegreeCurricularPlan degreeCurricularPlan, CurricularYear curricularYear,
            String name) {

        ExecutionInterval executionInterval = ExecutionInterval.getExecutionInterval(academicInterval);

        return executionInterval == null ? Collections.EMPTY_LIST : getExecutionCoursesByDegreeCurricularPlanAndSemesterAndCurricularYearAndName(
                executionInterval, degreeCurricularPlan, curricularYear, name);
    }

    public static Collection<ExecutionCourse> filterByAcademicInterval(AcademicInterval academicInterval) {
        ExecutionInterval executionInterval = ExecutionInterval.getExecutionInterval(academicInterval);

        return executionInterval == null ? Collections.<ExecutionCourse> emptyList() : executionInterval
                .getAssociatedExecutionCoursesSet();
    }

    public static ExecutionCourse getExecutionCourseByInitials(AcademicInterval academicInterval, String courseInitials) {

        ExecutionInterval executionInterval = ExecutionInterval.getExecutionInterval(academicInterval);
        return readBySiglaAndExecutionPeriod(courseInitials, executionInterval);
    }

    public static List<ExecutionCourse> searchByAcademicIntervalAndExecutionDegreeYearAndName(AcademicInterval academicInterval,
            ExecutionDegree executionDegree, CurricularYear curricularYear, String name) {

        ExecutionInterval executionInterval = ExecutionInterval.getExecutionInterval(academicInterval);

        return getExecutionCoursesByDegreeCurricularPlanAndSemesterAndCurricularYearAndName(executionInterval,
                executionDegree.getDegreeCurricularPlan(), curricularYear, name);
    }

    public static List<ExecutionCourse> getExecutionCoursesByDegreeCurricularPlanAndSemesterAndCurricularYearAndName(
            final ExecutionInterval interval, final DegreeCurricularPlan degreeCurricularPlan,
            final CurricularYear curricularYear, final String name) {

        final String normalizedName = (name != null) ? StringNormalizer.normalize(name).replaceAll("%", ".*") : null;
        final List<ExecutionCourse> result = new ArrayList<ExecutionCourse>();

        final Predicate<ExecutionCourse> hasScopePredicate = ec -> ec.getAssociatedCurricularCoursesSet().stream()
                .anyMatch(cc -> cc.hasScopeInGivenSemesterAndCurricularYearInDCP(curricularYear, degreeCurricularPlan,
                        ec.getExecutionInterval()));

        for (final ExecutionCourse executionCourse : interval.getAssociatedExecutionCoursesSet()) {
            final String executionCourseName = StringNormalizer.normalize(executionCourse.getNome());
            if (normalizedName != null && executionCourseName.matches(normalizedName)
                    && hasScopePredicate.test(executionCourse)) {
                result.add(executionCourse);
            }
        }
        return result;
    }

    public boolean isSplittable() {
        if (getAssociatedCurricularCoursesSet().size() < 2) {
            return false;
        }
        return true;
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
     * This method returns the portuguese name and the english name with the
     * rules implemented in getNome() method
     */
    public LocalizedString getNameI18N() {
        LocalizedString nameI18N = new LocalizedString();
        nameI18N = nameI18N.with(org.fenixedu.academic.util.LocaleUtils.PT, super.getNome());

        final StringBuilder stringBuilder = new StringBuilder();

        final Set<String> names = new HashSet<String>();

        for (final CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
            if (curricularCourse.isActive(getExecutionInterval())) {
                final String name = curricularCourse.getNameEn();
                if (!names.contains(name)) {
                    names.add(name);
                    if (stringBuilder.length() > 0) {
                        stringBuilder.append(" / ");
                    }
                    stringBuilder.append(name);
                }
            }
        }

        if (stringBuilder.length() > 0) {
            nameI18N = nameI18N.with(org.fenixedu.academic.util.LocaleUtils.EN, stringBuilder.toString());
            return nameI18N;
        }

        boolean unique = true;
        final String nameEn = getAssociatedCurricularCoursesSet().isEmpty() ? null : getAssociatedCurricularCoursesSet()
                .iterator().next().getNameEn();

        for (final CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
            if (curricularCourse.getNameEn() == null || !curricularCourse.getNameEn().equals(nameEn)) {
                unique = false;
                break;
            }
        }

        if (unique && nameEn != null) {
            nameI18N = nameI18N.with(org.fenixedu.academic.util.LocaleUtils.EN, nameEn);
            return nameI18N;
        } else {
            nameI18N = nameI18N.with(org.fenixedu.academic.util.LocaleUtils.EN, super.getNome());
            return nameI18N;
        }
    }

    public Professorship getProfessorshipForCurrentUser() {
        return this.getProfessorship(AccessControl.getPerson());
    }

    public int getEnrolmentCount() {
        int result = 0;
        for (final Attends attends : getAttendsSet()) {
            if (attends.getEnrolment() != null) {
                result++;
            }
        }
        return result;
    }

    public boolean isDissertation() {
        for (CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
            if (curricularCourse.isDissertation()) {
                return true;
            }
        }
        return false;
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

    @Atomic
    public void associateCurricularCourse(final CurricularCourse curricularCourse) {
        addAssociatedCurricularCourses(curricularCourse);
    }

    @Atomic
    public void dissociateCurricularCourse(final CurricularCourse curricularCourse) {
        super.removeAssociatedCurricularCourses(curricularCourse);
    }

    public Double getEctsCredits() {
        Double ects = null;
        for (CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
            if (curricularCourse.isActive(getExecutionInterval())) {
                if (ects == null) {
                    ects = curricularCourse.getEctsCredits();
                } else if (!ects.equals(curricularCourse.getEctsCredits())) {
                    throw new DomainException("error.invalid.ectsCredits");
                }
            }
        }
        return ects;
    }

    public Set<OccupationPeriod> getLessonPeriods() {
        final Set<OccupationPeriod> result = new TreeSet<OccupationPeriod>(new Comparator<OccupationPeriod>() {
            @Override
            public int compare(final OccupationPeriod op1, final OccupationPeriod op2) {
                final int i = op1.getPeriodInterval().getStart().compareTo(op2.getPeriodInterval().getStart());
                return i == 0 ? op1.getExternalId().compareTo(op2.getExternalId()) : i;
            }
        });
        for (final ExecutionDegree executionDegree : getExecutionDegrees()) {
            result.addAll(executionDegree.getPeriodLessons(getExecutionInterval()));
        }
        return result;
    }

    public ExecutionInterval getExecutionInterval() {
        return super.getExecutionPeriod();
    }

}
