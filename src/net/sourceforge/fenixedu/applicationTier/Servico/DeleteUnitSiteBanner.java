package net.sourceforge.fenixedu.applicationTier.Servico;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.UnitSiteBanner;

public class DeleteUnitSiteBanner extends FenixService {

    public void run(final UnitSite site, final UnitSiteBanner banner) {
	banner.delete();
    }

}
