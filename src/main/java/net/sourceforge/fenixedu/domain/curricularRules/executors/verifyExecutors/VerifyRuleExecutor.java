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
package net.sourceforge.fenixedu.domain.curricularRules.executors.verifyExecutors;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.executors.RuleResult;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;

abstract public class VerifyRuleExecutor {

    private static class NullVerifyExecutor extends VerifyRuleExecutor {

        private NullVerifyExecutor() {
        }

        @Override
        protected RuleResult verifyEnrolmentWithRules(ICurricularRule curricularRule, EnrolmentContext enrolmentContext,
                DegreeModule degreeModuleToVerify, CourseGroup parentCourseGroup) {
            return RuleResult.createNA(degreeModuleToVerify);
        }

        @Override
        protected RuleResult verifyEnrolmentWithTemporaryEnrolment(ICurricularRule curricularRule,
                EnrolmentContext enrolmentContext, DegreeModule degreeModuleToVerify, CourseGroup parentCourseGroup) {
            return RuleResult.createNA(degreeModuleToVerify);
        }
    }

    public static final VerifyRuleExecutor NULL_VERIFY_EXECUTOR = new NullVerifyExecutor();

    final public RuleResult verify(ICurricularRule curricularRule, final VerifyRuleLevel verifyRuleLevel,
            final EnrolmentContext enrolmentContext, final DegreeModule degreeModuleToVerify,
            final CourseGroup rootOrCycleCurriculumGroup) {

        if (!rootOrCycleCurriculumGroup.isCycleCourseGroup() && !rootOrCycleCurriculumGroup.isRoot()) {
            throw new DomainException(
                    "error.net.sourceforge.fenixedu.domain.curricularRules.executors.verifyExecutors.VerifyRuleExecutor.parent.course.group.should.be.root.or.cycle.course.group");
        }

        if (verifyRuleLevel == VerifyRuleLevel.ENROLMENT_WITH_RULES) {
            return verifyEnrolmentWithRules(curricularRule, enrolmentContext, degreeModuleToVerify, rootOrCycleCurriculumGroup);
        } else if (verifyRuleLevel == VerifyRuleLevel.ENROLMENT_WITH_RULES_AND_TEMPORARY) {
            return verifyEnrolmentWithTemporaryEnrolment(curricularRule, enrolmentContext, degreeModuleToVerify,
                    rootOrCycleCurriculumGroup);
        } else if (verifyRuleLevel == VerifyRuleLevel.DEGREE_CONCLUSION_WITH_RULES) {
            return verifyDegreeConclusionWithRules(curricularRule, enrolmentContext, degreeModuleToVerify,
                    rootOrCycleCurriculumGroup);
        } else {
            throw new DomainException(
                    "error.net.sourceforge.fenixedu.domain.curricularRules.executors.verifyExecutors.VerifyRuleExecutor.invalid.verify.level");
        }
    }

    protected boolean isApproved(final EnrolmentContext enrolmentContext, final CurricularCourse curricularCourse,
            final CourseGroup parentCourseGroup) {
        final CurriculumGroup curriculumGroup =
                enrolmentContext.getStudentCurricularPlan().findCurriculumGroupFor(parentCourseGroup);
        return curriculumGroup != null ? curriculumGroup.isApproved(curricularCourse) : false;
    }

    protected boolean isEnrolledIn(final EnrolmentContext enrolmentContext, final CourseGroup courseGroup) {
        return enrolmentContext.getStudentCurricularPlan().findCurriculumGroupFor(courseGroup) != null;
    }

    protected boolean hasEnrolmentWithEnroledState(final EnrolmentContext enrolmentContext,
            final CurricularCourse curricularCourse, final ExecutionSemester executionSemester) {
        return enrolmentContext.getStudentCurricularPlan().getRoot()
                .hasEnrolmentWithEnroledState(curricularCourse, executionSemester);
    }

    protected RuleResult verifyDegreeConclusionWithRules(ICurricularRule curricularRule, EnrolmentContext enrolmentContext,
            DegreeModule degreeModuleToVerify, CourseGroup rootOrCycleCourseGroup) {
        return RuleResult.createNA(degreeModuleToVerify);
    }

    abstract protected RuleResult verifyEnrolmentWithRules(ICurricularRule curricularRule, EnrolmentContext enrolmentContext,
            DegreeModule degreeModuleToVerify, CourseGroup rootOrCycleCourseGroup);

    abstract protected RuleResult verifyEnrolmentWithTemporaryEnrolment(ICurricularRule curricularRule,
            EnrolmentContext enrolmentContext, DegreeModule degreeModuleToVerify, CourseGroup rootOrCycleCourseGroup);

}
