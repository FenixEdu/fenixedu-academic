package net.sourceforge.fenixedu.applicationTier.Servico.contacts;

import net.sourceforge.fenixedu.domain.contacts.PartyContact;
import pt.ist.fenixWebFramework.services.Service;

public class DeletePartyContact {

    @Service
    public static void run(PartyContact contact) {
	contact.getParty().logDeleteContact(contact);
	contact.delete();
    }
}
