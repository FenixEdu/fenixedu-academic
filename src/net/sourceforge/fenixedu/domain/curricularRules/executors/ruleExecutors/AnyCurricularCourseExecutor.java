package net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.AnyCurricularCourse;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.executors.RuleResult;
import net.sourceforge.fenixedu.domain.enrolment.EnroledOptionalEnrolment;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.enrolment.OptionalDegreeModuleToEnrol;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.CurricularRuleLabelFormatter;

public class AnyCurricularCourseExecutor extends CurricularRuleExecutor {

    /**
     * -> if getDegree() == null ? getBolonhaDegreeType() == null ? any degree
     * from IST ? getBolonhaDegreeType() != null ? any degree with same
     * DegreeType -> else ? check selected degree -> if departmentUnit != null ?
     * CurricularCourse from CompetenceCourse that belong to that Department
     */
    @Override
    protected RuleResult executeEnrolmentWithRules(final ICurricularRule curricularRule,
	    IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {

	final AnyCurricularCourse rule = (AnyCurricularCourse) curricularRule;

	if (!rule.appliesToContext(sourceDegreeModuleToEvaluate.getContext())) {
	    return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
	}

	final CurricularCourse curricularCourseToEnrol;
	if (sourceDegreeModuleToEvaluate.isEnroling()) {
	    final OptionalDegreeModuleToEnrol optionalDegreeModuleToEnrol = (OptionalDegreeModuleToEnrol) sourceDegreeModuleToEvaluate;
	    curricularCourseToEnrol = optionalDegreeModuleToEnrol.getCurricularCourse();

	    if (isApproved(enrolmentContext, curricularCourseToEnrol) || isEnroled(enrolmentContext, curricularCourseToEnrol)
		    || isEnrolling(enrolmentContext, curricularCourseToEnrol)) {

		return RuleResult.createFalse(sourceDegreeModuleToEvaluate.getDegreeModule(),
			"curricularRules.ruleExecutors.AnyCurricularCourseExecutor.already.approved.or.enroled",
			curricularCourseToEnrol.getName(), rule.getDegreeModuleToApplyRule().getName());
	    }

	} else if (sourceDegreeModuleToEvaluate.isEnroled()) {
	    curricularCourseToEnrol = ((EnroledOptionalEnrolment) sourceDegreeModuleToEvaluate).getOptionalCurricularCourse();
	} else {
	    throw new DomainException(
		    "error.curricularRules.executors.ruleExecutors.AnyCurricularCourseExecutor.unexpected.degree.module.to.evaluate");
	}

	final ExecutionPeriod executionPeriod = enrolmentContext.getExecutionPeriod();
	final Degree degree = curricularCourseToEnrol.getDegree();

	boolean result = true;

	result &= rule.hasCredits() ? rule.getCredits().equals(curricularCourseToEnrol.getEctsCredits(executionPeriod)) : true;

	result &= rule.hasDegree() ? rule.getDegree() == degree : rule.hasBolonhaDegreeType() ? degree.getDegreeType() == rule
		.getBolonhaDegreeType() : true;

	result &= rule.hasDepartmentUnit() ? rule.getDepartmentUnit().hasCompetenceCourses(
		curricularCourseToEnrol.getCompetenceCourse()) : true;

	if (result) {
	    return RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());
	} else {
	    if (sourceDegreeModuleToEvaluate.isEnroled()) {
		return RuleResult.createTrue(EnrolmentResultType.IMPOSSIBLE, sourceDegreeModuleToEvaluate.getDegreeModule());
	    } else {
		return RuleResult.createFalseWithLiteralMessage(sourceDegreeModuleToEvaluate.getDegreeModule(),
			CurricularRuleLabelFormatter.getLabel(rule));
	    }
	}

    }

    @Override
    protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(final ICurricularRule curricularRule,
	    final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
	return executeEnrolmentWithRules(curricularRule, sourceDegreeModuleToEvaluate, enrolmentContext);
    }

    @Override
    protected RuleResult executeEnrolmentInEnrolmentEvaluation(final ICurricularRule curricularRule,
	    final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
	return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
    }

}
