/*
 * Created on 2004/08/30
 *
 */
package net.sourceforge.fenixedu.domain.support;

import net.sourceforge.fenixedu.domain.RootDomainObject;

/**
 * @author Luis Cruz
 * 
 */
public class GlossaryEntry extends GlossaryEntry_Base {

    public GlossaryEntry() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
	removeRootDomainObject();
	deleteDomainObject();
    }

}
