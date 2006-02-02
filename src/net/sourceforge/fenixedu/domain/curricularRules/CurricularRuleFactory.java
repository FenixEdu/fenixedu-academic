/*
 * Created on Jan 20, 2006
 */
package net.sourceforge.fenixedu.domain.curricularRules;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.precedencies.PrecedenciesFactory;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;

public class CurricularRuleFactory extends CurricularRuleUtils {
        
    public static CurricularRule createRestrictionDoneDegreeModule(DegreeModule degreeModuleToApplyRule, DegreeModule doneDegreeModule, ExecutionPeriod begin, ExecutionPeriod end, CurricularRuleType ruleType) {
        return PrecedenciesFactory.createRestrictionDoneDegreeModule(degreeModuleToApplyRule, doneDegreeModule, begin, end, ruleType);
    }
    
    public static CurricularRule createRestrictioNotDoneCurricularCourse(DegreeModule degreeModuleToApplyRule, DegreeModule notDoneDegreeModule, ExecutionPeriod begin, ExecutionPeriod end, CurricularRuleType ruleType) {
        return PrecedenciesFactory.createRestrictioNotDoneCurricularCourse(degreeModuleToApplyRule, notDoneDegreeModule, begin, end, ruleType);
    }
}
