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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Function;

import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.curricularPeriod.CurricularPeriod;
import org.fenixedu.academic.domain.curricularRules.CurricularRule;
import org.fenixedu.academic.domain.curricularRules.CurricularRuleValidationType;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import org.fenixedu.academic.domain.degreeStructure.BranchCourseGroup;
import org.fenixedu.academic.domain.degreeStructure.BranchType;
import org.fenixedu.academic.domain.degreeStructure.Context;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.domain.degreeStructure.CurricularStage;
import org.fenixedu.academic.domain.degreeStructure.CycleCourseGroup;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.domain.degreeStructure.OptionalCurricularCourse;
import org.fenixedu.academic.domain.degreeStructure.ProgramConclusion;
import org.fenixedu.academic.domain.degreeStructure.RootCourseGroup;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.space.SpaceUtils;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicCalendarEntry;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicCalendarRootEntry;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicInterval;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicPeriod;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicYearCE;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicYears;
import org.fenixedu.academic.dto.CurricularPeriodInfoDTO;
import org.fenixedu.academic.predicate.AcademicPredicates;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.util.MarkType;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.commons.i18n.I18N;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.Atomic;

public class DegreeCurricularPlan extends DegreeCurricularPlan_Base {

    public static final Comparator<DegreeCurricularPlan> COMPARATOR_BY_NAME = new Comparator<DegreeCurricularPlan>() {

        @Override
        public int compare(final DegreeCurricularPlan o1, final DegreeCurricularPlan o2) {
            return o1.getName().compareTo(o2.getName());
        }

    };

    public static final Comparator<DegreeCurricularPlan> COMPARATOR_BY_PRESENTATION_NAME =
            new Comparator<DegreeCurricularPlan>() {

                @Override
                public int compare(final DegreeCurricularPlan o1, final DegreeCurricularPlan o2) {
                    final int c = o1.getPresentationName().compareTo(o2.getPresentationName());
                    return c == 0 ? DomainObjectUtil.COMPARATOR_BY_ID.compare(o1, o2) : c;
                }

            };

    /**
     * This might look a strange comparator, but the idea is to show a list of
     * degree curricular plans according to, in the following order: 1. It's
     * degree type 2. Reverse order of ExecutionDegrees 3. It's degree code (in
     * order to roughly order them by prebolonha/bolonha) OR reverse order of
     * their own name
     *
     * For an example, see the coordinator's portal.
     */
    public static final Comparator<DegreeCurricularPlan> DEGREE_CURRICULAR_PLAN_COMPARATOR_BY_DEGREE_TYPE_AND_EXECUTION_DEGREE_AND_DEGREE_CODE =
            new Comparator<DegreeCurricularPlan>() {

                @Override
                public int compare(final DegreeCurricularPlan o1, final DegreeCurricularPlan o2) {
                    final int degreeTypeCompare = o1.getDegreeType().getName().compareTo(o2.getDegreeType().getName());
                    if (degreeTypeCompare != 0) {
                        return degreeTypeCompare;
                    }

                    int finalCompare = o1.getDegree().getSigla().compareTo(o2.getDegree().getSigla());
                    if (finalCompare == 0) {
                        finalCompare = o2.getName().compareTo(o1.getName());
                    }
                    if (finalCompare == 0) {
                        finalCompare = o1.getExternalId().compareTo(o2.getExternalId());
                    }
                    return finalCompare;
                }

            };

    protected DegreeCurricularPlan() {
        super();
        super.setRootDomainObject(Bennu.getInstance());
        super.setApplyPreviousYearsEnrolmentRule(Boolean.TRUE);
        super.setCurricularRuleValidationType(CurricularRuleValidationType.SEMESTER);
    }

    private DegreeCurricularPlan(final Degree degree, final String name, final GradeScale gradeScale) {
        this();
        if (degree == null) {
            throw new DomainException("degreeCurricularPlan.degree.not.null");
        }
        if (name == null) {
            throw new DomainException("degreeCurricularPlan.name.not.null");
        }
        super.setDegree(degree);
        super.setName(name);
        super.setGradeScale(gradeScale);
    }

    protected DegreeCurricularPlan(final Degree degree, final String name, final DegreeCurricularPlanState state,
            final Date inicialDate, final Date endDate, final Integer degreeDuration, final Integer minimalYearForOptionalCourses,
            final Double neededCredits, final MarkType markType, final Integer numerusClausus, final String annotation,
            final GradeScale gradeScale) {

        this(degree, name, gradeScale);
        super.setCurricularStage(CurricularStage.DRAFT);
        super.setState(state);

        oldStructureFieldsChange(inicialDate, endDate, degreeDuration, minimalYearForOptionalCourses, neededCredits, markType,
                numerusClausus, annotation);
    }

    private void oldStructureFieldsChange(final Date inicialDate, final Date endDate, final Integer degreeDuration,
            final Integer minimalYearForOptionalCourses, final Double neededCredits, final MarkType markType,
            final Integer numerusClausus, final String annotation) {

        if (inicialDate == null) {
            throw new DomainException("degreeCurricularPlan.inicialDate.not.null");
        } else if (degreeDuration == null) {
            throw new DomainException("degreeCurricularPlan.degreeDuration.not.null");
        } else if (minimalYearForOptionalCourses == null) {
            throw new DomainException("degreeCurricularPlan.minimalYearForOptionalCourses.not.null");
        }

        this.setInitialDateYearMonthDay(new YearMonthDay(inicialDate));
        this.setEndDateYearMonthDay(endDate != null ? new YearMonthDay(endDate) : null);
        this.setDegreeDuration(degreeDuration);
        this.setMinimalYearForOptionalCourses(minimalYearForOptionalCourses);
        this.setNeededCredits(neededCredits);
        this.setMarkType(markType);
        this.setNumerusClausus(numerusClausus);
        this.setAnotation(annotation);
    }

    DegreeCurricularPlan(final Degree degree, final String name, final GradeScale gradeScale, final Person creator,
            final CurricularPeriod curricularPeriod) {
        this(degree, name, gradeScale);

        if (creator == null) {
            throw new DomainException("degreeCurricularPlan.creator.not.null");
        }

        if (curricularPeriod == null) {
            throw new DomainException("degreeCurricularPlan.curricularPeriod.not.null");
        }
        createDefaultCourseGroups();

        setCurricularPlanMembersGroup(creator.getUser().groupOf());
        setDegreeStructure(curricularPeriod);
        setState(DegreeCurricularPlanState.ACTIVE);

        newStructureFieldsChange(CurricularStage.DRAFT, null);
    }

