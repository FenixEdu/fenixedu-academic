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
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.curricularPeriod.CurricularPeriod;
import org.fenixedu.academic.domain.curricularRules.CreditsLimit;
import org.fenixedu.academic.domain.curricularRules.CurricularRule;
import org.fenixedu.academic.domain.curricularRules.CurricularRuleType;
import org.fenixedu.academic.domain.curricularRules.DegreeModulesSelectionLimit;
import org.fenixedu.academic.domain.curricularRules.Exclusiveness;
import org.fenixedu.academic.domain.curricularRules.ICurricularRule;
import org.fenixedu.academic.domain.curricularRules.PreviousYearsEnrolmentCurricularRule;
import org.fenixedu.academic.domain.curricularRules.executors.RuleResult;
import org.fenixedu.academic.domain.curricularRules.executors.ruleExecutors.PreviousYearsEnrolmentByYearExecutor.SkipCollectCurricularCoursesPredicate;
import org.fenixedu.academic.domain.curricularRules.executors.verifyExecutors.VerifyRuleLevel;
import org.fenixedu.academic.domain.degreeStructure.Context;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.domain.enrolment.EnroledCurriculumModuleWrapper;
import org.fenixedu.academic.domain.enrolment.EnrolmentContext;
import org.fenixedu.academic.domain.enrolment.IDegreeModuleToEvaluate;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.curriculum.ICurriculumEntry;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.CycleCurriculumGroup;

public class PreviousYearsEnrolmentBySemesterExecutor extends CurricularRuleExecutor {

