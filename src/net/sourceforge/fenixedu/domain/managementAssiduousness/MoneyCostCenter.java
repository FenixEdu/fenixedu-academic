/*
 * Created on 11/Dez/2004
 */
package net.sourceforge.fenixedu.domain.managementAssiduousness;

import net.sourceforge.fenixedu.domain.RootDomainObject;


/**
 * @author T�nia Pous�o
 *
 */
public class MoneyCostCenter extends MoneyCostCenter_Base {

	public MoneyCostCenter() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

}

