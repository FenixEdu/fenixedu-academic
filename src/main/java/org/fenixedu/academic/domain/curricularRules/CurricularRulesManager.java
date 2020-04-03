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
/*
 * Created on Jan 20, 2006
 */
package org.fenixedu.academic.domain.curricularRules;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.domain.degreeStructure.OptionalCurricularCourse;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.util.LogicOperator;
import org.fenixedu.academic.dto.bolonhaManager.CurricularRuleParametersDTO;

import pt.ist.fenixframework.FenixFramework;

public class CurricularRulesManager {

    public static CurricularRule createCurricularRule(DegreeModule toApplyRule, ExecutionInterval begin, ExecutionInterval end,
            CurricularRuleType curricularRuleType, CurricularRuleParametersDTO parametersDTO) {

        switch (curricularRuleType) {

        case PRECEDENCY_APPROVED_DEGREE_MODULE:
            return createRestrictionDoneDegreeModule(toApplyRule, begin, end, parametersDTO);

        case PRECEDENCY_ENROLED_DEGREE_MODULE:
            return createRestrictionEnroledDegreeModule(toApplyRule, begin, end, parametersDTO);

        case RESTRICTION_NOT_ENROLED_DEGREE_MODULE:
            return createRestrictionNotEnroledDegreeModule(toApplyRule, begin, end, parametersDTO);

        case CREDITS_LIMIT:
            return createCreditsLimit(toApplyRule, begin, end, parametersDTO);

        case DEGREE_MODULES_SELECTION_LIMIT:
            return createDegreeModulesSelectionLimit(toApplyRule, begin, end, parametersDTO);

        case ENROLMENT_TO_BE_APPROVED_BY_COORDINATOR:
            return createEnrolmentToBeApprovedByCoordinator(toApplyRule, begin, end, parametersDTO);

        case PRECEDENCY_BETWEEN_DEGREE_MODULES:
            return createRestrictionBetweenDegreeModules(toApplyRule, begin, end, parametersDTO);

        case EXCLUSIVENESS:
            return createExclusiveness(toApplyRule, begin, end, parametersDTO);

        case ANY_CURRICULAR_COURSE:
            return createAnyCurricularCourse(toApplyRule, begin, end, parametersDTO);

        case ANY_CURRICULAR_COURSE_EXCEPTIONS:
            return createAnyCurricularCourseExceptions(toApplyRule, begin, end, parametersDTO);

        case MINIMUM_NUMBER_OF_CREDITS_TO_ENROL:
            return createMinimumNumberOfCreditsToEnrol(toApplyRule, begin, end, parametersDTO);

        case EVEN_ODD:
            return createEvenOdd(toApplyRule, begin, end, parametersDTO);

        default:
            break;
        }

        return null;
    }

    public static CurricularRule createCompositeRule(LogicOperator logicOperator, CurricularRule... curricularRules) {
        return CurricularRule.createCurricularRule(logicOperator, curricularRules);
    }

    public static void editCurricularRule(CurricularRule curricularRule, ExecutionInterval beginExecutionPeriod,
            ExecutionInterval endExecutionPeriod) {
        curricularRule.edit(beginExecutionPeriod, endExecutionPeriod);
    }

    private static CurricularRule createMinimumNumberOfCreditsToEnrol(DegreeModule toApplyRule, ExecutionInterval begin,
            ExecutionInterval end, CurricularRuleParametersDTO parametersDTO) {

        final CourseGroup contextCourseGroup =
                (CourseGroup) FenixFramework.getDomainObject(parametersDTO.getContextCourseGroupID());

        return new MinimumNumberOfCreditsToEnrol(toApplyRule, contextCourseGroup, begin, end, parametersDTO.getMinimumCredits());
    }

    private static CurricularRule createEvenOdd(DegreeModule toApplyRule, ExecutionInterval begin, ExecutionInterval end,
            CurricularRuleParametersDTO parametersDTO) {

        final CourseGroup contextCourseGroup =
                (CourseGroup) FenixFramework.getDomainObject(parametersDTO.getContextCourseGroupID());

        return new EvenOddRule(toApplyRule, contextCourseGroup, parametersDTO.getCurricularPeriodInfoDTO().getOrder(),
                parametersDTO.getCurricularPeriodInfoDTO().getPeriodType(), parametersDTO.getEven(), begin, end);

    }

    private static CurricularRule createAnyCurricularCourse(DegreeModule toApplyRule, ExecutionInterval begin,
            ExecutionInterval end, CurricularRuleParametersDTO parametersDTO) {

        final CourseGroup contextCourseGroup =
                (CourseGroup) FenixFramework.getDomainObject(parametersDTO.getContextCourseGroupID());
        final Degree degree = FenixFramework.getDomainObject(parametersDTO.getSelectedDegreeID());
        final Unit departmentUnit = FenixFramework.getDomainObject(parametersDTO.getSelectedDepartmentUnitID());

        return new AnyCurricularCourse((OptionalCurricularCourse) toApplyRule, contextCourseGroup, begin, end,
                parametersDTO.getMinimumCredits(), parametersDTO.getMaximumCredits(),
                parametersDTO.getCurricularPeriodInfoDTO().getOrder(), parametersDTO.getDegreeType(), degree, departmentUnit);
    }

    private static CurricularRule createAnyCurricularCourseExceptions(DegreeModule toApplyRule, ExecutionInterval begin,
            ExecutionInterval end, CurricularRuleParametersDTO parametersDTO) {

        final CourseGroup contextCourseGroup =
                (CourseGroup) FenixFramework.getDomainObject(parametersDTO.getContextCourseGroupID());

        return new AnyCurricularCourseExceptions((OptionalCurricularCourse) toApplyRule, contextCourseGroup, begin, end,
                parametersDTO.getOptionalsConfiguration());
    }

