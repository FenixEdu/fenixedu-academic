/*
 * Created on Feb 1, 2006
 */
package net.sourceforge.fenixedu.domain.curricularRules;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.util.LogicOperators;

public class CurricularRuleUtils {
    
    public static CurricularRule createCompositeRuleNOT(DegreeModule degreeModuleToApplyRule, ExecutionPeriod begin, ExecutionPeriod end, CurricularRuleType ruleType, CurricularRule curricularRule) {
        return new CompositeRule(degreeModuleToApplyRule, begin, end, ruleType, LogicOperators.NOT, curricularRule);
    }
}


/*
public static CurricularRule createCompositeRuleAND(CurricularRule firstCurricularRule, CurricularRule secondCurricularRule) {
    return new CompositeRule(LogicOperators.AND, firstCurricularRule, secondCurricularRule);
}

public static CurricularRule createCompositeRuleAND(CurricularRule ... curricularRules) {
    return createCompositeRuleAND(Arrays.asList(curricularRules));
}

public static CurricularRule createCompositeRuleOR(CurricularRule firstCurricularRule, CurricularRule secondCurricularRule) {
    return new CompositeRule(LogicOperators.OR, firstCurricularRule, secondCurricularRule);
}

public static CurricularRule createCompositeRuleOR(CurricularRule ... curricularRules) {
    return createCompositeRuleOR(Arrays.asList(curricularRules));
}

public static CurricularRule createCompositeRuleNOT(CurricularRule curricularRule) {
    return new CompositeRule(LogicOperators.NOT, curricularRule, null);
}

private static CurricularRule createCompositeRuleAND(final List<CurricularRule> curricularRules) {
    if (curricularRules.size() == 2) {
        return createCompositeRuleAND(curricularRules.get(0), curricularRules.get(1));
    } else if (curricularRules.size() > 2) {                        
        return createCompositeRuleAND(curricularRules.get(0), 
                createCompositeRuleAND(curricularRules.subList(1, curricularRules.size())));
    }
    throw new DomainException("invalid.curricular.rules.number");
}

private static CurricularRule createCompositeRuleOR(final List<CurricularRule> curricularRules) {
    if (curricularRules.size() == 2) {
        return createCompositeRuleOR(curricularRules.get(0), curricularRules.get(1));            
    } else if (curricularRules.size() > 2) {                        
        return createCompositeRuleOR(curricularRules.get(0), 
                createCompositeRuleOR(curricularRules.subList(1, curricularRules.size())));
    }
    throw new DomainException("invalid.curricular.rules.number");
}
*/
