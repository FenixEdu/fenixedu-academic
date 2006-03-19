/*
 * Created on 19/Nov/2003
 * 
 */
package net.sourceforge.fenixedu.domain.grant.contract;

import net.sourceforge.fenixedu.domain.RootDomainObject;


/**
 * @author Barbosa
 * @author Pica
 *  
 */
public class GrantType extends GrantType_Base {

	public GrantType() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

}