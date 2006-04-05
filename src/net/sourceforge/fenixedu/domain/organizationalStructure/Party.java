/*
 * Created on Feb 10, 2006
 *	by mrsp
 */
package net.sourceforge.fenixedu.domain.organizationalStructure;


import net.sourceforge.fenixedu.domain.RootDomainObject;

public abstract class Party extends Party_Base {
    
    public Party() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());        
        setOjbConcreteClass(getClass().getName());
    }
}
