package net.sourceforge.fenixedu.domain.curricularRules.executors.verifyExecutors;

import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.RestrictionDoneDegreeModule;
import net.sourceforge.fenixedu.domain.curricularRules.executors.RuleResult;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;

public class RestrictionDoneDegreeModuleVerifier extends VerifyRuleExecutor {

    @Override
    protected RuleResult verifyEnrolmentWithRules(ICurricularRule curricularRule, EnrolmentContext enrolmentContext,
	    DegreeModule degreeModuleToVerify, CourseGroup parentCourseGroup) {

	final RestrictionDoneDegreeModule restrictionDoneDegreeModule = (RestrictionDoneDegreeModule) curricularRule;

	if (isApproved(enrolmentContext, restrictionDoneDegreeModule.getPrecedenceDegreeModule(), parentCourseGroup)) {
	    return RuleResult.createTrue(degreeModuleToVerify);
	}

	return RuleResult.createFalse(degreeModuleToVerify);
    }

    @Override
    protected RuleResult verifyEnrolmentWithTemporaryEnrolment(ICurricularRule curricularRule, EnrolmentContext enrolmentContext,
	    DegreeModule degreeModuleToVerify, CourseGroup parentCourseGroup) {
	final RestrictionDoneDegreeModule restrictionDoneDegreeModule = (RestrictionDoneDegreeModule) curricularRule;

	if (isApproved(enrolmentContext, restrictionDoneDegreeModule.getPrecedenceDegreeModule(), parentCourseGroup)
		|| hasEnrolmentWithEnroledState(enrolmentContext, restrictionDoneDegreeModule.getPrecedenceDegreeModule(),
			enrolmentContext.getExecutionPeriod().getPreviousExecutionPeriod())) {
	    return RuleResult.createTrue(degreeModuleToVerify);
	}

	return RuleResult.createFalse(degreeModuleToVerify);

    }

}
