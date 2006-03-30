

package net.sourceforge.fenixedu.domain.cms.infrastructure;


import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.cms.messaging.MailMessage;

public class MailQueue extends MailQueue_Base {

    public MailQueue() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
        MailingListQueue.remove(this, this.getMailingList());
        for (MailMessage mailMessage : this.getMessages()) {
            MailingListQueueOutgoingMails.remove(this, mailMessage);
        }
		
        removeRootDomainObject();
        super.deleteDomainObject();
    }
}
