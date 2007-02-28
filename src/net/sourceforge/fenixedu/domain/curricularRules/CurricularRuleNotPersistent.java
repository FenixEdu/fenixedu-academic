package net.sourceforge.fenixedu.domain.curricularRules;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors.CurricularRuleExecutorFactory;
import net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors.RuleResult;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;

abstract public class CurricularRuleNotPersistent implements ICurricularRule {

    @Override
    public boolean equals(Object obj) {
	CurricularRuleNotPersistent curricularRuleNotPersistent = null;
	if (obj instanceof CurricularRuleNotPersistent) {
	    curricularRuleNotPersistent = (CurricularRuleNotPersistent) obj;
	} else {
	    return false;
	}

	return this.getDegreeModuleToApplyRule() == curricularRuleNotPersistent
		.getDegreeModuleToApplyRule()
		&& this.getCurricularRuleType() == curricularRuleNotPersistent.getCurricularRuleType();
    }

    @Override
    public int hashCode() {
	final StringBuilder builder = new StringBuilder();
	builder.append(String.valueOf(getDegreeModuleToApplyRule().hashCode()));
	builder.append('@');
	builder.append(String.valueOf(getCurricularRuleType().hashCode()));
	return builder.toString().hashCode();
    }

    public boolean appliesToContext(Context context) {
	return context == null || this.appliesToCourseGroup(context.getParentCourseGroup());
    }

    public boolean appliesToCourseGroup(CourseGroup courseGroup) {
	return (this.getContextCourseGroup() == null || this.getContextCourseGroup() == courseGroup);
    }

    public boolean hasContextCourseGroup() {
	return getContextCourseGroup() != null;
    }

    public boolean isCompositeRule() {
	return getCurricularRuleType() == null;
    }

    public boolean isValid(ExecutionPeriod executionPeriod) {
	return (getBegin().isBeforeOrEquals(executionPeriod) && (getEnd() == null || getEnd()
		.isAfterOrEquals(executionPeriod)));
    }

    public boolean isValid(ExecutionYear executionYear) {
	for (ExecutionPeriod executionPeriod : executionYear.getExecutionPeriods()) {
	    if (isValid(executionPeriod)) {
		return true;
	    }
	}
	return false;
    }
    
    public boolean isVisible() {
        return false;
    }

    public RuleResult evaluate(final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, EnrolmentContext enrolmentContext) {
	return CurricularRuleExecutorFactory.findExecutor(this).execute(this, sourceDegreeModuleToEvaluate, enrolmentContext);
    }

}
