

package net.sourceforge.fenixedu.applicationTier.Servico.cms.messaging;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

import net.sourceforge.fenixedu.applicationTier.Servico.cms.CmsService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.NotImplementedException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.cms.messaging.MailConversation;
import net.sourceforge.fenixedu.domain.cms.messaging.MailMessage;
import net.sourceforge.fenixedu.domain.cms.messaging.MailingList;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 13:27:06,19/Out/2005
 * @version $Id$
 */
public class ProcessReceivedMail extends CmsService
{
	public MailMessage run(MimeMessage message, String messageName, String messageDescription) throws FenixServiceException, ExcepcaoPersistencia
	{
		MailMessage mailMessage = null;
		Person creator=null;
		try
		{
			if (message.isMimeType("multipart/*"))
			{
				Multipart mp = (Multipart) message.getContent();
				Multipart cleanedMultipart = removeNonTextAttachments(mp);
				message.setContent(cleanedMultipart);
				message.saveChanges();
			}

			// I must convert this into an array because code generator crashes
			// if a DAO method has an array parameter
			Collection<Address> addressesList = new ArrayList<Address>();
			Address[] addresses = message.getAllRecipients();
			for (int i = 0; i < addresses.length; i++)
			{
				addressesList.add(addresses[i]);
			}
            
			//Collection<MailingList> mailingLists = persistentSupport.getIPersistentMailingList().readReceptorMailingListsForAddress(addressesList, this.readFenixCMS().getConfiguration().getMailingListsHostToUse());
            List<MailingList> mailingLists = new ArrayList<MailingList>();
                         
			boolean messageCreated = false;
			boolean firstMailingList = true;
			if (mailingLists.size() > 0)
			{				
				mailMessage = this.createMessage(message);
				mailMessage.setName(messageName);
				mailMessage.setDescription(messageDescription);
				messageCreated = true;
			}
			for (MailingList mailingList : mailingLists)
			{
				if (firstMailingList)
				{
					//I assume that the creator of the message (and possibly the conversation) is the creator of the first receipient mailinglist
					creator=mailingList.getCreator();
					firstMailingList = false;
				}
				if (this.senderIsAllowedToSendMessage(message, mailingList))
				{
					if (!messageCreated)
					{
						// the very same message will be added to all
						// mailing lists
						// so we must create only one instance
						// the creation occur when one sender who can send to
						// this mailinglist is found
						// the same message is added to all subsequent
						// mailingLists
					}
					mailingList.getQueue().addMessages(mailMessage);
					this.addMailMessageToMailingList(mailMessage, mailingList, creator);
				}
			}

		}
		catch (Exception e)
		{
			throw new FenixServiceException(e);
		}

		this.updateRootObjectReferences(mailMessage);		
        
        //return mailMessage;
        
        throw new NotImplementedException();
	}

	/**
	 * @param mailMessage
	 * @param mailingList
	 * @return
	 * @throws MessagingException
	 */
	private boolean senderIsAllowedToSendMessage(MimeMessage mailMessage, MailingList mailingList) throws MessagingException {
		if (! mailingList.getMembersOnly()) {
            // FIXME: must check if the email exists is a valid fenix person email, that is, exists in the database
            return true;
        }
        
		Group group = mailingList.getGroup();
        
		for(Person person: group.getElements())
		{
			Address[] senders = mailMessage.getFrom();
            
			for (int i = 0; i < senders.length; i++) {
				if (senders[i] instanceof InternetAddress)
				{
					InternetAddress address = (InternetAddress) senders[i];
					if (address.getAddress() != null
							&& address.getAddress().equalsIgnoreCase(person.getEmail()))
					{
						return true;
					}
				}
			}
		}
        
        return false;
	}

	/**
	 * @param mailMessage
	 * @param mailingList
	 * @throws MessagingException
	 */
	private void addMailMessageToMailingList(MailMessage mailMessage, MailingList mailingList,
			Person creator) throws MessagingException
	{
		MailConversation conversation = mailingList.getMostRecentMailConversationOnSubject(mailMessage.getSubject());
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
			mailingList.addChildren(conversation);
			conversation.setCreator(creator);
		}
		mailMessage.setCreator(creator);
		conversation.addChildren(mailMessage);
		mailingList.addChildren(mailMessage);
	}

	/**
	 * @param message
	 * @return
	 * @throws MessagingException
	 * @throws IOException
	 */
	private MailMessage createMessage(MimeMessage message) throws IOException, MessagingException
	{
		MailMessage mailMessage = new MailMessage();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		message.writeTo(baos);
		mailMessage.setBody(baos.toString());
		return mailMessage;

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
				if ((!part.isMimeType("text/*") && !part.isMimeType("mime-forward"))
						&& this.readFenixCMS().getConfiguration().getFilterNonTextualAttachmentsToUse())
				{
					BodyPart removalNotification = new MimeBodyPart();
					removalNotification.setHeader("FenixRemovedContent", "NonTextualAttachmentsNotAllowed");
					removalNotification.setText("(Attachment Removed: Attachments Not Allowed)");
					mp.addBodyPart(removalNotification);
					mp.removeBodyPart(part);
				}

				if (part.getSize() > this.readFenixCMS().getConfiguration().getMaxAttachmentSizeToUse())
				{
					BodyPart removalNotification = new MimeBodyPart();
					removalNotification.setHeader("FenixRemovedContent", "MessagePartTooLarge");
					removalNotification.setText("(Attachment Removed: Attachment Too Large)");
					mp.addBodyPart(removalNotification);
					mp.removeBodyPart(part);
				}
			}
		}
		return mp;
	}

	protected void updateRootObjectReferences(MailMessage message) throws ExcepcaoPersistencia
	{
            this.readFenixCMS().addContents(message);
            this.readFenixCMS().addUsers(message.getCreator());
            Iterator<MailConversation> conversationsInterator = message.getMailConversationsIterator();
            while (conversationsInterator.hasNext())
		{ // TODO: gedl aqui talvez seja necessário verificar se a relação
                    // ainda não existe (enviei mail ao João Cachopo em 27-10-2005,
                    // 18:35)
                    // em caso afirmativo verificar nos outros serviços do CMS ou
                    // alterar as classes não base de relação
                    MailConversation conversation = conversationsInterator.next();
                    message.getCms().addContents(conversation);
		}
	}
}