    private void createDefaultCourseGroups() {
        RootCourseGroup.createRoot(this, getName(), getName());
    }

    private void newStructureFieldsChange(final CurricularStage curricularStage, final ExecutionYear beginExecutionYear) {

        if (curricularStage == null) {
            throw new DomainException("degreeCurricularPlan.curricularStage.not.null");
        } else if (!getExecutionDegreesSet().isEmpty() && curricularStage == CurricularStage.DRAFT) {
            throw new DomainException("degreeCurricularPlan.has.already.been.executed");
        } else if (curricularStage == CurricularStage.APPROVED) {
            approve(beginExecutionYear);
        } else {
            setCurricularStage(curricularStage);
        }
    }

    private void commonFieldsChange(final String name, final GradeScale gradeScale) {
        if (name == null) {
            throw new DomainException("degreeCurricularPlan.name.not.null");
        }

        // assert unique pair name/degree
        for (final DegreeCurricularPlan dcp : this.getDegree().getDegreeCurricularPlansSet()) {
            if (dcp != this && dcp.getName().equalsIgnoreCase(name)) {
                throw new DomainException("error.degreeCurricularPlan.existing.name.and.degree");
            }
        }
        this.setName(name);

        this.setGradeScale(gradeScale);
    }

    public void edit(final String name, final DegreeCurricularPlanState state, final Date inicialDate, final Date endDate,
            final Integer degreeDuration, final Integer minimalYearForOptionalCourses, final Double neededCredits,
            final MarkType markType, final Integer numerusClausus, final String annotation, final GradeScale gradeScale) {

        commonFieldsChange(name, gradeScale);
        oldStructureFieldsChange(inicialDate, endDate, degreeDuration, minimalYearForOptionalCourses, neededCredits, markType,
                numerusClausus, annotation);

        this.setState(state);
    }

    public void edit(final String name, final CurricularStage stage, final DegreeCurricularPlanState state,
            final GradeScale gradeScale, final ExecutionYear beginExecutionInterval) {

        /*
         * Allow degree curricular plans approved without program conclusion 
        if (stage.equals(CurricularStage.APPROVED)
                && !getAllCoursesGroups().stream().map(CourseGroup::getProgramConclusion).anyMatch(Objects::nonNull)) {
            throw new DomainException("error.degreeCurricularPlan.missing.program.conclusion");
        }
        */

        if (isApproved()
                && (name != null && !getName().equals(name) || gradeScale != null && !getGradeScale().equals(gradeScale))) {
            throw new DomainException("error.degreeCurricularPlan.already.approved");
        } else {
            commonFieldsChange(name, gradeScale);
        }

        newStructureFieldsChange(stage, beginExecutionInterval);

        this.setState(state);
        this.getRoot().setName(name);
        this.getRoot().setNameEn(name);
    }

    private void approve(final ExecutionYear beginExecutionYear) {
        if (isApproved()) {
            return;
        }

        if (!getCanModify().booleanValue()) {
            throw new DomainException("error.degreeCurricularPlan.already.approved");
        }

        final ExecutionInterval beginExecutionPeriod;
        if (beginExecutionYear == null) {
            throw new DomainException("error.invalid.execution.year");
        } else {
            beginExecutionPeriod = beginExecutionYear.getFirstExecutionPeriod();
            if (beginExecutionPeriod == null) {
                throw new DomainException("error.invalid.execution.period");
            }
        }

        checkIfCurricularCoursesBelongToApprovedCompetenceCourses();
        initBeginExecutionPeriodForDegreeCurricularPlan(getRoot(), beginExecutionPeriod);
        setCurricularStage(CurricularStage.APPROVED);
    }

    private void checkIfCurricularCoursesBelongToApprovedCompetenceCourses() {
        final List<String> notApprovedCompetenceCourses = new ArrayList<>();
        for (final DegreeModule degreeModule : getDcpDegreeModules(CurricularCourse.class)) {
            final CurricularCourse curricularCourse = (CurricularCourse) degreeModule;
            if (!curricularCourse.isOptionalCurricularCourse() && !curricularCourse.getCompetenceCourse().isApproved()) {
                notApprovedCompetenceCourses.add(curricularCourse.getCompetenceCourse().getDepartmentUnit().getName() + " > "
                        + curricularCourse.getCompetenceCourse().getName());
            }
        }
        if (!notApprovedCompetenceCourses.isEmpty()) {
            final String[] result = new String[notApprovedCompetenceCourses.size()];
            throw new DomainException("error.not.all.competence.courses.are.approved",
                    notApprovedCompetenceCourses.toArray(result));
        }
    }

    private void initBeginExecutionPeriodForDegreeCurricularPlan(final CourseGroup courseGroup,
            final ExecutionInterval beginExecutionPeriod) {

        if (beginExecutionPeriod == null) {
            throw new DomainException("");
        }

        for (final CurricularRule curricularRule : courseGroup.getCurricularRulesSet()) {
            curricularRule.setBegin(beginExecutionPeriod);
        }
        for (final Context context : courseGroup.getChildContextsSet()) {
            context.setBeginExecutionPeriod(beginExecutionPeriod);
            if (!context.getChildDegreeModule().isLeaf()) {
                initBeginExecutionPeriodForDegreeCurricularPlan((CourseGroup) context.getChildDegreeModule(),
                        beginExecutionPeriod);
            }
        }
    }

    public boolean isBolonhaDegree() {
        return getDegree().isBolonhaDegree();
    }

    @Deprecated
    public boolean isEmpty() {
        return false;
    }

    public boolean isApproved() {
        return getCurricularStage() == CurricularStage.APPROVED;
    }

    public boolean isDraft() {
        return getCurricularStage() == CurricularStage.DRAFT;
    }

    public boolean isActive() {
        return getState() == DegreeCurricularPlanState.ACTIVE;
    }

    private Boolean getCanBeDeleted() {
        return canDeleteRoot() && getStudentCurricularPlansSet().isEmpty() && getCurricularCoursesSet().isEmpty()
                && getExecutionDegreesSet().isEmpty();
    }

    private boolean canDeleteRoot() {
        return getRoot().getCanBeDeleted();
    }

