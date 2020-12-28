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

import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.curricularRules.ICurricularRule;
import org.fenixedu.academic.domain.curricularRules.executors.RuleResult;
import org.fenixedu.academic.domain.enrolment.EnrolmentContext;
import org.fenixedu.academic.domain.enrolment.IDegreeModuleToEvaluate;
import org.fenixedu.academic.service.AcademicPermissionService;
import org.fenixedu.academic.util.CurricularRuleLabelFormatter;

public class EnrolmentToBeApprovedByCoordinatorExecutor extends CurricularRuleExecutor {

    @Override
    protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(final ICurricularRule curricularRule,
            IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
        return executeEnrolmentVerificationWithRules(curricularRule, sourceDegreeModuleToEvaluate, enrolmentContext);
    }

    @Override
    protected RuleResult executeEnrolmentInEnrolmentEvaluation(final ICurricularRule curricularRule,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
        return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
    }

    @Override
    protected RuleResult executeEnrolmentVerificationWithRules(ICurricularRule curricularRule,
            IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, EnrolmentContext enrolmentContext) {

        if (isPersonAuthorized(enrolmentContext)) {
            return RuleResult.createWarning(sourceDegreeModuleToEvaluate.getDegreeModule(),
                    "curricularRules.ruleExecutors.EnrolmentToBeApprovedByCoordinatorExecutor.degree.module.needs.aproval.by.coordinator",
                    sourceDegreeModuleToEvaluate.getName());
        }

        if (sourceDegreeModuleToEvaluate.isEnroled()) {
            return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
        }

        return RuleResult.createFalse(sourceDegreeModuleToEvaluate.getDegreeModule(),
                "curricularRules.ruleExecutors.EnrolmentToBeApprovedByCoordinatorExecutor.degree.module.needs.aproval.by.coordinator",
                sourceDegreeModuleToEvaluate.getName());
    }

    @Override
    protected RuleResult executeEnrolmentPrefilter(ICurricularRule curricularRule,
            IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, EnrolmentContext enrolmentContext) {
        if (isPersonAuthorized(enrolmentContext)) {
            return RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());
        }

        if (!sourceDegreeModuleToEvaluate.isLeaf()) {
            return RuleResult.createImpossibleWithLiteralMessage(sourceDegreeModuleToEvaluate.getDegreeModule(),
                    CurricularRuleLabelFormatter.getLabel(curricularRule));
        }

        return sourceDegreeModuleToEvaluate.isEnroled() ? RuleResult
                .createImpossibleWithLiteralMessage(sourceDegreeModuleToEvaluate.getDegreeModule(), CurricularRuleLabelFormatter
                        .getLabel(curricularRule)) : RuleResult.createFalse(sourceDegreeModuleToEvaluate.getDegreeModule());
    }

    private boolean isPersonAuthorized(EnrolmentContext enrolmentContext) {
        return AcademicAccessRule.isProgramAccessibleToFunction(AcademicOperationType.STUDENT_ENROLMENTS,
                enrolmentContext.getStudentCurricularPlan().getDegree(), enrolmentContext.getResponsiblePerson().getUser())
                || AcademicPermissionService.hasAccess("ACADEMIC_OFFICE_ENROLMENTS",
                        enrolmentContext.getStudentCurricularPlan().getDegree(),
                        enrolmentContext.getResponsiblePerson().getUser());
    }

    @Override
    protected boolean canBeEvaluated(ICurricularRule curricularRule, IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate,
            EnrolmentContext enrolmentContext) {
        return true;
    }

}
