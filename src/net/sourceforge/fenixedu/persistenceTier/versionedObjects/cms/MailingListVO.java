/**
 * 
 */


package net.sourceforge.fenixedu.persistenceTier.versionedObjects.cms;


import java.util.ArrayList;
import java.util.Collection;

import javax.mail.Address;
import javax.mail.internet.InternetAddress;

import net.sourceforge.fenixedu.domain.cms.infrastructure.MailAddressAlias;
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
	public Collection<MailingList> readAllMailingListsWithOutgoingMails()
	{
		Collection<MailingList> lists = readAll(MailingList.class);
		Collection<MailingList> result = new ArrayList<MailingList>();
		for (MailingList list : lists)
		{
			if (list.getQueue().getMessagesCount() > 0)
			{
				result.add(list);
			}
		}

		return result;
	}

	public Collection<MailingList> readReceptorMailingListsForAddress(Collection<Address> addresses, String mailingListDomain)
			throws ExcepcaoPersistencia
	{
		Collection<MailingList> lists = readAll(MailingList.class);
		Collection<MailingList> result = new ArrayList<MailingList>();
		for (MailingList mailingList : lists)
		{

			if (this.isMailingListReceipient(addresses, mailingList,mailingListDomain))
			{
				result.add(mailingList);
			}
		}
		return result;
	}

	private boolean isMailingListReceipient(Collection<Address> recipients, MailingList mailingList,String mailingListDomain)
	{
		return this.isMailingListAddressInReceipients(recipients, mailingList,mailingListDomain)
				|| this.isMailingListAliasInReceipients(recipients, mailingList,mailingListDomain);
	}

	private boolean isMailingListAliasInReceipients(Collection<Address> recipients,
			MailingList mailingList,String mailingListDomain)
	{
		boolean result = false;
		for (MailAddressAlias alias : mailingList.getAliases())
		{
			StringBuilder buffer = new StringBuilder().append(alias.getAddress()).append("@").append(mailingListDomain);
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
			MailingList mailingList, String mailingListDomain)
	{
		StringBuilder buffer = new StringBuilder().append(mailingList.getAddress()).append("@").append(mailingListDomain);
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
