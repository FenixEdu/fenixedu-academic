/*
 * Created on 27/Out/2003
 *
 */
package net.sourceforge.fenixedu.domain;

/**
 * Dominio
 * 
 * @author Jo�o Mota 27/Out/2003
 */
public class Coordinator extends Coordinator_Base {

	public Coordinator() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

}
