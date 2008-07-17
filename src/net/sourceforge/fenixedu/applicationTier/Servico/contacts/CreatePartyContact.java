package net.sourceforge.fenixedu.applicationTier.Servico.contacts;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.contacts.PartyContactBean;

public class CreatePartyContact extends Service {
    public void run(PartyContactBean contactBean) {
	contactBean.createNewContact();
    }
}
