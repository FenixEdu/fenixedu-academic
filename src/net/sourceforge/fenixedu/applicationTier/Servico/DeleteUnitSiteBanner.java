package net.sourceforge.fenixedu.applicationTier.Servico;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.UnitSiteBanner;

public class DeleteUnitSiteBanner extends Service {

    public void run(UnitSite site, UnitSiteBanner banner) {
        banner.delete();
    }

}
