/*
 * Created on Jan 20, 2006
 */
package net.sourceforge.fenixedu.domain.curricularRules.precedencies;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRuleType;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class RestrictionDoneDegreeModule extends RestrictionDoneDegreeModule_Base {

    protected RestrictionDoneDegreeModule(DegreeModule doneDegreeModule) {
        super();
        if (doneDegreeModule == null) {
            throw new DomainException("curricular.rule.invalid.parameters");
        }
        setDoneDegreeModule(doneDegreeModule);        
    }
    
    public RestrictionDoneDegreeModule(DegreeModule degreeModuleToApplyRule, DegreeModule doneDegreeModule,
            ExecutionPeriod begin, ExecutionPeriod end, CurricularRuleType ruleType) {
        
        this(doneDegreeModule);
        
        if (degreeModuleToApplyRule == null || begin == null || ruleType == null) {
            throw new DomainException("curricular.rule.invalid.parameters");
        }
        setDegreeModuleToApplyRule(degreeModuleToApplyRule);
        setBegin(begin);
        setEnd(end);
        setCurricularRuleType(ruleType);        
    }

    @Override
    public String getLabel() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean evaluate(Class< ? extends DomainObject> object) {
        // TODO Auto-generated method stub
        return false;
    }
}
