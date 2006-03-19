/*
 * Created on 27/Out/2003
 * 
 */
package net.sourceforge.fenixedu.domain.grant.owner;

import net.sourceforge.fenixedu.domain.RootDomainObject;


/**
 * @author Barbosa
 * @author Pica
 *  
 */
public class GrantOwner extends GrantOwner_Base {

	public GrantOwner() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

}
