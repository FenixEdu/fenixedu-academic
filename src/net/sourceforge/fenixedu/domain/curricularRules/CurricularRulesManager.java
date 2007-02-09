/*
 * Created on Jan 20, 2006
 */
package net.sourceforge.fenixedu.domain.curricularRules;

import net.sourceforge.fenixedu.dataTransferObject.bolonhaManager.CurricularRuleParametersDTO;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.degreeStructure.OptionalCurricularCourse;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.util.LogicOperators;

public class CurricularRulesManager {

    public static CurricularRule createCurricularRule(DegreeModule toApplyRule, ExecutionPeriod begin,
	    ExecutionPeriod end, CurricularRuleType curricularRuleType,
	    CurricularRuleParametersDTO parametersDTO) {

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

	default:
	    break;
	}

	return null;
    }

    public static CurricularRule createCompositeRule(LogicOperators logicOperator,
	    CurricularRule... curricularRules) {
	return CurricularRule.createCurricularRule(logicOperator, curricularRules);
    }

    public static void editCurricularRule(CurricularRule curricularRule,
	    ExecutionPeriod beginExecutionPeriod, ExecutionPeriod endExecutionPeriod) {
	curricularRule.edit(beginExecutionPeriod, endExecutionPeriod);
    }

    private static CurricularRule createMinimumNumberOfCreditsToEnrol(DegreeModule toApplyRule,
	    ExecutionPeriod begin, ExecutionPeriod end, CurricularRuleParametersDTO parametersDTO) {

	final CourseGroup contextCourseGroup = (CourseGroup) RootDomainObject.getInstance()
		.readDegreeModuleByOID(parametersDTO.getContextCourseGroupID());

	return new MinimumNumberOfCreditsToEnrol(toApplyRule, contextCourseGroup, begin, end,
		parametersDTO.getMinimumCredits());
    }

    private static CurricularRule createAnyCurricularCourse(DegreeModule toApplyRule,
	    ExecutionPeriod begin, ExecutionPeriod end, CurricularRuleParametersDTO parametersDTO) {

	final CourseGroup contextCourseGroup = (CourseGroup) RootDomainObject.getInstance()
		.readDegreeModuleByOID(parametersDTO.getContextCourseGroupID());
	final Degree degree = RootDomainObject.getInstance().readDegreeByOID(
		parametersDTO.getSelectedDegreeID());
	final Unit departmentUnit = (Unit) RootDomainObject.getInstance().readPartyByOID(
		parametersDTO.getSelectedDepartmentUnitID());

	return new AnyCurricularCourse((OptionalCurricularCourse) toApplyRule, contextCourseGroup, begin, end,
		parametersDTO.getCredits(), parametersDTO.getCurricularPeriodInfoDTO().getOrder(),
		parametersDTO.getMinimumYear(), parametersDTO.getMaximumYear(), parametersDTO
			.getDegreeType(), degree, departmentUnit);
    }

    private static CurricularRule createExclusiveness(DegreeModule firstExclusiveDegreeModule,
	    ExecutionPeriod begin, ExecutionPeriod end, CurricularRuleParametersDTO parametersDTO) {

	final DegreeModule secondExclusiveDegreeModule = RootDomainObject.getInstance()
		.readDegreeModuleByOID(parametersDTO.getSelectedDegreeModuleID());
	final CourseGroup contextCourseGroup = (CourseGroup) RootDomainObject.getInstance()
		.readDegreeModuleByOID(parametersDTO.getContextCourseGroupID());

	final Exclusiveness firstRule = new Exclusiveness(firstExclusiveDegreeModule,
		secondExclusiveDegreeModule, contextCourseGroup, begin, end);

	new Exclusiveness(secondExclusiveDegreeModule, firstExclusiveDegreeModule, contextCourseGroup,
		begin, end);

	return firstRule;
    }

    private static CurricularRule createRestrictionBetweenDegreeModules(DegreeModule toApplyRule,
	    ExecutionPeriod begin, ExecutionPeriod end, CurricularRuleParametersDTO parametersDTO) {

	final DegreeModule precedenceDegreeModule = RootDomainObject.getInstance()
		.readDegreeModuleByOID(parametersDTO.getSelectedDegreeModuleID());
	final CourseGroup contextCourseGroup = (CourseGroup) RootDomainObject.getInstance()
		.readDegreeModuleByOID(parametersDTO.getContextCourseGroupID());

	return new RestrictionBetweenDegreeModules((CourseGroup) toApplyRule,
		(CourseGroup) precedenceDegreeModule, parametersDTO.getMinimumCredits(),
		contextCourseGroup, begin, end);
    }

    private static CurricularRule createEnrolmentToBeApprovedByCoordinator(DegreeModule toApplyRule,
	    ExecutionPeriod begin, ExecutionPeriod end, CurricularRuleParametersDTO parametersDTO) {

	final CourseGroup contextCourseGroup = (CourseGroup) RootDomainObject.getInstance()
		.readDegreeModuleByOID(parametersDTO.getContextCourseGroupID());

	return new EnrolmentToBeApprovedByCoordinator((CurricularCourse) toApplyRule,
		contextCourseGroup, begin, end);
    }

    private static CurricularRule createCreditsLimit(DegreeModule toApplyRule, ExecutionPeriod begin,
	    ExecutionPeriod end, CurricularRuleParametersDTO parametersDTO) {

	final CourseGroup contextCourseGroup = (CourseGroup) RootDomainObject.getInstance()
		.readDegreeModuleByOID(parametersDTO.getContextCourseGroupID());

	return new CreditsLimit(toApplyRule, contextCourseGroup, begin, end, parametersDTO
		.getMinimumCredits(), parametersDTO.getMaximumCredits());
    }

    private static CurricularRule createDegreeModulesSelectionLimit(DegreeModule toApplyRule,
	    ExecutionPeriod begin, ExecutionPeriod end, CurricularRuleParametersDTO parametersDTO) {

	final CourseGroup contextCourseGroup = (CourseGroup) RootDomainObject.getInstance()
		.readDegreeModuleByOID(parametersDTO.getContextCourseGroupID());

	return new DegreeModulesSelectionLimit((CourseGroup) toApplyRule, contextCourseGroup, begin,
		end, parametersDTO.getMinimumLimit(), parametersDTO.getMaximumLimit());
    }

    private static CurricularRule createRestrictionEnroledDegreeModule(DegreeModule toApplyRule,
	    ExecutionPeriod begin, ExecutionPeriod end, CurricularRuleParametersDTO parametersDTO) {

	final DegreeModule enroledDegreeModule = RootDomainObject.getInstance().readDegreeModuleByOID(
		parametersDTO.getSelectedDegreeModuleID());
	final CourseGroup contextCourseGroup = (CourseGroup) RootDomainObject.getInstance()
		.readDegreeModuleByOID(parametersDTO.getContextCourseGroupID());

	return new RestrictionEnroledDegreeModule((CurricularCourse) toApplyRule,
		(CurricularCourse) enroledDegreeModule, contextCourseGroup, parametersDTO
			.getCurricularPeriodInfoDTO(), begin, end);
    }

    private static CurricularRule createRestrictionDoneDegreeModule(DegreeModule toApplyRule,
	    ExecutionPeriod begin, ExecutionPeriod end, CurricularRuleParametersDTO parametersDTO) {

	final DegreeModule done = RootDomainObject.getInstance().readDegreeModuleByOID(
		parametersDTO.getSelectedDegreeModuleID());
	final CourseGroup contextCourseGroup = (CourseGroup) RootDomainObject.getInstance()
		.readDegreeModuleByOID(parametersDTO.getContextCourseGroupID());

	return new RestrictionDoneDegreeModule((CurricularCourse) toApplyRule, (CurricularCourse) done,
		contextCourseGroup, parametersDTO.getCurricularPeriodInfoDTO(), begin, end);
    }
}
