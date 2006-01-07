package net.sourceforge.fenixedu.domain.cms.infrastructure;

import net.sourceforge.fenixedu.domain.cms.messaging.MailingList;
import relations.MailingListAlias;
public class MailAddressAlias extends MailAddressAlias_Base {
    
    public MailAddressAlias() {
        super();
    }

	public void delete()
	{
		for (MailingList mailingList : this.getMailingLists())
		{
			MailingListAlias.remove(mailingList,this);
		}		
		
		super.deleteDomainObject();
	}
    
}
