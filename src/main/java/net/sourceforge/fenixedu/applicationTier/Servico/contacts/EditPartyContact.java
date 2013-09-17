package net.sourceforge.fenixedu.applicationTier.Servico.contacts;

import net.sourceforge.fenixedu.dataTransferObject.contacts.PartyContactBean;
import net.sourceforge.fenixedu.domain.contacts.PartyContact;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddress;
import net.sourceforge.fenixedu.domain.contacts.WebAddress;
import pt.ist.fenixframework.Atomic;

public class EditPartyContact {

    @Atomic
    public static Boolean run(PartyContactBean contactBean, final boolean toBeValidated) {
        // return type true if value changed
        Boolean wasChanged = contactBean.edit();
        if (wasChanged) {
            final PartyContact contact = contactBean.getContact();
            if (toBeValidated) {
                contact.triggerValidationProcessIfNeeded();
            } else {
                if (contact instanceof PhysicalAddress || contact instanceof WebAddress) {
                    contact.setValid();
                }
            }
        }
        return wasChanged;
    }
}
