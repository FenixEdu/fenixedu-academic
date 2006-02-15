package net.sourceforge.fenixedu.domain.curricularRules;

public abstract class Rule extends Rule_Base {
    
    protected boolean belongsToCompositeRule() {        
        return (getParentCompositeRule() != null);
    }
}
