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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.curricularRules.executors.RuleResult;
import org.fenixedu.academic.domain.degreeStructure.Context;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.domain.enrolment.EnroledCurriculumModuleWrapper;
import org.fenixedu.academic.domain.enrolment.EnrolmentContext;
import org.fenixedu.academic.domain.enrolment.IDegreeModuleToEvaluate;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.curriculum.ICurriculumEntry;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumGroup;

public class PreviousYearsEnrolmentBySemesterExecutor extends PreviousYearsEnrolmentByYearExecutor {

    @Override
    protected RuleResult hasAnyCurricularCoursesToEnrolInPreviousYears(final EnrolmentContext enrolmentContext,
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

                    //This test prevents annual courses enroled in first period to block enrolments in other periods
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

    @Override
    protected void collectCurricularCoursesToEnrol(final Map<Integer, Set<CurricularCourse>> result,
            final CourseGroup courseGroup, final EnrolmentContext enrolmentContext,
            final IDegreeModuleToEvaluate sourceToEvaluate) {

        final SortedSet<Context> contexts = getChildCurricularCoursesContextsToEvaluate(courseGroup, enrolmentContext);

        removeApprovedOrEnrolledOrEnrollingOrNotSatifyingCurricularRules(contexts, enrolmentContext, sourceToEvaluate);

        removeAllThatCanBeApprovedInOtherPeriod(contexts, courseGroup, enrolmentContext);

        addValidCurricularCourses(result, contexts, courseGroup, enrolmentContext.getExecutionPeriod());
    }

    private void removeAllThatCanBeApprovedInOtherPeriod(final SortedSet<Context> contexts, CourseGroup courseGroup,
            final EnrolmentContext enrolmentContext) {

        final CurriculumGroup curriculumGroup = enrolmentContext.getStudentCurricularPlan().findCurriculumGroupFor(courseGroup);

        final double missingEctsToConcludeGroup;
        if (curriculumGroup != null) {
            missingEctsToConcludeGroup = curriculumGroup.getDegreeModule().getMinEctsCredits(enrolmentContext.getExecutionYear())
                    - calculateTotalEctsInGroup(enrolmentContext, curriculumGroup)
                    - calculateEnrollingEctsCreditsInCurricularCoursesFor(enrolmentContext, courseGroup);
        } else {
            missingEctsToConcludeGroup = courseGroup.getMinEctsCredits(enrolmentContext.getExecutionYear());
        }

        final Iterator<Context> iterator = contexts.iterator();
        while (iterator.hasNext()) {
            final Context context = iterator.next();
            if (canObtainApprovalInOtherCurricularPeriod(missingEctsToConcludeGroup, contexts, context, enrolmentContext)) {
                iterator.remove();
            }
        }
    }

    //TODO: rewrite with enrolment context to unify code with PreviousYearsEnrolmentByYearExecutor
    @Override
    protected double calculateTotalEctsInGroup(final EnrolmentContext enrolmentContext, final CurriculumGroup curriculumGroup) {
        double result = curriculumGroup.getCreditsConcluded(enrolmentContext.getExecutionPeriod().getExecutionYear());
        result += curriculumGroup.getEnroledEctsCredits(enrolmentContext.getExecutionPeriod());

        return result;
    }

    private double calculateEnrollingEctsCreditsInCurricularCoursesFor(final EnrolmentContext enrolmentContext,
            final CourseGroup courseGroup) {
        double result = 0;

        for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : enrolmentContext.getDegreeModulesToEvaluate()) {
            if (degreeModuleToEvaluate.isLeaf() && degreeModuleToEvaluate.isEnroling()
                    && degreeModuleToEvaluate.getCurriculumGroup().getDegreeModule() == courseGroup) {
                result += degreeModuleToEvaluate.getDegreeModule().getMinEctsCredits(enrolmentContext.getExecutionYear());
            }
        }

        return result;

    }

    private boolean canObtainApprovalInOtherCurricularPeriod(final double missingEctsToConcludeGroup,
            final SortedSet<Context> contexts, final Context curricularCourseContext, EnrolmentContext enrolmentContext) {
        double ectsToApproveInOtherPeriods = 0;
        final Set<CurricularCourse> uniqueOtherPeriodCourses = contexts.stream().filter(
                ctx -> ctx.getCurricularPeriod().getChildOrder() > curricularCourseContext.getCurricularPeriod().getChildOrder())
                .map(ctx -> (CurricularCourse) ctx.getChildDegreeModule()).collect(Collectors.toSet());

        for (final CurricularCourse curricularCourse : uniqueOtherPeriodCourses) {
            ectsToApproveInOtherPeriods += curricularCourse.getMinEctsCredits(enrolmentContext.getExecutionYear());
        }

        return ectsToApproveInOtherPeriods >= missingEctsToConcludeGroup;
    }

    private void addValidCurricularCourses(final Map<Integer, Set<CurricularCourse>> result, final Set<Context> contexts,
            final CourseGroup courseGroup, final ExecutionInterval executionInterval) {
        for (final Context context : contexts) {
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

}
