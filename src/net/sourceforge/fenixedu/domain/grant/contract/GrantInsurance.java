/*
 * Created on Jun 26, 2004
 */
package net.sourceforge.fenixedu.domain.grant.contract;

import net.sourceforge.fenixedu.domain.RootDomainObject;


/**
 * @author Barbosa
 * @author Pica
 */
public class GrantInsurance extends GrantInsurance_Base {

	public GrantInsurance() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

}