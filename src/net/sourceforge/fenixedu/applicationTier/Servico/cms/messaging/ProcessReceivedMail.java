

package net.sourceforge.fenixedu.applicationTier.Servico.cms.messaging;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.tools.ant.taskdefs.optional.net.MimeMail;

import net.sourceforge.fenixedu.applicationTier.Servico.cms.CmsService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.cms.IUserGroup;
import net.sourceforge.fenixedu.domain.cms.infrastructure.IMailAddressAlias;
import net.sourceforge.fenixedu.domain.cms.messaging.IMailConversation;
import net.sourceforge.fenixedu.domain.cms.messaging.IMailMessage;
import net.sourceforge.fenixedu.domain.cms.messaging.IMailingList;
import net.sourceforge.fenixedu.domain.cms.messaging.MailConversation;
import net.sourceforge.fenixedu.domain.cms.messaging.MailMessage;
import net.sourceforge.fenixedu.domain.cms.messaging.MailingList;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;
import relations.CmsContents;
import relations.CmsUsers;
import relations.ContentHierarchy;
import relations.ContentOwnership;
import relations.MailingListQueueOutgoingMails;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 13:27:06,19/Out/2005
 * @version $Id$
 */
public class ProcessReceivedMail extends CmsService
{
	public IMailMessage run(MimeMessage message, String messageName, String messageDescription)
			throws FenixServiceException
	{
		IMailMessage mailMessage = null;
		try
		{
			if (message.isMimeType("multipart/*"))
			{
				Multipart mp = (Multipart) message.getContent();
				Multipart cleanedMultipart = removeNonTextAttachments(mp);
				message.setContent(cleanedMultipart);
				message.saveChanges();
			}

			Collection<IMailingList> mailingLists = PersistenceSupportFactory.getDefaultPersistenceSupport().getIPersistentObject().readAll(MailingList.class);
			Address[] messageDestiny = (Address[]) message.getAllRecipients();
			boolean messageCreated = false;
			for (IMailingList mailingList : mailingLists)
			{
				if (this.isMailingListReceipient(messageDestiny, mailingList) && this.senderIsAllowedToSendMessage(message,mailingList))
				{
					if (!messageCreated)
					{
						// we must create the message if AT LEAST one mailing
						// list is receipient (further matches just add the same
						// message instance to the mailing list archive)
						mailMessage = this.createMessage(message);
						mailMessage.setName(messageName);
						mailMessage.setDescription(messageDescription);
						messageCreated = true;
					}
					MailingListQueueOutgoingMails.add(mailingList.getQueue(), mailMessage);
					this.addMailMessageToMailingList(mailMessage, mailingList);
					ContentOwnership.add(mailingList.getOwner(), mailMessage);
				}
			}

		}
		catch (Exception e)
		{
			throw new FenixServiceException(e);
		}

		return mailMessage;
	}

