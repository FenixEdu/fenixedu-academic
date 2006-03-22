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
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>
 * <br/>
 * <br/>
 * <br/>
 * Created on 15:04:08,21/Mar/2006
 * @version $Id$
 */
public class SendMailReport {

	private Map<SendStatus,Collection<Person>> report = new HashMap<SendStatus,Collection<Person>>();
	
	public void add(SendStatus status,Person person)
	{
		Collection<Person> currentValues = this.report.get(status);
		if (currentValues == null)
		{
			currentValues = new ArrayList<Person>();
			this.report.put(status,currentValues);
		}
		currentValues.add(person);
	}
	
	public Collection<Person> get(SendStatus status)
	{
		return this.report.get(status);
	}
	
}
