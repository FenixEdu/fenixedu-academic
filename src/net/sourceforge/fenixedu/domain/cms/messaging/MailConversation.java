

package net.sourceforge.fenixedu.domain.cms.messaging;


import java.util.Comparator;
import java.util.Iterator;

import javax.mail.MessagingException;

import net.sourceforge.fenixedu.commons.OrderedIterator;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.cms.predicates.ContentAssignableClassPredicate;

import org.apache.commons.collections.iterators.FilterIterator;
import org.apache.commons.lang.StringUtils;

public class MailConversation extends MailConversation_Base
{

	private class LastMessagesFirstComparator implements Comparator
	{
		public int compare(Object arg0, Object arg1)
		{
			MailMessage message1 = (MailMessage) arg0;
			MailMessage message2 = (MailMessage) arg1;
			return -message1.getCreationDate().compareTo(message2.getCreationDate());
		}
	}

	public MailConversation()
	{
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	public boolean isAboutSubject(final String subject)
	{
		boolean result = false;
		if (subject != null && this.getSubject() != null)
		{
			String[] replyMarkers = this.getCms().getConfiguration().getConversationReplyMarkersToUse();
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
		int index = StringUtils.indexOfAny(subject.trim(), this.getCms().getConfiguration().getConversationReplyMarkers());
		result = (index == 0);

		return result;
	}

	@SuppressWarnings("unchecked")
	public Iterator<MailMessage> getMailMessagesIterator()
	{
		return new OrderedIterator<MailMessage>(new FilterIterator(this.getChildrenIterator(), new ContentAssignableClassPredicate(MailMessage.class)), new LastMessagesFirstComparator());
	}

	@SuppressWarnings("unchecked")
	public Iterator<MailingList> getMailingListsIterator()
	{
		return new FilterIterator(this.getParentsIterator(), new ContentAssignableClassPredicate(MailingList.class));
	}

	public int getMailingListsCount()
	{
		int listsCount = 0;
		Iterator<MailingList> lists = this.getMailingListsIterator();
		while (lists.hasNext())
		{
			lists.next();
			listsCount++;
		}
		return listsCount;
	}

	public MailMessage getLastReply()
	{
		Iterator<MailMessage> messageIterator = this.getMailMessagesIterator();
		MailMessage mailMessage = null;

		while (messageIterator.hasNext())
		{
			MailMessage currentMessage = messageIterator.next();
			if (mailMessage == null
					|| mailMessage.getCreationDate().before(currentMessage.getCreationDate()))
			{
				mailMessage = currentMessage;
			}
		}
		return mailMessage;
	}

	public MailMessage getFirstMessage()
	{
		Iterator<MailMessage> messageIterator = this.getMailMessagesIterator();
		MailMessage mailMessage = null;

		while (messageIterator.hasNext())
		{
			MailMessage currentMessage = messageIterator.next();
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
		Iterator<MailMessage> messages = this.getMailMessagesIterator();
		while (messages.hasNext())
		{
			messages.next();
			mailMessagesCount++;
		}
		return mailMessagesCount;
	}

	public int getSize() throws MessagingException
	{
		int size = 0;
		Iterator<MailMessage> messagesIterator = this.getMailMessagesIterator();
		while (messagesIterator.hasNext())
		{
			MailMessage message = messagesIterator.next();
			size += message.getSize();
		}
		return size;
	}

	@Override
	public void delete()
	{
		Iterator<MailMessage> iterator = this.getMailMessagesIterator();
		while (iterator.hasNext())
		{
			MailMessage message = iterator.next();
			if (message.getMailConversationsCount() == 0) message.delete();
		}

		super.delete();
	}
}
