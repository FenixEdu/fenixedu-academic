package net.sourceforge.fenixedu.domain.curricularRules.executors.verifyExecutors;

import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.RestrictionEnroledDegreeModule;
import net.sourceforge.fenixedu.domain.curricularRules.executors.RuleResult;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;

public class RestrictionEnroledDegreeModuleVerifier extends VerifyRuleExecutor {

    @Override
    protected RuleResult verify(ICurricularRule curricularRule, EnrolmentContext enrolmentContext,
	    DegreeModule degreeModuleToVerify, CourseGroup parentCourseGroup) {

	final RestrictionEnroledDegreeModule restrictionEnroledDegreeModule = (RestrictionEnroledDegreeModule) curricularRule;

	if (isApproved(enrolmentContext, restrictionEnroledDegreeModule.getPrecedenceDegreeModule(), parentCourseGroup)) {
	    return RuleResult.createTrue(degreeModuleToVerify);
	}

	for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : enrolmentContext
		.getAllChildDegreeModulesToEvaluateFor(parentCourseGroup)) {

	    if (degreeModuleToEvaluate.isLeaf()
		    && degreeModuleToEvaluate.isFor(restrictionEnroledDegreeModule.getPrecedenceDegreeModule())) {
		return RuleResult.createTrue(degreeModuleToVerify);
	    }
	}

	return RuleResult.createFalse(degreeModuleToVerify);

    }

    @Override
    protected RuleResult verifyWithTemporaryEnrolment(ICurricularRule curricularRule, EnrolmentContext enrolmentContext,
	    DegreeModule degreeModuleToVerify, CourseGroup parentCourseGroup) {
	return verify(curricularRule, enrolmentContext, degreeModuleToVerify, parentCourseGroup);
    }

}
