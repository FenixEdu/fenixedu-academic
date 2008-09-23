package net.sourceforge.fenixedu.applicationTier.Servico.contacts;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.contacts.PartyContact;

public class DeletePartyContact extends FenixService {

    public void run(final PartyContact contact) {
	contact.delete();
    }
}
