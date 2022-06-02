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

import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.accessControl.StudentGroup;
import org.fenixedu.academic.domain.accessControl.TeacherGroup;
import org.fenixedu.academic.domain.accessControl.TeacherResponsibleOfExecutionCourseGroup;
import org.fenixedu.academic.domain.curriculum.CurricularCourseType;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.degreeStructure.BibliographicReferences;
import org.fenixedu.academic.domain.degreeStructure.BibliographicReferences.BibliographicReferenceType;
import org.fenixedu.academic.domain.degreeStructure.CompetenceCourseInformation;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.executionCourse.SummariesSearchBean;
import org.fenixedu.academic.domain.messaging.ExecutionCourseForum;
import org.fenixedu.academic.domain.organizationalStructure.DepartmentUnit;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.student.WeeklyWorkLoad;
import org.fenixedu.academic.domain.studentCurriculum.Dismissal;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicInterval;
import org.fenixedu.academic.dto.GenericPair;
import org.fenixedu.academic.dto.teacher.executionCourse.SearchExecutionCourseAttendsBean;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.predicate.ExecutionCoursePredicates;
import org.fenixedu.academic.service.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import org.fenixedu.academic.service.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import org.fenixedu.academic.service.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.DateFormatUtil;
import org.fenixedu.academic.util.LocaleUtils;
import org.fenixedu.academic.util.ProposalState;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.signals.DomainObjectEvent;
import org.fenixedu.bennu.core.signals.Signal;
import org.fenixedu.commons.StringNormalizer;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.messaging.core.domain.Sender;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.joda.time.YearMonthDay;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.core.AbstractDomainObject;
import pt.ist.fenixframework.dml.runtime.RelationAdapter;

import java.math.BigDecimal;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.fenixedu.academic.predicate.AccessControl.check;

public class ExecutionCourse extends ExecutionCourse_Base {
    public static final String CREATED_SIGNAL = "academic.executionCourse.create";
    public static final String EDITED_SIGNAL = "academic.executionCourse.create";
    public static final String ACRONYM_CHANGED_SIGNAL = "academic.executionCourse.acronym.edit";

    public static List<ExecutionCourse> readNotEmptyExecutionCourses() {
        return new ArrayList<>(Bennu.getInstance().getExecutionCoursesSet());
    }

    public static final Comparator<ExecutionCourse> EXECUTION_COURSE_EXECUTION_PERIOD_COMPARATOR =
            Comparator.comparing(ExecutionCourse_Base::getExecutionPeriod);

    public static final Comparator<ExecutionCourse> EXECUTION_COURSE_NAME_COMPARATOR = (o1, o2) -> {
        final int c = Collator.getInstance().compare(o1.getNome(), o2.getNome());
        return c == 0 ? DomainObjectUtil.COMPARATOR_BY_ID.compare(o1, o2) : c;
    };

    public static final Comparator<ExecutionCourse> EXECUTION_COURSE_COMPARATOR_BY_EXECUTION_PERIOD_AND_NAME = (o1, o2) -> {
        final int cep = EXECUTION_COURSE_EXECUTION_PERIOD_COMPARATOR.compare(o1, o2);
        if (cep != 0) {
            return cep;
        }
        final int c = EXECUTION_COURSE_NAME_COMPARATOR.compare(o1, o2);
        return c == 0 ? DomainObjectUtil.COMPARATOR_BY_ID.compare(o1, o2) : c;
    };