    public void delete() {
        if (getCanBeDeleted()) {
            setDegree(null);
            getRoot().delete();
            if (getDegreeStructure() != null) {
                getDegreeStructure().delete();
            }
            setShift(null);
            setMembersGroup(null);
            setRootDomainObject(null);
            deleteDomainObject();
        } else {
            throw new DomainException("error.degree.curricular.plan.cant.delete");
        }
    }

    public String print() {
        StringBuilder dcp = new StringBuilder();

        dcp.append("[DCP ").append(this.getExternalId()).append("] ").append(this.getName()).append("\n");
        this.getRoot().print(dcp, "", null);

        return dcp.toString();
    }

    public GradeScale getGradeScaleChain() {
        return super.getGradeScale() != null ? super.getGradeScale() : getDegree().getGradeScaleChain();
    }

    @Deprecated
    public ExecutionDegree getExecutionDegreeByYear(final ExecutionYear executionYear) {
        for (final ExecutionDegree executionDegree : getExecutionDegreesSet()) {
            if (executionDegree.getExecutionYear() == executionYear) {
                return executionDegree;
            }
        }
        return null;
    }

    // FIXME: Optimization Required
    public ExecutionDegree getExecutionDegreeByAcademicInterval(final AcademicInterval academicInterval) {
        AcademicCalendarEntry academicCalendarEntry = academicInterval.getAcademicCalendarEntry();
        while (!(academicCalendarEntry instanceof AcademicCalendarRootEntry)) {
            if (academicCalendarEntry instanceof AcademicYearCE) {
                ExecutionYear year = ExecutionYear.getExecutionYear((AcademicYearCE) academicCalendarEntry);
                for (ExecutionDegree executionDegree : getExecutionDegreesSet()) {
                    if (executionDegree.getExecutionYear().getAcademicInterval().equals(year.getAcademicInterval())) {
                        return executionDegree;
                    }
                }
            }

            academicCalendarEntry = academicCalendarEntry.getParentEntry();
        }

        return null;
    }

    public Set<ExecutionYear> getExecutionYears() {
        Set<ExecutionYear> result = new HashSet<>();
        for (final ExecutionDegree executionDegree : getExecutionDegreesSet()) {
            result.add(executionDegree.getExecutionYear());
        }
        return result;
    }

    public ExecutionYear getMostRecentExecutionYear() {
        return getMostRecentExecutionDegree().getExecutionYear();
    }

    public ExecutionDegree getExecutionDegreeByYearAndCampus(final ExecutionYear executionYear, final Space campus) {
        for (final ExecutionDegree executionDegree : getExecutionDegreesSet()) {
            if (executionDegree.getExecutionYear() == executionYear && executionDegree.getCampus() == campus) {
                return executionDegree;
            }
        }
        return null;
    }

    public boolean hasExecutionDegreeByYearAndCampus(final ExecutionYear executionYear, final Space campus) {
        return getExecutionDegreeByYearAndCampus(executionYear, campus) != null;
    }

    public boolean hasAnyExecutionDegreeFor(final ExecutionYear executionYear) {
        for (final ExecutionDegree executionDegree : this.getExecutionDegreesSet()) {
            if (executionDegree.getExecutionYear() == executionYear) {
                return true;
            }
        }
        return false;
    }

    public boolean hasExecutionDegreeFor(final ExecutionYear executionYear) {
        return getExecutionDegreeByYear(executionYear) != null;
    }

    public ExecutionDegree getMostRecentExecutionDegree() {
        if (getExecutionDegreesSet().isEmpty()) {
            return null;
        }

        final ExecutionYear currentYear = ExecutionYear.findCurrent(getDegree().getCalendar());
        ExecutionDegree result = getExecutionDegreeByYear(currentYear);
        if (result != null) {
            return result;
        }

        final List<ExecutionDegree> sorted = new ArrayList<>(getExecutionDegreesSet());
        Collections.sort(sorted, ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_YEAR);

        final ExecutionDegree first = sorted.iterator().next();
        if (sorted.size() == 1) {
            return first;
        }

        if (first.getExecutionYear().isAfter(currentYear)) {
            return first;
        } else {
            final ListIterator<ExecutionDegree> iter = sorted.listIterator(sorted.size());
            while (iter.hasPrevious()) {
                final ExecutionDegree executionDegree = iter.previous();
                if (executionDegree.getExecutionYear().isBeforeOrEquals(currentYear)) {
                    return executionDegree;
                }
            }
        }

        return null;
    }

    public ExecutionDegree getFirstExecutionDegree() {
        return getExecutionDegreesSet().stream().min(ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_YEAR).orElse(null);
    }

    public Set<ExecutionCourse> getExecutionCourses(final ExecutionInterval executionInterval) {
        final Set<ExecutionCourse> result = new HashSet<>();
        addExecutionCoursesForExecutionPeriod(result, executionInterval, getRoot().getChildContextsSet());
        return result;
    }

    private void addExecutionCoursesForExecutionPeriod(final Set<ExecutionCourse> executionCourses,
            final ExecutionInterval executionInterval, final Set<Context> contexts) {
        for (final Context context : contexts) {
            final DegreeModule degreeModule = context.getChildDegreeModule();
            if (degreeModule instanceof CurricularCourse) {
                final CurricularCourse curricularCourse = (CurricularCourse) degreeModule;
                executionCourses.addAll(curricularCourse.getExecutionCoursesByExecutionPeriod(executionInterval));
            } else if (degreeModule instanceof CourseGroup) {
                final CourseGroup courseGroup = (CourseGroup) degreeModule;
                addExecutionCoursesForExecutionPeriod(executionCourses, executionInterval, courseGroup.getChildContextsSet());
            }
        }
    }

    /**
     * @deprecated use {@link #getExecutionCoursesByExecutionInterval(ExecutionInterval)}
     */
    @Deprecated
    public Set<ExecutionCourse> getExecutionCoursesByExecutionPeriod(final ExecutionInterval executionInterval) {
        return getExecutionCourses(executionInterval);
    }

    public Set<CurricularCourse> getAllCurricularCourses() {
        final Set<DegreeModule> curricularCourses = new TreeSet<DegreeModule>(DegreeModule.COMPARATOR_BY_NAME) {
            @Override
            public boolean add(final DegreeModule degreeModule) {
                return degreeModule instanceof CurricularCourse && super.add(degreeModule);
            }
        };
        getRoot().getAllDegreeModules(curricularCourses);
        return (Set) curricularCourses;
    }

