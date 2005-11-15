

package net.sourceforge.fenixedu.domain.cms.messaging;


import java.util.Comparator;
import java.util.Iterator;

import javax.mail.MessagingException;

import net.sourceforge.fenixedu.commons.OrderedIterator;
import net.sourceforge.fenixedu.domain.cms.predicates.ContentAssignableClassPredicate;

import org.apache.commons.collections.iterators.FilterIterator;
import org.apache.commons.lang.StringUtils;

public class MailConversation extends MailConversation_Base
{

	private class LastMessagesFirstComparator implements Comparator
	{
		public int compare(Object arg0, Object arg1)
		{
			IMailMessage message1 = (IMailMessage) arg0;
			IMailMessage message2 = (IMailMessage) arg1;
			return - message1.getCreationDate().compareTo(message2.getCreationDate());
		}
	}
	public MailConversation()
	{
		super();
	}
	public boolean isAboutSubject(final String subject)
	{
		boolean result = false;
		if (subject != null && this.getSubject() != null)
		{
			String[] replyMarkers = this.getOwner().getCms().getConfiguration().getConversationReplyMarkersToUse();
			String thisConversationSubject = this.getSubject().trim();
			String targetSubject = new String(subject);
			if (this.subjectIndicatesReply(subject))
			{
				for (int i = 0; i < replyMarkers.length; i++)
				{
					targetSubject = targetSubject.replaceFirst(replyMarkers[i], "");
				}

				targetSubject = targetSubject.trim();
				result = targetSubject.equalsIgnoreCase(thisConversationSubject);
			}
		}
		return result;
	}

	/**
	 * This checks if the provided subject starts with one of the configure
	 * reply markers (see CmsConfiguration)
	 * 
	 * @param subject
	 * @return wether this subject indicates a reply
	 */
	private boolean subjectIndicatesReply(String subject)
	{
		boolean result = false;
		int index = StringUtils.indexOfAny(subject.trim(), this.getOwner().getCms().getConfiguration().getConversationReplyMarkers());
		result = (index == 0);

		return result;
	}

	@SuppressWarnings("unchecked")
	public Iterator<IMailMessage> getMailMessagesIterator()
	{
		return new OrderedIterator(new FilterIterator(this.getChildrenIterator(), new ContentAssignableClassPredicate(IMailMessage.class)),new LastMessagesFirstComparator());
	}

	public IMailMessage getLastReply()
	{
		Iterator<IMailMessage> messageIterator = this.getMailMessagesIterator();
		IMailMessage mailMessage = null;

		while (messageIterator.hasNext())
		{
			IMailMessage currentMessage = messageIterator.next();
			if (mailMessage == null
					|| mailMessage.getCreationDate().before(currentMessage.getCreationDate()))
			{
				mailMessage = currentMessage;
			}
		}
		return mailMessage;
	}

	public IMailMessage getFirstMessage()
	{
		Iterator<IMailMessage> messageIterator = this.getMailMessagesIterator();
		IMailMessage mailMessage = null;

		while (messageIterator.hasNext())
		{
			IMailMessage currentMessage = messageIterator.next();
			if (mailMessage == null
					|| currentMessage.getCreationDate().before(mailMessage.getCreationDate()))
			{
				mailMessage = currentMessage;
			}
		}
		return mailMessage;
	}

	public int getMailMessagesCount()
	{
		int mailMessagesCount = 0;
		Iterator<IMailMessage> messages = this.getMailMessagesIterator();
		while (messages.hasNext())			
		{
			messages.next();
			mailMessagesCount++;
		}
		return mailMessagesCount;
	}

	public int getSize() throws MessagingException
	{
		int size=0;
		Iterator<IMailMessage> messagesIterator = this.getMailMessagesIterator();
		while(messagesIterator.hasNext())
		{
			IMailMessage message = messagesIterator.next();
			size+=message.getSize();
		}
		return size;
	}
}
