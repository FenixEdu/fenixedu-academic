package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.ScientificCouncilSite;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.presentationTier.Action.webSiteManager.ManageUnitSiteManagers;

public class ManageSiteManagersDA extends ManageUnitSiteManagers {

	@Override
    protected UnitSite getSite(HttpServletRequest request) {
		Integer siteId = getId(request.getParameter("siteID"));
		
		if (siteId != null) {
			return (UnitSite) RootDomainObject.getInstance().readSiteByOID(siteId);
		}
		else {
			return ScientificCouncilSite.getSite();
		}
    }

    @Override
	protected String getRemoveServiceName() {
		return "RemoveScientificSiteManager";
	}

    @Override
	protected String getAddServiceName() {
		return "AddScientificSiteManager";
	}
    
}