	/**
	 * @param mailMessage
	 * @param mailingList
	 * @return
	 * @throws MessagingException 
	 */
	private boolean senderIsAllowedToSendMessage(MimeMessage mailMessage, IMailingList mailingList) throws MessagingException
	{
		boolean result = false;
		if (mailingList.getMembersOnly())
		{
			Iterator<IUserGroup> groups = mailingList.getGroupsIterator();
			while(groups.hasNext())
			{
				IUserGroup group= groups.next();
				Iterator<IPerson> persons = group.getElementsIterator();
				while(persons.hasNext())
				{
					IPerson person = persons.next();
					Address[] senders = mailMessage.getFrom();
					for (int i = 0; i < senders.length; i++)
					{
						if (senders[i] instanceof InternetAddress)
						{
							InternetAddress address = (InternetAddress) senders[i];
							if (address.getAddress()!=null && address.getAddress().equalsIgnoreCase(person.getEmail()))
							{
								result=true;
								break;
							}
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * @param mailMessage
	 * @param mailingList
	 * @throws MessagingException
	 */
	private void addMailMessageToMailingList(IMailMessage mailMessage, IMailingList mailingList)
			throws MessagingException
	{
		IMailConversation conversation = mailingList.getMostRecentMailConversationOnSubject(mailMessage.getSubject());
		if (conversation == null)
		{
			conversation = new MailConversation();
			String subject = mailMessage.getSubject();
			if (subject == null)
			{
				subject = "";
			}
			conversation.setSubject(subject);
			conversation.setName(conversation.getSubject());
			conversation.setDescription(conversation.getName() + "@"
					+ new Date(System.currentTimeMillis()));
			ContentOwnership.add(mailingList.getOwner(), conversation);
			ContentHierarchy.add(conversation, mailingList);
		}
		ContentHierarchy.add(mailMessage, conversation);
		ContentHierarchy.add(mailMessage, mailingList);
	}

	/**
	 * @param message
	 * @return
	 * @throws MessagingException
	 * @throws IOException
	 */
	private IMailMessage createMessage(MimeMessage message) throws IOException, MessagingException
	{
		MailMessage mailMessage = new MailMessage();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		message.writeTo(baos);
		mailMessage.setBody(baos.toString());
		return mailMessage;

	}

	private boolean isMailingListReceipient(Address[] messageDestiny, IMailingList mailingList)
	{
		return this.isMailingListAddressInReceipients(messageDestiny, mailingList)
				|| this.isMailingListAliasInReceipients(messageDestiny,mailingList);
	}

	private boolean isMailingListAliasInReceipients(Address[] messageDestiny, IMailingList mailingList)
	{		
		boolean result = false;		
		for (IMailAddressAlias alias : mailingList.getAliases())
		{
			StringBuffer buffer = new StringBuffer().append(alias.getAddress()).append("@").append(mailingList.getCms().getConfiguration().getMailingListsHostToUse());
			for (int i = 0; i < messageDestiny.length; i++)
			{
				if (messageDestiny[i] instanceof InternetAddress)
				{
					InternetAddress address = (InternetAddress) messageDestiny[i];
					if (address.getAddress().equals(buffer.toString()))
					{
						result = true;
						break;
					}
				}
			}			
		}

		return result;
	}

	private boolean isMailingListAddressInReceipients(Address[] messageDestiny, IMailingList mailingList)
	{
		StringBuffer buffer = new StringBuffer().append(mailingList.getAddress()).append("@").append(mailingList.getCms().getConfiguration().getMailingListsHostToUse());
		String address = buffer.toString();
		boolean result = false;

		for (int i = 0; i < messageDestiny.length; i++)
		{
			Address recipient = messageDestiny[i];
			if (recipient instanceof InternetAddress)
			{
				if (address.equals(((InternetAddress) recipient).getAddress()))
				{
					result = true;
					break;
				}
			}
		}
		return result;
	}

	private Multipart removeNonTextAttachments(Multipart mp) throws MessagingException, IOException,
			ExcepcaoPersistencia
	{
		for (int i = 0; i < mp.getCount(); i++)
		{
			BodyPart part = mp.getBodyPart(i);
			if (part.isMimeType("multipart/*"))
			{
				// recurse
				removeNonTextAttachments((Multipart) part.getContent());
			}
			else
			{
				if (!part.isMimeType("text/*")
						&& this.readFenixCMS().getConfiguration().getFilterNonTextualAttachmentsToUse())
				{
					BodyPart removalNotification = new MimeBodyPart();
					removalNotification.setHeader("FenixRemovedContent", "NonTextualAttachmentsNotAllowed");
					removalNotification.setText("-----------------------------------");
					mp.addBodyPart(removalNotification);
					mp.removeBodyPart(part);
				}

				if (part.getSize() > this.readFenixCMS().getConfiguration().getMaxAttachmentSizeToUse())
				{
					BodyPart removalNotification = new MimeBodyPart();
					removalNotification.setHeader("FenixRemovedContent", "MessagePartTooLarge");
					removalNotification.setText("-----------------------------------");
					mp.addBodyPart(removalNotification);
					mp.removeBodyPart(part);
				}
			}
		}
		return mp;
	}

	@Override
	protected void updateRootObjectReferences(ServiceRequest request, ServiceResponse response)
			throws FilterException, Exception
	{
		IMailMessage message = (IMailMessage) response.getReturnObject();
		CmsContents.add(this.readFenixCMS(), message);
		CmsUsers.add(this.readFenixCMS(), message.getOwner());
		Iterator<IMailConversation> conversationsInterator = message.getMailConversationsIterator();
		while (conversationsInterator.hasNext())
		{ // TODO: gedl aqui talvez seja necessário verificar se a relação
			// ainda não existe (enviei mail ao João Cachopo em 27-10-2005,
			// 18:35)
			// em caso afirmativo verificar nos outros serviços do CMS ou
			// alterar as classes não base de relação
			IMailConversation conversation = conversationsInterator.next();
			CmsContents.add(message.getCms(), conversation);
		}
	}
}
