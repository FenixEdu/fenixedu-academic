

package net.sourceforge.fenixedu.domain.cms.infrastructure;


import net.sourceforge.fenixedu.domain.cms.messaging.MailMessage;

public class MailQueue extends MailQueue_Base {

    public MailQueue() {
        super();
    }

    public void delete() {
        MailingListQueue.remove(this, this.getMailingList());
        for (MailMessage mailMessage : this.getMessages()) {
            MailingListQueueOutgoingMails.remove(this, mailMessage);
        }
		
        super.deleteDomainObject();
    }
}
