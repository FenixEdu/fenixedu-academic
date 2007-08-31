package net.sourceforge.fenixedu.domain.curricularRules.executors.verifyExecutors;

import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.executors.RuleResult;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.person.RoleType;

public class EnrolmentToBeApprovedByCoordinatorVerifier extends VerifyRuleExecutor {

    @Override
    protected RuleResult verify(ICurricularRule curricularRule, EnrolmentContext enrolmentContext,
	    DegreeModule degreeModuleToVerify, CourseGroup parentCourseGroup) {

	if (enrolmentContext.getResponsiblePerson().hasRole(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE)) {
	    return RuleResult.createTrue(degreeModuleToVerify);
	}

	return RuleResult.createFalse(degreeModuleToVerify);
    }

    @Override
    protected RuleResult verifyWithTemporaryEnrolment(ICurricularRule curricularRule, EnrolmentContext enrolmentContext,
	    DegreeModule degreeModuleToVerify, CourseGroup parentCourseGroup) {
	return verify(curricularRule, enrolmentContext, degreeModuleToVerify, parentCourseGroup);
    }

}
