/**
 * 
 */
package net.sourceforge.fenixedu.domain.cms.messaging.email;

import net.sourceforge.fenixedu.domain.Person;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 15:11:39,3/Abr/2006
 * @version $Id$
 */
public class PersonRecipient extends Recipient {

    private Person subject;

    public PersonRecipient(Person person) {
	this.subject = person;
    }

    @Override
    public EMailAddress getAddress() {
	return new EMailAddress(this.subject.getEmail());
    }

    public Person getSubject() {
	return subject;
    }

    public void setSubject(Person subject) {
	this.subject = subject;
    }

}
