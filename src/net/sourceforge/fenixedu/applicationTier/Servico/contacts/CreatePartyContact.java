package net.sourceforge.fenixedu.applicationTier.Servico.contacts;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.contacts.PartyContactBean;

public class CreatePartyContact extends FenixService {
    public void run(PartyContactBean contactBean) {
	contactBean.createNewContact();
    }
}