    private static CurricularRule createExclusiveness(DegreeModule firstExclusiveDegreeModule, ExecutionInterval begin,
            ExecutionInterval end, CurricularRuleParametersDTO parametersDTO) {

        final DegreeModule secondExclusiveDegreeModule =
                FenixFramework.getDomainObject(parametersDTO.getSelectedDegreeModuleID());
        final CourseGroup contextCourseGroup =
                (CourseGroup) FenixFramework.getDomainObject(parametersDTO.getContextCourseGroupID());

        final Exclusiveness firstRule =
                new Exclusiveness(firstExclusiveDegreeModule, secondExclusiveDegreeModule, contextCourseGroup, begin, end);

        new Exclusiveness(secondExclusiveDegreeModule, firstExclusiveDegreeModule, contextCourseGroup, begin, end);

        return firstRule;
    }

    private static CurricularRule createRestrictionBetweenDegreeModules(DegreeModule toApplyRule, ExecutionInterval begin,
            ExecutionInterval end, CurricularRuleParametersDTO parametersDTO) {

        final DegreeModule precedenceDegreeModule = FenixFramework.getDomainObject(parametersDTO.getSelectedDegreeModuleID());
        final CourseGroup contextCourseGroup =
                (CourseGroup) FenixFramework.getDomainObject(parametersDTO.getContextCourseGroupID());

        return new RestrictionBetweenDegreeModules(toApplyRule, precedenceDegreeModule, parametersDTO.getMinimumCredits(),
                contextCourseGroup, begin, end);
    }

    private static CurricularRule createEnrolmentToBeApprovedByCoordinator(DegreeModule toApplyRule, ExecutionInterval begin,
            ExecutionInterval end, CurricularRuleParametersDTO parametersDTO) {

        final CourseGroup contextCourseGroup =
                (CourseGroup) FenixFramework.getDomainObject(parametersDTO.getContextCourseGroupID());

        return new EnrolmentToBeApprovedByCoordinator(toApplyRule, contextCourseGroup, begin, end);
    }

    private static CurricularRule createCreditsLimit(DegreeModule toApplyRule, ExecutionInterval begin, ExecutionInterval end,
            CurricularRuleParametersDTO parametersDTO) {

        final CourseGroup contextCourseGroup =
                (CourseGroup) FenixFramework.getDomainObject(parametersDTO.getContextCourseGroupID());

        return new CreditsLimit(toApplyRule, contextCourseGroup, begin, end, parametersDTO.getMinimumCredits(),
                parametersDTO.getMaximumCredits());
    }

    private static CurricularRule createDegreeModulesSelectionLimit(DegreeModule toApplyRule, ExecutionInterval begin,
            ExecutionInterval end, CurricularRuleParametersDTO parametersDTO) {

        final CourseGroup contextCourseGroup =
                (CourseGroup) FenixFramework.getDomainObject(parametersDTO.getContextCourseGroupID());

        return new DegreeModulesSelectionLimit((CourseGroup) toApplyRule, contextCourseGroup, begin, end,
                parametersDTO.getMinimumLimit(), parametersDTO.getMaximumLimit());
    }

    private static CurricularRule createRestrictionEnroledDegreeModule(DegreeModule toApplyRule, ExecutionInterval begin,
            ExecutionInterval end, CurricularRuleParametersDTO parametersDTO) {

        final DegreeModule enroledDegreeModule = FenixFramework.getDomainObject(parametersDTO.getSelectedDegreeModuleID());
        final CourseGroup contextCourseGroup =
                (CourseGroup) FenixFramework.getDomainObject(parametersDTO.getContextCourseGroupID());

        return new RestrictionEnroledDegreeModule((CurricularCourse) toApplyRule, (CurricularCourse) enroledDegreeModule,
                contextCourseGroup, parametersDTO.getCurricularPeriodInfoDTO(), begin, end);
    }

    private static CurricularRule createRestrictionNotEnroledDegreeModule(DegreeModule toApplyRule, ExecutionInterval begin,
            ExecutionInterval end, CurricularRuleParametersDTO parametersDTO) {

        final DegreeModule notEnroledDegreeModule = FenixFramework.getDomainObject(parametersDTO.getSelectedDegreeModuleID());
        final CourseGroup contextCourseGroup =
                (CourseGroup) FenixFramework.getDomainObject(parametersDTO.getContextCourseGroupID());

        return new RestrictionNotEnroledDegreeModule((CurricularCourse) toApplyRule, (CurricularCourse) notEnroledDegreeModule,
                contextCourseGroup, parametersDTO.getCurricularPeriodInfoDTO(), begin, end);
    }

    private static CurricularRule createRestrictionDoneDegreeModule(DegreeModule toApplyRule, ExecutionInterval begin,
            ExecutionInterval end, CurricularRuleParametersDTO parametersDTO) {

        final DegreeModule done = FenixFramework.getDomainObject(parametersDTO.getSelectedDegreeModuleID());
        final CourseGroup contextCourseGroup =
                (CourseGroup) FenixFramework.getDomainObject(parametersDTO.getContextCourseGroupID());

        return new RestrictionDoneDegreeModule((CurricularCourse) toApplyRule, (CurricularCourse) done, contextCourseGroup,
                parametersDTO.getCurricularPeriodInfoDTO(), begin, end);
    }
}
