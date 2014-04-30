package net.sourceforge.fenixedu.presentationTier.Action.webSiteManager;

import javax.servlet.http.HttpServletRequest;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "webSiteManager", path = "/managePedagogicalCouncilSite", functionality = ListSitesAction.class)
public class PedagogicalCouncilSiteManagementDA extends CustomUnitSiteManagementDA {

    @Override
    protected void setContext(HttpServletRequest request) {
        request.setAttribute("siteActionName", "/managePedagogicalCouncilSite.do");
        request.setAttribute("siteContextParam", "oid");
        request.setAttribute("siteContextParamValue", getSite(request).getExternalId());
        request.setAttribute("announcementsActionName", "/managePedagogicalCouncilAnnouncements.do");
        request.setAttribute("unitId", getSite(request).getUnit().getExternalId());
    }

    @Override
    protected String getAuthorNameForFile(HttpServletRequest request) {
        return getUserView(request).getPerson().getName();
    }

}
