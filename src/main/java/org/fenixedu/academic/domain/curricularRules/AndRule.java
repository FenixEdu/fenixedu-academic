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
import org.fenixedu.academic.domain.curricularRules.executors.verifyExecutors.VerifyRuleLevel;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.domain.enrolment.EnrolmentContext;
import org.fenixedu.academic.domain.enrolment.IDegreeModuleToEvaluate;
import org.fenixedu.academic.domain.util.LogicOperator;
import org.fenixedu.academic.dto.GenericPair;

public class AndRule extends AndRule_Base {

    public AndRule(CurricularRule... curricularRules) {
        initCompositeRule(curricularRules);
        setCompositeRuleType(LogicOperator.AND);
    }

    @Override
    public List<GenericPair<Object, Boolean>> getLabel() {
        return getLabel("label.operator.and");
    }

    @Override
    public RuleResult evaluate(IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, EnrolmentContext enrolmentContext) {
        RuleResult result = RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());
        for (final CurricularRule curricularRule : getCurricularRulesSet()) {
            result = result.and(curricularRule.evaluate(sourceDegreeModuleToEvaluate, enrolmentContext));
        }
        return result;
    }

    @Override
    public RuleResult verify(VerifyRuleLevel verifyRuleLevel, EnrolmentContext enrolmentContext,
            DegreeModule degreeModuleToVerify, CourseGroup parentCourseGroup) {
        RuleResult result = RuleResult.createTrue(degreeModuleToVerify);
        for (final CurricularRule curricularRule : getCurricularRulesSet()) {
            result =
                    result.and(curricularRule.verify(verifyRuleLevel, enrolmentContext, degreeModuleToVerify, parentCourseGroup));
        }
        return result;
    }

}
