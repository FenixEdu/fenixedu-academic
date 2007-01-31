package net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.RestrictionEnroledDegreeModule;
import net.sourceforge.fenixedu.domain.enrolment.DegreeModuleToEnrol;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

public class RestrictionEnroledDegreeModuleExecutor extends RuleExecutor {

    @Override
    protected RuleResult executeWithRules(CurricularRule curricularRule, EnrolmentContext enrolmentContext) {
	
	final RestrictionEnroledDegreeModule rule = (RestrictionEnroledDegreeModule) curricularRule;
	final DegreeModuleToEnrol moduleToEnrol = getDegreeModuleToEnrolFor(enrolmentContext, rule.getDegreeModuleToApplyRule());
	if (moduleToEnrol == null) {
	    throw new DomainException("error.curricularRules.RestrictionEnroledDegreeModuleExecutor.cannot.find.degreeModuleToEnrol");
	}
	
	boolean result = true;
	result &= rule.appliesToContext(moduleToEnrol.getContext());
	result &= (enrolmentContext.getStudentCurricularPlan().isEnroledInExecutionPeriod(
		   (CurricularCourse) rule.getPrecedenceDegreeModule(),enrolmentContext.getExecutionPeriod()) 
		|| enrolmentContext.getStudentCurricularPlan().isEnroledInExecutionPeriod(
		   (CurricularCourse) rule.getPrecedenceDegreeModule(), enrolmentContext.getExecutionPeriod().getPreviousExecutionPeriod()));
	
	return result ? RuleResult.createTrue() : RuleResult.createFalse(new LabelFormatter().appendLabel("reason"));
    }

}
