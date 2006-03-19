/*
 * Created on 6/Jan/2004
 *  
 */
package net.sourceforge.fenixedu.domain;

import org.apache.struts.util.MessageResources;

/**
 * @author T�nia Pous�o
 * 
 */
public class PaymentPhase extends PaymentPhase_Base {

    public PaymentPhase() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	public String getDescriptionFromMessageResourses() {
        MessageResources messages = MessageResources
            .getMessageResources("resources.ApplicationResources");

        String newDescription = null;
        newDescription = messages.getMessage(super.getDescription());
        if (newDescription == null) {
            newDescription = super.getDescription();
        }
        return newDescription;
    }

}
