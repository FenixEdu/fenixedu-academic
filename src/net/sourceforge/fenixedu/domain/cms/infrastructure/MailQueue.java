

package net.sourceforge.fenixedu.domain.cms.infrastructure;


import relations.MailingListQueue;
import relations.MailingListQueueOutgoingMails;
import net.sourceforge.fenixedu.domain.cms.messaging.IMailMessage;
import net.sourceforge.fenixedu.domain.cms.messaging.IMailingList;
import net.sourceforge.fenixedu.stm.VBox;
import net.sourceforge.fenixedu.stm.RelationList;
import net.sourceforge.fenixedu.stm.OJBFunctionalSetWrapper;

public class MailQueue extends MailQueue_Base
{

	public MailQueue()
	{
		super();
	}

	public void delete()
	{
		MailingListQueue.remove(this.getMailingList(), this);
		for (IMailMessage mailMessage : this.getMessages())
		{
			MailingListQueueOutgoingMails.remove(this, mailMessage);
		}
		
		super.deleteDomainObject();
	}

}
