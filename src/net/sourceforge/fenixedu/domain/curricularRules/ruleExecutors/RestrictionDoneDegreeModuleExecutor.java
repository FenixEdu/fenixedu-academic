package net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.RestrictionDoneDegreeModule;
import net.sourceforge.fenixedu.domain.enrolment.DegreeModuleToEnrol;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

public class RestrictionDoneDegreeModuleExecutor extends RuleExecutor {

    @Override
    protected RuleResult executeWithRules(CurricularRule curricularRule, EnrolmentContext enrolmentContext) {
	
	final RestrictionDoneDegreeModule rule = (RestrictionDoneDegreeModule) curricularRule;
	
	final DegreeModuleToEnrol moduleToEnrol = getDegreeModuleToEnrolFor(enrolmentContext, rule.getDegreeModuleToApplyRule());
	if (moduleToEnrol == null) {
	    throw new DomainException("error.curricularRules.RestrictionDoneDegreeModuleExecutor.cannot.find.degreeModuleToEnrol");
	}
	
	if (!rule.appliesToContext(moduleToEnrol.getContext())) {
	    return RuleResult.createFalse(new LabelFormatter().appendLabel("rule.doesnot.apply.to.context"));
	}
	if (!enrolmentContext.getStudentCurricularPlan().isApproved((CurricularCourse) rule.getPrecedenceDegreeModule())) {
	    return RuleResult.createFalse(new LabelFormatter().appendLabel("rule.student.is.not.approved.to.precendenceDegreeModule"));
	}
	
	return RuleResult.createTrue();
    }

}
