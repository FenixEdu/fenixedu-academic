package net.sourceforge.fenixedu.domain.curricularRules;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.curricularRules.executors.RuleResult;
import net.sourceforge.fenixedu.domain.curricularRules.executors.verifyExecutors.VerifyRuleLevel;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.util.LogicOperator;

public class AndRule extends AndRule_Base {

    public AndRule(CurricularRule... curricularRules) {
	initCompositeRule(curricularRules);
	setCompositeRuleType(LogicOperator.AND);
    }

    @Override
    public List<GenericPair<Object, Boolean>> getLabel() {
	return getLabel("label.operator.and");
    }

    @Override
    public RuleResult evaluate(IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, EnrolmentContext enrolmentContext) {
	RuleResult result = RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());
	for (final CurricularRule curricularRule : getCurricularRules()) {
	    result = result.and(curricularRule.evaluate(sourceDegreeModuleToEvaluate, enrolmentContext));
	}
	return result;
    }

    @Override
    public RuleResult verify(VerifyRuleLevel verifyRuleLevel, EnrolmentContext enrolmentContext,
	    DegreeModule degreeModuleToVerify, CourseGroup parentCourseGroup) {
	RuleResult result = RuleResult.createTrue(degreeModuleToVerify);
	for (final CurricularRule curricularRule : getCurricularRules()) {
	    result = result
		    .and(curricularRule.verify(verifyRuleLevel, enrolmentContext, degreeModuleToVerify, parentCourseGroup));
	}
	return result;
    }

}
