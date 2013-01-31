package net.sourceforge.fenixedu.applicationTier.Servico;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.UnitSiteLink;

public class DeleteUnitSiteLink extends FenixService {

	public void run(UnitSite site, UnitSiteLink link) {
		link.delete();
	}

}
