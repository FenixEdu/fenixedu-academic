package net.sourceforge.fenixedu.applicationTier.Servico.contacts;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.contacts.PartyContactBean;

public class EditPartyContact extends FenixService {
    public void run(PartyContactBean contactBean) {
	contactBean.edit();
    }
}
