package net.sourceforge.fenixedu.presentationTier.Action.webSiteManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "webSiteManager", path = "/managePedagogicalCouncilAnnouncements", functionality = ListSitesAction.class)
@Forwards(@Forward(name = "viewAnnouncementsRedirect",
        path = "/webSiteManager/managePedagogicalCouncilAnnouncements.do?method=viewAnnouncements&tabularVersion=true"))
public class PedagogicalCouncilAnnouncementsDA extends UnitSiteAnnouncementManagement {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("siteActionName", "/managePedagogicalCouncilSite.do");
        request.setAttribute("siteContextParam", "oid");
        request.setAttribute("siteContextParamValue", getSite(request).getExternalId());
        request.setAttribute("announcementsActionName", "/managePedagogicalCouncilAnnouncements.do");
        request.setAttribute("unitId", getSite(request).getUnit().getExternalId());
        return super.execute(mapping, actionForm, request, response);
    }

    @Override
    protected String getContextInformation(ActionMapping mapping, HttpServletRequest request) {
        return "/managePedagogicalCouncilAnnouncements.do";
    }

}