    public List<CurricularCourse> getCurricularCoursesWithExecutionIn(final ExecutionYear executionYear) {
        List<CurricularCourse> curricularCourses = new ArrayList<>();
        for (CurricularCourse curricularCourse : getCurricularCoursesSet()) {
            for (ExecutionInterval executionInterval : executionYear.getChildIntervals()) {
                List<ExecutionCourse> executionCourses = curricularCourse.getExecutionCoursesByExecutionPeriod(executionInterval);
                if (!executionCourses.isEmpty()) {
                    curricularCourses.add(curricularCourse);
                    break;
                }
            }
        }
        return curricularCourses;
    }

    public List<CurricularCourse> getCurricularCoursesByBasicAttribute(final Boolean basic) {
        if (isBolonhaDegree()) {
            return Collections.emptyList();
        }

        final List<CurricularCourse> curricularCourses = new ArrayList<>();
        for (final CurricularCourse curricularCourse : getCurricularCoursesSet()) {
            if (curricularCourse.getBasic().equals(basic)) {
                curricularCourses.add(curricularCourse);
            }
        }
        return curricularCourses;
    }

    // -------------------------------------------------------------
    // BEGIN: Only for enrollment purposes
    // -------------------------------------------------------------

    public CurricularCourse getCurricularCourseByCode(final String code) {
        for (CurricularCourse curricularCourse : getCurricularCoursesSet()) {
            if (curricularCourse.getCode() != null && curricularCourse.getCode().equals(code)) {
                return curricularCourse;
            }
        }
        return null;
    }

    public CurricularCourse getCurricularCourseByAcronym(final String acronym) {
        for (CurricularCourse curricularCourse : getCurricularCoursesSet()) {
            if (curricularCourse.getAcronym().equals(acronym)) {
                return curricularCourse;
            }
        }
        return null;
    }

    @Override
    public Set<CurricularCourse> getCurricularCoursesSet() {
        return this.getCurricularCourses((ExecutionYear) null);
    }

    public Set<CurricularCourse> getCurricularCourses(final ExecutionInterval executionInterval) {
        final Set<CurricularCourse> curricularCourses = new HashSet<>();
        for (final CurricularCourse curricularCourse : super.getCurricularCoursesSet()) {
            if (curricularCourse.hasScopeInGivenSemesterAndCurricularYearInDCP(null, null, executionInterval)) {
                curricularCourses.add(curricularCourse);
            }
        }
        final ExecutionYear executionYear = executionInterval.getExecutionYear();
        for (final DegreeModule degreeModule : getDcpDegreeModules(CurricularCourse.class, executionYear)) {
            curricularCourses.add((CurricularCourse) degreeModule);
        }
        return curricularCourses;
    }

    /**
     * Method to get a filtered list of a dcp's curricular courses, with at
     * least one open context in the given execution year
     *
     * @return All curricular courses that are present in the dcp
     */
    private Set<CurricularCourse> getCurricularCourses(final ExecutionYear executionYear) {
        final Set<CurricularCourse> result = new HashSet<>();
        for (final DegreeModule degreeModule : getDcpDegreeModules(CurricularCourse.class, executionYear)) {
            result.add((CurricularCourse) degreeModule);
        }
        return result;
    }

    public void applyToCurricularCourses(final ExecutionYear executionYear, final Predicate predicate) {
        getRoot().applyToCurricularCourses(executionYear, predicate);
    }

    /**
     * Method to get an unfiltered list of a bolonha dcp's competence courses
     *
     * @return All competence courses that were or still are present in the dcp,
     *         ordered by name
     */
    public List<CompetenceCourse> getCompetenceCourses() {
        if (isBolonhaDegree()) {
            return getCompetenceCourses(null);
        } else {
            return new ArrayList<>();
        }

    }

    /**
     * Method to get a filtered list of a dcp's competence courses in the given
     * execution year. Each competence courses is connected with a curricular
     * course with at least one open context in the execution year
     *
     * @return All competence courses that are present in the dcp
     */
    public List<CompetenceCourse> getCompetenceCourses(final ExecutionYear executionYear) {
        SortedSet<CompetenceCourse> result = new TreeSet<>(CompetenceCourse.COMPETENCE_COURSE_COMPARATOR_BY_NAME);

        if (isBolonhaDegree()) {
            for (final CurricularCourse curricularCourse : getCurricularCourses(executionYear)) {
                if (!curricularCourse.isOptionalCurricularCourse()) {
                    result.add(curricularCourse.getCompetenceCourse());
                }
            }
            return new ArrayList<>(result);
        } else {
            return new ArrayList<>();
        }
    }

    public Set<CurricularCourse> getActiveCurricularCourses(final ExecutionInterval executionInterval) {
        final Set<CurricularCourse> result = new HashSet<>();
        for (final CurricularCourse curricularCourse : getCurricularCoursesSet()) {
            if (curricularCourse.hasAnyActiveContext(executionInterval)) {
                result.add(curricularCourse);
            }
        }
        return result;
    }

    public CourseGroup createCourseGroup(final CourseGroup parentCourseGroup, final String name, final String nameEn,
            final ExecutionInterval begin, final ExecutionInterval end, final ProgramConclusion programConclusion) {
        return new CourseGroup(parentCourseGroup, name, nameEn, begin, end, programConclusion);
    }

    public BranchCourseGroup createBranchCourseGroup(final CourseGroup parentCourseGroup, final String name, final String nameEn,
            final BranchType branchType, final ExecutionInterval begin, final ExecutionInterval end) {
        return new BranchCourseGroup(parentCourseGroup, name, nameEn, branchType, begin, end);
    }

    public CurricularCourse createCurricularCourse(final Double weight, final String prerequisites, final String prerequisitesEn,
            final CurricularStage curricularStage, final CompetenceCourse competenceCourse, final CourseGroup parentCourseGroup,
            final CurricularPeriod curricularPeriod, final ExecutionInterval begin, final ExecutionInterval end) {

        if (competenceCourse.getCurricularCourse(this) != null) {
            throw new DomainException("competenceCourse.already.has.a.curricular.course.in.degree.curricular.plan");
        }
        checkIfAnualBeginsInFirstPeriod(competenceCourse, curricularPeriod);

        return new CurricularCourse(weight, prerequisites, prerequisitesEn, curricularStage, competenceCourse, parentCourseGroup,
                curricularPeriod, begin, end);
    }

