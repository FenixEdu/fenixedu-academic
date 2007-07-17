/*
 * Created on Jan 21, 2004
 */
package net.sourceforge.fenixedu.domain.grant.contract;

import net.sourceforge.fenixedu.domain.RootDomainObject;

/**
 * @author pica
 * @author barbosa
 */
public class GrantProject extends GrantProject_Base {

    public GrantProject() {
	setRootDomainObject(RootDomainObject.getInstance());
    }

}