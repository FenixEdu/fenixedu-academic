/*
 * Created on 24/Jul/2003
 *
 */
package net.sourceforge.fenixedu.domain.onlineTests;

import net.sourceforge.fenixedu.domain.RootDomainObject;

/**
 * @author Susana Fernandes
 */
public class Question extends Question_Base {

    public Question() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	public void delete() {
        removeMetadata();
        removeRootDomainObject();
        super.deleteDomainObject();
    }

}
