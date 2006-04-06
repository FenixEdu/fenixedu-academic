package net.sourceforge.fenixedu.domain.cms.infrastructure;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.cms.messaging.MailingList;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class MailAddressAlias extends MailAddressAlias_Base {

    public MailAddressAlias() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
        for (MailingList mailingList : this.getMailingLists()) {
            MailingListAlias.remove(this, mailingList);
        }

		removeRootDomainObject();
        super.deleteDomainObject();
    }

    // read static methods

    public static MailAddressAlias readByAddress(String name) throws ExcepcaoPersistencia {
        for (final MailAddressAlias mailAddressAlias : RootDomainObject.getInstance()
                .getMailAddressAliass()) {
            
            if (mailAddressAlias.getAddress().equals(name)) {
                return mailAddressAlias;
            }
        }

        return null;
    }

}
