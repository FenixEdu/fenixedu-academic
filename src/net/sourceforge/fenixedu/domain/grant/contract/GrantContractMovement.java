/*
 * Created on 3/Jul/2004
 * 
 */
package net.sourceforge.fenixedu.domain.grant.contract;

import net.sourceforge.fenixedu.domain.RootDomainObject;


/**
 * @author Barbosa
 * @author Pica
 *  
 */
public class GrantContractMovement extends GrantContractMovement_Base {

	public GrantContractMovement() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

}