    public static final Comparator<ExecutionCourse> EXECUTION_COURSE_COMPARATOR_BY_EXECUTION_PERIOD_REVERSED_AND_NAME =
            (o1, o2) -> {
                final int cep = EXECUTION_COURSE_EXECUTION_PERIOD_COMPARATOR.compare(o2, o1);
                if (cep != 0) {
                    return cep;
                }
                final int c = EXECUTION_COURSE_NAME_COMPARATOR.compare(o1, o2);
                return c == 0 ? DomainObjectUtil.COMPARATOR_BY_ID.compare(o1, o2) : c;
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
                                || otherExecutionCourse.getExecutionPeriod().isAfter(previous.getExecutionPeriod())) {
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

    public ExecutionCourse(LocalizedString nameI18, final String nome, final String sigla,
            final ExecutionSemester executionSemester, EntryPhase entryPhase) {
        super();

        setRootDomainObject(Bennu.getInstance());
        addAssociatedEvaluations(new FinalEvaluation());
        setAvailableGradeSubmission(Boolean.TRUE);

        setNameI18N(nameI18);
        setNome(nameI18.getContent(LocaleUtils.PT));
        setExecutionPeriod(executionSemester);
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

        Signal.emit(ExecutionCourse.CREATED_SIGNAL, new DomainObjectEvent<>(this));
    }

    public void editInformation(final LocalizedString nameI18N, String nome, String sigla, String comment,
            Boolean availableGradeSubmission, EntryPhase entryPhase) {
        setNameI18N(nameI18N);
        setNome(getNomePT());
        setSigla(sigla);
        setComment(comment);
        setAvailableGradeSubmission(availableGradeSubmission);
        if (entryPhase != null) {
            setEntryPhase(entryPhase);
        }
        Signal.emit(ExecutionCourse.EDITED_SIGNAL, new DomainObjectEvent<>(this));

    }

    public void editCourseLoad(ShiftType type, BigDecimal unitQuantity, BigDecimal totalQuantity) {
        CourseLoad courseLoad = getCourseLoadByShiftType(type);
        if (courseLoad == null) {
            new CourseLoad(this, type, unitQuantity, totalQuantity);
        } else {
            courseLoad.edit(unitQuantity, totalQuantity);
        }
    }

    public List<Grouping> getGroupings() {
        List<Grouping> result = new ArrayList<>();
        for (final ExportGrouping exportGrouping : this.getExportGroupingsSet()) {
            if (exportGrouping.getProposalState().getState() == ProposalState.ACEITE
                    || exportGrouping.getProposalState().getState() == ProposalState.CRIADOR) {
                result.add(exportGrouping.getGrouping());
            }
        }
        return result;
    }

    public Grouping getGroupingByName(String groupingName) {
        for (final Grouping grouping : this.getGroupings()) {
            if (grouping.getName().equals(groupingName)) {
                return grouping;
            }
        }
        return null;
    }

    public boolean existsGroupingExecutionCourse(ExportGrouping groupPropertiesExecutionCourse) {
        return getExportGroupingsSet().contains(groupPropertiesExecutionCourse);
    }

    public boolean existsGroupingExecutionCourse() {
        return getExportGroupingsSet().isEmpty();
    }

    public boolean hasProposals() {
        boolean result = false;
        boolean found = false;
        Collection<ExportGrouping> groupPropertiesExecutionCourseList = getExportGroupingsSet();
        Iterator<ExportGrouping> iter = groupPropertiesExecutionCourseList.iterator();
        while (iter.hasNext() && !found) {
            ExportGrouping groupPropertiesExecutionCourseAux = iter.next();
            if (groupPropertiesExecutionCourseAux.getProposalState().getState() == 3) {
                result = true;
                found = true;
            }
        }
        return result;
    }

    public boolean isMasterDegreeDFAOrDEAOnly() {
        for (final CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
            DegreeType degreeType = curricularCourse.getDegreeCurricularPlan().getDegree().getDegreeType();
            if (!degreeType.isPreBolonhaMasterDegree() && !degreeType.isAdvancedFormationDiploma()
                    && !degreeType.isSpecializationDegree() && !degreeType.isAdvancedSpecializationDiploma()) {
                return false;
            }
        }
        return true;
    }

    public void createEvaluationMethod(final LocalizedString evaluationElements) {
        if (evaluationElements == null) {
            throw new NullPointerException();
        }

        final EvaluationMethod evaluationMethod = new EvaluationMethod();
        evaluationMethod.setExecutionCourse(this);
        evaluationMethod.setEvaluationElements(evaluationElements);

    }

    public void copyEvaluationMethodFrom(ExecutionCourse executionCourseFrom) {
        if (executionCourseFrom.getEvaluationMethod() != null) {
            final EvaluationMethod evaluationMethodFrom = executionCourseFrom.getEvaluationMethod();
            final EvaluationMethod evaluationMethodTo = this.getEvaluationMethod();
            if (evaluationMethodTo == null) {
                this.createEvaluationMethod(evaluationMethodFrom.getEvaluationElements());
            } else {
                evaluationMethodTo.edit(evaluationMethodFrom.getEvaluationElements());
            }
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
                title, getNameI18N().getContent(), this.getDegreePresentationString());
    }

    public List<BibliographicReference> copyBibliographicReferencesFrom(final ExecutionCourse executionCourseFrom) {
        final List<BibliographicReference> notCopiedBibliographicReferences = new ArrayList<>();

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

    public boolean hasAttendsFor(final Student student) {
        return getAttendsByStudent(student) != null;
    }

    public List<Exam> getAssociatedExams() {
        List<Exam> associatedExams = new ArrayList<>();

        for (Evaluation evaluation : this.getAssociatedEvaluationsSet()) {
            if (evaluation instanceof Exam) {
                associatedExams.add((Exam) evaluation);
            }
        }

        return associatedExams;
    }

    public List<WrittenEvaluation> getAssociatedWrittenEvaluations() {
        Set<WrittenEvaluation> writtenEvaluations = new HashSet<>();
        writtenEvaluations.addAll(this.getAssociatedExams());
        writtenEvaluations.addAll(this.getAssociatedWrittenTests());

        return new ArrayList<>(writtenEvaluations);

    }

    public List<WrittenTest> getAssociatedWrittenTests() {
        List<WrittenTest> associatedWrittenTests = new ArrayList<>();

        for (Evaluation evaluation : this.getAssociatedEvaluationsSet()) {
            if (evaluation instanceof WrittenTest) {
                associatedWrittenTests.add((WrittenTest) evaluation);
            }
        }

        return associatedWrittenTests;
    }

    // Delete Method
    public void delete() {
        DomainException.throwWhenDeleteBlocked(getDeletionBlockers());
        if (super.getSender() != null) {
            final Sender sender = super.getSender();
            setSender(null);
            sender.delete();
        }
        if (getEvaluationMethod() != null) {
            getEvaluationMethod().delete();
        }

        for (; !getExportGroupingsSet().isEmpty(); getExportGroupingsSet().iterator().next().delete()) {
            ;
        }
        for (; !getGroupingSenderExecutionCourseSet().isEmpty(); getGroupingSenderExecutionCourseSet().iterator().next()
                .delete()) {
            ;
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

        removeFinalEvaluations();
        getAssociatedCurricularCoursesSet().clear();
        getNonAffiliatedTeachersSet().clear();
        getTeacherGroupSet().clear();
        getStudentGroupSet().clear();
        getSpecialCriteriaOverExecutionCourseGroupSet().clear();
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
        if (!getGroupings().isEmpty()) {
            blockers.add(BundleUtil.getString(Bundle.APPLICATION, "error.execution.course.cant.delete"));
        }
        if (!getAssociatedBibliographicReferencesSet().isEmpty()) {
            blockers.add(BundleUtil.getString(Bundle.APPLICATION, "error.execution.course.cant.delete"));
        }
        if (!hasOnlyFinalEvaluations()) {
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
    }

    private void removeFinalEvaluations() {
        final Iterator<Evaluation> iterator = getAssociatedEvaluationsSet().iterator();
        while (iterator.hasNext()) {
            final Evaluation evaluation = iterator.next();
            if (evaluation.isFinal()) {
                iterator.remove();
                evaluation.delete();
            } else {
                throw new DomainException("error.ExecutionCourse.cannot.remove.non.final.evaluation");
            }
        }
    }

    private boolean hasOnlyFinalEvaluations() {
        for (final Evaluation evaluation : getAssociatedEvaluationsSet()) {
            if (!evaluation.isFinal()) {
                return false;
            }
        }
        return true;
    }

    public boolean teacherLecturesExecutionCourse(Teacher teacher) {
        for (Professorship professorship : this.getProfessorshipsSet()) {
            if (professorship.getTeacher() == teacher) {
                return true;
            }
        }
        return false;
    }

    public List<Project> getAssociatedProjects() {
        final List<Project> result = new ArrayList<>();

        for (Evaluation evaluation : this.getAssociatedEvaluationsSet()) {
            if (evaluation instanceof Project) {
                result.add((Project) evaluation);
            }
        }
        return result;
    }

    private int countAssociatedStudentsByEnrolmentNumber(int enrolmentNumber) {
        int executionCourseAssociatedStudents = 0;

        for (CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
            executionCourseAssociatedStudents = curricularCourse.countAssociatedStudentsByExecutionPeriodAndEnrolmentNumber(
                    getExecutionPeriod(), enrolmentNumber, executionCourseAssociatedStudents);
        }

        return executionCourseAssociatedStudents;
    }

    public Integer getTotalEnrolmentStudentNumber() {
        int executionCourseStudentNumber = 0;
        for (final CurricularCourse curricularCourseFromExecutionCourseEntry : getAssociatedCurricularCoursesSet()) {
            for (final Enrolment enrolment : curricularCourseFromExecutionCourseEntry.getEnrolments()) {
                if (enrolment.getExecutionPeriod() == getExecutionPeriod()) {
                    executionCourseStudentNumber++;
                }
            }
        }
        return executionCourseStudentNumber;
    }

    public Integer getFirstTimeEnrolmentStudentNumber() {
        return countAssociatedStudentsByEnrolmentNumber(1);
    }

    public Integer getSecondTimeEnrolmentStudentNumber() {
        return countAssociatedStudentsByEnrolmentNumber(2);
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
        Set<Shift> result = new HashSet<>();
        for (CourseLoad courseLoad : getCourseLoadsSet()) {
            result.addAll(courseLoad.getShiftsSet());
        }
        return result;
    }

    public Set<LessonInstance> getAssociatedLessonInstances() {
        Set<LessonInstance> result = new HashSet<>();
        for (CourseLoad courseLoad : getCourseLoadsSet()) {
            result.addAll(courseLoad.getLessonInstancesSet());
        }

        return result;
    }

    public Double getStudentsNumberByShift(ShiftType shiftType) {
        int numShifts = getNumberOfShifts(shiftType);

        if (numShifts == 0) {
            return 0.0;
        } else {
            return (double) getTotalEnrolmentStudentNumber() / numShifts;
        }
    }

    public List<Enrolment> getActiveEnrollments() {
        List<Enrolment> results = new ArrayList<>();

        for (CurricularCourse curricularCourse : this.getAssociatedCurricularCoursesSet()) {
            List<Enrolment> enrollments = curricularCourse.getActiveEnrollments(this.getExecutionPeriod());

            results.addAll(enrollments);
        }
        return results;
    }

    public List<Dismissal> getDismissals() {
        List<Dismissal> results = new ArrayList<>();

        for (CurricularCourse curricularCourse : this.getAssociatedCurricularCoursesSet()) {
            List<Dismissal> dismissals = curricularCourse.getDismissals(this.getExecutionPeriod());

            results.addAll(dismissals);
        }
        return results;
    }

    public boolean areAllOptionalCurricularCoursesWithLessTenEnrolments() {
        int enrolments = 0;
        for (CurricularCourse curricularCourse : this.getAssociatedCurricularCoursesSet()) {
            if (curricularCourse.getType() != null && curricularCourse.getType().equals(CurricularCourseType.OPTIONAL_COURSE)) {
                enrolments += curricularCourse.getEnrolmentsByExecutionPeriod(this.getExecutionPeriod()).size();
                if (enrolments >= 10) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    public static final Comparator<Evaluation> EVALUATION_COMPARATOR = new Comparator<Evaluation>() {

        @Override
        public int compare(Evaluation evaluation1, Evaluation evaluation2) {
            final String evaluation1ComparisonString = evaluationComparisonString(evaluation1);
            final String evaluation2ComparisonString = evaluationComparisonString(evaluation2);
            return evaluation1ComparisonString.compareTo(evaluation2ComparisonString);
        }

        private String evaluationComparisonString(final Evaluation evaluation) {
            final String evaluationTypeDistinguisher;

            if (evaluation instanceof AdHocEvaluation) {
                evaluationTypeDistinguisher = "0";
            } else if (evaluation instanceof Project) {
                evaluationTypeDistinguisher = "1";
            } else if (evaluation instanceof WrittenEvaluation) {
                evaluationTypeDistinguisher = "2";
            } else if (evaluation instanceof FinalEvaluation) {
                evaluationTypeDistinguisher = "Z";
            } else {
                evaluationTypeDistinguisher = "3";
            }

            return DateFormatUtil.format(evaluationTypeDistinguisher + "_yyyy/MM/dd", evaluation.getEvaluationDate())
                    + evaluation.getExternalId();
        }
    };

    public List<Evaluation> getOrderedAssociatedEvaluations() {
        final List<Evaluation> orderedEvaluations = new ArrayList<>(getAssociatedEvaluationsSet());
        Collections.sort(orderedEvaluations, EVALUATION_COMPARATOR);
        return orderedEvaluations;
    }

    public Set<Attends> getOrderedAttends() {
        final Set<Attends> orderedAttends = new TreeSet<>(Attends.COMPARATOR_BY_STUDENT_NUMBER);
        orderedAttends.addAll(getAttendsSet());
        return orderedAttends;
    }

    private static class CurricularCourseExecutionCourseListener extends RelationAdapter<ExecutionCourse, CurricularCourse> {

        @Override
        public void afterAdd(ExecutionCourse execution, CurricularCourse curricular) {
            for (final Enrolment enrolment : curricular.getEnrolments()) {
                if (enrolment.getExecutionPeriod().equals(execution.getExecutionPeriod())) {
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
            if (!alreadyHasAttend(enrolment, executionCourse.getExecutionPeriod())) {
                Attends attends = executionCourse.getAttendsByStudent(enrolment.getStudentCurricularPlan().getRegistration());
                if (attends == null) {
                    attends = new Attends(enrolment.getStudentCurricularPlan().getRegistration(), executionCourse);
                }
                enrolment.addAttends(attends);
            }
        }

        private static boolean alreadyHasAttend(Enrolment enrolment, ExecutionSemester executionSemester) {
            for (Attends attends : enrolment.getAttendsSet()) {
                if (attends.getExecutionCourse().getExecutionPeriod().equals(executionSemester)) {
                    return true;
                }
            }
            return false;
        }

        private void fillCourseLoads(ExecutionCourse execution, CurricularCourse curricular) {
            for (ShiftType shiftType : ShiftType.values()) {
                BigDecimal totalHours = curricular.getTotalHoursByShiftType(shiftType, execution.getExecutionPeriod());
                if (totalHours != null && totalHours.compareTo(BigDecimal.ZERO) == 1) {
                    CourseLoad courseLoad = execution.getCourseLoadByShiftType(shiftType);
                    if (courseLoad == null) {
                        new CourseLoad(execution, shiftType, null, totalHours);
                    }
                }
            }
        }
    }

    public class WeeklyWorkLoadView {
        final Interval executionPeriodInterval;

        final int numberOfWeeks;

        final Interval[] intervals;

        final int[] numberResponses;

        final int[] contactSum;

        final int[] autonomousStudySum;

        final int[] otherSum;

        final int[] totalSum;

        public WeeklyWorkLoadView(final Interval executionPeriodInterval) {
            this.executionPeriodInterval = executionPeriodInterval;
            final Period period = executionPeriodInterval.toPeriod();
            int extraWeek = period.getDays() > 0 ? 1 : 0;
            numberOfWeeks = (period.getYears() * 12 + period.getMonths()) * 4 + period.getWeeks() + extraWeek + 1;
            intervals = new Interval[numberOfWeeks];
            numberResponses = new int[numberOfWeeks];
            contactSum = new int[numberOfWeeks];
            autonomousStudySum = new int[numberOfWeeks];
            otherSum = new int[numberOfWeeks];
            totalSum = new int[numberOfWeeks];
            for (int i = 0; i < numberOfWeeks; i++) {
                final DateTime start = executionPeriodInterval.getStart().plusWeeks(i);
                final DateTime end = start.plusWeeks(1);
                intervals[i] = new Interval(start, end);
            }
        }

        public void add(final Attends attends) {
            for (final WeeklyWorkLoad weeklyWorkLoad : attends.getWeeklyWorkLoadsSet()) {
                final int weekIndex = weeklyWorkLoad.getWeekOffset();
                if (consistentAnswers(attends, weekIndex)) {
                    numberResponses[weekIndex]++;

                    final Integer contact = weeklyWorkLoad.getContact();
                    contactSum[weekIndex] += contact != null ? contact : 0;

                    final Integer autounomousStudy = weeklyWorkLoad.getAutonomousStudy();
                    autonomousStudySum[weekIndex] += autounomousStudy != null ? autounomousStudy : 0;

                    final Integer other = weeklyWorkLoad.getOther();
                    otherSum[weekIndex] += other != null ? other : 0;

                    totalSum[weekIndex] = contactSum[weekIndex] + autonomousStudySum[weekIndex] + otherSum[weekIndex];
                }
            }
        }

        private boolean consistentAnswers(final Attends attends, final int weekIndex) {
            int weeklyTotal = 0;
            for (final Attends someAttends : attends.getRegistration().getAssociatedAttendsSet()) {
                for (final WeeklyWorkLoad weeklyWorkLoad : someAttends.getWeeklyWorkLoadsSet()) {
                    if (weeklyWorkLoad.getWeekOffset() == weekIndex) {
                        weeklyTotal += weeklyWorkLoad.getTotal();
                    }
                }
            }
            return weeklyTotal <= 140;
        }

        public Interval[] getIntervals() {
            return intervals;
        }

        public Interval getExecutionPeriodInterval() {
            return executionPeriodInterval;
        }

        public int[] getContactSum() {
            return contactSum;
        }

        public int[] getAutonomousStudySum() {
            return autonomousStudySum;
        }

        public int[] getOtherSum() {
            return otherSum;
        }

        public int[] getNumberResponses() {
            return numberResponses;
        }

        public double[] getContactAverage() {
            return average(getContactSum(), getNumberResponses());
        }

        public double[] getAutonomousStudyAverage() {
            return average(getAutonomousStudySum(), getNumberResponses());
        }

        public double[] getOtherAverage() {
            return average(getOtherSum(), getNumberResponses());
        }

        public double[] getTotalAverage() {
            final double[] valuesAverage = new double[numberOfWeeks];
            for (int i = 0; i < numberOfWeeks; i++) {
                valuesAverage[i] = Math.round(
                        (0.0 + getContactSum()[i] + getAutonomousStudySum()[i] + getOtherSum()[i]) / getNumberResponses()[i]);
            }
            return valuesAverage;
        }

        private double[] average(final int[] values, final int[] divisor) {
            final double[] valuesAverage = new double[numberOfWeeks];
            for (int i = 0; i < numberOfWeeks; i++) {
                valuesAverage[i] = Math.round((0.0 + values[i]) / divisor[i]);
            }
            return valuesAverage;
        }

        private double add(final double[] values) {
            double total = 0;
            for (double value : values) {
                total += value;
            }
            return total;
        }

        public double getContactAverageTotal() {
            return add(getContactAverage());
        }

        public double getAutonomousStudyAverageTotal() {
            return add(getAutonomousStudyAverage());
        }

        public double getOtherAverageTotal() {
            return add(getOtherAverage());
        }

        public double getTotalAverageTotal() {
            return add(getTotalAverage());
        }

        public int getNumberResponsesTotal() {
            int total = 0;
            for (int i = 0; i < getNumberResponses().length; i++) {
                total += getNumberResponses()[i];
            }
            return total;
        }

        private int getNumberWeeksForAverageCalculation() {
            if (!getAttendsSet().isEmpty()) {
                final Attends attends = findAttendsWithEnrolment();
                if (attends != null) {
                    int currentWeekOffset = attends.calculateCurrentWeekOffset();
                    if (currentWeekOffset > 0 && currentWeekOffset < numberOfWeeks) {
                        return currentWeekOffset;
                    }
                }
            }
            return numberOfWeeks;
        }

        public double getContactAverageTotalAverage() {
            final int numberOfWeeks = getNumberWeeksForAverageCalculation();
            return numberOfWeeks > 0 ? Math.round(getContactAverageTotal() / numberOfWeeks) : 0;
        }

        public double getAutonomousStudyAverageTotalAverage() {
            final int numberOfWeeks = getNumberWeeksForAverageCalculation();
            return numberOfWeeks > 0 ? Math.round(getAutonomousStudyAverageTotal() / getNumberWeeksForAverageCalculation()) : 0;
        }

        public double getOtherAverageTotalAverage() {
            final int numberOfWeeks = getNumberWeeksForAverageCalculation();
            return numberOfWeeks > 0 ? Math.round(getOtherAverageTotal() / getNumberWeeksForAverageCalculation()) : 0;
        }

        public double getTotalAverageTotalAverage() {
            final int numberOfWeeks = getNumberWeeksForAverageCalculation();
            return numberOfWeeks > 0 ? Math.round(getTotalAverageTotal() / getNumberWeeksForAverageCalculation()) : 0;
        }

        public double getNumberResponsesTotalAverage() {
            final int numberOfWeeks = getNumberWeeksForAverageCalculation();
            return numberOfWeeks > 0 ? Math.round((0.0 + getNumberResponsesTotal()) / getNumberWeeksForAverageCalculation()) : 0;
        }

    }

    public Interval getInterval() {
        final ExecutionSemester executionSemester = getExecutionPeriod();
        final DateTime beginningOfSemester = new DateTime(executionSemester.getBeginDateYearMonthDay());
        final DateTime firstMonday = beginningOfSemester.withField(DateTimeFieldType.dayOfWeek(), 1);
        final DateTime endOfSemester = new DateTime(executionSemester.getEndDateYearMonthDay());
        final DateTime nextLastMonday = endOfSemester.withField(DateTimeFieldType.dayOfWeek(), 1).plusWeeks(1);
        return new Interval(firstMonday, nextLastMonday);
    }

    public WeeklyWorkLoadView getWeeklyWorkLoadView() {
        final Attends attends = findAttendsWithEnrolment();
        if (attends != null) {
            final Interval interval = attends.getWeeklyWorkLoadInterval();
            final WeeklyWorkLoadView weeklyWorkLoadView = new WeeklyWorkLoadView(interval);
            for (final Attends attend : getAttendsSet()) {
                weeklyWorkLoadView.add(attend);
            }
            return weeklyWorkLoadView;
        } else {
            return null;
        }
    }

    private Attends findAttendsWithEnrolment() {
        for (final Attends attends : getAttendsSet()) {
            if (attends.getEnrolment() != null) {
                return attends;
            }
        }
        return null;
    }

    public boolean hasGrouping(final Grouping grouping) {
        for (final ExportGrouping exportGrouping : getExportGroupingsSet()) {
            if (grouping == exportGrouping.getGrouping()) {
                return true;
            }
        }
        return false;
    }

    public Shift findShiftByName(final String shiftName) {
        for (final Shift shift : getAssociatedShifts()) {
            if (shift.getNome().equals(shiftName)) {
                return shift;
            }
        }
        return null;
    }

    public Set<Shift> findShiftByType(final ShiftType shiftType) {
        final Set<Shift> shifts = new HashSet<>();
        for (final Shift shift : getAssociatedShifts()) {
            if (shift.containsType(shiftType)) {
                shifts.add(shift);
            }
        }
        return shifts;
    }

    public Set<SchoolClass> findSchoolClasses() {
        final Set<SchoolClass> schoolClasses = new HashSet<>();
        for (final Shift shift : getAssociatedShifts()) {
            schoolClasses.addAll(shift.getAssociatedClassesSet());
        }
        return schoolClasses;
    }

    public List<Summary> readSummariesOfTeachersWithoutProfessorship() {

        List<Summary> summaries = new ArrayList<>();
        for (Summary summary : this.getAssociatedSummariesSet()) {
            if (summary.getProfessorship() == null && (summary.getTeacher() != null
                    || (summary.getTeacherName() != null && !summary.getTeacherName().equals("")))) {
                summaries.add(summary);
            }
        }
        return summaries;
    }

    public ExportGrouping getExportGrouping(final Grouping grouping) {
        for (final ExportGrouping exportGrouping : this.getExportGroupingsSet()) {
            if (exportGrouping.getGrouping() == grouping) {
                return exportGrouping;
            }
        }
        return null;
    }

    public boolean hasExportGrouping(final Grouping grouping) {
        return getExportGrouping(grouping) != null;
    }

    public boolean hasScopeInGivenSemesterAndCurricularYearInDCP(CurricularYear curricularYear,
            DegreeCurricularPlan degreeCurricularPlan) {
        for (CurricularCourse curricularCourse : this.getAssociatedCurricularCoursesSet()) {
            if (curricularCourse.hasScopeInGivenSemesterAndCurricularYearInDCP(curricularYear, degreeCurricularPlan,
                    getExecutionPeriod())) {
                return true;
            }
        }
        return false;
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
        final SortedSet<Degree> degrees = new TreeSet<>(Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);
        for (final CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
            final DegreeCurricularPlan degreeCurricularPlan = curricularCourse.getDegreeCurricularPlan();
            degrees.add(degreeCurricularPlan.getDegree());
        }
        return degrees;
    }

    public SortedSet<CurricularCourse> getCurricularCoursesSortedByDegreeAndCurricularCourseName() {
        final SortedSet<CurricularCourse> curricularCourses =
                new TreeSet<>(CurricularCourse.CURRICULAR_COURSE_COMPARATOR_BY_DEGREE_AND_NAME);
        curricularCourses.addAll(getAssociatedCurricularCoursesSet());
        return curricularCourses;
    }

    public Set<CompetenceCourse> getCompetenceCourses() {
        final Set<CompetenceCourse> competenceCourses = new HashSet<>();
        for (final CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
            final CompetenceCourse competenceCourse = curricularCourse.getCompetenceCourse();
            if (competenceCourse != null) {
                competenceCourses.add(competenceCourse);
            }
        }
        return competenceCourses;
    }

    public Set<CompetenceCourseInformation> getCompetenceCoursesInformations() {
        final Set<CompetenceCourseInformation> competenceCourseInformations = new HashSet<>();
        for (final CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
            final CompetenceCourse competenceCourse = curricularCourse.getCompetenceCourse();
            if (competenceCourse != null) {
                final CompetenceCourseInformation competenceCourseInformation =
                        competenceCourse.findCompetenceCourseInformationForExecutionPeriod(getExecutionPeriod());
                if (competenceCourseInformation != null) {
                    competenceCourseInformations.add(competenceCourseInformation);
                }
            }
        }
        return competenceCourseInformations;
    }

    public boolean hasAnyDegreeGradeToSubmit(final ExecutionSemester period, final DegreeCurricularPlan degreeCurricularPlan) {
        for (final CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
            if (degreeCurricularPlan == null || degreeCurricularPlan.equals(curricularCourse.getDegreeCurricularPlan())) {
                if (curricularCourse.hasAnyDegreeGradeToSubmit(period)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasAnyDegreeMarkSheetToConfirm(ExecutionSemester period, DegreeCurricularPlan degreeCurricularPlan) {
        for (final CurricularCourse curricularCourse : this.getAssociatedCurricularCoursesSet()) {
            if (degreeCurricularPlan == null || degreeCurricularPlan.equals(curricularCourse.getDegreeCurricularPlan())) {
                if (curricularCourse.hasAnyDegreeMarkSheetToConfirm(period)) {
                    return true;
                }
            }
        }
        return false;
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
        final SortedSet<Shift> shifts = new TreeSet<>(Shift.SHIFT_COMPARATOR_BY_NAME);
        for (final Shift shift : getAssociatedShifts()) {
            if (shift.containsType(shiftType)) {
                shifts.add(shift);
            }
        }
        return shifts;
    }

    public void setShiftNames() {
        final SortedSet<Shift> shifts =
                constructSortedSet(getAssociatedShifts(), Shift.SHIFT_COMPARATOR_BY_TYPE_AND_ORDERED_LESSONS);
        int counter = 0;
        for (final Shift shift : shifts) {
            if (shift.isCustomName()) {
                continue;
            }
            final String name = constructShiftName(shift, ++counter);
            shift.setNome(name);
        }
    }

    private static <T> SortedSet<T> constructSortedSet(Collection<T> collection, Comparator<? super T> c) {
        final SortedSet<T> sortedSet = new TreeSet<>(c);
        sortedSet.addAll(collection);
        return sortedSet;
    }

    public boolean hasProjectsWithOnlineSubmission() {
        for (Project project : getAssociatedProjects()) {
            if (project.getOnlineSubmissionsAllowed()) {
                return true;
            }
        }

        return false;
    }

    public List<Project> getProjectsWithOnlineSubmission() {
        List<Project> result = new ArrayList<>();
        for (Project project : getAssociatedProjects()) {
            if (project.getOnlineSubmissionsAllowed()) {
                result.add(project);
            }
        }

        return result;
    }

    public List<Project> getPastProjectsWithOnlineSubmission() {
        List<Project> result = new ArrayList<>();
        for (Project project : getAssociatedProjects()) {
            if (project.getOnlineSubmissionsAllowed() && project.isPastProject()) {
                result.add(project);
            }
        }

        return result;
    }

    private Set<SchoolClass> getAllSchoolClassesOrBy(DegreeCurricularPlan degreeCurricularPlan) {
        final Set<SchoolClass> result = new HashSet<>();
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

    public boolean isLecturedIn(final ExecutionYear executionYear) {
        return getExecutionPeriod().getExecutionYear() == executionYear;
    }

    public boolean isLecturedIn(final ExecutionSemester executionSemester) {
        return getExecutionPeriod() == executionSemester;
    }

    public SortedSet<Professorship> getProfessorshipsSortedAlphabetically() {
        final SortedSet<Professorship> professorhips = new TreeSet<>(Professorship.COMPARATOR_BY_PERSON_NAME);
        professorhips.addAll(getProfessorshipsSet());
        return professorhips;
    }

    public SummariesSearchBean getSummariesSearchBean() {
        return new SummariesSearchBean(this);
    }

    public Set<Lesson> getLessons() {
        final Set<Lesson> lessons = new HashSet<>();
        for (final Shift shift : getAssociatedShifts()) {
            lessons.addAll(shift.getAssociatedLessonsSet());
        }
        return lessons;
    }

    public boolean hasAnyLesson() {
        for (CourseLoad courseLoad : getCourseLoadsSet()) {
            for (final Shift shift : courseLoad.getShiftsSet()) {
                if (!shift.getAssociatedLessonsSet().isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

    public SortedSet<WrittenEvaluation> getWrittenEvaluations() {
        final SortedSet<WrittenEvaluation> writtenEvaluations = new TreeSet<>(WrittenEvaluation.COMPARATOR_BY_BEGIN_DATE);
        for (final Evaluation evaluation : getAssociatedEvaluationsSet()) {
            if (evaluation instanceof WrittenEvaluation) {
                writtenEvaluations.add((WrittenEvaluation) evaluation);
            }
        }
        return writtenEvaluations;
    }

    public SortedSet<Shift> getShiftsOrderedByLessons() {
        final SortedSet<Shift> shifts = new TreeSet<>(Shift.SHIFT_COMPARATOR_BY_TYPE_AND_ORDERED_LESSONS);
        shifts.addAll(getAssociatedShifts());
        return shifts;
    }

    public Map<CompetenceCourse, Set<CurricularCourse>> getCurricularCoursesIndexedByCompetenceCourse() {
        final Map<CompetenceCourse, Set<CurricularCourse>> curricularCourseMap = new HashMap<>();
        for (final CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
            if (!curricularCourse.getDegreeType().isPreBolonhaDegree()) {
                final CompetenceCourse competenceCourse = curricularCourse.getCompetenceCourse();
                if (competenceCourse != null) {
                    final Set<CurricularCourse> curricularCourses;
                    if (curricularCourseMap.containsKey(competenceCourse)) {
                        curricularCourses = curricularCourseMap.get(competenceCourse);
                    } else {
                        curricularCourses = new TreeSet<>(CurricularCourse.CURRICULAR_COURSE_COMPARATOR_BY_DEGREE_AND_NAME);
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
            if ((referenceType.equals(BibliographicReferenceType.SECONDARY) && bibliographicReference.getOptional())
                    || (referenceType.equals(BibliographicReferenceType.MAIN) && !bibliographicReference.getOptional())) {
                return true;
            }
        }
        for (final CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
            final CompetenceCourse competenceCourse = curricularCourse.getCompetenceCourse();
            if (competenceCourse != null) {
                final CompetenceCourseInformation competenceCourseInformation =
                        competenceCourse.findCompetenceCourseInformationForExecutionPeriod(getExecutionPeriod());
                if (competenceCourseInformation != null) {
                    final BibliographicReferences bibliographicReferences =
                            competenceCourseInformation.getBibliographicReferences();
                    if (bibliographicReferences != null) {
                        for (final BibliographicReferences.BibliographicReference bibliographicReference : bibliographicReferences
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
        final List<LessonPlanning> lessonPlannings = new ArrayList<>();
        for (LessonPlanning planning : getLessonPlanningsSet()) {
            if (planning.getLessonType().equals(lessonType)) {
                lessonPlannings.add(planning);
            }
        }
        lessonPlannings.sort(LessonPlanning.COMPARATOR_BY_ORDER);
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
        Set<ShiftType> shiftTypes = new TreeSet<>();
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

    public void createLessonPlanningsUsingSummariesFrom(Shift shift) {
        List<Summary> summaries = new ArrayList<>();
        summaries.addAll(shift.getAssociatedSummariesSet());
        summaries.sort(new ReverseComparator(Summary.COMPARATOR_BY_DATE_AND_HOUR));
        for (Summary summary : summaries) {
            for (ShiftType shiftType : shift.getTypes()) {
                new LessonPlanning(summary.getTitle(), summary.getSummaryText(), shiftType, this);
            }
        }
    }

    public void deleteLessonPlanningsByLessonType(ShiftType shiftType) {
        List<LessonPlanning> lessonPlanningsOrderedByOrder = getLessonPlanningsOrderedByOrder(shiftType);
        for (LessonPlanning planning : lessonPlanningsOrderedByOrder) {
            planning.deleteWithoutReOrder();
        }
    }

    public Integer getNumberOfShifts(ShiftType shiftType) {
        int numShifts = 0;
        for (Shift shiftEntry : getAssociatedShifts()) {
            if (shiftEntry.containsType(shiftType)) {
                numShifts++;
            }
        }
        return numShifts;
    }

    public Double getCurricularCourseEnrolmentsWeight(CurricularCourse curricularCourse) {
        Double totalEnrolmentStudentNumber = new Double(getTotalEnrolmentStudentNumber());
        if (totalEnrolmentStudentNumber > 0d) {
            return curricularCourse.getTotalEnrolmentStudentNumber(getExecutionPeriod()) / totalEnrolmentStudentNumber;
        } else {
            return 0d;
        }
    }

    public Set<ShiftType> getOldShiftTypesToEnrol() {
        final List<ShiftType> validShiftTypes =
                Arrays.asList(ShiftType.TEORICA, ShiftType.PRATICA, ShiftType.LABORATORIAL, ShiftType.TEORICO_PRATICA);

        final Set<ShiftType> result = new HashSet<>(4);
        for (final Shift shift : getAssociatedShifts()) {
            for (ShiftType shiftType : shift.getTypes()) {
                if (validShiftTypes.contains(shiftType)) {
                    result.add(shiftType);
                    break;
                }
            }
        }
        return result;
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
            BigDecimal ccTotalHours = curricularCourse.getTotalHoursByShiftType(type, getExecutionPeriod());
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

    public List<Summary> getSummariesByShiftType(ShiftType shiftType) {
        List<Summary> summaries = new ArrayList<>();
        for (Summary summary : getAssociatedSummariesSet()) {
            if (summary.getSummaryType() != null && summary.getSummaryType().equals(shiftType)) {
                summaries.add(summary);
            }
        }
        return summaries;
    }

    public List<Summary> getSummariesWithoutProfessorshipButWithTeacher(Teacher teacher) {
        List<Summary> summaries = new ArrayList<>();
        if (teacher != null) {
            for (Summary summary : getAssociatedSummariesSet()) {
                if (summary.getTeacher() != null && summary.getTeacher().equals(teacher)) {
                    summaries.add(summary);
                }
            }
        }
        return summaries;
    }

    public void moveSummariesFromTeacherToProfessorship(Teacher teacher, Professorship professorship) {
        List<Summary> summaries = getSummariesWithoutProfessorshipButWithTeacher(teacher);
        for (Summary summary : summaries) {
            summary.moveFromTeacherToProfessorship(professorship);
        }
    }

    public String getNome() {
        return getNomePT() != null ? getNomePT() : super.getNome();

    }

//    public String getNome() {
//        return getNomePT();
//    }

    public String getNomePT() {
        return getNomeI18(LocaleUtils.PT);
    }

    public String getNomeEN() {
        return getNomeI18(LocaleUtils.EN);
    }

    public String getName() {
        return getNameI18N().getContent();
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

    public Registration getRegistration(Person person) {
        for (Registration registration : person.getStudents()) {
            for (StudentCurricularPlan studentCurricularPlan : registration.getStudentCurricularPlansSet()) {
                for (Enrolment enrolment : studentCurricularPlan.getEnrolmentsSet()) {
                    for (ExecutionCourse course : enrolment.getExecutionCourses()) {
                        if (course.equals(this)) {
                            return registration;
                        }
                    }
                }
            }
        }

        return null;
    }

    public ExecutionYear getExecutionYear() {
        return getExecutionPeriod().getExecutionYear();
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
        TreeSet<BibliographicReference> references = new TreeSet<>(BibliographicReference.COMPARATOR_BY_ORDER);
        references.addAll(getAssociatedBibliographicReferencesSet());
        return references;
    }

    public void setBibliographicReferencesOrder(List<BibliographicReference> references) {
        for (int i = 0; i < references.size(); i++) {
            references.get(i).setReferenceOrder(i);
        }
    }

    public List<BibliographicReference> getMainBibliographicReferences() {
        List<BibliographicReference> references = new ArrayList<>();

        for (BibliographicReference reference : getAssociatedBibliographicReferencesSet()) {
            if (!reference.isOptional()) {
                references.add(reference);
            }
        }

        return references;
    }

    public List<BibliographicReference> getSecondaryBibliographicReferences() {
        List<BibliographicReference> references = new ArrayList<>();

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

    public Collection<Curriculum> getCurriculums(final ExecutionYear executionYear) {
        final Collection<Curriculum> result = new HashSet<>();

        for (final CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
            final Curriculum curriculum = executionYear == null ? curricularCourse.findLatestCurriculum() : curricularCourse
                    .findLatestCurriculumModifiedBefore(executionYear.getEndDate());
            if (curriculum != null) {
                result.add(curriculum);
            }
        }

        return result;
    }

    public boolean isInExamPeriod() {
        final YearMonthDay yearMonthDay = new YearMonthDay();
        final ExecutionSemester executionSemester = getExecutionPeriod();
        final ExecutionYear executionYear = getExecutionPeriod().getExecutionYear();
        for (final CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
            final DegreeCurricularPlan degreeCurricularPlan = curricularCourse.getDegreeCurricularPlan();
            final ExecutionDegree executionDegree = degreeCurricularPlan.getExecutionDegreeByYear(executionYear);
            final YearMonthDay startExamsPeriod;
            if (executionSemester.getSemester() == 1) {
                startExamsPeriod = executionDegree.getPeriodExamsFirstSemester().getStartYearMonthDay();
            } else if (executionSemester.getSemester() == 2) {
                startExamsPeriod = executionDegree.getPeriodExamsSecondSemester().getStartYearMonthDay();
            } else {
                throw new DomainException("unsupported.execution.period.semester");
            }
            if (!startExamsPeriod.minusDays(2).isAfter(yearMonthDay)) {
                return true;
            }
        }

        return false;
    }

    public List<Grouping> getGroupingsToEnrol() {
        final List<Grouping> result = new ArrayList<>();
        for (final Grouping grouping : getGroupings()) {
            if (checkPeriodEnrollmentFor(grouping)) {
                result.add(grouping);
            }
        }
        return result;
    }

    private boolean checkPeriodEnrollmentFor(final Grouping grouping) {
        final IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory.getInstance();
        final IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory.getGroupEnrolmentStrategyInstance(grouping);
        return strategy.checkEnrolmentDate(grouping, Calendar.getInstance());

    }

    public SortedSet<ExecutionDegree> getFirsExecutionDegreesByYearWithExecutionIn(ExecutionYear executionYear) {
        SortedSet<ExecutionDegree> result = new TreeSet<>(ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_YEAR);
        for (CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
            ExecutionDegree executionDegree = curricularCourse.getDegreeCurricularPlan().getExecutionDegreeByYear(executionYear);
            if (executionDegree != null) {
                result.add(executionDegree);
            }
        }
        return result;
    }

    public Set<ExecutionDegree> getExecutionDegrees() {
        Set<ExecutionDegree> result = new HashSet<>();
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

    public Set<Department> getDepartments() {
        final ExecutionSemester executionSemester = getExecutionPeriod();
        final Set<Department> departments = new TreeSet<>(Department.COMPARATOR_BY_NAME);
        for (final CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
            final CompetenceCourse competenceCourse = curricularCourse.getCompetenceCourse();
            if (competenceCourse != null) {
                final DepartmentUnit departmentUnit = competenceCourse.getDepartmentUnit(executionSemester);
                if (departmentUnit != null) {
                    final Department department = departmentUnit.getDepartment();
                    if (department != null) {
                        departments.add(department);
                    }
                }
            }
        }
        return departments;
    }

    public String getDepartmentNames() {
        final ExecutionSemester executionSemester = getExecutionPeriod();
        final Set<String> departments = new TreeSet<>();
        for (final CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
            final CompetenceCourse competenceCourse = curricularCourse.getCompetenceCourse();
            if (competenceCourse != null) {
                final DepartmentUnit departmentUnit = competenceCourse.getDepartmentUnit(executionSemester);
                if (departmentUnit != null) {
                    final Department department = departmentUnit.getDepartment();
                    if (department != null) {
                        departments.add(department.getName());
                    }
                }
            }
        }
        return StringUtils.join(departments, ", ");
    }

    public boolean isFromDepartment(final Department departmentToCheck) {
        for (final CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
            if (departmentToCheck == curricularCourse.getCompetenceCourse().getDepartmentUnit().getDepartment()) {
                return true;
            }
        }
        return false;
    }

    private OccupationPeriod findOccupationPeriodFor(final ExecutionSemester executionSemester,
            final CurricularCourse curricularCourse) {
        final ExecutionDegree executionDegree = curricularCourse.getExecutionDegreeFor(executionSemester.getExecutionYear());
        final Set<Integer> curricularYears = curricularCourse.getParentContextsByExecutionSemester(executionSemester).stream()
                .map(context -> context.getCurricularYear()).collect(Collectors.toSet());
        return executionDegree == null ? null : executionDegree.getOccupationPeriodReferencesSet().stream()
                .filter(ref -> ref.getPeriodType() == OccupationPeriodType.LESSONS)
                .filter(ref -> ref.getSemester().intValue() == executionSemester.getSemester().intValue())
                .filter(ref -> overlap(ref.getCurricularYears().getYears(), curricularYears))
                .map(ref -> ref.getOccupationPeriod()).max(Comparator.comparing(OccupationPeriod::getStartDate)).orElse(null);
    }

    private boolean overlap(final Collection<Integer> list, final Set<Integer> set) {
        if (list.isEmpty()) {
            return true;
        }
        for (final Integer year : list) {
            if (year == -1 || set.contains(year)) {
                return true;
            }
        }
        return false;
    }

    public OccupationPeriod getLessonOccupationPeriod() {
        final ExecutionSemester semester = getExecutionPeriod();
        return getAssociatedCurricularCoursesSet().stream().map(cc -> findOccupationPeriodFor(semester, cc))
                .filter(Objects::nonNull).distinct().max(Comparator.comparing(OccupationPeriod::getStartDate)).orElse(null);
    }

    public GenericPair<YearMonthDay, YearMonthDay> getMaxLessonsPeriod() {
        final OccupationPeriod occupationPeriod = getLessonOccupationPeriod();
        return occupationPeriod == null ? null : new GenericPair<>(occupationPeriod.getStartYearMonthDay(),
                occupationPeriod.getEndYearMonthDayWithNextPeriods());
    }

    public Map<ShiftType, CourseLoad> getCourseLoadsMap() {
        Map<ShiftType, CourseLoad> result = new HashMap<>();
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

    public boolean hasCourseLoadForType(ShiftType type) {
        CourseLoad courseLoad = getCourseLoadByShiftType(type);
        return courseLoad != null && !courseLoad.isEmpty();
    }

    public boolean verifyNameEquality(String[] nameWords) {
        if (nameWords != null) {
            String courseName = getNome() + " " + getSigla();
            String[] courseNameWords = StringNormalizer.normalize(courseName).trim().split(" ");
            int j, i;
            for (i = 0; i < nameWords.length; i++) {
                if (!nameWords[i].equals("")) {
                    for (j = 0; j < courseNameWords.length; j++) {
                        if (courseNameWords[j].equals(nameWords[i])) {
                            break;
                        }
                    }
                    if (j == courseNameWords.length) {
                        return false;
                    }
                }
            }
            return i == nameWords.length;
        }
        return false;
    }

    public Set<Space> getAllRooms() {
        Set<Space> result = new HashSet<>();
        Set<Lesson> lessons = getLessons();
        for (Lesson lesson : lessons) {
            Space room = lesson.getSala();
            if (room != null) {
                result.add(room);
            }
        }
        return result;
    }

    public String getLocalizedEvaluationMethodText() {
        final EvaluationMethod evaluationMethod = getEvaluationMethod();
        if (evaluationMethod != null) {
            final LocalizedString evaluationElements = evaluationMethod.getEvaluationElements();
            return evaluationElements.getContent();
        }
        for (final CompetenceCourse competenceCourse : getCompetenceCourses()) {
            final LocalizedString lstring = competenceCourse.getLocalizedEvaluationMethod(getExecutionPeriod());
            if (lstring != null) {
                return lstring.getContent();
            }
        }
        return "";
    }

    public String getEvaluationMethodText() {
        if (getEvaluationMethod() != null) {
            final LocalizedString evaluationElements = getEvaluationMethod().getEvaluationElements();

            return evaluationElements != null && evaluationElements.getContent(LocaleUtils.PT) != null ? evaluationElements
                    .getContent(LocaleUtils.PT) : !getCompetenceCourses().isEmpty() ? getCompetenceCourses().iterator().next()
                            .getEvaluationMethod() : "";
        } else {
            return !getCompetenceCourses().isEmpty() ? getCompetenceCourses().iterator().next().getEvaluationMethod() : "";
        }
    }

    public String getEvaluationMethodTextEn() {
        if (getEvaluationMethod() != null) {
            final LocalizedString evaluationElements = getEvaluationMethod().getEvaluationElements();

            return evaluationElements != null && evaluationElements.getContent(LocaleUtils.EN) != null ? evaluationElements
                    .getContent(LocaleUtils.EN) : !getCompetenceCourses().isEmpty() ? getCompetenceCourses().iterator().next()
                            .getEvaluationMethod() : "";
        } else {
            return !getCompetenceCourses().isEmpty() ? getCompetenceCourses().iterator().next().getEvaluationMethod() : "";
        }
    }

    public Set<ExecutionCourseForum> getForuns() {
        return getForumSet();
    }

    public AcademicInterval getAcademicInterval() {
        return getExecutionPeriod().getAcademicInterval();
    }

    public static ExecutionCourse readBySiglaAndExecutionPeriod(final String sigla, ExecutionSemester executionSemester) {
        for (ExecutionCourse executionCourse : executionSemester.getAssociatedExecutionCoursesSet()) {
            if (sigla.equalsIgnoreCase(executionCourse.getSigla())) {
                return executionCourse;
            }
        }
        return null;
    }

    public static ExecutionCourse readLastByExecutionYearAndSigla(final String sigla, ExecutionYear executionYear) {
        SortedSet<ExecutionCourse> result = new TreeSet<>(EXECUTION_COURSE_EXECUTION_PERIOD_COMPARATOR);
        for (final ExecutionSemester executionSemester : executionYear.getExecutionPeriodsSet()) {
            for (ExecutionCourse executionCourse : executionSemester.getAssociatedExecutionCoursesSet()) {
                if (sigla.equalsIgnoreCase(executionCourse.getSigla())) {
                    result.add(executionCourse);
                }
            }
        }
        return result.isEmpty() ? null : result.last();
    }

    public static ExecutionCourse readLastBySigla(final String sigla) {
        SortedSet<ExecutionCourse> result = new TreeSet<>(EXECUTION_COURSE_EXECUTION_PERIOD_COMPARATOR);
        for (ExecutionCourse executionCourse : Bennu.getInstance().getExecutionCoursesSet()) {
            if (sigla.equalsIgnoreCase(executionCourse.getSigla())) {
                result.add(executionCourse);
            }
        }
        return result.isEmpty() ? null : result.last();
    }

    public static ExecutionCourse readLastByExecutionIntervalAndSigla(final String sigla, ExecutionInterval executionInterval) {
        return executionInterval instanceof ExecutionSemester ? readBySiglaAndExecutionPeriod(sigla,
                (ExecutionSemester) executionInterval) : readLastByExecutionYearAndSigla(sigla,
                        (ExecutionYear) executionInterval);
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
        for (final ExecutionCourse executionCourse : getExecutionPeriod().getAssociatedExecutionCoursesSet()) {
            if (executionCourse != this && executionCourse.getSigla().equalsIgnoreCase(code)) {
                return true;
            }
        }
        return false;
    }

    public Collection<MarkSheet> getAssociatedMarkSheets() {
        Collection<MarkSheet> markSheets = new HashSet<>();
        for (CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
            markSheets.addAll(curricularCourse.getMarkSheetsByPeriod(getExecutionPeriod()));
        }
        return markSheets;
    }

    public Set<Exam> getPublishedExamsFor(final CurricularCourse curricularCourse) {

        final Set<Exam> result = new HashSet<>();
        for (final WrittenEvaluation eachEvaluation : getWrittenEvaluations()) {
            if (eachEvaluation.isExam()) {
                final Exam exam = (Exam) eachEvaluation;
                if (exam.isExamsMapPublished() && exam.contains(curricularCourse)) {
                    result.add(exam);
                }
            }
        }

        return result;

    }

    public List<AdHocEvaluation> getAssociatedAdHocEvaluations() {
        final List<AdHocEvaluation> result = new ArrayList<>();

        for (Evaluation evaluation : this.getAssociatedEvaluationsSet()) {
            if (evaluation instanceof AdHocEvaluation) {
                result.add((AdHocEvaluation) evaluation);
            }
        }
        return result;
    }

    public List<AdHocEvaluation> getOrderedAssociatedAdHocEvaluations() {
        List<AdHocEvaluation> associatedAdHocEvaluations = getAssociatedAdHocEvaluations();
        associatedAdHocEvaluations.sort(AdHocEvaluation.AD_HOC_EVALUATION_CREATION_DATE_COMPARATOR);
        return associatedAdHocEvaluations;
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
        final Set<DegreeCurricularPlan> dcps = new HashSet<>();
        for (final Attends attends : this.getAttendsSet()) {
            dcps.add(attends.getStudentCurricularPlanFromAttends().getDegreeCurricularPlan());
        }
        return dcps;
    }

    public void searchAttends(SearchExecutionCourseAttendsBean attendsBean) {
        check(this, ExecutionCoursePredicates.executionCourseLecturingTeacherOrDegreeCoordinator);
        final Predicate<Attends> filter = attendsBean.getFilters();
        final Collection<Attends> validAttends = new HashSet<>();
        final Map<Integer, Integer> enrolmentNumberMap = new HashMap<>();
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
                    attends.getEnrolment().getNumberOfTotalEnrolmentsInThisCourse(attends.getEnrolment().getExecutionPeriod());
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
        Collection<DegreeCurricularPlan> result = new HashSet<>();
        for (CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
            result.add(curricularCourse.getDegreeCurricularPlan());
        }
        return result;
    }

    public List<WrittenEvaluation> getAssociatedWrittenEvaluationsForScopeAndContext(List<Integer> curricularYears,
            DegreeCurricularPlan degreeCurricularPlan) {
        List<WrittenEvaluation> result = new ArrayList<>();
        for (WrittenEvaluation writtenEvaluation : getWrittenEvaluations()) {
            if (writtenEvaluation.hasScopeOrContextFor(curricularYears, degreeCurricularPlan)) {
                result.add(writtenEvaluation);
            }
        }
        return result;
    }

    public static List<ExecutionCourse> filterByAcademicIntervalAndDegreeCurricularPlanAndCurricularYearAndName(
            AcademicInterval academicInterval, DegreeCurricularPlan degreeCurricularPlan, CurricularYear curricularYear,
            String name) {

        // FIXME (PERIODS) must be changed when ExecutionCourse is linked to
        // ExecutionInterval
        ExecutionSemester executionSemester = (ExecutionSemester) ExecutionInterval.getExecutionInterval(academicInterval);

        return executionSemester == null ? Collections.EMPTY_LIST : executionSemester
                .getExecutionCoursesByDegreeCurricularPlanAndSemesterAndCurricularYearAndName(degreeCurricularPlan,
                        curricularYear, name);
    }

    public static Collection<ExecutionCourse> filterByAcademicInterval(AcademicInterval academicInterval) {
        // FIXME (PERIODS) must be changed when ExecutionCourse is linked to
        // ExecutionInterval
        ExecutionSemester executionSemester = (ExecutionSemester) ExecutionInterval.getExecutionInterval(academicInterval);

        return executionSemester == null ? Collections.emptyList() : executionSemester.getAssociatedExecutionCoursesSet();
    }

    public static ExecutionCourse getExecutionCourseByInitials(AcademicInterval academicInterval, String courseInitials) {

        // FIXME (PERIODS) must be changed when ExecutionCourse is linked to
        // ExecutionInterval
        ExecutionSemester executionSemester = (ExecutionSemester) ExecutionInterval.getExecutionInterval(academicInterval);
        return executionSemester.getExecutionCourseByInitials(courseInitials);
    }

    public static List<ExecutionCourse> searchByAcademicIntervalAndExecutionDegreeYearAndName(AcademicInterval academicInterval,
            ExecutionDegree executionDegree, CurricularYear curricularYear, String name) {

        // FIXME (PERIODS) must be changed when ExecutionCourse is linked to
        // ExecutionInterval
        ExecutionSemester executionSemester = (ExecutionSemester) ExecutionInterval.getExecutionInterval(academicInterval);

        return executionSemester.getExecutionCoursesByDegreeCurricularPlanAndSemesterAndCurricularYearAndName(
                executionDegree.getDegreeCurricularPlan(), curricularYear, name);
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

    @Override
    public Sender getSender() {
        return Optional.ofNullable(super.getSender()).orElseGet(this::buildDefaultSender);
    }

    @Atomic
    private Sender buildDefaultSender() {
        Sender sender = Sender.from(Installation.getInstance().getInstituitionalEmailAddress("noreply")).as(createFromName())
                .replyTo(getEmail()).members(TeacherGroup.get(this)).recipients(TeacherGroup.get(this))
                .recipients(StudentGroup.get(this)).recipients(TeacherResponsibleOfExecutionCourseGroup.get(this)).build();
        setSender(sender);
        return sender;
    }

    private String createFromName() {
        String degreeName = getDegreePresentationString();
        String courseName = getNome();
        String period = getExecutionPeriod().getQualifiedName().replace('/', '-');
        return String.format("%s (%s: %s, %s)", Unit.getInstitutionAcronym(), degreeName, courseName, period);
    }

//    private void appendName(final StringBuilder stringBuilder, final Set<String> names, final String name) {
//        if (!names.contains(name)) {
//            names.add(name);
//            if (stringBuilder.length() > 0) {
//                stringBuilder.append(" / ");
//            }
//            stringBuilder.append(name);
//        }
//    }

    public Professorship getProfessorshipForCurrentUser() {
        return this.getProfessorship(AccessControl.getPerson());
    }

    public boolean hasAnyEnrolment(ExecutionDegree executionDegree) {
        for (Attends attend : getAttendsSet()) {
            if (attend.getEnrolment() != null) {
                StudentCurricularPlan scp = attend.getRegistration().getStudentCurricularPlan(getExecutionPeriod());
                if (scp != null) {
                    ExecutionDegree studentExecutionDegree = scp.getDegreeCurricularPlan()
                            .getExecutionDegreeByYearAndCampus(getExecutionYear(), scp.getCampus(getExecutionYear()));
                    if (studentExecutionDegree == executionDegree) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean hasEnrolmentsInAnyCurricularCourse() {
        for (CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
            if (curricularCourse.hasEnrolmentForPeriod(getExecutionPeriod())) {
                return true;
            }
            if (curricularCourse.isAnual()
                    && getExecutionPeriod().getPreviousExecutionPeriod().getExecutionYear() == getExecutionYear()) {
                if (curricularCourse.hasEnrolmentForPeriod(getExecutionPeriod().getPreviousExecutionPeriod())) {
                    return true;
                }
            }
        }
        return false;
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

    @Atomic
    public void changeProjectTutorialCourse() {
        setProjectTutorialCourse(!getProjectTutorialCourse());
    }

    @Override
    public void addAssociatedCurricularCourses(final CurricularCourse curricularCourse) {
        Collection<ExecutionCourse> executionCourses = curricularCourse.getAssociatedExecutionCoursesSet();

        for (ExecutionCourse executionCourse : executionCourses) {
            if (this != executionCourse && executionCourse.getExecutionPeriod() == getExecutionPeriod()) {
                throw new DomainException("error.executionCourse.curricularCourse.already.associated");
            }
        }

        super.addAssociatedCurricularCourses(curricularCourse);
        Signal.emit(ExecutionCourse.EDITED_SIGNAL, new DomainObjectEvent<>(this));
    }

    @Atomic
    public void associateCurricularCourse(final CurricularCourse curricularCourse) {
        addAssociatedCurricularCourses(curricularCourse);
    }

    @Atomic
    public void dissociateCurricularCourse(final CurricularCourse curricularCourse) {
        super.removeAssociatedCurricularCourses(curricularCourse);
        Signal.emit(ExecutionCourse.EDITED_SIGNAL, new DomainObjectEvent<>(this));
    }

    public Double getEctsCredits() {
        Double ects = null;
        for (CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
            if (curricularCourse.isActive(getExecutionPeriod())) {
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
        final Set<OccupationPeriod> result =
                new TreeSet<>(Comparator.comparing((OccupationPeriod op) -> op.getPeriodInterval().getStart())
                        .thenComparing(AbstractDomainObject::getExternalId));
        for (final ExecutionDegree executionDegree : getExecutionDegrees()) {
            result.add(executionDegree.getPeriodLessons(getExecutionPeriod()));
        }
        return result;
    }

    public String getNomeI18() {
        return getNameI18N() != null ? getNameI18N().getContent() : null;
    }

    public String getNomeI18(Locale locale) {
        return getNameI18N() != null ? getNameI18N().getContent(locale) : null;
    }

}
