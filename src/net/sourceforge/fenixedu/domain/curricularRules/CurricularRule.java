/*
 * Created on Jan 20, 2006
 */
package net.sourceforge.fenixedu.domain.curricularRules;

import net.sourceforge.fenixedu.domain.DomainObject;

public abstract class CurricularRule extends CurricularRule_Base {
 
    protected CurricularRule() {
        super();
        setOjbConcreteClass(getClass().getName());        
    }
    
    public abstract String getLabel();
    public abstract boolean evaluate(Class<? extends DomainObject> object);
}
