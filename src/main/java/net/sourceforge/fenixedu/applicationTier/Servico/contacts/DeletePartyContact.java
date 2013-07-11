package net.sourceforge.fenixedu.applicationTier.Servico.contacts;

import net.sourceforge.fenixedu.domain.contacts.PartyContact;
import pt.ist.fenixframework.Atomic;

public class DeletePartyContact {

    @Atomic
    public static void run(PartyContact contact) {
        contact.getParty().logDeleteContact(contact);
        contact.delete();
    }
}
