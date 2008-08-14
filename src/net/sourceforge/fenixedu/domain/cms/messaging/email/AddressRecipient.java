/**
 * 
 */
package net.sourceforge.fenixedu.domain.cms.messaging.email;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 15:11:45,3/Abr/2006
 * @version $Id$
 */
public class AddressRecipient extends Recipient {

    private EMailAddress address;

    public AddressRecipient(EMailAddress address) {
	this.address = address;
    }

    public EMailAddress getAddress() {
	return address;
    }

    public void setAddress(EMailAddress address) {
	this.address = address;
    }
}
