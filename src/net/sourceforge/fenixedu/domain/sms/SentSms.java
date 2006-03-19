/*
 * Created on 7/Jun/2004
 *  
 */
package net.sourceforge.fenixedu.domain.sms;

import net.sourceforge.fenixedu.domain.RootDomainObject;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 */
public class SentSms extends SentSms_Base {

	public SentSms() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

}