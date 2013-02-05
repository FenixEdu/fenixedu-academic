package net.sourceforge.fenixedu.applicationTier.Servico.research;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembers;

public class DeletePersistentGroup extends FenixService {

    public void run(PersistentGroupMembers group) {
        group.delete();
    }
}
