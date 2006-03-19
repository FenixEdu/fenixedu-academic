/*
 * Created on Jul 23, 2003
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.domain.Seminaries;

import net.sourceforge.fenixedu.domain.RootDomainObject;


/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at Jul 23, 2003, 9:49:19 AM
 *  
 */
public class Candidacy extends Candidacy_Base {

	public Candidacy() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

}
