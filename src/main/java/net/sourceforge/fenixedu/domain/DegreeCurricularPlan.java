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

import net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.DegreeCurricularPlanStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.IDegreeCurricularPlanStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.strategys.IDegreeCurricularPlanStrategy;
import net.sourceforge.fenixedu.dataTransferObject.CurricularPeriodInfoDTO;
import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseView;
import net.sourceforge.fenixedu.domain.accounting.serviceAgreementTemplates.DegreeCurricularPlanServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.MaximumNumberOfCreditsForEnrolmentPeriod;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.domain.degreeStructure.BranchCourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.BranchType;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularCourseFunctor;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleCourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.degreeStructure.OptionalCurricularCourse;
import net.sourceforge.fenixedu.domain.degreeStructure.RootCourseGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing;
import net.sourceforge.fenixedu.domain.space.SpaceUtils;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.RegistrationAgreement;
import net.sourceforge.fenixedu.domain.student.curriculum.AverageType;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendarEntry;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendarRootEntry;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicPeriod;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicYearCE;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.predicates.AcademicPredicates;
import net.sourceforge.fenixedu.predicates.DegreeCurricularPlanPredicates;
import net.sourceforge.fenixedu.util.Bundle;
import net.sourceforge.fenixedu.util.MarkType;
import net.sourceforge.fenixedu.util.PeriodState;
import net.sourceforge.fenixedu.util.SituationName;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.groups.NobodyGroup;
import org.fenixedu.bennu.core.groups.UserGroup;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.I18N;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class DegreeCurricularPlan extends DegreeCurricularPlan_Base {

    public static final Comparator<DegreeCurricularPlan> COMPARATOR_BY_NAME = new Comparator<DegreeCurricularPlan>() {

        @Override
        public int compare(DegreeCurricularPlan o1, DegreeCurricularPlan o2) {
            return o1.getName().compareTo(o2.getName());
        }

    };

    public static final Comparator<DegreeCurricularPlan> COMPARATOR_BY_PRESENTATION_NAME =
            new Comparator<DegreeCurricularPlan>() {

                @Override
                public int compare(DegreeCurricularPlan o1, DegreeCurricularPlan o2) {
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
                public int compare(DegreeCurricularPlan o1, DegreeCurricularPlan o2) {
                    final int degreeTypeCompare =
                            o1.getDegreeType().getLocalizedName().compareTo(o2.getDegreeType().getLocalizedName());
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
    }

    private DegreeCurricularPlan(Degree degree, String name, GradeScale gradeScale) {
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

    protected DegreeCurricularPlan(Degree degree, String name, DegreeCurricularPlanState state, Date inicialDate, Date endDate,
            Integer degreeDuration, Integer minimalYearForOptionalCourses, Double neededCredits, MarkType markType,
            Integer numerusClausus, String annotation, GradeScale gradeScale) {

        this(degree, name, gradeScale);
        super.setCurricularStage(CurricularStage.OLD);
        super.setState(state);

        oldStructureFieldsChange(inicialDate, endDate, degreeDuration, minimalYearForOptionalCourses, neededCredits, markType,
                numerusClausus, annotation);
    }

    private void oldStructureFieldsChange(Date inicialDate, Date endDate, Integer degreeDuration,
            Integer minimalYearForOptionalCourses, Double neededCredits, MarkType markType, Integer numerusClausus,
            String annotation) {

        if (inicialDate == null) {
            throw new DomainException("degreeCurricularPlan.inicialDate.not.null");
        } else if (degreeDuration == null) {
            throw new DomainException("degreeCurricularPlan.degreeDuration.not.null");
        } else if (minimalYearForOptionalCourses == null) {
            throw new DomainException("degreeCurricularPlan.minimalYearForOptionalCourses.not.null");
        }

        this.setInitialDateYearMonthDay(inicialDate != null ? new YearMonthDay(inicialDate) : new YearMonthDay());
        this.setEndDateYearMonthDay(endDate != null ? new YearMonthDay(endDate) : null);
        this.setDegreeDuration(degreeDuration);
        this.setMinimalYearForOptionalCourses(minimalYearForOptionalCourses);
        this.setNeededCredits(neededCredits);
        this.setMarkType(markType);
        this.setNumerusClausus(numerusClausus);
        this.setAnotation(annotation);
    }

    DegreeCurricularPlan(Degree degree, String name, GradeScale gradeScale, Person creator, CurricularPeriod curricularPeriod) {
        this(degree, name, gradeScale);

        if (creator == null) {
            throw new DomainException("degreeCurricularPlan.creator.not.null");
        }

        if (curricularPeriod == null) {
            throw new DomainException("degreeCurricularPlan.curricularPeriod.not.null");
        }
        setCurricularPlanMembersGroup(UserGroup.of(creator.getUser()));
        setDegreeStructure(curricularPeriod);
        setState(DegreeCurricularPlanState.ACTIVE);

        newStructureFieldsChange(CurricularStage.DRAFT, null);

        createDefaultCourseGroups();
        createDefaultCurricularRules();
        new DegreeCurricularPlanServiceAgreementTemplate(this);

    }

    private void createDefaultCourseGroups() {
        RootCourseGroup.createRoot(this, getName(), getName());
    }

    private void createDefaultCurricularRules() {
        new MaximumNumberOfCreditsForEnrolmentPeriod(getRoot(), ExecutionSemester.readActualExecutionSemester());
    }

    private void newStructureFieldsChange(CurricularStage curricularStage, ExecutionYear beginExecutionYear) {

        if (curricularStage == null) {
            throw new DomainException("degreeCurricularPlan.curricularStage.not.null");
        } else if (hasAnyExecutionDegrees() && curricularStage == CurricularStage.DRAFT) {
            throw new DomainException("degreeCurricularPlan.has.already.been.executed");
        } else if (isBolonhaDegree() && curricularStage == CurricularStage.APPROVED) {
            approve(beginExecutionYear);
        } else {
            setCurricularStage(curricularStage);
        }
    }

    private void commonFieldsChange(String name, GradeScale gradeScale) {
        if (name == null) {
            throw new DomainException("degreeCurricularPlan.name.not.null");
        }

        // assert unique pair name/degree
        for (final DegreeCurricularPlan dcp : this.getDegree().getDegreeCurricularPlans()) {
            if (dcp != this && dcp.getName().equalsIgnoreCase(name)) {
                throw new DomainException("error.degreeCurricularPlan.existing.name.and.degree");
            }
        }
        this.setName(name);

        this.setGradeScale(gradeScale);
    }

    public void edit(String name, DegreeCurricularPlanState state, Date inicialDate, Date endDate, Integer degreeDuration,
            Integer minimalYearForOptionalCourses, Double neededCredits, MarkType markType, Integer numerusClausus,
            String annotation, GradeScale gradeScale) {

        commonFieldsChange(name, gradeScale);
        oldStructureFieldsChange(inicialDate, endDate, degreeDuration, minimalYearForOptionalCourses, neededCredits, markType,
                numerusClausus, annotation);

        this.setState(state);
    }

    public void edit(String name, CurricularStage curricularStage, DegreeCurricularPlanState state, GradeScale gradeScale,
            ExecutionYear beginExecutionYear) {

        if (isApproved()
                && (name != null && !getName().equals(name) || gradeScale != null && !getGradeScale().equals(gradeScale))) {
            throw new DomainException("error.degreeCurricularPlan.already.approved");
        } else {
            commonFieldsChange(name, gradeScale);
        }

        newStructureFieldsChange(curricularStage, beginExecutionYear);

        this.setState(state);
        this.getRoot().setName(name);
        this.getRoot().setNameEn(name);
    }

    private void approve(ExecutionYear beginExecutionYear) {
        if (isApproved()) {
            return;
        }

        if (!getCanModify().booleanValue()) {
            throw new DomainException("error.degreeCurricularPlan.already.approved");
        }

        final ExecutionSemester beginExecutionPeriod;
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
        final List<String> notApprovedCompetenceCourses = new ArrayList<String>();
        for (final DegreeModule degreeModule : getDcpDegreeModules(CurricularCourse.class)) {
            final CurricularCourse curricularCourse = (CurricularCourse) degreeModule;
            if (!curricularCourse.isOptional() && !curricularCourse.getCompetenceCourse().isApproved()) {
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
            final ExecutionSemester beginExecutionPeriod) {

        if (beginExecutionPeriod == null) {
            throw new DomainException("");
        }

        for (final CurricularRule curricularRule : courseGroup.getCurricularRules()) {
            curricularRule.setBegin(beginExecutionPeriod);
        }
        for (final Context context : courseGroup.getChildContexts()) {
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

    public boolean isEmpty() {
        return false;
    }

    /**
     * Temporary method, after all degrees migration this is no longer necessary
     */
    public boolean isBoxStructure() {
        return !getCurricularStage().equals(CurricularStage.OLD);
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
        return canDeleteRoot() && !hasAnyStudentCurricularPlans() && !hasAnyCurricularCourseEquivalences()
                && !hasAnyEnrolmentPeriods() && !hasAnyCurricularCourses() && !hasAnyExecutionDegrees() && !hasAnyAreas()
                && canDeleteServiceAgreement() && !getTeachersWithIncompleteEvaluationWorkGroupSet().isEmpty();
    }

    private boolean canDeleteRoot() {
        return getRoot() == null || getRoot().getCanBeDeleted();
    }

    private boolean canDeleteServiceAgreement() {
        return !hasServiceAgreementTemplate() || !getServiceAgreementTemplate().hasAnyPostingRules()
                && !getServiceAgreementTemplate().hasAnyServiceAgreements();
    }

    public void delete() {
        if (getCanBeDeleted()) {
            setDegree(null);
            if (hasRoot()) {
                getRoot().delete();
            }
            if (hasDegreeStructure()) {
                getDegreeStructure().delete();
            }
            if (hasServiceAgreementTemplate()) {
                DegreeCurricularPlanServiceAgreementTemplate template = getServiceAgreementTemplate();
                setServiceAgreementTemplate(null);
                template.delete();
            }
            setRootDomainObject(null);
            deleteDomainObject();
        } else {
            throw new DomainException("error.degree.curricular.plan.cant.delete");
        }
    }

    public String print() {
        if (hasRoot()) {
            StringBuilder dcp = new StringBuilder();

            dcp.append("[DCP ").append(this.getExternalId()).append("] ").append(this.getName()).append("\n");
            this.getRoot().print(dcp, "", null);

            return dcp.toString();
        } else {
            return "";
        }
    }

    public GradeScale getGradeScaleChain() {
        return super.getGradeScale() != null ? super.getGradeScale() : getDegree().getGradeScaleChain();
    }

    @Deprecated
    public ExecutionDegree getExecutionDegreeByYear(ExecutionYear executionYear) {
        for (final ExecutionDegree executionDegree : getExecutionDegreesSet()) {
            if (executionDegree.getExecutionYear() == executionYear) {
                return executionDegree;
            }
        }
        return null;
    }

    // FIXME: Optimization Required
    public ExecutionDegree getExecutionDegreeByAcademicInterval(AcademicInterval academicInterval) {
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

    public Set<ExecutionDegree> getExecutionDegreesWithProposalPeriodOpen() {
        final Set<ExecutionDegree> executionDegrees = new TreeSet<ExecutionDegree>(new Comparator<ExecutionDegree>() {

            @Override
            public int compare(ExecutionDegree o1, ExecutionDegree o2) {
                return o2.getExecutionYear().compareTo(o1.getExecutionYear());
            }
        });
        for (final ExecutionDegree executionDegree : getExecutionDegreesSet()) {
            final Scheduleing scheduling = executionDegree.getScheduling();
            if (scheduling != null && scheduling.isInsideProposalSubmissionPeriod()) {
                executionDegrees.add(executionDegree);
            }
        }
        return Collections.unmodifiableSet(executionDegrees);
    }

    public Set<ExecutionYear> getExecutionYears() {
        Set<ExecutionYear> result = new HashSet<ExecutionYear>();
        for (final ExecutionDegree executionDegree : getExecutionDegreesSet()) {
            result.add(executionDegree.getExecutionYear());
        }
        return result;
    }

    public ExecutionYear getMostRecentExecutionYear() {
        return getMostRecentExecutionDegree().getExecutionYear();
    }

    public ExecutionDegree getExecutionDegreeByYearAndCampus(ExecutionYear executionYear, Space campus) {
        for (final ExecutionDegree executionDegree : getExecutionDegreesSet()) {
            if (executionDegree.getExecutionYear() == executionYear && executionDegree.getCampus() == campus) {
                return executionDegree;
            }
        }
        return null;
    }

    public boolean hasExecutionDegreeByYearAndCampus(ExecutionYear executionYear, Space campus) {
        return getExecutionDegreeByYearAndCampus(executionYear, campus) != null;
    }

    public boolean hasAnyExecutionDegreeFor(ExecutionYear executionYear) {
        for (final ExecutionDegree executionDegree : this.getExecutionDegreesSet()) {
            if (executionDegree.getExecutionYear() == executionYear) {
                return true;
            }
        }
        return false;
    }

    public boolean hasExecutionDegreeFor(ExecutionYear executionYear) {
        return getExecutionDegreeByYear(executionYear) != null;
    }

    public ExecutionDegree getMostRecentExecutionDegree() {
        if (!hasAnyExecutionDegrees()) {
            return null;
        }

        final ExecutionYear currentYear = ExecutionYear.readCurrentExecutionYear();
        ExecutionDegree result = getExecutionDegreeByYear(currentYear);
        if (result != null) {
            return result;
        }

        final List<ExecutionDegree> sorted = new ArrayList<ExecutionDegree>(getExecutionDegrees());
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
        if (getExecutionDegrees().isEmpty()) {
            return null;
        }
        return Collections.min(getExecutionDegrees(), ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_YEAR);
    }

    public Set<ExecutionCourse> getExecutionCoursesByExecutionPeriod(ExecutionSemester executionSemester) {
        final Set<ExecutionCourse> result = new HashSet<ExecutionCourse>();
        for (final CurricularCourse curricularCourse : super.getCurricularCoursesSet()) {
            for (final ExecutionCourse executionCourse : curricularCourse.getAssociatedExecutionCourses()) {
                if (executionCourse.getExecutionPeriod() == executionSemester) {
                    result.add(executionCourse);
                }
            }
        }
        if (getRoot() != null) {
            addExecutionCoursesForExecutionPeriod(result, executionSemester, getRoot().getChildContextsSet());
        }
        return result;
    }

    public SortedSet<DegreeModuleScope> getDegreeModuleScopes() {
        final SortedSet<DegreeModuleScope> degreeModuleScopes =
                new TreeSet<DegreeModuleScope>(
                        DegreeModuleScope.COMPARATOR_BY_CURRICULAR_YEAR_AND_SEMESTER_AND_CURRICULAR_COURSE_NAME);
        for (final CurricularCourse curricularCourse : this.getCurricularCourses()) {
            degreeModuleScopes.addAll(curricularCourse.getDegreeModuleScopes());
        }
        return degreeModuleScopes;
    }

    public Set<DegreeModuleScope> getDegreeModuleScopesFor(final Integer year, final Integer semester) {
        final Set<DegreeModuleScope> result =
                new TreeSet<DegreeModuleScope>(
                        DegreeModuleScope.COMPARATOR_BY_CURRICULAR_YEAR_AND_SEMESTER_AND_CURRICULAR_COURSE_NAME);

        for (final DegreeModuleScope each : getDegreeModuleScopes()) {
            if (each.isActive(year, semester)) {
                result.add(each);
            }
        }

        return result;
    }

    public Set<DegreeModuleScope> getDegreeModuleScopesFor(final ExecutionYear executionYear) {
        final Set<DegreeModuleScope> result =
                new TreeSet<DegreeModuleScope>(
                        DegreeModuleScope.COMPARATOR_BY_CURRICULAR_YEAR_AND_SEMESTER_AND_CURRICULAR_COURSE_NAME);

        for (final DegreeModuleScope each : getDegreeModuleScopes()) {
            if (each.isActiveForExecutionYear(executionYear)) {
                result.add(each);
            }
        }

        return result;
    }

    public void addExecutionCoursesForExecutionPeriod(final Set<ExecutionCourse> executionCourses,
            final ExecutionSemester executionSemester, final Set<Context> contexts) {
        for (final Context context : contexts) {
            final DegreeModule degreeModule = context.getChildDegreeModule();
            if (degreeModule instanceof CurricularCourse) {
                final CurricularCourse curricularCourse = (CurricularCourse) degreeModule;
                executionCourses.addAll(curricularCourse.getExecutionCoursesByExecutionPeriod(executionSemester));
            } else if (degreeModule instanceof CourseGroup) {
                final CourseGroup courseGroup = (CourseGroup) degreeModule;
                addExecutionCoursesForExecutionPeriod(executionCourses, executionSemester, courseGroup.getChildContextsSet());
            }
        }
    }

    public List<ExecutionCourse> getExecutionCoursesByExecutionPeriodAndSemesterAndYear(ExecutionSemester executionSemester,
            Integer curricularYear, Integer semester) {

        List<ExecutionCourse> result = new ArrayList<ExecutionCourse>();
        for (final CurricularCourse curricularCourse : this.getCurricularCourses()) {
            for (final DegreeModuleScope degreeModuleScope : curricularCourse.getDegreeModuleScopes()) {
                if (degreeModuleScope.getCurricularSemester().equals(semester)
                        && degreeModuleScope.getCurricularYear().equals(curricularYear)) {
                    for (final ExecutionCourse executionCourse : curricularCourse.getAssociatedExecutionCourses()) {
                        if (executionCourse.getExecutionPeriod().equals(executionSemester)) {
                            result.add(executionCourse);
                        }
                    }
                    break;
                }
            }
        }
        return result;
    }

    public Set<CurricularCourse> getAllCurricularCourses() {
        final Set<DegreeModule> curricularCourses = new TreeSet<DegreeModule>(DegreeModule.COMPARATOR_BY_NAME) {
            @Override
            public boolean add(DegreeModule degreeModule) {
                return degreeModule instanceof CurricularCourse && super.add(degreeModule);
            }
        };
        curricularCourses.addAll(super.getCurricularCoursesSet());
        if (hasRoot()) {
            getRoot().getAllDegreeModules(curricularCourses);
        }
        return (Set) curricularCourses;
    }

    public List<CurricularCourse> getCurricularCoursesWithExecutionIn(ExecutionYear executionYear) {
        List<CurricularCourse> curricularCourses = new ArrayList<CurricularCourse>();
        for (CurricularCourse curricularCourse : getCurricularCourses()) {
            for (ExecutionSemester executionSemester : executionYear.getExecutionPeriods()) {
                List<ExecutionCourse> executionCourses = curricularCourse.getExecutionCoursesByExecutionPeriod(executionSemester);
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

        final List<CurricularCourse> curricularCourses = new ArrayList<CurricularCourse>();
        for (final CurricularCourse curricularCourse : getCurricularCourses()) {
            if (curricularCourse.getBasic().equals(basic)) {
                curricularCourses.add(curricularCourse);
            }
        }
        return curricularCourses;
    }

    public EnrolmentPeriodInCurricularCourses getActualEnrolmentPeriod() {
        for (final EnrolmentPeriod enrolmentPeriod : this.getEnrolmentPeriods()) {
            if (enrolmentPeriod instanceof EnrolmentPeriodInCurricularCourses && enrolmentPeriod.isValid()) {
                return (EnrolmentPeriodInCurricularCourses) enrolmentPeriod;
            }
        }
        return null;
    }

    public boolean hasActualEnrolmentPeriodInCurricularCourses() {
        return getActualEnrolmentPeriod() != null;
    }

    public EnrolmentPeriodInSpecialSeasonEvaluations getNextSpecialSeasonEnrolmentPeriod() {
        final List<EnrolmentPeriodInSpecialSeasonEvaluations> positivesSet =
                new ArrayList<EnrolmentPeriodInSpecialSeasonEvaluations>();
        for (final EnrolmentPeriod enrolmentPeriod : this.getEnrolmentPeriods()) {
            if (enrolmentPeriod instanceof EnrolmentPeriodInSpecialSeasonEvaluations && enrolmentPeriod.isUpcomingPeriod()) {
                positivesSet.add((EnrolmentPeriodInSpecialSeasonEvaluations) enrolmentPeriod);
            }
        }
        return positivesSet.isEmpty() ? null : Collections.min(positivesSet,
                EnrolmentPeriodInSpecialSeasonEvaluations.COMPARATOR_BY_START);
    }

    public boolean hasOpenSpecialSeasonEnrolmentPeriod(ExecutionSemester executionSemester) {
        for (final EnrolmentPeriod enrolmentPeriod : this.getEnrolmentPeriods()) {
            if (enrolmentPeriod instanceof EnrolmentPeriodInSpecialSeasonEvaluations && enrolmentPeriod.isFor(executionSemester)
                    && enrolmentPeriod.isValid()) {
                return true;
            }
        }
        return false;
    }

    public EnrolmentPeriodInCurricularCoursesSpecialSeason getActualEnrolmentPeriodInCurricularCoursesSpecialSeason() {
        for (final EnrolmentPeriod enrolmentPeriod : this.getEnrolmentPeriods()) {
            if (enrolmentPeriod instanceof EnrolmentPeriodInCurricularCoursesSpecialSeason && enrolmentPeriod.isValid()) {
                return (EnrolmentPeriodInCurricularCoursesSpecialSeason) enrolmentPeriod;
            }
        }
        return null;
    }

    public boolean hasOpenEnrolmentPeriodInCurricularCoursesSpecialSeason(final ExecutionSemester executionSemester) {
        for (final EnrolmentPeriod enrolmentPeriod : getEnrolmentPeriods()) {
            if (enrolmentPeriod instanceof EnrolmentPeriodInCurricularCoursesSpecialSeason) {
                final EnrolmentPeriodInCurricularCoursesSpecialSeason enrolmentPeriodInCurricularCourses =
                        (EnrolmentPeriodInCurricularCoursesSpecialSeason) enrolmentPeriod;
                if (enrolmentPeriodInCurricularCourses.isFor(executionSemester) && enrolmentPeriodInCurricularCourses.isValid()) {
                    return true;
                }
            }
        }

        return false;
    }

    public EnrolmentPeriodInCurricularCoursesFlunkedSeason getActualEnrolmentPeriodInCurricularCoursesFlunkedSeason() {
        for (final EnrolmentPeriod enrolmentPeriod : this.getEnrolmentPeriods()) {
            if (enrolmentPeriod instanceof EnrolmentPeriodInCurricularCoursesFlunkedSeason && enrolmentPeriod.isValid()) {
                return (EnrolmentPeriodInCurricularCoursesFlunkedSeason) enrolmentPeriod;
            }
        }
        return null;
    }

    public boolean hasOpenEnrolmentPeriodInCurricularCoursesFor(final ExecutionSemester executionSemester) {
        for (final EnrolmentPeriod enrolmentPeriod : getEnrolmentPeriods()) {
            if (enrolmentPeriod instanceof EnrolmentPeriodInCurricularCourses) {
                final EnrolmentPeriodInCurricularCourses enrolmentPeriodInCurricularCourses =
                        (EnrolmentPeriodInCurricularCourses) enrolmentPeriod;
                if (enrolmentPeriodInCurricularCourses.isFor(executionSemester) && enrolmentPeriodInCurricularCourses.isValid()) {
                    return true;
                }
            }
        }

        return false;
    }

    public EnrolmentPeriodInCurricularCourses getNextEnrolmentPeriod() {
        final DateTime now = new DateTime();
        final List<EnrolmentPeriodInCurricularCourses> result = new ArrayList<EnrolmentPeriodInCurricularCourses>();
        for (final EnrolmentPeriod enrolmentPeriod : this.getEnrolmentPeriods()) {
            if (enrolmentPeriod instanceof EnrolmentPeriodInCurricularCourses
                    && enrolmentPeriod.getStartDateDateTime().isAfter(now)) {
                result.add((EnrolmentPeriodInCurricularCourses) enrolmentPeriod);
            }
        }
        if (!result.isEmpty()) {
            Collections.sort(result, EnrolmentPeriodInCurricularCourses.COMPARATOR_BY_START);
            return result.iterator().next();
        }
        return null;
    }

    public EnrolmentPeriodInCurricularCoursesSpecialSeason getNextEnrolmentPeriodInCurricularCoursesSpecialSeason() {
        final DateTime now = new DateTime();
        final List<EnrolmentPeriodInCurricularCoursesSpecialSeason> result =
                new ArrayList<EnrolmentPeriodInCurricularCoursesSpecialSeason>();
        for (EnrolmentPeriod enrolmentPeriod : this.getEnrolmentPeriods()) {
            if (enrolmentPeriod instanceof EnrolmentPeriodInCurricularCoursesSpecialSeason
                    && enrolmentPeriod.getStartDateDateTime().isAfter(now)) {
                result.add((EnrolmentPeriodInCurricularCoursesSpecialSeason) enrolmentPeriod);
            }
        }
        if (!result.isEmpty()) {
            Collections.sort(result, EnrolmentPeriodInCurricularCoursesSpecialSeason.COMPARATOR_BY_START);
            return result.iterator().next();
        }
        return null;
    }

    public EnrolmentPeriodInCurricularCoursesFlunkedSeason getNextEnrolmentPeriodInCurricularCoursesFlunkedSeason() {
        final DateTime now = new DateTime();
        final List<EnrolmentPeriodInCurricularCoursesFlunkedSeason> result =
                new ArrayList<EnrolmentPeriodInCurricularCoursesFlunkedSeason>();
        for (EnrolmentPeriod enrolmentPeriod : this.getEnrolmentPeriods()) {
            if (enrolmentPeriod instanceof EnrolmentPeriodInCurricularCoursesFlunkedSeason
                    && enrolmentPeriod.getStartDateDateTime().isAfter(now)) {
                result.add((EnrolmentPeriodInCurricularCoursesFlunkedSeason) enrolmentPeriod);
            }
        }
        if (!result.isEmpty()) {
            Collections.sort(result, EnrolmentPeriodInCurricularCoursesFlunkedSeason.COMPARATOR_BY_START);
            return result.iterator().next();
        }
        return null;
    }

    public EnrolmentPeriodInClasses getCurrentClassesEnrollmentPeriod() {
        for (final EnrolmentPeriod enrolmentPeriod : this.getEnrolmentPeriodsSet()) {
            if (enrolmentPeriod instanceof EnrolmentPeriodInClasses
                    && enrolmentPeriod.getExecutionPeriod().getState().equals(PeriodState.CURRENT)) {
                return (EnrolmentPeriodInClasses) enrolmentPeriod;
            }
        }
        return null;
    }

    public CandidacyPeriodInDegreeCurricularPlan getCurrentCandidacyPeriodInDCP() {
        for (final EnrolmentPeriod enrolmentPeriod : this.getEnrolmentPeriodsSet()) {
            if (enrolmentPeriod instanceof CandidacyPeriodInDegreeCurricularPlan
                    && enrolmentPeriod.getExecutionPeriod().getExecutionYear().isCurrent()) {
                return (CandidacyPeriodInDegreeCurricularPlan) enrolmentPeriod;
            }
        }
        return null;
    }

    public CandidacyPeriodInDegreeCurricularPlan getCandidacyPeriod(final ExecutionYear executionYear) {
        final List<EnrolmentPeriod> enrolmentPeriods =
                getEnrolmentPeriodsBy(executionYear.getFirstExecutionPeriod(), CandidacyPeriodInDegreeCurricularPlan.class);
        return (CandidacyPeriodInDegreeCurricularPlan) (!enrolmentPeriods.isEmpty() ? enrolmentPeriods.iterator().next() : null);

    }

    public boolean hasCandidacyPeriodFor(final ExecutionYear executionYear) {
        return hasEnrolmentPeriodFor(executionYear.getFirstExecutionPeriod(), CandidacyPeriodInDegreeCurricularPlan.class);
    }

    public RegistrationPeriodInDegreeCurricularPlan getRegistrationPeriod(final ExecutionYear executionYear) {
        final List<EnrolmentPeriod> enrolmentPeriods =
                getEnrolmentPeriodsBy(executionYear.getFirstExecutionPeriod(), RegistrationPeriodInDegreeCurricularPlan.class);
        return (RegistrationPeriodInDegreeCurricularPlan) (!enrolmentPeriods.isEmpty() ? enrolmentPeriods.iterator().next() : null);
    }

    public boolean hasRegistrationPeriodFor(final ExecutionYear executionYear) {
        return hasEnrolmentPeriodFor(executionYear.getFirstExecutionPeriod(), RegistrationPeriodInDegreeCurricularPlan.class);
    }

    private List<EnrolmentPeriod> getEnrolmentPeriodsBy(final ExecutionSemester executionSemester, Class clazz) {
        final List<EnrolmentPeriod> result = new ArrayList<EnrolmentPeriod>();
        for (final EnrolmentPeriod enrolmentPeriod : getEnrolmentPeriodsSet()) {
            if (enrolmentPeriod.getClass().equals(clazz) && enrolmentPeriod.getExecutionPeriod() == executionSemester) {
                result.add(enrolmentPeriod);
            }
        }

        return result;
    }

    private boolean hasEnrolmentPeriodFor(final ExecutionSemester executionSemester, Class clazz) {
        for (final EnrolmentPeriod enrolmentPeriod : getEnrolmentPeriodsSet()) {
            if (enrolmentPeriod.getClass().equals(clazz) && enrolmentPeriod.getExecutionPeriod() == executionSemester) {
                return true;
            }
        }

        return false;
    }

    public Collection<ExecutionYear> getCandidacyPeriodsExecutionYears() {
        return getEnrolmentPeriodsExecutionYears(CandidacyPeriodInDegreeCurricularPlan.class);
    }

    private Set<ExecutionYear> getEnrolmentPeriodsExecutionYears(Class clazz) {
        Set<ExecutionYear> result = new HashSet<ExecutionYear>();
        for (final EnrolmentPeriod enrolmentPeriod : getEnrolmentPeriodsSet()) {
            if (clazz == null || enrolmentPeriod.getClass().equals(clazz)) {
                result.add(enrolmentPeriod.getExecutionPeriod().getExecutionYear());
            }
        }
        return result;
    }

    public EnrolmentPeriodInCurricularCoursesSpecialSeason getEnrolmentPeriodInCurricularCoursesSpecialSeasonByExecutionPeriod(
            ExecutionSemester executionSemester) {
        for (EnrolmentPeriod enrolmentPeriod : getEnrolmentPeriods()) {
            if (enrolmentPeriod instanceof EnrolmentPeriodInCurricularCoursesSpecialSeason
                    && enrolmentPeriod.getExecutionPeriod().equals(executionSemester)) {
                return (EnrolmentPeriodInCurricularCoursesSpecialSeason) enrolmentPeriod;
            }
        }
        return null;
    }

    public EnrolmentPeriodInCurricularCourses getEnrolmentPeriodInCurricularCoursesBy(final ExecutionSemester executionSemester) {
        for (final EnrolmentPeriod each : getEnrolmentPeriods()) {
            if (each instanceof EnrolmentPeriodInCurricularCourses && each.getExecutionPeriod().equals(executionSemester)) {
                return (EnrolmentPeriodInCurricularCourses) each;
            }
        }

        return null;
    }

    public ReingressionPeriod getReingressionPeriod(final ExecutionSemester executionSemester) {
        for (final EnrolmentPeriod period : getEnrolmentPeriods()) {
            if (period instanceof ReingressionPeriod && period.isFor(executionSemester)) {
                return (ReingressionPeriod) period;
            }
        }
        return null;
    }

    public boolean hasEnrolmentPeriodInCurricularCourses(final ExecutionSemester executionSemester) {
        return getEnrolmentPeriodInCurricularCoursesBy(executionSemester) != null;
    }

    // -------------------------------------------------------------
    // BEGIN: Only for enrollment purposes
    // -------------------------------------------------------------

    public CurricularCourse getCurricularCourseByCode(String code) {
        for (CurricularCourse curricularCourse : getCurricularCourses()) {
            if (curricularCourse.getCode() != null && curricularCourse.getCode().equals(code)) {
                return curricularCourse;
            }
        }
        return null;
    }

    public CurricularCourse getCurricularCourseByAcronym(String acronym) {
        for (CurricularCourse curricularCourse : getCurricularCourses()) {
            if (curricularCourse.getAcronym().equals(acronym)) {
                return curricularCourse;
            }
        }
        return null;
    }

    @Override
    public Set<CurricularCourse> getCurricularCoursesSet() {
        if (isBoxStructure()) {
            return this.getCurricularCourses((ExecutionYear) null);
        } else {
            return super.getCurricularCoursesSet();
        }
    }

    public void doForAllCurricularCourses(final CurricularCourseFunctor curricularCourseFunctor) {
        for (final CurricularCourse curricularCourse : super.getCurricularCoursesSet()) {
            curricularCourse.doForAllCurricularCourses(curricularCourseFunctor);
        }
        final RootCourseGroup rootCourseGroup = getRoot();
        if (rootCourseGroup != null) {
            rootCourseGroup.doForAllCurricularCourses(curricularCourseFunctor);
        }
    }

    public Set<CurricularCourse> getCurricularCourses(final ExecutionSemester executionSemester) {
        final Set<CurricularCourse> curricularCourses = new HashSet<CurricularCourse>();
        for (final CurricularCourse curricularCourse : super.getCurricularCoursesSet()) {
            if (curricularCourse.hasScopeInGivenSemesterAndCurricularYearInDCP(null, null, executionSemester)) {
                curricularCourses.add(curricularCourse);
            }
        }
        final ExecutionYear executionYear = executionSemester.getExecutionYear();
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
        final Set<CurricularCourse> result = new HashSet<CurricularCourse>();
        if (isBoxStructure()) {
            for (final DegreeModule degreeModule : getDcpDegreeModules(CurricularCourse.class, executionYear)) {
                result.add((CurricularCourse) degreeModule);
            }
        } else {
            for (CurricularCourse curricularCourse : getCurricularCourses()) {
                if (curricularCourse.hasAnyActiveDegreModuleScope(executionYear)) {
                    result.add(curricularCourse);
                }
            }
        }
        return result;
    }

    public void applyToCurricularCourses(final ExecutionYear executionYear, final Predicate predicate) {
        if (isBoxStructure()) {
            if (hasRoot()) {
                getRoot().applyToCurricularCourses(executionYear, predicate);
            }
        } else {
            for (final CurricularCourse curricularCourse : super.getCurricularCoursesSet()) {
                if (curricularCourse.hasAnyActiveDegreModuleScope(executionYear)) {
                    curricularCourse.applyToCurricularCourses(executionYear, predicate);
                }
            }
        }
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
            return new ArrayList<CompetenceCourse>();
        }

    }

    /**
     * Method to get a filtered list of a dcp's competence courses in the given
     * execution year. Each competence courses is connected with a curricular
     * course with at least one open context in the execution year
     * 
     * @return All competence courses that are present in the dcp
     */
    public List<CompetenceCourse> getCompetenceCourses(ExecutionYear executionYear) {
        SortedSet<CompetenceCourse> result = new TreeSet<CompetenceCourse>(CompetenceCourse.COMPETENCE_COURSE_COMPARATOR_BY_NAME);

        if (isBolonhaDegree()) {
            for (final CurricularCourse curricularCourse : getCurricularCourses(executionYear)) {
                if (!curricularCourse.isOptionalCurricularCourse()) {
                    result.add(curricularCourse.getCompetenceCourse());
                }
            }
            return new ArrayList<CompetenceCourse>(result);
        } else {
            return new ArrayList<CompetenceCourse>();
        }
    }

    public List<Branch> getCommonAreas() {
        return (List<Branch>) CollectionUtils.select(getAreas(), new Predicate() {
            @Override
            public boolean evaluate(Object obj) {
                Branch branch = (Branch) obj;
                if (branch.getBranchType() == null) {
                    return branch.getName().equals("") && branch.getCode().equals("");
                }
                return branch.getBranchType().equals(net.sourceforge.fenixedu.domain.branch.BranchType.COMNBR);

            }
        });
    }

    public Set<CurricularCourse> getActiveCurricularCourses() {
        final Set<CurricularCourse> result = new HashSet<CurricularCourse>();
        for (final CurricularCourse curricularCourse : getCurricularCourses()) {
            if (curricularCourse.hasAnyActiveDegreModuleScope()) {
                result.add(curricularCourse);
            }
        }
        return result;
    }

    public Set<CurricularCourse> getActiveCurricularCourses(final ExecutionSemester executionSemester) {
        final Set<CurricularCourse> result = new HashSet<CurricularCourse>();
        for (final CurricularCourse curricularCourse : getCurricularCourses()) {
            if (curricularCourse.hasAnyActiveDegreModuleScope(executionSemester)) {
                result.add(curricularCourse);
            }
        }
        return result;
    }

    public List<CurricularCourseScope> getActiveCurricularCourseScopes() {
        final List<CurricularCourseScope> result = new ArrayList<CurricularCourseScope>();
        for (final CurricularCourse course : getCurricularCourses()) {
            result.addAll(course.getActiveScopes());
        }
        return result;
    }

    public boolean isGradeValid(Grade grade) {

        IDegreeCurricularPlanStrategyFactory degreeCurricularPlanStrategyFactory =
                DegreeCurricularPlanStrategyFactory.getInstance();
        IDegreeCurricularPlanStrategy degreeCurricularPlanStrategy =
                degreeCurricularPlanStrategyFactory.getDegreeCurricularPlanStrategy(this);

        if (grade.isEmpty()) {
            return false;
        }

        return degreeCurricularPlanStrategy.checkMark(grade.getValue());
    }

    /**
     * Used to create a CurricularCourse to non box structure
     */
    public CurricularCourse createCurricularCourse(String name, String code, String acronym, Boolean enrolmentAllowed,
            CurricularStage curricularStage) {
        return new CurricularCourse(this, name, code, acronym, enrolmentAllowed, curricularStage);
    }

    public CourseGroup createCourseGroup(final CourseGroup parentCourseGroup, final String name, final String nameEn,
            final ExecutionSemester begin, final ExecutionSemester end) {
        return new CourseGroup(parentCourseGroup, name, nameEn, begin, end);
    }

    public BranchCourseGroup createBranchCourseGroup(final CourseGroup parentCourseGroup, final String name, final String nameEn,
            final BranchType branchType, final ExecutionSemester begin, final ExecutionSemester end) {
        return new BranchCourseGroup(parentCourseGroup, name, nameEn, branchType, begin, end);
    }

    public CurricularCourse createCurricularCourse(Double weight, String prerequisites, String prerequisitesEn,
            CurricularStage curricularStage, CompetenceCourse competenceCourse, CourseGroup parentCourseGroup,
            CurricularPeriod curricularPeriod, ExecutionSemester beginExecutionPeriod, ExecutionSemester endExecutionPeriod) {

        if (competenceCourse.getCurricularCourse(this) != null) {
            throw new DomainException("competenceCourse.already.has.a.curricular.course.in.degree.curricular.plan");
        }
        checkIfAnualBeginsInFirstPeriod(competenceCourse, curricularPeriod);

        return new CurricularCourse(weight, prerequisites, prerequisitesEn, curricularStage, competenceCourse, parentCourseGroup,
                curricularPeriod, beginExecutionPeriod, endExecutionPeriod);
    }

    public CurricularCourse createOptionalCurricularCourse(CourseGroup parentCourseGroup, String name, String nameEn,
            CurricularStage curricularStage, CurricularPeriod curricularPeriod, ExecutionSemester beginExecutionPeriod,
            ExecutionSemester endExecutionPeriod) {

        return new OptionalCurricularCourse(parentCourseGroup, name, nameEn, curricularStage, curricularPeriod,
                beginExecutionPeriod, endExecutionPeriod);
    }

    private void checkIfAnualBeginsInFirstPeriod(final CompetenceCourse competenceCourse, final CurricularPeriod curricularPeriod) {
        if (competenceCourse.isAnual() && !curricularPeriod.hasChildOrderValue(1)) {
            throw new DomainException("competenceCourse.anual.but.trying.to.associate.curricular.course.not.to.first.period");
        }
    }

    public List<DegreeModule> getDcpDegreeModules(final Class<? extends DegreeModule> clazz) {
        return getDcpDegreeModules(clazz, (ExecutionYear) null);
    }

    public List<DegreeModule> getDcpDegreeModules(final Class<? extends DegreeModule> clazz, final ExecutionYear executionYear) {
        return hasRoot() ? new ArrayList<DegreeModule>(getRoot().collectAllChildDegreeModules(clazz, executionYear)) : Collections.EMPTY_LIST;
    }

    public List<DegreeModule> getDcpDegreeModules(final Class<? extends DegreeModule> clazz,
            final ExecutionSemester executionSemester) {
        return hasRoot() ? new ArrayList<DegreeModule>(getRoot().collectAllChildDegreeModules(clazz, executionSemester)) : Collections.EMPTY_LIST;
    }

    public List<List<DegreeModule>> getDcpDegreeModulesIncludingFullPath(Class<? extends DegreeModule> clazz,
            ExecutionYear executionYear) {

        final List<List<DegreeModule>> result = new ArrayList<List<DegreeModule>>();
        final List<DegreeModule> path = new ArrayList<DegreeModule>();

        if (clazz.isAssignableFrom(CourseGroup.class)) {
            path.add(this.getRoot());

            result.add(path);
        }

        this.getRoot().collectChildDegreeModulesIncludingFullPath(clazz, result, path, executionYear);

        return result;
    }

    public Branch getBranchByName(final String branchName) {
        if (branchName != null) {
            for (final Branch branch : getAreas()) {
                if (branchName.equals(branch.getName())) {
                    return branch;
                }
            }
        }
        return null;
    }

    public Boolean getUserCanBuild() {
        Person person = AccessControl.getPerson();
        return AcademicPredicates.MANAGE_DEGREE_CURRICULAR_PLANS.evaluate(this.getDegree())
                || (this.isBolonhaDegree() ? this.getCurricularPlanMembersGroup().isMember(person.getUser()) : false);
    }

    public Boolean getCanModify() {
        if (isApproved()) {
            return false;
        }

        final Collection<ExecutionDegree> executionDegrees = getExecutionDegrees();
        return executionDegrees.size() > 1 ? false : executionDegrees.isEmpty()
                || executionDegrees.iterator().next().getExecutionYear().isCurrent();
    }

    public Group getCurricularPlanMembersGroup() {
        return getMembersGroup() != null ? getMembersGroup().toGroup() : NobodyGroup.get();
    }

    public void setCurricularPlanMembersGroup(Group group) {
        check(this, DegreeCurricularPlanPredicates.scientificCouncilWritePredicate);
        setMembersGroup(group.toPersistentGroup());
    }

    @Override
    public void setCurricularStage(CurricularStage curricularStage) {
        check(this, DegreeCurricularPlanPredicates.scientificCouncilWritePredicate);
        super.setCurricularStage(curricularStage);
    }

    @Override
    public void setDegree(Degree degree) {
        check(this, DegreeCurricularPlanPredicates.scientificCouncilWritePredicate);
        super.setDegree(degree);
    }

    @Override
    public void setRoot(RootCourseGroup courseGroup) {
        check(this, DegreeCurricularPlanPredicates.scientificCouncilWritePredicate);
        super.setRoot(courseGroup);
    }

    @Override
    public void setDegreeStructure(CurricularPeriod degreeStructure) {
        check(this, DegreeCurricularPlanPredicates.scientificCouncilWritePredicate);
        super.setDegreeStructure(degreeStructure);
    }

    @Override
    public void setGradeScale(GradeScale gradeScale) {
        check(this, DegreeCurricularPlanPredicates.scientificCouncilWritePredicate);
        super.setGradeScale(gradeScale);
    }

    @Override
    public void setName(String name) {
        check(this, DegreeCurricularPlanPredicates.scientificCouncilWritePredicate);
        super.setName(name);
    }

    public String getPresentationName() {
        return getPresentationName(ExecutionYear.readCurrentExecutionYear(), I18N.getLocale());
    }

    public String getPresentationName(final ExecutionYear executionYear) {
        return getPresentationName(executionYear, I18N.getLocale());
    }

    public String getPresentationName(final ExecutionYear executionYear, final Locale locale) {
        return getDegree().getPresentationName(executionYear, locale) + " - " + getName();
    }

    public Set<MasterDegreeCandidate> readMasterDegreeCandidates() {
        final Set<MasterDegreeCandidate> result = new HashSet<MasterDegreeCandidate>();
        for (final ExecutionDegree executionDegree : this.getExecutionDegreesSet()) {
            result.addAll(executionDegree.getMasterDegreeCandidatesSet());
        }
        return result;
    }

    public Set<MasterDegreeCandidate> readMasterDegreeCandidatesBySpecialization(final Specialization specialization) {
        final Set<MasterDegreeCandidate> result = new HashSet<MasterDegreeCandidate>();
        for (final MasterDegreeCandidate masterDegreeCandidate : readMasterDegreeCandidates()) {
            if (masterDegreeCandidate.getSpecialization() == specialization) {
                result.add(masterDegreeCandidate);
            }
        }
        return result;
    }

    public Set<MasterDegreeCandidate> readMasterDegreeCandidatesBySituatioName(final SituationName situationName) {
        final Set<MasterDegreeCandidate> result = new HashSet<MasterDegreeCandidate>();
        for (final MasterDegreeCandidate masterDegreeCandidate : readMasterDegreeCandidates()) {
            if (masterDegreeCandidate.hasCandidateSituationWith(situationName)) {
                result.add(masterDegreeCandidate);
            }
        }
        return result;
    }

    public Set<MasterDegreeCandidate> readMasterDegreeCandidatesByCourseAssistant(boolean courseAssistant) {
        final Set<MasterDegreeCandidate> result = new HashSet<MasterDegreeCandidate>();
        for (final MasterDegreeCandidate masterDegreeCandidate : readMasterDegreeCandidates()) {
            if (masterDegreeCandidate.getCourseAssistant() == courseAssistant) {
                result.add(masterDegreeCandidate);
            }
        }
        return result;
    }

    public List<MasterDegreeThesisDataVersion> readActiveMasterDegreeThesisDataVersions() {
        List<MasterDegreeThesisDataVersion> masterDegreeThesisDataVersions = new ArrayList<MasterDegreeThesisDataVersion>();
        for (StudentCurricularPlan studentCurricularPlan : this.getStudentCurricularPlans()) {
            MasterDegreeThesisDataVersion masterDegreeThesisDataVersion =
                    studentCurricularPlan.readActiveMasterDegreeThesisDataVersion();
            if (masterDegreeThesisDataVersion != null) {
                masterDegreeThesisDataVersions.add(masterDegreeThesisDataVersion);
            }
        }
        return masterDegreeThesisDataVersions;
    }

    // -------------------------------------------------------------
    // read static methods
    // -------------------------------------------------------------

    public static List<DegreeCurricularPlan> readNotEmptyDegreeCurricularPlans() {
        final List<DegreeCurricularPlan> result =
                new ArrayList<DegreeCurricularPlan>(Bennu.getInstance().getDegreeCurricularPlansSet());
        result.remove(readEmptyDegreeCurricularPlan());
        return result;
    }

    public static DegreeCurricularPlan readEmptyDegreeCurricularPlan() {
        return EmptyDegreeCurricularPlan.getInstance();
    }

    public static Set<DegreeCurricularPlan> readBolonhaDegreeCurricularPlans() {
        final Set<DegreeCurricularPlan> result = new HashSet<DegreeCurricularPlan>();

        for (final Degree degree : Degree.readBolonhaDegrees()) {
            result.addAll(degree.getDegreeCurricularPlans());
        }

        return result;
    }

    public static Set<DegreeCurricularPlan> readPreBolonhaDegreeCurricularPlans() {
        final Set<DegreeCurricularPlan> result = new HashSet<DegreeCurricularPlan>();

        for (final Degree degree : Degree.readOldDegrees()) {
            result.addAll(degree.getDegreeCurricularPlans());
        }

        return result;
    }

    static public List<DegreeCurricularPlan> readByCurricularStage(final CurricularStage curricularStage) {
        final List<DegreeCurricularPlan> result = new ArrayList<DegreeCurricularPlan>();
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
    public static List<DegreeCurricularPlan> readByDegreeTypeAndState(DegreeType degreeType, DegreeCurricularPlanState state) {
        List<DegreeCurricularPlan> result = new ArrayList<DegreeCurricularPlan>();
        for (DegreeCurricularPlan degreeCurricularPlan : readNotEmptyDegreeCurricularPlans()) {
            if (degreeCurricularPlan.getDegree().getDegreeType() == degreeType
                    && (state == null || degreeCurricularPlan.getState() == state)) {

                result.add(degreeCurricularPlan);
            }
        }
        return result;
    }

    /**
     * If state is null then just degree type is checked
     */
    public static List<DegreeCurricularPlan> readByDegreeTypesAndState(Set<DegreeType> degreeTypes,
            DegreeCurricularPlanState state) {
        List<DegreeCurricularPlan> result = new ArrayList<DegreeCurricularPlan>();
        for (DegreeCurricularPlan degreeCurricularPlan : readNotEmptyDegreeCurricularPlans()) {
            if (degreeTypes.contains(degreeCurricularPlan.getDegree().getDegreeType())
                    && (state == null || degreeCurricularPlan.getState() == state)) {

                result.add(degreeCurricularPlan);
            }
        }
        return result;
    }

    public static DegreeCurricularPlan readByNameAndDegreeSigla(String name, String degreeSigla) {
        for (final DegreeCurricularPlan degreeCurricularPlan : readNotEmptyDegreeCurricularPlans()) {
            if (degreeCurricularPlan.getName().equalsIgnoreCase(name)
                    && degreeCurricularPlan.getDegree().getSigla().equalsIgnoreCase(degreeSigla)) {
                return degreeCurricularPlan;
            }
        }
        return null;
    }

    public Set<CurricularCourseScope> findCurricularCourseScopesIntersectingPeriod(final Date beginDate, final Date endDate) {
        final Set<CurricularCourseScope> curricularCourseScopes = new HashSet<CurricularCourseScope>();
        for (final CurricularCourse curricularCourse : getCurricularCoursesSet()) {
            curricularCourseScopes.addAll(curricularCourse.findCurricularCourseScopesIntersectingPeriod(beginDate, endDate));
        }
        return curricularCourseScopes;
    }

    public ExecutionDegree createExecutionDegree(ExecutionYear executionYear, Space campus, Boolean publishedExamMap) {

        if (isBolonhaDegree() && isDraft()) {
            throw new DomainException("degree.curricular.plan.not.approved.cannot.create.execution.degree", this.getName());
        }

        if (this.hasAnyExecutionDegreeFor(executionYear)) {
            throw new DomainException("degree.curricular.plan.already.has.execution.degree.for.this.year", this.getName(),
                    executionYear.getYear());
        }

        return new ExecutionDegree(this, executionYear, campus, publishedExamMap);
    }

    public CurricularPeriod getCurricularPeriodFor(int year, int semester) {
        final CurricularPeriodInfoDTO[] curricularPeriodInfos = buildCurricularPeriodInfoDTOsFor(year, semester);
        return getDegreeStructure().getCurricularPeriod(curricularPeriodInfos);
    }

    private CurricularPeriodInfoDTO[] buildCurricularPeriodInfoDTOsFor(int year, int semester) {
        final CurricularPeriodInfoDTO[] curricularPeriodInfos;
        if (getDegreeType().getYears() > 1) {

            curricularPeriodInfos =
                    new CurricularPeriodInfoDTO[] { new CurricularPeriodInfoDTO(year, AcademicPeriod.YEAR),
                            new CurricularPeriodInfoDTO(semester, AcademicPeriod.SEMESTER) };

        } else {
            curricularPeriodInfos =
                    new CurricularPeriodInfoDTO[] { new CurricularPeriodInfoDTO(semester, AcademicPeriod.SEMESTER) };
        }
        return curricularPeriodInfos;
    }

    public CurricularPeriod createCurricularPeriodFor(int year, int semester) {
        final CurricularPeriodInfoDTO[] curricularPeriodInfos = buildCurricularPeriodInfoDTOsFor(year, semester);

        return getDegreeStructure().addCurricularPeriod(curricularPeriodInfos);
    }

    @Override
    public YearMonthDay getInitialDateYearMonthDay() {
        if (isBolonhaDegree() && hasAnyExecutionDegrees()) {
            final ExecutionDegree firstExecutionDegree = getFirstExecutionDegree();
            return firstExecutionDegree.getExecutionYear().getBeginDateYearMonthDay();
        } else {
            return super.getInitialDateYearMonthDay();
        }
    }

    @Override
    public YearMonthDay getEndDateYearMonthDay() {
        if (isBolonhaDegree() && hasAnyExecutionDegrees()) {
            final ExecutionDegree mostRecentExecutionDegree = getMostRecentExecutionDegree();
            if (mostRecentExecutionDegree.getExecutionYear() == ExecutionYear.readCurrentExecutionYear()) {
                return null;
            } else {
                return mostRecentExecutionDegree.getExecutionYear().getBeginDateYearMonthDay();
            }
        } else {
            return super.getEndDateYearMonthDay();
        }
    }

    public void addExecutionCourses(final Collection<ExecutionCourseView> executionCourseViews,
            final ExecutionSemester... executionPeriods) {
        if (executionCourseViews != null && executionPeriods != null) {
            // Pre-Bolonha structure search
            for (final CurricularCourse curricularCourse : super.getCurricularCoursesSet()) {
                for (final ExecutionSemester executionSemester : executionPeriods) {
                    for (final ExecutionCourse executionCourse : curricularCourse.getAssociatedExecutionCoursesSet()) {
                        if (executionCourse.getExecutionPeriod() == executionSemester) {
                            for (final CurricularCourseScope curricularCourseScope : curricularCourse.getScopesSet()) {
                                if (curricularCourseScope.isActiveForExecutionPeriod(executionSemester)) {
                                    executionCourseViews
                                            .add(constructExecutionCourseView(executionCourse, curricularCourseScope));
                                }
                            }
                        }
                    }
                }
            }

            // Bolonha structure search
            CourseGroup root = getRoot();
            if (root != null) {
                addExecutionCourses(root, executionCourseViews, executionPeriods);
            }
        }
    }

    private void addExecutionCourses(final CourseGroup courseGroup, final Collection<ExecutionCourseView> executionCourseViews,
            final ExecutionSemester... executionPeriods) {
        for (final Context context : courseGroup.getChildContextsSet()) {
            for (final ExecutionSemester executionSemester : executionPeriods) {
                if (context.isValid(executionSemester)) {
                    final DegreeModule degreeModule = context.getChildDegreeModule();
                    if (degreeModule.isLeaf()) {
                        final CurricularCourse curricularCourse = (CurricularCourse) degreeModule;
                        for (final ExecutionCourse executionCourse : curricularCourse.getAssociatedExecutionCoursesSet()) {
                            if (executionCourse.getExecutionPeriod() == executionSemester) {
                                final Integer curricularYear = context.getCurricularYear();
                                executionCourseViews.add(constructExecutionCourseView(executionCourse, curricularYear));
                            }
                        }
                    } else {
                        final CourseGroup childCourseGroup = (CourseGroup) degreeModule;
                        addExecutionCourses(childCourseGroup, executionCourseViews, executionPeriods);
                    }
                }
            }
        }
    }

    private ExecutionCourseView constructExecutionCourseView(final ExecutionCourse executionCourse, final Integer curricularYear) {
        final ExecutionCourseView executionCourseView = new ExecutionCourseView(executionCourse);
        executionCourseView.setCurricularYear(curricularYear);
        executionCourseView.setDegreeCurricularPlanAnotation(getAnotation());
        return executionCourseView;
    }

    private ExecutionCourseView constructExecutionCourseView(final ExecutionCourse executionCourse,
            final CurricularCourseScope curricularCourseScope) {
        final Integer curricularYear = curricularCourseScope.getCurricularSemester().getCurricularYear().getYear();
        final ExecutionCourseView executionCourseView = constructExecutionCourseView(executionCourse, curricularYear);
        executionCourseView.setAnotation(curricularCourseScope.getAnotation());
        return executionCourseView;
    }

    public Collection<StudentCurricularPlan> getActiveStudentCurricularPlans() {
        final Collection<StudentCurricularPlan> result = new HashSet<StudentCurricularPlan>();

        for (StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
            if (studentCurricularPlan.isActive()) {
                result.add(studentCurricularPlan);
            }
        }

        return result;
    }

    public Set<Registration> getRegistrations() {
        final Set<Registration> registrations = new HashSet<Registration>();

        for (StudentCurricularPlan studentCurricularPlan : getActiveStudentCurricularPlans()) {
            registrations.add(studentCurricularPlan.getRegistration());
        }

        return registrations;
    }

    public Collection<Registration> getActiveRegistrations() {
        final Collection<Registration> result = new HashSet<Registration>();

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
        if (hasAnyExecutionDegrees()) {
            return getMostRecentExecutionDegree().getCampus();
        }
        return SpaceUtils.getDefaultCampus();
    }

    @Override
    public Integer getDegreeDuration() {
        final Integer degreeDuration = super.getDegreeDuration();
        return degreeDuration == null ? Integer.valueOf(getDegree().getDegreeType().getYears()) : degreeDuration;
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

    public String getGraduateTitle() {
        return getGraduateTitle(ExecutionYear.readCurrentExecutionYear(), I18N.getLocale());
    }

    public String getGraduateTitle(final ExecutionYear executionYear, final Locale locale) {
        return getGraduateTitle(executionYear, (CycleType) null, locale);
    }

    public String getGraduateTitle(final ExecutionYear executionYear, final CycleType cycleType, final Locale locale) {
        if (cycleType == null) {
            if (isBolonhaDegree()) {
                return getLastOrderedCycleCourseGroup().getGraduateTitle(executionYear, locale);
            } else {
                final StringBuilder res = new StringBuilder(getDegreeType().getGraduateTitle(locale));
                res.append(" ").append(BundleUtil.getString(Bundle.APPLICATION, locale, "label.in"));
                res.append(" ").append(getDegree().getFilteredName(executionYear, locale));

                return res.toString();
            }
        }

        if (getDegreeType().getCycleTypes().isEmpty()) {
            throw new DomainException("DegreeCurricularPlan.has.no.cycle.type");
        }

        if (!getDegreeType().hasCycleTypes(cycleType)) {
            throw new DomainException("DegreeCurricularPlan.doesnt.have.such.cycle.type");
        }

        return getCycleCourseGroup(cycleType).getGraduateTitle(executionYear, locale);
    }

    public List<CurricularCourse> getDissertationCurricularCourses(ExecutionYear year) {
        List<CurricularCourse> result = new ArrayList<CurricularCourse>();

        List<ExecutionYear> years = new ArrayList<ExecutionYear>();

        if (year == null) {
            year = ExecutionYear.readCurrentExecutionYear();
            while (year != null) {
                years.add(year);
                year = year.getPreviousExecutionYear();
            }
        } else {
            years.add(year);
        }

        for (ExecutionYear executionYear : years) {
            for (CurricularCourse curricularCourse : getCurricularCourses(executionYear)) {
                if (curricularCourse.isDissertation()) {
                    result.add(curricularCourse);
                }
            }
        }

        return result;
    }

    public Set<Thesis> getThesis(ExecutionYear executionYear) {
        final Set<Thesis> thesis = new HashSet<Thesis>();
        for (CurricularCourse curricularCourse : getDissertationCurricularCourses(executionYear)) {
            for (Enrolment enrolment : curricularCourse.getEnrolmentsByExecutionYear(executionYear)) {
                StudentCurricularPlan studentCurricularPlan = enrolment.getStudentCurricularPlan();

                if (studentCurricularPlan.getDegreeCurricularPlan() != this) {
                    continue;
                }
                thesis.addAll(enrolment.getTheses());
            }
        }
        return thesis;
    }

    public Set<Enrolment> getDissertationEnrolments(ExecutionYear executionYear) {
        final Set<Enrolment> enrolments = new HashSet<Enrolment>();
        for (CurricularCourse curricularCourse : getDissertationCurricularCourses(executionYear)) {
            for (Enrolment enrolment : curricularCourse.getEnrolmentsByExecutionYear(executionYear)) {
                enrolments.add(enrolment);
            }
        }
        return enrolments;
    }

    public List<CurricularCourse> getDissertationCurricularCourses() {
        return getDissertationCurricularCourses(ExecutionYear.readCurrentExecutionYear());
    }

    public DegreeCurricularPlanEquivalencePlan createEquivalencePlan(final DegreeCurricularPlan sourceDegreeCurricularPlan) {
        return new DegreeCurricularPlanEquivalencePlan(this, sourceDegreeCurricularPlan);
    }

    public boolean hasDegreeModule(final DegreeModule degreeModule) {
        return hasRoot() ? getRoot().hasDegreeModule(degreeModule) : false;
    }

    public final List<StudentCurricularPlan> getLastStudentCurricularPlan() {
        List<StudentCurricularPlan> studentCurricularPlans = new ArrayList<StudentCurricularPlan>();
        for (StudentCurricularPlan studentCurricularPlan : this.getStudentCurricularPlans()) {
            studentCurricularPlans.add(studentCurricularPlan.getRegistration().getLastStudentCurricularPlan());

        }
        return studentCurricularPlans;
    }

    public Set<CourseGroup> getAllCoursesGroups() {
        final Set<DegreeModule> courseGroups = new TreeSet<DegreeModule>(DegreeModule.COMPARATOR_BY_NAME) {
            @Override
            public boolean add(DegreeModule degreeModule) {
                return degreeModule instanceof CourseGroup && super.add(degreeModule);
            }
        };
        if (hasRoot()) {
            courseGroups.add(getRoot());
            getRoot().getAllDegreeModules(courseGroups);
        }
        return (Set) courseGroups;
    }

    public Set<BranchCourseGroup> getAllBranches() {
        final Set<DegreeModule> branches = new TreeSet<DegreeModule>(DegreeModule.COMPARATOR_BY_NAME) {
            @Override
            public boolean add(DegreeModule degreeModule) {
                return degreeModule instanceof BranchCourseGroup && super.add(degreeModule);
            }
        };
        if (hasRoot()) {
            branches.add(getRoot());
            getRoot().getAllDegreeModules(branches);
        }
        return (Set) branches;
    }

    public Set<BranchCourseGroup> getBranchesByType(net.sourceforge.fenixedu.domain.degreeStructure.BranchType branchType) {
        final Set<BranchCourseGroup> branchesByType = new TreeSet<BranchCourseGroup>(DegreeModule.COMPARATOR_BY_NAME);
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
        return getBranchesByType(net.sourceforge.fenixedu.domain.degreeStructure.BranchType.MAJOR);
    }

    public Set<BranchCourseGroup> getMinorBranches() {
        return getBranchesByType(net.sourceforge.fenixedu.domain.degreeStructure.BranchType.MINOR);
    }

    public boolean hasBranches() {
        return getAllBranches().isEmpty() ? false : true;
    }

    public boolean hasBranchesByType(net.sourceforge.fenixedu.domain.degreeStructure.BranchType branchType) {
        return getBranchesByType(branchType).isEmpty() ? false : true;
    }

    public Set<DegreeModule> getAllDegreeModules() {
        final Set<DegreeModule> degreeModules = new TreeSet<DegreeModule>(DegreeModule.COMPARATOR_BY_NAME);
        final RootCourseGroup rootCourseGroup = getRoot();
        if (rootCourseGroup != null) {
            rootCourseGroup.getAllDegreeModules(degreeModules);
        }
        degreeModules.addAll(super.getCurricularCoursesSet());
        return degreeModules;
    }

    public static Set<DegreeCurricularPlan> getDegreeCurricularPlans(final Set<DegreeType> degreeTypes) {
        final Set<DegreeCurricularPlan> degreeCurricularPlans =
                new TreeSet<DegreeCurricularPlan>(DegreeCurricularPlan.COMPARATOR_BY_PRESENTATION_NAME);

        for (final Degree degree : Degree.readNotEmptyDegrees()) {
            if (degreeTypes.contains(degree.getDegreeType())) {
                for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlansSet()) {
                    if (degreeCurricularPlan.isActive()) {
                        degreeCurricularPlans.add(degreeCurricularPlan);
                    }
                }
            }
        }
        return degreeCurricularPlans;
    }

    public List<StudentCurricularPlan> getStudentsCurricularPlanGivenEntryYear(ExecutionYear entryYear) {
        List<StudentCurricularPlan> studentsGivenEntryYear = new ArrayList<StudentCurricularPlan>();
        ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();

        for (Registration registration : this.getActiveRegistrations()) {
            if (registration.getStartDate() != null && registration.getStartExecutionYear().equals(entryYear)
                    && registration.isRegistered(currentExecutionYear)
                    && !registration.getRegistrationAgreement().equals(RegistrationAgreement.ERASMUS)) {
                studentsGivenEntryYear.add(registration.getActiveStudentCurricularPlan());
            }
        }
        return studentsGivenEntryYear;
    }

    /*
     * Returns a list of students without tutor for the given entry year. This
     * students never had a tutor. Students that have expired tutorships do not
     * appear in this list.
     */
    public List<StudentCurricularPlan> getStudentsWithoutTutorGivenEntryYear(ExecutionYear entryYear) {
        List<StudentCurricularPlan> studentsWithoutTutor = new ArrayList<StudentCurricularPlan>();
        for (StudentCurricularPlan scp : getStudentsCurricularPlanGivenEntryYear(entryYear)) {
            if (scp.getActiveTutorship() == null && scp.getTutorships().isEmpty()) {
                studentsWithoutTutor.add(scp);
            }
        }
        Collections.sort(studentsWithoutTutor, StudentCurricularPlan.COMPARATOR_BY_STUDENT_NUMBER);
        Collections.reverse(studentsWithoutTutor);
        return studentsWithoutTutor;
    }

    public Set<CurricularCourse> getCurricularCoursesByExecutionYearAndCurricularYear(ExecutionYear executionYear,
            Integer curricularYear) {
        Set<CurricularCourse> result = new HashSet<CurricularCourse>();

        for (final CurricularCourse curricularCourse : getCurricularCoursesWithExecutionIn(executionYear)) {
            for (final DegreeModuleScope degreeModuleScope : curricularCourse.getDegreeModuleScopes()) {
                if (degreeModuleScope.getCurricularYear().equals(curricularYear)) {
                    result.add(curricularCourse);
                }
            }
        }
        return result;
    }

    /**
     * This must be completely refactored. A pattern of some sort is desirable
     * in order to make this instance-dependent. Just did this due to time
     * constrains.
     */

    static final Set<String> bestAverage = new HashSet<String>();
    static {
        bestAverage.add("MB02/04");
        bestAverage.add("MB03/05");
        bestAverage.add("MIOES02/04");
        bestAverage.add("MT02/04");
        bestAverage.add("MT03/05");
        bestAverage.add("MT05/07");
    };

    static final Set<String> weightedAverage = new HashSet<String>();
    static {
        weightedAverage.add("MEE02/04");
        weightedAverage.add("MEE03/05");
        weightedAverage.add("MF02/04");
        weightedAverage.add("MF03/05");
        weightedAverage.add("MC02/04");
        weightedAverage.add("MC03/05");
        weightedAverage.add("MEMAT02/04");
        weightedAverage.add("MEQ03/04");
        weightedAverage.add("MSIG02/04");
        weightedAverage.add("MCES02/04");
        weightedAverage.add("MEIC02/04");
        weightedAverage.add("MEIC03/05");
        weightedAverage.add("ML03/05");
        weightedAverage.add("ML02/04");
        weightedAverage.add("ML05/07");
        weightedAverage.add("MEE04/06");
        weightedAverage.add("MEE05/07");
    };

    public AverageType getAverageType() {
        if (getDegreeType() == DegreeType.MASTER_DEGREE) {
            if (bestAverage.contains(getName())) {
                return AverageType.BEST;
            } else if (weightedAverage.contains(getName())) {
                return AverageType.WEIGHTED;
            } else {
                return AverageType.SIMPLE;
            }
        } else {
            return AverageType.WEIGHTED;
        }
    }

    public Set<Registration> getRegistrations(ExecutionYear executionYear, Set<Registration> registrations) {
        for (final StudentCurricularPlan studentCurricularPlan : this.getStudentCurricularPlans()) {
            if (studentCurricularPlan.isActive(executionYear)) {
                if (studentCurricularPlan.getRegistration() != null) {
                    registrations.add(studentCurricularPlan.getRegistration());
                }
            }
        }
        return registrations;
    }

    public List<StudentCurricularPlan> getStudentsCurricularPlans(ExecutionYear executionYear, List<StudentCurricularPlan> result) {
        for (final StudentCurricularPlan studentCurricularPlan : this.getStudentCurricularPlans()) {
            if (studentCurricularPlan.isActive(executionYear)) {
                result.add(studentCurricularPlan);
            }
        }
        return result;
    }

    public boolean isToApplyPreviousYearsEnrolmentRule() {
        return getApplyPreviousYearsEnrolmentRule();
    }

    public ExecutionSemester getFirstExecutionPeriodEnrolments() {
        return ExecutionSemester.readFirstEnrolmentsExecutionPeriod();
    }

    public boolean hasTargetEquivalencePlanFor(final DegreeCurricularPlan degreeCurricularPlan) {
        for (final DegreeCurricularPlanEquivalencePlan equivalencePlan : getTargetEquivalencePlansSet()) {
            if (equivalencePlan.getDegreeCurricularPlan().equals(degreeCurricularPlan)) {
                return true;
            }
        }
        return false;
    }

    public boolean canSubmitImprovementMarkSheets(final ExecutionYear executionYear) {
        SortedSet<ExecutionDegree> sortedExecutionDegrees =
                new TreeSet<ExecutionDegree>(ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_YEAR);
        sortedExecutionDegrees.addAll(getExecutionDegreesSet());
        return sortedExecutionDegrees.last().getExecutionYear().equals(executionYear.getPreviousExecutionYear());
    }

    public Set<ExecutionYear> getBeginContextExecutionYears() {
        return isBoxStructure() ? getRoot().getBeginContextExecutionYears() : Collections.EMPTY_SET;
    }

    public ExecutionYear getOldestContextExecutionYear() {
        List<ExecutionYear> beginContextExecutionYears = new ArrayList<ExecutionYear>(getBeginContextExecutionYears());

        Collections.sort(beginContextExecutionYears, ExecutionYear.COMPARATOR_BY_YEAR);

        return beginContextExecutionYears.isEmpty() ? null : beginContextExecutionYears.iterator().next();
    }

    public MultiLanguageString getDescriptionI18N() {
        MultiLanguageString result = new MultiLanguageString();

        if (!StringUtils.isEmpty(getDescription())) {
            result = result.with(MultiLanguageString.pt, getDescription());
        }
        if (!StringUtils.isEmpty(getDescriptionEn())) {
            result = result.with(MultiLanguageString.en, getDescriptionEn());
        }

        return result;
    }

    public Collection<CycleCourseGroup> getDestinationAffinities(final CycleType sourceCycleType) {
        if (hasRoot()) {
            final CycleCourseGroup cycleCourseGroup = getRoot().getCycleCourseGroup(sourceCycleType);
            if (cycleCourseGroup != null) {
                return cycleCourseGroup.getDestinationAffinities();
            }
        }
        return Collections.emptyList();
    }

    public Double getEctsCredits() {
        return getDegree().getEctsCredits();
    }

    public boolean isScientificCommissionMember(final ExecutionYear executionYear) {
        final ExecutionDegree executionDegree = getExecutionDegreeByYear(executionYear);
        return executionDegree != null && executionDegree.isScientificCommissionMember();
    }

    public boolean isScientificCommissionMember(final ExecutionYear executionYear, final Person person) {
        final ExecutionDegree executionDegree = getExecutionDegreeByYear(executionYear);
        return executionDegree != null && executionDegree.isScientificCommissionMember(person);
    }

    public void checkUserIsScientificCommissionMember(final ExecutionYear executionYear) {
        if (!isScientificCommissionMember(executionYear)) {
            throw new DomainException("degree.scientificCommission.notMember");
        }
    }

    public static List<DegreeCurricularPlan> readByDegreeTypesAndStateWithExecutionDegreeForYear(Set<DegreeType> degreeTypes,
            DegreeCurricularPlanState state, ExecutionYear executionYear) {

        final List<DegreeCurricularPlan> result = new ArrayList<DegreeCurricularPlan>();
        for (final DegreeCurricularPlan degreeCurricularPlan : readByDegreeTypesAndState(degreeTypes, state)) {
            if (degreeCurricularPlan.hasExecutionDegreeFor(executionYear)) {
                result.add(degreeCurricularPlan);
            }
        }

        return result;

    }

    public static Collection<DegreeCurricularPlan> readByAcademicInterval(AcademicInterval academicInterval) {
        ExecutionYear year = ExecutionYear.readByAcademicInterval(academicInterval);
        return year.getDegreeCurricularPlans();
    }

    public boolean isCurrentUserScientificCommissionMember(final ExecutionYear executionYear) {
        final Person person = AccessControl.getPerson();
        return person != null && getDegree().isMemberOfCurrentScientificCommission(person, executionYear);
    }

    public ExecutionYear getInauguralExecutionYear() {
        return Collections.min(getExecutionDegreesSet(), ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_YEAR).getExecutionYear();
    }

    public ExecutionYear getLastExecutionYear() {
        return Collections.max(getExecutionDegreesSet(), ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_YEAR).getExecutionYear();
    }

    @Deprecated
    public java.util.Date getEndDate() {
        org.joda.time.YearMonthDay ymd = getEndDateYearMonthDay();
        return ymd == null ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Deprecated
    public void setEndDate(java.util.Date date) {
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
    public void setInitialDate(java.util.Date date) {
        if (date == null) {
            setInitialDateYearMonthDay(null);
        } else {
            setInitialDateYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
        }
    }

    public Set<CurricularCourse> getCurricularCoursesWithoutExecutionCourseFor(final ExecutionSemester semester) {
        Set<CurricularCourse> curricularCourses = getCurricularCourses(semester);

        Set<CurricularCourse> result = new HashSet<CurricularCourse>();

        for (CurricularCourse curricularCourse : curricularCourses) {
            if (curricularCourse.getExecutionCoursesByExecutionPeriod(semester).isEmpty()) {
                result.add(curricularCourse);
            }
        }

        return result;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Branch> getAreas() {
        return getAreasSet();
    }

    @Deprecated
    public boolean hasAnyAreas() {
        return !getAreasSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.TutorshipIntention> getTutorshipIntention() {
        return getTutorshipIntentionSet();
    }

    @Deprecated
    public boolean hasAnyTutorshipIntention() {
        return !getTutorshipIntentionSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.ExecutionDegree> getExecutionDegrees() {
        return getExecutionDegreesSet();
    }

    @Deprecated
    public boolean hasAnyExecutionDegrees() {
        return !getExecutionDegreesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.DegreeContext> getDegreeContexts() {
        return getDegreeContextsSet();
    }

    @Deprecated
    public boolean hasAnyDegreeContexts() {
        return !getDegreeContextsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.DegreeCurricularPlanEquivalencePlan> getTargetEquivalencePlans() {
        return getTargetEquivalencePlansSet();
    }

    @Deprecated
    public boolean hasAnyTargetEquivalencePlans() {
        return !getTargetEquivalencePlansSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.CurricularCourse> getCurricularCourses() {
        return getCurricularCoursesSet();
    }

    @Deprecated
    public boolean hasAnyCurricularCourses() {
        return !getCurricularCoursesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.StudentCurricularPlan> getStudentCurricularPlans() {
        return getStudentCurricularPlansSet();
    }

    @Deprecated
    public boolean hasAnyStudentCurricularPlans() {
        return !getStudentCurricularPlansSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.EnrolmentPeriod> getEnrolmentPeriods() {
        return getEnrolmentPeriodsSet();
    }

    @Deprecated
    public boolean hasAnyEnrolmentPeriods() {
        return !getEnrolmentPeriodsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.CurricularCourseEquivalence> getCurricularCourseEquivalences() {
        return getCurricularCourseEquivalencesSet();
    }

    @Deprecated
    public boolean hasAnyCurricularCourseEquivalences() {
        return !getCurricularCourseEquivalencesSet().isEmpty();
    }

    @Deprecated
    public boolean hasDegreeStructure() {
        return getDegreeStructure() != null;
    }

    @Deprecated
    public boolean hasDescription() {
        return getDescription() != null;
    }

    @Deprecated
    public boolean hasMinimalYearForOptionalCourses() {
        return getMinimalYearForOptionalCourses() != null;
    }

    @Deprecated
    public boolean hasServiceAgreementTemplate() {
        return getServiceAgreementTemplate() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasNumerusClausus() {
        return getNumerusClausus() != null;
    }

    @Deprecated
    public boolean hasApplyPreviousYearsEnrolmentRule() {
        return getApplyPreviousYearsEnrolmentRule() != null;
    }

    @Deprecated
    public boolean hasAnotation() {
        return getAnotation() != null;
    }

    @Deprecated
    public boolean hasGradeScale() {
        return getGradeScale() != null;
    }

    @Deprecated
    public boolean hasShift() {
        return getShift() != null;
    }

    @Deprecated
    public boolean hasInitialDateYearMonthDay() {
        return getInitialDateYearMonthDay() != null;
    }

    @Deprecated
    public boolean hasDescriptionEn() {
        return getDescriptionEn() != null;
    }

    @Deprecated
    public boolean hasName() {
        return getName() != null;
    }

    @Deprecated
    public boolean hasCurricularPlanMembersGroup() {
        return getCurricularPlanMembersGroup() != null;
    }

    @Deprecated
    public boolean hasEquivalencePlan() {
        return getEquivalencePlan() != null;
    }

    @Deprecated
    public boolean hasNeededCredits() {
        return getNeededCredits() != null;
    }

    @Deprecated
    public boolean hasState() {
        return getState() != null;
    }

    @Deprecated
    public boolean hasEndDateYearMonthDay() {
        return getEndDateYearMonthDay() != null;
    }

    @Deprecated
    public boolean hasMarkType() {
        return getMarkType() != null;
    }

    @Deprecated
    public boolean hasRoot() {
        return getRoot() != null;
    }

    @Deprecated
    public boolean hasDegree() {
        return getDegree() != null;
    }

    @Deprecated
    public boolean hasDegreeDuration() {
        return getDegreeDuration() != null;
    }

    @Deprecated
    public boolean hasCurricularStage() {
        return getCurricularStage() != null;
    }

}
