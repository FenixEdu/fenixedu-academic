package net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.AnyCurricularCourse;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.enrolment.OptionalDegreeModuleToEnrol;
import net.sourceforge.fenixedu.util.CurricularRuleLabelFormatter;

public class AnyCurricularCourseExecutor extends CurricularRuleExecutor {

    /**
     * -> if getDegree() == null 
     *      ? getBolonhaDegreeType() == null ? any degree from IST
     *      ? getBolonhaDegreeType() != null ? any degree with same DegreeType
     * -> else 
     * 	    ? check selected degree
     * 
     * -> if departmentUnit != null ? CurricularCourse from CompetenceCourse that belong to that Department      
     */
    @Override
    protected RuleResult executeEnrolmentWithRules(final ICurricularRule curricularRule,
	    IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {

	final AnyCurricularCourse rule = (AnyCurricularCourse) curricularRule;
	final OptionalDegreeModuleToEnrol moduleToEnrol = (OptionalDegreeModuleToEnrol) searchDegreeModuleToEvaluate(
		enrolmentContext, rule);

	if (!rule.appliesToContext(moduleToEnrol.getContext())) {
	    return RuleResult.createNA();
	}

	final CurricularCourse curricularCourseToEnrol = moduleToEnrol.getCurricularCourse();

	if (isApproved(enrolmentContext, curricularCourseToEnrol)
		|| isEnroled(enrolmentContext, curricularCourseToEnrol)
		|| isEnrolling(enrolmentContext, curricularCourseToEnrol)) {

	    return RuleResult
		    .createFalse(
			    "curricularRules.ruleExecutors.AnyCurricularCourseExecutor.already.approved.or.enroled",
			    curricularCourseToEnrol.getName(), rule.getDegreeModuleToApplyRule()
				    .getName());
	}

	final ExecutionPeriod executionPeriod = enrolmentContext.getExecutionPeriod();
	final Degree degree = curricularCourseToEnrol.getDegree();

	boolean result = true;

	result &= rule.hasCredits() ? rule.getCredits().equals(
		curricularCourseToEnrol.getEctsCredits(executionPeriod)) : true;

	result &= rule.hasDegree() ? rule.getDegree() == degree : rule.hasBolonhaDegreeType() ? degree
		.getDegreeType() == rule.getBolonhaDegreeType() : true;

	result &= rule.hasDepartmentUnit() ? rule.getDepartmentUnit().hasCompetenceCourses(
		curricularCourseToEnrol.getCompetenceCourse()) : true;

	return result ? RuleResult.createTrue() : RuleResult
		.createFalseWithLiteralMessage(CurricularRuleLabelFormatter.getLabel(rule));
    }

    @Override
    protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(final ICurricularRule curricularRule, final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
	return executeEnrolmentWithRules(curricularRule, sourceDegreeModuleToEvaluate, enrolmentContext);
    }

    @Override
    protected RuleResult executeEnrolmentInEnrolmentEvaluation(final ICurricularRule curricularRule, final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
	return RuleResult.createNA();
    }

}
