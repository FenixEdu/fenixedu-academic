package net.sourceforge.fenixedu.domain.curricularRules;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.util.LogicOperator;

public class OrRule extends OrRule_Base {
    
    public  OrRule(CurricularRule... curricularRules) {
	initCompositeRule(curricularRules);
	setCompositeRuleType(LogicOperator.OR);
    }

    @Override
    public List<GenericPair<Object, Boolean>> getLabel() {
	return getLabel("label.operator.or");
    }    
}
