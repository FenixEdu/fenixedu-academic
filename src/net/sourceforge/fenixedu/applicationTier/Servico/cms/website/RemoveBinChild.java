package net.sourceforge.fenixedu.applicationTier.Servico.cms.website;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.cms.Bin;
import net.sourceforge.fenixedu.domain.cms.Content;

public class RemoveBinChild extends Service {

    public void run(Bin parent, Content child) {
        parent.removeChildren(child);
    }
}
