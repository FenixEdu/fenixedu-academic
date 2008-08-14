/**
 * 
 */
package net.sourceforge.fenixedu.domain.cms.messaging.email;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.cms.messaging.email.Recipient.SendStatus;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 15:04:08,21/Mar/2006
 * @version $Id$
 */
public class SendMailReport {

    private Map<SendStatus, Collection<Person>> personReport = new HashMap<SendStatus, Collection<Person>>();
    private Map<SendStatus, Collection<EMailAddress>> addressReport = new HashMap<SendStatus, Collection<EMailAddress>>();

    public void add(SendStatus status, Person person) {
	Collection<Person> currentValues = this.personReport.get(status);
	if (currentValues == null) {
	    currentValues = new ArrayList<Person>();
	    this.personReport.put(status, currentValues);
	}
	currentValues.add(person);
    }

    public void add(SendStatus status, EMailAddress address) {
	Collection<EMailAddress> currentValues = this.addressReport.get(status);
	if (currentValues == null) {
	    currentValues = new ArrayList<EMailAddress>();
	    this.addressReport.put(status, currentValues);
	}
	currentValues.add(address);
    }

    public Collection<Person> getPersonReport(SendStatus status) {
	return this.personReport.get(status);
    }

    public Collection<EMailAddress> getAddressReport(SendStatus status) {
	return this.addressReport.get(status);
    }

}
