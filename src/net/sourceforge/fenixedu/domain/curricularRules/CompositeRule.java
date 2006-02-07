/*
 * Created on Jan 20, 2006
 */
package net.sourceforge.fenixedu.domain.curricularRules;

import java.util.Iterator;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.util.LogicOperators;

public class CompositeRule extends CompositeRule_Base {

    public CompositeRule(DegreeModule degreeModuleToApplyRule, ExecutionPeriod begin, ExecutionPeriod end,
            CurricularRuleType ruleType, LogicOperators compositeRuleType, CurricularRule ... curricularRules) {
        
        super();

        if (degreeModuleToApplyRule == null || begin == null || ruleType == null) {
            throw new DomainException("curricular.rule.invalid.parameters");
        }
        
        if (compositeRuleType.equals(LogicOperators.NOT) && curricularRules.length > 1) {
            throw new DomainException("incorrect.NOT.composite.rule.use");
        }
        else if ((compositeRuleType.equals(LogicOperators.AND) || compositeRuleType.equals(LogicOperators.OR)) && curricularRules.length < 2) {
            throw new DomainException("incorrect.composite.rule.use");
        }
        else if (!compositeRuleType.equals(LogicOperators.AND) && !compositeRuleType.equals(LogicOperators.OR) && !compositeRuleType.equals(LogicOperators.NOT)) {
            throw new DomainException("unsupported.composite.rule");
        }
        initCompositeRule(degreeModuleToApplyRule, begin, end, ruleType, compositeRuleType, curricularRules);  
    }
    
    private void initCompositeRule(DegreeModule degreeModuleToApplyRule, ExecutionPeriod begin, ExecutionPeriod end,
            CurricularRuleType ruleType, LogicOperators compositeRuleType, CurricularRule ... curricularRules) {
        
        setDegreeModuleToApplyRule(degreeModuleToApplyRule);
        setBegin(begin);
        setEnd(end);
        setCurricularRuleType(ruleType);
        
        setCompositeRuleType(compositeRuleType);        

        for (final CurricularRule curricularRule : curricularRules) {
            curricularRule.setParentCompositeRule(this);
        }
    }

    @Override
    public String getLabel() {
        final StringBuilder stringBuilder = new StringBuilder();        
        if (getCompositeRuleType().equals(LogicOperators.NOT)) {
            stringBuilder.append(getCurricularRules().get(0).getLabel());
        } else {
            String operator = "";
            if (getCompositeRuleType().equals(LogicOperators.AND)) {
                operator = " e "; //TODO: For test purpose only
            } else if (getCompositeRuleType().equals(LogicOperators.OR)) {
                operator = " ou "; //TODO: For test purpose only
            } else {
                throw new DomainException("unsupported.composite.rule");
            }            
            final Iterator<CurricularRule> curricularRulesIterator = getCurricularRules().listIterator();
            while (curricularRulesIterator.hasNext()) {
                stringBuilder.append(curricularRulesIterator.next().getLabel());
                if (curricularRulesIterator.hasNext()) {
                    stringBuilder.append(operator);
                }
            }
        }
        return stringBuilder.toString();
    }

    @Override
    public boolean evaluate(Class< ? extends DomainObject> object) {        
        boolean result = true;        
        if (getCompositeRuleType().equals(LogicOperators.NOT)) {
            result = !getCurricularRules().get(0).evaluate(object);                       
        } else if (getCompositeRuleType().equals(LogicOperators.AND)) {            
            for (final CurricularRule curricularRule : getCurricularRules()) {
                result &= curricularRule.evaluate(object);
                if (!result) {
                    return false;
                }
            }            
        } else if (getCompositeRuleType().equals(LogicOperators.OR)) {
            for (final CurricularRule curricularRule : getCurricularRules()) {
                result |= curricularRule.evaluate(object);
            }            
        } else {
            throw new DomainException("unsupported.composite.rule");
        }        
        return result;
    }
    
    @Override
    public void delete() {
        removeDegreeModuleToApplyRule();
        for (; !getCurricularRules().isEmpty(); getCurricularRules().get(0).delete());
        super.deleteDomainObject();
    }
}