    public CurricularCourse createOptionalCurricularCourse(final CourseGroup parentCourseGroup, final String name,
            final String nameEn, final CurricularStage curricularStage, final CurricularPeriod curricularPeriod,
            final ExecutionInterval begin, final ExecutionInterval end) {

        return new OptionalCurricularCourse(parentCourseGroup, name, nameEn, curricularStage, curricularPeriod, begin, end);
    }

    private void checkIfAnualBeginsInFirstPeriod(final CompetenceCourse competenceCourse,
            final CurricularPeriod curricularPeriod) {
        if (competenceCourse.isAnual() && !curricularPeriod.hasChildOrderValue(1)) {
            throw new DomainException("competenceCourse.anual.but.trying.to.associate.curricular.course.not.to.first.period");
        }
    }

    public List<DegreeModule> getDcpDegreeModules(final Class<? extends DegreeModule> clazz) {
        return getDcpDegreeModules(clazz, (ExecutionYear) null);
    }

    public List<DegreeModule> getDcpDegreeModules(final Class<? extends DegreeModule> clazz, final ExecutionYear executionYear) {
        return new ArrayList<>(getRoot().collectAllChildDegreeModules(clazz, executionYear));
    }

    public List<List<DegreeModule>> getDcpDegreeModulesIncludingFullPath(final Class<? extends DegreeModule> clazz,
            final ExecutionYear executionYear) {

        final List<List<DegreeModule>> result = new ArrayList<>();
        final List<DegreeModule> path = new ArrayList<>();

        if (clazz.isAssignableFrom(CourseGroup.class)) {
            path.add(this.getRoot());

            result.add(path);
        }

        this.getRoot().collectChildDegreeModulesIncludingFullPath(clazz, result, path, executionYear);

        return result;
    }

    public Boolean getUserCanBuild() {
        Person person = AccessControl.getPerson();
        return AcademicPredicates.MANAGE_DEGREE_CURRICULAR_PLANS.evaluate(this.getDegree())
                || this.getCurricularPlanMembersGroup().isMember(person.getUser());
    }

    public Boolean getCanModify() {
        if (isApproved()) {
            return false;
        }

        final Collection<ExecutionDegree> executionDegrees = getExecutionDegreesSet();
        return executionDegrees.size() > 1 ? false : executionDegrees.isEmpty()
                || executionDegrees.iterator().next().getExecutionYear().isCurrent();
    }

    public Group getCurricularPlanMembersGroup() {
        return getMembersGroup() != null ? getMembersGroup().toGroup() : Group.nobody();
    }

    public void setCurricularPlanMembersGroup(final Group group) {
        setMembersGroup(group.toPersistentGroup());
    }

    @Atomic
    public void editDuration(final AcademicPeriod duration) {

        if (duration == null) {
            throw new DomainException("error.degreeCurricularPlan.duration.cannot.be.null");
        }

        if (!getDegreeStructure().getAcademicPeriod().equals(duration)) {
            setDegreeStructure(new CurricularPeriod(duration));
        }
    }

    @Override
    public void setDegreeStructure(final CurricularPeriod degreeStructure) {

        if (degreeStructure == null || !(degreeStructure.getAcademicPeriod() instanceof AcademicYears)) {
            throw new DomainException("error.degreeCurricularPlan.degreeStructure.must.be.specified.in.years");
        }

        final CurricularPeriod currentDegreeStructure = super.getDegreeStructure();
        if (currentDegreeStructure != degreeStructure) {

            if (!getAllCurricularCourses().isEmpty()) {
                throw new DomainException(
                        "error.degreeCurricularPlan.degreeStructure.cannot.be.changed.when.already.has.curricular.courses");
            }

            //not the most elegant solution but avoids breaking API
            if (currentDegreeStructure != null) {
                currentDegreeStructure.delete();
            }

        }

        super.setDegreeStructure(degreeStructure);
    }

    public String getPresentationName() {
        return getPresentationName(ExecutionYear.findCurrent(getDegree().getCalendar()), I18N.getLocale());
    }

    public String getPresentationName(final ExecutionYear executionYear) {
        return getPresentationName(executionYear, I18N.getLocale());
    }

    public String getPresentationName(final ExecutionYear executionYear, final Locale locale) {
        return getDegree().getPresentationName(executionYear, locale) + " - " + getName();
    }

    // -------------------------------------------------------------
    // read static methods
    // -------------------------------------------------------------

    /**
     * @deprecated DegreeCurricularPlans cannot be empty anymore so usage of this method is unecessary
     */
    @Deprecated
    public static List<DegreeCurricularPlan> readNotEmptyDegreeCurricularPlans() {
        return new ArrayList<>(Bennu.getInstance().getDegreeCurricularPlansSet());
    }

    public static Set<DegreeCurricularPlan> readBolonhaDegreeCurricularPlans() {
        final Set<DegreeCurricularPlan> result = new HashSet<>();

        for (final Degree degree : Degree.readBolonhaDegrees()) {
            result.addAll(degree.getDegreeCurricularPlansSet());
        }

        return result;
    }

    public static Set<DegreeCurricularPlan> readPreBolonhaDegreeCurricularPlans() {
        final Set<DegreeCurricularPlan> result = new HashSet<>();

        for (final Degree degree : Degree.readOldDegrees()) {
            result.addAll(degree.getDegreeCurricularPlansSet());
        }

        return result;
    }

    static public List<DegreeCurricularPlan> readByCurricularStage(final CurricularStage curricularStage) {
        final List<DegreeCurricularPlan> result = new ArrayList<>();
        for (final DegreeCurricularPlan degreeCurricularPlan : readNotEmptyDegreeCurricularPlans()) {
            if (degreeCurricularPlan.getCurricularStage().equals(curricularStage)) {
                result.add(degreeCurricularPlan);
            }
        }
        return result;
    }

    /**
     * If state is null then just degree type is checked
     */
    public static List<DegreeCurricularPlan> readByDegreeTypeAndState(final java.util.function.Predicate<DegreeType> degreeType,
            final DegreeCurricularPlanState state) {
        List<DegreeCurricularPlan> result = new ArrayList<>();
        for (DegreeCurricularPlan degreeCurricularPlan : readNotEmptyDegreeCurricularPlans()) {
            if (degreeType.test(degreeCurricularPlan.getDegree().getDegreeType())
                    && (state == null || degreeCurricularPlan.getState() == state)) {

                result.add(degreeCurricularPlan);
            }
        }
        return result;
    }