    @Override
    protected RuleResult executeEnrolmentVerificationWithRules(final ICurricularRule curricularRule,
            IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {

        if (isEnrollingInCourseGroupsOnly(enrolmentContext, sourceDegreeModuleToEvaluate)) {
            return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
        }

        final PreviousYearsEnrolmentCurricularRule previousYearsEnrolmentCurricularRule =
                (PreviousYearsEnrolmentCurricularRule) curricularRule;
        final Map<Integer, Set<CurricularCourse>> curricularCoursesToEnrolByYear = getCurricularCoursesToEnrolByYear(
                previousYearsEnrolmentCurricularRule, enrolmentContext, sourceDegreeModuleToEvaluate, false);

//        printCurricularCoursesToEnrol(curricularCoursesToEnrolByYear);

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

    private void printCurricularCoursesToEnrol(Map<Integer, Set<CurricularCourse>> curricularCoursesToEnrolByYear) {
        for (final Entry<Integer, Set<CurricularCourse>> entry : curricularCoursesToEnrolByYear.entrySet()) {
            System.out.println("Year " + entry.getKey());
            for (final CurricularCourse curricularCourse : entry.getValue()) {
                System.out.println(curricularCourse.getName());
            }

            System.out.println("-------------");
        }

    }

    private boolean hasCurricularCoursesToEnrolInPreviousYears(Map<Integer, Set<CurricularCourse>> curricularCoursesToEnrolByYear,
            Integer curricularYear) {
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

    private RuleResult hasAnyCurricularCoursesToEnrolInPreviousYears(final EnrolmentContext enrolmentContext,
            final Map<Integer, Set<CurricularCourse>> curricularCoursesToEnrolByYear,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate) {

        RuleResult result = RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());
        for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : enrolmentContext
                .getAllChildDegreeModulesToEvaluateFor(sourceDegreeModuleToEvaluate.getDegreeModule())) {
            if (degreeModuleToEvaluate.isLeaf()) {

                if (degreeModuleToEvaluate.isAnnualCurricularCourse(enrolmentContext.getExecutionPeriod().getExecutionYear())) {

                    if (degreeModuleToEvaluate.getContext() == null) {
                        continue;
                    }

                    if (degreeModuleToEvaluate.isEnroled()) {
                        final ICurriculumEntry curriculumEntry =
                                (ICurriculumEntry) ((EnroledCurriculumModuleWrapper) degreeModuleToEvaluate)
                                        .getCurriculumModule();
                        if (curriculumEntry.getExecutionInterval() != enrolmentContext.getExecutionPeriod()) {
                            continue;
                        }
                    }
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

    private RuleResult createFalseRuleResult(final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate,
            final IDegreeModuleToEvaluate degreeModuleToEvaluate) {
        return RuleResult.createFalse(degreeModuleToEvaluate.getDegreeModule(),
                "curricularRules.ruleExecutors.PreviousYearsEnrolmentBySemesterExecutor", sourceDegreeModuleToEvaluate.getName(),
                degreeModuleToEvaluate.getContext().getCurricularYear().toString());
    }

    private RuleResult createImpossibleRuleResult(final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate,
            final IDegreeModuleToEvaluate degreeModuleToEvaluate) {
        return RuleResult.createImpossible(degreeModuleToEvaluate.getDegreeModule(),
                "curricularRules.ruleExecutors.PreviousYearsEnrolmentBySemesterExecutor", sourceDegreeModuleToEvaluate.getName(),
                degreeModuleToEvaluate.getContext().getCurricularYear().toString());
    }

    private Map<Integer, Set<CurricularCourse>> getCurricularCoursesToEnrolByYear(
            final PreviousYearsEnrolmentCurricularRule previousYearsEnrolmentCurricularRule,
            final EnrolmentContext enrolmentContext, final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate,
            final boolean withTemporaryEnrolments) {
        final Map<Integer, Set<CurricularCourse>> result = new HashMap<Integer, Set<CurricularCourse>>();

        for (CourseGroup courseGroup : getCourseGroupsToEvaluate(
                previousYearsEnrolmentCurricularRule.getDegreeModuleToApplyRule(), enrolmentContext)) {
            collectCourseGroupCurricularCoursesToEnrol(result, courseGroup, enrolmentContext, sourceDegreeModuleToEvaluate,
                    withTemporaryEnrolments);
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

    private void collectCourseGroupCurricularCoursesToEnrol(final Map<Integer, Set<CurricularCourse>> result,
            final CourseGroup courseGroup, final EnrolmentContext enrolmentContext,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, boolean withTemporaryEnrolments) {

        if (!isToCollectCurricularCourses(courseGroup, enrolmentContext, sourceDegreeModuleToEvaluate, withTemporaryEnrolments)) {
            return;
        }

        final int childDegreeModulesCount =
                courseGroup.getActiveChildContextsWithMax(enrolmentContext.getExecutionPeriod()).size();

        collectCurricularCoursesToEnrol(result, courseGroup, enrolmentContext, sourceDegreeModuleToEvaluate,
                withTemporaryEnrolments);

        final int minModules = getMinModules(courseGroup, enrolmentContext.getExecutionPeriod());
        final int maxModules = getMaxModules(courseGroup, enrolmentContext.getExecutionPeriod());
        if (minModules == maxModules) {
            if (maxModules == childDegreeModulesCount) {
                // N-N == Nchilds
                collectChildCourseGroupsCurricularCoursesToEnrol(result, courseGroup, enrolmentContext,
                        sourceDegreeModuleToEvaluate, withTemporaryEnrolments);
            } else {
                // N-N <> Nchilds
                if (getSelectedChildDegreeModules(courseGroup, enrolmentContext).size() < minModules) {
                    collectChildCourseGroupsCurricularCoursesToEnrol(result, courseGroup, enrolmentContext,
                            sourceDegreeModuleToEvaluate, withTemporaryEnrolments);
                } else {
                    collectSelectedChildCourseGroupsCurricularCoursesToEnrol(result, courseGroup, enrolmentContext,
                            sourceDegreeModuleToEvaluate, withTemporaryEnrolments);
                }
            }
        } else {
            // N-M
            if (getSelectedChildDegreeModules(courseGroup, enrolmentContext).size() < minModules) {
                collectChildCourseGroupsCurricularCoursesToEnrol(result, courseGroup, enrolmentContext,
                        sourceDegreeModuleToEvaluate, withTemporaryEnrolments);
            } else {
                collectSelectedChildCourseGroupsCurricularCoursesToEnrol(result, courseGroup, enrolmentContext,
                        sourceDegreeModuleToEvaluate, withTemporaryEnrolments);
            }
        }

    }

    private boolean isToCollectCurricularCourses(CourseGroup courseGroup, EnrolmentContext enrolmentContext,
            IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, boolean withTemporaryEnrolments) {
        return !isConcluded(courseGroup, enrolmentContext, sourceDegreeModuleToEvaluate, withTemporaryEnrolments)
                && !isExclusiveWithExisting(courseGroup, enrolmentContext)
                && !hasRuleBypassingPreviousYearsEnrolmentCurricularRule(courseGroup, enrolmentContext)
                && !getSkipCollectCurricularCoursesPredicate().skip(courseGroup, enrolmentContext);
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
            System.out.println("Could not set SKIP_COLLECT_CURRICULAR_COURSES_PREDICATE to null");
        }
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
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final boolean withTemporaryEnrolments) {
        final CurriculumGroup curriculumGroup = enrolmentContext.getStudentCurricularPlan().findCurriculumGroupFor(courseGroup);

        if (curriculumGroup == null) {
            return false;
        }

        final double minEctsToApprove = curriculumGroup.getDegreeModule().getMinEctsCredits();
        final double totalEcts = calculateTotalEctsInGroup(enrolmentContext, curriculumGroup, withTemporaryEnrolments);

        return totalEcts >= minEctsToApprove;
    }

    private void collectSelectedChildCourseGroupsCurricularCoursesToEnrol(Map<Integer, Set<CurricularCourse>> result,
            CourseGroup courseGroup, EnrolmentContext enrolmentContext, IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate,
            boolean withTemporaryEnrolments) {

        for (final DegreeModule degreeModule : getSelectedChildDegreeModules(courseGroup, enrolmentContext)) {
            if (degreeModule.isCourseGroup()) {
                collectCourseGroupCurricularCoursesToEnrol(result, (CourseGroup) degreeModule, enrolmentContext,
                        sourceDegreeModuleToEvaluate, withTemporaryEnrolments);
            }
        }

    }

    private Set<DegreeModule> getSelectedChildDegreeModules(final CourseGroup courseGroup,
            final EnrolmentContext enrolmentContext) {
        final Set<DegreeModule> result = new HashSet<DegreeModule>();

        for (final DegreeModule degreeModule : courseGroup.getChildDegreeModulesValidOn(enrolmentContext.getExecutionPeriod())) {
            if (enrolmentContext.getStudentCurricularPlan().hasDegreeModule(degreeModule)) {
                result.add(degreeModule);
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

    private int getMinModules(final CourseGroup courseGroup, final ExecutionInterval executionInterval) {
        final DegreeModulesSelectionLimit degreeModulesSelectionLimit =
                courseGroup.getDegreeModulesSelectionLimitRule(executionInterval);
        if (degreeModulesSelectionLimit != null) {
            return degreeModulesSelectionLimit.getMinimumLimit();
        }

        final CreditsLimit creditsLimit = courseGroup.getCreditsLimitRule(executionInterval);
        if (creditsLimit != null) {
            final SortedSet<DegreeModule> sortedChilds =
                    new TreeSet<DegreeModule>(new DegreeModule.ComparatorByMinEcts(executionInterval));
            sortedChilds.addAll(courseGroup.getChildDegreeModulesValidOn(executionInterval));
            int counter = 0;
            double total = 0d;
            for (final DegreeModule degreeModule : sortedChilds) {
                total += degreeModule.getMinEctsCredits();
                if (total > creditsLimit.getMinimumCredits()) {
                    break;
                } else {
                    counter++;
                }
            }

            return counter;
        }

        return courseGroup.getChildDegreeModulesValidOn(executionInterval).size();
    }

    private int getMaxModules(final CourseGroup courseGroup, final ExecutionInterval executionInterval) {
        final DegreeModulesSelectionLimit degreeModulesSelectionLimit =
                courseGroup.getDegreeModulesSelectionLimitRule(executionInterval);
        if (degreeModulesSelectionLimit != null) {
            return degreeModulesSelectionLimit.getMaximumLimit();
        }

        final CreditsLimit creditsLimit = courseGroup.getCreditsLimitRule(executionInterval);
        if (creditsLimit != null) {
            final SortedSet<DegreeModule> sortedChilds =
                    new TreeSet<DegreeModule>(new DegreeModule.ComparatorByMinEcts(executionInterval));
            sortedChilds.addAll(courseGroup.getChildDegreeModulesValidOn(executionInterval));
            int counter = 0;
            double total = 0d;
            for (final DegreeModule degreeModule : sortedChilds) {
                total += degreeModule.getMaxEctsCredits();
                if (total > creditsLimit.getMaximumCredits()) {
                    break;
                } else {
                    counter++;
                }
            }

            return counter;

        }

        return courseGroup.getChildDegreeModulesValidOn(executionInterval).size();
    }

    private void collectCurricularCoursesToEnrol(final Map<Integer, Set<CurricularCourse>> result, final CourseGroup courseGroup,
            final EnrolmentContext enrolmentContext, final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate,
            final boolean withTemporaryEnrolments) {

        final CurriculumGroup curriculumGroup = enrolmentContext.getStudentCurricularPlan().findCurriculumGroupFor(courseGroup);

        final double missingEctsToConcludeGroup;
        if (curriculumGroup != null) {
            missingEctsToConcludeGroup = curriculumGroup.getDegreeModule().getMinEctsCredits()
                    - calculateTotalEctsInGroup(enrolmentContext, curriculumGroup, withTemporaryEnrolments)
                    - calculateEnrollingEctsCreditsInCurricularCoursesFor(enrolmentContext, courseGroup);
        } else {
            missingEctsToConcludeGroup = courseGroup.getMinEctsCredits();
        }

        // we need curricular courses in all periods, otherwise enrolment with
        // exclusiviness in next semester will
        // not be enforced
        // e.g. B required A. If boot are in alternative semesters, that means
        // we should force enrolment in A in this
        // semester, otherwise the group will not close in next semester
        final Map<CurricularPeriod, Set<Context>> childContextsWithMaxByCurricularPeriod =
                courseGroup.getActiveChildCurricularContextsWithMaxByCurricularPeriod(enrolmentContext.getExecutionPeriod());

        final SortedSet<Context> sortedCurricularCoursesContexts =
                getChildCurricularCoursesContextsToEvaluate(courseGroup, enrolmentContext);

        removeApprovedOrEnrolledOrEnrollingOrNotSatifyingCurricularRules(sortedCurricularCoursesContexts,
                childContextsWithMaxByCurricularPeriod, enrolmentContext, sourceDegreeModuleToEvaluate, withTemporaryEnrolments);

        removeCurricularCoursesThatCanBeApprovedInOtherCurricularPeriod(missingEctsToConcludeGroup,
                childContextsWithMaxByCurricularPeriod, sortedCurricularCoursesContexts, enrolmentContext);

        addValidCurricularCourses(result, sortedCurricularCoursesContexts, courseGroup, enrolmentContext.getExecutionPeriod());
    }

    private void removeCurricularCoursesThatCanBeApprovedInOtherCurricularPeriod(final double missingEctsToConcludeGroup,
            final Map<CurricularPeriod, Set<Context>> childContextsByCurricularPeriod,
            final SortedSet<Context> sortedCurricularCoursesContexts, final EnrolmentContext enrolmentContext) {

        for (final Entry<CurricularPeriod, Set<Context>> each : childContextsByCurricularPeriod.entrySet()) {
            for (final Context context : each.getValue()) {
                if (canObtainApprovalInOtherCurricularPeriod(missingEctsToConcludeGroup, context,
                        childContextsByCurricularPeriod)) {
                    sortedCurricularCoursesContexts.remove(context);
                }
            }
        }

    }

    private void removeApprovedOrEnrolledOrEnrollingOrNotSatifyingCurricularRules(
            final SortedSet<Context> sortedCurricularCoursesContexts,
            final Map<CurricularPeriod, Set<Context>> childContextsWithMaxByCurricularPeriod,
            final EnrolmentContext enrolmentContext, final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate,
            final boolean withTemporaryEnrolments) {
        final Iterator<Context> iterator = sortedCurricularCoursesContexts.iterator();
        while (iterator.hasNext()) {
            final Context context = iterator.next();
            final CurricularCourse curricularCourse = (CurricularCourse) context.getChildDegreeModule();
            if (isApproved(enrolmentContext, curricularCourse)) {
                iterator.remove();
                removeFromChildContextsByCurricularPeriod(childContextsWithMaxByCurricularPeriod, context);

            } else if (isEnroled(enrolmentContext, curricularCourse, withTemporaryEnrolments)
                    || isEnrolling(enrolmentContext, curricularCourse)) {
                iterator.remove();
                removeFromChildContextsByCurricularPeriod(childContextsWithMaxByCurricularPeriod, context);
            } else if (!isCurricularRulesSatisfied(enrolmentContext, curricularCourse, sourceDegreeModuleToEvaluate)) {
                iterator.remove();
                removeFromChildContextsByCurricularPeriod(childContextsWithMaxByCurricularPeriod, context);
            }
        }

    }

    private void removeFromChildContextsByCurricularPeriod(final Map<CurricularPeriod, Set<Context>> result,
            final Context contextToRemove) {
        final DegreeModule degreeModule = contextToRemove.getChildDegreeModule();

        for (final Entry<CurricularPeriod, Set<Context>> each : result.entrySet()) {
            final Iterator<Context> iterator = each.getValue().iterator();
            while (iterator.hasNext()) {
                Context candidate = iterator.next();
                if (candidate.getChildDegreeModule() == degreeModule) {
                    iterator.remove();
                }
            }
        }

    }

    private double calculateEnrollingEctsCreditsInCurricularCoursesFor(final EnrolmentContext enrolmentContext,
            final CourseGroup courseGroup) {
        double result = 0;

        for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : enrolmentContext.getDegreeModulesToEvaluate()) {
            if (degreeModuleToEvaluate.isLeaf() && degreeModuleToEvaluate.isEnroling()
                    && degreeModuleToEvaluate.getCurriculumGroup().getDegreeModule() == courseGroup) {
                result += degreeModuleToEvaluate.getDegreeModule().getMinEctsCredits();
            }
        }

        return result;

    }

    private boolean canObtainApprovalInOtherCurricularPeriod(final double missingEctsToConcludeGroup,
            final Context curricularCourseContext, final Map<CurricularPeriod, Set<Context>> contextsByCurricularPeriod) {

        final int curricularCourseSemester = curricularCourseContext.getCurricularPeriod().getChildOrder();
        double ectsToPerformInOtherCurricularPeriods = 0;

        for (final Entry<CurricularPeriod, Set<Context>> each : contextsByCurricularPeriod.entrySet()) {
            if (each.getKey().getChildOrder() == curricularCourseSemester) {
                continue;
            }

            for (final Context context : each.getValue()) {
                final CurricularCourse curricularCourse = (CurricularCourse) context.getChildDegreeModule();
                // final String name = curricularCourse.getName();
                ectsToPerformInOtherCurricularPeriods += curricularCourse.getMinEctsCredits();
            }
        }

        return ectsToPerformInOtherCurricularPeriods >= missingEctsToConcludeGroup;

    }

    private double calculateTotalEctsInGroup(final EnrolmentContext enrolmentContext, final CurriculumGroup curriculumGroup,
            final boolean withTemporaryEnrolments) {
        double result = curriculumGroup.getCreditsConcluded(enrolmentContext.getExecutionPeriod().getExecutionYear());
        result += curriculumGroup.getEnroledEctsCredits(enrolmentContext.getExecutionPeriod());

        if (withTemporaryEnrolments) {
            result += curriculumGroup.getEnroledEctsCredits(enrolmentContext.getExecutionPeriod().getPrevious());
        }

        return result;
    }

    private boolean isEnroled(final EnrolmentContext enrolmentContext, final CurricularCourse curricularCourse,
            final boolean withTemporaryEnrolments) {
        if (withTemporaryEnrolments) {
            return isEnroled(enrolmentContext, curricularCourse, enrolmentContext.getExecutionPeriod())
                    || isEnroled(enrolmentContext, curricularCourse, enrolmentContext.getExecutionPeriod().getPrevious());
        }

        return isEnroled(enrolmentContext, curricularCourse, enrolmentContext.getExecutionPeriod());

    }

    private boolean isCurricularRulesSatisfied(EnrolmentContext enrolmentContext, CurricularCourse curricularCourse,
            IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate) {
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
            result = result.and(curricularRule.verify(getVerifyRuleLevel(enrolmentContext), enrolmentContext, curricularCourse,
                    (CourseGroup) sourceDegreeModuleToEvaluate.getDegreeModule()));
        }

        return result.isTrue();

    }

    private VerifyRuleLevel getVerifyRuleLevel(final EnrolmentContext enrolmentContext) {
        return enrolmentContext
                .getCurricularRuleLevel() == CurricularRuleLevel.ENROLMENT_WITH_RULES_AND_TEMPORARY_ENROLMENT ? VerifyRuleLevel.ENROLMENT_WITH_RULES_AND_TEMPORARY : VerifyRuleLevel.ENROLMENT_WITH_RULES;
    }

    private SortedSet<Context> getChildCurricularCoursesContextsToEvaluate(final CourseGroup courseGroup,
            final EnrolmentContext enrolmentContext) {
        final ExecutionInterval executionSemester = enrolmentContext.getExecutionPeriod();

        final SortedSet<Context> result = new TreeSet<Context>(Context.COMPARATOR_BY_CURRICULAR_YEAR);

        final int minModules = getMinModules(courseGroup, executionSemester);
        final int maxModules = getMaxModules(courseGroup, executionSemester);
        final int childDegreeModulesCount =
                courseGroup.getActiveChildContextsWithMax(enrolmentContext.getExecutionPeriod()).size();

        if (minModules == maxModules) {
            if (maxModules == childDegreeModulesCount) {
                // N-N == Nchilds
                result.addAll(courseGroup.getActiveChildContextsWithMaxCurricularPeriodForCurricularCourses(
                        enrolmentContext.getExecutionPeriod()));
            } else {
                // N-N <> Nchilds
                if (getSelectedChildDegreeModules(courseGroup, enrolmentContext).size() < minModules) {
                    result.addAll(courseGroup.getActiveChildContextsWithMaxCurricularPeriodForCurricularCourses(
                            enrolmentContext.getExecutionPeriod()));
                } else {
                    result.addAll(getSelectedChildCurricularCoursesContexts(courseGroup, enrolmentContext));
                }
            }
        } else {
            // N-M
            if (getSelectedChildDegreeModules(courseGroup, enrolmentContext).size() < minModules) {
                result.addAll(courseGroup.getActiveChildContextsWithMaxCurricularPeriodForCurricularCourses(
                        enrolmentContext.getExecutionPeriod()));
            } else {
                result.addAll(getSelectedChildCurricularCoursesContexts(courseGroup, enrolmentContext));
            }
        }

        return result;

    }

    private Set<Context> getSelectedChildCurricularCoursesContexts(final CourseGroup courseGroup,
            final EnrolmentContext enrolmentContext) {
        final Set<Context> result = new HashSet<Context>();

        for (final Context context : courseGroup.getActiveChildContexts()) {
            if (context.getChildDegreeModule().isCurricularCourse()
                    && enrolmentContext.getStudentCurricularPlan().hasDegreeModule(context.getChildDegreeModule())) {
                result.add(context);
            }
        }

        for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : enrolmentContext.getDegreeModulesToEvaluate()) {
            if (degreeModuleToEvaluate.isLeaf() && degreeModuleToEvaluate.getCurriculumGroup() != null
                    && degreeModuleToEvaluate.getCurriculumGroup().getDegreeModule() == courseGroup) {

                if (degreeModuleToEvaluate.isAnnualCurricularCourse(enrolmentContext.getExecutionPeriod().getExecutionYear())
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

    private void addValidCurricularCourses(final Map<Integer, Set<CurricularCourse>> result,
            final Set<Context> curricularCoursesContexts, final CourseGroup courseGroup,
            final ExecutionInterval executionInterval) {
        for (final Context context : curricularCoursesContexts) {
            if (context.isValid(executionInterval)) {
                addCurricularCourse(result, context.getCurricularYear(), (CurricularCourse) context.getChildDegreeModule());
            }
        }
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

    private void collectChildCourseGroupsCurricularCoursesToEnrol(final Map<Integer, Set<CurricularCourse>> result,
            final CourseGroup courseGroup, final EnrolmentContext enrolmentContext,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final boolean withTemporaryEnrolments) {
        for (final DegreeModule childDegreeModule : courseGroup
                .getChildDegreeModulesValidOn(enrolmentContext.getExecutionPeriod())) {
            if (childDegreeModule.isCourseGroup()) {
                collectCourseGroupCurricularCoursesToEnrol(result, (CourseGroup) childDegreeModule, enrolmentContext,
                        sourceDegreeModuleToEvaluate, withTemporaryEnrolments);
            }
        }
    }

    @Override
    protected boolean canBeEvaluated(ICurricularRule curricularRule, IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate,
            EnrolmentContext enrolmentContext) {
        return true;
    }
}
