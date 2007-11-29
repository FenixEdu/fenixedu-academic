package net.sourceforge.fenixedu.domain.curricularRules.executors.verifyExecutors;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.curricularRules.Exclusiveness;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.executors.RuleResult;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class ExclusivenessVerifier extends VerifyRuleExecutor {

    @Override
    protected RuleResult verifyEnrolmentWithRules(ICurricularRule curricularRule, EnrolmentContext enrolmentContext,
	    DegreeModule degreeModuleToVerify, CourseGroup rootOrCycleCourseGroup) {

	final Exclusiveness exclusiveness = (Exclusiveness) curricularRule;
	final DegreeModule exclusiveDegreeModule = exclusiveness.getExclusiveDegreeModule();
	final IDegreeModuleToEvaluate degreeModuleToEvaluate = getDegreeModuleToEvaluate(enrolmentContext, exclusiveDegreeModule,
		rootOrCycleCourseGroup);

	if (degreeModuleToEvaluate != null) {
	    if (!degreeModuleToEvaluate.isLeaf()) {
		return RuleResult.createFalse(degreeModuleToVerify);
	    }

	    final CurricularCourse curricularCourse = (CurricularCourse) exclusiveDegreeModule;
	    if (isApproved(enrolmentContext, curricularCourse, rootOrCycleCourseGroup) || degreeModuleToEvaluate.isEnroled()
		    || degreeModuleToEvaluate.isEnroling()) {
		return RuleResult.createFalse(degreeModuleToVerify);
	    }
	}

	return RuleResult.createTrue(degreeModuleToVerify);
    }

    @Override
    protected RuleResult verifyEnrolmentWithTemporaryEnrolment(ICurricularRule curricularRule, EnrolmentContext enrolmentContext,
	    DegreeModule degreeModuleToVerify, CourseGroup rootOrCycleCourseGroup) {

	final Exclusiveness exclusiveness = (Exclusiveness) curricularRule;
	final DegreeModule exclusiveDegreeModule = exclusiveness.getExclusiveDegreeModule();
	final IDegreeModuleToEvaluate degreeModuleToEvaluate = getDegreeModuleToEvaluate(enrolmentContext, exclusiveDegreeModule,
		rootOrCycleCourseGroup);

	if (degreeModuleToEvaluate != null) {
	    if (!degreeModuleToEvaluate.isLeaf()) {
		return RuleResult.createFalse(degreeModuleToVerify);
	    }

	    final CurricularCourse curricularCourse = (CurricularCourse) exclusiveDegreeModule;
	    if (isApproved(enrolmentContext, curricularCourse, rootOrCycleCourseGroup)
		    || hasEnrolmentWithEnroledState(enrolmentContext, curricularCourse, enrolmentContext.getExecutionPeriod()
			    .getPreviousExecutionPeriod())) {
		return RuleResult.createFalse(degreeModuleToVerify);
	    }
	}

	return RuleResult.createTrue(degreeModuleToVerify);
    }

    private IDegreeModuleToEvaluate getDegreeModuleToEvaluate(final EnrolmentContext enrolmentContext,
	    final DegreeModule degreeModule, final CourseGroup rootOrCycleCourseGroup) {
	for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : enrolmentContext
		.getAllChildDegreeModulesToEvaluateFor(rootOrCycleCourseGroup)) {

	    if (degreeModuleToEvaluate.isFor(degreeModule)) {
		return degreeModuleToEvaluate;
	    }

	}

	return null;

    }

    @Override
    protected RuleResult verifyDegreeConclusionWithRules(ICurricularRule curricularRule, EnrolmentContext enrolmentContext,
	    DegreeModule degreeModuleToVerify, CourseGroup rootOrCycleCourseGroup) {

	final Exclusiveness exclusiveness = (Exclusiveness) curricularRule;
	final DegreeModule exclusiveDegreeModule = exclusiveness.getExclusiveDegreeModule();

	if (exclusiveDegreeModule.isCourseGroup()) {
	    if (isEnrolledIn(enrolmentContext, (CourseGroup) exclusiveDegreeModule)) {
		return RuleResult.createFalse(degreeModuleToVerify);
	    }
	} else if (exclusiveDegreeModule.isCurricularCourse()) {
	    if (isApproved(enrolmentContext, (CurricularCourse) exclusiveDegreeModule, rootOrCycleCourseGroup)) {
		return RuleResult.createFalse(degreeModuleToVerify);
	    }
	} else {
	    throw new DomainException(
		    "error.net.sourceforge.fenixedu.domain.curricularRules.executors.verifyExecutors.invalid.degree.module.to.verify");
	}

	return RuleResult.createTrue(degreeModuleToVerify);
    }
}
