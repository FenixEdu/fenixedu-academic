/*
 * Created on 19/Ago/2003
 *
 */

package net.sourceforge.fenixedu.domain.onlineTests;

import net.sourceforge.fenixedu.domain.RootDomainObject;

/**
 * @author Susana Fernandes
 */

public class DistributedTestAdvisory extends DistributedTestAdvisory_Base {

	public DistributedTestAdvisory() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

}
