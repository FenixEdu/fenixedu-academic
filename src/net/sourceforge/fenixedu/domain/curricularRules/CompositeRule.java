package net.sourceforge.fenixedu.domain.curricularRules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors.RuleResult;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import dml.runtime.RelationAdapter;

public abstract class CompositeRule extends CompositeRule_Base {
    
    static {
	CurricularRuleCompositeRule.addListener(new RelationAdapter<CompositeRule, CurricularRule>(){
	    @Override
	    public void beforeAdd(CompositeRule compositeRule, CurricularRule curricularRule) {
		if(curricularRule.getNotRule() != null) {
		    throw new DomainException("error.curricular.rule.invalid.state");
		}
	    } 
	});
	
    }
    
    protected CompositeRule() {
    }

    
    protected void initCompositeRule(CurricularRule... curricularRules) {
	if(curricularRules.length < 2) {
	    throw new DomainException("curricular.rule.invalid.parameters");
	}
	
	if(!haveAllSameDegreeModule(curricularRules)) {
	    throw new DomainException("curricular.rule.invalid.parameters");
	}
	
	this.setDegreeModuleToApplyRule(curricularRules[0].getDegreeModuleToApplyRule());
	
	this.setBegin(getBeginExecutionPeriod(curricularRules));
	this.setEnd(getEndExecutionPeriod(curricularRules));
	

	for (CurricularRule rule : curricularRules) {
	    rule.removeDegreeModuleToApplyRule();
	    rule.setParentCompositeRule(this);
	}
    }

    
    
    private ExecutionPeriod getEndExecutionPeriod(CurricularRule[] curricularRules) {
	ExecutionPeriod executionPeriod = null;
	for (CurricularRule rule : curricularRules) {
	    if(rule.getEnd() == null) {
		return null;
	    }
	    if(executionPeriod == null || rule.getEnd().isAfter(executionPeriod)) {
		executionPeriod = rule.getEnd();
	    }
	}
	return executionPeriod;
    }


    private ExecutionPeriod getBeginExecutionPeriod(CurricularRule... curricularRules) {
	ExecutionPeriod executionPeriod = null;
	for (CurricularRule rule : curricularRules) {
	    if(executionPeriod == null || rule.getBegin().isBefore(executionPeriod)) {
		executionPeriod = rule.getBegin();
	    }
	}
	return executionPeriod;
    }


    private boolean haveAllSameDegreeModule(CurricularRule... curricularRules) {
	DegreeModule degreeModule = curricularRules[0].getDegreeModuleToApplyRule();
	for (CurricularRule rule : curricularRules) {
	    if(!rule.getDegreeModuleToApplyRule().equals(degreeModule)) {
		return false;
	    }
	}
	return true;
    }


    @Override
    public abstract List<GenericPair<Object, Boolean>> getLabel();
    
    public List<GenericPair<Object, Boolean>> getLabel(final String operator) {
	final List<GenericPair<Object, Boolean>> labelList = new ArrayList<GenericPair<Object, Boolean>>();
	labelList.add(new GenericPair<Object, Boolean>("( ", false));
	final Iterator<CurricularRule> curricularRulesIterator = getCurricularRules().listIterator();
	while (curricularRulesIterator.hasNext()) {
	    labelList.addAll(curricularRulesIterator.next().getLabel());
	    if (curricularRulesIterator.hasNext()) {
		labelList.add(new GenericPair<Object, Boolean>(" ", false));
		labelList.add(new GenericPair<Object, Boolean>(operator, true));
		labelList.add(new GenericPair<Object, Boolean>(" ", false));
	    }
	}
	labelList.add(new GenericPair<Object, Boolean>(" )", false));
	return labelList;
    }

    @Override
    public RuleResult evaluate(final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
        switch (getCompositeRuleType()) {

        case AND:
            RuleResult result = RuleResult.createTrue();
            for (final CurricularRule curricularRule : getCurricularRules()) {
                result = result.and(curricularRule.evaluate(sourceDegreeModuleToEvaluate, enrolmentContext));
            }
            return result;
        
        case OR:
            RuleResult resultOR = RuleResult.createFalse(null);
            for (final CurricularRule curricularRule : getCurricularRules()) {
                resultOR = resultOR.or(curricularRule.evaluate(sourceDegreeModuleToEvaluate, enrolmentContext));
                if (resultOR.isTrue()) {
                    return resultOR;
                }
            }
            return resultOR;
            
        default:
            throw new DomainException("unsupported.composite.rule");
        }
    }        

    @Override
    public boolean appliesToContext(Context context) {
        for (CurricularRule curricularRule : this.getCurricularRules()) {
            if (!curricularRule.appliesToContext(context)) {
               return false; 
            }
        }
        return true;
    }

    @Override
    protected void removeOwnParameters() {
        for (; !getCurricularRules().isEmpty(); getCurricularRules().get(0).delete());
    }

    @Override
    public boolean isLeaf() {
        return false;
    }
}
