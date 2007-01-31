package net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors;

import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.Exclusiveness;
import net.sourceforge.fenixedu.domain.enrolment.DegreeModuleToEnrol;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

public class ExclusivenessExecutor extends RuleExecutor {

    @Override
    protected RuleResult executeWithRules(CurricularRule curricularRule, EnrolmentContext enrolmentContext) {
	
	final Exclusiveness rule = (Exclusiveness) curricularRule;
	
	final DegreeModuleToEnrol moduleToEnrol = getDegreeModuleToEnrolFor(enrolmentContext, rule.getDegreeModuleToApplyRule());
	if (moduleToEnrol == null) {
	    throw new DomainException("error.curricularRules.RestrictionDoneDegreeModuleExecutor.cannot.find.degreeModuleToEnrol");
	}
	
	boolean result = true;
	result &= rule.appliesToContext(moduleToEnrol.getContext());
	//result &= enrolmentContext.getStudentCurricularPlan().isApproved((CurricularCourse) rule.get());
	
	return result ? RuleResult.createTrue() : RuleResult.createFalse(new LabelFormatter().appendLabel("reason"));
    }

}
