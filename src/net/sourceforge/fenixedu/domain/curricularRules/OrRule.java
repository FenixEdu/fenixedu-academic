package net.sourceforge.fenixedu.domain.curricularRules;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.curricularRules.executors.RuleResult;
import net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors.EnrolmentResultType;
import net.sourceforge.fenixedu.domain.curricularRules.executors.verifyExecutors.VerifyRuleLevel;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.util.LogicOperator;

public class OrRule extends OrRule_Base {

    public OrRule(CurricularRule... curricularRules) {
	initCompositeRule(curricularRules);
	setCompositeRuleType(LogicOperator.OR);
    }

    @Override
    public List<GenericPair<Object, Boolean>> getLabel() {
	return getLabel("label.operator.or");
    }

    @Override
    public RuleResult evaluate(IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, EnrolmentContext enrolmentContext) {
	RuleResult resultOR = RuleResult.createFalse(EnrolmentResultType.NULL, sourceDegreeModuleToEvaluate.getDegreeModule());
	for (final CurricularRule curricularRule : getCurricularRules()) {
	    resultOR = resultOR.or(curricularRule.evaluate(sourceDegreeModuleToEvaluate, enrolmentContext));
	    if (resultOR.isTrue() && resultOR.isValidated(sourceDegreeModuleToEvaluate.getDegreeModule())) {
		return RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());
	    }
	}
	return resultOR;
    }

    @Override
    public RuleResult verify(VerifyRuleLevel verifyRuleLevel, EnrolmentContext enrolmentContext,
	    DegreeModule degreeModuleToVerify, CourseGroup parentCourseGroup) {
	RuleResult resultOR = RuleResult.createFalse(EnrolmentResultType.NULL, degreeModuleToVerify);
	for (final CurricularRule curricularRule : getCurricularRules()) {
	    resultOR = resultOR.or(curricularRule.verify(verifyRuleLevel, enrolmentContext, degreeModuleToVerify,
		    parentCourseGroup));
	    if (resultOR.isTrue() && resultOR.isValidated(degreeModuleToVerify)) {
		return RuleResult.createTrue(degreeModuleToVerify);
	    }
	}
	return resultOR;
    }
}
