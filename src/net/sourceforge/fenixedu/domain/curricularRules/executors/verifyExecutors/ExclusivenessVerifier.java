package net.sourceforge.fenixedu.domain.curricularRules.executors.verifyExecutors;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.curricularRules.Exclusiveness;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.executors.RuleResult;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;

public class ExclusivenessVerifier extends VerifyRuleExecutor {

    @Override
    protected RuleResult verify(ICurricularRule curricularRule, EnrolmentContext enrolmentContext,
	    DegreeModule degreeModuleToVerify, CourseGroup parentCourseGroup) {

	final Exclusiveness exclusiveness = (Exclusiveness) curricularRule;
	final DegreeModule exclusiveDegreeModule = exclusiveness.getExclusiveDegreeModule();
	final IDegreeModuleToEvaluate degreeModuleToEvaluate = getDegreeModuleToEvaluate(enrolmentContext, exclusiveDegreeModule,
		parentCourseGroup);

	if (degreeModuleToEvaluate != null) {
	    if (!degreeModuleToEvaluate.isLeaf()) {
		return RuleResult.createFalse(degreeModuleToVerify);
	    }

	    final CurricularCourse curricularCourse = (CurricularCourse) exclusiveDegreeModule;
	    if (isApproved(enrolmentContext, curricularCourse, parentCourseGroup) || degreeModuleToEvaluate.isEnroled()
		    || degreeModuleToEvaluate.isEnroling()) {
		return RuleResult.createFalse(degreeModuleToVerify);
	    }
	}

	return RuleResult.createTrue(degreeModuleToVerify);
    }

    @Override
    protected RuleResult verifyWithTemporaryEnrolment(ICurricularRule curricularRule, EnrolmentContext enrolmentContext,
	    DegreeModule degreeModuleToVerify, CourseGroup parentCourseGroup) {

	final Exclusiveness exclusiveness = (Exclusiveness) curricularRule;
	final DegreeModule exclusiveDegreeModule = exclusiveness.getExclusiveDegreeModule();
	final IDegreeModuleToEvaluate degreeModuleToEvaluate = getDegreeModuleToEvaluate(enrolmentContext, exclusiveDegreeModule,
		parentCourseGroup);

	if (degreeModuleToEvaluate != null) {
	    if (!degreeModuleToEvaluate.isLeaf()) {
		return RuleResult.createFalse(degreeModuleToVerify);
	    }

	    final CurricularCourse curricularCourse = (CurricularCourse) exclusiveDegreeModule;
	    if (isApproved(enrolmentContext, curricularCourse, parentCourseGroup)
		    || hasEnrolmentWithEnroledState(enrolmentContext, curricularCourse, enrolmentContext.getExecutionPeriod()
			    .getPreviousExecutionPeriod())) {
		return RuleResult.createFalse(degreeModuleToVerify);
	    }
	}

	return RuleResult.createTrue(degreeModuleToVerify);
    }

    private IDegreeModuleToEvaluate getDegreeModuleToEvaluate(final EnrolmentContext enrolmentContext,
	    final DegreeModule degreeModule, final CourseGroup parentCourseGroup) {
	for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : enrolmentContext
		.getAllChildDegreeModulesToEvaluateFor(parentCourseGroup)) {

	    if (degreeModuleToEvaluate.isFor(degreeModule)) {
		return degreeModuleToEvaluate;
	    }

	}

	return null;

    }

}
