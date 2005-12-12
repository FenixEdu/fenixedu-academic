/**
 * 
 */


package net.sourceforge.fenixedu.persistenceTier.versionedObjects.cms;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.mail.Address;
import javax.mail.internet.InternetAddress;

import net.sourceforge.fenixedu.domain.cms.infrastructure.IMailAddressAlias;
import net.sourceforge.fenixedu.domain.cms.messaging.IMailingList;
import net.sourceforge.fenixedu.domain.cms.messaging.MailingList;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.cms.IPersistentMailingList;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 1:58:13,16/Nov/2005
 * @version $Id$
 */
public class MailingListVO extends VersionedObjectsBase implements IPersistentMailingList
{
	public Collection<IMailingList> readAllMailingListsWithOutgoingMails()
	{
		Collection<IMailingList> lists = readAll(MailingList.class);
		Collection<IMailingList> result = new ArrayList<IMailingList>();
		for (IMailingList list : lists)
		{
			if (list.getQueue().getMessagesCount() > 0)
			{
				result.add(list);
			}
		}

		return result;
	}

	public Collection<IMailingList> readReceptorMailingListsForAddress(Collection<Address> addresses, String mailingListDomain)
			throws ExcepcaoPersistencia
	{
		Collection<IMailingList> lists = readAll(MailingList.class);
		Collection<IMailingList> result = new ArrayList<IMailingList>();
		for (IMailingList mailingList : lists)
		{

			if (this.isMailingListReceipient(addresses, mailingList,mailingListDomain))
			{
				result.add(mailingList);
			}
		}
		return result;
	}

	private boolean isMailingListReceipient(Collection<Address> recipients, IMailingList mailingList,String mailingListDomain)
	{
		return this.isMailingListAddressInReceipients(recipients, mailingList,mailingListDomain)
				|| this.isMailingListAliasInReceipients(recipients, mailingList,mailingListDomain);
	}

	private boolean isMailingListAliasInReceipients(Collection<Address> recipients,
			IMailingList mailingList,String mailingListDomain)
	{
		boolean result = false;
		for (IMailAddressAlias alias : mailingList.getAliases())
		{
			StringBuffer buffer = new StringBuffer().append(alias.getAddress()).append("@").append(mailingListDomain);
			for (Address address : recipients)
			{
				if (address instanceof InternetAddress)
				{
					InternetAddress internetAddress = (InternetAddress) address;
					if (internetAddress.getAddress().equals(buffer.toString()))
					{
						result = true;
						break;
					}
				}
			}
		}

		return result;
	}

	private boolean isMailingListAddressInReceipients(Collection<Address> recipients,
			IMailingList mailingList, String mailingListDomain)
	{
		StringBuffer buffer = new StringBuffer().append(mailingList.getAddress()).append("@").append(mailingListDomain);
		String mailingListAddress = buffer.toString();
		boolean result = false;

		for (Address address : recipients)
		{
			if (address instanceof InternetAddress)
			{
				if (mailingListAddress.equals(((InternetAddress) address).getAddress()))
				{
					result = true;
					break;
				}
			}
		}
		return result;
	}

}
