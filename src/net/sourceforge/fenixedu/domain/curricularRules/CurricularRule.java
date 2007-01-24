package net.sourceforge.fenixedu.domain.curricularRules;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.util.LogicOperators;

public abstract class CurricularRule extends CurricularRule_Base {
 
    protected CurricularRule() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setOjbConcreteClass(getClass().getName());        
    }
    
    protected void edit(ExecutionPeriod beginExecutionPeriod, ExecutionPeriod endExecutionPeriod) {
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

    public boolean appliesToContext(Context context) {
        return this.appliesToCourseGroup(context);
    }
    
    private boolean appliesToCourseGroup(Context context) {
        return (this.getContextCourseGroup() == null || this.getContextCourseGroup() == context.getParentCourseGroup());
    }
    
    public boolean isCompositeRule() {
        return getCurricularRuleType() == null;
    }
    
    protected boolean belongsToCompositeRule() {        
        return (getParentCompositeRule() != null);
    }
    
    @Override
    public ExecutionPeriod getBegin() {
        return belongsToCompositeRule() ? getParentCompositeRule().getBegin() : super.getBegin();
    }

    @Override
    public ExecutionPeriod getEnd() {
        return belongsToCompositeRule() ? getParentCompositeRule().getEnd() : super.getEnd();
    }

    @Override
    public DegreeModule getDegreeModuleToApplyRule() {
        return belongsToCompositeRule() ? getParentCompositeRule()
                .getDegreeModuleToApplyRule() : super.getDegreeModuleToApplyRule();
    }

    @Override
    public CourseGroup getContextCourseGroup() {
        return belongsToCompositeRule() ? getParentCompositeRule().getContextCourseGroup()
                : super.getContextCourseGroup();
    }
    
    public boolean isValid(ExecutionPeriod executionPeriod) {
    	return (getBegin().isBeforeOrEquals(executionPeriod) && (getEnd() == null || getEnd().isAfterOrEquals(executionPeriod)));
    }
    
    public boolean isValid(ExecutionYear executionYear) {
        for (ExecutionPeriod executionPeriod : executionYear.getExecutionPeriods()) {
            if(isValid(executionPeriod)) {
                return true;
            }
        }
        return false;
    }

    protected void checkExecutionPeriods(ExecutionPeriod beginExecutionPeriod, ExecutionPeriod endExecutionPeriod) {
        if (endExecutionPeriod != null && beginExecutionPeriod.isAfter(endExecutionPeriod)) {
            throw new DomainException("curricular.rule.begin.is.after.end.execution.period");
        }
    }
    

    public static CurricularRule createCurricularRule(LogicOperators logicOperator, CurricularRule... curricularRules) {
	switch (logicOperator) {
	case AND:
	    return new AndRule(curricularRules);
	case OR:
	    return new OrRule(curricularRules);
	case NOT:
	    if(curricularRules.length != 1) {
		throw new DomainException("error.invalid.notRule.parameters");
	    }
	    return new NotRule(curricularRules[0]);
	default:
	    throw new DomainException("error.unsupported.logic.operator");
	}
    }
    
    protected abstract void removeOwnParameters();
    public abstract boolean isLeaf();    
    public abstract List<GenericPair<Object, Boolean>> getLabel();
    public abstract RuleResult evaluate(EnrolmentContext enrolmentContext);
}
