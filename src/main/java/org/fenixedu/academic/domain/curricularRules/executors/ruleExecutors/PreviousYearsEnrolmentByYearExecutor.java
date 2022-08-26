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
package org.fenixedu.academic.domain.curricularRules.executors.ruleExecutors;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.curricularRules.CreditsLimit;
import org.fenixedu.academic.domain.curricularRules.CurricularRule;
import org.fenixedu.academic.domain.curricularRules.CurricularRuleType;
import org.fenixedu.academic.domain.curricularRules.DegreeModulesSelectionLimit;
import org.fenixedu.academic.domain.curricularRules.Exclusiveness;
import org.fenixedu.academic.domain.curricularRules.ICurricularRule;
import org.fenixedu.academic.domain.curricularRules.PreviousYearsEnrolmentCurricularRule;
import org.fenixedu.academic.domain.curricularRules.executors.RuleResult;
import org.fenixedu.academic.domain.curricularRules.executors.verifyExecutors.VerifyRuleLevel;
import org.fenixedu.academic.domain.degreeStructure.Context;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.domain.enrolment.EnrolmentContext;
import org.fenixedu.academic.domain.enrolment.IDegreeModuleToEvaluate;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumLine;
import org.fenixedu.academic.domain.studentCurriculum.CycleCurriculumGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PreviousYearsEnrolmentByYearExecutor extends CurricularRuleExecutor {

    private static final Logger logger = LoggerFactory.getLogger(PreviousYearsEnrolmentByYearExecutor.class);

    static public interface SkipCollectCurricularCoursesPredicate {
        public boolean skip(final CourseGroup courseGroup, final EnrolmentContext enrolmentContext);
    }

    static private SkipCollectCurricularCoursesPredicate SKIP_COLLECT_CURRICULAR_COURSES_PREDICATE =
            (courseGroup, enrolmentContext) -> {
                return false;
            };

    static public SkipCollectCurricularCoursesPredicate getSkipCollectCurricularCoursesPredicate() {
        return SKIP_COLLECT_CURRICULAR_COURSES_PREDICATE;
    }

    static public void setSkipCollectCurricularCoursesPredicate(final SkipCollectCurricularCoursesPredicate input) {
        if (input != null) {
            SKIP_COLLECT_CURRICULAR_COURSES_PREDICATE = input;
        } else {
            logger.error("Could not set SKIP_COLLECT_CURRICULAR_COURSES_PREDICATE to null");
        }
    }

    @Override
    protected RuleResult executeEnrolmentVerificationWithRules(final ICurricularRule curricularRule,
            IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {

        if (isEnrollingInCourseGroupsOnly(enrolmentContext, sourceDegreeModuleToEvaluate)) {
            return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
        }

        final PreviousYearsEnrolmentCurricularRule previousYearsEnrolmentCurricularRule =
                (PreviousYearsEnrolmentCurricularRule) curricularRule;
        final Map<Integer, Set<CurricularCourse>> curricularCoursesToEnrolByYear = getCurricularCoursesToEnrolByYear(
                previousYearsEnrolmentCurricularRule, enrolmentContext, sourceDegreeModuleToEvaluate);

        printCurricularCoursesToEnrol(curricularCoursesToEnrolByYear);

        return hasAnyCurricularCoursesToEnrolInPreviousYears(enrolmentContext, curricularCoursesToEnrolByYear,
                sourceDegreeModuleToEvaluate);

    }

    private boolean isEnrollingInCourseGroupsOnly(final EnrolmentContext enrolmentContext,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate) {
        for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : enrolmentContext
                .getAllChildDegreeModulesToEvaluateFor(sourceDegreeModuleToEvaluate.getDegreeModule())) {
            if (degreeModuleToEvaluate.isLeaf()) {
                return false;
            }
        }

        return true;
    }

    protected void printCurricularCoursesToEnrol(Map<Integer, Set<CurricularCourse>> curricularCoursesToEnrolByYear) {
        for (final Entry<Integer, Set<CurricularCourse>> entry : curricularCoursesToEnrolByYear.entrySet()) {
            logger.debug("Year " + entry.getKey());
            for (final CurricularCourse curricularCourse : entry.getValue()) {
                logger.debug(curricularCourse.getName());
            }

            logger.debug("-------------");
        }

    }

    protected RuleResult hasAnyCurricularCoursesToEnrolInPreviousYears(final EnrolmentContext enrolmentContext,
            final Map<Integer, Set<CurricularCourse>> curricularCoursesToEnrolByYear,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate) {

        RuleResult result = RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());
        for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : enrolmentContext
                .getAllChildDegreeModulesToEvaluateFor(sourceDegreeModuleToEvaluate.getDegreeModule())) {
            if (degreeModuleToEvaluate.isLeaf()) {

                if (degreeModuleToEvaluate.isAnnualCurricularCourse(enrolmentContext.getExecutionYear())
                        && degreeModuleToEvaluate.getContext() == null) {
                    continue;
                }

                if (degreeModuleToEvaluate.getContext() == null) {
                    throw new DomainException("error.degreeModuleToEvaluate.has.invalid.context",
                            degreeModuleToEvaluate.getName(), degreeModuleToEvaluate.getExecutionInterval().getQualifiedName());
                }

                if (hasCurricularCoursesToEnrolInPreviousYears(curricularCoursesToEnrolByYear,
                        degreeModuleToEvaluate.getContext().getCurricularYear())) {

                    if (degreeModuleToEvaluate.isEnroled()) {
                        result = result.and(createImpossibleRuleResult(sourceDegreeModuleToEvaluate, degreeModuleToEvaluate));
                    } else {
                        result = result.and(createFalseRuleResult(sourceDegreeModuleToEvaluate, degreeModuleToEvaluate));
                    }
                }
            }
        }

        return result;
    }

    protected boolean hasCurricularCoursesToEnrolInPreviousYears(
            Map<Integer, Set<CurricularCourse>> curricularCoursesToEnrolByYear, Integer curricularYear) {
        for (int i = curricularYear; i > 0; i--) {
            final int previousYear = i - 1;

            if (!curricularCoursesToEnrolByYear.containsKey(previousYear)) {
                continue;
            }

            if (!curricularCoursesToEnrolByYear.get(previousYear).isEmpty()) {
                return true;
            }
        }

        return false;

    }

    private Map<Integer, Set<CurricularCourse>> getCurricularCoursesToEnrolByYear(
            final PreviousYearsEnrolmentCurricularRule previousYearsEnrolmentCurricularRule,
            final EnrolmentContext enrolmentContext, final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate) {
        final Map<Integer, Set<CurricularCourse>> result = new HashMap<Integer, Set<CurricularCourse>>();

        for (CourseGroup courseGroup : getCourseGroupsToEvaluate(
                previousYearsEnrolmentCurricularRule.getDegreeModuleToApplyRule(), enrolmentContext)) {
            collectCourseGroupCurricularCoursesToEnrol(result, courseGroup, enrolmentContext, sourceDegreeModuleToEvaluate);
        }

        return result;
    }

    private Collection<CourseGroup> getCourseGroupsToEvaluate(final CourseGroup courseGroup,
            final EnrolmentContext enrolmentContext) {
        if (courseGroup.isRoot()) {
            final Collection<CourseGroup> res = new HashSet<CourseGroup>();
            for (final CycleType cycleType : enrolmentContext.getStudentCurricularPlan().getDegreeType().getCycleTypes()) {
                CycleCurriculumGroup cycleCurriculumGroup =
                        enrolmentContext.getStudentCurricularPlan().getRoot().getCycleCurriculumGroup(cycleType);
                if (cycleCurriculumGroup != null) {
                    if (cycleCurriculumGroup.isExternal()) {
                        throw new DomainException("error.cycleCurriculumGroup.cannot.be.external");
                    }

                    res.add(cycleCurriculumGroup.getDegreeModule());
                }
            }
            return res;
        } else {
            return Collections.singleton(courseGroup);
        }
    }

    protected void collectCourseGroupCurricularCoursesToEnrol(final Map<Integer, Set<CurricularCourse>> result,
            final CourseGroup courseGroup, final EnrolmentContext enrolmentContext,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate) {

        if (!isToCollectCurricularCourses(courseGroup, enrolmentContext, sourceDegreeModuleToEvaluate)) {
            return;
        }

        final int childDegreeModulesCount = getChildDegreeModulesCount(courseGroup, enrolmentContext);

        collectCurricularCoursesToEnrol(result, courseGroup, enrolmentContext, sourceDegreeModuleToEvaluate);

        final int minModules = getMinModules(courseGroup, enrolmentContext.getExecutionYear());
        final int maxModules = getMaxModules(courseGroup, enrolmentContext.getExecutionYear());
        if (minModules == maxModules) {
            if (maxModules == childDegreeModulesCount) {
                // N-N == Nchilds
                collectChildCourseGroupsCurricularCoursesToEnrol(result, courseGroup, enrolmentContext,
                        sourceDegreeModuleToEvaluate);
            } else {
                // N-N <> Nchilds
                if (getSelectedChildDegreeModules(courseGroup, enrolmentContext).size() < minModules) {
                    collectChildCourseGroupsCurricularCoursesToEnrol(result, courseGroup, enrolmentContext,
                            sourceDegreeModuleToEvaluate);
                } else {
                    collectSelectedChildCourseGroupsCurricularCoursesToEnrol(result, courseGroup, enrolmentContext,
                            sourceDegreeModuleToEvaluate);
                }
            }
        } else {
            // N-M
            if (getSelectedChildDegreeModules(courseGroup, enrolmentContext).size() < minModules) {
                collectChildCourseGroupsCurricularCoursesToEnrol(result, courseGroup, enrolmentContext,
                        sourceDegreeModuleToEvaluate);
            } else {
                collectSelectedChildCourseGroupsCurricularCoursesToEnrol(result, courseGroup, enrolmentContext,
                        sourceDegreeModuleToEvaluate);
            }
        }

    }

    protected void collectChildCourseGroupsCurricularCoursesToEnrol(final Map<Integer, Set<CurricularCourse>> result,
            final CourseGroup courseGroup, final EnrolmentContext enrolmentContext,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate) {
        for (final DegreeModule childDegreeModule : courseGroup
                .getChildDegreeModulesValidOnExecutionAggregation(enrolmentContext.getExecutionYear())) {
            if (childDegreeModule.isCourseGroup()) {
                collectCourseGroupCurricularCoursesToEnrol(result, (CourseGroup) childDegreeModule, enrolmentContext,
                        sourceDegreeModuleToEvaluate);
            }
        }
    }

    protected Set<DegreeModule> getSelectedChildDegreeModules(final CourseGroup courseGroup,
            final EnrolmentContext enrolmentContext) {
        final Set<DegreeModule> result = new HashSet<DegreeModule>();

        for (final DegreeModule degreeModule : courseGroup
                .getChildDegreeModulesValidOnExecutionAggregation(enrolmentContext.getExecutionYear())) {
            final StudentCurricularPlan studentCurricularPlan = enrolmentContext.getStudentCurricularPlan();
            if (!degreeModule.isLeaf()) {
                if (studentCurricularPlan.hasDegreeModule(degreeModule)) {
                    result.add(degreeModule);
                }
            } else {
                final CurricularCourse curricularCourse = (CurricularCourse) degreeModule;
                if (studentCurricularPlan.isApproved(curricularCourse)) {
                    final CurriculumLine curriculumLine = studentCurricularPlan.getApprovedCurriculumLine(curricularCourse);
                    if (curriculumLine.getCurriculumGroup().getDegreeModule() != null
                            && curriculumLine.getCurriculumGroup().getDegreeModule() == courseGroup) {
                        result.add(degreeModule);
                    }
                }
            }
        }

        for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : enrolmentContext.getDegreeModulesToEvaluate()) {
            if (degreeModuleToEvaluate.getCurriculumGroup() != null
                    && degreeModuleToEvaluate.getCurriculumGroup().getDegreeModule() == courseGroup) {
                result.add(degreeModuleToEvaluate.getDegreeModule());
            }
        }

        return result;

    }

    protected void collectSelectedChildCourseGroupsCurricularCoursesToEnrol(Map<Integer, Set<CurricularCourse>> result,
            CourseGroup courseGroup, EnrolmentContext enrolmentContext, IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate) {

        for (final DegreeModule degreeModule : getSelectedChildDegreeModules(courseGroup, enrolmentContext)) {
            if (degreeModule.isCourseGroup()) {
                collectCourseGroupCurricularCoursesToEnrol(result, (CourseGroup) degreeModule, enrolmentContext,
                        sourceDegreeModuleToEvaluate);
            }
        }

    }

    protected int getMinModules(final CourseGroup courseGroup, final ExecutionYear executionYear) {
        final DegreeModulesSelectionLimit degreeModulesSelectionLimit =
                courseGroup.getDegreeModulesSelectionLimitRule(executionYear);
        if (degreeModulesSelectionLimit != null) {
            return degreeModulesSelectionLimit.getMinimumLimit();
        }

        final CreditsLimit creditsLimit = courseGroup.getCreditsLimitRule(executionYear);
        if (creditsLimit != null) {
            final SortedSet<DegreeModule> sortedChilds =
                    new TreeSet<DegreeModule>(new DegreeModule.ComparatorByMinEcts(executionYear));
            sortedChilds.addAll(courseGroup.getChildDegreeModulesValidOnExecutionAggregation(executionYear));
            int counter = 0;
            double total = 0d;
            for (final DegreeModule degreeModule : sortedChilds) {
                total += degreeModule.getMinEctsCredits(executionYear);
                if (total > creditsLimit.getMinimumCredits()) {
                    break;
                } else {
                    counter++;
                }
            }

            return counter;
        }

        return courseGroup.getChildDegreeModulesValidOnExecutionAggregation(executionYear).size();
    }

    protected int getMaxModules(final CourseGroup courseGroup, final ExecutionYear executionYear) {
        final DegreeModulesSelectionLimit degreeModulesSelectionLimit =
                courseGroup.getDegreeModulesSelectionLimitRule(executionYear);
        if (degreeModulesSelectionLimit != null) {
            return degreeModulesSelectionLimit.getMaximumLimit();
        }

        final CreditsLimit creditsLimit = courseGroup.getCreditsLimitRule(executionYear);
        if (creditsLimit != null) {
            final SortedSet<DegreeModule> sortedChilds =
                    new TreeSet<DegreeModule>(new DegreeModule.ComparatorByMinEcts(executionYear));
            sortedChilds.addAll(courseGroup.getChildDegreeModulesValidOnExecutionAggregation(executionYear));
            int counter = 0;
            double total = 0d;
            for (final DegreeModule degreeModule : sortedChilds) {
                total += degreeModule.getMaxEctsCredits(executionYear);
                if (total > creditsLimit.getMaximumCredits()) {
                    break;
                } else {
                    counter++;
                }
            }

            return counter;

        }

        return courseGroup.getChildDegreeModulesValidOnExecutionAggregation(executionYear).size();
    }

    protected boolean isToCollectCurricularCourses(CourseGroup courseGroup, EnrolmentContext enrolmentContext,
            IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate) {
        return !isConcluded(courseGroup, enrolmentContext, sourceDegreeModuleToEvaluate)
                && !isExclusiveWithExisting(courseGroup, enrolmentContext)
                && !hasRuleBypassingPreviousYearsEnrolmentCurricularRule(courseGroup, enrolmentContext)
                && !isConcludedByModules(courseGroup, enrolmentContext)
                && !getSkipCollectCurricularCoursesPredicate().skip(courseGroup, enrolmentContext);
    }

    private boolean isConcludedByModules(CourseGroup courseGroup, EnrolmentContext enrolmentContext) {

        final CurriculumGroup curriculumGroup = enrolmentContext.getStudentCurricularPlan().findCurriculumGroupFor(courseGroup);

        if (curriculumGroup == null) {
            return false;
        }

        final DegreeModulesSelectionLimit degreeModulesSelectionLimit =
                (DegreeModulesSelectionLimit) curriculumGroup.getMostRecentActiveCurricularRule(
                        CurricularRuleType.DEGREE_MODULES_SELECTION_LIMIT, enrolmentContext.getExecutionYear());

        if (degreeModulesSelectionLimit == null) {
            return false;
        }

        final int minModulesToApprove = degreeModulesSelectionLimit.getMinimumLimit();
        final int approvedModules = curriculumGroup.getCurriculumModulesSet().stream()
                .filter(x -> x.isConcluded(enrolmentContext.getExecutionYear()).value()).collect(Collectors.toSet()).size();
        final int enroledModules = enrolmentContext.getDegreeModulesToEvaluate().stream()
                .filter(x -> x.isLeaf() && x.getCurriculumGroup() == curriculumGroup).collect(Collectors.toSet()).size();

        return approvedModules + enroledModules >= minModulesToApprove;

    }

    private boolean isExclusiveWithExisting(CourseGroup courseGroup, EnrolmentContext enrolmentContext) {
        for (final Exclusiveness exclusiveness : courseGroup.getExclusivenessRules(enrolmentContext.getExecutionPeriod())) {
            if (isEnroled(enrolmentContext, exclusiveness.getExclusiveDegreeModule())) {
                return true;
            }
        }

        return false;
    }

    static private boolean hasRuleBypassingPreviousYearsEnrolmentCurricularRule(final CourseGroup courseGroup,
            final EnrolmentContext enrolmentContext) {

        final List<CurricularRuleType> bypassing = Arrays.asList(new CurricularRuleType[] {

                CurricularRuleType.ENROLMENT_TO_BE_APPROVED_BY_COORDINATOR

        });

        for (final CurricularRule curricularRule : courseGroup.getCurricularRules(enrolmentContext.getExecutionPeriod())) {
            if (bypassing.contains(curricularRule.getCurricularRuleType())) {
                return true;
            }
        }

        return false;
    }

    private boolean isConcluded(final CourseGroup courseGroup, final EnrolmentContext enrolmentContext,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate) {
        final CurriculumGroup curriculumGroup = enrolmentContext.getStudentCurricularPlan().findCurriculumGroupFor(courseGroup);

        if (curriculumGroup == null) {
            return false;
        }

        final double minEctsToApprove = curriculumGroup.getDegreeModule().getMinEctsCredits(enrolmentContext.getExecutionYear());
        final double totalEcts = calculateTotalEctsInGroup(enrolmentContext, curriculumGroup);

        return totalEcts >= minEctsToApprove;
    }

    protected int getChildDegreeModulesCount(final CourseGroup courseGroup, final EnrolmentContext enrolmentContext) {
        int childDegreeModulesCount = 0;
        for (final Context context : courseGroup.getChildContextsSet()) {
            if (context.isOpen(enrolmentContext.getExecutionYear())) {
                childDegreeModulesCount++;
            }
        }
        return childDegreeModulesCount;
    }

    protected void collectCurricularCoursesToEnrol(final Map<Integer, Set<CurricularCourse>> result,
            final CourseGroup courseGroup, final EnrolmentContext enrolmentContext,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate) {

        final SortedSet<Context> sortedCurricularCoursesContexts =
                getChildCurricularCoursesContextsToEvaluate(courseGroup, enrolmentContext);

        removeApprovedOrEnrolledOrEnrollingOrNotSatifyingCurricularRules(sortedCurricularCoursesContexts, enrolmentContext,
                sourceDegreeModuleToEvaluate);

        addValidCurricularCourses(result, sortedCurricularCoursesContexts, courseGroup, enrolmentContext.getExecutionYear());
    }

    protected void removeApprovedOrEnrolledOrEnrollingOrNotSatifyingCurricularRules(
            final SortedSet<Context> sortedCurricularCoursesContexts, final EnrolmentContext enrolmentContext,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate) {

        final Iterator<Context> iterator = sortedCurricularCoursesContexts.iterator();

        while (iterator.hasNext()) {

            final Context context = iterator.next();
            final CurricularCourse curricularCourse = (CurricularCourse) context.getChildDegreeModule();

            if (isApproved(enrolmentContext, curricularCourse)) {
                iterator.remove();

            } else if (isEnroled(enrolmentContext, curricularCourse) || isEnrolling(enrolmentContext, curricularCourse)) {
                iterator.remove();

            } else if (!isCurricularRulesSatisfied(enrolmentContext, context, sourceDegreeModuleToEvaluate)) {
                iterator.remove();

            }
        }

    }

    private void addValidCurricularCourses(final Map<Integer, Set<CurricularCourse>> result,
            final Set<Context> curricularCoursesContexts, final CourseGroup courseGroup, final ExecutionYear executionYear) {
        for (final Context context : curricularCoursesContexts) {
            if (context.isValidForExecutionAggregation(executionYear)) {
                addCurricularCourse(result, context.getCurricularYear(), (CurricularCourse) context.getChildDegreeModule());
            }
        }
    }

    protected double calculateTotalEctsInGroup(final EnrolmentContext enrolmentContext, final CurriculumGroup curriculumGroup) {
        double result = curriculumGroup.getCreditsConcluded(enrolmentContext.getExecutionYear());
        result += curriculumGroup.getEnroledEctsCredits(enrolmentContext.getExecutionYear());

        return result;
    }

    private boolean isCurricularRulesSatisfied(EnrolmentContext enrolmentContext, Context context,
            IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate) {
        final CurricularCourse curricularCourse = (CurricularCourse) context.getChildDegreeModule();

        RuleResult result = RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());
        //Besides course unit rules we can only securely evaluate root curricum group rules, other group rules would require that we know the curriculum group 
        //from where the course unit was collected, otherwise we could wrongly evaluate rules, which is very dangerous if we are collecting course units from 
        //previous years and curriculum group is not enroled yet. A possible improvement, would be to find the corresponding curriculum groups, but branches 
        //and optional groups would require extra caution because two different groups might have the same course unit
        for (final ICurricularRule curricularRule : Stream
                .concat(curricularCourse.getCurricularRules(enrolmentContext.getExecutionPeriod()).stream(),
                        enrolmentContext.getStudentCurricularPlan().getRoot()
                                .getCurricularRules(enrolmentContext.getExecutionPeriod()).stream())
                .collect(Collectors.toSet())) {

            if (!curricularRule.appliesToContext(context)) {
                continue;
            }

            result = result.and(curricularRule.verify(getVerifyRuleLevel(enrolmentContext), enrolmentContext, curricularCourse,
                    (CourseGroup) sourceDegreeModuleToEvaluate.getDegreeModule()));
        }

        return result.isTrue();

    }

    private VerifyRuleLevel getVerifyRuleLevel(final EnrolmentContext enrolmentContext) {
        return enrolmentContext
                .getCurricularRuleLevel() == CurricularRuleLevel.ENROLMENT_WITH_RULES_AND_TEMPORARY_ENROLMENT ? VerifyRuleLevel.ENROLMENT_WITH_RULES_AND_TEMPORARY : VerifyRuleLevel.ENROLMENT_WITH_RULES;
    }

    protected SortedSet<Context> getChildCurricularCoursesContextsToEvaluate(final CourseGroup courseGroup,
            final EnrolmentContext enrolmentContext) {
        final ExecutionYear executionYear = enrolmentContext.getExecutionYear();

        final SortedSet<Context> result = new TreeSet<Context>(Context.COMPARATOR_BY_CURRICULAR_YEAR);

        final int minModules = getMinModules(courseGroup, executionYear);
        final int maxModules = getMaxModules(courseGroup, executionYear);
        final int childDegreeModulesCount = getChildDegreeModulesCount(courseGroup, enrolmentContext);

        if (minModules == maxModules) {
            if (maxModules == childDegreeModulesCount) {
                // N-N == Nchilds
                result.addAll(getActiveChildCurricularCourses(courseGroup, enrolmentContext));
            } else {
                // N-N <> Nchilds
                if (getSelectedChildDegreeModules(courseGroup, enrolmentContext).size() < minModules) {
                    result.addAll(getActiveChildCurricularCourses(courseGroup, enrolmentContext));
                } else {
                    result.addAll(getSelectedChildCurricularCoursesContexts(courseGroup, enrolmentContext));
                }
            }
        } else {
            // N-M
            if (getSelectedChildDegreeModules(courseGroup, enrolmentContext).size() < minModules) {
                result.addAll(getActiveChildCurricularCourses(courseGroup, enrolmentContext));
            } else {
                result.addAll(getSelectedChildCurricularCoursesContexts(courseGroup, enrolmentContext));
            }
        }

        return result;

    }

    private Set<Context> getActiveChildCurricularCourses(final CourseGroup courseGroup, final EnrolmentContext enrolmentContext) {
        final Set<Context> result = new HashSet<Context>();
        for (final Context context : courseGroup.getChildContextsSet()) {
            if (context.isOpen(enrolmentContext.getExecutionYear()) && context.getChildDegreeModule().isCurricularCourse()) {
                result.add(context);
            }
        }
        return result;
    }

    private Set<Context> getSelectedChildCurricularCoursesContexts(final CourseGroup courseGroup,
            final EnrolmentContext enrolmentContext) {
        final Set<Context> result = new HashSet<Context>();

        for (final Context context : courseGroup.getChildContextsSet()) {
            if (context.isOpen(enrolmentContext.getExecutionYear()) && context.getChildDegreeModule().isCurricularCourse()) {
                final CurricularCourse curricularCourse = (CurricularCourse) context.getChildDegreeModule();
                final StudentCurricularPlan studentCurricularPlan = enrolmentContext.getStudentCurricularPlan();
                if (studentCurricularPlan.isApproved(curricularCourse)) {
                    final CurriculumLine approvedLine = studentCurricularPlan.getApprovedCurriculumLine(curricularCourse);

                    if (approvedLine.getCurriculumGroup().getDegreeModule() != null
                            && approvedLine.getCurriculumGroup().getDegreeModule() == courseGroup) {
                        result.add(context);
                    }
                }
            }
        }

        for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : enrolmentContext.getDegreeModulesToEvaluate()) {
            if (degreeModuleToEvaluate.isLeaf() && degreeModuleToEvaluate.getCurriculumGroup() != null
                    && degreeModuleToEvaluate.getCurriculumGroup().getDegreeModule() == courseGroup) {

                if (degreeModuleToEvaluate.isAnnualCurricularCourse(enrolmentContext.getExecutionYear())
                        && degreeModuleToEvaluate.getContext() == null) {
                    continue;
                }

                final Context context = degreeModuleToEvaluate.getContext();
                if (context == null) {
                    throw new DomainException("error.degreeModuleToEvaluate.has.invalid.context",
                            degreeModuleToEvaluate.getName(), degreeModuleToEvaluate.getExecutionInterval().getQualifiedName());
                }
                result.add(context);
            }
        }
        return result;
    }

    private void addCurricularCourse(Map<Integer, Set<CurricularCourse>> result, Integer curricularYear,
            CurricularCourse curricularCourse) {
        Set<CurricularCourse> curricularCourses = result.get(curricularYear);

        if (curricularCourses == null) {
            curricularCourses = new HashSet<CurricularCourse>();
            result.put(curricularYear, curricularCourses);
        }

        curricularCourses.add(curricularCourse);

    }

    protected RuleResult createFalseRuleResult(final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate,
            final IDegreeModuleToEvaluate degreeModuleToEvaluate) {
        return RuleResult.createFalse(degreeModuleToEvaluate.getDegreeModule(),
                "curricularRules.ruleExecutors.PreviousYearsEnrolmentByYearExecutor", sourceDegreeModuleToEvaluate.getName(),
                degreeModuleToEvaluate.getContext().getCurricularYear().toString());
    }

    protected RuleResult createImpossibleRuleResult(final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate,
            final IDegreeModuleToEvaluate degreeModuleToEvaluate) {
        return RuleResult.createImpossible(degreeModuleToEvaluate.getDegreeModule(),
                "curricularRules.ruleExecutors.PreviousYearsEnrolmentByYearExecutor", sourceDegreeModuleToEvaluate.getName(),
                degreeModuleToEvaluate.getContext().getCurricularYear().toString());
    }

    @Override
    protected boolean canBeEvaluated(ICurricularRule curricularRule, IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate,
            EnrolmentContext enrolmentContext) {
        return true;
    }
}
