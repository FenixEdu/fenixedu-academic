/**
 * 
 */


package net.sourceforge.fenixedu.persistenceTier.OJB.cms;


import java.util.Collection;
import java.util.List;

import javax.mail.Address;
import javax.mail.internet.InternetAddress;

import net.sourceforge.fenixedu.domain.cms.messaging.MailingList;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.cms.IPersistentMailingList;

import org.apache.ojb.broker.query.QueryBySQL;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 1:55:15,16/Nov/2005
 * @version $Id$
 */
public class PersistentMailingListOJB extends PersistentObjectOJB implements IPersistentMailingList
{
	public Collection<MailingList> readAllMailingListsWithOutgoingMails() throws ExcepcaoPersistencia
	{
		QueryBySQL query = new QueryBySQL(MailingList.class, "select CMS_MAILING_LIST.* from CMS_MAILING_LIST inner join CMS_MAIL_QUEUE on CMS_MAILING_LIST.KEY_QUEUE=CMS_MAIL_QUEUE.ID_INTERNAL inner join CMS_MAIL_MESSAGE_MAIL_QUEUE on CMS_MAIL_MESSAGE_MAIL_QUEUE.KEY_MAIL_QUEUE=CMS_MAIL_QUEUE.ID_INTERNAL");
		try
		{
			return (List) getCurrentPersistenceBroker().getCollectionByQuery(query);
		}
		catch (Exception x)
		{
			throw new ExcepcaoPersistencia("readAllMailingListsWithOutgointMails: could not execute query");
		}

	}

	public Collection<MailingList> readReceptorMailingListsForAddress(Collection<Address> addresses, String mailingListDomain)
			throws ExcepcaoPersistencia
	{
		StringBuilder sqlQueryAliases= new StringBuilder();
		StringBuilder sqlQueryAddresses = new StringBuilder();
		sqlQueryAliases.append("select CMS_MAILING_LIST.* from CMS_MAILING_LIST inner join CMS_MAIL_ADDRESS_ALIAS_MAILING_LIST on CMS_MAILING_LIST.ID_INTERNAL=CMS_MAIL_ADDRESS_ALIAS_MAILING_LIST.KEY_MAILING_LIST inner join CMS_MAIL_ADDRESS_ALIAS on CMS_MAIL_ADDRESS_ALIAS.ID_INTERNAL=CMS_MAIL_ADDRESS_ALIAS.ID_INTERNAL ");
		sqlQueryAddresses.append("select CMS_MAILING_LIST.* from CMS_MAILING_LIST ");
		boolean firstAddress = true;
		for (Address address : addresses)
		{
			if (address instanceof InternetAddress)
			{
				InternetAddress internetAddress = (InternetAddress) address;
				if (internetAddress.getAddress() != null 
						&& mailingListDomain.equals(internetAddress.getAddress().substring(internetAddress.getAddress().indexOf("@")+1)))
				{
					String addressWithoutHost = internetAddress.getAddress().subSequence(0, internetAddress.getAddress().indexOf("@")).toString();
					if (firstAddress)
					{
						firstAddress = false;
						sqlQueryAliases.append("where ").append(" CMS_MAIL_ADDRESS_ALIAS.ADDRESS=").append("\"").append(addressWithoutHost).append("\" ");
						sqlQueryAddresses.append("where ").append(" CMS_MAILING_LIST.ADDRESS=").append("\"").append(addressWithoutHost).append("\" ");
					}
					else
					{
						sqlQueryAliases.append("or ").append(" CMS_MAIL_ADDRESS_ALIAS.ADDRESS=").append("\"").append(addressWithoutHost).append("\" ");
						sqlQueryAddresses.append("or ").append(" CMS_MAILING_LIST.ADDRESS=").append("\"").append(addressWithoutHost).append("\" ");
					}
					
				}
			}
		}
		try
		{
			QueryBySQL query = new QueryBySQL(MailingList.class, sqlQueryAliases.append(" UNION ").append(sqlQueryAddresses).toString());
			return (List) getCurrentPersistenceBroker().getCollectionByQuery(query);
		}
		catch (Exception x)
		{
			throw new ExcepcaoPersistencia("readAllMailingListsWithOutgointMails: could not execute query");
		}
	}

}
