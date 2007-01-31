package net.sourceforge.fenixedu.domain.curricularRules;

import java.util.List;

import dml.runtime.RelationAdapter;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors.RuleResult;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class NotRule extends NotRule_Base {
    
    static {
	CurricularRuleNotRule.addListener(new RelationAdapter<NotRule, CurricularRule>() {
	    @Override
	    public void beforeAdd(NotRule notRule, CurricularRule curricularRule) {
	        if(curricularRule.getParentCompositeRule() != null) {
	            throw new DomainException("error.curricular.rule.invalid.state");
	        }
	    }
	});
    }
    
    public NotRule(CurricularRule rule) {
	if(rule == null || rule.getParentCompositeRule() != null) {
	    throw new DomainException("curricular.rule.invalid.parameters");
	}
	
	setDegreeModuleToApplyRule(rule.getDegreeModuleToApplyRule());
	rule.removeDegreeModuleToApplyRule();
	setBegin(rule.getBegin());
	setEnd(rule.getEnd());
        setWrappedRule(rule);
    }

    @Override
    public List<GenericPair<Object, Boolean>> getLabel() {
	return getWrappedRule().getLabel();
    }

    @Override
    public boolean isLeaf() {
	return false;
    }

    @Override
    protected void removeOwnParameters() {
	getWrappedRule().delete();
    }
}
