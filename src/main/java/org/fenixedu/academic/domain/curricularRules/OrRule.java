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
package org.fenixedu.academic.domain.curricularRules;

import java.util.List;

import org.fenixedu.academic.domain.curricularRules.executors.RuleResult;
import org.fenixedu.academic.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;
import org.fenixedu.academic.domain.curricularRules.executors.ruleExecutors.EnrolmentResultType;
import org.fenixedu.academic.domain.curricularRules.executors.verifyExecutors.VerifyRuleLevel;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.domain.enrolment.EnrolmentContext;
import org.fenixedu.academic.domain.enrolment.IDegreeModuleToEvaluate;
import org.fenixedu.academic.domain.util.LogicOperator;
import org.fenixedu.academic.dto.GenericPair;

public class OrRule extends OrRule_Base {

    public OrRule(CurricularRule... curricularRules) {
        initCompositeRule(curricularRules);
        setCompositeRuleType(LogicOperator.OR);
    }

    @Override
    public List<GenericPair<Object, Boolean>> getLabel() {
        return getLabel("label.operator.or");
    }

    @Override
    public RuleResult evaluate(IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, EnrolmentContext enrolmentContext) {
        if (enrolmentContext.getCurricularRuleLevel() == CurricularRuleLevel.ENROLMENT_PREFILTER) {
            //Future improvement: only return true if all rule evaluations resulted in NA
            return RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());
        }

        RuleResult resultOR = RuleResult.createFalse(EnrolmentResultType.NULL, sourceDegreeModuleToEvaluate.getDegreeModule());
        for (final CurricularRule curricularRule : getCurricularRulesSet()) {
            resultOR = resultOR.or(curricularRule.evaluate(sourceDegreeModuleToEvaluate, enrolmentContext));
            if (resultOR.isTrue() && resultOR.isValidated(sourceDegreeModuleToEvaluate.getDegreeModule())) {
                return RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());
            }
        }
        return resultOR;
    }

    @Override
    public RuleResult verify(VerifyRuleLevel verifyRuleLevel, EnrolmentContext enrolmentContext,
            DegreeModule degreeModuleToVerify, CourseGroup parentCourseGroup) {
        RuleResult resultOR = RuleResult.createFalse(EnrolmentResultType.NULL, degreeModuleToVerify);
        for (final CurricularRule curricularRule : getCurricularRulesSet()) {
            resultOR =
                    resultOR.or(curricularRule.verify(verifyRuleLevel, enrolmentContext, degreeModuleToVerify, parentCourseGroup));
            if (resultOR.isTrue() && resultOR.isValidated(degreeModuleToVerify)) {
                return RuleResult.createTrue(degreeModuleToVerify);
            }
        }
        return resultOR;
    }
}
