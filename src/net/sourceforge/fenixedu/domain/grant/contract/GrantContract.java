/*
 * Created on 18/Nov/2003
 * 
 */
package net.sourceforge.fenixedu.domain.grant.contract;

import net.sourceforge.fenixedu.domain.RootDomainObject;



/**
 * @author Barbosa
 * @author Pica
 *  
 */
public class GrantContract extends GrantContract_Base {

	public GrantContract() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

}