    /**
     * If state is null then just degree type is checked
     */
    public static List<DegreeCurricularPlan> readByDegreeTypesAndState(final java.util.function.Predicate<DegreeType> predicate,
            final DegreeCurricularPlanState state) {
        List<DegreeCurricularPlan> result = new ArrayList<>();
        for (DegreeCurricularPlan degreeCurricularPlan : readNotEmptyDegreeCurricularPlans()) {
            if (predicate.test(degreeCurricularPlan.getDegree().getDegreeType())
                    && (state == null || degreeCurricularPlan.getState() == state)) {

                result.add(degreeCurricularPlan);
            }
        }
        return result;
    }

    public static DegreeCurricularPlan readByNameAndDegreeSigla(final String name, final String degreeSigla) {
        for (final DegreeCurricularPlan degreeCurricularPlan : readNotEmptyDegreeCurricularPlans()) {
            if (degreeCurricularPlan.getName().equalsIgnoreCase(name)
                    && degreeCurricularPlan.getDegree().getSigla().equalsIgnoreCase(degreeSigla)) {
                return degreeCurricularPlan;
            }
        }
        return null;
    }

    public ExecutionDegree createExecutionDegree(final ExecutionYear executionYear, final Space campus,
            final Boolean publishedExamMap) {

        if (isBolonhaDegree() && isDraft()) {
            throw new DomainException("degree.curricular.plan.not.approved.cannot.create.execution.degree", this.getName());
        }

        if (this.hasAnyExecutionDegreeFor(executionYear)) {
            throw new DomainException("degree.curricular.plan.already.has.execution.degree.for.this.year", this.getName(),
                    executionYear.getYear());
        }

        return new ExecutionDegree(this, executionYear, campus, publishedExamMap);
    }

    public CurricularPeriod getCurricularPeriodFor(final int year, final int semester) {
        final CurricularPeriodInfoDTO[] curricularPeriodInfos = buildCurricularPeriodInfoDTOsFor(year, semester);
        return getDegreeStructure().getCurricularPeriod(curricularPeriodInfos);
    }

    private CurricularPeriodInfoDTO[] buildCurricularPeriodInfoDTOsFor(final int year, final int semester) {
        final CurricularPeriodInfoDTO[] curricularPeriodInfos;
        if (getDurationInYears() > 1) {

            curricularPeriodInfos = new CurricularPeriodInfoDTO[] { new CurricularPeriodInfoDTO(year, AcademicPeriod.YEAR),
                    new CurricularPeriodInfoDTO(semester, AcademicPeriod.SEMESTER) };

        } else {
            curricularPeriodInfos =
                    new CurricularPeriodInfoDTO[] { new CurricularPeriodInfoDTO(semester, AcademicPeriod.SEMESTER) };
        }
        return curricularPeriodInfos;
    }

    public CurricularPeriod createCurricularPeriodFor(final int year, final int semester) {
        final CurricularPeriodInfoDTO[] curricularPeriodInfos = buildCurricularPeriodInfoDTOsFor(year, semester);

        return getDegreeStructure().addCurricularPeriod(curricularPeriodInfos);
    }

    @Override
    public YearMonthDay getInitialDateYearMonthDay() {
        final ExecutionDegree firstExecutionDegree = getFirstExecutionDegree();
        return firstExecutionDegree != null ? firstExecutionDegree.getExecutionYear()
                .getBeginDateYearMonthDay() : super.getInitialDateYearMonthDay();
    }

    public Collection<StudentCurricularPlan> getActiveStudentCurricularPlans() {
        final Collection<StudentCurricularPlan> result = new HashSet<>();

        for (StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
            if (studentCurricularPlan.isActive()) {
                result.add(studentCurricularPlan);
            }
        }

        return result;
    }

    public Set<Registration> getRegistrations() {
        final Set<Registration> registrations = new HashSet<>();

        for (StudentCurricularPlan studentCurricularPlan : getActiveStudentCurricularPlans()) {
            registrations.add(studentCurricularPlan.getRegistration());
        }

        return registrations;
    }

    public Collection<Registration> getActiveRegistrations() {
        final Collection<Registration> result = new HashSet<>();

        for (StudentCurricularPlan studentCurricularPlan : getActiveStudentCurricularPlans()) {
            final Registration registration = studentCurricularPlan.getRegistration();

            if (registration.isActive()) {
                result.add(registration);
            }
        }

        return result;
    }

    public boolean isPast() {
        return getState().equals(DegreeCurricularPlanState.PAST);
    }

    public Space getCampus(final ExecutionYear executionYear) {
        for (final ExecutionDegree executionDegree : getExecutionDegreesSet()) {
            if (executionDegree.getExecutionYear() == executionYear) {
                return executionDegree.getCampus();
            }
        }

        return null;
    }

    public Space getCurrentCampus() {
        for (final ExecutionDegree executionDegree : getExecutionDegreesSet()) {
            final ExecutionYear executionYear = executionDegree.getExecutionYear();
            if (executionYear.isCurrent()) {
                return executionDegree.getCampus();
            }
        }

        return null;
    }

    public Space getLastCampus() {
        if (!getExecutionDegreesSet().isEmpty()) {
            return getMostRecentExecutionDegree().getCampus();
        }
        return SpaceUtils.getDefaultCampus();
    }

    @Override
    public Integer getDegreeDuration() {
        final Integer degreeDuration = super.getDegreeDuration();
        return degreeDuration == null ? getDurationInYears() : degreeDuration;
    }

    public DegreeType getDegreeType() {
        return getDegree().getDegreeType();
    }

    public boolean isDegreeOrBolonhaDegreeOrBolonhaIntegratedMasterDegree() {
        return getDegreeType().isDegreeOrBolonhaDegreeOrBolonhaIntegratedMasterDegree();
    }

    public boolean isFirstCycle() {
        return getDegree().isFirstCycle();
    }

    public CycleCourseGroup getFirstCycleCourseGroup() {
        return isFirstCycle() ? getRoot().getFirstCycleCourseGroup() : null;
    }

    public boolean isSecondCycle() {
        return getDegree().isSecondCycle();
    }

    public CycleCourseGroup getSecondCycleCourseGroup() {
        return isSecondCycle() ? getRoot().getSecondCycleCourseGroup() : null;
    }

