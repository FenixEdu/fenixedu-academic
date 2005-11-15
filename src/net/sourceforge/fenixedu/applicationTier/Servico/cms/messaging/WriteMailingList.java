/**
 * 
 */


package net.sourceforge.fenixedu.applicationTier.Servico.cms.messaging;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import net.sourceforge.fenixedu.applicationTier.Servico.cms.CmsService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.cms.IUserGroup;
import net.sourceforge.fenixedu.domain.cms.infrastructure.IMailAddressAlias;
import net.sourceforge.fenixedu.domain.cms.infrastructure.MailAddressAlias;
import net.sourceforge.fenixedu.domain.cms.infrastructure.MailQueue;
import net.sourceforge.fenixedu.domain.cms.messaging.IMailingList;
import net.sourceforge.fenixedu.domain.cms.messaging.MailingList;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;
import relations.CmsContents;
import relations.CmsUsers;
import relations.ContentOwnership;
import relations.GroupMailingList;
import relations.MailingListAlias;
import relations.MailingListQueue;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 16:27:21,18/Out/2005
 * @version $Id$
 */
public class WriteMailingList extends CmsService
{
	public IMailingList run(String name, String description, String address, Collection<String> aliasAdresses,Collection<IUserGroup> mailingListGroups,boolean membersOnly, boolean replyToList,IPerson owner)
			throws FenixServiceException, ExcepcaoPersistencia	
	{
		MailingList mailingList = new MailingList();
		MailQueue queue = new MailQueue();
		mailingList.setName(name);
		mailingList.setDescription(description);
		mailingList.setAddress(address);
		mailingList.setClosed(false);
		mailingList.setMembersOnly(membersOnly);
		mailingList.setReplyToList(replyToList);
		
		ContentOwnership.add(owner, mailingList);
		MailingListQueue.add(mailingList, queue);
		Collection<IMailAddressAlias> aliases = new ArrayList<IMailAddressAlias>();
		for (String aliasAddress : aliasAdresses)
		{
			IMailAddressAlias existingAlias = PersistenceSupportFactory.getDefaultPersistenceSupport().getIPersistentMailAdressAlias().readByAddress(aliasAddress);
			if (existingAlias ==null)
			{
				existingAlias = new MailAddressAlias();
				existingAlias.setAddress(address);
			}
			aliases.add(existingAlias);			
		}
		for (IMailAddressAlias alias : aliases)
		{
			MailingListAlias.add(mailingList,alias);			
		}
		for (IUserGroup group : mailingListGroups)
		{
			GroupMailingList.add(mailingList,group);
		}

		return mailingList;
	}

	protected void updateRootObjectReferences(ServiceRequest request, ServiceResponse response)
			throws FilterException, Exception
	{
		IMailingList mailingList = (IMailingList) response.getReturnObject();
		IPerson owner = (IPerson) request.getServiceParameters().getParameter(7); //ok, I know, this is ugly. Who in the world wrote BERSERK ?? ;)		
		CmsUsers.add(this.readFenixCMS(),owner);
		CmsContents.add(this.readFenixCMS(),mailingList);
	}
}
