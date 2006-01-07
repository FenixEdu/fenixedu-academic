

package net.sourceforge.fenixedu.domain.cms.infrastructure;


import net.sourceforge.fenixedu.domain.cms.messaging.MailMessage;
import relations.MailingListQueue;
import relations.MailingListQueueOutgoingMails;

public class MailQueue extends MailQueue_Base
{

	public MailQueue()
	{
		super();
	}

	public void delete()
	{
		MailingListQueue.remove(this.getMailingList(), this);
		for (MailMessage mailMessage : this.getMessages())
		{
			MailingListQueueOutgoingMails.remove(this, mailMessage);
		}
		
		super.deleteDomainObject();
	}

}