    public CycleCourseGroup getThirdCycleCourseGroup() {
        return isBolonhaDegree() ? getRoot().getThirdCycleCourseGroup() : null;
    }

    public CycleCourseGroup getCycleCourseGroup(final CycleType cycleType) {
        return isBolonhaDegree() ? getRoot().getCycleCourseGroup(cycleType) : null;
    }

    public CycleCourseGroup getLastOrderedCycleCourseGroup() {
        return isBolonhaDegree() ? getCycleCourseGroup(getDegreeType().getLastOrderedCycleType()) : null;
    }

    public String getGraduateTitle(final ExecutionYear executionYear, final ProgramConclusion programConclusion,
            final Locale locale) {
        return programConclusion.groupFor(this).map(cg -> cg.getGraduateTitle(executionYear, locale)).orElse(null);
    }

    public boolean hasDegreeModule(final DegreeModule degreeModule) {
        return getRoot().hasDegreeModule(degreeModule);
    }

    public final List<StudentCurricularPlan> getLastStudentCurricularPlan() {
        List<StudentCurricularPlan> studentCurricularPlans = new ArrayList<>();
        for (StudentCurricularPlan studentCurricularPlan : this.getStudentCurricularPlansSet()) {
            studentCurricularPlans.add(studentCurricularPlan.getRegistration().getLastStudentCurricularPlan());

        }
        return studentCurricularPlans;
    }

    public Set<CourseGroup> getAllCoursesGroups() {
        final Set<DegreeModule> courseGroups = new TreeSet<DegreeModule>(DegreeModule.COMPARATOR_BY_NAME) {
            @Override
            public boolean add(final DegreeModule degreeModule) {
                return degreeModule instanceof CourseGroup && super.add(degreeModule);
            }
        };
        courseGroups.add(getRoot());
        getRoot().getAllDegreeModules(courseGroups);
        return (Set) courseGroups;
    }

    public Set<BranchCourseGroup> getAllBranches() {
        final Set<DegreeModule> branches = new TreeSet<DegreeModule>(DegreeModule.COMPARATOR_BY_NAME) {
            @Override
            public boolean add(final DegreeModule degreeModule) {
                return degreeModule instanceof BranchCourseGroup && super.add(degreeModule);
            }
        };
        branches.add(getRoot());
        getRoot().getAllDegreeModules(branches);
        return (Set) branches;
    }

    public Set<BranchCourseGroup> getBranchesByType(final org.fenixedu.academic.domain.degreeStructure.BranchType branchType) {
        final Set<BranchCourseGroup> branchesByType = new TreeSet<>(DegreeModule.COMPARATOR_BY_NAME);
        final Set<BranchCourseGroup> branches = getAllBranches();
        if (branches == null) {
            return null;
        }
        for (BranchCourseGroup branch : branches) {
            if (branch.getBranchType() == branchType) {
                branchesByType.add(branch);
            }
        }
        return branchesByType;
    }

    public Set<BranchCourseGroup> getMajorBranches() {
        return getBranchesByType(org.fenixedu.academic.domain.degreeStructure.BranchType.MAJOR);
    }

    public Set<BranchCourseGroup> getMinorBranches() {
        return getBranchesByType(org.fenixedu.academic.domain.degreeStructure.BranchType.MINOR);
    }

    public boolean hasBranches() {
        return getAllBranches().isEmpty() ? false : true;
    }

    public boolean hasBranchesByType(final org.fenixedu.academic.domain.degreeStructure.BranchType branchType) {
        return getBranchesByType(branchType).isEmpty() ? false : true;
    }

    public Set<DegreeModule> getAllDegreeModules() {
        final Set<DegreeModule> degreeModules = new TreeSet<>(DegreeModule.COMPARATOR_BY_NAME);
        final RootCourseGroup rootCourseGroup = getRoot();
        rootCourseGroup.getAllDegreeModules(degreeModules);
        return degreeModules;
    }

