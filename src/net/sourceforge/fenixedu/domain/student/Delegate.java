/*
 * Created on Feb 18, 2004
 *  
 */
package net.sourceforge.fenixedu.domain.student;

import net.sourceforge.fenixedu.domain.RootDomainObject;


/**
 * @author <a href="mailto:lesa@mega.ist.utl.pt">Leonor Almeida </a>
 * @author <a href="mailto:shmc@mega.ist.utl.pt">Sergio Montelobo </a>
 * 
 */
public class Delegate extends Delegate_Base {

	public Delegate() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	public void delete() {
		removeExecutionYear();
		removeStudent();
		removeDegree();
		deleteDomainObject();
	}

}
