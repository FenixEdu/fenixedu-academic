/*
 * Created on 10/Set/2003
 *
 */
package net.sourceforge.fenixedu.domain.onlineTests;

import net.sourceforge.fenixedu.domain.RootDomainObject;

/**
 * @author Susana Fernandes
 */
public class StudentTestLog extends StudentTestLog_Base {

	public StudentTestLog() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

}
