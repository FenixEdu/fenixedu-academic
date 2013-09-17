package net.sourceforge.fenixedu.presentationTier.Action.publico;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.UnitSite;
import pt.ist.fenixframework.FenixFramework;

public class ShowResearchResultInUnitSite extends ShowResearchResult {

    @Override
    protected void putSiteOnRequest(HttpServletRequest request) {
        String siteID = request.getParameter("siteID");
        if (siteID != null) {
            UnitSite site = (UnitSite) FenixFramework.getDomainObject(siteID);
            request.setAttribute("site", site);
            request.setAttribute("unit", site.getUnit());
        }
    }

}
