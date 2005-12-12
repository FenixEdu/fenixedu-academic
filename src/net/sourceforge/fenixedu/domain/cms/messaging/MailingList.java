

package net.sourceforge.fenixedu.domain.cms.messaging;


import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;

import javax.mail.MessagingException;

import net.sourceforge.fenixedu.commons.OrderedIterator;
import net.sourceforge.fenixedu.domain.cms.Content;
import net.sourceforge.fenixedu.domain.cms.predicates.ContentAssignableClassPredicate;
import net.sourceforge.fenixedu.domain.cms.predicates.ContentPredicate;

import org.apache.commons.collections.iterators.FilterIterator;

import relations.ContentHierarchy;

public class MailingList extends MailingList_Base
{
	public MailingList()
	{
		super();
	}

	
	private class LastConversationsFirstComparator implements Comparator
	{
		public int compare(Object arg0, Object arg1)
		{
			IMailConversation conversation1 = (IMailConversation) arg0;
			IMailConversation conversation2 = (IMailConversation) arg1;
			return - conversation1.getCreationDate().compareTo(conversation2.getCreationDate());
		}
	}
	
	private class LastMessagesFirstComparator implements Comparator
	{
		public int compare(Object arg0, Object arg1)
		{
			IMailMessage message1 = (IMailMessage) arg0;
			IMailMessage message2 = (IMailMessage) arg1;
			return - message1.getCreationDate().compareTo(message2.getCreationDate());
		}
	}
	
	private class ConversationIsAboutSubject extends ContentPredicate
	{
		private String subject;

		public ConversationIsAboutSubject(String subject)
		{
			this.subject = subject;
		}

		public boolean evaluate(Content mailConversation)
		{
			boolean result = false;
			IMailConversation conversation = (IMailConversation) mailConversation;
			result = conversation.isAboutSubject(this.subject);
			return result;
		}

	}

	public Iterator<IMailConversation> getMailConversationsIterator()
	{
		return new OrderedIterator(new FilterIterator(this.getChildrenIterator(), new ContentAssignableClassPredicate(IMailConversation.class)),new LastConversationsFirstComparator());
	}

	public Iterator<IMailConversation> getConversationsOnSubjectIterator(String subject)
	{
		return new OrderedIterator(new FilterIterator(this.getMailConversationsIterator(), new ConversationIsAboutSubject(subject)),new LastConversationsFirstComparator());
	}
	
	public Iterator<IMailMessage> getMailMessagesIterator()
	{
		return new OrderedIterator(new FilterIterator(this.getChildrenIterator(), new ContentAssignableClassPredicate(IMailMessage.class)),new LastMessagesFirstComparator());
	}

	public IMailConversation getMostRecentMailConversationOnSubject(String subject)
	{
		Iterator<IMailConversation> conversationsIterator = this.getConversationsOnSubjectIterator(subject);
		IMailConversation result = null;
		while (conversationsIterator.hasNext())
		{
			IMailConversation currentConversation = conversationsIterator.next();
			if (result == null || result.getCreationDate().before(currentConversation.getCreationDate()))
			{
				result = currentConversation;
			}
		}

		return result;
	}

	public int getMailConversationsCount()
	{
		int count =0;
		Iterator<IMailConversation> mailConversationsIterator = this.getMailConversationsIterator();
		while(mailConversationsIterator.hasNext())
		{
			count++;
			mailConversationsIterator.next();
		}
		return count;
	}

	public int getMailMessagesCount()
	{
		int count =0;
		Iterator<IMailMessage> mailMessagesIterator = this.getMailMessagesIterator();
		while(mailMessagesIterator.hasNext())
		{
			count++;
			mailMessagesIterator.next();
		}
		return count;		
	}

	public int getSize() throws MessagingException
	{
		int size=0;
		Iterator<IMailConversation> mailConversationsIterator = this.getMailConversationsIterator();
		while(mailConversationsIterator.hasNext())
		{
			IMailConversation conversation = mailConversationsIterator.next();
			size+=conversation.getSize();
		}
		return size;
	}
	
	@Override
	public void delete()
	{
		Iterator<IMailConversation> iterator = this.getMailConversationsIterator();
		while (iterator.hasNext())
		{
			IMailConversation conversation = iterator.next();
			if(conversation.getMailingListsCount()==0)
				conversation.delete();
		}
		super.delete();
	}
}