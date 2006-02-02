/*
 * Created on Jan 20, 2006
 */
package net.sourceforge.fenixedu.domain.curricularRules.precedencies;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRuleType;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRuleUtils;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;

public class PrecedenciesFactory extends CurricularRuleUtils {
        
    // Precedencies
    public static CurricularRule createRestrictionDoneDegreeModule(DegreeModule degreeModuleToApplyRule, DegreeModule doneDegreeModule, ExecutionPeriod begin, ExecutionPeriod end, CurricularRuleType ruleType) {        
        return new RestrictionDoneDegreeModule(degreeModuleToApplyRule, doneDegreeModule, begin, end, ruleType);
    }

    public static CurricularRule createRestrictioNotDoneCurricularCourse(DegreeModule degreeModuleToApplyRule, DegreeModule notDoneDegreeModule, ExecutionPeriod begin, ExecutionPeriod end, CurricularRuleType ruleType) {        
        return createCompositeRuleNOT(degreeModuleToApplyRule, begin, end, ruleType, new RestrictionDoneDegreeModule(notDoneDegreeModule));
    }
}