    public static Set<DegreeCurricularPlan> getDegreeCurricularPlans(final java.util.function.Predicate<DegreeType> predicate) {
        final Set<DegreeCurricularPlan> degreeCurricularPlans =
                new TreeSet<>(DegreeCurricularPlan.COMPARATOR_BY_PRESENTATION_NAME);

        for (final Degree degree : Degree.readNotEmptyDegrees()) {
            if (predicate.test(degree.getDegreeType())) {
                for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlansSet()) {
                    if (degreeCurricularPlan.isActive()) {
                        degreeCurricularPlans.add(degreeCurricularPlan);
                    }
                }
            }
        }
        return degreeCurricularPlans;
    }

    public Set<CurricularCourse> getCurricularCoursesByExecutionYearAndCurricularYear(final ExecutionYear executionYear,
            final Integer curricularYear) {
        Set<CurricularCourse> result = new HashSet<>();

        for (final CurricularCourse curricularCourse : getCurricularCoursesWithExecutionIn(executionYear)) {
            if (curricularCourse.getParentContextsSet().stream()
                    .anyMatch(ctx -> ctx.getCurricularYear().equals(curricularYear))) {
                result.add(curricularCourse);
            }
        }
        return result;
    }

    /**
     * This must be completely refactored. A pattern of some sort is desirable
     * in order to make this instance-dependent. Just did this due to time
     * constrains.
     */

    public Set<Registration> getRegistrations(final ExecutionYear executionYear, final Set<Registration> registrations) {
        for (final StudentCurricularPlan studentCurricularPlan : this.getStudentCurricularPlansSet()) {
            if (studentCurricularPlan.isActive(executionYear)) {
                if (studentCurricularPlan.getRegistration() != null) {
                    registrations.add(studentCurricularPlan.getRegistration());
                }
            }
        }
        return registrations;
    }

    public List<StudentCurricularPlan> getStudentsCurricularPlans(final ExecutionYear executionYear,
            final List<StudentCurricularPlan> result) {
        for (final StudentCurricularPlan studentCurricularPlan : this.getStudentCurricularPlansSet()) {
            if (studentCurricularPlan.isActive(executionYear)) {
                result.add(studentCurricularPlan);
            }
        }
        return result;
    }

    @Atomic
    public void editApplyPreviousYearsEnrolment(final Boolean input) {
        if (input != null) {
            setApplyPreviousYearsEnrolmentRule(input);
        }
    }

    public boolean isToApplyPreviousYearsEnrolmentRule() {
        return getApplyPreviousYearsEnrolmentRule();
    }

    public boolean canSubmitImprovementMarkSheets(final ExecutionYear executionYear) {
        if (getExecutionDegreesSet().isEmpty()) {
            return false;
        }
        SortedSet<ExecutionDegree> sortedExecutionDegrees = new TreeSet<>(ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_YEAR);
        sortedExecutionDegrees.addAll(getExecutionDegreesSet());
        return sortedExecutionDegrees.last().getExecutionYear().equals(executionYear.getPreviousExecutionYear());
    }

    public ExecutionInterval getBegin() {
        Set<ExecutionYear> beginContextExecutionYears = getBeginContextExecutionYears();
        return beginContextExecutionYears.isEmpty() ? null : Collections.min(beginContextExecutionYears,
                ExecutionYear.COMPARATOR_BY_YEAR);

    }

    public Set<ExecutionYear> getBeginContextExecutionYears() {
        return getRoot().getBeginContextExecutionYears();
    }

    public ExecutionYear getOldestContextExecutionYear() {
        List<ExecutionYear> beginContextExecutionYears = new ArrayList<>(getBeginContextExecutionYears());

        Collections.sort(beginContextExecutionYears, ExecutionYear.COMPARATOR_BY_YEAR);

        return beginContextExecutionYears.isEmpty() ? null : beginContextExecutionYears.iterator().next();
    }

    public LocalizedString getDescriptionI18N() {
        LocalizedString result = new LocalizedString();

        if (!StringUtils.isEmpty(getDescription())) {
            result = result.with(org.fenixedu.academic.util.LocaleUtils.PT, getDescription());
        }
        if (!StringUtils.isEmpty(getDescriptionEn())) {
            result = result.with(org.fenixedu.academic.util.LocaleUtils.EN, getDescriptionEn());
        }

        return result;
    }

    public void setDescriptionI18N(final LocalizedString input) {
        if (input != null && !input.isEmpty()) {
            setDescription(input.getContent(org.fenixedu.academic.util.LocaleUtils.PT));
            setDescriptionEn(input.getContent(org.fenixedu.academic.util.LocaleUtils.EN));
        } else {
            setDescription(null);
            setDescriptionEn(null);
        }
    }

    public Collection<CycleCourseGroup> getDestinationAffinities(final CycleType sourceCycleType) {
        final CycleCourseGroup cycleCourseGroup = getRoot().getCycleCourseGroup(sourceCycleType);
        if (cycleCourseGroup != null) {
            return cycleCourseGroup.getDestinationAffinitiesSet();
        }
        return Collections.EMPTY_LIST;
    }

    public Double getEctsCredits() {
        return getDegree().getEctsCredits();
    }

    public static List<DegreeCurricularPlan> readByDegreeTypesAndStateWithExecutionDegreeForYear(
            final java.util.function.Predicate<DegreeType> degreeTypes, final DegreeCurricularPlanState state,
            final ExecutionYear executionYear) {

        final List<DegreeCurricularPlan> result = new ArrayList<>();
        for (final DegreeCurricularPlan degreeCurricularPlan : readByDegreeTypesAndState(degreeTypes, state)) {
            if (degreeCurricularPlan.hasExecutionDegreeFor(executionYear)) {
                result.add(degreeCurricularPlan);
            }
        }

        return result;

    }

    public ExecutionYear getInauguralExecutionYear() {
        return getExecutionDegreesSet().stream().min(ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_YEAR)
                .map(ExecutionDegree::getExecutionYear).orElse(null);
    }

    public ExecutionYear getLastExecutionYear() {
        return getExecutionDegreesSet().stream().max(ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_YEAR)
                .map(ExecutionDegree::getExecutionYear).orElse(null);
    }

    @Deprecated
    public java.util.Date getEndDate() {
        org.joda.time.YearMonthDay ymd = getEndDateYearMonthDay();
        return ymd == null ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Deprecated
    public void setEndDate(final java.util.Date date) {
        if (date == null) {
            setEndDateYearMonthDay(null);
        } else {
            setEndDateYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
        }
    }

    @Deprecated
    public java.util.Date getInitialDate() {
        org.joda.time.YearMonthDay ymd = getInitialDateYearMonthDay();
        return ymd == null ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Deprecated
    public void setInitialDate(final java.util.Date date) {
        if (date == null) {
            setInitialDateYearMonthDay(null);
        } else {
            setInitialDateYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
        }
    }

    public int getDurationInYears() {
        if (getDegreeStructure() != null) {
            return Float.valueOf(getDegreeStructure().getAcademicPeriod().getWeight()).intValue();
        }
        return 0;
    }

    public int getDurationInSemesters() {
        return Float.valueOf(getDurationInYears() / AcademicPeriod.SEMESTER.getWeight()).intValue();
    }

    public int getDurationInYears(final CycleType cycleType) {

        if (cycleType == null || getDegreeType().hasExactlyOneCycleType()) {
            return getDurationInYears();
        }

        if (!getDegreeType().hasAnyCycleTypes()) {
            return 0;
        }

        return calculateCycleDuration(cycleType, ctx -> ctx.getCurricularPeriod().getParent(),
                cp -> cp.getAcademicPeriod().equals(AcademicPeriod.YEAR));

    }

    public int getDurationInSemesters(final CycleType cycleType) {
        return Float.valueOf(getDurationInYears(cycleType) / AcademicPeriod.SEMESTER.getWeight()).intValue();
    }

    private int calculateCycleDuration(final CycleType cycleType,
            final Function<Context, CurricularPeriod> curricularPeriodCollector,
            final java.util.function.Predicate<CurricularPeriod> curricularPeriodFilter) {

        final CycleCourseGroup cycleCourseGroup = getRoot().getCycleCourseGroup(cycleType);
        if (cycleCourseGroup == null) {
            //structure is not correct
            throw new DomainException("error.degreeCurricularPlan.unable.to.find.cycle.in.structure.to.calculate.duration",
                    cycleType.getDescription());
        }

        return Math.toIntExact(
                getAllCoursesGroups().stream().filter(cg -> cg.getParentCycleCourseGroups().contains(cycleCourseGroup))
                        .flatMap(cg -> cg.getChildContextsSet().stream()).filter(ctx -> ctx.getChildDegreeModule().isLeaf())
                        .map(curricularPeriodCollector).filter(curricularPeriodFilter).distinct().count());

    }

}
