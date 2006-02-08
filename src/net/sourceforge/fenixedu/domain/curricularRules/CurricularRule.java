/*
 * Created on Jan 20, 2006
 */
package net.sourceforge.fenixedu.domain.curricularRules;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.DomainObject;

public abstract class CurricularRule extends CurricularRule_Base {
 
    protected CurricularRule() {
        super();
        setOjbConcreteClass(getClass().getName());        
    }
    
    public abstract List<GenericPair<Object, Boolean>> getLabel();
    public abstract boolean evaluate(Class<? extends DomainObject> object);
    
    public void delete() {
        removeDegreeModuleToApplyRule();
        super.deleteDomainObject();
    }
}
