package net.sourceforge.fenixedu.domain;

/**
 * @author mrsp
 */

public abstract class Identification extends Identification_Base {
    
    public Identification() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setOjbConcreteClass(getClass().getName());        
    }          
}
