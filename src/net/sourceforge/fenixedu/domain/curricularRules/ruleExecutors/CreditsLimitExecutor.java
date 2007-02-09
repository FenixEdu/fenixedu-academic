package net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.CreditsLimit;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.enrolment.DegreeModuleToEnrol;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.OptionalDegreeModuleToEnrol;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;

public class CreditsLimitExecutor extends CurricularRuleExecutor {

    /**
     * With Rules
     * 
     * 1. If rule was selected due to an enrolment in a given Degree Module
     * 
     * 1.1 If Degree Module is OptionalCurricularCourse, continue; Rule returns NA if otherwise 
     * 1.1.1 Rule must apply to Context; returns NA if otherwise 
     * 1.1.2 Ects Credits of the chosen Curricular Course must not exceed the Rule limits; Rule returns False if otherwise
     * 
     * 2. Rule was gathered into a collection of rules and must be evaluated
     * 
     * 2.1 If Rule has a Course Group context, it must be the same as the ParentCourseGroup of the DegreeModule it applies to 
     * 		(note: this implies inspecting the StudentCurricularPlan to find this ParentCourseGroup).
     * 		Returns NA if otherwise.
     * 2.2 If the sum of Ects Credits of Aproved, Enroled and to be enroled Curricular Courses exceeds the Rule's Credits, it 
     * 		returns false; true if otherwise. 
     */
    
    @Override
    protected RuleResult executeEnrolmentWithRules(final CurricularRule curricularRule, final EnrolmentContext enrolmentContext) {
	
	final CreditsLimit rule = (CreditsLimit) curricularRule;
	
	if (ruleWasSelectedFromAnyModuleToEnrol(enrolmentContext, curricularRule)) {
	    return evaluateIfCanEnrolToDegreeModule(enrolmentContext, rule);
	} 
	
	if (appliesToCourseGroup(enrolmentContext, rule)) {
	    final CurriculumModule curriculumModule = searchCurriculumModule(enrolmentContext, rule);
	    
	    final Double ectsCredits = curriculumModule.getAprovedEctsCredits()
		    + curriculumModule.getEnroledEctsCredits(enrolmentContext.getExecutionPeriod())
		    + calculateEctsCreditsFromToEnrolCurricularCourses(enrolmentContext, rule);
	    
	    return rule.creditsExceedMaximum(ectsCredits) ? createFalseRuleResult(rule) : RuleResult.createTrue();
	    
	} else {
	    return RuleResult.createNA();
	}
    }

    
    private RuleResult evaluateIfCanEnrolToDegreeModule(final EnrolmentContext enrolmentContext, final CreditsLimit rule) {

	final DegreeModule degreeModule = rule.getDegreeModuleToApplyRule();
	if (degreeModule.isOptional()) {
	    
	    final OptionalDegreeModuleToEnrol optionalToEnrol = (OptionalDegreeModuleToEnrol) searchDegreeModuleToEnrol(enrolmentContext, rule);
	    final CurricularCourse curricularCourse = optionalToEnrol.getCurricularCourse();

	    if (appliesToContext(enrolmentContext, rule)) {
		final ExecutionPeriod executionPeriod = enrolmentContext.getExecutionPeriod();
		return rule.allowCredits(curricularCourse.getEctsCredits(executionPeriod)) ? RuleResult.createTrue() : createFalseRuleResult(rule);
	    }
	}
	return RuleResult.createNA();
    }

    
    private Double calculateEctsCreditsFromToEnrolCurricularCourses(EnrolmentContext enrolmentContext, CreditsLimit rule) {
	BigDecimal result = BigDecimal.ZERO;
	final CourseGroup courseGroup = (CourseGroup) rule.getDegreeModuleToApplyRule();
	final ExecutionPeriod executionPeriod = enrolmentContext.getExecutionPeriod();
	for (final DegreeModuleToEnrol moduleToEnrol : collectDegreeModuleToEnrolFromCourseGroup(enrolmentContext, courseGroup)) {
	    result = result.add(new BigDecimal(moduleToEnrol.getEctsCredits(executionPeriod)));
	}
	return Double.valueOf(result.doubleValue());
    }
    

    private RuleResult createFalseRuleResult(final CreditsLimit rule) {
	if (rule.getMinimumCredits().equals(rule.getMaximumCredits())) {
	    return RuleResult.createFalse(
		    "curricularRules.ruleExecutors.CreditsLimitExecutor.limit.not.fulfilled", 
		    rule.getDegreeModuleToApplyRule().getName(), rule.getMinimumCredits().toString());
	} else {
	    return RuleResult.createFalse(
		    "curricularRules.ruleExecutors.CreditsLimitExecutor.limits.not.fulfilled", 
		    rule.getDegreeModuleToApplyRule().getName(), rule.getMinimumCredits().toString(), rule.getMaximumCredits().toString());
	}
    }

    /**
     * With Rules and Temporary Enrolment
     * 
     * The same as previous, except for
     * 
     * 2.2 If the sum of Ects Credits of Aproved, Enroled and to be enroled Curricular Courses exceeds the Rule's Credits, it 
     * 		returns false.
     * 2.3 Sum the Ects Credits of the enrolments in the Enroled state of the Execution Period previous to this Enrolment Period.
     * 2.4 If the sum of 2.2 and 2.3 exceeds the Rule's limit, mark new enrolments as temporary; return True if otherwise.  
     * 
     */
    
    @Override
    protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(final CurricularRule curricularRule, final EnrolmentContext enrolmentContext) {
	
	final CreditsLimit rule = (CreditsLimit) curricularRule;
	
	if (ruleWasSelectedFromAnyModuleToEnrol(enrolmentContext, curricularRule)) {
	    return evaluateIfCanEnrolToDegreeModule(enrolmentContext, rule);
	} 
	
	if (appliesToCourseGroup(enrolmentContext, rule)) {
	    
	    final CurriculumModule curriculumModule = searchCurriculumModule(enrolmentContext, rule);
	    final ExecutionPeriod executionPeriod = enrolmentContext.getExecutionPeriod();
	    
	    Double ectsCredits = curriculumModule.getAprovedEctsCredits()
		    + curriculumModule.getEnroledEctsCredits(executionPeriod)
		    + calculateEctsCreditsFromToEnrolCurricularCourses(enrolmentContext, rule);
	    
	    if (rule.creditsExceedMaximum(ectsCredits)) {
		return createFalseRuleResult(rule);
	    }
	    
	    ectsCredits = Double.valueOf(ectsCredits.doubleValue()
		    + curriculumModule.getEnroledEctsCredits(executionPeriod.getPreviousExecutionPeriod()).doubleValue());
	    
	    return rule.creditsExceedMaximum(ectsCredits) ? RuleResult.createTrue(EnrolmentResultType.TEMPORARY) : RuleResult.createTrue();
	    
	} else {
	    return RuleResult.createNA();
	}
    }

    @Override
    protected RuleResult executeEnrolmentWithNoRules(final CurricularRule curricularRule, final EnrolmentContext enrolmentContext) {
	return RuleResult.createTrue();
    }
}
