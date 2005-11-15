/**
 * 
 */


package net.sourceforge.fenixedu.applicationTier.Filtro.cms.accessControl;


import java.util.ArrayList;
import java.util.Collection;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.FilterParameters;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 17:09:57,14/Nov/2005
 * @version $Id$
 */
public class SenderIsInFenixBD extends CmsAccessControlFilter
{

	public void execute(ServiceRequest arg0, ServiceResponse arg1, FilterParameters arg2)
			throws FilterException, Exception
	{
		MimeMessage message = (MimeMessage) arg0.getServiceParameters().getParameter(0);
		Collection<String> emailsToTest = new ArrayList<String>();
		for (int i = 0; i < message.getFrom().length; i++)
		{
			if (message.getFrom()[i] instanceof InternetAddress)
			{
				InternetAddress from = (InternetAddress) message.getFrom()[i];
				if (from.getAddress() != null)
				{
					emailsToTest.add(from.getAddress());
				}
			}
		}
		boolean emailInBD = PersistenceSupportFactory.getDefaultPersistenceSupport().getIPessoaPersistente().emailOwnedByFenixPerson(emailsToTest);		
		if (!emailInBD)
		{
			throw new EmailNotInDatabase();
		}
	}
}
