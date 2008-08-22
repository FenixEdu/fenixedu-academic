package net.sourceforge.fenixedu.domain.curricularRules;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.curricularRules.executors.RuleResult;
import net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors.CurricularRuleExecutorFactory;
import net.sourceforge.fenixedu.domain.curricularRules.executors.verifyExecutors.VerifyRuleLevel;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.util.LogicOperator;

import org.joda.time.YearMonthDay;

public abstract class CurricularRule extends CurricularRule_Base implements ICurricularRule {

    protected CurricularRule() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    protected void init(final DegreeModule degreeModuleToApplyRule, final CourseGroup contextCourseGroup,
	    final ExecutionSemester begin, final ExecutionSemester end, final CurricularRuleType type) {

	// TODO assure only one rule of a certain type for a given execution
	// period

	init(degreeModuleToApplyRule, contextCourseGroup, begin, end);
	checkCurricularRuleType(type);
	setCurricularRuleType(type);
    }

    private void checkCurricularRuleType(final CurricularRuleType type) {
	if (type == null) {
	    throw new DomainException("curricular.rule.invalid.parameters");
	}
    }

    protected void init(final DegreeModule degreeModuleToApplyRule, final CourseGroup contextCourseGroup,
	    final ExecutionSemester begin, final ExecutionSemester end) {

	checkParameters(degreeModuleToApplyRule, begin);
	checkExecutionPeriods(begin, end);
	setDegreeModuleToApplyRule(degreeModuleToApplyRule);
	setContextCourseGroup(contextCourseGroup);
	setBegin(begin);
	setEnd(end);
    }

    protected void checkParameters(final DegreeModule degreeModuleToApplyRule, final ExecutionSemester begin) {
	if (degreeModuleToApplyRule == null || begin == null) {
	    throw new DomainException("curricular.rule.invalid.parameters");
	}
	if (degreeModuleToApplyRule.isRoot()) {
	    throw new DomainException("curricular.rule.invalid.parameters");
	}
    }

    protected void edit(ExecutionSemester beginExecutionPeriod, ExecutionSemester endExecutionPeriod) {
	checkExecutionPeriods(beginExecutionPeriod, endExecutionPeriod);
	setBegin(beginExecutionPeriod);
	setEnd(endExecutionPeriod);
    }

    public void delete() {
	removeOwnParameters();
	removeCommonParameters();
	removeRootDomainObject();
	super.deleteDomainObject();
    }

    protected void removeCommonParameters() {
	removeDegreeModuleToApplyRule();
	removeBegin();
	removeEnd();
	removeParentCompositeRule();
	removeContextCourseGroup();
	removeRootDomainObject();
    }

    public boolean appliesToContext(final Context context) {
	return context == null || appliesToCourseGroup(context.getParentCourseGroup());
    }

    public boolean appliesToCourseGroup(final CourseGroup courseGroup) {
	return (!hasContextCourseGroup() || getContextCourseGroup() == courseGroup);
    }

    public boolean isCompositeRule() {
	return getCurricularRuleType() == null;
    }

    protected boolean belongsToCompositeRule() {
	return hasParentCompositeRule();
    }

    @Override
    public ExecutionSemester getBegin() {
	return belongsToCompositeRule() ? getParentCompositeRule().getBegin() : super.getBegin();
    }

    @Override
    public ExecutionSemester getEnd() {
	return belongsToCompositeRule() ? getParentCompositeRule().getEnd() : super.getEnd();
    }

    @Override
    public DegreeModule getDegreeModuleToApplyRule() {
	return belongsToCompositeRule() ? getParentCompositeRule().getDegreeModuleToApplyRule() : super
		.getDegreeModuleToApplyRule();
    }

    @Override
    public CourseGroup getContextCourseGroup() {
	return belongsToCompositeRule() ? getParentCompositeRule().getContextCourseGroup() : super.getContextCourseGroup();
    }

    public boolean hasContextCourseGroup(final CourseGroup parent) {
	return getContextCourseGroup() == parent;
    }
    
    @Override
    public boolean hasCurricularRuleType(final CurricularRuleType ruleType) {
        return getCurricularRuleType() == ruleType;
    }

    public boolean isValid(ExecutionSemester executionSemester) {
	return (getBegin().isBeforeOrEquals(executionSemester) && (getEnd() == null || getEnd()
		.isAfterOrEquals(executionSemester)));
    }

    public boolean isValid(ExecutionYear executionYear) {
	for (ExecutionSemester executionSemester : executionYear.getExecutionPeriods()) {
	    if (isValid(executionSemester)) {
		return true;
	    }
	}
	return false;
    }

    public boolean isActive() {
	return !hasEnd() || getEnd().containsDay(new YearMonthDay());
    }

    protected void checkExecutionPeriods(ExecutionSemester beginExecutionPeriod, ExecutionSemester endExecutionPeriod) {
	if (endExecutionPeriod != null && beginExecutionPeriod.isAfter(endExecutionPeriod)) {
	    throw new DomainException("curricular.rule.begin.is.after.end.execution.period");
	}
    }

    public boolean isVisible() {
	return true;
    }

    public RuleResult evaluate(final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
	return CurricularRuleExecutorFactory.findExecutor(this).execute(this, sourceDegreeModuleToEvaluate, enrolmentContext);
    }

    public RuleResult verify(final VerifyRuleLevel level, final EnrolmentContext enrolmentContext,
	    final DegreeModule degreeModuleToVerify, final CourseGroup parentCourseGroup) {
	return createVerifyRuleExecutor().verify(this, level, enrolmentContext, degreeModuleToVerify, parentCourseGroup);
    }

    abstract protected void removeOwnParameters();

    abstract public boolean isLeaf();

    abstract public List<GenericPair<Object, Boolean>> getLabel();

    static public CurricularRule createCurricularRule(final LogicOperator logicOperator, final CurricularRule... curricularRules) {
	switch (logicOperator) {
	case AND:
	    return new AndRule(curricularRules);
	case OR:
	    return new OrRule(curricularRules);
	case NOT:
	    if (curricularRules.length != 1) {
		throw new DomainException("error.invalid.notRule.parameters");
	    }
	    return new NotRule(curricularRules[0]);
	default:
	    throw new DomainException("error.unsupported.logic.operator");
	}
    }

}
