package net.sourceforge.fenixedu.domain.cms.infrastructure;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.cms.messaging.MailingList;

public class MailAddressAlias extends MailAddressAlias_Base {
    
    public MailAddressAlias() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

	public void delete()
	{
		for (MailingList mailingList : this.getMailingLists())
		{
			MailingListAlias.remove(this, mailingList);
		}		
		
		super.deleteDomainObject();
	}
    
}
