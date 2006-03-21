/*
 * Created on Apr 13, 2005
 *
 */
package net.sourceforge.fenixedu.domain.research.result;

import net.sourceforge.fenixedu.domain.RootDomainObject;


/**
 * @author Ricardo Rodrigues
 *
 */

public class PublicationTypeAttribute extends PublicationTypeAttribute_Base {

	public PublicationTypeAttribute() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

}
