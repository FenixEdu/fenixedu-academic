package net.sourceforge.fenixedu.domain.curricularRules.executors.verifyExecutors;

import net.sourceforge.fenixedu.domain.curricularRules.EvenOddRule;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.executors.RuleResult;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;

public class EvenOddRuleVerifier extends VerifyRuleExecutor {

    @Override
    protected RuleResult verifyEnrolmentWithRules(ICurricularRule curricularRule, EnrolmentContext enrolmentContext,
	    DegreeModule degreeModuleToVerify, CourseGroup parentCourseGroup) {
	final EvenOddRule evenOddRule = (EvenOddRule) curricularRule;

	if (evenOddRule.getCurricularPeriodOrder().equals(enrolmentContext.getExecutionPeriod().getSemester())) {
	    if (evenOddRule.getEven() && ((enrolmentContext.getRegistration().getStudent().getNumber().intValue() & 1) == 0)
		    || !evenOddRule.getEven()
		    && ((enrolmentContext.getRegistration().getStudent().getNumber().intValue() & 1) != 0)) {
		return RuleResult.createTrue(degreeModuleToVerify);
	    }

	    return RuleResult.createFalse(degreeModuleToVerify);
	} else {
	    return RuleResult.createNA(degreeModuleToVerify);
	}
    }

    @Override
    protected RuleResult verifyEnrolmentWithTemporaryEnrolment(ICurricularRule curricularRule, EnrolmentContext enrolmentContext,
	    DegreeModule degreeModuleToVerify, CourseGroup parentCourseGroup) {
	return verifyEnrolmentWithRules(curricularRule, enrolmentContext, degreeModuleToVerify, parentCourseGroup);
    }

}
