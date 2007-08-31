package net.sourceforge.fenixedu.domain.curricularRules.executors.verifyExecutors;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.executors.RuleResult;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;

abstract public class VerifyRuleExecutor {

    private static class NullVerifyExecutor extends VerifyRuleExecutor {

	private NullVerifyExecutor() {
	}

	@Override
	protected RuleResult verify(ICurricularRule curricularRule, EnrolmentContext enrolmentContext,
		DegreeModule degreeModuleToVerify, CourseGroup parentCourseGroup) {
	    return RuleResult.createNA(degreeModuleToVerify);
	}

	@Override
	protected RuleResult verifyWithTemporaryEnrolment(ICurricularRule curricularRule, EnrolmentContext enrolmentContext,
		DegreeModule degreeModuleToVerify, CourseGroup parentCourseGroup) {
	    return RuleResult.createNA(degreeModuleToVerify);
	}
    }

    public static final VerifyRuleExecutor NULL_VERIFY_EXECUTOR = new NullVerifyExecutor();

    final public RuleResult verify(ICurricularRule curricularRule, final VerifyRuleLevel verifyRuleLevel,
	    final EnrolmentContext enrolmentContext, final DegreeModule degreeModuleToVerify, final CourseGroup parentCourseGroup) {
	if (verifyRuleLevel == VerifyRuleLevel.WITH_RULES) {
	    return verify(curricularRule, enrolmentContext, degreeModuleToVerify, parentCourseGroup);
	} else if (verifyRuleLevel == VerifyRuleLevel.WITH_RULES_AND_TEMPORARY) {
	    return verifyWithTemporaryEnrolment(curricularRule, enrolmentContext, degreeModuleToVerify, parentCourseGroup);
	} else {
	    throw new DomainException(
		    "error.net.sourceforge.fenixedu.domain.curricularRules.executors.verifyExecutors.VerifyRuleExecutor.invalid.verify.level");
	}
    }

    protected boolean isApproved(final EnrolmentContext enrolmentContext, final CurricularCourse curricularCourse,
	    final CourseGroup parentCourseGroup) {
	final CurriculumGroup curriculumGroup = enrolmentContext.getStudentCurricularPlan().findCurriculumGroupFor(
		parentCourseGroup);
	return curriculumGroup != null ? curriculumGroup.isApproved(curricularCourse) : false;
    }

    protected boolean hasEnrolmentWithEnroledState(final EnrolmentContext enrolmentContext,
	    final CurricularCourse curricularCourse, final ExecutionPeriod executionPeriod) {
	return enrolmentContext.getStudentCurricularPlan().getRoot().hasEnrolmentWithEnroledState(curricularCourse,
		executionPeriod);
    }

    abstract protected RuleResult verify(ICurricularRule curricularRule, EnrolmentContext enrolmentContext,
	    DegreeModule degreeModuleToVerify, CourseGroup parentCourseGroup);

    abstract protected RuleResult verifyWithTemporaryEnrolment(ICurricularRule curricularRule, EnrolmentContext enrolmentContext,
	    DegreeModule degreeModuleToVerify, CourseGroup parentCourseGroup);

}
