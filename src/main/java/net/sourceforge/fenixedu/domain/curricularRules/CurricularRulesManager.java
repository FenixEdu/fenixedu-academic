/*
 * Created on Jan 20, 2006
 */
package net.sourceforge.fenixedu.domain.curricularRules;

import net.sourceforge.fenixedu.dataTransferObject.bolonhaManager.CurricularRuleParametersDTO;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.degreeStructure.OptionalCurricularCourse;
import net.sourceforge.fenixedu.domain.organizationalStructure.DepartmentUnit;
import net.sourceforge.fenixedu.domain.util.LogicOperator;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class CurricularRulesManager {

    public static CurricularRule createCurricularRule(DegreeModule toApplyRule, ExecutionSemester begin, ExecutionSemester end,
            CurricularRuleType curricularRuleType, CurricularRuleParametersDTO parametersDTO) {

        switch (curricularRuleType) {

        case PRECEDENCY_APPROVED_DEGREE_MODULE:
            return createRestrictionDoneDegreeModule(toApplyRule, begin, end, parametersDTO);

        case PRECEDENCY_ENROLED_DEGREE_MODULE:
            return createRestrictionEnroledDegreeModule(toApplyRule, begin, end, parametersDTO);

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

    public static void editCurricularRule(CurricularRule curricularRule, ExecutionSemester beginExecutionPeriod,
            ExecutionSemester endExecutionPeriod) {
        curricularRule.edit(beginExecutionPeriod, endExecutionPeriod);
    }

    private static CurricularRule createMinimumNumberOfCreditsToEnrol(DegreeModule toApplyRule, ExecutionSemester begin,
            ExecutionSemester end, CurricularRuleParametersDTO parametersDTO) {

        final CourseGroup contextCourseGroup =
                (CourseGroup) AbstractDomainObject.fromExternalId(parametersDTO.getContextCourseGroupID());

        return new MinimumNumberOfCreditsToEnrol(toApplyRule, contextCourseGroup, begin, end, parametersDTO.getMinimumCredits());
    }

    private static CurricularRule createEvenOdd(DegreeModule toApplyRule, ExecutionSemester begin, ExecutionSemester end,
            CurricularRuleParametersDTO parametersDTO) {

        final CourseGroup contextCourseGroup =
                (CourseGroup) AbstractDomainObject.fromExternalId(parametersDTO.getContextCourseGroupID());

        return new EvenOddRule(toApplyRule, contextCourseGroup, parametersDTO.getCurricularPeriodInfoDTO().getOrder(),
                parametersDTO.getCurricularPeriodInfoDTO().getPeriodType(), parametersDTO.getEven(), begin, end);

    }

    private static CurricularRule createAnyCurricularCourse(DegreeModule toApplyRule, ExecutionSemester begin,
            ExecutionSemester end, CurricularRuleParametersDTO parametersDTO) {

        final CourseGroup contextCourseGroup =
                (CourseGroup) AbstractDomainObject.fromExternalId(parametersDTO.getContextCourseGroupID());
        final Degree degree = AbstractDomainObject.fromExternalId(parametersDTO.getSelectedDegreeID());
        final DepartmentUnit departmentUnit =
                (DepartmentUnit) AbstractDomainObject.fromExternalId(parametersDTO.getSelectedDepartmentUnitID());

        return new AnyCurricularCourse((OptionalCurricularCourse) toApplyRule, contextCourseGroup, begin, end,
                parametersDTO.getMinimumCredits(), parametersDTO.getMaximumCredits(), parametersDTO.getCurricularPeriodInfoDTO()
                        .getOrder(), parametersDTO.getMinimumYear(), parametersDTO.getMaximumYear(),
                parametersDTO.getDegreeType(), degree, departmentUnit);
    }

    private static CurricularRule createExclusiveness(DegreeModule firstExclusiveDegreeModule, ExecutionSemester begin,
            ExecutionSemester end, CurricularRuleParametersDTO parametersDTO) {

        final DegreeModule secondExclusiveDegreeModule =
                AbstractDomainObject.fromExternalId(parametersDTO.getSelectedDegreeModuleID());
        final CourseGroup contextCourseGroup =
                (CourseGroup) AbstractDomainObject.fromExternalId(parametersDTO.getContextCourseGroupID());

        final Exclusiveness firstRule =
                new Exclusiveness(firstExclusiveDegreeModule, secondExclusiveDegreeModule, contextCourseGroup, begin, end);

        new Exclusiveness(secondExclusiveDegreeModule, firstExclusiveDegreeModule, contextCourseGroup, begin, end);

        return firstRule;
    }

    private static CurricularRule createRestrictionBetweenDegreeModules(DegreeModule toApplyRule, ExecutionSemester begin,
            ExecutionSemester end, CurricularRuleParametersDTO parametersDTO) {

        final DegreeModule precedenceDegreeModule =
                AbstractDomainObject.fromExternalId(parametersDTO.getSelectedDegreeModuleID());
        final CourseGroup contextCourseGroup =
                (CourseGroup) AbstractDomainObject.fromExternalId(parametersDTO.getContextCourseGroupID());

        return new RestrictionBetweenDegreeModules(toApplyRule, precedenceDegreeModule, parametersDTO.getMinimumCredits(),
                contextCourseGroup, begin, end);
    }

    private static CurricularRule createEnrolmentToBeApprovedByCoordinator(DegreeModule toApplyRule, ExecutionSemester begin,
            ExecutionSemester end, CurricularRuleParametersDTO parametersDTO) {

        final CourseGroup contextCourseGroup =
                (CourseGroup) AbstractDomainObject.fromExternalId(parametersDTO.getContextCourseGroupID());

        return new EnrolmentToBeApprovedByCoordinator((CurricularCourse) toApplyRule, contextCourseGroup, begin, end);
    }

    private static CurricularRule createCreditsLimit(DegreeModule toApplyRule, ExecutionSemester begin, ExecutionSemester end,
            CurricularRuleParametersDTO parametersDTO) {

        final CourseGroup contextCourseGroup =
                (CourseGroup) AbstractDomainObject.fromExternalId(parametersDTO.getContextCourseGroupID());

        return new CreditsLimit(toApplyRule, contextCourseGroup, begin, end, parametersDTO.getMinimumCredits(),
                parametersDTO.getMaximumCredits());
    }

    private static CurricularRule createDegreeModulesSelectionLimit(DegreeModule toApplyRule, ExecutionSemester begin,
            ExecutionSemester end, CurricularRuleParametersDTO parametersDTO) {

        final CourseGroup contextCourseGroup =
                (CourseGroup) AbstractDomainObject.fromExternalId(parametersDTO.getContextCourseGroupID());

        return new DegreeModulesSelectionLimit((CourseGroup) toApplyRule, contextCourseGroup, begin, end,
                parametersDTO.getMinimumLimit(), parametersDTO.getMaximumLimit());
    }

    private static CurricularRule createRestrictionEnroledDegreeModule(DegreeModule toApplyRule, ExecutionSemester begin,
            ExecutionSemester end, CurricularRuleParametersDTO parametersDTO) {

        final DegreeModule enroledDegreeModule =
                AbstractDomainObject.fromExternalId(parametersDTO.getSelectedDegreeModuleID());
        final CourseGroup contextCourseGroup =
                (CourseGroup) AbstractDomainObject.fromExternalId(parametersDTO.getContextCourseGroupID());

        return new RestrictionEnroledDegreeModule((CurricularCourse) toApplyRule, (CurricularCourse) enroledDegreeModule,
                contextCourseGroup, parametersDTO.getCurricularPeriodInfoDTO(), begin, end);
    }

    private static CurricularRule createRestrictionDoneDegreeModule(DegreeModule toApplyRule, ExecutionSemester begin,
            ExecutionSemester end, CurricularRuleParametersDTO parametersDTO) {

        final DegreeModule done = AbstractDomainObject.fromExternalId(parametersDTO.getSelectedDegreeModuleID());
        final CourseGroup contextCourseGroup =
                (CourseGroup) AbstractDomainObject.fromExternalId(parametersDTO.getContextCourseGroupID());

        return new RestrictionDoneDegreeModule((CurricularCourse) toApplyRule, (CurricularCourse) done, contextCourseGroup,
                parametersDTO.getCurricularPeriodInfoDTO(), begin, end);
    }
}
