/*
 * Created on 20/Nov/2003
 * 
 */
package net.sourceforge.fenixedu.domain.grant.contract;

import net.sourceforge.fenixedu.domain.RootDomainObject;


/**
 * @author Barbosa
 * @author Pica
 *  
 */
public class GrantOrientationTeacher extends GrantOrientationTeacher_Base {

	public GrantOrientationTeacher() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

}