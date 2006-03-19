/*
 * Created on Dec 10, 2004
 *
 */
package net.sourceforge.fenixedu.domain.student;

import net.sourceforge.fenixedu.domain.RootDomainObject;


/**
 * @author Luis Egidio, luis.egidio@ist.utl.pt
 *
 */
public class Senior extends Senior_Base {

	public Senior() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

}
