package net.sourceforge.fenixedu.applicationTier.Servico;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.UnitSiteLink;

public class DeleteUnitSiteLink extends Service {

    public void run(UnitSite site, UnitSiteLink link) {
        link.delete();
    }
    
}
