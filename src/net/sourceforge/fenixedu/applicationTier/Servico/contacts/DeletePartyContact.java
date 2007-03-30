package net.sourceforge.fenixedu.applicationTier.Servico.contacts;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.contacts.PartyContact;

public class DeletePartyContact extends Service {

    public void run(final PartyContact contact) {
	contact.delete();
    }
}
